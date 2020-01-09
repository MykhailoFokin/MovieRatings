package solvve.course.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import solvve.course.domain.UserTypes;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
@Sql(statements = "delete from user_types", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@ActiveProfiles("test")
public class UserTypesRepositoryTest {

    @Autowired
    private UserTypesRepository userTypesRepository;

    @Test
    public void testSave() {
        UserTypes r = new UserTypes();
        r = userTypesRepository.save(r);
        assertNotNull(r.getId());
        assertTrue(userTypesRepository.findById(r.getId()).isPresent());
    }
}
