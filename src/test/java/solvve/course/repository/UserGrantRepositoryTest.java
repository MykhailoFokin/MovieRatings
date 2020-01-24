package solvve.course.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import solvve.course.domain.UserGrant;
import solvve.course.domain.UserType;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
@Sql(statements = "delete from user_grant; delete from user_type;", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@ActiveProfiles("test")
public class UserGrantRepositoryTest {

    @Autowired
    private UserGrantRepository userGrantRepository;

    @Autowired
    private UserTypeRepository userTypeRepository;

    @Transactional
    @Test
    public void testSave() {
        UserType u = new UserType();
        u = userTypeRepository.save(u);
        UserGrant g = new UserGrant();
        g.setUserTypeId(u);
        g = userGrantRepository.save(g);
        assertNotNull(g.getId());
        assertTrue(userGrantRepository.findById(g.getId()).isPresent());
    }
}
