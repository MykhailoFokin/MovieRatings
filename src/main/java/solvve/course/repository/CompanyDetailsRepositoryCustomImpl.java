package solvve.course.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import solvve.course.domain.CompanyDetails;
import solvve.course.dto.CompanyDetailsFilter;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

public class CompanyDetailsRepositoryCustomImpl implements CompanyDetailsRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<CompanyDetails> findByFilter(CompanyDetailsFilter filter, Pageable pageable) {
        StringBuilder sb = new StringBuilder();
        sb.append("select c from CompanyDetails c where 1=1");
        Query query = createQueryApplyingFilter(filter, pageable.getSort(), sb);
        applyPaging(query, pageable);

        List<CompanyDetails> data = query.getResultList();

        long count = getCountOfCompanyDetails(filter);
        return new PageImpl<>(data, pageable, count);
    }

    private long getCountOfCompanyDetails(CompanyDetailsFilter filter) {
        StringBuilder sb = new StringBuilder();
        sb.append("select count(c) from CompanyDetails c where 1=1");
        Query query = createQueryApplyingFilter(filter, null, sb);
        return ((Number) query.getResultList().get(0)).longValue();
    }

    private Query createQueryApplyingFilter(CompanyDetailsFilter filter, Sort sort, StringBuilder sb) {
        if (filter.getName() != null) {
            sb.append(" and c.name = :name");
        }
        if (filter.getYearOfFoundation() != null) {
            sb.append(" and c.yearOfFoundation = :yearOfFoundation");
        }
        if (filter.getYearsOfFoundation() != null) {
            sb.append(" and c.yearOfFoundation in (:yearsOfFoundation)");
        }
        if (sort != null && sort.isSorted()) {
            sb.append(" order by ");
            for (Sort.Order order : sort.toList()) {
                sb.append("c.").append(order.getProperty()).append(" ").append(order.getDirection());
            }
        }

        Query query = entityManager.createQuery(sb.toString());

        if (filter.getName() != null) {
            query.setParameter("name", filter.getName());
        }
        if (filter.getYearOfFoundation() != null) {
            query.setParameter("yearOfFoundation", filter.getYearOfFoundation());
        }
        if (filter.getYearsOfFoundation() != null) {
            query.setParameter("yearsOfFoundation", filter.getYearsOfFoundation());
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
