package solvve.course.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import solvve.course.domain.ReleaseDetail;

import java.util.UUID;

@Repository
public interface ReleaseDetailRepository extends CrudRepository<ReleaseDetail, UUID>  {
}
