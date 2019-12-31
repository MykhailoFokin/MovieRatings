package solvve.course.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import solvve.course.domain.ReleaseDetails;

import java.util.UUID;

@Repository
public interface ReleaseDetailsRepository extends CrudRepository<ReleaseDetails, UUID>  {
}
