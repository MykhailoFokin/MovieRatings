package solvve.course.job;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import solvve.course.repository.PersonRepository;
import solvve.course.service.PersonService;

@Slf4j
@Component
public class UpdateAverageRatingOfRolesForPersonJob {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private PersonService personService;

    @Transactional(readOnly = true)
    @Scheduled(cron = "${update.average.rating.of.roles.for.person.job.cron}")
    public void updateAverageRatingOfRolesForPersons() {
        log.info("Job started");

        personRepository.getIdsOfPersons().forEach(personId -> {
            try {
                personService.updateAverageRatingOfRoleForPerson(personId);
            }
            catch (Exception e) {
                log.error("Failed to update average rating of roles for Person: {}", personId, e);
            }
        });

        log.info("Job finished");
    }
}
