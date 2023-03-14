/*
 * Copyright 2013-2023 Richard M. Hightower
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  		http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package io.nats.jparse;

import io.nats.jparse.node.*;
import io.nats.jparse.source.CharSource;
import io.nats.jparse.source.Sources;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static io.nats.jparse.Json.toRootNode;

public class Example {

    public static void main(String... args) {

        try {
            final String json = getJson();

            //final var engineeringEmployees = Path.atPath("departments[0].employees", json).asCollection().asArray();

            final File file = new File("./src/test/resources/json/depts.json");

            final RootNode rootNode = Json.toRootNode(Sources.fileSource(file));

            final ArrayNode engineeringEmployees = Path.atPath("departments[0].employees", rootNode)
                    .asCollection().asArray();

            final Node cindy = Path.atPath("[2]", engineeringEmployees);

            final Node cindyName = Path.atPath("[2].firstName", engineeringEmployees);
            final Node cindyId = Path.atPath(".id", cindy);
            final Node manager = Path.atPath("[2].manager", engineeringEmployees);


            System.out.println("      " + engineeringEmployees.toJsonString());
            System.out.println("      " + cindy.toJsonString());


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


            final Optional<ObjectNode> rick = engineeringEmployees.stream()
                    .map(node -> node.asCollection().asObject())
                    .filter(objectNode ->
                            objectNode.getString("firstName").equals("Rick")
                    ).findFirst();

            rick.ifPresent(node -> {
                System.out.println("Found  " + node);

                exploreNode(node);
            });


            final Optional<ObjectNode> rick2 = engineeringEmployees.findObjectNode(
                    objectNode ->
                            objectNode.getString("firstName").equals("Rick")

            );

            rick2.ifPresent(node -> {
                System.out.println("Found  " + node);

                exploreNode(node);
            });


            final List<Employee>  employees = engineeringEmployees.mapObjectNode(on ->
                    new Employee(on.getString("firstName"), on.getString("lastName"),
                            on.getString("dob"), on.getBoolean("manager"),
                            on.getInt("id"), on.getInt("managerId"))
            );

            employees.forEach(System.out::println);


            final ArrayNode departmentsNode = Path.atPath("departments", json).asCollection().asArray();

            final List<Department> departments = departmentsNode.mapObjectNode(on ->
                    new Department(on.getString("departmentName"),
                            on.getArrayNode("employees").mapObjectNode(en ->
                                    new Employee(en.getString("firstName"), en.getString("lastName"),
                                            en.getString("dob"), en.getBoolean("manager"),
                                            en.getInt("id"), en.getInt("managerId"))
                            )));

            departments.forEach(System.out::println);


            parseBadJson();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    private static void parseBadJson() {

        //RootNode rootNode = toRootNode(niceJson("'hi mom"));

        //RootNode rootNode = toRootNode(niceJson("['aabc', \n  'def', \n 123f3f.9]"));

        //RootNode rootNode = toRootNode(niceJson("{'name':'rick', \n\n\n" +
        //        " 'age':19, 'height'=10}"));
    }

    private static void exploreNode(ObjectNode node) {

        int id = node.getInt("id");
        String name = node.getString("firstName");
        String dob = node.getString("dob");
        boolean isManager = node.getBoolean("manager");
        int managerId = node.getInt("managerId");


        System.out.printf("%d %s %s %s %d \n", id, name, dob, isManager, managerId);


        final NumberNode idNode = node.getNumberNode("id");
        final StringNode nameNode = node.getStringNode("firstName");
        final StringNode dobNode = node.getStringNode("dob");
        final BooleanNode isManagerNode = node.getBooleanNode("manager");
        final NumberNode managerIdNode = node.getNumberNode("managerId");

        System.out.printf("%s %s %s %s %s \n", idNode, nameNode, dobNode, isManagerNode, managerIdNode);


        System.out.printf("%d %s %s %s %d \n", idNode.intValue(),
                nameNode.toString(), dobNode.toString(),
                isManagerNode.booleanValue(), managerIdNode.intValue());

    }


    private static String getJson() {

        try {
            final File file = new File("./src/test/resources/json/depts.json");
            final CharSource charSource = Sources.fileSource(file);
            return charSource.toString().trim();
        } catch (Exception ex) {
            throw new IllegalStateException(ex);
        }

    }
}
