package solvve.course.repository;

import solvve.course.domain.CompanyDetails;
import solvve.course.dto.CompanyDetailsFilter;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

public class CompanyDetailsRepositoryCustomImpl implements CompanyDetailsRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<CompanyDetails> findByFilter(CompanyDetailsFilter filter) {
        StringBuilder sb = new StringBuilder();
        sb.append("select c from CompanyDetails c where 1=1");
        if (filter.getName() != null) {
            sb.append(" and c.name = :name");
        }
        if (filter.getYearOfFoundation() != null) {
            sb.append(" and c.yearOfFoundation = :yearOfFoundation");
        }
        if (filter.getYearsOfFoundation() != null) {
            sb.append(" and c.yearOfFoundation in (:yearsOfFoundation)");
        }
        TypedQuery<CompanyDetails> query = entityManager.createQuery(sb.toString(), CompanyDetails.class);

        if (filter.getName() != null) {
            query.setParameter("name", filter.getName());
        }
        if (filter.getYearOfFoundation() != null) {
            query.setParameter("yearOfFoundation", filter.getYearOfFoundation());
        }
        if (filter.getYearsOfFoundation() != null) {
            query.setParameter("yearsOfFoundation", filter.getYearsOfFoundation());
        }

        return query.getResultList();
    }
}
