package solvve.course.repository;

import solvve.course.domain.CrewType;
import solvve.course.dto.CrewTypeFilter;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

public class CrewTypeRepositoryCustomImpl implements CrewTypeRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<CrewType> findByFilter(CrewTypeFilter filter) {
        StringBuilder sb = new StringBuilder();
        sb.append("select c from CrewType c where 1=1");
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
        TypedQuery<CrewType> query = entityManager.createQuery(sb.toString(), CrewType.class);

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

        return query.getResultList();
    }
}
