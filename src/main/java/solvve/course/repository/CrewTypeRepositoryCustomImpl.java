package solvve.course.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import solvve.course.domain.CrewType;
import solvve.course.dto.CrewTypeFilter;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

public class CrewTypeRepositoryCustomImpl implements CrewTypeRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<CrewType> findByFilter(CrewTypeFilter filter, Pageable pageable) {
        StringBuilder sb = new StringBuilder();
        sb.append("select c from CrewType c where 1=1");
        Query query = createQueryApplyingFilter(filter, pageable.getSort(), sb);
        applyPaging(query, pageable);

        List<CrewType> data = query.getResultList();

        long count = getCountOfCrewTypes(filter);
        return new PageImpl<>(data, pageable, count);
    }

    private long getCountOfCrewTypes(CrewTypeFilter filter) {
        StringBuilder sb = new StringBuilder();
        sb.append("select count(c) from CrewType c where 1=1");
        Query query = createQueryApplyingFilter(filter, null, sb);
        return ((Number) query.getResultList().get(0)).longValue();
    }

    private Query createQueryApplyingFilter(CrewTypeFilter filter, Sort sort, StringBuilder sb) {
        if (filter.getName() != null) {
            sb.append(" and c.name = :name");
        }
        if (filter.getCrewId() != null) {
            sb.append(" and c.crew.id = :crewId");
        }
        if (filter.getNames() != null) {
            sb.append(" and c.name in (:names)");
        }
        if (filter.getCrewIds() != null) {
            sb.append(" and c.crew.id in (:crewIds)");
        }
        if (sort != null && sort.isSorted()) {
            sb.append(" order by ");
            for (Sort.Order order : sort.toList()) {
                sb.append("v.").append(order.getProperty()).append(" ").append(order.getDirection());
            }
        }

        Query query = entityManager.createQuery(sb.toString());

        if (filter.getName() != null) {
            query.setParameter("name", filter.getName());
        }
        if (filter.getCrewId() != null) {
            query.setParameter("crewId", filter.getCrewId());
        }
        if (filter.getNames() != null) {
            query.setParameter("names", filter.getNames());
        }
        if (filter.getCrewIds() != null) {
            query.setParameter("crewIds", filter.getCrewIds());
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
