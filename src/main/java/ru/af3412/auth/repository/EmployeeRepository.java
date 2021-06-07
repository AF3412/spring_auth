package ru.af3412.auth.repository;

import org.springframework.data.repository.CrudRepository;
import ru.af3412.auth.domain.Employee;

public interface EmployeeRepository extends CrudRepository<Employee, Integer> {
}
