package solvve.course.service;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.github.database.rider.spring.api.DBRider;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import solvve.course.BaseTest;
import solvve.course.repository.RoleRepository;

import java.util.UUID;

@DBRider
public class RoleServiceDbRiderTest extends BaseTest {

    @Autowired
    private RoleService roleService;

    @Autowired
    private RoleRepository roleRepository;

    @Test
    @DataSet(value = "/datasets/testUpdateAverageRatingOfRole.xml", cleanAfter = true, skipCleaningFor = {"LANGUAGE"})
    @ExpectedDataSet(value = "/datasets/testUpdateAverageRatingOfRole_result.xml")
    public void testUpdateAverageRatingOfRole() {
        UUID roleId = UUID.fromString("c02c5981-7ced-4102-833c-c132986ccfff");
        roleService.updateAverageRatingOfRole(roleId);
    }
}
