package solvve.course.repository;

import org.bitbucket.brunneng.qb.JpaQueryBuilder;
import org.bitbucket.brunneng.qb.SpringQueryBuilderUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import solvve.course.domain.Crew;
import solvve.course.dto.CrewFilter;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class CrewRepositoryCustomImpl implements CrewRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<Crew> findByFilter(CrewFilter filter, Pageable pageable) {
        JpaQueryBuilder qb = new JpaQueryBuilder(entityManager);

        qb.append("select c from Crew c where 1=1");
        qb.append(" and c.person.id = :personId", filter.getPersonId());
        qb.append(" and c.movie.id = :movieId", filter.getMovieId());
        qb.append(" and c.crewType.id = :crewTypeId", filter.getCrewTypeId());
        qb.append(" and c.description = :description", filter.getDescription());
        qb.appendIn(" and c.person.id in (:personIds)", filter.getPersonIds());
        qb.appendIn(" and c.movie.id in (:movieIds)", filter.getMovieIds());
        qb.appendIn(" and c.crewType.id in (:crewTypeIds)", filter.getCrewTypesIds());
        qb.appendIn(" and c.description in (:descriptions)", filter.getDescriptions());

        return SpringQueryBuilderUtils.loadPage(qb, pageable, "id");
    }
}
