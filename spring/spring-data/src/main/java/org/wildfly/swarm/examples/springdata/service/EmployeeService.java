package org.wildfly.swarm.examples.springdata.service;

import java.util.List;

import org.wildfly.swarm.examples.springdata.EmployeeNotFound;
import org.wildfly.swarm.examples.springdata.model.Employee;

/**
 * @author Ken Finnigan
 */
public interface EmployeeService {
    Employee create(Employee employee);
    Employee delete(int id) throws EmployeeNotFound;
    List<Employee> findAll();
    Employee update(Employee employee) throws EmployeeNotFound;
    Employee findById(int id);
}
