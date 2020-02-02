package solvve.course.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import solvve.course.domain.Crew;
import java.util.UUID;

@Repository
public interface CrewRepository extends CrudRepository<Crew, UUID>, CrewRepositoryCustom {
}
