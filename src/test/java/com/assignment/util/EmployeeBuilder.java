package com.assignment.util;

import com.assignment.domain.Employee;

public final class EmployeeBuilder {
    private Long id;
    private String firstName;
    private String lastName;
    private String telephone;
    private String email;

    private EmployeeBuilder() {
    }

    public static EmployeeBuilder anEmployee() {
        return new EmployeeBuilder();
    }

    public static EmployeeBuilder anEmployeeWithDefaults() {
        return new EmployeeBuilder().withFirstName("Donald")
                .withLastName("Trump")
                .withEmail("trump@mail.us")
                .withTelephone("+100000001");
    }

    public EmployeeBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public EmployeeBuilder withFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public EmployeeBuilder withLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public EmployeeBuilder withTelephone(String telephone) {
        this.telephone = telephone;
        return this;
    }

    public EmployeeBuilder withEmail(String email) {
        this.email = email;
        return this;
    }

    public Employee build() {
        Employee employee = new Employee();
        employee.setFirstName(firstName);
        employee.setLastName(lastName);
        employee.setTelephone(telephone);
        employee.setEmail(email);
        return employee;
    }
}
