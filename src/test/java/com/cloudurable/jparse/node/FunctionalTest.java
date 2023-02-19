package com.cloudurable.jparse.node;

import com.cloudurable.jparse.Department;
import com.cloudurable.jparse.Employee;
import com.cloudurable.jparse.Path;
import com.cloudurable.jparse.source.CharSource;
import com.cloudurable.jparse.source.Sources;
import org.junit.jupiter.api.Test;

import java.io.File;

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


        final var departmentsNode = Path.atPath("departments", json).asCollection().asArray();
        final var departments = departmentsNode.mapObjectNode(on ->
                new Department(on.getString("departmentName"),
                        on.getArrayNode("employees").mapObjectNode(en ->
                                new Employee(en.getString("firstName"), en.getString("lastName"),
                                        en.getString("dob"), en.getBoolean("manager"),
                                        en.getInt("id"), en.getInt("managerId"))
                        )));

        assertEquals(2, departments.size());
    }

    @Test
    void map() {

        final String json = getJson();


        final var departmentsNode = Path.atPath("departments", json).asCollection().asArray();

        final var departments = departmentsNode.map(node -> {
            final var on = node.asCollection().asObject();
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


        final var departmentsNode = Path.atPath("departments", json).asCollection().asArray();

        final var engineering = departmentsNode.findObjectNode(objectNode ->
                objectNode.getNode("departmentName").asScalar().equalsString("Engineering"));

        assertTrue(engineering.isPresent());


    }

    @Test
    void find() {


        final String json = getJson();


        final var departmentsNode = Path.atPath("departments", json).asCollection().asArray();

        final var engineering = departmentsNode.find(node -> {
                    final var objectNode = node.asCollection().asObject();
                    return objectNode.getNode("departmentName").asScalar().equalsString("Engineering");
                }
        );

        assertTrue(engineering.isPresent());
    }

    @Test
    void filterObjects() {
        final String json = getJson();


        final var departmentsNode = Path.atPath("departments", json).asCollection().asArray();

        final var engineering = departmentsNode.filterObjects(objectNode -> {
                    return objectNode.getNode("departmentName").asScalar().equalsString("Engineering");
                }
        );

        assertEquals(1, engineering.size());
    }

    @Test
    void filter() {

        final String json = getJson();


        final var departmentsNode = Path.atPath("departments", json).asCollection().asArray();

        final var engineering = departmentsNode.filter(node -> {
                    final var objectNode = node.asCollection().asObject();
                    return objectNode.getNode("departmentName").asScalar().equalsString("Engineering");
                }
        );

        assertEquals(1, engineering.size());
    }
}