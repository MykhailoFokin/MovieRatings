package solvve.course.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import solvve.course.domain.Persons;
import java.util.UUID;

@Repository
public interface PersonsRepository extends CrudRepository<Persons, UUID> {
}
