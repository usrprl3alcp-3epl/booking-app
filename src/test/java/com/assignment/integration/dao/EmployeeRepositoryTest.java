package com.assignment.integration.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;

import com.assignment.dao.EmployeeRepository;

public class EmployeeRepositoryTest extends AbstractRepositoryTest {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public JpaRepository getRepository() {
        return employeeRepository;
    }
}
