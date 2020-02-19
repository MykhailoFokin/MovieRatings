package solvve.course.repository;

import org.springframework.data.repository.CrudRepository;
import solvve.course.domain.CrewType;

import java.util.UUID;

public interface CrewTypeRepository extends CrudRepository<CrewType, UUID>, CrewTypeRepositoryCustom {

}
