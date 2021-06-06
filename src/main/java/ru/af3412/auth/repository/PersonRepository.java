package ru.af3412.auth.repository;

import org.springframework.data.repository.CrudRepository;
import ru.af3412.auth.domain.Person;

public interface PersonRepository extends CrudRepository<Person, Integer> {
}
