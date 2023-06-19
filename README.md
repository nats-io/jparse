
# JParse - Easy JSON Parsing

JParse is a small, [fast](https://github.com/nats-io/jparse/wiki#is-jparse-fast), and compliant JSON parser that implements events parsing and index overlay.

# Goals

Fastest, most compliant and smallest JSON parser lib for the JVM, and it is developer friendly.

# License

Apache 2.


# What is JParse?
[JParse](https://github.com/nats-io/jparse/wiki#why-jparse) is an innovative JSON parser for JVM that offers lightning-fast parsing speeds through an index overlay
mechanism. JParse is the most efficient, [fastest](https://github.com/nats-io/jparse/wiki#is-jparse-fast), and developer-friendly JSON parser available for the JVM.
This makes it ideal for processing massive data sets, building complex data pipelines or simply for a faster
and more efficient way to parse JSON data.

JParse is a *JSON parser* plus a small subset of *JSONPath*.




## Using JParse

This is a [short guide on how to use JParse](https://github.com/nats-io/jparse/wiki#using-jparse).
([Go to the wiki for a longer guide on how to use JParse](https://github.com/nats-io/jparse/wiki#using-jparse).)

### Maven

JParse has no dependencies except the JVM itself.

```xml
<!-- See for latest version https://mvnrepository.com/artifact/io.nats/jparse -->
<dependency>
    <groupId>io.nats</groupId>
    <artifactId>jparse</artifactId>
    <version>1.2.0</version>
</dependency>

```

### Gradle

```gradle
//See for latest version  https://mvnrepository.com/artifact/io.nats/jparse
implementation group: 'io.nats', name: 'jparse', version: '1.2.3'

```


### Reading JSON from a File

```java
final File file = new File("./src/test/resources/json/depts.json");
final var rootNode = Json.toRootNode(Sources.fileSource(file));
```

### Extracting Specific Data

Extract employees from the engineering department:

```java

final var engineeringEmployees = Path.atPath("departments[0].employees", rootNode).asCollection().asArray();

```

### Working with JSON Path expressions

Extract specific node from the engineeringEmployees array:

```java 
final var cindy = Path.atPath("[2]", engineeringEmployees);

```

Extract different parts of the cindy node:

```java
final var cindyName = Path.atPath("[2].firstName", engineeringEmployees);
final var cindyId = Path.atPath(".id", cindy);
final var manager = Path.atPath("[2].manager", engineeringEmployees);
```

### API Overview

JParse uses standard Java types, making it straightforward to use with no surprises:

| JSON Type | Java Type |
| --- | --- |
| JSON number | java.lang.Number |
| JSON array | java.util.List |
| JSON array | Java array (e.g., int[]) |
| JSON String and every node | java.lang.CharSequence |
| JSON object | java.util.Map |


### Functional Programming with JParse
JParse supports Java streams and functional programming:

```java 

final var rick = engineeringEmployees.stream()
            .map(node -> node.asCollection().asObject())
            .filter(objectNode -> objectNode.getString("firstName").equals("Rick"))
            .findFirst();

```
You can also use JParse for functional mapping and find operations:

```java

final var rick2 = engineeringEmployees.findObjectNode(
objectNode -> objectNode.getString("firstName").equals("Rick")
);
```


### Object Mappings
JParse supports easy object mappings:

```java 
public record Employee(String firstName, String lastName,
String dob, boolean manager,
int id, int managerId) {
}
```

```java
final var employees = engineeringEmployees.mapObjectNode(on ->
    new Employee(on.getString("firstName"), on.getString("lastName"),
        on.getString("dob"), on.getBoolean("manager"),
        on.getInt("id"), on.getInt("managerId"))
);

```
