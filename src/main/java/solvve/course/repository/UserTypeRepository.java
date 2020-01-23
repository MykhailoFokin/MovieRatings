package solvve.course.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import solvve.course.domain.UserType;
import java.util.UUID;

@Repository
public interface UserTypeRepository extends CrudRepository<UserType, UUID> {

}
