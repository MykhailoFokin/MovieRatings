package solvve.course.repository;

import org.bitbucket.brunneng.qb.JpaQueryBuilder;
import org.bitbucket.brunneng.qb.SpringQueryBuilderUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import solvve.course.domain.Country;
import solvve.course.dto.CountryFilter;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class CountryRepositoryCustomImpl implements CountryRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<Country> findByFilter(CountryFilter filter, Pageable pageable) {
        JpaQueryBuilder qb = new JpaQueryBuilder(entityManager);
        qb.append("select c from Country c where 1=1");
        qb.appendIn(" and c.name in (:names)", filter.getNames());

        return SpringQueryBuilderUtils.loadPage(qb, pageable, "id");
    }
}
