package solvve.course.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import solvve.course.domain.MovieCompany;

import java.util.UUID;

@Repository
public interface MovieCompanyRepository extends CrudRepository<MovieCompany, UUID>, MovieCompanyRepositoryCustom {
}
