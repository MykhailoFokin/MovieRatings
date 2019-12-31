package solvve.course.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import solvve.course.domain.MovieProdCountries;

import java.util.UUID;

@Repository
public interface MovieProdCountriesRepository extends CrudRepository<MovieProdCountries, UUID> {
}
