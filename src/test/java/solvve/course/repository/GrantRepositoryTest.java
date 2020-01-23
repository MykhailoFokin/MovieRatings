package solvve.course.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import solvve.course.domain.Grant;
import solvve.course.domain.UserType;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
@Sql(statements = "delete from grant; delete from user_type;", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@ActiveProfiles("test")
public class GrantRepositoryTest {

    @Autowired
    private GrantRepository grantRepository;

    @Autowired
    private UserTypeRepository userTypeRepository;

    @Transactional
    @Test
    public void testSave() {
        UserType u = new UserType();
        u = userTypeRepository.save(u);
        Grant g = new Grant();
        g.setUserTypeId(u);
        g = grantRepository.save(g);
        assertNotNull(g.getId());
        assertTrue(grantRepository.findById(g.getId()).isPresent());
    }
}
