package solvve.course.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import solvve.course.domain.MovieCompany;
import solvve.course.dto.MovieCompanyFilter;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

public class MovieCompanyRepositoryCustomImpl implements MovieCompanyRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<MovieCompany> findByFilter(MovieCompanyFilter filter, Pageable pageable) {
        StringBuilder sb = new StringBuilder();
        sb.append("select m from MovieCompany m where 1=1");
        Query query = createQueryApplyingFilter(filter, pageable.getSort(), sb);
        applyPaging(query, pageable);

        List<MovieCompany> data = query.getResultList();

        long count = getCountOfMovieCompanies(filter);
        return new PageImpl<>(data, pageable, count);
    }

    private long getCountOfMovieCompanies(MovieCompanyFilter filter) {
        StringBuilder sb = new StringBuilder();
        sb.append("select count(m) from MovieCompany m where 1=1");
        Query query = createQueryApplyingFilter(filter, null, sb);
        return ((Number) query.getResultList().get(0)).longValue();
    }

    private Query createQueryApplyingFilter(MovieCompanyFilter filter, Sort sort, StringBuilder sb) {
        if (filter.getCompanyDetailsId() != null) {
            sb.append(" and m.companyDetails.id = :companyId");
        }
        if (filter.getMovieProductionTypes() != null) {
            sb.append(" and m.movieProductionType in (:movieProductionTypes)");
        }
        if (sort != null && sort.isSorted()) {
            sb.append(" order by ");
            for (Sort.Order order : sort.toList()) {
                sb.append("m.").append(order.getProperty()).append(" ").append(order.getDirection());
            }
        }

        Query query = entityManager.createQuery(sb.toString());

        if (filter.getCompanyDetailsId() != null) {
            query.setParameter("companyId", filter.getCompanyDetailsId());
        }
        if (filter.getMovieProductionTypes() != null) {
            query.setParameter("movieProductionTypes", filter.getMovieProductionTypes());
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
