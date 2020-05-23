package solvve.course.controller.integration;

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
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
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
import java.util.UUID;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@ActiveProfiles({"test", "integration-test"})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestPropertySource(properties = {
        "update.average.rating.of.movies.job.cron=* * * * * *",
        "update.average.rating.of.roles.job.cron=* * * * * *",
        "update.average.rating.of.movies.for.person.job.cron=*/2 * * * * *",
        "update.average.rating.of.roles.for.person.job.cron=* * * * * *"})
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
    public void testWorkingScenarioIntegration() throws InterruptedException {

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
        movie1Person2CreateDTO.setName("Sigourney Weaver");
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
        movie1Role3CreateDTO.setPersonId(movie1Person3.getId());
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
        movie1Role4CreateDTO.setPersonId(movie1Person4.getId());
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
        movie1Role5CreateDTO.setPersonId(movie1Person5.getId());
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
        movie1Role6CreateDTO.setPersonId(movie1Person6.getId());
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
        movie1Role7CreateDTO.setPersonId(movie1Person7.getId());
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
        movie1Role8CreateDTO.setPersonId(movie1Person8.getId());
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
        movie1Role9CreateDTO.setPersonId(movie1Person9.getId());
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
        newsCreateDTO.setDescription("In the distant future, the crew of the commercial spaceship Nostrom are on " +
                "their way home when they pick up a distress call from a distant moon. The crew are under obligation " +
                "to investigate and the spaceship descends on the moon afterwards. After a rough landing, three crew " +
                "members leave the spaceship to explore the area on the moon. At the same time as they discover a " +
                "hive colony of some unknown creature, the ship's computer deciphers the message to be a warning, not" +
                " a distress call. When one of the eggs is disturbed, the crew realizes that they are not alone on " +
                "the spaceship and they must deal with the consequences.");
        newsCreateDTO.setPublisherId(c1.getId());
        newsCreateDTO.setPublished(Instant.now());
        newsCreateDTO.setMovieId(movie1.getId());

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

        // Check rating before feedback
        news = performRequest("news/" + news.getId(),
                HttpMethod.GET,
                null,
                u2.getEmail(),
                u2Password,
                NewsReadDTO.class);

        Assert.assertEquals(0, news.getNewsRating(), Double.MIN_NORMAL);
        Assert.assertEquals(0, news.getLikesCount(), Double.MIN_NORMAL);

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

        // Check rating after feedback
        news = performRequest("news/" + news.getId(),
                HttpMethod.GET,
                null,
                u2.getEmail(),
                u2Password,
                NewsReadDTO.class);

        Assert.assertEquals(1, news.getNewsRating(), Double.MIN_NORMAL);
        Assert.assertEquals(2, news.getLikesCount(), Double.MIN_NORMAL);

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

        // Check that feedback was cancelled (deleted)
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", getBasicAuthorizationHeaderValue(u3.getEmail(), u3Password));
        HttpEntity<?> httpEntity = new HttpEntity<>(headers);

        Assertions.assertThatThrownBy(() -> new RestTemplate().exchange("http://localhost:8080/api/v1/news-feedbacks/"
                + u3Feedback.getId(),
                HttpMethod.GET, httpEntity, new ParameterizedTypeReference<NewsFeedbackReadDTO>() {}))
                .isInstanceOf(HttpClientErrorException.class).extracting("statusCode")
                .isEqualTo(HttpStatus.NOT_FOUND);

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

        // FINAL_13
        // u3 помечает слово в новости, что в нем есть ошибка, без указания правильного варианта
        UserTypoRequestCreateDTO userTypoRequestCreateDTO13U3 = new UserTypoRequestCreateDTO();
        userTypoRequestCreateDTO13U3.setNewsId(news.getId());
        userTypoRequestCreateDTO13U3.setRequesterId(u3.getId());
        userTypoRequestCreateDTO13U3.setSourceText("Nostrom");

        UserTypoRequestReadDTO u3TypoRequest = performRequest("news/" + news.getId() + "/user-typo-requests",
                HttpMethod.POST,
                userTypoRequestCreateDTO13U3,
                u3.getEmail(),
                u3Password,
                UserTypoRequestReadDTO.class);

        Assertions.assertThat(userTypoRequestCreateDTO13U3).isEqualToComparingFieldByField(u3TypoRequest);

        // FINAL_14
        // 14. с1 просматривает сигналы от пользователей и замечает новый сигнал
        List<UserTypoRequestReadDTO> allTypoRequests = performRequestForList("news/" + news.getId() +
                        "/user-typo-requests",
                        HttpMethod.GET,
                        null,
                        c1.getEmail(),
                        c1Password,
                        new ParameterizedTypeReference<List<UserTypoRequestReadDTO>>() {});

        Assertions.assertThat(allTypoRequests).extracting(UserTypoRequestReadDTO::getId)
                .containsAnyOf(u3TypoRequest.getId());

        // FINAL_15
        // 15. c1 начинает рассмотрение сигнала и исправляет ошибку

        UserTypoRequestPatchDTO userTypoRequestPatchDTO = new UserTypoRequestPatchDTO();
        userTypoRequestPatchDTO.setModeratorTypoReviewStatusType(ModeratorTypoReviewStatusType.IN_REVIEW);
        userTypoRequestPatchDTO.setModeratorId(c1.getId());

        UserTypoRequestReadDTO c1TypoForFix = performRequest("news/" + news.getId() + "/user-typo-requests/"
                        + u3TypoRequest.getId(),
                        HttpMethod.PATCH,
                        userTypoRequestPatchDTO,
                        c1.getEmail(),
                        c1Password,
                        UserTypoRequestReadDTO.class);

        Assertions.assertThat(c1TypoForFix).extracting(UserTypoRequestReadDTO::getModeratorTypoReviewStatusType)
                .isEqualTo(ModeratorTypoReviewStatusType.IN_REVIEW);
        Assertions.assertThat(c1TypoForFix).extracting(UserTypoRequestReadDTO::getModeratorId)
                .isEqualTo(c1.getId());

        UserTypoRequestPutDTO userTypoRequestPutDTO = new UserTypoRequestPutDTO();
        userTypoRequestPutDTO.setApprovedText("Nostromo");
        userTypoRequestPutDTO.setModeratorTypoReviewStatusType(ModeratorTypoReviewStatusType.FIXED);
        userTypoRequestPutDTO.setFixAppliedDate(Instant.now());

        UserTypoRequestReadDTO c1TypoFix = performRequest("moderator/" + c1.getId() + "/user-typo-requests/"
                        + u3TypoRequest.getId(),
                        HttpMethod.PUT,
                        userTypoRequestPutDTO,
                        c1.getEmail(),
                        c1Password,
                        UserTypoRequestReadDTO.class);

        NewsReadDTO newsAfterFix = performRequest("news/" + news.getId(),
                HttpMethod.GET,
                null,
                c1.getEmail(),
                c1Password,
                NewsReadDTO.class);

        Assertions.assertThat(newsAfterFix.getDescription()).contains(userTypoRequestPutDTO.getApprovedText());

        // FINAL_16
        // 16. c1 просматривает сигналы от пользователей - теперь список сигналов пуст
        allTypoRequests = performRequestForList("/user-typo-requests",
                HttpMethod.GET,
                null,
                c1.getEmail(),
                c1Password,
                new ParameterizedTypeReference<List<UserTypoRequestReadDTO>>() {});

        Assert.assertTrue(allTypoRequests.isEmpty());

        // FINAL_17
        // 17. Незарегистрированный пользователь просматривает контент новости и также видит,
        // что двоим она понравилась и одному не понравилась.
        NewsReadDTO guestNewsRead = performRequest("news/" + news.getId(),
                HttpMethod.GET,
                null,
                null,
                null,
                NewsReadDTO.class);

        Assert.assertEquals(1, guestNewsRead.getDislikesCount(), Double.MIN_NORMAL);
        Assert.assertEquals(2, guestNewsRead.getLikesCount(), Double.MIN_NORMAL);

        // FINAL_18
        // 18. u1 просматривает фильм.
        MovieReadDTO u1Movie = performRequest("movies/" + movie1.getId(),
                HttpMethod.GET,
                null,
                u1.getEmail(),
                u1Password,
                MovieReadDTO.class);

        Assertions.assertThat(movie1).isEqualToIgnoringGivenFields(u1Movie, "updatedAt");

        // FINAL_19
        // 19. u1 пишет отзыв на фильм и ставит высокую оценку. В отзыве один из фрагментов помечается как спойлер.
        MovieReviewCreateDTO movieReviewCreateDTO = new MovieReviewCreateDTO();
        movieReviewCreateDTO.setMovieId(u1Movie.getId());
        movieReviewCreateDTO.setPortalUserId(u1.getId());
        movieReviewCreateDTO.setTextReview("Good movie. Only one person survives. Ripley.");

        MovieReviewReadDTO movieReview = performRequest("moviereviews",
                HttpMethod.POST,
                movieReviewCreateDTO,
                u1.getEmail(),
                u1Password,
                MovieReviewReadDTO.class);

        Assertions.assertThat(movieReviewCreateDTO).isEqualToComparingFieldByField(movieReview);

        MovieVoteCreateDTO movieVoteCreateDTO = new MovieVoteCreateDTO();
        movieVoteCreateDTO.setMovieId(u1Movie.getId());
        movieVoteCreateDTO.setPortalUserId(u1.getId());
        movieVoteCreateDTO.setRating(UserVoteRatingType.R10);

        MovieVoteReadDTO u1Vote = performRequest("movievotes",
                HttpMethod.POST,
                movieVoteCreateDTO,
                u1.getEmail(),
                u1Password,
                MovieVoteReadDTO.class);

        Assertions.assertThat(movieVoteCreateDTO).isEqualToComparingFieldByField(u1Vote);

        MovieSpoilerDataCreateDTO spoilerDataCreateDTO = new MovieSpoilerDataCreateDTO();
        spoilerDataCreateDTO.setMovieReviewId(movieReview.getId());
        spoilerDataCreateDTO.setStartIndex(38);
        spoilerDataCreateDTO.setEndIndex(44);

        MovieSpoilerDataReadDTO u1Spoiler = performRequest("moviespoilerdata",
                HttpMethod.POST,
                spoilerDataCreateDTO,
                u1.getEmail(),
                u1Password,
                MovieSpoilerDataReadDTO.class);

        Assertions.assertThat(spoilerDataCreateDTO).isEqualToComparingFieldByField(u1Spoiler);

        // FINAL_20
        // 20. u2 просматривает фильм, отзыв не виден, т.к. он пока на модерации.
        List<MovieReviewReadDTO> movieReviewReadDTO = performRequestForList("movies/" + movie1.getId()
                        + "/movie-reviews",
                        HttpMethod.GET,
                        null,
                        u2.getEmail(),
                        u2Password,
                        new ParameterizedTypeReference<List<MovieReviewReadDTO>>() {});

        Assert.assertTrue(movieReviewReadDTO.isEmpty());

        // FINAL_21
        // 21. u2 ставит оценку фильму.
        movieVoteCreateDTO = new MovieVoteCreateDTO();
        movieVoteCreateDTO.setMovieId(u1Movie.getId());
        movieVoteCreateDTO.setPortalUserId(u1.getId());
        movieVoteCreateDTO.setRating(UserVoteRatingType.R9);

        MovieVoteReadDTO u2Vote = performRequest("movievotes",
                HttpMethod.POST,
                movieVoteCreateDTO,
                u2.getEmail(),
                u2Password,
                MovieVoteReadDTO.class);

        Assertions.assertThat(movieVoteCreateDTO).isEqualToComparingFieldByField(u2Vote);

        // FINAL_22
        // 22. m1 просматривает неподтвержденные отзывы и замечает один.
        movieReviewReadDTO = performRequestForList("movies/" + movie1.getId()
                        + "/movie-reviews/unmoderated",
                HttpMethod.GET,
                null,
                m1.getEmail(),
                m1Password,
                new ParameterizedTypeReference<List<MovieReviewReadDTO>>() {});

        Assertions.assertThat(movieReviewReadDTO.size()).isEqualTo(1);

        // FINAL_23
        // 23. m1 начинает просмотр отзыва и подтверждает, что он хороший. Пользователь u1 помечается как
        //     заслуживающий доверия, так что последующие его отзывы будут публиковаться сразу.
        MovieReviewPatchDTO movieReviewPatchDTO = new MovieReviewPatchDTO();
        movieReviewPatchDTO.setModeratedStatus(UserModeratedStatusType.INREVIEW);

        movieReview = performRequest("moviereviews/" + movieReview.getId(),
                HttpMethod.PATCH,
                movieReviewPatchDTO,
                m1.getEmail(),
                m1Password,
                MovieReviewReadDTO.class);

        Assert.assertEquals(movieReviewPatchDTO.getModeratedStatus(), movieReview.getModeratedStatus());

        movieReviewPatchDTO = new MovieReviewPatchDTO();
        movieReviewPatchDTO.setModeratedStatus(UserModeratedStatusType.SUCCESS);

        movieReview = performRequest("moviereviews/" + movieReview.getId(),
                HttpMethod.PATCH,
                movieReviewPatchDTO,
                m1.getEmail(),
                m1Password,
                MovieReviewReadDTO.class);

        Assert.assertEquals(movieReviewPatchDTO.getModeratedStatus(), movieReview.getModeratedStatus());

        // FINAL_24
        // 24. u3 просматривает фильм и ставит оценку. Она видит подтвержденный отзыв.
        MovieReadDTO u3Movie = performRequest("movies/" + movie1.getId(),
                HttpMethod.GET,
                null,
                u3.getEmail(),
                u3Password,
                MovieReadDTO.class);

        Assertions.assertThat(u3Movie).isEqualToIgnoringGivenFields(movie1,"updatedAt", "averageRating");

        movieVoteCreateDTO = new MovieVoteCreateDTO();
        movieVoteCreateDTO.setMovieId(u3Movie.getId());
        movieVoteCreateDTO.setPortalUserId(u3.getId());
        movieVoteCreateDTO.setRating(UserVoteRatingType.R8);

        MovieVoteReadDTO u3Vote = performRequest("movievotes",
                HttpMethod.POST,
                movieVoteCreateDTO,
                u3.getEmail(),
                u3Password,
                MovieVoteReadDTO.class);

        Assertions.assertThat(movieVoteCreateDTO).isEqualToComparingFieldByField(u3Vote);

        movieReviewReadDTO = performRequestForList("movies/" + movie1.getId()
                        + "/movie-reviews",
                HttpMethod.GET,
                null,
                u3.getEmail(),
                u3Password,
                new ParameterizedTypeReference<List<MovieReviewReadDTO>>() {});

        Assertions.assertThat(movieReviewReadDTO.size()).isEqualTo(1);

        // FINAL_25
        // 25. u3 помечает, что отзыв ей нравится.
        MovieReviewFeedbackCreateDTO movieReviewFeedbackCreateDTO = new MovieReviewFeedbackCreateDTO();
        movieReviewFeedbackCreateDTO.setMovieId(movie1.getId());
        movieReviewFeedbackCreateDTO.setMovieReviewId(movieReview.getId());
        movieReviewFeedbackCreateDTO.setPortalUserId(u3.getId());
        movieReviewFeedbackCreateDTO.setIsLiked(true);

        MovieReviewFeedbackReadDTO u3MovieReviewFeedback = performRequest("movie-reviews/" + movieReview.getId()
                + "/movie-review-feedbacks",
                HttpMethod.POST,
                movieReviewFeedbackCreateDTO,
                u3.getEmail(),
                u3Password,
                MovieReviewFeedbackReadDTO.class);

        Assertions.assertThat(movieReviewFeedbackCreateDTO).isEqualToComparingFieldByField(u3MovieReviewFeedback);

        // FINAL_26
        // 26. Незарегистрированный пользователь просматривает фильм и видит его среднюю оценку
        //     (если у вас расчет среднего выполняется по cron джобе, то поставьте, чтобы для теста
        //     она запускалась раз в пару секунд)
        MovieReadDTO guestMovie = performRequest("movies/" + movie1.getId(),
                HttpMethod.GET,
                null,
                null,
                null,
                MovieReadDTO.class);

        //Assert.assertEquals(8.0, guestMovie.getAverageRating(), Double.MIN_NORMAL);
        Assert.assertTrue(guestMovie.getAverageRating() >= 8.0);

        // FINAL_27
        // 27. c1 импортирует другой фильм, такой, чтобы один из актеров играл в первом, персонажей, актеров, crew и тд.
        //     Для этого создать эндпонт контент менеджера для импорта фильма по id во внешней системе.
        UUID internalMovieId = performRequest("external-movies-import/679",
                HttpMethod.GET,
                null,
                c1.getEmail(),
                c1Password,
                UUID.class);

        Assert.assertNotNull(internalMovieId);

        List<RoleReadDTO> importMovieRoles = performRequestForList("movie/" + internalMovieId + "/roles",
                HttpMethod.GET,
                null,
                c1.getEmail(),
                c1Password,
                new ParameterizedTypeReference<List<RoleReadDTO>>() {});

        Assertions.assertThat(importMovieRoles.size()).isEqualTo(27);

        // FINAL_28
        // 28. u1 ставит фильму низкую оценку, и пишет гневный отзыв.
        movieVoteCreateDTO = new MovieVoteCreateDTO();
        movieVoteCreateDTO.setMovieId(internalMovieId);
        movieVoteCreateDTO.setPortalUserId(u1.getId());
        movieVoteCreateDTO.setRating(UserVoteRatingType.R2);

        u1Vote = performRequest("movievotes",
                HttpMethod.POST,
                movieVoteCreateDTO,
                u1.getEmail(),
                u1Password,
                MovieVoteReadDTO.class);

        Assertions.assertThat(movieVoteCreateDTO).isEqualToComparingFieldByField(u1Vote);

        movieReviewCreateDTO = new MovieReviewCreateDTO();
        movieReviewCreateDTO.setMovieId(internalMovieId);
        movieReviewCreateDTO.setPortalUserId(u1.getId());
        movieReviewCreateDTO.setTextReview("The movie is shit. Fuck the director who made it.");

        movieReview = performRequest("moviereviews",
                HttpMethod.POST,
                movieReviewCreateDTO,
                u1.getEmail(),
                u1Password,
                MovieReviewReadDTO.class);

        Assertions.assertThat(movieReviewCreateDTO).isEqualToComparingFieldByField(movieReview);

        // FINAL_29
        // 29. u2, u3 оценивают фильм. Отзыв виден сразу. Оба отправляют сигнал модератору, что в отзыве есть маты.
        movieVoteCreateDTO = new MovieVoteCreateDTO();
        movieVoteCreateDTO.setMovieId(internalMovieId);
        movieVoteCreateDTO.setPortalUserId(u2.getId());
        movieVoteCreateDTO.setRating(UserVoteRatingType.R6);

        u2Vote = performRequest("movievotes",
                HttpMethod.POST,
                movieVoteCreateDTO,
                u2.getEmail(),
                u2Password,
                MovieVoteReadDTO.class);

        Assertions.assertThat(movieVoteCreateDTO).isEqualToComparingFieldByField(u2Vote);

        movieVoteCreateDTO = new MovieVoteCreateDTO();
        movieVoteCreateDTO.setMovieId(internalMovieId);
        movieVoteCreateDTO.setPortalUserId(u3.getId());
        movieVoteCreateDTO.setRating(UserVoteRatingType.R6);

        u3Vote = performRequest("movievotes",
                HttpMethod.POST,
                movieVoteCreateDTO,
                u3.getEmail(),
                u3Password,
                MovieVoteReadDTO.class);

        Assertions.assertThat(movieVoteCreateDTO).isEqualToComparingFieldByField(u3Vote);

        // Both request reviews for movie
        movieReviewReadDTO = performRequestForList("movies/" + internalMovieId
                        + "/movie-reviews",
                HttpMethod.GET,
                null,
                u2.getEmail(),
                u2Password,
                new ParameterizedTypeReference<List<MovieReviewReadDTO>>() {});

        Assertions.assertThat(movieReviewReadDTO.size()).isEqualTo(1);

        movieReviewReadDTO = performRequestForList("movies/" + internalMovieId
                        + "/movie-reviews",
                HttpMethod.GET,
                null,
                u3.getEmail(),
                u3Password,
                new ParameterizedTypeReference<List<MovieReviewReadDTO>>() {});

        Assertions.assertThat(movieReviewReadDTO.size()).isEqualTo(1);

        // Both send compliant on this review
        MovieReviewCompliantCreateDTO compliantCreateDTO = new MovieReviewCompliantCreateDTO();
        compliantCreateDTO.setMovieId(internalMovieId);
        compliantCreateDTO.setMovieReviewId(movieReview.getId());
        compliantCreateDTO.setPortalUserId(u2.getId());
        compliantCreateDTO.setDescription("Review contains filthy language!");

        MovieReviewCompliantReadDTO compliant = performRequest("movie-reviews/" + movieReview.getId()
                        + "/movie-review-compliants",
                HttpMethod.POST,
                compliantCreateDTO,
                u2.getEmail(),
                u2Password,
                MovieReviewCompliantReadDTO.class);

        Assertions.assertThat(compliantCreateDTO).isEqualToComparingFieldByField(compliant);
        Assert.assertEquals(compliant.getModeratedStatus(), UserModeratedStatusType.CREATED);

        compliantCreateDTO = new MovieReviewCompliantCreateDTO();
        compliantCreateDTO.setMovieId(internalMovieId);
        compliantCreateDTO.setMovieReviewId(movieReview.getId());
        compliantCreateDTO.setPortalUserId(u3.getId());
        compliantCreateDTO.setDescription("Review contains filthy language!");

        compliant = performRequest("movie-reviews/" + movieReview.getId()
                        + "/movie-review-compliants",
                HttpMethod.POST,
                compliantCreateDTO,
                u3.getEmail(),
                u3Password,
                MovieReviewCompliantReadDTO.class);

        Assertions.assertThat(compliantCreateDTO).isEqualToComparingFieldByField(compliant);
        Assert.assertEquals(compliant.getModeratedStatus(), UserModeratedStatusType.CREATED);

        // FINAL_30
        // 30. m1 просматривает список сигналов к нему, у видит 2 новых сигнала.
        List<MovieReviewCompliantReadDTO> compliants = performRequestForList("moderator/" + m1.getId()
                        + "/movie-review-compliants",
                HttpMethod.GET,
                null,
                m1.getEmail(),
                m1Password,
                new ParameterizedTypeReference<List<MovieReviewCompliantReadDTO>>() {});

        Assertions.assertThat(compliants.size()).isEqualTo(2);

        // FINAL_31
        // 31. m1 начинает рассмотрение одного из сигналов. Видит маты, удаляет обзор и банит u1.
        MovieReviewCompliantPatchDTO compliantPatch = new MovieReviewCompliantPatchDTO();
        compliantPatch.setModeratorId(m1.getId());
        compliantPatch.setModeratedStatus(UserModeratedStatusType.INREVIEW);

        MovieReviewCompliantReadDTO m1CompliantReview = performRequest("moderator/" + m1.getId()
                        + "/movie-review-compliants/" + compliant.getId(),
                HttpMethod.PATCH,
                compliantPatch,
                m1.getEmail(),
                m1Password,
                MovieReviewCompliantReadDTO.class);

        Assert.assertEquals(compliantPatch.getModeratedStatus(), m1CompliantReview.getModeratedStatus());
        Assert.assertEquals(compliantPatch.getModeratorId(), m1CompliantReview.getModeratorId());

        m1CompliantReview = performRequest("moderator/" + m1.getId()
                        + "/movie-review-compliants/" + m1CompliantReview.getId(),
                HttpMethod.DELETE,
                null,
                m1.getEmail(),
                m1Password,
                MovieReviewCompliantReadDTO.class);

        // Check that review was deleted - NOT FOUND
        headers = new HttpHeaders();
        headers.add("Authorization", getBasicAuthorizationHeaderValue(m1.getEmail(), m1Password));
        HttpEntity<?> httpEntity2 = new HttpEntity<>(headers);
        UUID reviewId =  movieReview.getId();

        Assertions.assertThatThrownBy(() -> new RestTemplate().exchange("http://localhost:8080/api/v1/moviereviews/"
                        + reviewId,
                HttpMethod.GET, httpEntity2, new ParameterizedTypeReference<MovieReviewReadDTO>() {}))
                .isInstanceOf(HttpClientErrorException.class).extracting("statusCode")
                .isEqualTo(HttpStatus.NOT_FOUND);

        PortalUserPatchDTO portalUserPatchDTO = new PortalUserPatchDTO();
        portalUserPatchDTO.setUserConfidence(UserConfidenceType.BLOCKED);

        u1 = performRequest("moderator/" + m1.getId() + "/portal-users/" + u1.getId(),
                HttpMethod.PATCH,
                portalUserPatchDTO,
                m1.getEmail(),
                m1Password,
                PortalUserReadDTO.class);

        // Check confidence level
        List<PortalUserReadDTO> blockedUsers = performRequestForList("moderator/" + m1.getId() + "/portal-users",
                HttpMethod.GET,
                null,
                m1.getEmail(),
                m1Password,
                new ParameterizedTypeReference<List<PortalUserReadDTO>>() {});

        Assertions.assertThat(blockedUsers.size()).isEqualTo(1);
        Assertions.assertThat(blockedUsers).extracting(PortalUserReadDTO::getUserConfidence)
                .contains(UserConfidenceType.BLOCKED);
        Assertions.assertThat(blockedUsers).extracting(PortalUserReadDTO::getId).contains(u1.getId());

        // FINAL_32
        // 32. m1 просматривает список сигналов к модераторам - теперь этот список пуст.
        compliants = performRequestForList("moderator/" + m1.getId()
                        + "/movie-review-compliants",
                HttpMethod.GET,
                null,
                m1.getEmail(),
                m1Password,
                new ParameterizedTypeReference<List<MovieReviewCompliantReadDTO>>() {});

        Assertions.assertThat(compliants.size()).isEqualTo(0);

        // FINAL_33
        // 33. u2, u3 оценивают роли актера, являющегося общим для двух фильмов. Роли оцениваются в 2 фильмах.
        List<RoleReadDTO> rolesOfOnePerson = performRequestForList("person/" + movie1Person2.getId() + "/roles",
                HttpMethod.GET,
                null,
                u2.getEmail(),
                u2Password,
                new ParameterizedTypeReference<List<RoleReadDTO>>() {});

        Assertions.assertThat(rolesOfOnePerson.size()).isEqualTo(2);

        UUID u2Id = u2.getId();
        String u2Email = u2.getEmail();
        UUID u3Id = u3.getId();
        String u3Email = u3.getEmail();

        rolesOfOnePerson.stream().forEach(role -> {
            RoleVoteCreateDTO roleVoteCreateDTO = new RoleVoteCreateDTO();
            roleVoteCreateDTO.setRoleId(role.getId());
            roleVoteCreateDTO.setPortalUserId(u2Id);
            roleVoteCreateDTO.setRating(UserVoteRatingType.R8);

            RoleVoteReadDTO roleVote = performRequest("rolevotes",
                    HttpMethod.POST,
                    roleVoteCreateDTO,
                    u2Email,
                    u2Password,
                    RoleVoteReadDTO.class);

            Assertions.assertThat(roleVoteCreateDTO).isEqualToComparingFieldByField(roleVote);

            roleVoteCreateDTO = new RoleVoteCreateDTO();
            roleVoteCreateDTO.setRoleId(role.getId());
            roleVoteCreateDTO.setPortalUserId(u3Id);
            roleVoteCreateDTO.setRating(UserVoteRatingType.R8);

            roleVote = performRequest("rolevotes",
                    HttpMethod.POST,
                    roleVoteCreateDTO,
                    u3Email,
                    u3Password,
                    RoleVoteReadDTO.class);

            Assertions.assertThat(roleVoteCreateDTO).isEqualToComparingFieldByField(roleVote);
        });

        // FINAL_34
        // 34. u1 пытается оценить роль актера, но запрос выдает ему ошибку 403, т.к. он забанен.
        RoleVoteCreateDTO roleVoteCreateDTO = new RoleVoteCreateDTO();
        roleVoteCreateDTO.setRoleId(movie1Role2.getId()); // Ripley from movie1
        roleVoteCreateDTO.setPortalUserId(u1.getId());
        roleVoteCreateDTO.setRating(UserVoteRatingType.R8);

        headers = new HttpHeaders();
        headers.add("Authorization", getBasicAuthorizationHeaderValue(u1.getEmail(), u1Password));
        HttpEntity<?> httpEntity34 = new HttpEntity<>(roleVoteCreateDTO, headers);

        UUID u1Id = u1.getId();
        Assertions.assertThatThrownBy(() -> new RestTemplate().exchange(
                "http://localhost:8080/api/v1/role/" + movie1Role2.getId() + "/portal-user/" + u1Id +
                        "/role-votes",
                HttpMethod.POST,
                httpEntity34,
                new ParameterizedTypeReference<RoleVoteReadDTO>() {}))
                .isInstanceOf(HttpClientErrorException.class).extracting("statusCode")
                .isEqualTo(HttpStatus.FORBIDDEN);

        // FINAL_35
        // 35. Незарегистрированный пользователь заходит на страницу актера, являющегося общим для 2 фильмов,
        //     читает информацию о нем и видит среднюю оценку по 2 фильмам в которых он играл.
        //     Также видит среднюю оценку по ролям этого актера.
        Thread.sleep(1000); //There are about 106 Persons(+manual input), job need some time just for iterate through it
        PersonReadDTO person = performRequest("persons/" + movie1Person2.getId(),
                HttpMethod.GET,
                null,
                null,
                null,
                PersonReadDTO.class);

        Assertions.assertThat(person).isEqualToIgnoringGivenFields(movie1Person2,"updatedAt",
                "averageMovieRating", "averageRoleRating");
        Assert.assertTrue(person.getAverageMovieRating() >= 5.0);
        Assert.assertTrue(person.getAverageRoleRating() >= 7.0);
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
        ResponseEntity<List<T>> response = restTemplate.exchange("http://localhost:8080/api/v1/" + url,
                httpMethod, httpEntity, returnClass);
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
        return response.getBody();
    }
}
