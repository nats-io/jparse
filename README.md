# JParse 

JParse, the most efficient JSON parser for the JVM yet.


# Goals 

Fastest, most compliant and smallest JSON parser lib for the JVM, and it is developer friendly.

# License 

Apache 2. 


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

## What is an Index overlay parser?

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

If you would like to learn more about index overlay parsers vs. DOM and Event parsers, read this
[JSON parser description: DOM vs. Index Overlay vs. Event Driven](https://github.com/RichardHightower/jparse/wiki/JSON-parser-description--DOM-vs.-Index-Overlay-vs.-Event-Driven).



...

## JParse origins 

Initially JParse was needed it for another project for a client.
This client changed direction,but the idea of the parser stayed in my mind. 
The idea was to write protocol handler on top of Java NIO but with the idea 
that we would have versions that worked natively in Vertx. It ended up being too big of an effort to 
finish with the budget allocated, but the initial testing showed about a 300x improvement over a legacy client. 

Can't say more than that about the client. Most of the speedup was due to both the protocol and JSON parser 
(as it is a mixed protocol version, routing with headers and JSON) being an index overlay. But, also, quite a bit 
of it was due to legacy code which needed some tuning and updated architecture work.

In general, index overlay is better for protocol headers if you only want a portion of the payload 
(the use case it was created for) or if you are doing integration or routing and don’t need the whole parse. 
An index overlay will not create a bunch of temporary buffers and /or intermediate conversions.

### Why it is better for deserialization
Also, an index overlay is better for any field type for struct or object deserialization. 
Imagine running into a number in JSON.

Is it a float, double, int, long, BigInteger, or BigDecimal?

It is better to wait until you try to deserialize it to do the final conversion because you will 
either do extra work or possibly lose precision if you convert it into a number before it is deserialized
into an object or struct field. Also, imagine that the struct or object does not use all of the fields from 
the JSON object, and some of those fields are Strings. 

String decoding (\n\r\t\uxxxx) can be a time-consuming
operations and the String decoding can take longer than the rest of the JSON parse for large strings 
like the filed named `description` or `notes`.

With an index overlay, only the fields that are used will have to be decoded. 
Decoding is expensive from a buffer copy and an expensive (relatively speaking) operation in general. 
This is a boon for frameworks like Vert.x that build on top of Netty, which goes through great 
pains to avoid buffer copies. Sorry, it slipped. It is a real bonus.

## Use Case Example Cloud Events
The perfect use case would be when you want to use tools like JSONPath to grab a portion of a JSON stream, 
and it would be realistically in the realm of being 100x faster than a non-index overlay parser. 

Another use case where an index overlay parser would do well would be a framework like [CloudEvents](https://cloudevents.io/). 

In the CloudEvents use case, you can read the `summary` and `message` type and route the message to the 
right microservice without final parsing the total payload, and can easily send the payload 
to downstream queues, streams, and services without doing extra work or additional buffer copies.


## Why not just update Boon?
Developers who worked on this were also the original authors of Boon, which was a utility library that became 
a JSON parser.
[Boon got a lot of attention back in 2014](https://www.infoq.com/news/2014/04/groovy-2.3-json), 
and it was the fastest way to parse JSON and serialize to/from JavaBeans circa 2014. 

Why not just update Boon?  Boon was meant to be a utility lib and became a JSON parser.
Boon is 90,000+ lines of code (just Java main not including test classes).
Boon does too many things that no one uses. It was due for a complete redesign. 
It also uses `Unsafe` which you can't do in later version of Java. 

JParse is feature complete now done and is only 5,098 lines long vs. 90,000 LoC of Boon. 
Jackson core is 55,000 LOC and there are other libs needed for various data types and mappings if you use Jackson. 


## What is JParse?

JParse is a *JSON parser* plus a small subset of *JSONPath*.
It is small (just 5,098 lines long). It uses an index overlay from the ground up which lays the foundation for quick JSONPath lookups 
as well as very fast mapping. It will not grow in feature set. Any other features will be part of other libs. 


## Is JParse fast?

Yes. 


<img width="1196" alt="image" src="https://user-images.githubusercontent.com/382678/219976407-eb8200f1-d038-44e2-bea7-504181fc2f52.png">

<img width="1164" alt="image" src="https://user-images.githubusercontent.com/382678/219976440-10ca4901-4a81-4d44-87e4-0fc970955fd4.png">


```text 
Benchmark                                      Mode  Cnt        Score   Error  Units
BenchMark.readGlossaryJParse                  thrpt    2  1034323.573          ops/s
BenchMark.readGlossaryNoggit                  thrpt    2   830511.356          ops/s
BenchMark.readGlossaryNoggitObjectBuilder     thrpt    2   541948.355          ops/s
BenchMark.readGlossaryJackson                 thrpt    2   468925.690          ops/s
```

```text 
Benchmark                             Mode  Cnt        Score   Error  Units
BenchMark.jParseBigDecimalArrayFast  thrpt    2  1201663.430          ops/s
BenchMark.jacksonBigDecimalArray     thrpt    2   722409.093          ops/s


BenchMark.jParseDoubleArrayFast      thrpt    2   890538.018          ops/s
BenchMark.jacksonDoubleArray         thrpt    2   627404.869          ops/s
```

To see more [JSON benchmarks go here](https://github.com/RichardHightower/jparse/wiki/JSON-BenchMarks).

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


            final var rick2 = engineeringEmployees.findObjectNode(
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

* None that we know of.
* Feature complete
* Back ported to Java 11. 
* Added support for event parser and parser that allows comments. 
* Does not support NaN, +Infinity, -Infinity because that is not in the JSON org spec.
* Passes full JSON parser compliance testing see https://github.com/nst/JSONTestSuite. RFC-8259 compliant


