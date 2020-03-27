package solvve.course.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import solvve.course.domain.Visit;
import solvve.course.dto.VisitFilter;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

public class VisitRepositoryCustomImpl implements VisitRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<Visit> findByFilter(VisitFilter filter, Pageable pageable) {
        StringBuilder sb = new StringBuilder();
        sb.append("select v from Visit v where 1=1");
        Query query = createQueryApplyingFilter(filter, pageable.getSort(), sb);
        applyPaging(query, pageable);

        List<Visit> data = query.getResultList();

        long count = getCountOfVisits(filter);
        return new PageImpl<>(data, pageable, count);
    }

    private long getCountOfVisits(VisitFilter filter) {
        StringBuilder sb = new StringBuilder();
        sb.append("select count(v) from Visit v where 1=1");
        Query query = createQueryApplyingFilter(filter, null, sb);
        return ((Number) query.getResultList().get(0)).longValue();
    }

    private Query createQueryApplyingFilter(VisitFilter filter, Sort sort, StringBuilder sb) {
        if (filter.getPortalUserId() != null) {
            sb.append(" and v.portalUser.id = :portalUserId");
        }
        if (filter.getStatuses() != null) {
            sb.append(" and v.status in (:statuses)");
        }
        if (filter.getStartAtFrom() != null) {
            sb.append(" and v.startAt >= (:startAtFrom)");
        }
        if (filter.getStartAtTo() != null) {
            sb.append(" and v.startAt < (:startAtTo)");
        }
        if (sort != null && sort.isSorted()) {
            sb.append(" order by ");
            for (Sort.Order order : sort.toList()) {
                sb.append("v.").append(order.getProperty()).append(" ").append(order.getDirection());
            }
        }

        Query query = entityManager.createQuery(sb.toString());

        if (filter.getPortalUserId() != null) {
            query.setParameter("portalUserId", filter.getPortalUserId());
        }
        if (filter.getStatuses() != null) {
            query.setParameter("statuses", filter.getStatuses());
        }
        if (filter.getStartAtFrom() != null) {
            query.setParameter("startAtFrom", filter.getStartAtFrom());
        }
        if (filter.getStartAtTo() != null) {
            query.setParameter("startAtTo", filter.getStartAtTo());
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
