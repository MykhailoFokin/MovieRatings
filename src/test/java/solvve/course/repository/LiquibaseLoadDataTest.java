package solvve.course.repository;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestPropertySource;
import solvve.course.BaseTest;

@TestPropertySource(properties = "spring.liquibase.change-log=classpath:db/changelog/db.changelog-master.xml")
public class LiquibaseLoadDataTest extends BaseTest {

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private CompanyDetailsRepository companyDetailsRepository;

    @Autowired
    private CrewRepository crewRepository;

    @Autowired
    private CrewTypeRepository crewTypeRepository;

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private LanguageRepository languageRepository;

    @Autowired
    private MovieCompanyRepository movieCompanyRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private MovieReviewRepository movieReviewRepository;

    @Autowired
    private MovieReviewCompliantRepository movieReviewCompliantRepository;

    @Autowired
    private MovieReviewFeedbackRepository movieReviewFeedbackRepository;

    @Autowired
    private MovieSpoilerDataRepository movieSpoilerDataRepository;

    @Autowired
    private MovieVoteRepository movieVoteRepository;

    @Autowired
    private NewsRepository newsRepository;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private PortalUserRepository portalUserRepository;

    @Autowired
    private ReleaseDetailRepository releaseDetailRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private RoleReviewRepository roleReviewRepository;

    @Autowired
    private RoleReviewFeedbackRepository roleReviewFeedbackRepository;

    @Autowired
    private RoleReviewCompliantRepository roleReviewCompliantRepository;

    @Autowired
    private RoleSpoilerDataRepository roleSpoilerDataRepository;

    @Autowired
    private RoleVoteRepository roleVoteRepository;

    @Autowired
    private UserGrantRepository userGrantRepository;

    @Autowired
    private UserTypeRepository userTypeRepository;

    @Autowired
    private VisitRepository visitRepository;

    @Test
    public void testDataLoaded() {
        Assert.assertTrue(countryRepository.count() > 0);
        Assert.assertTrue(companyDetailsRepository.count() > 0);
        Assert.assertTrue(crewRepository.count() > 0);
        Assert.assertTrue(crewTypeRepository.count() > 0);
        Assert.assertTrue(genreRepository.count() > 0);
        Assert.assertTrue(languageRepository.count() > 0);
        Assert.assertTrue(movieRepository.count() > 0);
        Assert.assertTrue(movieCompanyRepository.count() > 0);
        Assert.assertTrue(movieReviewRepository.count() > 0);
        Assert.assertTrue(movieReviewCompliantRepository.count() > 0);
        Assert.assertTrue(movieReviewFeedbackRepository.count() > 0);
        Assert.assertTrue(movieSpoilerDataRepository.count() > 0);
        Assert.assertTrue(movieVoteRepository.count() > 0);
        Assert.assertTrue(newsRepository.count() > 0);
        Assert.assertTrue(personRepository.count() > 0);
        Assert.assertTrue(portalUserRepository.count() > 0);
        Assert.assertTrue(releaseDetailRepository.count() > 0);
        Assert.assertTrue(roleRepository.count() > 0);
        Assert.assertTrue(roleReviewCompliantRepository.count() > 0);
        Assert.assertTrue(roleReviewFeedbackRepository.count() > 0);
        Assert.assertTrue(roleReviewRepository.count() > 0);
        Assert.assertTrue(roleSpoilerDataRepository.count() > 0);
        Assert.assertTrue(roleVoteRepository.count() > 0);
        Assert.assertTrue(userGrantRepository.count() > 0);
        Assert.assertTrue(userTypeRepository.count() > 0);
        Assert.assertTrue(visitRepository.count() > 0);

    }
}
