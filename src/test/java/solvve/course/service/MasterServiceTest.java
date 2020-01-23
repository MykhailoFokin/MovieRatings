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
import solvve.course.domain.Master;
import solvve.course.dto.MasterCreateDTO;
import solvve.course.dto.MasterPatchDTO;
import solvve.course.dto.MasterReadDTO;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.repository.MasterRepository;

import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@Sql(statements = "delete from master", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class MasterServiceTest {

    @Autowired
    private MasterRepository masterRepository;

    @Autowired
    private MasterService masterService;

    private Master createMaster() {
        Master master = new Master();
        master.setId(UUID.randomUUID());
        master.setName("MasterName");
        master.setPhone("645768767");
        master.setAbout("What about");
        return masterRepository.save(master);
    }

    @Test
    public void testGetMaster() {
        Master master = createMaster();

        MasterReadDTO readDTO = masterService.getMaster(master.getId());
        Assertions.assertThat(readDTO).isEqualToComparingFieldByField(master);
    }

    @Test(expected = EntityNotFoundException.class)
    public void testGetMasterWrongId() {
        masterService.getMaster(UUID.randomUUID());
    }

    @Transactional
    @Test
    public void testCreateMaster() {
        MasterCreateDTO create = new MasterCreateDTO();
        create.setName("Laplandia");
        MasterReadDTO read = masterService.createMaster(create);
        Assertions.assertThat(create).isEqualToComparingFieldByField(read);

        Master master = masterRepository.findById(read.getId()).get();
        Assertions.assertThat(read).isEqualToComparingFieldByField(master);
    }

    @Transactional
    @Test
    public void testPatchMaster() {
        Master master = createMaster();

        MasterPatchDTO patch = new MasterPatchDTO();
        patch.setName("MasterName");
        patch.setPhone("645768767");
        patch.setAbout("What about");
        MasterReadDTO read = masterService.patchMaster(master.getId(), patch);

        Assertions.assertThat(patch).isEqualToComparingFieldByField(read);

        master = masterRepository.findById(read.getId()).get();
        Assertions.assertThat(master).isEqualToComparingFieldByField(read);
    }

    @Transactional
    @Test
    public void testPatchMasterEmptyPatch() {
        Master master = createMaster();

        MasterPatchDTO patch = new MasterPatchDTO();
        MasterReadDTO read = masterService.patchMaster(master.getId(), patch);

        Assert.assertNotNull(read.getName());

        Master masterAfterUpdate = masterRepository.findById(read.getId()).get();

        Assert.assertNotNull(masterAfterUpdate.getName());

        Assertions.assertThat(master).isEqualToComparingFieldByField(masterAfterUpdate);
    }

    @Test
    public void testDeleteMaster() {
        Master master = createMaster();

        masterService.deleteMaster(master.getId());
        Assert.assertFalse(masterRepository.existsById(master.getId()));
    }

    @Test(expected = EntityNotFoundException.class)
    public void testDeleteMasterNotFound() {
        masterService.deleteMaster(UUID.randomUUID());
    }
}
