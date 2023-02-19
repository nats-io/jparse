# J Parse 

JParse, the most efficient JSON parser for the JVM yet.


# Why JParse 

JParse is the most efficient JSON parser for the JVM yet - it uses an index overlay to deliver lightning-fast parsing speeds. 

The JParse parser is designed to revolutionize the way developers process and analyze JSON data, by providing a smarter, 
more efficient approach to JSON parsing.

So, what is an index overlay, and why is it such a game-changer? Put simply, an index overlay is a mechanism that allows
our JSON parser to access and analyze data in real-time as it is being parsed (before it Is completely parsed and deserialized). 
This means that instead of having to wait until the entire JSON document is parsed before analyzing it, 
our parser can extract and analyze specific pieces of data that you want. 

This dramatically speeds up the parsing process and reduces the amount of memory required to parse large JSON documents. 
It can also speed up mapping JSON objects and arrays to Java objects. It for sure speeds up the process by avoiding tons of buffer copies.
This is especially the case if you only want a portion of the JSON payload and want to use the built-in JSONPath support.

But that's not all - with our JSON parser you can easily implement advanced features, such as support for incremental 
parsing, in-memory compression, and automatic schema generation. The index overlay feature is a boon and makes our 
parser the most efficient, flexible, and developer-friendly JSON parser available today.

Whether you're processing massive data sets, building complex data pipelines, or simply looking for a faster, 
more efficient way to parse JSON data, our index overlay JSON parser is the ideal solution.
Try it today and experience the power of real-time JSON parsing for yourself!

## What is an Index overlay parser

An index overlay parser is a type of parser used for processing structured data, particularly JSON data. 
Unlike traditional parsers that typically parse the entire data document before returning the results, 
an index overlay parser uses a mechanism that allows for real-time access and analysis of data as it is being parsed.

Specifically, an index overlay parser creates an index or a mapping of the data elements in the JSON document during 
the parsing process. This index allows the parser to quickly access and retrieve specific data elements in the document
as needed, without having to re-parse the entire document again. This dramatically speed up the parsing process, 
especially for large or complex JSON documents, and can also reduce the memory requirements for parsing.

For example, suppose you have a large JSON document that contains multiple nested objects, and you need to extract only 
a specific piece of data from it. With a traditional parser, you would have to parse the entire document, and then 
traverse the entire object hierarchy to find the data you need. With an index overlay parser, however, the parser 
can access the specific data element directly using the index it has created during parsing, significantly 
reducing the time and memory required to process the document.

In summary, an index overlay parser offers a more efficient and faster way to process JSON data, by creating an 
index of the data elements during parsing, and allowing for real-time access and analysis of data as it is being parsed.



## Why not just update Boon?
I am one of the original authors of Boon, which was a utility library that became a JSON parser.
[Boon got a lot of attention back in 2014](https://www.infoq.com/news/2014/04/groovy-2.3-json), 
and it was the fastest way to parse JSON and serialize to/from JavaBeans circa 2014. 

Why not just update Boon?  Boon was meant to be a utility lib and became a JSON parser.
Boon is 90,000 lines of code (just Java main not including test classes).
Boon does too many things that no one uses. It was due for a complete redesign. 
It also uses Unsafe which you can't do in later version of Java. 

JParse is mostly done and is only 4,100 lines long vs. 90,000 LoC of Boon. 
Jackson core is 55,000 LOC and there are other libs needed for various data types and mappings. 


## What is JParse?

JParse is a *JSON parser* plus a small subset of *JSONPath*.
It is small (just 4,200 lines long). It uses an index overlay from the ground up which lays the foundation for quick JSONPath lookups 
as well as very fast mapping. It will not grow in feature set. Any other features will be part of other libs. 


## Is JParse fast?

Yes. 


#### Parsing normal JSON

```text
Benchmark                                  Mode  Cnt       Score   Error  Units
BenchMark.simpleDeserializeJParseWebXML    thrpt    2  237334.575          ops/s
BenchMark.simpleDeserializeJacksonWebXML   thrpt    2  114135.958          ops/s
```

```text 
Benchmark                            Mode  Cnt        Score   Error  Units
BenchMark.simpleDeserializeJParseGlossary    thrpt    2  1059241.326          ops/s
BenchMark.simpleDeserializeJacksonGlossary   thrpt    2   466824.567          ops/s
```

```text 

Benchmark                                      Mode  Cnt        Score   Error  Units
BenchMark.readGlossaryJParse                  thrpt    2  1034323.573          ops/s
BenchMark.readGlossaryNoggit                  thrpt    2   830511.356          ops/s
BenchMark.readWebGlossaryNoggitObjectBuilder  thrpt    2   541948.355          ops/s
BenchMark.readGlossaryJackson                 thrpt    2   468925.690          ops/s

```


#### Parsing large primitive arrays and basic values

```text 

Benchmark                             Mode  Cnt        Score   Error  Units
BenchMark.jParseBigDecimalArrayFast  thrpt    2  1201663.430          ops/s
BenchMark.jacksonBigDecimalArray     thrpt    2   722409.093          ops/s


BenchMark.jParseDoubleArrayFast      thrpt    2   890538.018          ops/s
BenchMark.jacksonDoubleArray         thrpt    2   627404.869          ops/s


BenchMark.jParseFloatArrayFast       thrpt    2   894741.821          ops/s
BenchMark.jacksonFloatArray          thrpt    2   484765.612          ops/s



Benchmark                       Mode  Cnt        Score   Error  Units
BenchMark.jParseBigIntArray    thrpt    2   889298.905          ops/s
BenchMark.jacksonBigIntArray   thrpt    2   888873.432          ops/s

BenchMark.jParseIntArray       thrpt    2  1541039.040          ops/s
BenchMark.jacksonIntArray      thrpt    2  1124429.948          ops/s

BenchMark.jParseLongArray      thrpt    2  1418787.047          ops/s
BenchMark.jacksonLongArray     thrpt    2  1163603.399          ops/s
```

#### Serialize and Deserialize 
```text 

Benchmark                                         Mode  Cnt       Score   Error  Units
BenchMark.deserializeIntoMapJParseAndSerialize   thrpt    2  867284.089          ops/s
BenchMark.deserializeIntoMapJacksonAndSerialize  thrpt    2  264564.015          ops/s

```

```text
Benchmark                                                    Mode  Cnt       Score   Error  Units
BenchMark.deserializeIntoMapJParseAndGetPathThenSerialize   thrpt    2  788400.189          ops/s
BenchMark.deserializeIntoMapJacksonAndGetPathThenSerialize  thrpt    2  285313.787          ops/s
```


#### Working with Paths

```text

Benchmark                                        Mode  Cnt       Score   Error  Units
BenchMark.simpleDeserializeJParseThenPathGrab   thrpt    2  445645.130          ops/s
BenchMark.simpleDeserializeJacksonThenPathGrab  thrpt    2  293524.053          ops/s



Benchmark                                       Mode  Cnt       Score   Error  Units
BenchMark.simpleDeserializeJParseThenPathGrab  thrpt    2  467119.563          ops/s
BenchMark.simpleDeserializeJaywayThenPathGrab  thrpt    2  382242.810          ops/s


Benchmark                                             Mode  Cnt       Score   Error  Units
BenchMark.simpleDeserializeJParseThenPathGrabWEBXML  thrpt    2  142538.504          ops/s
BenchMark.simpleDeserializeJaywayThenPathGrabWEBXML  thrpt    2  113641.983          ops/s



```

## Using JParse




#### Sample JSON file for departments 

```javascript

{
  "departments": [
    {
      "departmentName" : "Engineering",
      "employees": [
        {
          "firstName": "Bob",
          "lastName": "Jones",
          "dob": "05/22/1990",
          "manager": true,
          "id": 111,
          "managerId": -1
        },
        {
          "firstName": "Rick",
          "lastName": "Hightower",
          "dob": "05/22/1990",
          "manager": false,
          "id": 777,
          "managerId": 111
        },
        {
          "firstName": "Cindy",
          "lastName": "Torre-alto",
          "dob": "04/15/1993",
          "manager": true,
          "id": 999,
          "managerId": 111
        }
      ]
    },
    {
      "departmentName" : "HR",
      "employees": [
        {
          "firstName": "Sarah",
          "lastName": "Jones",
          "dob": "05/22/1990",
          "manager": true,
          "id": 222,
          "managerId": 111
        },
        {
          "firstName": "Sam",
          "lastName": "Smith",
          "dob": "05/22/1990",
          "manager": true,
          "id": 555,
          "managerId": 222
        },
        {
          "firstName": "Suzy",
          "lastName": "Jones",
          "dob": "04/15/1993",
          "manager": true,
          "id": 551,
          "managerId": 222
        }
      ]
    }
  ]
}




```

Note this is a JSON payloads that has departments with two departments of employees, namely, HR and engineering departments.


#### Read JSON from a file

```java


final File file = new File("./src/test/resources/json/depts.json");

final var rootNode = Json.toRootNode(Sources.fileSource(file));

```


#### Grab just the employees from the engineering department

```java
            final File file = new File("./src/test/resources/json/depts.json");

            final var rootNode = Json.toRootNode(Sources.fileSource(file));

            final var engineeringEmployees = Path.atPath("departments[0].employees", rootNode)
                    .asCollection().asArray();

           System.out.println("      " + engineeringEmployees.toJsonString());

```

The object engineeringEmployees is a JSON node and it just contains the data from that part of the JSON document. 

#### Grab just the employees from the engineering department Output

```json  
    [
        {
          "firstName": "Bob",
          "lastName": "Jones",
          "dob": "05/22/1990",
          "manager": true,
          "id": 111,
          "managerId": -1
        },
        {
          "firstName": "Rick",
          "lastName": "Hightower",
          "dob": "05/22/1990",
          "manager": false,
          "id": 777,
          "managerId": 111
        },
        {
          "firstName": "Cindy",
          "lastName": "Torre-alto",
          "dob": "04/15/1993",
          "manager": true,
          "id": 999,
          "managerId": 111
        }
      ]
```
See how easy it is to slice and dice up a JSON doc?

#### Get the Cindy node from the engineeringEmployees array
```java 

            final var cindy = Path.atPath("[2]", engineeringEmployees);
           
            System.out.println("      " + cindy.toJsonString());
```
#### Get the Cindy node from the engineeringEmployees array Output
```javascript  
        {
          "firstName": "Cindy",
          "lastName": "Torre-alto",
          "dob": "04/15/1993",
          "manager": true,
          "id": 999,
          "managerId": 111
        }
```

#### Use different JSONPath expressions to get parts of the cindy node 

```java

            final var cindyName = Path.atPath("[2].firstName", engineeringEmployees);
            final var cindyId = Path.atPath(".id", cindy);
            final var manager = Path.atPath("[2].manager", engineeringEmployees);
```

Note that JSONPath nodes are always basic Java types and the API is always easy to use.
There are no surprises. 

#### Just List, Map, Number, and CharSequences. No complicated API

```java


            if (manager.asScalar().booleanValue()) {
                System.out.printf("This employee %s is a manager %s \n", cindyName, manager);
            }

            if (cindyName.asScalar().equalsString("Cindy")) {
                System.out.printf("The employee's name is  Cindy %s \n", cindyId);
            }

            if (cindyName instanceof CharSequence) {
                System.out.println("cirhyeName is a CharSequence");
            }

            if (cindyId instanceof Number) {
                System.out.println("cindyId is a Number");
            }

            if (engineeringEmployees instanceof List) {
                System.out.println("engineeringEmployees is a List " + engineeringEmployees.getClass().getName());
            }

            if (cindy instanceof Map) {
                System.out.println("cindy is a Map " + cindy.getClass().getName());
            }
            
```

Keep in mind that there is no complicated API to learn:
* java.lang.Number (JSON number)
* java.util.List (JSON array)
* Java array (e.g., int[])  (JSON array)
* java.lang.CharSequence (JSON String and every node)
* java.util.Map (JSON object)

In fact, the API for looking things up and mappings is more or less the Java streams API. 

#### Using Java streams and Java functional programming with JParse

```java
           final var rick = engineeringEmployees.stream()
                    .map(node -> node.asCollection().asObject())
                    .filter(objectNode ->
                            objectNode.getString("firstName").equals("Rick")
                    ).findFirst();

            rick.ifPresent(node -> {
                System.out.println("Found  " + node);

                exploreNode(node);
            });
```

We have automated and sped up common mappings and filtering. 

#### Using JParse to do functional mapping and find operations 

```java


            final var rick2 = engineeringEmployees.findFirstObjectNode(
                    objectNode ->
                            objectNode.getString("firstName").equals("Rick")

            );

            rick2.ifPresent(node -> {
                System.out.println("Found  " + node);

                exploreNode(node);
            });
```

The above does the same as the example previously to it but in less lines of code. 

#### Object mappings are powerful and easy to implement 

```java

            public record Employee(String firstName, String lastName,
                       String dob, boolean manager,
                       int id, int managerId) {
            }
            ...
            final var  employees = engineeringEmployees.mapObjectNode(on ->
                    new Employee(on.getString("firstName"), on.getString("lastName"),
                            on.getString("dob"), on.getBoolean("manager"),
                            on.getInt("id"), on.getInt("managerId"))
            );

            employees.forEach(System.out::println);
```

#### Output
```text 

Employee[firstName=Bob, lastName=Jones, dob=05/22/1990, manager=true, id=111, managerId=-1]
Employee[firstName=Rick, lastName=Hightower, dob=05/22/1990, manager=false, id=777, managerId=111]
Employee[firstName=Cindy, lastName=Torre-alto, dob=04/15/1993, manager=true, id=999, managerId=111]

```



#### Object mappings are powerful and easy to implement for nested cases too

```java

            public record Employee(String firstName, String lastName,
                       String dob, boolean manager,
                       int id, int managerId) {
            }
            
            public record Department (String name,
                                      List<Employee> employees) {
            }

            ...
            final var departmentsNode = Path.atPath("departments", json).asCollection().asArray();

            final var departments = departmentsNode.mapObjectNode(on ->
                    new Department(on.getString("departmentName"),
                    on.getArrayNode("employees").mapObjectNode(en ->
                    new Employee(en.getString("firstName"), en.getString("lastName"),
                    en.getString("dob"), en.getBoolean("manager"),
                    en.getInt("id"), en.getInt("managerId"))
                    )));

                    departments.forEach(System.out::println);
```

Working with objects attributes is quite easy, and you have many options. 

#### Output
```text 

Department[name=Engineering, employees=[Employee[firstName=Bob, lastName=Jones, dob=05/22/1990, manager=true, id=111, managerId=-1], Employee[firstName=Rick, lastName=Hightower, dob=05/22/1990, manager=false, id=777, managerId=111], Employee[firstName=Cindy, lastName=Torre-alto, dob=04/15/1993, manager=true, id=999, managerId=111]]]
Department[name=HR, employees=[Employee[firstName=Sarah, lastName=Jones, dob=05/22/1990, manager=true, id=222, managerId=111], Employee[firstName=Sam, lastName=Smith, dob=05/22/1990, manager=true, id=555, managerId=222], Employee[firstName=Suzy, lastName=Jones, dob=04/15/1993, manager=true, id=551, managerId=222]]]

```

#### Working with object attributes 

```java

    private static void exploreNode(ObjectNode node) {

        int id = node.getInt("id");
        String name = node.getString("firstName");
        String dob = node.getString("dob");
        boolean isManager = node.getBoolean("manager");
        int managerId = node.getInt("managerId");


        System.out.printf("%d %s %s %s %d \n", id, name, dob, isManager, managerId);


        final var idNode = node.getNumberNode("id");
        final var nameNode = node.getStringNode("firstName");
        final var dobNode = node.getStringNode("dob");
        final var isManagerNode = node.getBooleanNode("manager");
        final var managerIdNode = node.getNumberNode("managerId");

        System.out.printf("%s %s %s %s %s \n", idNode, nameNode, dobNode, isManagerNode, managerIdNode);


        System.out.printf("%d %s %s %s %d \n", idNode.intValue(),
                nameNode.toString(), dobNode.toString(),
                isManagerNode.booleanValue(), managerIdNode.intValue());

    }

```

#### Output
```text 
777 Rick 05/22/1990 false 111 
777 Rick 05/22/1990 false 111 
777 Rick 05/22/1990 false 111 
```


## Caveats and limitations


* Not done yet (BenchMark will change)
* Still needs to improve error handling  (Virtually none so no real useful messages if their are errors but easy to add).
* Boon had really excellent error messages and JParse will to
* Does not support any extras (by design)
* Does not support NaN, +Infinity, -Infinity because that is not in the JSON org spec.
* Does not support any attribute key types but Strings as per JSON org spec.
* Just strict JSON no minimal JSON (Boon and others support strict and non strict modes, JParse only does strict)


