package solvve.course.security;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import solvve.course.BaseTest;
import solvve.course.domain.PortalUser;
import solvve.course.repository.PortalUserRepository;

public class CurrentUserValidatorTest extends BaseTest {

    @Autowired
    private PortalUserRepository portalUserRepository;

    @Autowired
    private CurrentUserValidator currentUserValidator;

    @MockBean
    private AuthenticationResolver authenticationResolver;

    @Test
    public void testIsCurrentUser() {
        PortalUser portalUser = testObjectsFactory.createPortalUser();

        Authentication authentication = new TestingAuthenticationToken(portalUser.getEmail(), null);
        Mockito.when(authenticationResolver.getCurrentAuthentication()).thenReturn(authentication);

        Assert.assertTrue(currentUserValidator.isCurrentUser(portalUser.getId()));
    }

    @Test
    public void testIsDifferentUser() {
        PortalUser portalUser1 = testObjectsFactory.createPortalUser();
        PortalUser portalUser2 = testObjectsFactory.createPortalUser();

        Authentication authentication = new TestingAuthenticationToken(portalUser1.getEmail(), null);
        Mockito.when(authenticationResolver.getCurrentAuthentication()).thenReturn(authentication);

        Assert.assertFalse(currentUserValidator.isCurrentUser(portalUser2.getId()));
    }
}
