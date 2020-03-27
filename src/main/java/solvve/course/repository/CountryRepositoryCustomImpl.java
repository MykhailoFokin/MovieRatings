package solvve.course.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import solvve.course.domain.Country;
import solvve.course.dto.CountryFilter;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

public class CountryRepositoryCustomImpl implements CountryRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<Country> findByFilter(CountryFilter filter, Pageable pageable) {
        StringBuilder sb = new StringBuilder();
        sb.append("select c from Country c where 1=1");
        Query query = createQueryApplyingFilter(filter, pageable.getSort(), sb);
        applyPaging(query, pageable);

        List<Country> data = query.getResultList();

        long count = getCountOfCountries(filter);
        return new PageImpl<>(data, pageable, count);
    }

    private long getCountOfCountries(CountryFilter filter) {
        StringBuilder sb = new StringBuilder();
        sb.append("select count(c) from Country c where 1=1");
        Query query = createQueryApplyingFilter(filter, null, sb);
        return ((Number) query.getResultList().get(0)).longValue();
    }

    private Query createQueryApplyingFilter(CountryFilter filter, Sort sort, StringBuilder sb) {
        if (filter.getNames() != null) {
            sb.append(" and c.name in (:names)");
        }
        if (sort != null && sort.isSorted()) {
            sb.append(" order by ");
            for (Sort.Order order : sort.toList()) {
                sb.append("c.").append(order.getProperty()).append(" ").append(order.getDirection());
            }
        }

        Query query = entityManager.createQuery(sb.toString());

        if (filter.getNames() != null) {
            query.setParameter("names", filter.getNames());
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
