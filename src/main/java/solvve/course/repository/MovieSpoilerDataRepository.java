package solvve.course.repository;

import org.springframework.data.repository.CrudRepository;
import solvve.course.domain.MovieSpoilerData;

import java.util.UUID;

public interface MovieSpoilerDataRepository extends CrudRepository<MovieSpoilerData, UUID> {
}
