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
import org.springframework.transaction.annotation.Transactional;
import solvve.course.domain.*;
import solvve.course.dto.*;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.repository.*;
import solvve.course.utils.TestObjectsFactory;

import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@Sql(statements = {"delete from role_spoiler_data",
        " delete from role_review",
        " delete from portal_user",
        " delete from user_type",
        " delete from role",
        " delete from person"},
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class RoleSpoilerDataServiceTest {

    @Autowired
    private RoleSpoilerDataRepository roleSpoilerDataRepository;

    @Autowired
    private RoleSpoilerDataService roleSpoilerDataService;

    @Autowired
    private TestObjectsFactory testObjectsFactory;

    @Transactional
    @Test
    public void testGetRoleSpoilerData() {
        PortalUser portalUser = testObjectsFactory.createPortalUser();
        Person person = testObjectsFactory.createPerson();
        Role role = testObjectsFactory.createRole(person);
        RoleReview roleReview = testObjectsFactory.createRoleReview(portalUser, role);
        RoleSpoilerData roleSpoilerData = testObjectsFactory.createRoleSpoilerData(roleReview);

        RoleSpoilerDataReadDTO readDTO = roleSpoilerDataService.getRoleSpoilerData(roleSpoilerData.getId());
        Assertions.assertThat(readDTO).isEqualToIgnoringGivenFields(roleSpoilerData,
                "roleReviewId");
        Assertions.assertThat(readDTO.getRoleReviewId()).isEqualTo(roleSpoilerData.getRoleReviewId().getId());
    }

    @Test(expected = EntityNotFoundException.class)
    public void testGetRoleSpoilerDataWrongId() {
        roleSpoilerDataService.getRoleSpoilerData(UUID.randomUUID());
    }

    @Transactional
    @Test
    public void testCreateRoleSpoilerData() {
        PortalUser portalUser = testObjectsFactory.createPortalUser();
        Person person = testObjectsFactory.createPerson();
        Role role = testObjectsFactory.createRole(person);
        RoleReview roleReview = testObjectsFactory.createRoleReview(portalUser, role);

        RoleSpoilerDataCreateDTO create = new RoleSpoilerDataCreateDTO();
        create.setRoleReviewId(roleReview.getId());
        create.setStartIndex(100);
        create.setEndIndex(150);

        RoleSpoilerDataReadDTO read = roleSpoilerDataService.createRoleSpoilerData(create);
        Assertions.assertThat(create).isEqualToComparingFieldByField(read);

        RoleSpoilerData roleSpoilerData = roleSpoilerDataRepository.findById(read.getId()).get();
        Assertions.assertThat(read).isEqualToIgnoringGivenFields(roleSpoilerData,
                "roleReviewId");
        Assertions.assertThat(read.getRoleReviewId()).isEqualTo(roleSpoilerData.getRoleReviewId().getId());
    }

    @Transactional
    @Test
    public void testPatchRoleSpoilerData() {
        PortalUser portalUser = testObjectsFactory.createPortalUser();
        Person person = testObjectsFactory.createPerson();
        Role role = testObjectsFactory.createRole(person);
        RoleReview roleReview = testObjectsFactory.createRoleReview(portalUser, role);
        RoleSpoilerData roleSpoilerData = testObjectsFactory.createRoleSpoilerData(roleReview);

        RoleSpoilerDataPatchDTO patch = new RoleSpoilerDataPatchDTO();
        patch.setRoleReviewId(roleReview.getId());
        patch.setStartIndex(100);
        patch.setEndIndex(150);
        RoleSpoilerDataReadDTO read = roleSpoilerDataService.patchRoleSpoilerData(roleSpoilerData.getId(), patch);

        Assertions.assertThat(patch).isEqualToComparingFieldByField(read);

        roleSpoilerData = roleSpoilerDataRepository.findById(read.getId()).get();
        Assertions.assertThat(roleSpoilerData).isEqualToIgnoringGivenFields(read,
                "roleReviewId");
        Assertions.assertThat(roleSpoilerData.getRoleReviewId().getId()).isEqualTo(read.getRoleReviewId());
    }

    @Transactional
    @Test
    public void testPatchRoleSpoilerDataEmptyPatch() {
        PortalUser portalUser = testObjectsFactory.createPortalUser();
        Person person = testObjectsFactory.createPerson();
        Role role = testObjectsFactory.createRole(person);
        RoleReview roleReview = testObjectsFactory.createRoleReview(portalUser, role);
        RoleSpoilerData roleSpoilerData = testObjectsFactory.createRoleSpoilerData(roleReview);

        RoleSpoilerDataPatchDTO patch = new RoleSpoilerDataPatchDTO();
        RoleSpoilerDataReadDTO read = roleSpoilerDataService.patchRoleSpoilerData(roleSpoilerData.getId(), patch);

        Assert.assertNotNull(read.getRoleReviewId());
        Assert.assertNotNull(read.getStartIndex());
        Assert.assertNotNull(read.getEndIndex());

        RoleSpoilerData roleSpoilerDataAfterUpdate = roleSpoilerDataRepository.findById(read.getId()).get();

        Assert.assertNotNull(roleSpoilerDataAfterUpdate.getRoleReviewId());
        Assert.assertNotNull(roleSpoilerDataAfterUpdate.getStartIndex());
        Assert.assertNotNull(roleSpoilerDataAfterUpdate.getEndIndex());

        Assertions.assertThat(roleSpoilerData).isEqualToComparingFieldByField(roleSpoilerDataAfterUpdate);
    }

    @Test
    public void testDeleteRoleSpoilerData() {
        PortalUser portalUser = testObjectsFactory.createPortalUser();
        Person person = testObjectsFactory.createPerson();
        Role role = testObjectsFactory.createRole(person);
        RoleReview roleReview = testObjectsFactory.createRoleReview(portalUser, role);
        RoleSpoilerData roleSpoilerData = testObjectsFactory.createRoleSpoilerData(roleReview);

        roleSpoilerDataService.deleteRoleSpoilerData(roleSpoilerData.getId());
        Assert.assertFalse(roleSpoilerDataRepository.existsById(roleSpoilerData.getId()));
    }

    @Test(expected = EntityNotFoundException.class)
    public void testDeleteRoleSpoilerDataNotFound() {
        roleSpoilerDataService.deleteRoleSpoilerData(UUID.randomUUID());
    }

    @Transactional
    @Test
    public void testPutRoleSpoilerData() {
        PortalUser portalUser = testObjectsFactory.createPortalUser();
        Person person = testObjectsFactory.createPerson();
        Role role = testObjectsFactory.createRole(person);
        RoleReview roleReview = testObjectsFactory.createRoleReview(portalUser, role);
        RoleSpoilerData roleSpoilerData = testObjectsFactory.createRoleSpoilerData(roleReview);

        RoleSpoilerDataPutDTO put = new RoleSpoilerDataPutDTO();
        put.setRoleReviewId(roleReview.getId());
        put.setStartIndex(100);
        put.setEndIndex(150);
        RoleSpoilerDataReadDTO read = roleSpoilerDataService.putRoleSpoilerData(roleSpoilerData.getId(), put);

        Assertions.assertThat(put).isEqualToComparingFieldByField(read);

        roleSpoilerData = roleSpoilerDataRepository.findById(read.getId()).get();
        Assertions.assertThat(roleSpoilerData).isEqualToIgnoringGivenFields(read,
                "roleReviewId");
        Assertions.assertThat(roleSpoilerData.getRoleReviewId().getId()).isEqualTo(read.getRoleReviewId());
    }

    @Transactional
    @Test
    public void testPutRoleSpoilerDataEmptyPut() {
        PortalUser portalUser = testObjectsFactory.createPortalUser();
        Person person = testObjectsFactory.createPerson();
        Role role = testObjectsFactory.createRole(person);
        RoleReview roleReview = testObjectsFactory.createRoleReview(portalUser, role);
        RoleSpoilerData roleSpoilerData = testObjectsFactory.createRoleSpoilerData(roleReview);

        RoleSpoilerDataPutDTO put = new RoleSpoilerDataPutDTO();
        RoleSpoilerDataReadDTO read = roleSpoilerDataService.putRoleSpoilerData(roleSpoilerData.getId(), put);

        Assert.assertNull(read.getRoleReviewId());
        Assert.assertNull(read.getStartIndex());
        Assert.assertNull(read.getEndIndex());

        RoleSpoilerData roleSpoilerDataAfterUpdate = roleSpoilerDataRepository.findById(read.getId()).get();

        Assert.assertNull(roleSpoilerDataAfterUpdate.getRoleReviewId().getId());
        Assert.assertNull(roleSpoilerDataAfterUpdate.getStartIndex());
        Assert.assertNull(roleSpoilerDataAfterUpdate.getEndIndex());

        Assertions.assertThat(roleSpoilerData).isEqualToComparingFieldByField(roleSpoilerDataAfterUpdate);
    }
}
