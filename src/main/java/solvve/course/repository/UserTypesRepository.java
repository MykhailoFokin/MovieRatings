package solvve.course.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import solvve.course.domain.UserTypes;
import java.util.UUID;

@Repository
public interface UserTypesRepository extends CrudRepository<UserTypes, UUID> {

}
