package solvve.course.repository;

import org.bitbucket.brunneng.qb.JpaQueryBuilder;
import org.bitbucket.brunneng.qb.SpringQueryBuilderUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import solvve.course.domain.MovieCompany;
import solvve.course.dto.MovieCompanyFilter;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class MovieCompanyRepositoryCustomImpl implements MovieCompanyRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<MovieCompany> findByFilter(MovieCompanyFilter filter, Pageable pageable) {
        JpaQueryBuilder qb = new JpaQueryBuilder(entityManager);
        qb.append("select m from MovieCompany m where 1=1");
        qb.append(" and m.companyDetails.id = :companyId", filter.getCompanyDetailsId());
        qb.appendIn(" and m.movieProductionType in (:movieProductionTypes)", filter.getMovieProductionTypes());

        return SpringQueryBuilderUtils.loadPage(qb, pageable, "id");
    }
}
