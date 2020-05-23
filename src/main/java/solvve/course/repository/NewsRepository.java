package solvve.course.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import solvve.course.domain.News;
import java.util.UUID;
import java.util.stream.Stream;

@Repository
public interface NewsRepository extends CrudRepository<News, UUID> {

    @Query("select n.id from News n")
    Stream<UUID> getIdsOfNews();
}
