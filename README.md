# Własny język programowania -  Kompilatory

## Features

### Object-Oriented-Programming

- Classes and Methods
- Inheritance and Interfaces
- Encapsulation (Access Modifiers)
- Enums

### Built-In Data Types

- Primitive Types: ```byte```, ```int```, ```float```,  ```double```, ```char```, ```string```, ```bool```, ```long```
- Arrays: support for one-dimensional and multi-dimensional arrays

### Control Flow

- Conditionals: ```if```, ```elsif```, ```else```, ```endif```
- Loops: ```for```, ```while```, ```endfor```, ```endwhile```

### Memory Management

- Automatic Garbage Collection:
- Automates memory management by automatically reclaiming memory that is no longer in use, reducing the need for manual memory allocation and deallocation.

### Error Handling

- Exceptions: ```try```, ```catch```, ```throw```

### Operators

- a complete set of operators for performing various operations on variables and values.

### Logical Operators

- ```and```, ```or``` instead of &&, || respectively

### Comments

- The hash symbol "#" is used to denote comments, similar to Python.
- Comments allow to include text within code that is ignored by the compiler.

## Syntax Rules

### Variables and Data Types

Variables are declared with a specified data type, followed by the variable name and optional initialization.

- Basic Variable Declaration:

```
int x = 10;
flaot pi = 3.14;
bool isValid = true;
string name = "John";
```

- Array Declaration:

```
int[] numbers = new int[5];
int[][] matrix = new int[3][3];
```

### Functions (Methods)

Methods are defined with an access modifier, followed by the ```function``` keyword, the method name, parameters, return type, function body.

- Function Declaration:

```
ACCESS_MOD function FUNC_NAME(TYPE param1, ...) : RETURN_TYPE
 // function body
endfunction
```

- Example:

```
public function add(int a, int b) : int
return a + b;
endfunction
```

- ```Void``` return type if no value is returned by the method

### Classes

Classes are defined using the access modifier, ```class``` keyword, followed by the lass name. Methods and variables must be defined inside the class. Classes can extend one other class and implement multiple interfaces.

- Class Declaration:

```
ACCESS_MOD class CLASS_NAME extends ... implements ...
 // fields
 // methods
endclass
```

- Example:

```
public class Person implements Entity
 string name;
 int age;

 constructor Person(string name, int age)
  this.name = name;
  this.age = age;
 endconstructor

 public function get() : void
  print("Hello, my name is " + name);
 endfunction
endclass
```

### Conditionals

The conditional statement uses the ```if```, ```elsif```, ```else``` and ```endif``` keywords to control the flow of the program based on a condition.

- ```if```, ```elsif```, ```else```:

```
if (x > 0) then
 print("Positive);
elsif (x < 0) then
 print("Negative");
else
 print("Zero");
endif
```

### Loops

There are two primary loop structures: **for** and **while**. Both require a condition or iteration step and the body of the loop.

- ```for``` Loop:

```
for (int i = 0; i < 10; i++) then
 print(i);
endfor
```

- ```while``` Loop:

```
while (x < 10) then
 x++;
endwhile
```

### Logical Operators

The language supports logical operators for combining boolean expressions: ```and```, ```or```

- Example:

```
if (x > 0 and y > 0) then
 print("Both are positive")
endif
```

### Error handling

The language supports basic error handling operations using ```try```, ```catch```, ```throw```.

- ```Try```-```Catch```:

```
try
 int result = devide(a, b);
catch (DevideByZeroException e)
 print("Division by zero!");
endtry
```

- Throwing Exceptions

```
public function devide(int a, int b) : float
 if (b == 0) then
  throw new DivisionByZeroException("Cannot devide by zero!");
 endif;
 return a / b;
endfunction
```

### Access Modifiers and Keyword Extensions

Access modifiers control the visibility of classess, methods and variables. The following access modifiers are supported:

- ```public```: Accessible from anywhere.
- ```protected```: Accessible within the class and its subclasses.
- ```private```: Accessible only within the class.

The ```const``` keyword defines variables whose values cannot be changed after initialization. It ensures immatability for the declared variable.

- Basic usage:

```
const int MAX_VALUE = 100;
const string APP_NAME = "MyApp";
```

### Arithmetic Operators

Arithmetic operators are used to perform common mathematical operations:

- ```+``` (Addition): Adds values on either side of the operator

- ```-``` (Subtraction): Subtracts right-hand operand from left-hand operand

- ```*``` (Multiplication): Multiplies values on either side of the operator

- ```/``` (Division): Divides left-hand operand by right-hand operand

- ```%``` (Modulus): Returns the remainder of a division operation

- ```++``` (Increment): Increases the value by 1

- ```--``` (Decrement): Decreases the value by 1

### Assignment Operators

Assignment operators are used to assign values to variables:

- ```=``` (Simple Assignment): Assigns value of right operand to left operand

- ```+=``` (Add AND Assignment): Adds right operand to left operand and assigns result to left operand

- ```-=``` (Subtract AND Assignment): Subtracts right operand from left operand and assigns result to left operand

- ```*=``` (Multiply AND Assignment): Multiplies left operand by right operand and assigns result to left operand

- ```/=``` (Divide AND Assignment): Divides left operand by right operand and assigns result to left operand

- ```%=``` (Modulus AND Assignment): Takes modulus using two operands and assigns result to left operand

### Comparison Operators

Comparison operators are used to compare two values:

- ```==``` (Equal to): Checks if the values of two operands are equal

- ```!=``` (Not equal to): Checks if the values of two operands are not equal

- ```>``` (Greater than): Checks if the value of left operand is greater than the value of right operand

- ```<``` (Less than): Checks if the value of left operand is less than the value of right operand

- ```>=``` (Greater than or equal to): Checks if the value of left operand is greater than or equal to the value of right operand

- ```<=``` (Less than or equal to): Checks if the value of left operand is less than or equal to the value of right operand
endif

### Bitwise Operators

Bitwise operators perform operations on binary representations of numbers:

- ```&``` (Bitwise AND): Performs a Boolean AND operation on each bit of two integer expressions

- ```|``` (Bitwise OR): Performs a Boolean OR operation on each bit of two integer expressions

- ```^``` (Bitwise XOR): Performs a Boolean exclusive OR operation on each bit of two integer expressions

- ```~``` (Bitwise Complement): Inverts all the bits of an integer expression

- ```<<``` (Left Shift): Shifts the bits of the left operand to the left by the number of positions specified by the right operand

- ```>>``` (Right Shift): Shifts the bits of the left operand to the right by the number of positions specified by the right operand

### Ternary Operator

The ternary operator is a shorthand for an if-else statement:

- ```? :``` (Conditional): Takes three operands and returns one of two values depending on the evaluation of a Boolean expression

### String Concatenation

The ```+``` operator is overloaded to perform string concatenation when at least one operand is a string.

### Method overriding

The ```override``` keyword explicitly indicates that a method in a derived class is intended to override a method in its base class. This helps catch errors when the method signature doesn't match as expected.

```
public class Animal
  public function makeSound() : void
    print("Generic animal sound");
  endfunction
endclass

public class Dog extends Animal
  override public function makeSound() : void
    print("Woof!");
  endfunction
endclass
```

### Static Members

The ```static``` keyword is used to define class-level members that are shared across all instances of the class. Static members belong to the class itself rather than to any specific instance.

- Static Fields:

```
public class Counter
  static int count = 0;

  public function increment() : void
    Counter.count++;
  endfunction

  public static function getCount() : int
    return Counter.count;
  endfunction
endclass
```

- Static Methods:

```
public class MathUtils
  public static function square(int n) : int
    return n * n;
  endfunction

  public static function min(int a, int b) : int
    return a < b ? a : b;
  endfunction
endclass
```

### Modules can be imported using the ```import``` keyword

- ```import``` syntax:

```
import com.example.MyClass;
```

### Enumerations (```enum```)

Enums provides type-safe constants and can include methods and fields.

- Example:

```
enum Direction
 NORTH, EAST, WEST, SOUTH

 public function isVertical() : bool
  return this == NORTH or this == SOUTH;
 endfunction
endenum
```
