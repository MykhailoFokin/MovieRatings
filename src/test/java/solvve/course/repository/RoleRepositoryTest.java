package solvve.course.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import solvve.course.domain.Crew;
import solvve.course.domain.Role;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
@Sql(statements = "delete from role", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@ActiveProfiles("test")
public class RoleRepositoryTest {

    @Autowired
    private CrewRepository crewRepository;

    @Test
    public void testSave() {
        Crew r = new Crew();
        r = crewRepository.save(r);
        assertNotNull(r.getId());
        assertTrue(crewRepository.findById(r.getId()).isPresent());
    }
}
