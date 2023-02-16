package com.cloudurable.jparse;

import java.util.List;

public record Department (String name,
                          List<Employee> employees) {
}
