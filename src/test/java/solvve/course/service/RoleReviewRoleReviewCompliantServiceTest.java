package solvve.course.service;

import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import solvve.course.domain.*;
import solvve.course.dto.RoleReviewCompliantCreateDTO;
import solvve.course.dto.RoleReviewCompliantPatchDTO;
import solvve.course.dto.RoleReviewCompliantPutDTO;
import solvve.course.dto.RoleReviewCompliantReadDTO;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.repository.RoleReviewCompliantRepository;
import solvve.course.utils.TestObjectsFactory;

import java.util.List;
import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@Sql(statements = {"delete from role_review_compliant",
        "delete from role_review",
        "delete from portal_user",
        "delete from user_type",
        "delete from role",
        "delete from person"},
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class RoleReviewRoleReviewCompliantServiceTest {

    @Autowired
    private RoleReviewCompliantRepository roleReviewCompliantRepository;

    @Autowired
    private RoleReviewRoleReviewCompliantService roleReviewRoleReviewCompliantService;

    @Autowired
    private TestObjectsFactory testObjectsFactory;

    @Test
    public void testGetRoleReviewCompliant() {
        PortalUser portalUser = testObjectsFactory.createPortalUser();
        Person person = testObjectsFactory.createPerson();
        Movie movie = testObjectsFactory.createMovie();
        Role role = testObjectsFactory.createRole(person, movie);
        RoleReview roleReview = testObjectsFactory.createRoleReview(portalUser, role);
        RoleReviewCompliant roleReviewCompliant =
                testObjectsFactory.createRoleReviewCompliant(portalUser, role, roleReview);

        List<RoleReviewCompliantReadDTO> readDTO =
                roleReviewRoleReviewCompliantService.getRoleReviewRoleReviewCompliant(roleReview.getId());
        Assertions.assertThat(readDTO).extracting("id").containsExactlyInAnyOrder(roleReviewCompliant.getId());
    }

    @Test(expected = EntityNotFoundException.class)
    public void testGetRoleReviewCompliantWrongId() {
        roleReviewRoleReviewCompliantService.getRoleReviewRoleReviewCompliant(UUID.randomUUID());
    }

    @Test
    public void testCreateRoleReviewCompliant() {
        PortalUser portalUser = testObjectsFactory.createPortalUser();
        Person person = testObjectsFactory.createPerson();
        Movie movie = testObjectsFactory.createMovie();
        Role role = testObjectsFactory.createRole(person,movie);
        RoleReview roleReview = testObjectsFactory.createRoleReview(portalUser, role);

        RoleReviewCompliantCreateDTO create = new RoleReviewCompliantCreateDTO();
        create.setPortalUserId(portalUser.getId());
        create.setRoleId(role.getId());
        create.setRoleReviewId(roleReview.getId());
        create.setDescription("Just punish him!");
        create.setModeratedStatus(UserModeratedStatusType.SUCCESS);
        create.setModeratorId(portalUser.getId());

        RoleReviewCompliantReadDTO read =
                roleReviewRoleReviewCompliantService.createRoleReviewRoleReviewCompliant(roleReview.getId(), create);
        Assertions.assertThat(create).isEqualToComparingFieldByField(read);

        RoleReviewCompliant roleReviewCompliant = roleReviewCompliantRepository.findById(read.getId()).get();
        Assertions.assertThat(read).isEqualToIgnoringGivenFields(roleReviewCompliant,
                "portalUserId", "roleId", "roleReviewId", "moderatorId");
        Assertions.assertThat(read.getPortalUserId())
                .isEqualToComparingFieldByField(roleReviewCompliant.getPortalUser().getId());
        Assertions.assertThat(read.getRoleId())
                .isEqualToComparingFieldByField(roleReviewCompliant.getRole().getId());
        Assertions.assertThat(read.getRoleReviewId())
                .isEqualToComparingFieldByField(roleReviewCompliant.getRoleReview().getId());
        Assertions.assertThat(read.getModeratorId())
                .isEqualToComparingFieldByField(roleReviewCompliant.getModerator().getId());
    }

    @Test
    public void testPatchRoleReviewCompliant() {
        PortalUser portalUser = testObjectsFactory.createPortalUser();
        Person person = testObjectsFactory.createPerson();
        Movie movie = testObjectsFactory.createMovie();
        Role role = testObjectsFactory.createRole(person,movie);
        RoleReview roleReview = testObjectsFactory.createRoleReview(portalUser, role);
        RoleReviewCompliant roleReviewCompliant =
                testObjectsFactory.createRoleReviewCompliant(portalUser, role, roleReview);

        RoleReviewCompliantPatchDTO patch = new RoleReviewCompliantPatchDTO();
        patch.setPortalUserId(portalUser.getId());
        patch.setRoleId(role.getId());
        patch.setRoleReviewId(roleReview.getId());
        patch.setDescription("Just punish him!");
        patch.setModeratedStatus(UserModeratedStatusType.SUCCESS);
        patch.setModeratorId(portalUser.getId());
        RoleReviewCompliantReadDTO read =
                roleReviewRoleReviewCompliantService.patchRoleReviewRoleReviewCompliant(roleReview.getId(),
                        roleReviewCompliant.getId(),
                        patch);

        Assertions.assertThat(patch).isEqualToComparingFieldByField(read);

        roleReviewCompliant = roleReviewCompliantRepository.findById(read.getId()).get();
        Assertions.assertThat(roleReviewCompliant).isEqualToIgnoringGivenFields(roleReviewCompliant,
                "portalUser", "role", "roleReview", "moderator");
        Assertions.assertThat(roleReviewCompliant.getPortalUser().getId())
                .isEqualToComparingFieldByField(read.getPortalUserId());
        Assertions.assertThat(roleReviewCompliant.getRole().getId())
                .isEqualToComparingFieldByField(read.getRoleId());
        Assertions.assertThat(roleReviewCompliant.getRoleReview().getId())
                .isEqualToComparingFieldByField(read.getRoleReviewId());
        Assertions.assertThat(roleReviewCompliant.getModerator().getId())
                .isEqualToComparingFieldByField(read.getModeratorId());
    }

    @Test
    public void testPatchRoleReviewCompliantEmptyPatch() {
        PortalUser portalUser = testObjectsFactory.createPortalUser();
        Person person = testObjectsFactory.createPerson();
        Movie movie = testObjectsFactory.createMovie();
        Role role = testObjectsFactory.createRole(person, movie);
        RoleReview roleReview = testObjectsFactory.createRoleReview(portalUser, role);
        RoleReviewCompliant roleReviewCompliant =
                testObjectsFactory.createRoleReviewCompliant(portalUser, role, roleReview);

        RoleReviewCompliantPatchDTO patch = new RoleReviewCompliantPatchDTO();
        RoleReviewCompliantReadDTO read =
                roleReviewRoleReviewCompliantService.patchRoleReviewRoleReviewCompliant(roleReview.getId(),
                        roleReviewCompliant.getId(),
                        patch);

        Assert.assertNotNull(read.getPortalUserId());
        Assert.assertNotNull(read.getRoleId());
        Assert.assertNotNull(read.getRoleReviewId());
        Assert.assertNotNull(read.getDescription());
        Assert.assertNotNull(read.getModeratedStatus());
        Assert.assertNotNull(read.getModeratorId());

        RoleReviewCompliant roleReviewCompliantAfterUpdate =
                roleReviewCompliantRepository.findById(read.getId()).get();

        Assert.assertNotNull(roleReviewCompliantAfterUpdate.getPortalUser());
        Assert.assertNotNull(roleReviewCompliantAfterUpdate.getRole());
        Assert.assertNotNull(roleReviewCompliantAfterUpdate.getRoleReview());
        Assert.assertNotNull(roleReviewCompliantAfterUpdate.getDescription());
        Assert.assertNotNull(roleReviewCompliantAfterUpdate.getModeratedStatus());
        Assert.assertNotNull(roleReviewCompliantAfterUpdate.getModerator());

        Assertions.assertThat(roleReviewCompliant).isEqualToIgnoringGivenFields(roleReviewCompliantAfterUpdate,
                "portalUser", "role", "roleReview", "moderator");
        Assertions.assertThat(roleReviewCompliant.getPortalUser().getId())
                .isEqualTo(roleReviewCompliantAfterUpdate.getPortalUser().getId());
        Assertions.assertThat(roleReviewCompliant.getRole().getId())
                .isEqualTo(roleReviewCompliantAfterUpdate.getRole().getId());
        Assertions.assertThat(roleReviewCompliant.getRoleReview().getId())
                .isEqualTo(roleReviewCompliantAfterUpdate.getRoleReview().getId());
        Assertions.assertThat(roleReviewCompliant.getModerator().getId())
                .isEqualTo(roleReviewCompliantAfterUpdate.getModerator().getId());
    }

    @Test
    public void testDeleteRoleReviewCompliant() {
        PortalUser portalUser = testObjectsFactory.createPortalUser();
        Person person = testObjectsFactory.createPerson();
        Movie movie = testObjectsFactory.createMovie();
        Role role = testObjectsFactory.createRole(person,movie);
        RoleReview roleReview = testObjectsFactory.createRoleReview(portalUser, role);
        RoleReviewCompliant roleReviewCompliant =
                testObjectsFactory.createRoleReviewCompliant(portalUser, role, roleReview);

        roleReviewRoleReviewCompliantService.deleteRoleReviewRoleReviewCompliant(roleReview.getId(),
                roleReviewCompliant.getId());
        Assert.assertFalse(roleReviewCompliantRepository.existsById(roleReviewCompliant.getId()));
    }

    @Test(expected = EntityNotFoundException.class)
    public void testDeleteRoleReviewCompliantNotFound() {
        roleReviewRoleReviewCompliantService.deleteRoleReviewRoleReviewCompliant(UUID.randomUUID(), UUID.randomUUID());
    }

    @Test
    public void testPutRoleReviewCompliant() {
        PortalUser portalUser = testObjectsFactory.createPortalUser();
        Person person = testObjectsFactory.createPerson();
        Movie movie = testObjectsFactory.createMovie();
        Role role = testObjectsFactory.createRole(person,movie);
        RoleReview roleReview = testObjectsFactory.createRoleReview(portalUser, role);
        RoleReviewCompliant roleReviewCompliant =
                testObjectsFactory.createRoleReviewCompliant(portalUser, role, roleReview);

        RoleReviewCompliantPutDTO put = new RoleReviewCompliantPutDTO();
        put.setPortalUserId(portalUser.getId());
        put.setRoleId(role.getId());
        put.setRoleReviewId(roleReview.getId());
        put.setDescription("Just punish him!");
        put.setModeratedStatus(UserModeratedStatusType.SUCCESS);
        put.setModeratorId(portalUser.getId());
        RoleReviewCompliantReadDTO read =
                roleReviewRoleReviewCompliantService.updateRoleReviewRoleReviewCompliant(roleReview.getId(),
                        roleReviewCompliant.getId()
                        , put);

        Assertions.assertThat(put).isEqualToComparingFieldByField(read);

        roleReviewCompliant = roleReviewCompliantRepository.findById(read.getId()).get();
        Assertions.assertThat(roleReviewCompliant).isEqualToIgnoringGivenFields(roleReviewCompliant,
                "portalUser", "role", "roleReview", "moderator");
        Assertions.assertThat(roleReviewCompliant.getPortalUser().getId())
                .isEqualToComparingFieldByField(read.getPortalUserId());
        Assertions.assertThat(roleReviewCompliant.getRole().getId())
                .isEqualToComparingFieldByField(read.getRoleId());
        Assertions.assertThat(roleReviewCompliant.getRoleReview().getId())
                .isEqualToComparingFieldByField(read.getRoleReviewId());
        Assertions.assertThat(roleReviewCompliant.getModerator().getId())
                .isEqualToComparingFieldByField(read.getModeratorId());
    }

    @Test
    public void testPutRoleReviewCompliantEmptyPut() {
        PortalUser portalUser = testObjectsFactory.createPortalUser();
        Person person = testObjectsFactory.createPerson();
        Movie movie = testObjectsFactory.createMovie();
        Role role = testObjectsFactory.createRole(person, movie);
        RoleReview roleReview = testObjectsFactory.createRoleReview(portalUser, role);
        RoleReviewCompliant roleReviewCompliant =
                testObjectsFactory.createRoleReviewCompliant(portalUser, role, roleReview);

        RoleReviewCompliantPutDTO put = new RoleReviewCompliantPutDTO();
        RoleReviewCompliantReadDTO read =
                roleReviewRoleReviewCompliantService.updateRoleReviewRoleReviewCompliant(roleReview.getId(),
                        roleReviewCompliant.getId()
        , put);

        Assert.assertNotNull(read.getPortalUserId());
        Assert.assertNotNull(read.getRoleId());
        Assert.assertNotNull(read.getRoleReviewId());
        Assert.assertNull(read.getDescription());
        Assert.assertNull(read.getModeratedStatus());
        Assert.assertNull(read.getModeratorId());

        RoleReviewCompliant roleReviewCompliantAfterUpdate =
                roleReviewCompliantRepository.findById(read.getId()).get();

        Assert.assertNotNull(roleReviewCompliantAfterUpdate.getPortalUser());
        Assert.assertNotNull(roleReviewCompliantAfterUpdate.getRole());
        Assert.assertNotNull(roleReviewCompliantAfterUpdate.getRoleReview());
        Assert.assertNull(roleReviewCompliantAfterUpdate.getDescription());
        Assert.assertNull(roleReviewCompliantAfterUpdate.getModeratedStatus());
        Assert.assertNull(roleReviewCompliantAfterUpdate.getModerator());
    }
}
