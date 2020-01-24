package solvve.course.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import solvve.course.domain.UserGrant;

import java.util.UUID;

@Repository
public interface UserGrantRepository extends CrudRepository<UserGrant, UUID> {
}
