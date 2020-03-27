package solvve.course.event.listener;

import org.assertj.core.api.Assertions;
import org.hibernate.LazyInitializationException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import solvve.course.domain.Notification;
import solvve.course.domain.VisitStatus;
import solvve.course.event.VisitStatusChangedEvent;
import solvve.course.repository.NotificationRepository;
import solvve.course.service.CustomerNotificationService;
import solvve.course.utils.TestObjectsFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;

import static org.mockito.ArgumentMatchers.any;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@Sql(statements = {"delete from notification"},
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class NotifyCustomerOnFinishVisitListenerTest {

    @MockBean
    private CustomerNotificationService customerNotificationService;

    @SpyBean
    private NotifyCustomerOnFinishVisitListener notifyCustomerOnFinishVisitListener;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private TestObjectsFactory testObjectsFactory;

    @Test
    public void testOnEvent() {
        VisitStatusChangedEvent event = new VisitStatusChangedEvent();
        event.setVisitId(UUID.randomUUID());
        event.setNewStatus(VisitStatus.FINISHED);
        applicationEventPublisher.publishEvent(event);

        Mockito.verify(notifyCustomerOnFinishVisitListener, Mockito.timeout(500)).onEvent(event);
        Mockito.verify(customerNotificationService, Mockito.timeout(500))
                .notifyOnVisitStatusChangedToFinished(event.getVisitId());
    }

    @Test
    public void testOnEventNotFinished() {
        for (VisitStatus visitStatus : VisitStatus.values()) {

            if (visitStatus == VisitStatus.FINISHED) {
                continue;
            }

            VisitStatusChangedEvent event = new VisitStatusChangedEvent();
            event.setVisitId(UUID.randomUUID());
            event.setNewStatus(visitStatus);
            applicationEventPublisher.publishEvent(event);

            Mockito.verify(notifyCustomerOnFinishVisitListener, Mockito.never()).onEvent(any());
            Mockito.verify(customerNotificationService, Mockito.never()).notifyOnVisitStatusChangedToFinished(any());
        }
    }

    @Test
    public void testOnEventAsync() throws InterruptedException {
        VisitStatusChangedEvent event = new VisitStatusChangedEvent();
        event.setVisitId(UUID.randomUUID());
        event.setNewStatus(VisitStatus.FINISHED);

        List<Integer> checkListIntegers = new ArrayList<>();
        CountDownLatch latch = new CountDownLatch(1);
        Mockito.doAnswer(invocationOnMock -> {
            Thread.sleep(500);
            checkListIntegers.add(2);
            latch.countDown();
            return null;
        }).when(customerNotificationService).notifyOnVisitStatusChangedToFinished(event.getVisitId());

        applicationEventPublisher.publishEvent(event);
        checkListIntegers.add(1);

        latch.await();
        Mockito.verify(notifyCustomerOnFinishVisitListener).onEvent(event);
        Mockito.verify(customerNotificationService).notifyOnVisitStatusChangedToFinished(event.getVisitId());
        Assert.assertEquals(Arrays.asList(1, 2), checkListIntegers);
    }
}
