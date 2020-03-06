package solvve.course.job;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import solvve.course.repository.RoleRepository;
import solvve.course.service.RoleService;

@Slf4j
@Component
public class UpdateAverageRatingOfRolesJob {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private RoleService roleService;

    @Transactional(readOnly = true)
    @Scheduled(cron = "${update.average.rating.of.roles.job.cron}")
    public void updateAverageRatingOfRoles() {
        log.info("Job started");

        roleRepository.getIdsOfRoles().forEach(roleId -> {
            try {
                roleService.updateAverageRatingOfRole(roleId);
            }
            catch (Exception e) {
                log.error("Failed to update average rating for role: {}", roleId, e);
            }
        });

        log.info("Job finished");
    }
}
