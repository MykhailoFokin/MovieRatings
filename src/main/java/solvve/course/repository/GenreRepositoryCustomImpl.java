package solvve.course.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import solvve.course.domain.Genre;
import solvve.course.dto.GenreFilter;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

public class GenreRepositoryCustomImpl implements GenreRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<Genre> findByFilter(GenreFilter filter, Pageable pageable) {
        StringBuilder sb = new StringBuilder();
        sb.append("select g from Genre g where 1=1");
        Query query = createQueryApplyingFilter(filter, pageable.getSort(), sb);
        applyPaging(query, pageable);

        List<Genre> data = query.getResultList();

        long count = getCountOfGenre(filter);
        return new PageImpl<>(data, pageable, count);
    }

    private long getCountOfGenre(GenreFilter filter) {
        StringBuilder sb = new StringBuilder();
        sb.append("select count(g) from Genre g where 1=1");
        Query query = createQueryApplyingFilter(filter, null, sb);
        return ((Number) query.getResultList().get(0)).longValue();
    }

    private Query createQueryApplyingFilter(GenreFilter filter, Sort sort, StringBuilder sb) {
        if (filter.getMovieId() != null) {
            sb.append(" and g.movie.id = :movieId");
        }
        if (filter.getGenres() != null) {
            sb.append(" and g.name in (:genres)");
        }
        if (sort != null && sort.isSorted()) {
            sb.append(" order by ");
            for (Sort.Order order : sort.toList()) {
                sb.append("g.").append(order.getProperty()).append(" ").append(order.getDirection());
            }
        }

        Query query = entityManager.createQuery(sb.toString());

        if (filter.getMovieId() != null) {
            query.setParameter("movieId", filter.getMovieId());
        }
        if (filter.getGenres() != null) {
            query.setParameter("genres", filter.getGenres());
        }

        return query;
    }

    private void applyPaging(Query query, Pageable pageable) {
        if (pageable.isPaged()) {
            query.setMaxResults(pageable.getPageSize());
            query.setFirstResult((int) pageable.getOffset());
        }
    }
}
