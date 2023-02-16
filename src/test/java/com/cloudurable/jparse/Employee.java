package com.cloudurable.jparse;

public record Employee(String firstName, String lastName,
                       String dob, boolean manager,
                       int id, int managerId) {
}
