package org.wildfly.swarm.examples.springdata.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.wildfly.swarm.examples.springdata.model.Employee;

/**
 * @author Ken Finnigan
 */
@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
}
