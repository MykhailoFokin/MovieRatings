package solvve.course.repository;

import org.assertj.core.api.Assertions;
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

    @Test
    public void testGetMasterWithEmptyFilter() {
        Master m1 = createMaster("MasterName1", "555-555-551", "Test about1");
        Master m2 = createMaster("MasterName2", "555-555-551", "Test about2");
        Master m3 = createMaster("MasterName2", "555-555-552", "Test about2");
        Master m4 = createMaster("MasterName3", "555-555-552", "Test about3");

        MasterFilter filter = new MasterFilter();
        Assertions.assertThat(masterService.getMasters(filter)).extracting("Id")
                .containsExactlyInAnyOrder(m1.getId(), m2.getId(), m3.getId(), m4.getId());
    }

    @Test
    public void testGetMasterByName() {
        createMaster("MasterName1", "555-555-551", "Test about1");
        Master m2 = createMaster("MasterName2", "555-555-551", "Test about2");
        Master m3 = createMaster("MasterName2", "555-555-552", "Test about2");
        createMaster("MasterName3", "555-555-552", "Test about3");

        MasterFilter filter = new MasterFilter();
        filter.setName("MasterName2");
        Assertions.assertThat(masterService.getMasters(filter)).extracting("Id")
                .containsExactlyInAnyOrder(m2.getId(), m3.getId());
    }

    @Test
    public void testGetMasterByPhone() {
        Master m1 = createMaster("MasterName1", "555-555-551", "Test about1");
        Master m2 = createMaster("MasterName2", "555-555-551", "Test about2");
        createMaster("MasterName2", "555-555-552", "Test about2");
        createMaster("MasterName3", "555-555-552", "Test about3");

        MasterFilter filter = new MasterFilter();
        filter.setPhone("555-555-551");
        Assertions.assertThat(masterService.getMasters(filter)).extracting("Id")
                .containsExactlyInAnyOrder(m1.getId(), m2.getId());
    }

    @Test
    public void testGetMasterByAbout() {
        createMaster("MasterName1", "555-555-551", "Test about1");
        createMaster("MasterName2", "555-555-551", "Test about2");
        createMaster("MasterName2", "555-555-552", "Test about2");
        Master m4 = createMaster("MasterName3", "555-555-552", "Test about3");

        MasterFilter filter = new MasterFilter();
        filter.setAbout("Test about3");
        Assertions.assertThat(masterService.getMasters(filter)).extracting("Id")
                .containsExactlyInAnyOrder(m4.getId());
    }

    @Test
    public void testGetMastersByNames() {
        Master m1 = createMaster("MasterName1", "555-555-551", "Test about1");
        createMaster("MasterName2", "555-555-551", "Test about2");
        createMaster("MasterName2", "555-555-552", "Test about2");
        Master m4 = createMaster("MasterName3", "555-555-552", "Test about3");

        MasterFilter filter = new MasterFilter();
        filter.setNames(List.of("MasterName1", "MasterName3"));
        Assertions.assertThat(masterService.getMasters(filter)).extracting("Id")
                .containsExactlyInAnyOrder(m1.getId(), m4.getId());
    }

    @Test
    public void testGetMastersByPhones() {
        createMaster("MasterName1", "555-555-551", "Test about1");
        createMaster("MasterName2", "555-555-551", "Test about2");
        Master m3 = createMaster("MasterName2", "555-555-552", "Test about2");
        Master m4 = createMaster("MasterName3", "555-555-553", "Test about3");

        MasterFilter filter = new MasterFilter();
        filter.setPhones(List.of("555-555-552", "555-555-553"));
        Assertions.assertThat(masterService.getMasters(filter)).extracting("Id")
                .containsExactlyInAnyOrder(m3.getId(), m4.getId());
    }

    @Test
    public void testGetMastersByAbouts() {
        Master m1 = createMaster("MasterName1", "555-555-551", "Test about1");
        createMaster("MasterName2", "555-555-551", "Test about2");
        createMaster("MasterName2", "555-555-552", "Test about2");
        Master m4 = createMaster("MasterName3", "555-555-552", "Test about3");

        MasterFilter filter = new MasterFilter();
        filter.setAbouts(List.of("Test about1", "Test about3"));
        Assertions.assertThat(masterService.getMasters(filter)).extracting("Id")
                .containsExactlyInAnyOrder(m1.getId(), m4.getId());
    }

    @Test
    public void testGetMastersWithAllFilters() {
        createMaster("MasterName1", "555-555-551", "Test about1");
        createMaster("MasterName2", "555-555-551", "Test about2");
        Master m3 = createMaster("MasterName2", "555-555-552", "Test about2");
        createMaster("MasterName3", "555-555-552", "Test about3");

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

    private Master createMaster(String masterName, String phone, String about) {
        Master master = new Master();
        master.setId(UUID.randomUUID());
        master.setName(masterName);
        master.setPhone(phone);
        master.setAbout(about);
        return masterRepository.save(master);
    }
}
