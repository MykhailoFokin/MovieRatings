package solvve.course.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import solvve.course.domain.Visit;

import java.util.UUID;

@Repository
public interface VisitRepository extends CrudRepository<Visit, UUID> {
}
