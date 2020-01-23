package solvve.course.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import solvve.course.domain.Grants;
import solvve.course.domain.UserTypes;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
@Sql(statements = "delete from grants; delete from user_types;", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@ActiveProfiles("test")
public class GrantsRepositoryTest {

    @Autowired
    private GrantsRepository grantsRepository;

    @Autowired
    private UserTypesRepository userTypesRepository;

    @Transactional
    @Test
    public void testSave() {
        UserTypes u = new UserTypes();
        u = userTypesRepository.save(u);
        Grants g = new Grants();
        g.setUserTypeId(u);
        g = grantsRepository.save(g);
        assertNotNull(g.getId());
        assertTrue(grantsRepository.findById(g.getId()).isPresent());
    }
}
