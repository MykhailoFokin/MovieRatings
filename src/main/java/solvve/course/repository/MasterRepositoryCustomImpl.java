package solvve.course.repository;

import solvve.course.domain.Master;
import solvve.course.dto.MasterFilter;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

public class MasterRepositoryCustomImpl implements MasterRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Master> findByFilter(MasterFilter filter) {
        StringBuilder sb = new StringBuilder();
        sb.append("select m from Master m where 1=1");
        if (filter.getName() != null) {
            sb.append(" and m.name = :name");
        }
        if (filter.getPhone() != null) {
            sb.append(" and m.phone = :phone");
        }
        if (filter.getAbout() != null) {
            sb.append(" and m.about = :about");
        }
        if (filter.getNames() != null) {
            sb.append(" and m.name in (:names)");
        }
        if (filter.getPhones() != null) {
            sb.append(" and m.phone in (:phones)");
        }
        if (filter.getAbouts() != null) {
            sb.append(" and m.about in (:abouts)");
        }
        TypedQuery<Master> query = entityManager.createQuery(sb.toString(), Master.class);

        if (filter.getName() != null) {
            query.setParameter("name", filter.getName());
        }
        if (filter.getPhone() != null) {
            query.setParameter("phone", filter.getPhone());
        }
        if (filter.getAbout() != null) {
            query.setParameter("about", filter.getAbout());
        }
        if (filter.getNames() != null) {
            query.setParameter("names", filter.getNames());
        }
        if (filter.getPhones() != null) {
            query.setParameter("phones", filter.getPhones());
        }
        if (filter.getAbouts() != null) {
            query.setParameter("abouts", filter.getAbouts());
        }

        return query.getResultList();
    }
}
