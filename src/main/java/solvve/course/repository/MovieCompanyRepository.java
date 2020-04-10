package solvve.course.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import solvve.course.domain.MovieCompany;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MovieCompanyRepository extends CrudRepository<MovieCompany, UUID>, MovieCompanyRepositoryCustom {

    @Query("select mc from MovieCompany mc join mc.movies m "
            + " where m.id = :movieId ")
    Optional<List<MovieCompany>> findCompaniesByMovieId(UUID movieId);
}
