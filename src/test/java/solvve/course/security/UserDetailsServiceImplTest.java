package solvve.course.security;

import org.assertj.core.api.Assertions;
import org.assertj.core.util.IterableUtil;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.support.TransactionTemplate;
import solvve.course.BaseTest;
import solvve.course.domain.PortalUser;
import solvve.course.domain.UserGroupType;
import solvve.course.domain.UserRole;
import solvve.course.domain.UserType;
import solvve.course.repository.PortalUserRepository;
import solvve.course.repository.UserRoleRepository;
import solvve.course.utils.TestObjectsFactory;

import java.util.ArrayList;

public class UserDetailsServiceImplTest extends BaseTest {

    @Autowired
    private PortalUserRepository portalUserRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private TransactionTemplate transactionTemplate;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private TestObjectsFactory testObjectsFactory;

    @Test
    public void testLoadUserByUserName() {
        PortalUser portalUser = transactionTemplate.execute(status -> {
            UserType userType = testObjectsFactory.createUserType();
            PortalUser u = testObjectsFactory.generateFlatEntityWithoutId(PortalUser.class);
            u.setUserType(userType);
            u.setUserRoles(new ArrayList<>(IterableUtil.toCollection(userRoleRepository.findAll())));
            return portalUserRepository.save(u);
        });

        UserDetails userDetails = userDetailsService.loadUserByUsername(portalUser.getEmail());
        Assert.assertEquals(portalUser.getEmail(), userDetails.getUsername());
        Assert.assertEquals(portalUser.getEncodedPassword(), userDetails.getPassword());
        Assert.assertFalse(userDetails.getAuthorities().isEmpty());
        Assertions.assertThat(userDetails.getAuthorities())
                .extracting("authority").containsExactlyInAnyOrder(
                portalUser.getUserRoles().stream().map(ur -> ur.getUserGroupType().toString()).toArray());
    }

    @Test(expected = UsernameNotFoundException.class)
    public void testUserNotFound() {
        userDetailsService.loadUserByUsername("wrong name");
    }
}
