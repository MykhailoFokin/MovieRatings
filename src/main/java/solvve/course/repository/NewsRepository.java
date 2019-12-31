package solvve.course.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import solvve.course.domain.News;
import java.util.UUID;

@Repository
public interface NewsRepository extends CrudRepository<News, UUID> {
}
