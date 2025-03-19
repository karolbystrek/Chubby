# Własny język programowania -  Kompilatory

## Features

### Object-Oriented-Programming

- Classes and Methods
- Inheritance and Interfaces
- Encapsulation (Access Modifiers)

### Built-In Data Types

- Primitive Types: int, float, double, string, bool, long, uint
- Arrays: support for one-dimensional and multi-dimensional arrays

### Control Flow

- Conditionals: if, elsif, else, endif
- Loops: for, while, endfor, endwhile

### Error Handling

- Exceptions: try, catch, throw

### Logical Operators

- and ($$), or (||)

## Syntax Rules

### Variables and Data Types

Variables are declared with a specified data type, followed by the variable name and optional initialization.

- Basic Variable Declaration:

```docker
int x = 10;
flaot pi = 3.14;
bool isValid = true;
stirng name = "John";
```

- Array Declaration

```docker
int[] numbers = new int[5];
int[][] matrix = new int[3][3];
```

### Functions (Methods)

Methods are defined with an access modifier, followed by the **function** keyword, the method name, parameters, return type, function body.

- Function Declaration

```docker
ACCESS_MOD function FUNC_NAME(TYPE param1, ...) : RETURN_TYPE
	// function body
endfunction
```

- Example

```docker
public function add(int a, int b) : int
	return a + b;
endfunction
```

- **Void** return type if no value is returned by the method

### Classes

Classes are defined using the access modifier, **class** keyword, followed by the lass name. Methods and variables must be defined inside the class. Classes can extend one other class and implement multiple interfaces.

- Class Declaration

```docker
ACCESS_MOD class CLASS_NAME extends ... implements ...
	// fields
	// methods
endclass
```

- Example

```docker
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

The conditional statement uses the **if, elsif,** **else** and **endif** keywords to control the flow of the program based on a condition.

- If-Else

```docker
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

- For Loop

```docker
for (int i = 0; i < 10; i++) then
	print(i);
endfor
```

- While Loop

```docker
while (x < 10) then
	x++;
endwhile
```

### Logical Operators

The language supports logical operators for combining boolean expressions: **and, or**

- Example

```docker
if (x > 0 and y > 0) then
	print("Both are positive")
endif
```
