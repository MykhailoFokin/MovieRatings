package solvve.course.service;

import com.fasterxml.jackson.databind.jsontype.impl.AsExistingPropertyTypeSerializer;
import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import solvve.course.domain.CrewType;
import solvve.course.dto.CrewTypeCreateDTO;
import solvve.course.dto.CrewTypePatchDTO;
import solvve.course.dto.CrewTypePutDTO;
import solvve.course.dto.CrewTypeReadDTO;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.repository.CrewTypeRepository;
import solvve.course.utils.TestObjectsFactory;

import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@Sql(statements = "delete from crew_type", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class CrewTypeServiceTest {

    @Autowired
    private CrewTypeRepository crewTypeRepository;

    @Autowired
    private CrewTypeService crewTypeService;

    @Autowired
    private TestObjectsFactory testObjectsFactory;

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
        CrewTypeCreateDTO create = new CrewTypeCreateDTO();
        create.setName("Director");
        CrewTypeReadDTO read = crewTypeService.createCrewType(create);
        Assertions.assertThat(create).isEqualToComparingFieldByField(read);

        CrewType crewType = crewTypeRepository.findById(read.getId()).get();
        Assertions.assertThat(read).isEqualToIgnoringGivenFields(crewType,"crewId");
    }

    @Test
    public void testPatchCrewType() {
        CrewType crewType = testObjectsFactory.createCrewType();

        CrewTypePatchDTO patch = new CrewTypePatchDTO();
        patch.setName("Director");
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

        CrewTypePutDTO put = new CrewTypePutDTO();
        put.setName("Director");
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

        Assert.assertNull(read.getName());

        CrewType crewTypeAfterUpdate = crewTypeRepository.findById(read.getId()).get();

        Assert.assertNull(crewTypeAfterUpdate.getName());
    }
}
