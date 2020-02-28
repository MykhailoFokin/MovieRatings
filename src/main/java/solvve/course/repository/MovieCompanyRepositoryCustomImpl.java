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
        if (filter.getCompanyDetailsId() != null) {
            sb.append(" and m.companyDetails.id = :companyId");
        }
        if (filter.getMovieProductionTypes() != null) {
            sb.append(" and m.movieProductionType in (:movieProductionTypes)");
        }
        TypedQuery<MovieCompany> query = entityManager.createQuery(sb.toString(), MovieCompany.class);

        if (filter.getCompanyDetailsId() != null) {
            query.setParameter("companyId", filter.getCompanyDetailsId());
        }
        if (filter.getMovieProductionTypes() != null) {
            query.setParameter("movieProductionTypes", filter.getMovieProductionTypes());
        }

        return query.getResultList();
    }
}
