package solvve.course.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import solvve.course.domain.Crew;
import solvve.course.dto.CrewFilter;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

public class CrewRepositoryCustomImpl implements CrewRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<Crew> findByFilter(CrewFilter filter, Pageable pageable) {
        StringBuilder sb = new StringBuilder();
        sb.append("select c from Crew c where 1=1");
        Query query = createQueryApplyingFilter(filter, pageable.getSort(), sb);
        applyPaging(query, pageable);

        List<Crew> data = query.getResultList();

        long count = getCountOfCrews(filter);
        return new PageImpl<>(data, pageable, count);
    }

    private long getCountOfCrews(CrewFilter filter) {
        StringBuilder sb = new StringBuilder();
        sb.append("select count(c) from Crew c where 1=1");
        Query query = createQueryApplyingFilter(filter, null, sb);
        return ((Number) query.getResultList().get(0)).longValue();
    }

    private Query createQueryApplyingFilter(CrewFilter filter, Sort sort, StringBuilder sb) {
        if (filter.getPersonId() != null) {
            sb.append(" and c.person.id = :personId");
        }
        if (filter.getMovieId() != null) {
            sb.append(" and c.movie.id = :movieId");
        }
        if (filter.getCrewTypeId() != null) {
            sb.append(" and c.crewType.id = :crewTypeId");
        }
        if (filter.getDescription() != null) {
            sb.append(" and c.description = :description");
        }
        if (filter.getPersonIds() != null) {
            sb.append(" and c.person.id in (:personIds)");
        }
        if (filter.getMovieIds() != null) {
            sb.append(" and c.movie.id in (:movieIds)");
        }
        if (filter.getCrewTypesIds() != null) {
            sb.append(" and c.crewType.id in (:crewTypeIds)");
        }
        if (filter.getDescriptions() != null) {
            sb.append(" and c.description in (:descriptions)");
        }
        if (sort != null && sort.isSorted()) {
            sb.append(" order by ");
            for (Sort.Order order : sort.toList()) {
                sb.append("v.").append(order.getProperty()).append(" ").append(order.getDirection());
            }
        }

        Query query = entityManager.createQuery(sb.toString());

        if (filter.getPersonId() != null) {
            query.setParameter("personId", filter.getPersonId());
        }
        if (filter.getMovieId() != null) {
            query.setParameter("movieId", filter.getMovieId());
        }
        if (filter.getCrewTypeId() != null) {
            query.setParameter("crewTypeId", filter.getCrewTypeId());
        }
        if (filter.getDescription() != null) {
            query.setParameter("description", filter.getDescription());
        }
        if (filter.getPersonIds() != null) {
            query.setParameter("personIds", filter.getPersonIds());
        }
        if (filter.getMovieIds() != null) {
            query.setParameter("movieIds", filter.getMovieIds());
        }
        if (filter.getCrewTypesIds() != null) {
            query.setParameter("crewTypeIds", filter.getCrewTypesIds());
        }
        if (filter.getDescriptions() != null) {
            query.setParameter("descriptions", filter.getDescriptions());
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
