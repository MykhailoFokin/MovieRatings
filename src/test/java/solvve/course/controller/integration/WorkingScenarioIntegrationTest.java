package solvve.course.controller.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Test;
import org.postgresql.shaded.com.ongres.scram.common.bouncycastle.base64.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import solvve.course.BaseTest;
import solvve.course.domain.*;
import solvve.course.dto.*;
import solvve.course.repository.PortalUserRepository;
import solvve.course.utils.TestObjectsFactory;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

@ActiveProfiles({"test", "integration-test"})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class WorkingScenarioIntegrationTest extends BaseTest {

    @Autowired
    private PortalUserRepository portalUserRepository;

    @Autowired
    private TestObjectsFactory testObjectsFactory;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testWorkingScenarioIntegration() throws JsonProcessingException {

        String a1Password = "pjd#1~!93kFDfx";
        String m1Password = "sd33k2k3kr44g4";
        String c1Password = "r65hb767jj66j4";
        String u1Password = "k4358gmrfidgjb";
        String u2Password = "g43bm90mgk5577";
        String u3Password = "v!4599u7njyhky";

        PortalUser a1 = createPortalUserAssignedToRole(UserGroupType.ADMIN,"adminWorkingScenario@gmail.com",
                a1Password);

        // 1.  Регистрируется пользователь m1
        // FINAL_1
        PortalUserCreateDTO m1PortalUserCreateDTO =
                testObjectsFactory.createPortalUserCreateDTOWithEncodedPassword(m1Password);

        PortalUserReadDTO m1 = performRequest("registration",
                HttpMethod.POST,
                m1PortalUserCreateDTO,
                null,
                null,
                PortalUserReadDTO.class);

        Assertions.assertThat(m1PortalUserCreateDTO).isEqualToIgnoringGivenFields(m1, "userTypeId");

        // 2.  a1 дает m1 роль модератора.
        // FINAL_2

        UserRoleReadDTO moderatorRole = performRequest("user-roles/role/" + UserGroupType.MODERATOR,
                HttpMethod.GET,
                null,
                a1.getEmail(),
                a1Password,
                UserRoleReadDTO.class);

        Assert.assertEquals(moderatorRole.getUserGroupType(),UserGroupType.MODERATOR);

        List<PortalUserUserRoleReadDTO> m1UserRoles =
                performRequestForList("portal-user/" + m1.getId() + "/user-roles/" + moderatorRole.getId(),
                HttpMethod.POST,
                null,
                a1.getEmail(),
                a1Password,
                new ParameterizedTypeReference<List<PortalUserUserRoleReadDTO>>() {});

        Assertions.assertThat(m1UserRoles).extracting("id").contains(moderatorRole.getId());

        // 3.  Регистрируется пользователь c1
        // FINAL_3
        PortalUserCreateDTO c1PortalUserCreateDTO =
                testObjectsFactory.createPortalUserCreateDTOWithEncodedPassword(c1Password);

        PortalUserReadDTO c1 = performRequest("registration",
                HttpMethod.POST,
                c1PortalUserCreateDTO,
                null,
                null,
                PortalUserReadDTO.class);

        Assertions.assertThat(c1PortalUserCreateDTO).isEqualToIgnoringGivenFields(c1, "userTypeId");

        // 4.  a1 дает c1 роль контент менеджера.
        // FINAL_4

        UserRoleReadDTO contentManagerRole = performRequest("user-roles/role/" + UserGroupType.CONTENTMANAGER,
                HttpMethod.GET,
                null,
                a1.getEmail(),
                a1Password,
                UserRoleReadDTO.class);

        Assert.assertEquals(contentManagerRole.getUserGroupType(),UserGroupType.CONTENTMANAGER);

        List<PortalUserUserRoleReadDTO> c1UserRoles =
                performRequestForList("portal-user/" + c1.getId() + "/user-roles/" + contentManagerRole.getId(),
                        HttpMethod.POST,
                        null,
                        a1.getEmail(),
                        a1Password,
                        new ParameterizedTypeReference<List<PortalUserUserRoleReadDTO>>() {});

        Assertions.assertThat(c1UserRoles).extracting("id").contains(contentManagerRole.getId());

        // 5.  Регистрируются 3 обычных юзеров. Мужчины: u1, u2 и женщина u3
        // FINAL_5
        PortalUserCreateDTO u1PortalUserCreateDTO =
                testObjectsFactory.createPortalUserCreateDTOWithEncodedPassword(u1Password);
        u1PortalUserCreateDTO.setGender(Gender.MALE);

        PortalUserReadDTO u1 = performRequest("registration",
                HttpMethod.POST,
                u1PortalUserCreateDTO,
                null,
                null,
                PortalUserReadDTO.class);

        Assertions.assertThat(u1PortalUserCreateDTO).isEqualToIgnoringGivenFields(u1, "userTypeId");

        PortalUserCreateDTO u2PortalUserCreateDTO =
                testObjectsFactory.createPortalUserCreateDTOWithEncodedPassword(u2Password);
        u2PortalUserCreateDTO.setGender(Gender.MALE);

        PortalUserReadDTO u2 = performRequest("registration",
                HttpMethod.POST,
                u2PortalUserCreateDTO,
                null,
                null,
                PortalUserReadDTO.class);

        Assertions.assertThat(u2PortalUserCreateDTO).isEqualToIgnoringGivenFields(u2, "userTypeId");

        PortalUserCreateDTO u3PortalUserCreateDTO =
                testObjectsFactory.createPortalUserCreateDTOWithEncodedPassword(u3Password);
        u3PortalUserCreateDTO.setGender(Gender.MALE);

        PortalUserReadDTO u3 = performRequest("registration",
                HttpMethod.POST,
                u3PortalUserCreateDTO,
                null,
                null,
                PortalUserReadDTO.class);

        Assertions.assertThat(u3PortalUserCreateDTO).isEqualToIgnoringGivenFields(u3, "userTypeId");

        // 6.  u2 исправляет имя в своем профиле.
        // FINAL_6
        PortalUserPatchDTO u2PatchName = new PortalUserPatchDTO();
        u2PatchName.setName("NewName6");

        u2 = performRequest("portalusers/" + u2.getId(),
                HttpMethod.PATCH,
                u2PatchName,
                u2.getEmail(),
                u2Password,
                PortalUserReadDTO.class);

        Assert.assertEquals(u2.getName(),u2PatchName.getName());

        // 7.  c1 создает фильм, персонажей, актеров, crew и тд. Взять какой-то реальный фильм для этого (не
        // рандомайзить и не импортировать).
        // FINAL_7
        // https://www.imdb.com/title/tt0078748

        MovieCreateDTO movieCreateDTO1 = new MovieCreateDTO();
        movieCreateDTO1.setTitle("Alien");
        movieCreateDTO1.setYear((short) 1979);
        movieCreateDTO1.setIsPublished(true);
        movieCreateDTO1.setCamera("Panavision PSR R-200, Panavision C-Series Lenses\n" +
                "Panavision Panaflex, Panavision C-Series Lenses");
        movieCreateDTO1.setAspectRatio("2.20 : 1 (70 mm prints)\n" +
                "2.39 : 1");
        movieCreateDTO1.setColour("Eastman Kodak");
        movieCreateDTO1.setLaboratory("DeLuxe, Hollywood (CA), USA (prints)\n" +
                "Rank Film Laboratories, Denham, UK (processing)");
        movieCreateDTO1.setSoundMix("70 mm 6-Track (70 mm prints) | Dolby (as Dolby Stereo)");
        movieCreateDTO1.setDescription("After a space merchant vessel receives an unknown transmission as a distress " +
                "call, one of the crew is attacked by a mysterious life form and they soon realize that its life " +
                "cycle has merely begun.");
        movieCreateDTO1.setCritique("Critique Of Capitalism");

        MovieReadDTO movie1 = performRequest("movies",
                HttpMethod.POST,
                movieCreateDTO1,
                c1.getEmail(),
                c1Password,
                MovieReadDTO.class);

        Assertions.assertThat(movieCreateDTO1).isEqualToComparingFieldByField(movie1);

        //Movie 1. Character 1
        PersonCreateDTO movie1Person1CreateDTO = new PersonCreateDTO();
        movie1Person1CreateDTO.setName("Tom");
        movie1Person1CreateDTO.setSurname("Skerritt");

        PersonReadDTO movie1Person1 = performRequest("persons",
                HttpMethod.POST,
                movie1Person1CreateDTO,
                c1.getEmail(),
                c1Password,
                PersonReadDTO.class);

        Assertions.assertThat(movie1Person1CreateDTO).isEqualToComparingFieldByField(movie1Person1);

        RoleCreateDTO movie1Role1CreateDTO = new RoleCreateDTO();
        movie1Role1CreateDTO.setTitle("Dallas");
        movie1Role1CreateDTO.setMovieId(movie1.getId());
        movie1Role1CreateDTO.setPersonId(movie1Person1.getId());
        movie1Role1CreateDTO.setRoleType(RoleType.SECOND);
        movie1Role1CreateDTO.setDescription("Captain. Lead of the group.");

        RoleReadDTO movie1Role1 = performRequest("roles",
                HttpMethod.POST,
                movie1Role1CreateDTO,
                c1.getEmail(),
                c1Password,
                RoleReadDTO.class);

        Assertions.assertThat(movie1Role1CreateDTO).isEqualToComparingFieldByField(movie1Role1);

        //Movie 1. Character 2
        PersonCreateDTO movie1Person2CreateDTO = new PersonCreateDTO();
        movie1Person2CreateDTO.setName("Sigourney");
        movie1Person2CreateDTO.setSurname("Weaver");

        PersonReadDTO movie1Person2 = performRequest("persons",
                HttpMethod.POST,
                movie1Person2CreateDTO,
                c1.getEmail(),
                c1Password,
                PersonReadDTO.class);

        Assertions.assertThat(movie1Person2CreateDTO).isEqualToComparingFieldByField(movie1Person2);

        RoleCreateDTO movie1Role2CreateDTO = new RoleCreateDTO();
        movie1Role2CreateDTO.setTitle("Ripley");
        movie1Role2CreateDTO.setMovieId(movie1.getId());
        movie1Role2CreateDTO.setPersonId(movie1Person2.getId());
        movie1Role2CreateDTO.setRoleType(RoleType.LEAD);
        movie1Role2CreateDTO.setDescription("Main character.");

        RoleReadDTO movie1Role2 = performRequest("roles",
                HttpMethod.POST,
                movie1Role2CreateDTO,
                c1.getEmail(),
                c1Password,
                RoleReadDTO.class);

        Assertions.assertThat(movie1Role2CreateDTO).isEqualToComparingFieldByField(movie1Role2);

        //Movie 1. Character 3
        PersonCreateDTO movie1Person3CreateDTO = new PersonCreateDTO();
        movie1Person3CreateDTO.setName("Veronica");
        movie1Person3CreateDTO.setSurname("Cartwright");

        PersonReadDTO movie1Person3 = performRequest("persons",
                HttpMethod.POST,
                movie1Person3CreateDTO,
                c1.getEmail(),
                c1Password,
                PersonReadDTO.class);

        Assertions.assertThat(movie1Person3CreateDTO).isEqualToComparingFieldByField(movie1Person3);

        RoleCreateDTO movie1Role3CreateDTO = new RoleCreateDTO();
        movie1Role3CreateDTO.setTitle("Lambert");
        movie1Role3CreateDTO.setMovieId(movie1.getId());
        movie1Role3CreateDTO.setPersonId(movie1Person2.getId());
        movie1Role3CreateDTO.setRoleType(RoleType.SECOND);
        movie1Role3CreateDTO.setDescription("Crew member.");

        RoleReadDTO movie1Role3 = performRequest("roles",
                HttpMethod.POST,
                movie1Role3CreateDTO,
                c1.getEmail(),
                c1Password,
                RoleReadDTO.class);

        Assertions.assertThat(movie1Role3CreateDTO).isEqualToComparingFieldByField(movie1Role3);

        //Movie 1. Character 4
        PersonCreateDTO movie1Person4CreateDTO = new PersonCreateDTO();
        movie1Person4CreateDTO.setName("Harry");
        movie1Person4CreateDTO.setSurname("Stanton");
        movie1Person4CreateDTO.setMiddleName("Dean");

        PersonReadDTO movie1Person4 = performRequest("persons",
                HttpMethod.POST,
                movie1Person4CreateDTO,
                c1.getEmail(),
                c1Password,
                PersonReadDTO.class);

        Assertions.assertThat(movie1Person4CreateDTO).isEqualToComparingFieldByField(movie1Person4);

        RoleCreateDTO movie1Role4CreateDTO = new RoleCreateDTO();
        movie1Role4CreateDTO.setTitle("Brett");
        movie1Role4CreateDTO.setMovieId(movie1.getId());
        movie1Role4CreateDTO.setPersonId(movie1Person2.getId());
        movie1Role4CreateDTO.setRoleType(RoleType.SECOND);
        movie1Role4CreateDTO.setDescription("Crew member. Repairman.");

        RoleReadDTO movie1Role4 = performRequest("roles",
                HttpMethod.POST,
                movie1Role4CreateDTO,
                c1.getEmail(),
                c1Password,
                RoleReadDTO.class);

        Assertions.assertThat(movie1Role4CreateDTO).isEqualToComparingFieldByField(movie1Role4);

        //Movie 1. Character 5
        PersonCreateDTO movie1Person5CreateDTO = new PersonCreateDTO();
        movie1Person5CreateDTO.setName("John");
        movie1Person5CreateDTO.setSurname("Hurt");

        PersonReadDTO movie1Person5 = performRequest("persons",
                HttpMethod.POST,
                movie1Person5CreateDTO,
                c1.getEmail(),
                c1Password,
                PersonReadDTO.class);

        Assertions.assertThat(movie1Person5CreateDTO).isEqualToComparingFieldByField(movie1Person5);

        RoleCreateDTO movie1Role5CreateDTO = new RoleCreateDTO();
        movie1Role5CreateDTO.setTitle("Kane");
        movie1Role5CreateDTO.setMovieId(movie1.getId());
        movie1Role5CreateDTO.setPersonId(movie1Person2.getId());
        movie1Role5CreateDTO.setRoleType(RoleType.SECOND);
        movie1Role5CreateDTO.setDescription("Crew member.");

        RoleReadDTO movie1Role5 = performRequest("roles",
                HttpMethod.POST,
                movie1Role5CreateDTO,
                c1.getEmail(),
                c1Password,
                RoleReadDTO.class);

        Assertions.assertThat(movie1Role5CreateDTO).isEqualToComparingFieldByField(movie1Role5);

        //Movie 1. Character 6
        PersonCreateDTO movie1Person6CreateDTO = new PersonCreateDTO();
        movie1Person6CreateDTO.setName("Ian");
        movie1Person6CreateDTO.setSurname("Holm");

        PersonReadDTO movie1Person6 = performRequest("persons",
                HttpMethod.POST,
                movie1Person6CreateDTO,
                c1.getEmail(),
                c1Password,
                PersonReadDTO.class);

        Assertions.assertThat(movie1Person6CreateDTO).isEqualToComparingFieldByField(movie1Person6);

        RoleCreateDTO movie1Role6CreateDTO = new RoleCreateDTO();
        movie1Role6CreateDTO.setTitle("Ash");
        movie1Role6CreateDTO.setMovieId(movie1.getId());
        movie1Role6CreateDTO.setPersonId(movie1Person2.getId());
        movie1Role6CreateDTO.setRoleType(RoleType.SECOND);
        movie1Role6CreateDTO.setDescription("Crew member. Android.");

        RoleReadDTO movie1Role6 = performRequest("roles",
                HttpMethod.POST,
                movie1Role6CreateDTO,
                c1.getEmail(),
                c1Password,
                RoleReadDTO.class);

        Assertions.assertThat(movie1Role6CreateDTO).isEqualToComparingFieldByField(movie1Role6);

        //Movie 1. Character 7
        PersonCreateDTO movie1Person7CreateDTO = new PersonCreateDTO();
        movie1Person7CreateDTO.setName("Yaphet");
        movie1Person7CreateDTO.setSurname("Kotto");

        PersonReadDTO movie1Person7 = performRequest("persons",
                HttpMethod.POST,
                movie1Person7CreateDTO,
                c1.getEmail(),
                c1Password,
                PersonReadDTO.class);

        Assertions.assertThat(movie1Person7CreateDTO).isEqualToComparingFieldByField(movie1Person7);

        RoleCreateDTO movie1Role7CreateDTO = new RoleCreateDTO();
        movie1Role7CreateDTO.setTitle("Parker");
        movie1Role7CreateDTO.setMovieId(movie1.getId());
        movie1Role7CreateDTO.setPersonId(movie1Person2.getId());
        movie1Role7CreateDTO.setRoleType(RoleType.SECOND);
        movie1Role7CreateDTO.setDescription("Crew member. Repairman.");

        RoleReadDTO movie1Role7 = performRequest("roles",
                HttpMethod.POST,
                movie1Role7CreateDTO,
                c1.getEmail(),
                c1Password,
                RoleReadDTO.class);

        Assertions.assertThat(movie1Role7CreateDTO).isEqualToComparingFieldByField(movie1Role7);

        //Movie 1. Character 8
        PersonCreateDTO movie1Person8CreateDTO = new PersonCreateDTO();
        movie1Person8CreateDTO.setName("Bolaji");
        movie1Person8CreateDTO.setSurname("Badejo");

        PersonReadDTO movie1Person8 = performRequest("persons",
                HttpMethod.POST,
                movie1Person8CreateDTO,
                c1.getEmail(),
                c1Password,
                PersonReadDTO.class);

        Assertions.assertThat(movie1Person8CreateDTO).isEqualToComparingFieldByField(movie1Person8);

        RoleCreateDTO movie1Role8CreateDTO = new RoleCreateDTO();
        movie1Role8CreateDTO.setTitle("Alien");
        movie1Role8CreateDTO.setMovieId(movie1.getId());
        movie1Role8CreateDTO.setPersonId(movie1Person2.getId());
        movie1Role8CreateDTO.setRoleType(RoleType.SECOND);
        movie1Role8CreateDTO.setDescription("Alien. New life form.");

        RoleReadDTO movie1Role8 = performRequest("roles",
                HttpMethod.POST,
                movie1Role8CreateDTO,
                c1.getEmail(),
                c1Password,
                RoleReadDTO.class);

        Assertions.assertThat(movie1Role8CreateDTO).isEqualToComparingFieldByField(movie1Role8);

        //Movie 1. Character 9
        PersonCreateDTO movie1Person9CreateDTO = new PersonCreateDTO();
        movie1Person9CreateDTO.setName("Helen");
        movie1Person9CreateDTO.setSurname("Horton");

        PersonReadDTO movie1Person9 = performRequest("persons",
                HttpMethod.POST,
                movie1Person9CreateDTO,
                c1.getEmail(),
                c1Password,
                PersonReadDTO.class);

        Assertions.assertThat(movie1Person9CreateDTO).isEqualToComparingFieldByField(movie1Person9);

        RoleCreateDTO movie1Role9CreateDTO = new RoleCreateDTO();
        movie1Role9CreateDTO.setTitle("Mother");
        movie1Role9CreateDTO.setMovieId(movie1.getId());
        movie1Role9CreateDTO.setPersonId(movie1Person2.getId());
        movie1Role9CreateDTO.setRoleType(RoleType.SECOND);
        movie1Role9CreateDTO.setDescription("AI. Main computer - voice control.");

        RoleReadDTO movie1Role9 = performRequest("roles",
                HttpMethod.POST,
                movie1Role9CreateDTO,
                c1.getEmail(),
                c1Password,
                RoleReadDTO.class);

        Assertions.assertThat(movie1Role9CreateDTO).isEqualToComparingFieldByField(movie1Role9);

        // Crew
        // https://www.imdb.com/title/tt0078748/fullcredits?ref_=tt_cl_sm#cast
        // Crew 1
        PersonCreateDTO movie1CrewPerson1CreateDTO = new PersonCreateDTO();
        movie1CrewPerson1CreateDTO.setName("Ridley");
        movie1CrewPerson1CreateDTO.setSurname("Scott");

        PersonReadDTO movie1CrewPerson1 = performRequest("persons",
                HttpMethod.POST,
                movie1CrewPerson1CreateDTO,
                c1.getEmail(),
                c1Password,
                PersonReadDTO.class);

        Assertions.assertThat(movie1CrewPerson1CreateDTO).isEqualToComparingFieldByField(movie1CrewPerson1);

        CrewTypeCreateDTO crewTypeCreateDTO = new CrewTypeCreateDTO();
        crewTypeCreateDTO.setName("Director");

        CrewTypeReadDTO crewType = performRequest("crewtypes",
                HttpMethod.POST,
                crewTypeCreateDTO,
                c1.getEmail(),
                c1Password,
                CrewTypeReadDTO.class);

        Assertions.assertThat(crewTypeCreateDTO).isEqualToComparingFieldByField(crewType);

        CrewCreateDTO crewCreateDTO = new CrewCreateDTO();
        crewCreateDTO.setMovieId(movie1.getId());
        crewCreateDTO.setPersonId(movie1CrewPerson1.getId());
        crewCreateDTO.setDescription("Director");
        crewCreateDTO.setCrewTypeId(crewType.getId());

        CrewReadDTO crew = performRequest("crew",
                HttpMethod.POST,
                crewCreateDTO,
                c1.getEmail(),
                c1Password,
                CrewReadDTO.class);

        Assertions.assertThat(crewCreateDTO).isEqualToComparingFieldByField(crew);

        // Crew 2
        PersonCreateDTO movie1CrewPersonCreateDTO = new PersonCreateDTO();
        movie1CrewPersonCreateDTO.setName("Dan");
        movie1CrewPersonCreateDTO.setSurname("O'Bannon");

        PersonReadDTO movie1CrewPerson = performRequest("persons",
                HttpMethod.POST,
                movie1CrewPersonCreateDTO,
                c1.getEmail(),
                c1Password,
                PersonReadDTO.class);

        Assertions.assertThat(movie1CrewPersonCreateDTO).isEqualToComparingFieldByField(movie1CrewPerson);

        crewTypeCreateDTO = new CrewTypeCreateDTO();
        crewTypeCreateDTO.setName("Writing Credits");

        crewType = performRequest("crewtypes",
                HttpMethod.POST,
                crewTypeCreateDTO,
                c1.getEmail(),
                c1Password,
                CrewTypeReadDTO.class);

        Assertions.assertThat(crewTypeCreateDTO).isEqualToComparingFieldByField(crewType);

        crewCreateDTO = new CrewCreateDTO();
        crewCreateDTO.setMovieId(movie1.getId());
        crewCreateDTO.setPersonId(movie1CrewPerson.getId());
        crewCreateDTO.setDescription("screenplay by");
        crewCreateDTO.setCrewTypeId(crewType.getId());

        crew = performRequest("crew",
                HttpMethod.POST,
                crewCreateDTO,
                c1.getEmail(),
                c1Password,
                CrewReadDTO.class);

        Assertions.assertThat(crewCreateDTO).isEqualToComparingFieldByField(crew);

        crewCreateDTO = new CrewCreateDTO();
        crewCreateDTO.setMovieId(movie1.getId());
        crewCreateDTO.setPersonId(movie1CrewPerson.getId());
        crewCreateDTO.setDescription("story by");
        crewCreateDTO.setCrewTypeId(crewType.getId());

        crew = performRequest("crew",
                HttpMethod.POST,
                crewCreateDTO,
                c1.getEmail(),
                c1Password,
                CrewReadDTO.class);

        Assertions.assertThat(crewCreateDTO).isEqualToComparingFieldByField(crew);

        // Ronald Shusett	...	(story by)
        movie1CrewPersonCreateDTO = new PersonCreateDTO();
        movie1CrewPersonCreateDTO.setName("Ronald");
        movie1CrewPersonCreateDTO.setSurname("Shusett");

        movie1CrewPerson = performRequest("persons",
                HttpMethod.POST,
                movie1CrewPersonCreateDTO,
                c1.getEmail(),
                c1Password,
                PersonReadDTO.class);

        Assertions.assertThat(movie1CrewPersonCreateDTO).isEqualToComparingFieldByField(movie1CrewPerson);

        crewCreateDTO = new CrewCreateDTO();
        crewCreateDTO.setMovieId(movie1.getId());
        crewCreateDTO.setPersonId(movie1CrewPerson.getId());
        crewCreateDTO.setDescription("story by");
        crewCreateDTO.setCrewTypeId(crewType.getId());

        crew = performRequest("crew",
                HttpMethod.POST,
                crewCreateDTO,
                c1.getEmail(),
                c1Password,
                CrewReadDTO.class);

        Assertions.assertThat(crewCreateDTO).isEqualToComparingFieldByField(crew);

        // Produced by
        // Gordon Carroll	...	producer
        movie1CrewPersonCreateDTO = new PersonCreateDTO();
        movie1CrewPersonCreateDTO.setName("Gordon");
        movie1CrewPersonCreateDTO.setSurname("Carroll");

        movie1CrewPerson = performRequest("persons",
                HttpMethod.POST,
                movie1CrewPersonCreateDTO,
                c1.getEmail(),
                c1Password,
                PersonReadDTO.class);

        Assertions.assertThat(movie1CrewPersonCreateDTO).isEqualToComparingFieldByField(movie1CrewPerson);

        crewTypeCreateDTO = new CrewTypeCreateDTO();
        crewTypeCreateDTO.setName("Produced by");

        crewType = performRequest("crewtypes",
                HttpMethod.POST,
                crewTypeCreateDTO,
                c1.getEmail(),
                c1Password,
                CrewTypeReadDTO.class);

        Assertions.assertThat(crewTypeCreateDTO).isEqualToComparingFieldByField(crewType);

        crewCreateDTO = new CrewCreateDTO();
        crewCreateDTO.setMovieId(movie1.getId());
        crewCreateDTO.setPersonId(movie1CrewPerson.getId());
        crewCreateDTO.setDescription("producer");
        crewCreateDTO.setCrewTypeId(crewType.getId());

        crew = performRequest("crew",
                HttpMethod.POST,
                crewCreateDTO,
                c1.getEmail(),
                c1Password,
                CrewReadDTO.class);

        Assertions.assertThat(crewCreateDTO).isEqualToComparingFieldByField(crew);

        // David Giler	...	producer
        movie1CrewPersonCreateDTO = new PersonCreateDTO();
        movie1CrewPersonCreateDTO.setName("David");
        movie1CrewPersonCreateDTO.setSurname("Giler");

        movie1CrewPerson = performRequest("persons",
                HttpMethod.POST,
                movie1CrewPersonCreateDTO,
                c1.getEmail(),
                c1Password,
                PersonReadDTO.class);

        Assertions.assertThat(movie1CrewPersonCreateDTO).isEqualToComparingFieldByField(movie1CrewPerson);

        crewCreateDTO = new CrewCreateDTO();
        crewCreateDTO.setMovieId(movie1.getId());
        crewCreateDTO.setPersonId(movie1CrewPerson.getId());
        crewCreateDTO.setDescription("producer");
        crewCreateDTO.setCrewTypeId(crewType.getId());

        crew = performRequest("crew",
                HttpMethod.POST,
                crewCreateDTO,
                c1.getEmail(),
                c1Password,
                CrewReadDTO.class);

        Assertions.assertThat(crewCreateDTO).isEqualToComparingFieldByField(crew);

        // Walter Hill	...	producer
        movie1CrewPersonCreateDTO = new PersonCreateDTO();
        movie1CrewPersonCreateDTO.setName("Walter");
        movie1CrewPersonCreateDTO.setSurname("Hill");

        movie1CrewPerson = performRequest("persons",
                HttpMethod.POST,
                movie1CrewPersonCreateDTO,
                c1.getEmail(),
                c1Password,
                PersonReadDTO.class);

        Assertions.assertThat(movie1CrewPersonCreateDTO).isEqualToComparingFieldByField(movie1CrewPerson);

        crewCreateDTO = new CrewCreateDTO();
        crewCreateDTO.setMovieId(movie1.getId());
        crewCreateDTO.setPersonId(movie1CrewPerson.getId());
        crewCreateDTO.setDescription("producer");
        crewCreateDTO.setCrewTypeId(crewType.getId());

        crew = performRequest("crew",
                HttpMethod.POST,
                crewCreateDTO,
                c1.getEmail(),
                c1Password,
                CrewReadDTO.class);

        Assertions.assertThat(crewCreateDTO).isEqualToComparingFieldByField(crew);

        // Ivor Powell	...	associate producer
        movie1CrewPersonCreateDTO = new PersonCreateDTO();
        movie1CrewPersonCreateDTO.setName("Ivor");
        movie1CrewPersonCreateDTO.setSurname("Powell");

        movie1CrewPerson = performRequest("persons",
                HttpMethod.POST,
                movie1CrewPersonCreateDTO,
                c1.getEmail(),
                c1Password,
                PersonReadDTO.class);

        Assertions.assertThat(movie1CrewPersonCreateDTO).isEqualToComparingFieldByField(movie1CrewPerson);

        crewCreateDTO = new CrewCreateDTO();
        crewCreateDTO.setMovieId(movie1.getId());
        crewCreateDTO.setPersonId(movie1CrewPerson.getId());
        crewCreateDTO.setDescription("associate producer");
        crewCreateDTO.setCrewTypeId(crewType.getId());

        crew = performRequest("crew",
                HttpMethod.POST,
                crewCreateDTO,
                c1.getEmail(),
                c1Password,
                CrewReadDTO.class);

        Assertions.assertThat(crewCreateDTO).isEqualToComparingFieldByField(crew);

        // Ronald Shusett	...	executive producer
        movie1CrewPersonCreateDTO = new PersonCreateDTO();
        movie1CrewPersonCreateDTO.setName("Ronald");
        movie1CrewPersonCreateDTO.setSurname("Shusett");

        movie1CrewPerson = performRequest("persons",
                HttpMethod.POST,
                movie1CrewPersonCreateDTO,
                c1.getEmail(),
                c1Password,
                PersonReadDTO.class);

        Assertions.assertThat(movie1CrewPersonCreateDTO).isEqualToComparingFieldByField(movie1CrewPerson);

        crewCreateDTO = new CrewCreateDTO();
        crewCreateDTO.setMovieId(movie1.getId());
        crewCreateDTO.setPersonId(movie1CrewPerson.getId());
        crewCreateDTO.setDescription("executive producer");
        crewCreateDTO.setCrewTypeId(crewType.getId());

        crew = performRequest("crew",
                HttpMethod.POST,
                crewCreateDTO,
                c1.getEmail(),
                c1Password,
                CrewReadDTO.class);

        Assertions.assertThat(crewCreateDTO).isEqualToComparingFieldByField(crew);

        // Music by
        // Jerry Goldsmith
        movie1CrewPersonCreateDTO = new PersonCreateDTO();
        movie1CrewPersonCreateDTO.setName("Jerry");
        movie1CrewPersonCreateDTO.setSurname("Goldsmith");

        movie1CrewPerson = performRequest("persons",
                HttpMethod.POST,
                movie1CrewPersonCreateDTO,
                c1.getEmail(),
                c1Password,
                PersonReadDTO.class);

        Assertions.assertThat(movie1CrewPersonCreateDTO).isEqualToComparingFieldByField(movie1CrewPerson);

        crewTypeCreateDTO = new CrewTypeCreateDTO();
        crewTypeCreateDTO.setName("Music by");

        crewType = performRequest("crewtypes",
                HttpMethod.POST,
                crewTypeCreateDTO,
                c1.getEmail(),
                c1Password,
                CrewTypeReadDTO.class);

        Assertions.assertThat(crewTypeCreateDTO).isEqualToComparingFieldByField(crewType);

        crewCreateDTO = new CrewCreateDTO();
        crewCreateDTO.setMovieId(movie1.getId());
        crewCreateDTO.setPersonId(movie1CrewPerson.getId());
        crewCreateDTO.setDescription("Music by");
        crewCreateDTO.setCrewTypeId(crewType.getId());

        crew = performRequest("crew",
                HttpMethod.POST,
                crewCreateDTO,
                c1.getEmail(),
                c1Password,
                CrewReadDTO.class);

        Assertions.assertThat(crewCreateDTO).isEqualToComparingFieldByField(crew);

        // Cinematography by
        // Derek Vanlint	...	director of photography
        movie1CrewPersonCreateDTO = new PersonCreateDTO();
        movie1CrewPersonCreateDTO.setName("Derek");
        movie1CrewPersonCreateDTO.setSurname("Vanlint");

        movie1CrewPerson = performRequest("persons",
                HttpMethod.POST,
                movie1CrewPersonCreateDTO,
                c1.getEmail(),
                c1Password,
                PersonReadDTO.class);

        Assertions.assertThat(movie1CrewPersonCreateDTO).isEqualToComparingFieldByField(movie1CrewPerson);

        crewTypeCreateDTO = new CrewTypeCreateDTO();
        crewTypeCreateDTO.setName("Cinematography by");

        crewType = performRequest("crewtypes",
                HttpMethod.POST,
                crewTypeCreateDTO,
                c1.getEmail(),
                c1Password,
                CrewTypeReadDTO.class);

        Assertions.assertThat(crewTypeCreateDTO).isEqualToComparingFieldByField(crewType);

        crewCreateDTO = new CrewCreateDTO();
        crewCreateDTO.setMovieId(movie1.getId());
        crewCreateDTO.setPersonId(movie1CrewPerson.getId());
        crewCreateDTO.setDescription("director of photography");
        crewCreateDTO.setCrewTypeId(crewType.getId());

        crew = performRequest("crew",
                HttpMethod.POST,
                crewCreateDTO,
                c1.getEmail(),
                c1Password,
                CrewReadDTO.class);

        Assertions.assertThat(crewCreateDTO).isEqualToComparingFieldByField(crew);

        // Film Editing by
        // David Crowther	...	(director's cut)
        movie1CrewPersonCreateDTO = new PersonCreateDTO();
        movie1CrewPersonCreateDTO.setName("David");
        movie1CrewPersonCreateDTO.setSurname("Crowther");

        movie1CrewPerson = performRequest("persons",
                HttpMethod.POST,
                movie1CrewPersonCreateDTO,
                c1.getEmail(),
                c1Password,
                PersonReadDTO.class);

        Assertions.assertThat(movie1CrewPersonCreateDTO).isEqualToComparingFieldByField(movie1CrewPerson);

        crewTypeCreateDTO = new CrewTypeCreateDTO();
        crewTypeCreateDTO.setName("Film Editing by");

        crewType = performRequest("crewtypes",
                HttpMethod.POST,
                crewTypeCreateDTO,
                c1.getEmail(),
                c1Password,
                CrewTypeReadDTO.class);

        Assertions.assertThat(crewTypeCreateDTO).isEqualToComparingFieldByField(crewType);

        crewCreateDTO = new CrewCreateDTO();
        crewCreateDTO.setMovieId(movie1.getId());
        crewCreateDTO.setPersonId(movie1CrewPerson.getId());
        crewCreateDTO.setDescription("director's cut");
        crewCreateDTO.setCrewTypeId(crewType.getId());

        crew = performRequest("crew",
                HttpMethod.POST,
                crewCreateDTO,
                c1.getEmail(),
                c1Password,
                CrewReadDTO.class);

        Assertions.assertThat(crewCreateDTO).isEqualToComparingFieldByField(crew);

        // Terry Rawlings
        movie1CrewPersonCreateDTO = new PersonCreateDTO();
        movie1CrewPersonCreateDTO.setName("Terry");
        movie1CrewPersonCreateDTO.setSurname("Rawlings");

        movie1CrewPerson = performRequest("persons",
                HttpMethod.POST,
                movie1CrewPersonCreateDTO,
                c1.getEmail(),
                c1Password,
                PersonReadDTO.class);

        Assertions.assertThat(movie1CrewPersonCreateDTO).isEqualToComparingFieldByField(movie1CrewPerson);

        crewCreateDTO = new CrewCreateDTO();
        crewCreateDTO.setMovieId(movie1.getId());
        crewCreateDTO.setPersonId(movie1CrewPerson.getId());
        crewCreateDTO.setDescription("Editor");
        crewCreateDTO.setCrewTypeId(crewType.getId());

        crew = performRequest("crew",
                HttpMethod.POST,
                crewCreateDTO,
                c1.getEmail(),
                c1Password,
                CrewReadDTO.class);

        Assertions.assertThat(crewCreateDTO).isEqualToComparingFieldByField(crew);

        // Peter Weatherley
        movie1CrewPersonCreateDTO = new PersonCreateDTO();
        movie1CrewPersonCreateDTO.setName("Peter");
        movie1CrewPersonCreateDTO.setSurname("Weatherley");

        movie1CrewPerson = performRequest("persons",
                HttpMethod.POST,
                movie1CrewPersonCreateDTO,
                c1.getEmail(),
                c1Password,
                PersonReadDTO.class);

        Assertions.assertThat(movie1CrewPersonCreateDTO).isEqualToComparingFieldByField(movie1CrewPerson);

        crewCreateDTO = new CrewCreateDTO();
        crewCreateDTO.setMovieId(movie1.getId());
        crewCreateDTO.setPersonId(movie1CrewPerson.getId());
        crewCreateDTO.setDescription("Editor");
        crewCreateDTO.setCrewTypeId(crewType.getId());

        crew = performRequest("crew",
                HttpMethod.POST,
                crewCreateDTO,
                c1.getEmail(),
                c1Password,
                CrewReadDTO.class);

        Assertions.assertThat(crewCreateDTO).isEqualToComparingFieldByField(crew);

        // Genre
        GenreCreateDTO genreCreateDTO = new GenreCreateDTO();
        genreCreateDTO.setName(MovieGenreType.SCIENCE_FICTION);
        genreCreateDTO.setMovieId(movie1.getId());

        GenreReadDTO genre = performRequest("genres",
                HttpMethod.POST,
                genreCreateDTO,
                c1.getEmail(),
                c1Password,
                GenreReadDTO.class);

        Assertions.assertThat(genreCreateDTO).isEqualToComparingFieldByField(genre);

        // Release details
        // https://www.imdb.com/title/tt0078748/releaseinfo?ref_=ttloc_sa_2
        CountryCreateDTO countryCreateDTO = new CountryCreateDTO();
        countryCreateDTO.setName("USA");

        CountryReadDTO country = performRequest("countries",
                HttpMethod.POST,
                countryCreateDTO,
                c1.getEmail(),
                c1Password,
                CountryReadDTO.class);

        Assertions.assertThat(countryCreateDTO).isEqualToComparingFieldByField(country);

        ReleaseDetailCreateDTO releaseDetailCreateDTO = new ReleaseDetailCreateDTO();
        releaseDetailCreateDTO.setReleaseDate(LocalDate.of(1979, 05, 25));
        releaseDetailCreateDTO.setCountryId(country.getId());
        releaseDetailCreateDTO.setMovieId(movie1.getId());

        ReleaseDetailReadDTO releaseDetail = performRequest("releasedetails",
                HttpMethod.POST,
                releaseDetailCreateDTO,
                c1.getEmail(),
                c1Password,
                ReleaseDetailReadDTO.class);

        Assertions.assertThat(releaseDetailCreateDTO).isEqualToComparingFieldByField(releaseDetail);

        countryCreateDTO = new CountryCreateDTO();
        countryCreateDTO.setName("Soviet Union");

        country = performRequest("countries",
                HttpMethod.POST,
                countryCreateDTO,
                c1.getEmail(),
                c1Password,
                CountryReadDTO.class);

        Assertions.assertThat(countryCreateDTO).isEqualToComparingFieldByField(country);

        releaseDetailCreateDTO = new ReleaseDetailCreateDTO();
        releaseDetailCreateDTO.setReleaseDate(LocalDate.of(1979, 05, 28));
        releaseDetailCreateDTO.setCountryId(country.getId());
        releaseDetailCreateDTO.setMovieId(movie1.getId());

        releaseDetail = performRequest("releasedetails",
                HttpMethod.POST,
                releaseDetailCreateDTO,
                c1.getEmail(),
                c1Password,
                ReleaseDetailReadDTO.class);

        Assertions.assertThat(releaseDetailCreateDTO).isEqualToComparingFieldByField(releaseDetail);

        countryCreateDTO = new CountryCreateDTO();
        countryCreateDTO.setName("Japan");

        country = performRequest("countries",
                HttpMethod.POST,
                countryCreateDTO,
                c1.getEmail(),
                c1Password,
                CountryReadDTO.class);

        Assertions.assertThat(countryCreateDTO).isEqualToComparingFieldByField(country);

        releaseDetailCreateDTO = new ReleaseDetailCreateDTO();
        releaseDetailCreateDTO.setReleaseDate(LocalDate.of(1979, 07, 21));
        releaseDetailCreateDTO.setCountryId(country.getId());
        releaseDetailCreateDTO.setMovieId(movie1.getId());

        releaseDetail = performRequest("releasedetails",
                HttpMethod.POST,
                releaseDetailCreateDTO,
                c1.getEmail(),
                c1Password,
                ReleaseDetailReadDTO.class);

        Assertions.assertThat(releaseDetailCreateDTO).isEqualToComparingFieldByField(releaseDetail);

        countryCreateDTO = new CountryCreateDTO();
        countryCreateDTO.setName("Brazil");

        country = performRequest("countries",
                HttpMethod.POST,
                countryCreateDTO,
                c1.getEmail(),
                c1Password,
                CountryReadDTO.class);

        Assertions.assertThat(countryCreateDTO).isEqualToComparingFieldByField(country);

        releaseDetailCreateDTO = new ReleaseDetailCreateDTO();
        releaseDetailCreateDTO.setReleaseDate(LocalDate.of(1979, 8, 20));
        releaseDetailCreateDTO.setCountryId(country.getId());
        releaseDetailCreateDTO.setMovieId(movie1.getId());

        releaseDetail = performRequest("releasedetails",
                HttpMethod.POST,
                releaseDetailCreateDTO,
                c1.getEmail(),
                c1Password,
                ReleaseDetailReadDTO.class);

        Assertions.assertThat(releaseDetailCreateDTO).isEqualToComparingFieldByField(releaseDetail);

        countryCreateDTO = new CountryCreateDTO();
        countryCreateDTO.setName("Puerto Rico");

        country = performRequest("countries",
                HttpMethod.POST,
                countryCreateDTO,
                c1.getEmail(),
                c1Password,
                CountryReadDTO.class);

        Assertions.assertThat(countryCreateDTO).isEqualToComparingFieldByField(country);

        releaseDetailCreateDTO = new ReleaseDetailCreateDTO();
        releaseDetailCreateDTO.setReleaseDate(LocalDate.of(1979, 8, 30));
        releaseDetailCreateDTO.setCountryId(country.getId());
        releaseDetailCreateDTO.setMovieId(movie1.getId());

        releaseDetail = performRequest("releasedetails",
                HttpMethod.POST,
                releaseDetailCreateDTO,
                c1.getEmail(),
                c1Password,
                ReleaseDetailReadDTO.class);

        Assertions.assertThat(releaseDetailCreateDTO).isEqualToComparingFieldByField(releaseDetail);

        countryCreateDTO = new CountryCreateDTO();
        countryCreateDTO.setName("Spain");

        country = performRequest("countries",
                HttpMethod.POST,
                countryCreateDTO,
                c1.getEmail(),
                c1Password,
                CountryReadDTO.class);

        Assertions.assertThat(countryCreateDTO).isEqualToComparingFieldByField(country);

        releaseDetailCreateDTO = new ReleaseDetailCreateDTO();
        releaseDetailCreateDTO.setReleaseDate(LocalDate.of(1979, 9, 01));
        releaseDetailCreateDTO.setCountryId(country.getId());
        releaseDetailCreateDTO.setMovieId(movie1.getId());

        releaseDetail = performRequest("releasedetails",
                HttpMethod.POST,
                releaseDetailCreateDTO,
                c1.getEmail(),
                c1Password,
                ReleaseDetailReadDTO.class);

        Assertions.assertThat(releaseDetailCreateDTO).isEqualToComparingFieldByField(releaseDetail);

        countryCreateDTO = new CountryCreateDTO();
        countryCreateDTO.setName("UK");

        country = performRequest("countries",
                HttpMethod.POST,
                countryCreateDTO,
                c1.getEmail(),
                c1Password,
                CountryReadDTO.class);

        Assertions.assertThat(countryCreateDTO).isEqualToComparingFieldByField(country);

        releaseDetailCreateDTO = new ReleaseDetailCreateDTO();
        releaseDetailCreateDTO.setReleaseDate(LocalDate.of(1979, 9, 6));
        releaseDetailCreateDTO.setCountryId(country.getId());
        releaseDetailCreateDTO.setMovieId(movie1.getId());

        releaseDetail = performRequest("releasedetails",
                HttpMethod.POST,
                releaseDetailCreateDTO,
                c1.getEmail(),
                c1Password,
                ReleaseDetailReadDTO.class);

        Assertions.assertThat(releaseDetailCreateDTO).isEqualToComparingFieldByField(releaseDetail);

        countryCreateDTO = new CountryCreateDTO();
        countryCreateDTO.setName("France");

        country = performRequest("countries",
                HttpMethod.POST,
                countryCreateDTO,
                c1.getEmail(),
                c1Password,
                CountryReadDTO.class);

        Assertions.assertThat(countryCreateDTO).isEqualToComparingFieldByField(country);

        releaseDetailCreateDTO = new ReleaseDetailCreateDTO();
        releaseDetailCreateDTO.setReleaseDate(LocalDate.of(1979, 9, 12));
        releaseDetailCreateDTO.setCountryId(country.getId());
        releaseDetailCreateDTO.setMovieId(movie1.getId());

        releaseDetail = performRequest("releasedetails",
                HttpMethod.POST,
                releaseDetailCreateDTO,
                c1.getEmail(),
                c1Password,
                ReleaseDetailReadDTO.class);

        Assertions.assertThat(releaseDetailCreateDTO).isEqualToComparingFieldByField(releaseDetail);

        // Movie production companies
        CompanyDetailsCreateDTO companyDetailsCreateDTO = new CompanyDetailsCreateDTO();
        companyDetailsCreateDTO.setName("Twentieth Century-Fox");
        companyDetailsCreateDTO.setOverview("Movie production company");
        companyDetailsCreateDTO.setYearOfFoundation(LocalDate.of(1935, 5, 31));

        CompanyDetailsReadDTO companyDetails = performRequest("companydetails",
                HttpMethod.POST,
                companyDetailsCreateDTO,
                c1.getEmail(),
                c1Password,
                CompanyDetailsReadDTO.class);

        Assertions.assertThat(companyDetailsCreateDTO).isEqualToComparingFieldByField(companyDetails);

        MovieCompanyCreateDTO movieCompanyCreateDTO = new MovieCompanyCreateDTO();
        movieCompanyCreateDTO.setDescription("Movie production Company");
        movieCompanyCreateDTO.setCompanyDetailsId(companyDetails.getId());
        movieCompanyCreateDTO.setMovieProductionType(MovieProductionType.PRODUCTION_COMPANIES);

        MovieCompanyReadDTO movieCompany = performRequest("moviecompanies",
                HttpMethod.POST,
                movieCompanyCreateDTO,
                c1.getEmail(),
                c1Password,
                MovieCompanyReadDTO.class);

        Assertions.assertThat(movieCompanyCreateDTO).isEqualToComparingFieldByField(movieCompany);

        companyDetailsCreateDTO = new CompanyDetailsCreateDTO();
        companyDetailsCreateDTO.setName("Brandywine Productions");
        companyDetailsCreateDTO.setOverview("Movie production company");
        companyDetailsCreateDTO.setYearOfFoundation(LocalDate.of(1935, 5, 31));

        companyDetails = performRequest("companydetails",
                HttpMethod.POST,
                companyDetailsCreateDTO,
                c1.getEmail(),
                c1Password,
                CompanyDetailsReadDTO.class);

        Assertions.assertThat(companyDetailsCreateDTO).isEqualToComparingFieldByField(companyDetails);

        movieCompanyCreateDTO = new MovieCompanyCreateDTO();
        movieCompanyCreateDTO.setDescription("Movie production Company");
        movieCompanyCreateDTO.setCompanyDetailsId(companyDetails.getId());
        movieCompanyCreateDTO.setMovieProductionType(MovieProductionType.PRODUCTION_COMPANIES);

        movieCompany = performRequest("moviecompanies",
                HttpMethod.POST,
                movieCompanyCreateDTO,
                c1.getEmail(),
                c1Password,
                MovieCompanyReadDTO.class);

        Assertions.assertThat(movieCompanyCreateDTO).isEqualToComparingFieldByField(movieCompany);

        // FINAL_8
        // c1 создает новость о фильме
        String newsTopic = "Movie Alien released";
        NewsCreateDTO newsCreateDTO = new NewsCreateDTO();
        newsCreateDTO.setTopic(newsTopic);
        newsCreateDTO.setDescription("In the distant future, the crew of the commercial spaceship Nostromo are on " +
                "their way home when they pick up a distress call from a distant moon. The crew are under obligation " +
                "to investigate and the spaceship descends on the moon afterwards. After a rough landing, three crew " +
                "members leave the spaceship to explore the area on the moon. At the same time as they discover a " +
                "hive colony of some unknown creature, the ship's computer deciphers the message to be a warning, not" +
                " a distress call. When one of the eggs is disturbed, the crew realizes that they are not alone on " +
                "the spaceship and they must deal with the consequences.");
        newsCreateDTO.setPublisherId(c1.getId());
        newsCreateDTO.setPublished(Instant.now());

        NewsReadDTO news = performRequest("news",
                HttpMethod.POST,
                newsCreateDTO,
                c1.getEmail(),
                c1Password,
                NewsReadDTO.class);

        Assertions.assertThat(newsCreateDTO).isEqualToComparingFieldByField(news);

        // FINAL_9
        // u1 просматривает контент новости. (тут и далее это будет означать, что надо сделать запрос на чтение и
        // проверить упоминаемое поле/поля, что там ожидаемое значение)
        NewsReadDTO newsReview = performRequest("news/" + news.getId(),
                HttpMethod.GET,
                null,
                u1.getEmail(),
                u1Password,
                NewsReadDTO.class);

        Assert.assertEquals(newsReview.getTopic(), newsTopic);

        // FINAL_10
        // u1 и u2 отмечают, что новость им нравится.
        NewsFeedbackCreateDTO newsFeedbackCreateDTO = new NewsFeedbackCreateDTO();
        newsFeedbackCreateDTO.setPortalUserId(u1.getId());
        newsFeedbackCreateDTO.setNewsId(news.getId());
        newsFeedbackCreateDTO.setIsLiked(true);

        NewsFeedbackReadDTO u1Feedback = performRequest("news-feedbacks",
                HttpMethod.POST,
                newsFeedbackCreateDTO,
                u1.getEmail(),
                u1Password,
                NewsFeedbackReadDTO.class);

        Assertions.assertThat(newsFeedbackCreateDTO).isEqualToComparingFieldByField(u1Feedback);
        //Assert.assertEquals(newsFeedbackReadResponseU1.getBody().getNewsId(), newsResponse.getBody().getId());

        newsFeedbackCreateDTO = new NewsFeedbackCreateDTO();
        newsFeedbackCreateDTO.setPortalUserId(u2.getId());
        newsFeedbackCreateDTO.setNewsId(news.getId());
        newsFeedbackCreateDTO.setIsLiked(true);

        NewsFeedbackReadDTO u2Feedback = performRequest("news-feedbacks",
                HttpMethod.POST,
                newsFeedbackCreateDTO,
                u2.getEmail(),
                u2Password,
                NewsFeedbackReadDTO.class);

        Assertions.assertThat(newsFeedbackCreateDTO).isEqualToComparingFieldByField(u2Feedback);

        // FINAL_11
        // Пользователь u3 - ошибочно отмечает, что новость ей нравится, а потом отменяет оценку
        NewsFeedbackCreateDTO newsFeedbackCreateDTOU3 = new NewsFeedbackCreateDTO();
        newsFeedbackCreateDTOU3.setPortalUserId(u3.getId());
        newsFeedbackCreateDTOU3.setNewsId(news.getId());
        newsFeedbackCreateDTOU3.setIsLiked(true);

        NewsFeedbackReadDTO u3Feedback = performRequest("news-feedbacks",
                HttpMethod.POST,
                newsFeedbackCreateDTOU3,
                u3.getEmail(),
                u3Password,
                NewsFeedbackReadDTO.class);

        Assertions.assertThat(newsFeedbackCreateDTOU3).isEqualToComparingFieldByField(u3Feedback);

        performRequest("news-feedbacks/" + u3Feedback.getId(),
                HttpMethod.DELETE,
                null,
                u3.getEmail(),
                u3Password,
                Void.class);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", getBasicAuthorizationHeaderValue(u3.getEmail(), u3Password));
        HttpEntity<?> httpEntity = new HttpEntity<>(headers);

        Assertions.assertThatThrownBy(() -> new RestTemplate().exchange("http://localhost:8080/api/v1/news-feedbacks/"
                + u3Feedback.getId(),
                HttpMethod.GET, HttpEntity.EMPTY, new ParameterizedTypeReference<NewsFeedbackReadDTO>() {}))
                .isInstanceOf(HttpClientErrorException.class).extracting("statusCode")
                .isEqualTo(HttpStatus.UNAUTHORIZED);

        // FINAL_12
        // Пользователь u3 - отмечает, что новость ей не нравится
        NewsFeedbackCreateDTO newsFeedbackCreateDTO12U3 = new NewsFeedbackCreateDTO();
        newsFeedbackCreateDTO12U3.setPortalUserId(u1.getId());
        newsFeedbackCreateDTO12U3.setNewsId(news.getId());
        newsFeedbackCreateDTO12U3.setIsLiked(false);

        NewsFeedbackReadDTO u3FeedbackDislike = performRequest("news-feedbacks",
                HttpMethod.POST,
                newsFeedbackCreateDTO12U3,
                u3.getEmail(),
                u3Password,
                NewsFeedbackReadDTO.class);

        Assertions.assertThat(newsFeedbackCreateDTO12U3).isEqualToComparingFieldByField(u3FeedbackDislike);
/*
        // FINAL_13
        // u3 помечает слово в новости, что в нем есть ошибка, без указания правильного варианта
        UserTypoRequestCreateDTO userTypoRequestCreateDTO13U3 = new UserTypoRequestCreateDTO();
        userTypoRequestCreateDTO13U3.setNewsId(news.getId());
        userTypoRequestCreateDTO13U3.setRequesterId(u3.getId());
        userTypoRequestCreateDTO13U3.setMovieId(movie1.getId());
        userTypoRequestCreateDTO13U3.setSourceText("Nostromo");
        userTypoRequestCreateDTO13U3.setProposedText("");

        UserTypoRequestReadDTO u3TypoRequest = performRequest("news/" + news.getId() + "/user-typo-requests/" +
                u3.getId(),
                HttpMethod.POST,
                userTypoRequestCreateDTO13U3,
                u3.getEmail(),
                u3Password,
                UserTypoRequestReadDTO.class);

        Assertions.assertThat(userTypoRequestCreateDTO13U3).isEqualToComparingFieldByField(u3TypoRequest);
*/
        /*
        13. u3 помечает слово в новости, что в нем есть ошибка, без указания правильного варианта
        14. с1 просматривает сигналы от пользователей и замечает новый сигнал
        15. c1 начинает рассмотрение сигнала и исправляет ошибку
        16. c1 просматривает сигналы от пользователей - теперь список сигналов пуст
        17. Незарегистрированный пользователь просматривает контент новости и также видит,
            что двоим она понравилась и одному не понравилась.
        18. u1 просматривает фильм.
        19. u1 пишет отзыв на фильм и ставит высокую оценку. В отзыве один из фрагментов помечается как спойлер.
        20. u2 просматривает фильм, отзыв не виден, т.к. он пока на модерации.
        21. u2 ставит оценку фильму.
        22. m1 просматривает неподтвержденные отзывы и замечает один.
        23. m1 начинает просмотр отзыва и подтверждает, что он хороший. Пользователь u1 помечается как
            заслуживающий доверия, так что последующие его отзывы будут публиковаться сразу.
        24. u3 просматривает фильм и ставит оценку. Она видит подтвержденный отзыв.
        25. u3 помечает, что отзыв ей нравится.
        26. Незарегистрированный пользователь просматривает фильм и видит его среднюю оценку
            (если у вас расчет среднего выполняется по cron джобе, то поставьте, чтобы для теста
            она запускалась раз в пару секунд)
        27. c1 импортирует другой фильм, такой, чтобы один из актеров играл в первом, персонажей, актеров, crew и тд.
            Для этого создать эндпонт контент менеджера для импорта фильма по id во внешней системе.
        28. u1 ставит фильму низкую оценку, и пишет гневный отзыв.
        29. u2, u3 оценивают фильм. Отзыв виден сразу. Оба отправляют сигнал модератору, что в отзыве есть маты.
        30. m1 просматривает список сигналов к нему, у видит 2 новых сигнала.
        31. m1 начинает рассмотрение одного из сигналов. Видит маты, удаляет обзор и банит u1.
        32. m1 просматривает список сигналов к модераторам - теперь этот список пуст.
        33. u2, u3 оценивают роли актера, являющегося общим для двух фильмов. Роли оцениваются в 2 фильмах.
        34. u1 пытается оценить роль актера, но запрос выдает ему ошибку 403, т.к. он забанен.
        35. Незарегистрированный пользователь заходит на страницу актера, являющегося общим для 2 фильмов,
            читает информацию о нем и видит среднюю оценку по 2 фильмам в которых он играл.
            Также видит среднюю оценку по ролям этого актера.
        */
    }

    private PortalUser createPortalUserAssignedToRole(UserGroupType userGroupType, String email, String password) {
        UserRole userRole = testObjectsFactory.getUserRole(userGroupType);
        PortalUser portalUser = testObjectsFactory.generateFlatEntityWithoutId(PortalUser.class);
        portalUser.setUserType(testObjectsFactory.createUserType());
        portalUser.getUserRoles().add(userRole);
        if (userGroupType == UserGroupType.ADMIN) {
            portalUser.setName("Admin");
            portalUser.setLogin("admin");
        }
        portalUser.setEmail(email);
        portalUser.setEncodedPassword(passwordEncoder.encode(password));
        return portalUserRepository.save(portalUser);
    }

    private String getBasicAuthorizationHeaderValue(String userName, String password) {
        return "Basic " + new String((Base64.encode(String.format("%s:%s", userName, password).getBytes())));
    }

    private <T> T performRequest(String url,
                                      HttpMethod httpMethod,
                                      Object body,
                                      String email,
                                      String password,
                                      Class<T> returnClass) {
        HttpHeaders headers = new HttpHeaders();
        if (email != null) {
            headers.add("Authorization", getBasicAuthorizationHeaderValue(email, password));
        }
        HttpEntity<?> httpEntity = new HttpEntity<>(body, headers);
        RestTemplate restTemplate = new RestTemplate();
        if (httpMethod == HttpMethod.PATCH) {
            HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
            requestFactory.setConnectTimeout(500);
            requestFactory.setReadTimeout(500);
            restTemplate.setRequestFactory(requestFactory);
        }
        ResponseEntity<T> response = restTemplate.exchange("http://localhost:8080/api/v1/" + url,
                httpMethod, httpEntity, returnClass);
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
        return response.getBody();
    }

    private <T> List<T> performRequestForList(String url,
                                              HttpMethod httpMethod,
                                              Object body,
                                              String email,
                                              String password,
                                              ParameterizedTypeReference<List<T>> returnClass) {
        HttpHeaders headers = new HttpHeaders();
        if (email != null) {
            headers.add("Authorization", getBasicAuthorizationHeaderValue(email, password));
        }
        HttpEntity<?> httpEntity = new HttpEntity<>(body, headers);
        RestTemplate restTemplate = new RestTemplate();
        /*HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        requestFactory.setConnectTimeout(500);
        requestFactory.setReadTimeout(500);
        restTemplate.setRequestFactory(requestFactory);*/
        ResponseEntity<List<T>> response = restTemplate.exchange("http://localhost:8080/api/v1/" + url,
                httpMethod, httpEntity, returnClass);
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
        return response.getBody();
    }
}
