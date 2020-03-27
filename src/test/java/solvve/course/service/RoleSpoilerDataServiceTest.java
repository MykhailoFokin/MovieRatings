package solvve.course.service;

import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import solvve.course.BaseTest;
import solvve.course.domain.*;
import solvve.course.dto.*;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.repository.*;

import java.util.UUID;

public class RoleSpoilerDataServiceTest extends BaseTest {

    @Autowired
    private RoleSpoilerDataRepository roleSpoilerDataRepository;

    @Autowired
    private RoleSpoilerDataService roleSpoilerDataService;

    @Test
    public void testGetRoleSpoilerData() {
        PortalUser portalUser = testObjectsFactory.createPortalUser();
        Person person = testObjectsFactory.createPerson();
        Movie movie = testObjectsFactory.createMovie();
        Role role = testObjectsFactory.createRole(person, movie);
        RoleReview roleReview = testObjectsFactory.createRoleReview(portalUser, role);
        RoleSpoilerData roleSpoilerData = testObjectsFactory.createRoleSpoilerData(roleReview);

        RoleSpoilerDataReadDTO readDTO = roleSpoilerDataService.getRoleSpoilerData(roleSpoilerData.getId());
        Assertions.assertThat(readDTO).isEqualToIgnoringGivenFields(roleSpoilerData,
                "roleReviewId");
        Assertions.assertThat(readDTO.getRoleReviewId()).isEqualTo(roleSpoilerData.getRoleReview().getId());
    }

    @Test(expected = EntityNotFoundException.class)
    public void testGetRoleSpoilerDataWrongId() {
        roleSpoilerDataService.getRoleSpoilerData(UUID.randomUUID());
    }

    @Test
    public void testCreateRoleSpoilerData() {
        PortalUser portalUser = testObjectsFactory.createPortalUser();
        Person person = testObjectsFactory.createPerson();
        Movie movie = testObjectsFactory.createMovie();
        Role role = testObjectsFactory.createRole(person,movie);
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
        Assertions.assertThat(read.getRoleReviewId()).isEqualTo(roleSpoilerData.getRoleReview().getId());
    }

    @Test
    public void testPatchRoleSpoilerData() {
        PortalUser portalUser = testObjectsFactory.createPortalUser();
        Person person = testObjectsFactory.createPerson();
        Movie movie = testObjectsFactory.createMovie();
        Role role = testObjectsFactory.createRole(person,movie);
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
                "roleReview");
        Assertions.assertThat(roleSpoilerData.getRoleReview().getId()).isEqualTo(read.getRoleReviewId());
    }

    @Test
    public void testPatchRoleSpoilerDataEmptyPatch() {
        PortalUser portalUser = testObjectsFactory.createPortalUser();
        Person person = testObjectsFactory.createPerson();
        Movie movie = testObjectsFactory.createMovie();
        Role role = testObjectsFactory.createRole(person, movie);
        RoleReview roleReview = testObjectsFactory.createRoleReview(portalUser, role);
        RoleSpoilerData roleSpoilerData = testObjectsFactory.createRoleSpoilerData(roleReview);

        RoleSpoilerDataPatchDTO patch = new RoleSpoilerDataPatchDTO();
        RoleSpoilerDataReadDTO read = roleSpoilerDataService.patchRoleSpoilerData(roleSpoilerData.getId(), patch);

        Assert.assertNotNull(read.getRoleReviewId());
        Assert.assertNotNull(read.getStartIndex());
        Assert.assertNotNull(read.getEndIndex());

        RoleSpoilerData roleSpoilerDataAfterUpdate = roleSpoilerDataRepository.findById(read.getId()).get();

        Assert.assertNotNull(roleSpoilerDataAfterUpdate.getRoleReview());
        Assert.assertNotNull(roleSpoilerDataAfterUpdate.getStartIndex());
        Assert.assertNotNull(roleSpoilerDataAfterUpdate.getEndIndex());

        Assertions.assertThat(roleSpoilerData).isEqualToIgnoringGivenFields(roleSpoilerDataAfterUpdate,
                "roleReview");
        Assertions.assertThat(roleSpoilerData.getRoleReview().getId())
                .isEqualTo(roleSpoilerDataAfterUpdate.getRoleReview().getId());
    }

    @Test
    public void testDeleteRoleSpoilerData() {
        PortalUser portalUser = testObjectsFactory.createPortalUser();
        Person person = testObjectsFactory.createPerson();
        Movie movie = testObjectsFactory.createMovie();
        Role role = testObjectsFactory.createRole(person,movie);
        RoleReview roleReview = testObjectsFactory.createRoleReview(portalUser, role);
        RoleSpoilerData roleSpoilerData = testObjectsFactory.createRoleSpoilerData(roleReview);

        roleSpoilerDataService.deleteRoleSpoilerData(roleSpoilerData.getId());
        Assert.assertFalse(roleSpoilerDataRepository.existsById(roleSpoilerData.getId()));
    }

    @Test(expected = EntityNotFoundException.class)
    public void testDeleteRoleSpoilerDataNotFound() {
        roleSpoilerDataService.deleteRoleSpoilerData(UUID.randomUUID());
    }

    @Test
    public void testPutRoleSpoilerData() {
        PortalUser portalUser = testObjectsFactory.createPortalUser();
        Person person = testObjectsFactory.createPerson();
        Movie movie = testObjectsFactory.createMovie();
        Role role = testObjectsFactory.createRole(person,movie);
        RoleReview roleReview = testObjectsFactory.createRoleReview(portalUser, role);
        RoleSpoilerData roleSpoilerData = testObjectsFactory.createRoleSpoilerData(roleReview);

        RoleSpoilerDataPutDTO put = new RoleSpoilerDataPutDTO();
        put.setRoleReviewId(roleReview.getId());
        put.setStartIndex(100);
        put.setEndIndex(150);
        RoleSpoilerDataReadDTO read = roleSpoilerDataService.updateRoleSpoilerData(roleSpoilerData.getId(), put);

        Assertions.assertThat(put).isEqualToComparingFieldByField(read);

        roleSpoilerData = roleSpoilerDataRepository.findById(read.getId()).get();
        Assertions.assertThat(roleSpoilerData).isEqualToIgnoringGivenFields(read,
                "roleReview");
        Assertions.assertThat(roleSpoilerData.getRoleReview().getId()).isEqualTo(read.getRoleReviewId());
    }

    @Test
    public void testPutRoleSpoilerDataEmptyPut() {
        PortalUser portalUser = testObjectsFactory.createPortalUser();
        Person person = testObjectsFactory.createPerson();
        Movie movie = testObjectsFactory.createMovie();
        Role role = testObjectsFactory.createRole(person, movie);
        RoleReview roleReview = testObjectsFactory.createRoleReview(portalUser, role);
        RoleSpoilerData roleSpoilerData = testObjectsFactory.createRoleSpoilerData(roleReview);

        RoleSpoilerDataPutDTO put = new RoleSpoilerDataPutDTO();
        RoleSpoilerDataReadDTO read = roleSpoilerDataService.updateRoleSpoilerData(roleSpoilerData.getId(), put);

        Assert.assertNotNull(read.getRoleReviewId());
        Assert.assertNull(read.getStartIndex());
        Assert.assertNull(read.getEndIndex());

        RoleSpoilerData roleSpoilerDataAfterUpdate = roleSpoilerDataRepository.findById(read.getId()).get();

        Assert.assertNotNull(roleSpoilerDataAfterUpdate.getRoleReview().getId());
        Assert.assertNull(roleSpoilerDataAfterUpdate.getStartIndex());
        Assert.assertNull(roleSpoilerDataAfterUpdate.getEndIndex());

        Assertions.assertThat(roleSpoilerData).isEqualToIgnoringGivenFields(roleSpoilerDataAfterUpdate,
                "roleReview", "startIndex", "endIndex", "updatedAt");
    }
}
