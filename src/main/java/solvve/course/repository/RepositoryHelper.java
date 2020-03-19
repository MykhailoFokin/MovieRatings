package solvve.course.repository;

import org.springframework.stereotype.Component;
import solvve.course.domain.ModeratorTypoReviewStatusType;
import solvve.course.exception.EntityNotFoundException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.UUID;

@Component
public class RepositoryHelper {

    @PersistenceContext
    private EntityManager entityManager;

    public <E> E getReferenceIfExists(Class<E> entityClass, UUID id) {
        validateIFExists(entityClass, id);
        return entityManager.getReference(entityClass, id);
    }

    public <E> void validateIFExists(Class<E> entityClass, UUID id) {
        Query query = entityManager.createQuery("select count(e) from " + entityClass.getSimpleName()
                + " e where e.id = :id");
        query.setParameter("id", id);
        boolean exists = ((Number) query.getSingleResult()).intValue() > 0;
        if (!exists) {
            throw new EntityNotFoundException(entityClass, id);
        }
    }

    public <E> boolean validateIfExistsNotNewsUserReviewStatus(Class<E> entityClass, UUID id,
                                                               ModeratorTypoReviewStatusType moderatorTypoReviewStatusType) {
        Query query = entityManager.createQuery("select count(e) from " + entityClass.getSimpleName()
                + " e where e.id = :id and e.moderatorTypoReviewStatusType != :moderatorTypoReviewStatusType");
        query.setParameter("id", id);
        query.setParameter("moderatorTypoReviewStatusType", moderatorTypoReviewStatusType);
        return ((Number) query.getSingleResult()).intValue() > 0;
    }
}
