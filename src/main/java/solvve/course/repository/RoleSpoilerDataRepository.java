package solvve.course.repository;

import org.springframework.data.repository.CrudRepository;
import solvve.course.domain.RoleSpoilerData;

import java.util.UUID;

public interface RoleSpoilerDataRepository extends CrudRepository<RoleSpoilerData, UUID> {
}