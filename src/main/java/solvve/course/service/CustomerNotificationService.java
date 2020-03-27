package solvve.course.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import solvve.course.domain.Notification;
import solvve.course.repository.NotificationRepository;

import java.util.UUID;

@Service
public class CustomerNotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    public Notification notifyOnVisitStatusChangedToFinished(UUID visitId) {

        Notification notification = new Notification();
        notification.setEntityIdentifier(visitId.toString());
        notification.setMessage("Your visit status changed to FINISHED");
        notification.setSourceEntity("visit");
        notification = notificationRepository.save(notification);
        return notification;
    }
}
