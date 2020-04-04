package solvve.course.service;

import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import solvve.course.BaseTest;
import solvve.course.domain.Crew;
import solvve.course.domain.CrewType;
import solvve.course.dto.CrewTypeCreateDTO;
import solvve.course.dto.CrewTypePatchDTO;
import solvve.course.dto.CrewTypePutDTO;
import solvve.course.dto.CrewTypeReadDTO;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.repository.CrewTypeRepository;
import solvve.course.utils.TestObjectsFactory;

import java.util.UUID;

public class CrewTypeServiceTest extends BaseTest {

    @Autowired
    private CrewTypeRepository crewTypeRepository;

    @Autowired
    private CrewTypeService crewTypeService;

    @Test
    public void testGetCrewType() {
        CrewType crewType = testObjectsFactory.createCrewType();

        CrewTypeReadDTO readDTO = crewTypeService.getCrewType(crewType.getId());
        Assertions.assertThat(readDTO).isEqualToIgnoringGivenFields(crewType,"crewId");
    }

    @Test(expected = EntityNotFoundException.class)
    public void testGetCrewTypeWrongId() {
        crewTypeService.getCrewType(UUID.randomUUID());
    }

    @Test
    public void testCreateCrewType() {
        CrewTypeCreateDTO create = testObjectsFactory.createCrewTypeCreateDTO();
        create.setCrewId(null);
        CrewTypeReadDTO read = crewTypeService.createCrewType(create);
        Assertions.assertThat(create).isEqualToComparingFieldByField(read);

        CrewType crewType = crewTypeRepository.findById(read.getId()).get();
        Assertions.assertThat(read).isEqualToIgnoringGivenFields(crewType,"crewId");
    }

    @Test
    public void testPatchCrewType() {
        CrewType crewType = testObjectsFactory.createCrewType();

        CrewTypePatchDTO patch = testObjectsFactory.createCrewTypePatchDTO();
        patch.setCrewId(null);
        CrewTypeReadDTO read = crewTypeService.patchCrewType(crewType.getId(), patch);

        Assertions.assertThat(patch).isEqualToComparingFieldByField(read);

        crewType = crewTypeRepository.findById(read.getId()).get();
        Assertions.assertThat(crewType).isEqualToIgnoringGivenFields(read, "crew");
    }

    @Test
    public void testPatchCrewTypeEmptyPatch() {
        CrewType crewType = testObjectsFactory.createCrewType();

        CrewTypePatchDTO patch = new CrewTypePatchDTO();
        CrewTypeReadDTO read = crewTypeService.patchCrewType(crewType.getId(), patch);

        Assert.assertNotNull(read.getName());

        CrewType crewTypeAfterUpdate = crewTypeRepository.findById(read.getId()).get();

        Assert.assertNotNull(crewTypeAfterUpdate.getName());

        Assertions.assertThat(crewType).isEqualToComparingFieldByField(crewTypeAfterUpdate);
    }

    @Test
    public void testDeleteCrewType() {
        CrewType crewType = testObjectsFactory.createCrewType();

        crewTypeService.deleteCrewType(crewType.getId());
        Assert.assertFalse(crewTypeRepository.existsById(crewType.getId()));
    }

    @Test(expected = EntityNotFoundException.class)
    public void testDeleteCrewTypeNotFound() {
        crewTypeService.deleteCrewType(UUID.randomUUID());
    }

    @Test
    public void testPutCrewType() {
        CrewType crewType = testObjectsFactory.createCrewType();

        CrewTypePutDTO put = testObjectsFactory.createCrewTypePutDTO();
        put.setCrewId(null);
        CrewTypeReadDTO read = crewTypeService.updateCrewType(crewType.getId(), put);

        Assertions.assertThat(put).isEqualToComparingFieldByField(read);

        crewType = crewTypeRepository.findById(read.getId()).get();
        Assertions.assertThat(crewType).isEqualToIgnoringGivenFields(read,"crew");
    }

    @Test
    public void testPutCrewTypeEmptyPut() {
        CrewType crewType = testObjectsFactory.createCrewType();

        CrewTypePutDTO put = new CrewTypePutDTO();
        CrewTypeReadDTO read = crewTypeService.updateCrewType(crewType.getId(), put);

        Assert.assertNotNull(read.getName());

        CrewType crewTypeAfterUpdate = crewTypeRepository.findById(read.getId()).get();

        Assert.assertNotNull(crewTypeAfterUpdate.getName());
    }
}
