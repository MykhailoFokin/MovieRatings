package solvve.course.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import solvve.course.domain.Language;
import solvve.course.domain.LanguageType;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface LanguageRepository extends CrudRepository<Language, UUID> {

    @Query("select l from Language l join l.movies m "
            + " where m.id = :movieId ")
    Optional<List<Language>> findLanguagesByMovieId(UUID movieId);

    @Query("select l.id from Language l where l.name = :languageType ")
    UUID findMovieLanguageByType(LanguageType languageType);
}
