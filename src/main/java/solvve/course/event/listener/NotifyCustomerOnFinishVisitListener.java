package solvve.course.event.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import solvve.course.event.VisitStatusChangedEvent;
import solvve.course.service.CustomerNotificationService;

@Slf4j
@Component
public class NotifyCustomerOnFinishVisitListener {

    @Autowired
    private CustomerNotificationService customerNotificationService;

    @Async
    @EventListener(condition = "#event.newStatus == T(solvve.course.domain.VisitStatus).FINISHED")
    public void onEvent(VisitStatusChangedEvent event) {
        log.info("handling {}", event);
        customerNotificationService.notifyOnVisitStatusChangedToFinished(event.getVisitId());
    }
}
