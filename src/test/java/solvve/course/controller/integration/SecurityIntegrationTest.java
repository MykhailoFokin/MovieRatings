package solvve.course.controller.integration;

import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Test;
import org.postgresql.shaded.com.ongres.scram.common.bouncycastle.base64.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import solvve.course.BaseTest;
import solvve.course.domain.*;
import solvve.course.dto.*;
import solvve.course.repository.PortalUserRepository;
import solvve.course.repository.UserRoleRepository;
import solvve.course.repository.VisitRepository;

import java.util.List;
import java.util.UUID;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@ActiveProfiles({"test", "integration-test"})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class SecurityIntegrationTest extends BaseTest {

    @Autowired
    private PortalUserRepository portalUserRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private VisitRepository visitRepository;

    @Test
    public void testHealthNoSecurity() {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Void> response = restTemplate.getForEntity("http://localhost:8080/health", Void.class);
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testGetVisitsNoSecurity() {
        RestTemplate restTemplate = new RestTemplate();
        Assertions.assertThatThrownBy(() -> restTemplate.exchange(
                "http://localhost:8080/api/v1/visits", HttpMethod.GET, HttpEntity.EMPTY,
                new ParameterizedTypeReference<PageResult<VisitReadDTO>>() {}))
                .isInstanceOf(HttpClientErrorException.class).extracting("statusCode")
                .isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    public void testGetVisits() {
        String email = "test@gmail.com";
        String password = "pass123";

        PortalUser portalUser = createPortalUser(email, password, UserGroupType.ADMIN);

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", getBasicAuthorizationHeaderValue(email, password));
        HttpEntity<?> httpEntity = new HttpEntity<>(headers);

        ResponseEntity<PageResult<VisitReadDTO>> response = restTemplate.exchange(
                "http://localhost:8080/api/v1/visits", HttpMethod.GET, httpEntity,
                new ParameterizedTypeReference<PageResult<VisitReadDTO>>() {});
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testGetVisitsWrongPassword() {
        String email = "test@gmail.com";
        String password = "pass123";

        UserType userType = testObjectsFactory.createUserType();
        PortalUser portalUser = testObjectsFactory.generateFlatEntityWithoutId(PortalUser.class);
        portalUser.setEmail(email);
        portalUser.setEncodedPassword(passwordEncoder.encode(password));
        portalUser.setUserType(userType);
        portalUserRepository.save(portalUser);

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", getBasicAuthorizationHeaderValue(email, "wrong pass"));
        HttpEntity<?> httpEntity = new HttpEntity<>(headers);

        Assertions.assertThatThrownBy(() -> restTemplate.exchange(
                "http://localhost:8080/api/v1/visits", HttpMethod.GET, httpEntity,
                new ParameterizedTypeReference<PageResult<VisitReadDTO>>() {}))
                .isInstanceOf(HttpClientErrorException.class).extracting("statusCode")
                .isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    public void testGetVisitsWrongUser() {
        String email = "test@gmail.com";
        String password = "pass123";

        UserType userType = testObjectsFactory.createUserType();
        PortalUser portalUser = testObjectsFactory.generateFlatEntityWithoutId(PortalUser.class);
        portalUser.setEmail(email);
        portalUser.setEncodedPassword(passwordEncoder.encode(password));
        portalUser.setUserType(userType);
        portalUserRepository.save(portalUser);

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", getBasicAuthorizationHeaderValue("wrong user", password));
        HttpEntity<?> httpEntity = new HttpEntity<>(headers);

        Assertions.assertThatThrownBy(() -> restTemplate.exchange(
                "http://localhost:8080/api/v1/visits", HttpMethod.GET, httpEntity,
                new ParameterizedTypeReference<PageResult<VisitReadDTO>>() {}))
                .isInstanceOf(HttpClientErrorException.class).extracting("statusCode")
                .isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    public void testGetVisitsNoSession() {
        String email = "test@gmail.com";
        String password = "pass123";

        PortalUser portalUser = createPortalUser(email, password, UserGroupType.ADMIN);

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", getBasicAuthorizationHeaderValue(email, password));
        HttpEntity<?> httpEntity = new HttpEntity<>(headers);

        ResponseEntity<PageResult<VisitReadDTO>> response = restTemplate.exchange(
                "http://localhost:8080/api/v1/visits", HttpMethod.GET, httpEntity,
                new ParameterizedTypeReference<PageResult<VisitReadDTO>>() {});
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());

        Assert.assertNull(response.getHeaders().get("Set-Cookie"));
    }

    @Test
    public void testGetMovieRecommendationsNoRoles() {
        String email = "test@gmail.com";
        String password = "pass123";

        UserType userType = testObjectsFactory.createUserType();
        PortalUser portalUser = testObjectsFactory.generateFlatEntityWithoutId(PortalUser.class);
        portalUser.setEmail(email);
        portalUser.setEncodedPassword(passwordEncoder.encode(password));
        portalUser.setUserType(userType);
        portalUser = portalUserRepository.save(portalUser);
        UUID portalUserId = portalUser.getId();

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", getBasicAuthorizationHeaderValue(email, password));
        HttpEntity<?> httpEntity = new HttpEntity<>(headers);

        Assertions.assertThatThrownBy(() -> restTemplate.exchange(
                "http://localhost:8080//api/v1/portal-user/" + portalUserId + "/movies/recommendations",
                HttpMethod.GET, httpEntity, new ParameterizedTypeReference<List<MovieReadDTO>>() {}))
                .isInstanceOf(HttpClientErrorException.class).extracting("statusCode").isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    public void testGetVisitAdmin() {
        String email = "test@gmail.com";
        String password = "pass123";

        PortalUser portalUser = createPortalUser(email, password, UserGroupType.ADMIN);
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", getBasicAuthorizationHeaderValue(email, password));
        HttpEntity<?> httpEntity = new HttpEntity<>(headers);

        ResponseEntity<PageResult<VisitReadDTO>> response = restTemplate.exchange(
                "http://localhost:8080//api/v1/visits/",
                HttpMethod.GET, httpEntity, new ParameterizedTypeReference<PageResult<VisitReadDTO>>() {});
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testGetVisitContentManager() {
        String email = "test@gmail.com";
        String password = "pass123";

        PortalUser portalUser = createPortalUser(email, password, UserGroupType.CONTENTMANAGER);
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", getBasicAuthorizationHeaderValue(email, password));
        HttpEntity<?> httpEntity = new HttpEntity<>(headers);

        ResponseEntity<PageResult<VisitReadDTO>> response = restTemplate.exchange(
                "http://localhost:8080//api/v1/visits/",
                HttpMethod.GET, httpEntity, new ParameterizedTypeReference<PageResult<VisitReadDTO>>() {});
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testGetMovieSpoilerDataModerator() {
        String email = "test@gmail.com";
        String password = "pass123";

        PortalUser portalUser = createPortalUser(email, password, UserGroupType.MODERATOR);
        RestTemplate restTemplate = new RestTemplate();

        Movie movie = testObjectsFactory.createMovie();
        MovieReview movieReview = testObjectsFactory.createMovieReview(portalUser, movie);
        MovieSpoilerData movieSpoilerData = testObjectsFactory.createMovieSpoilerData(movieReview);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", getBasicAuthorizationHeaderValue(email, password));
        HttpEntity<?> httpEntity = new HttpEntity<>(headers);

        ResponseEntity<MovieSpoilerDataReadDTO> response = restTemplate.exchange(
                "http://localhost:8080//api/v1/moviespoilerdata/" + movieSpoilerData.getId(),
                HttpMethod.GET, httpEntity, new ParameterizedTypeReference<MovieSpoilerDataReadDTO>() {});
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testGetMovieLeaderBoardGuest() {
        String email = "test@gmail.com";
        String password = "pass123";

        PortalUser portalUser = createPortalUser(email, password, UserGroupType.GUEST);
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", getBasicAuthorizationHeaderValue(email, password));
        HttpEntity<?> httpEntity = new HttpEntity<>(headers);

        ResponseEntity<List<MovieInLeaderBoardReadDTO>> response = restTemplate.exchange(
                "http://localhost:8080//api/v1/movies/leader-board",
                HttpMethod.GET, httpEntity, new ParameterizedTypeReference<List<MovieInLeaderBoardReadDTO>>() {});
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testGetMovieRecommendationsWrongUser() {
        String password = "pass123";

        PortalUser portalUser1 = createPortalUser("test1@gmail.com", "123", UserGroupType.USER);
        PortalUser portalUser2 = createPortalUser("test2@gmail.com", password, UserGroupType.USER);

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", getBasicAuthorizationHeaderValue(portalUser2.getEmail(), password));
        HttpEntity<?> httpEntity = new HttpEntity<>(headers);

        Assertions.assertThatThrownBy(() -> restTemplate.exchange(
                "http://localhost:8080//api/v1/portal-user/" + portalUser1.getId() + "/movies/recommendations",
                HttpMethod.GET, httpEntity, new ParameterizedTypeReference<List<MovieReadDTO>>() {}))
                .isInstanceOf(HttpClientErrorException.class).extracting("statusCode").isEqualTo(HttpStatus
                .FORBIDDEN);
    }

    private String getBasicAuthorizationHeaderValue(String userName, String password) {
        return "Basic " + new String((Base64.encode(String.format("%s:%s", userName, password).getBytes())));
    }

    private PortalUser createPortalUser(String email, String password, UserGroupType userGroupType) {
        UserRole userRole = userRoleRepository.findByUserGroupType(userGroupType);

        UserType userType = testObjectsFactory.createUserType();
        PortalUser portalUser = testObjectsFactory.generateFlatEntityWithoutId(PortalUser.class);
        portalUser.setEmail(email);
        portalUser.setEncodedPassword(passwordEncoder.encode(password));
        portalUser.setUserType(userType);
        portalUser.getUserRoles().add(userRole);
        return portalUserRepository.save(portalUser);
    }
}
