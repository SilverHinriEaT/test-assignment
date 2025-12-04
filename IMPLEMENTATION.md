# Реалізація NumberList

## Параметри студента
- Номер у списку групи: **6**
- C3 = 6 % 3 = **0** → Лінійний двонаправлений список
- C5 = 6 % 5 = **1** → Трійкова система числення (основа 3)
- C7 = 6 % 7 = **6** → Алгебраїчне та логічне OR двох чисел
- Додаткова система: (1+1) % 5 = **2** → Вісімкова система (основа 8)
- Номер залікової книжки: **6**

## Структура реалізації

### Клас NumberListImpl
Реалізує інтерфейс `NumberList` з використанням:

1. **Внутрішня структура**: Двонаправлений зв'язаний список (doubly linked list) вузлів (Node)
2. **Представлення числа**: Кожен вузол містить одну цифру в трійковій системі (0, 1, або 2)
3. **Порядок зберігання**: Перша цифра у head, остання у tail

### Основні методи

#### Конструктори
- `NumberListImpl()` - порожній список
- `NumberListImpl(String value)` - список з десяткового числа у строковому форматі
- `NumberListImpl(File file)` - список з файлу, що містить десяткове число

#### Основні операції зі списком (List interface)
- `add()`, `remove()`, `get()`, `set()` та інші стандартні методи List
- `size()`, `isEmpty()`, `clear()`
- `contains()`, `indexOf()`, `lastIndexOf()`
- `iterator()`, `listIterator()`

#### Спеціальні операції
- `swap(int index1, int index2)` - обмін двох елементів
- `sortAscending()` - сортування за зростанням
- `sortDescending()` - сортування за спаданням
- `shiftLeft()` - циклічний зсув вліво
- `shiftRight()` - циклічний зсув вправо

#### Операції з числами
- `toDecimalString()` - повертає строкове представлення в десятковій системі
- `toString()` - повертає представлення в трійковій системі
- `changeScale()` - конвертує число з трійкової в вісімкову систему
- `additionalOperation(NumberList arg)` - виконує битовий OR двох чисел

### Приклади використання

```java
// Створення списку з десяткового числа
NumberListImpl list = new NumberListImpl("15");
// У трійковій системі: 15 (дес.) = 120 (трійк.)

// Конвертація до вісімкової системи
NumberListImpl octal = list.changeScale();
// 15 (дес.) = 120 (трійк.) = 17 (вісімк.)

// Операція OR
NumberListImpl list1 = new NumberListImpl("15");
NumberListImpl list2 = new NumberListImpl("2");
NumberListImpl result = list1.additionalOperation(list2);
// 15 | 2 = 15 (у десятковій системі)

// Отримання десяткового представлення
String decimal = list.toDecimalString(); // "15"
```

## Побудова проекту

### Використання Maven
```bash
mvn clean compile
mvn test
```

### Використання Makefile
```bash
make deps
make compile
make test
```

## Тестування

Проект містить набір JUnit тестів для перевірки всіх функцій:
- `NumberListTest` - базові операції зі списками
- `StringListTest` - конструктори з рядків
- `FileListTest` - конструктори з файлів
- `ScaleOfNotationChangeTest` - конвертація систем числення
- `AdditionalOperationTest` - операція OR

Усі тести використовують `assumeTrue()` для перевірки номера залікової книжки, тому активні лише відповідні тести для цього студента.
