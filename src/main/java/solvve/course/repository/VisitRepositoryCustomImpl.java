package solvve.course.repository;

import org.bitbucket.brunneng.qb.JpaQueryBuilder;
import org.bitbucket.brunneng.qb.SpringQueryBuilderUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import solvve.course.domain.Visit;
import solvve.course.dto.VisitFilter;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class VisitRepositoryCustomImpl implements VisitRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<Visit> findByFilter(VisitFilter filter, Pageable pageable) {
        JpaQueryBuilder qb = new JpaQueryBuilder(entityManager);

        qb.append("select v from Visit v where 1=1");
        qb.append("and v.portalUser.id = :portalUserId", filter.getPortalUserId());
        qb.append("and v.status in :statuses", filter.getStatuses());
        qb.append("and v.startAt >= :startAtFrom", filter.getStartAtFrom());
        qb.append("and v.startAt < :startAtTo", filter.getStartAtTo());

        return SpringQueryBuilderUtils.loadPage(qb, pageable, "id");
    }
}
