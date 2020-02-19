package solvve.course.repository;

import solvve.course.domain.Crew;
import solvve.course.dto.CrewFilter;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

public class CrewRepositoryCustomImpl implements CrewRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Crew> findByFilter(CrewFilter filter) {
        StringBuilder sb = new StringBuilder();
        sb.append("select c from Crew c where 1=1");
        if (filter.getPersonId() != null) {
            sb.append(" and c.personId.id = :personId");
        }
        if (filter.getMovieId() != null) {
            sb.append(" and c.movieId.id = :movieId");
        }
        if (filter.getCrewTypeId() != null) {
            sb.append(" and c.crewTypeId.id = :crewTypeId");
        }
        if (filter.getDescription() != null) {
            sb.append(" and c.description = :description");
        }
        if (filter.getPersonIds() != null) {
            sb.append(" and c.personId.id in (:personIds)");
        }
        if (filter.getMovieIds() != null) {
            sb.append(" and c.movieId.id in (:movieIds)");
        }
        if (filter.getCrewTypesIds() != null) {
            sb.append(" and c.crewTypeId.id in (:crewTypeIds)");
        }
        if (filter.getDescriptions() != null) {
            sb.append(" and c.description in (:descriptions)");
        }
        TypedQuery<Crew> query = entityManager.createQuery(sb.toString(), Crew.class);

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

        return query.getResultList();
    }
}
