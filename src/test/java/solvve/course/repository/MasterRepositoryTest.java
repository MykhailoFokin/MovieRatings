package solvve.course.repository;

import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import solvve.course.domain.Master;
import solvve.course.dto.MasterFilter;
import solvve.course.service.MasterService;
import solvve.course.utils.TestObjectsFactory;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest
@Sql(statements = "delete from master", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@ActiveProfiles("test")
public class MasterRepositoryTest {

    @Autowired
    private MasterRepository masterRepository;

    @Autowired
    private MasterService masterService;

    @Autowired
    private TestObjectsFactory testObjectsFactory;

    @Test
    public void testGetMasterWithEmptyFilter() {
        Master m1 = testObjectsFactory.createMaster("MasterName1", "555-555-551", "Test about1");
        Master m2 = testObjectsFactory.createMaster("MasterName2", "555-555-551", "Test about2");
        Master m3 = testObjectsFactory.createMaster("MasterName2", "555-555-552", "Test about2");
        Master m4 = testObjectsFactory.createMaster("MasterName3", "555-555-552", "Test about3");

        MasterFilter filter = new MasterFilter();
        Assertions.assertThat(masterService.getMasters(filter)).extracting("Id")
                .containsExactlyInAnyOrder(m1.getId(), m2.getId(), m3.getId(), m4.getId());
    }

    @Test
    public void testGetMasterByName() {
        testObjectsFactory.createMaster("MasterName1", "555-555-551", "Test about1");
        Master m2 = testObjectsFactory.createMaster("MasterName2", "555-555-551", "Test about2");
        Master m3 = testObjectsFactory.createMaster("MasterName2", "555-555-552", "Test about2");
        testObjectsFactory.createMaster("MasterName3", "555-555-552", "Test about3");

        MasterFilter filter = new MasterFilter();
        filter.setName("MasterName2");
        Assertions.assertThat(masterService.getMasters(filter)).extracting("Id")
                .containsExactlyInAnyOrder(m2.getId(), m3.getId());
    }

    @Test
    public void testGetMasterByPhone() {
        Master m1 = testObjectsFactory.createMaster("MasterName1", "555-555-551", "Test about1");
        Master m2 = testObjectsFactory.createMaster("MasterName2", "555-555-551", "Test about2");
        testObjectsFactory.createMaster("MasterName2", "555-555-552", "Test about2");
        testObjectsFactory.createMaster("MasterName3", "555-555-552", "Test about3");

        MasterFilter filter = new MasterFilter();
        filter.setPhone("555-555-551");
        Assertions.assertThat(masterService.getMasters(filter)).extracting("Id")
                .containsExactlyInAnyOrder(m1.getId(), m2.getId());
    }

    @Test
    public void testGetMasterByAbout() {
        testObjectsFactory.createMaster("MasterName1", "555-555-551", "Test about1");
        testObjectsFactory.createMaster("MasterName2", "555-555-551", "Test about2");
        testObjectsFactory.createMaster("MasterName2", "555-555-552", "Test about2");
        Master m4 = testObjectsFactory.createMaster("MasterName3", "555-555-552", "Test about3");

        MasterFilter filter = new MasterFilter();
        filter.setAbout("Test about3");
        Assertions.assertThat(masterService.getMasters(filter)).extracting("Id")
                .containsExactlyInAnyOrder(m4.getId());
    }

    @Test
    public void testGetMastersByNames() {
        Master m1 = testObjectsFactory.createMaster("MasterName1", "555-555-551", "Test about1");
        testObjectsFactory.createMaster("MasterName2", "555-555-551", "Test about2");
        testObjectsFactory.createMaster("MasterName2", "555-555-552", "Test about2");
        Master m4 = testObjectsFactory.createMaster("MasterName3", "555-555-552", "Test about3");

        MasterFilter filter = new MasterFilter();
        filter.setNames(List.of("MasterName1", "MasterName3"));
        Assertions.assertThat(masterService.getMasters(filter)).extracting("Id")
                .containsExactlyInAnyOrder(m1.getId(), m4.getId());
    }

    @Test
    public void testGetMastersByPhones() {
        testObjectsFactory.createMaster("MasterName1", "555-555-551", "Test about1");
        testObjectsFactory.createMaster("MasterName2", "555-555-551", "Test about2");
        Master m3 = testObjectsFactory.createMaster("MasterName2", "555-555-552", "Test about2");
        Master m4 = testObjectsFactory.createMaster("MasterName3", "555-555-553", "Test about3");

        MasterFilter filter = new MasterFilter();
        filter.setPhones(List.of("555-555-552", "555-555-553"));
        Assertions.assertThat(masterService.getMasters(filter)).extracting("Id")
                .containsExactlyInAnyOrder(m3.getId(), m4.getId());
    }

    @Test
    public void testGetMastersByAbouts() {
        Master m1 = testObjectsFactory.createMaster("MasterName1", "555-555-551", "Test about1");
        testObjectsFactory.createMaster("MasterName2", "555-555-551", "Test about2");
        testObjectsFactory.createMaster("MasterName2", "555-555-552", "Test about2");
        Master m4 = testObjectsFactory.createMaster("MasterName3", "555-555-552", "Test about3");

        MasterFilter filter = new MasterFilter();
        filter.setAbouts(List.of("Test about1", "Test about3"));
        Assertions.assertThat(masterService.getMasters(filter)).extracting("Id")
                .containsExactlyInAnyOrder(m1.getId(), m4.getId());
    }

    @Test
    public void testGetMastersWithAllFilters() {
        testObjectsFactory.createMaster("MasterName1", "555-555-551", "Test about1");
        testObjectsFactory.createMaster("MasterName2", "555-555-551", "Test about2");
        Master m3 = testObjectsFactory.createMaster("MasterName2", "555-555-552", "Test about2");
        testObjectsFactory.createMaster("MasterName3", "555-555-552", "Test about3");

        MasterFilter filter = new MasterFilter();
        filter.setName("MasterName2");
        filter.setPhone("555-555-552");
        filter.setAbout("Test about2");
        filter.setNames(List.of("MasterName1","MasterName2","MasterName3"));
        filter.setPhones(List.of("555-555-551", "555-555-554", "555-555-552","555-555-555"));
        filter.setAbouts(List.of("Test about1","Test about2","Test about3"));
        Assertions.assertThat(masterService.getMasters(filter)).extracting("Id")
                .containsExactlyInAnyOrder(m3.getId());
    }

    @Test
    public void testCteatedAtIsSet() {
        Master entity = testObjectsFactory.createMaster();

        Instant createdAtBeforeReload = entity.getCreatedAt();
        Assert.assertNotNull(createdAtBeforeReload);
        entity = masterRepository.findById(entity.getId()).get();

        Instant createdAtAfterReload = entity.getCreatedAt();
        Assert.assertNotNull(createdAtAfterReload);
        Assert.assertEquals(createdAtBeforeReload, createdAtAfterReload);
    }

    @Test
    public void testModifiedAtIsSet() {
        Master entity = testObjectsFactory.createMaster();

        Instant modifiedAtBeforeReload = entity.getModifiedAt();
        Assert.assertNotNull(modifiedAtBeforeReload);
        entity = masterRepository.findById(entity.getId()).get();

        Instant modifiedAtAfterReload = entity.getModifiedAt();
        Assert.assertNotNull(modifiedAtAfterReload);
        Assert.assertEquals(modifiedAtBeforeReload, modifiedAtAfterReload);
    }

    @Test
    public void testModifiedAtIsModified() {
        Master entity = testObjectsFactory.createMaster();

        Instant modifiedAtBeforeReload = entity.getModifiedAt();
        Assert.assertNotNull(modifiedAtBeforeReload);

        entity.setName("NewNameTest");
        masterRepository.save(entity);
        entity = masterRepository.findById(entity.getId()).get();

        Instant modifiedAtAfterReload = entity.getModifiedAt();
        Assert.assertNotNull(modifiedAtAfterReload);
        Assert.assertTrue(modifiedAtBeforeReload.compareTo(modifiedAtAfterReload) < 1);
    }
}
