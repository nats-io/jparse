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
package io.nats.jparse.node;

import io.nats.jparse.examples.Department;
import io.nats.jparse.examples.Employee;
import io.nats.jparse.Path;
import io.nats.jparse.source.CharSource;
import io.nats.jparse.source.Sources;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


class FunctionalTest {

    private static String getJson() {

        try {
            final File file = new File("./src/test/resources/json/depts.json");
            final CharSource charSource = Sources.fileSource(file);
            return charSource.toString().trim();
        } catch (Exception ex) {
            throw new IllegalStateException(ex);
        }

    }

    @Test
    void mapObjectNode() {

        final String json = getJson();


        final ArrayNode departmentsNode = Path.atPath("departments", json).asCollection().asArray();
        final List<Department> departments = departmentsNode.mapObjectNode(on ->
                new Department(on.getString("departmentName"),
                        on.getArrayNode("employees").mapObjectNode(en ->
                                new Employee(en.getString("firstName"), en.getString("lastName"),
                                        en.getString("dob"), en.getBoolean("manager"),
                                        en.getInt("id"), en.getInt("managerId"))
                        )));

        Assertions.assertEquals(2, departments.size());
    }

    @Test
    void map() {

        final String json = getJson();


        final ArrayNode departmentsNode = Path.atPath("departments", json).asCollection().asArray();

        final List<Department> departments = departmentsNode.map(node -> {
            final ObjectNode on = node.asCollection().asObject();
            return new Department(on.getString("departmentName"),
                    on.getArrayNode("employees").mapObjectNode(en ->
                            new Employee(en.getString("firstName"), en.getString("lastName"),
                                    en.getString("dob"), en.getBoolean("manager"),
                                    en.getInt("id"), en.getInt("managerId"))
                    ));
        });


    }

    @Test
    void findObjectNode() {

        final String json = getJson();


        final ArrayNode departmentsNode = Path.atPath("departments", json).asCollection().asArray();

        final Optional<ObjectNode> engineering = departmentsNode.findObjectNode(objectNode ->
                objectNode.getNode("departmentName").asScalar().equalsString("Engineering"));

        Assertions.assertTrue(engineering.isPresent());


    }

    @Test
    void find() {


        final String json = getJson();


        final ArrayNode departmentsNode = Path.atPath("departments", json).asCollection().asArray();

        final Optional<Node> engineering = departmentsNode.find(node -> {
                    final ObjectNode objectNode = node.asCollection().asObject();
                    return objectNode.getNode("departmentName").asScalar().equalsString("Engineering");
                }
        );

        Assertions.assertTrue(engineering.isPresent());
    }

    @Test
    void filterObjects() {
        final String json = getJson();


        final ArrayNode departmentsNode = Path.atPath("departments", json).asCollection().asArray();

        final List<ObjectNode> engineering = departmentsNode.filterObjects(objectNode -> {
                    return objectNode.getNode("departmentName").asScalar().equalsString("Engineering");
                }
        );

        Assertions.assertEquals(1, engineering.size());
    }

    @Test
    void filter() {

        final String json = getJson();


        final ArrayNode departmentsNode = Path.atPath("departments", json).asCollection().asArray();

        final List<Node> engineering = departmentsNode.filter(node -> {
                    final ObjectNode objectNode = node.asCollection().asObject();
                    return objectNode.getNode("departmentName").asScalar().equalsString("Engineering");
                }
        );

        Assertions.assertEquals(1, engineering.size());
    }
}
