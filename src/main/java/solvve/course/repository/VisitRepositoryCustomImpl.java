package solvve.course.repository;

import solvve.course.domain.Visit;
import solvve.course.dto.VisitFilter;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

public class VisitRepositoryCustomImpl implements VisitRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Visit> findByFilter(VisitFilter filter) {
        StringBuilder sb = new StringBuilder();
        sb.append("select v from Visit v where 1=1");
        if (filter.getUserId() != null) {
            sb.append(" and v.userId.id = :userId");
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
        TypedQuery<Visit> query = entityManager.createQuery(sb.toString(), Visit.class);

        if (filter.getUserId() != null) {
            query.setParameter("userId", filter.getUserId());
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

        return query.getResultList();
    }
}
