import json
import sys


def get_or_add_employee(data, full_name):
    """Находит сотрудника по имени или добавляет его и возвращает номер."""
    employees = data["employees"]
    for employee_id, name in employees.items():
        if name == full_name:
            return employee_id

    employee_id = max(employees, default=0) + 1
    employees[employee_id] = full_name
    return employee_id


def load_data(path):
    """Читает JSON и подготавливает словари шлемов и сотрудников."""
    with open(path, encoding="utf-8") as f:
        data = json.load(f)

    helmets = {}
    for h in data["helmets"]:
        helmets[h["serial"]] = h
    data["helmets"] = helmets

    employees = {}
    for employee in data["employees"]:
        employees[employee["employee_id"]] = employee["full_name"]
    data["employees"] = employees

    return data


def register_helmet(data, serial, model_id, batch):
    """Операция для добавления шлема по серийнику"""
    if serial in data["helmets"]:
        print("Шлем", serial, "уже зарегистрирован")
        return False
    data["helmets"][serial] = {"serial": serial, "model_id": model_id, "batch": batch}
    return True


def create_act(data, serial, date, inspector):
    """Создает новый акт контроля для шлема и возвращает его номер"""
    max_id = 0
    for act in data["acts"]:
        max_id = max(max_id, act["act_id"])
    act_id = max_id + 1
    inspector_id = get_or_add_employee(data, inspector)
    data["acts"].append({"act_id": act_id, "serial": serial, "date": date, "inspector_id": inspector_id, "decision": None})
    return act_id


def add_result(data, act_id, item_id, passed, defect=""):
    """Операция ввода результата одного пункта проверки"""
    for r in data["results"]:
        if r["act_id"] == act_id and r["item_id"] == item_id:
            print("Пункт", item_id, "уже записан в акте", act_id)
            return False
    data["results"].append({"act_id": act_id, "item_id": item_id, "passed": passed, "defect": defect})
    return True


def decide(results):
    """Алгоритм 1. Решение по акту"""
    decision = "годен"
    for r in results:
        if not r["passed"]:
            decision = "брак"
    return decision


def close_act(data, act_id):
    "Операця по выносу решения по акту и сохранения его"
    act_results = [r for r in data["results"] if r["act_id"] == act_id]
    if not act_results:
        return None

    decision = decide(act_results)
    for a in data["acts"]:
        if a["act_id"] == act_id:
            a["decision"] = decision
    return decision


def defect_statistics(models, acts, helmets, date_from, date_to):
    """Алгоритм 2. Брак по моделям за период"""
    stats = []
    for m in models:
        defects = 0
        for a in acts:
            act_model = helmets[a["serial"]]["model_id"]
            if act_model == m["model_id"] and date_from <= a["date"] <= date_to and a["decision"] == "брак":
                defects += 1
        stats.append((m["name"], defects))
    return stats


def print_protocol(data, act_id):
    """Выводит протокол контроля по акту"""
    act = None
    for a in data["acts"]:
        if a["act_id"] == act_id:
            act = a
    items = {}
    for i in data["check_items"]:
        items[i["item_id"]] = i["name"]
    print("Протокол контроля, акт", act["act_id"])
    inspector = data["employees"][act["inspector_id"]]
    print("Шлем:", act["serial"], " дата: ", act["date"], " контролер:", inspector)
    for r in data["results"]:
        if r["act_id"] == act_id:
            mark = "пройден"
            if not r["passed"]:
                mark = "Не пройден: " + r["defect"]
            print(items[r["item_id"]], "-", mark)
    print("Решение:", act["decision"])


def main():
    # Для запуска нужно 2 аргумента
    if len(sys.argv) < 2:
        print("Запуск: python main.py data.json")
        return
    data = load_data(sys.argv[1])

    # Новый шлем проходит контроль
    register_helmet(data, "VR1-0003", 1, "P-12")
    act_id = create_act(data, "VR1-0003", "2026-10-25", "Петров И. С.")
    add_result(data, act_id, 1, True)
    add_result(data, act_id, 2, True)
    add_result(data, act_id, 3, True)
    add_result(data, act_id, 4, False, "Нечёткость изображения по краям")
    close_act(data, act_id)
    print_protocol(data, act_id)

    print()
    print("Брак по моделям за 2026-10-20 - 2026-10-31:")
    for name, count in defect_statistics(data["models"], data["acts"], data["helmets"], "2026-10-20", "2026-10-31"):
        print(" ", name, "-", count)


if __name__ == "__main__":
    main()