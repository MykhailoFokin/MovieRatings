package solvve.course.repository;

import org.bitbucket.brunneng.qb.JpaQueryBuilder;
import org.bitbucket.brunneng.qb.SpringQueryBuilderUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import solvve.course.domain.Genre;
import solvve.course.dto.GenreFilter;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class GenreRepositoryCustomImpl implements GenreRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<Genre> findByFilter(GenreFilter filter, Pageable pageable) {
        JpaQueryBuilder qb = new JpaQueryBuilder(entityManager);
        qb.append("select g from Genre g where 1=1");
        qb.append(" and g.movie.id = :movieId", filter.getMovieId());
        qb.appendIn(" and g.name in (:genres)", filter.getGenres());

        return SpringQueryBuilderUtils.loadPage(qb, pageable, "id");
    }
}
