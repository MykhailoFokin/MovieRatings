package solvve.course.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import solvve.course.domain.Notification;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface NotificationRepository extends CrudRepository<Notification, UUID> {

    Optional<Notification> findByEntityIdentifierAndSourceEntity(String entityIdentifier, String sourceEntity);
}
