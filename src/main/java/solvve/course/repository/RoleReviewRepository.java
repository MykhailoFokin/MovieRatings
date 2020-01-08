package solvve.course.repository;

import org.springframework.data.repository.CrudRepository;
import solvve.course.domain.RoleReview;

import java.util.UUID;

public interface RoleReviewRepository extends CrudRepository<RoleReview, UUID> {
}
