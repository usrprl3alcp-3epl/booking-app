package com.assignment.dao;

import com.assignment.domain.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data-REST will create implementation and REST resource by itself.
 * We should just configure and customize it.
 */
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

}
