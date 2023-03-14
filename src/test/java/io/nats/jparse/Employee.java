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

import java.util.Objects;

public final class Employee {
    private final String firstName;
    private final String lastName;
    private final String dob;
    private final boolean manager;
    private final int id;
    private final int managerId;

    public Employee(String firstName, String lastName,
             String dob, boolean manager,
             int id, int managerId) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.dob = dob;
        this.manager = manager;
        this.id = id;
        this.managerId = managerId;
    }

    public String firstName() {
        return firstName;
    }

    public String lastName() {
        return lastName;
    }

    public String dob() {
        return dob;
    }

    public boolean manager() {
        return manager;
    }

    public int id() {
        return id;
    }

    public int managerId() {
        return managerId;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        Employee that = (Employee) obj;
        return Objects.equals(this.firstName, that.firstName) &&
                Objects.equals(this.lastName, that.lastName) &&
                Objects.equals(this.dob, that.dob) &&
                this.manager == that.manager &&
                this.id == that.id &&
                this.managerId == that.managerId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, dob, manager, id, managerId);
    }

    @Override
    public String toString() {
        return "Employee[" +
                "firstName=" + firstName + ", " +
                "lastName=" + lastName + ", " +
                "dob=" + dob + ", " +
                "manager=" + manager + ", " +
                "id=" + id + ", " +
                "managerId=" + managerId + ']';
    }

}
