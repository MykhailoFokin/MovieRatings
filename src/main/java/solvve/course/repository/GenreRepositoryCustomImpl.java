package solvve.course.repository;

import solvve.course.domain.Genre;
import solvve.course.dto.GenreFilter;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

public class GenreRepositoryCustomImpl implements GenreRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Genre> findByFilter(GenreFilter filter) {
        StringBuilder sb = new StringBuilder();
        sb.append("select g from Genre g where 1=1");
        if (filter.getMovieId() != null) {
            sb.append(" and g.movieId.id = :movieId");
        }
        if (filter.getGenres() != null) {
            sb.append(" and g.name in (:genres)");
        }
        TypedQuery<Genre> query = entityManager.createQuery(sb.toString(), Genre.class);

        if (filter.getMovieId() != null) {
            query.setParameter("movieId", filter.getMovieId());
        }
        if (filter.getGenres() != null) {
            query.setParameter("genres", filter.getGenres());
        }

        return query.getResultList();
    }
}
