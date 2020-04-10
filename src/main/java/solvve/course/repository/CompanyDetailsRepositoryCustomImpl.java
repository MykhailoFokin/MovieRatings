package solvve.course.repository;

import org.bitbucket.brunneng.qb.JpaQueryBuilder;
import org.bitbucket.brunneng.qb.SpringQueryBuilderUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import solvve.course.domain.CompanyDetails;
import solvve.course.dto.CompanyDetailsFilter;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class CompanyDetailsRepositoryCustomImpl implements CompanyDetailsRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<CompanyDetails> findByFilter(CompanyDetailsFilter filter, Pageable pageable) {
        JpaQueryBuilder qb = new JpaQueryBuilder(entityManager);

        qb.append("select c from CompanyDetails c where 1=1");
        qb.append("and c.name = :name", filter.getName());
        qb.append("and c.yearOfFoundation = :yearOfFoundation", filter.getYearOfFoundation());
        qb.appendIn("and c.yearOfFoundation in (:yearsOfFoundation)", filter.getYearsOfFoundation());

        return SpringQueryBuilderUtils.loadPage(qb, pageable, "id");
    }
}
