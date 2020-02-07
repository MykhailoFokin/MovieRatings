package solvve.course.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import solvve.course.domain.Language;

import java.util.UUID;

@Repository
public interface LanguageRepository extends CrudRepository<Language, UUID> {
}
