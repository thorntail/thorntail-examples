package org.wildfly.swarm.examples.springdata.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.wildfly.swarm.examples.springdata.EmployeeNotFound;
import org.wildfly.swarm.examples.springdata.model.Employee;
import org.wildfly.swarm.examples.springdata.service.EmployeeService;

/**
 * @author Ken Finnigan
 */
@RestController
@RequestMapping("/employee")
public class EmployeeRestController {

    @Autowired
    EmployeeService service;

    @RequestMapping(method = RequestMethod.GET)
    public List<Employee> allEmployees() {
        return service.findAll();
    }

    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Employee createEmployee(@RequestBody Employee employee) {
        return service.create(employee);
    }

    @RequestMapping(value = "/{employeeId}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Employee deleteEmployee(@PathVariable Integer employeeId) throws EmployeeNotFound {
        return service.delete(employeeId);
    }

    @RequestMapping(value = "/{employeeId}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Employee updateEmployee(@PathVariable Integer employeeId, @RequestBody Employee employee) throws EmployeeNotFound {
        return service.update(employee);
    }

    @RequestMapping(value = "/{employeeId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Employee getEmployee(@PathVariable Integer employeeId) {
        return service.findById(employeeId);
    }
}
