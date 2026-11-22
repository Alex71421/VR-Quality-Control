# Взаимное рецензирование кода

## Рецензия на код Толмачева Ивана Андреевича

**Рецензируемый** – Толмачев Иван Андреевич, ИКБО-31-24.

**Рецензент** – Ушверидзе Александр Александрович, ИКБО-31-24.

**Проверенная версия** – [репозиторий warehouse-ar-picking](https://github.com/phio69/warehouse-ar-picking), [коммит d4a9abf](https://github.com/phio69/warehouse-ar-picking/commit/d4a9abfa59f96a2c6972b7fa368cb70c1a4db5db).

### Что проверил

Сверил диаграмму классов с Java-кодом, проверил читаемость кода, наследование `Picker` от `Employee`, две реализации интерфейса `MarkerScanner` и использование коллекций `List` и `Map`. Собрал и запустил обе версии по командам из README на Java 11.0.17. Для обеих версий использовал CSV из `src/java/data`. Вывод совпал после исключения заголовка и строк со временем.

### Замечания

1. В `Main.java` задание выполняется, даже если комплектовщик с указанным `picker_id` отсутствует в списке сотрудников. Проверил на копии CSV с `picker_id = 999`. Предлагаю при `picker == null` выводить сообщение и пропускать задание. [Main.java, строки 35–45](https://github.com/phio69/warehouse-ar-picking/blob/d4a9abfa59f96a2c6972b7fa368cb70c1a4db5db/src/java/Main.java#L35-L45)
2. В `PickTask.java` поиск дополнительных ячеек запускается даже тогда, когда из основной ячейки уже взяли всё нужное количество. Предлагаю выполнять цикл добора только при `taken < item.getQuantity()`. [PickTask.java, строки 55–63](https://github.com/phio69/warehouse-ar-picking/blob/d4a9abfa59f96a2c6972b7fa368cb70c1a4db5db/src/java/PickTask.java#L55-L63)

## Замечания к моему коду и результаты исправлений

**Рецензент** – Толмачев Иван Андреевич, ИКБО-31-24. [Репозиторий рецензента](https://github.com/phio69/warehouse-ar-picking)

**Проверенная версия** – Java-реализация, UML-диаграмма классов и процедурная версия на Python. [Коммит 78321b2](https://github.com/Alex71421/VR-Quality-Control/commit/78321b2e9527bcb37ac6f07d9800f029e2d72d67)

### №1. Пустой акт закрывается с решением «годен»

> - **Где:** `ControlAct.java`, метод `decide()`, строки 58–66 –
>   https://github.com/Alex71421/VR-Quality-Control/blob/78321b2e9527bcb37ac6f07d9800f029e2d72d67/src/java/src/main/java/vrqualitycontrol/ControlAct.java#L58-L66
> - **Проблема.** Решение по умолчанию равно «годен». Если в акт не внесено ни одного результата, `closeAct()` всё равно вернёт «годен». Проверил: создал акт без результатов и закрыл его – получил «годен».
> - **Предложение.** В начале `decide()` добавить проверку:
>   ```java
>   if (results.isEmpty()) {
>       return null;
>   }
>   ```
>   Тогда акт без проверок останется без решения.

**Исправлено.** В начале `closeAct()` и `close_act()` добавил проверку на отсутствие результатов. Пустой акт остаётся без решения. [Коммит 50b5ed1](https://github.com/Alex71421/VR-Quality-Control/commit/50b5ed1ff437d9ebc48c01702166d0529932197d)

### №2. Номер нового акта может совпасть с существующим

> - **Где:** `QualityControl.java`, строка 34: `int actId = acts.size() + 1;` –
>   https://github.com/Alex71421/VR-Quality-Control/blob/78321b2e9527bcb37ac6f07d9800f029e2d72d67/src/java/src/main/java/vrqualitycontrol/QualityControl.java#L34
> - **Проблема.** Номер считается по количеству актов. Если в данных нумерация с пропуском, номера повторятся. Проверил: оставил в `data.json` акты 1, 2 и 4. Новый акт тоже получил номер 4.
> - **Предложение.** Брать наибольший номер и прибавлять 1, как уже сделано для сотрудников в `getOrAddEmployee()`:
>   ```java
>   int actId = 1;
>   for (ControlAct act : acts) {
>       actId = Math.max(actId, act.getActId() + 1);
>   }
>   ```

**Исправлено.** В Java и Python номер нового акта рассчитывается как максимальный существующий номер плюс один. Проверил, что после актов 1, 2 и 4 новый акт получает номер 5. [Коммит ea9d4f9](https://github.com/Alex71421/VR-Quality-Control/commit/ea9d4f9212512e9a7ddf63da6b39a37d3f7a877e)

### №3. `Report` можно реализовать один раз в `TextReport`

> - **Где:**
>   - `TextReport.java`, строка 4;
>   - `InspectionProtocol.java`, строка 3;
>   - `DefectReport.java`, строка 5 –
>     https://github.com/Alex71421/VR-Quality-Control/blob/78321b2e9527bcb37ac6f07d9800f029e2d72d67/src/java/src/main/java/vrqualitycontrol/TextReport.java#L4
> - **Проблема.** Метод `print()` написан в `TextReport`, но `implements Report` повторяется в каждом подклассе. Новый отчёт легко объявить без `implements Report`.
> - **Предложение.** Написать `public abstract class TextReport implements Report`, а в подклассах оставить `extends TextReport`. На диаграмме перенести стрелку реализации `Report` на `TextReport`.

**Не исправлено.** Сохранил две явные реализации `Report` в `InspectionProtocol` и `DefectReport`, как показано на диаграмме классов. Общий метод `print()` уже вынесен в `TextReport`, а `Main` использует оба отчёта через тип `Report`.

### №4. Кодировка вывода не задана

> - **Где:** `Main.java`, метод `main()` –
>   https://github.com/Alex71421/VR-Quality-Control/blob/78321b2e9527bcb37ac6f07d9800f029e2d72d67/src/java/src/main/java/vrqualitycontrol/Main.java#L7
> - **Проблема.** Если кодировка консоли не UTF-8, русский текст выводится как «????». Проверил при запуске с `LANG=C`.
> - **Предложение.** Первой строкой в `main()` задать UTF-8. Понадобятся импорты `java.io.PrintStream`, `java.io.FileOutputStream`, `java.io.FileDescriptor` и `java.nio.charset.StandardCharsets`:
>   ```java
>   System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out), true, StandardCharsets.UTF_8));
>   ```

**Исправлено.** В начале `main()` для текущего потока `System.out` задана кодировка UTF-8. Проверил вывод на Java 11 с принудительной кодировкой JVM `US-ASCII`. [Коммит d12a450](https://github.com/Alex71421/VR-Quality-Control/commit/d12a450065941e4265b5e444bfd9328b9790cb45)

На штатном файле `data.json` после исправлений Java и Python выдают прежний протокол контроля и ту же статистику брака.
