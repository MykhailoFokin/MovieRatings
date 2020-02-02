package solvve.course.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import solvve.course.domain.Master;

import java.util.UUID;

@Repository
public interface MasterRepository  extends CrudRepository<Master, UUID>, MasterRepositoryCustom {

}
