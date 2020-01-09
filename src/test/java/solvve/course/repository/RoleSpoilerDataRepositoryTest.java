package solvve.course.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import solvve.course.domain.RoleSpoilerData;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
@Sql(statements = "delete from role_spoiler_data", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@ActiveProfiles("test")
public class RoleSpoilerDataRepositoryTest {

    @Autowired
    private RoleSpoilerDataRepository roleSpoilerDataRepository;

    @Test
    public void testSave() {
        RoleSpoilerData r = new RoleSpoilerData();
        r = roleSpoilerDataRepository.save(r);
        assertNotNull(r.getId());
        assertTrue(roleSpoilerDataRepository.findById(r.getId()).isPresent());
    }
}
