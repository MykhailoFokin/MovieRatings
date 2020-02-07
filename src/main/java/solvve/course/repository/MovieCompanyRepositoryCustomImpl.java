package solvve.course.repository;

import solvve.course.domain.MovieCompany;
import solvve.course.dto.MovieCompanyFilter;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

public class MovieCompanyRepositoryCustomImpl implements MovieCompanyRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<MovieCompany> findByFilter(MovieCompanyFilter filter) {
        StringBuilder sb = new StringBuilder();
        sb.append("select m from MovieCompany m where 1=1");
        if (filter.getCompanyId() != null) {
            sb.append(" and m.companyId.id = :companyId");
        }
        if (filter.getMovieProductionTypes() != null) {
            sb.append(" and m.movieProductionType in (:movieProductionTypes)");
        }
        TypedQuery<MovieCompany> query = entityManager.createQuery(sb.toString(), MovieCompany.class);

        if (filter.getCompanyId() != null) {
            query.setParameter("companyId", filter.getCompanyId());
        }
        if (filter.getMovieProductionTypes() != null) {
            query.setParameter("movieProductionTypes", filter.getMovieProductionTypes());
        }

        return query.getResultList();
    }
}
