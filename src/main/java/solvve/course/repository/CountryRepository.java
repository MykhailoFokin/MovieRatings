package solvve.course.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import solvve.course.domain.Country;

import java.util.UUID;

@Repository
public interface CountryRepository extends CrudRepository<Country, UUID>, CountryRepositoryCustom {
}
