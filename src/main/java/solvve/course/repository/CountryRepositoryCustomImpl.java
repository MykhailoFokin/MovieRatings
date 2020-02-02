package solvve.course.repository;

import solvve.course.domain.Country;
import solvve.course.dto.CountryFilter;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

public class CountryRepositoryCustomImpl implements CountryRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Country> findByFilter(CountryFilter filter) {
        StringBuilder sb = new StringBuilder();
        sb.append("select c from Country c where 1=1");
        if (filter.getNames() != null) {
            sb.append(" and c.name in (:names)");
        }
        TypedQuery<Country> query = entityManager.createQuery(sb.toString(), Country.class);

        if (filter.getNames() != null) {
            query.setParameter("names", filter.getNames());
        }

        return query.getResultList();
    }
}
