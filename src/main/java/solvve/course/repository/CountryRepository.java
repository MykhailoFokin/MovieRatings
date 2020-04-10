package solvve.course.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import solvve.course.domain.Country;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CountryRepository extends CrudRepository<Country, UUID>, CountryRepositoryCustom {

    @Query("select l from Country l join l.movies m "
            + " where m.id = :movieId ")
    Optional<List<Country>> findCountriesByMovieId(UUID movieId);
}
