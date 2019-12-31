package solvve.course.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import solvve.course.domain.Countries;

import java.util.UUID;

@Repository
public interface CountriesRepository extends CrudRepository<Countries, UUID> {
}
