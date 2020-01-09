package solvve.course.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import solvve.course.domain.CrewType;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
@Sql(statements = "delete from crew_type", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@ActiveProfiles("test")
public class CrewTypeRepositoryTest {

    @Autowired
    private CrewTypeRepository crewTypeRepository;

    @Test
    public void testSave() {
        CrewType r = new CrewType();
        r = crewTypeRepository.save(r);
        assertNotNull(r.getId());
        assertTrue(crewTypeRepository.findById(r.getId()).isPresent());
    }
}
