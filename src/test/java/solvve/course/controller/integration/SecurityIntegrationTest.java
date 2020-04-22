package solvve.course.controller.integration;

import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Test;
import org.postgresql.shaded.com.ongres.scram.common.bouncycastle.base64.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import solvve.course.BaseTest;
import solvve.course.domain.PortalUser;
import solvve.course.domain.UserType;
import solvve.course.dto.PageResult;
import solvve.course.dto.VisitReadDTO;
import solvve.course.repository.PortalUserRepository;

import java.util.UUID;

@ActiveProfiles({"test", "integration-test"})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class SecurityIntegrationTest extends BaseTest {

    @Autowired
    private PortalUserRepository portalUserRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

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

        UserType userType = testObjectsFactory.createUserType();
        PortalUser portalUser = testObjectsFactory.generateFlatEntityWithoutId(PortalUser.class);
        portalUser.setEmail(email);
        portalUser.setEncodedPassword(passwordEncoder.encode(password));
        portalUser.setUserType(userType);
        portalUserRepository.save(portalUser);

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

        UserType userType = testObjectsFactory.createUserType();
        PortalUser portalUser = testObjectsFactory.generateFlatEntityWithoutId(PortalUser.class);
        portalUser.setEmail(email);
        portalUser.setEncodedPassword(passwordEncoder.encode(password));
        portalUser.setUserType(userType);
        portalUserRepository.save(portalUser);

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

    private String getBasicAuthorizationHeaderValue(String userName, String password) {
        return "Basic " + new String((Base64.encode(String.format("%s:%s", userName, password).getBytes())));
    }
}
