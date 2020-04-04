package solvve.course.service;

import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import solvve.course.BaseTest;
import solvve.course.domain.*;
import solvve.course.dto.RoleSpoilerDataCreateDTO;
import solvve.course.dto.RoleSpoilerDataPatchDTO;
import solvve.course.dto.RoleSpoilerDataPutDTO;
import solvve.course.dto.RoleSpoilerDataReadDTO;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.repository.RoleSpoilerDataRepository;

import java.util.List;
import java.util.UUID;

public class RoleReviewSpoilerDataServiceTest extends BaseTest {

    @Autowired
    private RoleSpoilerDataRepository roleSpoilerDataRepository;

    @Autowired
    private RoleReviewSpoilerDataService roleReviewSpoilerDataService;

    @Test
    public void testGetRoleSpoilerData() {
        PortalUser portalUser = testObjectsFactory.createPortalUser();
        Person person = testObjectsFactory.createPerson();
        Movie movie = testObjectsFactory.createMovie();
        Role role = testObjectsFactory.createRole(person, movie);
        RoleReview roleReview = testObjectsFactory.createRoleReview(portalUser, role);
        RoleSpoilerData roleSpoilerData1 = testObjectsFactory.createRoleSpoilerData(roleReview);
        RoleSpoilerData roleSpoilerData2 = testObjectsFactory.createRoleSpoilerData(roleReview);

        List<RoleSpoilerDataReadDTO> readDTO =
                roleReviewSpoilerDataService.getRoleReviewSpoilerData(roleReview.getId());
        Assertions.assertThat(readDTO).extracting("id").containsExactlyInAnyOrder(roleSpoilerData1.getId(),
                roleSpoilerData2.getId());
    }

    @Test(expected = EntityNotFoundException.class)
    public void testGetRoleSpoilerDataWrongId() {
        roleReviewSpoilerDataService.getRoleReviewSpoilerData(UUID.randomUUID());
    }

    @Test
    public void testCreateRoleSpoilerData() {
        PortalUser portalUser = testObjectsFactory.createPortalUser();
        Person person = testObjectsFactory.createPerson();
        Movie movie = testObjectsFactory.createMovie();
        Role role = testObjectsFactory.createRole(person,movie);
        RoleReview roleReview = testObjectsFactory.createRoleReview(portalUser, role);

        RoleSpoilerDataCreateDTO create = testObjectsFactory.createRoleSpoilerDataCreateDTO();
        create.setRoleReviewId(roleReview.getId());

        RoleSpoilerDataReadDTO read = roleReviewSpoilerDataService.createRoleReviewSpoilerData(roleReview.getId(),
                create);
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

        RoleSpoilerDataPatchDTO patch = testObjectsFactory.createRoleSpoilerDataPatchDTO();
        patch.setRoleReviewId(roleReview.getId());
        RoleSpoilerDataReadDTO read =
                roleReviewSpoilerDataService.patchRoleReviewSpoilerData(roleReview.getId(), roleSpoilerData.getId(),
                        patch);

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
        RoleSpoilerDataReadDTO read =
                roleReviewSpoilerDataService.patchRoleReviewSpoilerData(roleReview.getId(), roleSpoilerData.getId(),
                        patch);

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

        roleReviewSpoilerDataService.deleteRoleReviewSpoilerData(roleReview.getId(), roleSpoilerData.getId());
        Assert.assertFalse(roleSpoilerDataRepository.existsById(roleSpoilerData.getId()));
    }

    @Test(expected = EntityNotFoundException.class)
    public void testDeleteRoleSpoilerDataNotFound() {
        roleReviewSpoilerDataService.deleteRoleReviewSpoilerData(UUID.randomUUID(), UUID.randomUUID());
    }

    @Test
    public void testPutRoleSpoilerData() {
        PortalUser portalUser = testObjectsFactory.createPortalUser();
        Person person = testObjectsFactory.createPerson();
        Movie movie = testObjectsFactory.createMovie();
        Role role = testObjectsFactory.createRole(person,movie);
        RoleReview roleReview = testObjectsFactory.createRoleReview(portalUser, role);
        RoleSpoilerData roleSpoilerData = testObjectsFactory.createRoleSpoilerData(roleReview);

        RoleSpoilerDataPutDTO put = testObjectsFactory.createRoleSpoilerDataPutDTO();
        put.setRoleReviewId(roleReview.getId());
        RoleSpoilerDataReadDTO read =
                roleReviewSpoilerDataService.updateRoleReviewSpoilerData(roleReview.getId(), roleSpoilerData.getId(),
                        put);

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
        RoleSpoilerDataReadDTO read =
                roleReviewSpoilerDataService.updateRoleReviewSpoilerData(roleReview.getId(), roleSpoilerData.getId(),
                        put);

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
