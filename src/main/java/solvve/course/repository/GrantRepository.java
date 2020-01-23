package solvve.course.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import solvve.course.domain.Grant;

import java.util.UUID;

@Repository
public interface GrantRepository extends CrudRepository<Grant, UUID> {
}
