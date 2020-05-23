package solvve.course.repository;

import org.bitbucket.brunneng.qb.JpaQueryBuilder;
import org.bitbucket.brunneng.qb.SpringQueryBuilderUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import solvve.course.domain.CrewType;
import solvve.course.dto.CrewTypeFilter;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class CrewTypeRepositoryCustomImpl implements CrewTypeRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<CrewType> findByFilter(CrewTypeFilter filter, Pageable pageable) {
        JpaQueryBuilder qb = new JpaQueryBuilder(entityManager);
        qb.append("select c from CrewType c where 1=1");
        qb.append(" and c.name = :name", filter.getName());
        qb.appendIn(" and c.name in (:names)", filter.getNames());

        return SpringQueryBuilderUtils.loadPage(qb, pageable, "id");
    }
}
