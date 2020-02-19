package solvve.course.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;
import solvve.course.domain.*;
import solvve.course.dto.*;
import solvve.course.repository.*;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Component
@Service
public class TestObjectsFactory {

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private CompanyDetailsRepository companyDetailsRepository;

    @Autowired
    private MovieCompanyRepository movieCompanyRepository;

    @Autowired
    private LanguageRepository languageRepository;

    @Autowired
    private TransactionTemplate transactionTemplate;

    @Autowired
    private PortalUserRepository portalUserRepository;

    @Autowired
    private VisitRepository visitRepository;

    @Autowired
    private UserTypeRepository userTypeRepository;

    @Autowired
    private CrewTypeRepository crewTypeRepository;

    @Autowired
    private CrewRepository crewRepository;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private MovieReviewCompliantRepository movieReviewCompliantRepository;

    @Autowired
    private MovieReviewRepository movieReviewRepository;

    @Autowired
    private MovieReviewFeedbackRepository movieReviewFeedbackRepository;

    @Autowired
    private MovieSpoilerDataRepository movieSpoilerDataRepository;

    @Autowired
    private MovieVoteRepository movieVoteRepository;

    @Autowired
    private NewsRepository newsRepository;

    @Autowired
    private ReleaseDetailRepository releaseDetailRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private RoleReviewCompliantRepository roleReviewCompliantRepository;

    @Autowired
    private RoleReviewRepository roleReviewRepository;

    @Autowired
    private RoleReviewFeedbackRepository roleReviewFeedbackRepository;

    @Autowired
    private RoleSpoilerDataRepository roleSpoilerDataRepository;

    @Autowired
    private RoleVoteRepository roleVoteRepository;

    @Autowired
    private UserGrantRepository userGrantRepository;

    public Movie createMovie() {
        Movie movie = new Movie();
        movie.setTitle("Movie Test");
        movie.setYear((short) 2019);
        movie.setAspectRatio("1:10");
        movie.setCamera("Panasonic");
        movie.setColour("Black");
        movie.setCritique("123");
        movie.setDescription("Description");
        movie.setLaboratory("CaliforniaDreaming");
        movie.setSoundMix("DolbySurround");
        movie.setIsPublished(true);
        return movieRepository.save(movie);
    }

    public Movie createMovie(Set<Country> countrySet, String title, Short year, String aspectRatio,
                             String camera, String colour, String critique, String description,
                             String laboratory, String soundMix, Boolean isPublished,
                             Set<MovieCompany> movieCompanySet, Set<Language> languageSet) {
        Movie movie = new Movie();
        movie.setTitle(title);
        movie.setYear(year);
        movie.setAspectRatio(aspectRatio);
        movie.setCamera(camera);
        movie.setColour(colour);
        movie.setCritique(critique);
        movie.setDescription(description);
        movie.setLaboratory(laboratory);
        movie.setSoundMix(soundMix);
        movie.setIsPublished(isPublished);
        movie.setMovieProdCountries(countrySet);
        movie.setMovieProdLanguages(languageSet);
        movie.setMovieProdCompanies(movieCompanySet);
        return movieRepository.save(movie);
    }

    public Movie createMovie(Set<Country> countrySet) {
        Movie movie = new Movie();
        movie.setTitle("Movie Test");
        movie.setYear((short) 2019);
        movie.setAspectRatio("1:10");
        movie.setCamera("Panasonic");
        movie.setColour("Black");
        movie.setCritique("123");
        movie.setDescription("Description");
        movie.setLaboratory("CaliforniaDreaming");
        movie.setSoundMix("DolbySurround");
        movie.setIsPublished(true);
        movie.setMovieProdCountries(countrySet);
        return movieRepository.save(movie);
    }

    public Set<Country> createCountrySet() {
        Country c = new Country();
        c.setName("C1");
        c = countryRepository.save(c);
        Set<Country> sc = new HashSet<>();
        sc.add(c);
        return sc;
    }

    public Country createCountry() {
        Country country = new Country();
        country.setName("Ukraine");
        return countryRepository.save(country);
    }

    public Country createCountry(String name) {
        Country country = new Country();
        country.setName(name);
        return countryRepository.save(country);
    }

    public MovieCreateDTO createMovieCreateDTO() {
        MovieCreateDTO create = new MovieCreateDTO();
        create.setTitle("Movie Test");
        create.setYear((short) 2019);
        create.setAspectRatio("1:10");
        create.setCamera("Panasonic");
        create.setColour("Black");
        create.setCritique("123");
        create.setDescription("Description");
        create.setLaboratory("CaliforniaDreaming");
        create.setSoundMix("DolbySurround");
        create.setIsPublished(true);
        return create;
    }

    public MoviePatchDTO createMoviePatchDTO() {
        MoviePatchDTO patch = new MoviePatchDTO();
        patch.setTitle("Movie Test");
        patch.setYear((short) 2019);
        patch.setAspectRatio("1:10");
        patch.setCamera("Panasonic");
        patch.setColour("Black");
        patch.setCritique("123");
        patch.setDescription("Description");
        patch.setLaboratory("CaliforniaDreaming");
        patch.setSoundMix("DolbySurround");
        patch.setIsPublished(true);
        return patch;
    }

    public MoviePutDTO createMoviePutDTO() {
        MoviePutDTO put = new MoviePutDTO();
        put.setTitle("Movie Test");
        put.setYear((short) 2019);
        put.setAspectRatio("1:10");
        put.setCamera("Panasonic");
        put.setColour("Black");
        put.setCritique("123");
        put.setDescription("Description");
        put.setLaboratory("CaliforniaDreaming");
        put.setSoundMix("DolbySurround");
        put.setIsPublished(true);
        return put;
    }

    public Genre createGenre(Movie movie) {
        Genre genre = new Genre();
        genre.setMovieId(movie);
        genre.setName(MovieGenreType.ACTION);
        return genreRepository.save(genre);
    }

    public Genre createGenre() {
        Genre genre = new Genre();
        genre.setName(MovieGenreType.ACTION);
        return genreRepository.save(genre);
    }

    public Genre createGenre(Movie movie, MovieGenreType movieGenreType) {
        Genre genre = new Genre();
        genre.setMovieId(movie);
        genre.setName(movieGenreType);
        return genreRepository.save(genre);
    }

    public GenreReadDTO createGenresRead() {
        GenreReadDTO genre = new GenreReadDTO();
        genre.setId(UUID.randomUUID());
        genre.setName(MovieGenreType.ACTION);
        return genre;
    }

    public GenreCreateDTO createGenreCreateDTO() {
        GenreCreateDTO genreCreateDTO = new GenreCreateDTO();
        genreCreateDTO.setName(MovieGenreType.ACTION);
        return genreCreateDTO;
    }

    public GenrePatchDTO createGenrePatchDTO() {
        GenrePatchDTO dto = new GenrePatchDTO();
        dto.setName(MovieGenreType.ACTION);
        return dto;
    }

    public GenrePutDTO createGenrePutDTO() {
        GenrePutDTO dto = new GenrePutDTO();
        dto.setName(MovieGenreType.ACTION);
        return dto;
    }

    public CompanyDetails createCompanyDetails() {
        CompanyDetails companyDetails = new CompanyDetails();
        companyDetails.setName("Paramount");
        companyDetails.setOverview("Test Test Test");
        companyDetails.setYearOfFoundation(LocalDate.now());
        return companyDetailsRepository.save(companyDetails);
    }

    public CompanyDetails createCompanyDetails(String name, String overview, LocalDate yearOfFoundation) {
        CompanyDetails companyDetails = new CompanyDetails();
        companyDetails.setName(name);
        companyDetails.setOverview(overview);
        companyDetails.setYearOfFoundation(yearOfFoundation);
        return companyDetailsRepository.save(companyDetails);
    }

    public CompanyDetailsCreateDTO createCompanyDetailsCreateDTO() {
        CompanyDetailsCreateDTO dto = new CompanyDetailsCreateDTO();
        dto.setName("Paramount");
        dto.setOverview("Test Test Test");
        dto.setYearOfFoundation(LocalDate.now());
        return dto;
    }

    public CompanyDetailsPatchDTO createCompanyDetailsPatchDTO() {
        CompanyDetailsPatchDTO dto = new CompanyDetailsPatchDTO();
        dto.setName("Paramount");
        dto.setOverview("Test Test Test");
        dto.setYearOfFoundation(LocalDate.now());
        return dto;
    }

    public CompanyDetailsPutDTO createCompanyDetailsPutDTO() {
        CompanyDetailsPutDTO dto = new CompanyDetailsPutDTO();
        dto.setName("Paramount");
        dto.setOverview("Test Test Test");
        dto.setYearOfFoundation(LocalDate.now());
        return dto;
    }

    public MovieCompany createMovieCompany(CompanyDetails companyDetails, MovieProductionType movieProductionType) {
        MovieCompany movieCompany = new MovieCompany();
        movieCompany.setCompanyId(companyDetails);
        movieCompany.setMovieProductionType(movieProductionType);
        movieCompany.setDescription("DescTest");
        return movieCompanyRepository.save(movieCompany);
    }

    public MovieCompanyCreateDTO createMovieCompanyCreateDTO(UUID companyDetailId,
                                                             MovieProductionType movieProductionType) {
        MovieCompanyCreateDTO dto = new MovieCompanyCreateDTO();
        dto.setCompanyId(companyDetailId);
        dto.setMovieProductionType(movieProductionType);
        dto.setDescription("DescTest");
        return dto;
    }

    public MovieCompanyPatchDTO createMovieCompanyPatchDTO(UUID companyDetailId,
                                                           MovieProductionType movieProductionType) {
        MovieCompanyPatchDTO dto = new MovieCompanyPatchDTO();
        dto.setCompanyId(companyDetailId);
        dto.setMovieProductionType(movieProductionType);
        dto.setDescription("DescTest");
        return dto;
    }

    public MovieCompanyPutDTO createMovieCompanyPutDTO(UUID companyDetailId,
                                                       MovieProductionType movieProductionType) {
        MovieCompanyPutDTO dto = new MovieCompanyPutDTO();
        dto.setCompanyId(companyDetailId);
        dto.setMovieProductionType(movieProductionType);
        dto.setDescription("DescTest");
        return dto;
    }

    public Language createLanguage() {
        Language language = new Language();
        language.setName(LanguageType.UKRAINIAN);
        return languageRepository.save(language);
    }

    public Language createLanguage(LanguageType languageType) {
        Language language = new Language();
        language.setName(languageType);
        return languageRepository.save(language);
    }

    public LanguageReadDTO createLanguageReadDTO() {
        LanguageReadDTO dto = new LanguageReadDTO();
        dto.setName(LanguageType.UKRAINIAN);
        return dto;
    }

    public LanguagePatchDTO createLanguagePatchDTO() {
        LanguagePatchDTO dto = new LanguagePatchDTO();
        dto.setName(LanguageType.UKRAINIAN);
        return dto;
    }

    public LanguagePutDTO createLanguagePutDTO() {
        LanguagePutDTO dto = new LanguagePutDTO();
        dto.setName(LanguageType.UKRAINIAN);
        return dto;
    }

    public LanguageCreateDTO createLanguageCreateDTO() {
        LanguageCreateDTO dto = new LanguageCreateDTO();
        dto.setName(LanguageType.UKRAINIAN);
        return dto;
    }

    public void inTransaction(Runnable runnable) {
        transactionTemplate.executeWithoutResult(status -> {
            runnable.run();
        });
    }

    public PortalUser createPortalUser() {
        UserType userType = new UserType();
        userType.setUserGroup(UserGroupType.USER);
        userType = userTypeRepository.save(userType);

        PortalUser portalUser = new PortalUser();
        portalUser.setUserTypeId(userType);
        portalUser.setSurname("Surname");
        portalUser.setName("Name");
        portalUser.setMiddleName("MiddleName");
        portalUser.setLogin("Login");
        portalUser.setUserConfidence(UserConfidenceType.NORMAL);
        portalUser = portalUserRepository.save(portalUser);

        return portalUser;
    }

    public Visit createVisit(PortalUser portalUser) {
        Visit visit = new Visit();
        visit.setUserId(portalUser);
        visit.setStartAt(LocalDateTime.of(2019, 12, 4, 17, 30, 0)
                .toInstant(ZoneOffset.UTC));
        visit.setFinishAt(LocalDateTime.of(2019, 12, 4, 17, 30, 0)
                .toInstant(ZoneOffset.UTC));
        visit.setStatus(VisitStatus.FINISHED);
        return visitRepository.save(visit);
    }

    public Visit createVisit(PortalUser portalUser, VisitStatus visitStatus) {
        Visit visit = new Visit();
        visit.setUserId(portalUser);
        visit.setStartAt(LocalDateTime.of(2019, 12, 4, 17, 30, 0)
                .toInstant(ZoneOffset.UTC));
        visit.setFinishAt(LocalDateTime.of(2019, 12, 4, 17, 30, 0)
                .toInstant(ZoneOffset.UTC));
        visit.setStatus(visitStatus);
        return visitRepository.save(visit);
    }

    public Visit createVisit(PortalUser portalUser, VisitStatus visitStatus, Instant startAt) {
        Visit visit = new Visit();
        visit.setUserId(portalUser);
        visit.setStartAt(startAt);
        visit.setFinishAt(LocalDateTime.of(2020, 12, 4, 17, 30, 0)
                .toInstant(ZoneOffset.UTC));
        visit.setStatus(visitStatus);
        return visitRepository.save(visit);
    }

    public VisitCreateDTO createVisitCreateDTO(PortalUser portalUser) {
        VisitCreateDTO create = new VisitCreateDTO();
        create.setUserId(portalUser.getId());
        create.setStartAt(LocalDateTime.of(2020, 12, 4, 17, 30, 0)
                .toInstant(ZoneOffset.UTC));
        create.setFinishAt(LocalDateTime.of(2020, 12, 4, 17, 30, 0)
                .toInstant(ZoneOffset.UTC));
        create.setStatus(VisitStatus.FINISHED);
        return create;
    }

    public VisitPatchDTO createVisitPatchDTO(PortalUser portalUser) {
        VisitPatchDTO patch = new VisitPatchDTO();
        patch.setUserId(portalUser.getId());
        patch.setStartAt(LocalDateTime.of(2020, 12, 4, 17, 30, 0)
                .toInstant(ZoneOffset.UTC));
        patch.setFinishAt(LocalDateTime.of(2020, 12, 4, 17, 30, 0)
                .toInstant(ZoneOffset.UTC));
        patch.setStatus(VisitStatus.FINISHED);
        return patch;
    }

    public VisitPutDTO createVisitPutDTO(PortalUser portalUser) {
        VisitPutDTO put = new VisitPutDTO();
        put.setUserId(portalUser.getId());
        put.setStartAt(LocalDateTime.of(2020, 12, 4, 17, 30, 0)
                .toInstant(ZoneOffset.UTC));
        put.setFinishAt(LocalDateTime.of(2020, 12, 4, 17, 30, 0)
                .toInstant(ZoneOffset.UTC));
        put.setStatus(VisitStatus.FINISHED);
        return put;
    }

    public CrewType createCrewType() {
        CrewType crewType = new CrewType();
        crewType.setName("Director");
        crewType = crewTypeRepository.save(crewType);
        return  crewType;
    }

    public Person createPerson() {
        Person person = new Person();
        person.setSurname("Surname");
        person.setName("Name");
        person.setMiddleName("MiddleName");
        person = personRepository.save(person);
        return person;
    }

    public Crew createCrew(Person person, CrewType crewType, Movie movie) {
        Crew crew = new Crew();
        crew.setPersonId(person);
        crew.setCrewTypeId(crewType);
        crew.setMovieId(movie);
        crew.setDescription("Description");
        return crewRepository.save(crew);
    }

    public MovieReviewCompliant createMovieReviewCompliant(PortalUser portalUser,
                                                           Movie movie,
                                                           MovieReview movieReview) {
        MovieReviewCompliant movieReviewCompliant = new MovieReviewCompliant();
        movieReviewCompliant.setUserId(portalUser);
        movieReviewCompliant.setMovieId(movie);
        movieReviewCompliant.setMovieReviewId(movieReview);
        movieReviewCompliant.setDescription("Just punish him!");
        movieReviewCompliant.setModeratedStatus(UserModeratedStatusType.SUCCESS);
        movieReviewCompliant.setModeratorId(portalUser);
        return movieReviewCompliantRepository.save(movieReviewCompliant);
    }

    public MovieReview createMovieReview(PortalUser portalUser, Movie movie) {
        MovieReview movieReview = new MovieReview();
        movieReview.setUserId(portalUser);
        movieReview.setMovieId(movie);
        movieReview.setTextReview("This movie can be described as junk.");
        movieReview.setModeratedStatus(UserModeratedStatusType.SUCCESS);
        movieReview.setModeratorId(portalUser);
        movieReview = movieReviewRepository.save(movieReview);
        return movieReview;
    }

    public MovieReviewFeedback createMovieReviewFeedback(PortalUser portalUser,
                                                         Movie movie,
                                                         MovieReview movieReview) {
        MovieReviewFeedback movieReviewFeedback = new MovieReviewFeedback();
        movieReviewFeedback.setUserId(portalUser);
        movieReviewFeedback.setMovieId(movie);
        movieReviewFeedback.setMovieReviewId(movieReview);
        movieReviewFeedback.setIsLiked(true);
        return movieReviewFeedbackRepository.save(movieReviewFeedback);
    }

    public MovieSpoilerData createMovieSpoilerData(MovieReview movieReview) {
        MovieSpoilerData movieSpoilerData = new MovieSpoilerData();
        movieSpoilerData.setMovieReviewId(movieReview);
        movieSpoilerData.setStartIndex(100);
        movieSpoilerData.setEndIndex(150);
        return movieSpoilerDataRepository.save(movieSpoilerData);
    }

    public MovieVote createMovieVote(PortalUser portalUser, Movie movie) {
        MovieVote movieVote = new MovieVote();
        movieVote.setMovieId(movie);
        movieVote.setUserId(portalUser);
        movieVote.setRating(UserVoteRatingType.R9);
        return movieVoteRepository.save(movieVote);
    }

    public News createNews(PortalUser portalUser) {
        News news = new News();
        news.setPublisher(portalUser);
        news.setTopic("Main_News");
        news.setDescription("Our main news are absent today!");
        news.setPublished(Instant.now());
        return newsRepository.save(news);
    }

    public UserType createUserType() {
        UserType userType = new UserType();
        userType.setUserGroup(UserGroupType.USER);
        userType = userTypeRepository.save(userType);
        return userType;
    }

    public PortalUser createPortalUser(UserType userType) {
        PortalUser portalUser = new PortalUser();
        portalUser.setUserTypeId(userType);
        portalUser.setSurname("Surname");
        portalUser.setName("Name");
        portalUser.setMiddleName("MiddleName");
        portalUser.setLogin("Login");
        portalUser.setUserConfidence(UserConfidenceType.NORMAL);
        return portalUserRepository.save(portalUser);
    }

    public ReleaseDetail createReleaseDetail(Movie movie, Country country) {
        ReleaseDetail releaseDetail = new ReleaseDetail();
        releaseDetail.setMovieId(movie);
        releaseDetail.setCountryId(country);
        releaseDetail.setReleaseDate(LocalDate.now(ZoneOffset.UTC));
        return releaseDetailRepository.save(releaseDetail);
    }

    public RoleReviewCompliant createRoleReviewCompliant(PortalUser portalUser, Role role, RoleReview roleReview) {
        RoleReviewCompliant roleReviewCompliant = new RoleReviewCompliant();
        roleReviewCompliant.setUserId(portalUser);
        roleReviewCompliant.setRoleId(role);
        roleReviewCompliant.setRoleReviewId(roleReview);
        roleReviewCompliant.setDescription("Just punish him!");
        roleReviewCompliant.setModeratedStatus(UserModeratedStatusType.SUCCESS);
        roleReviewCompliant.setModeratorId(portalUser);
        return roleReviewCompliantRepository.save(roleReviewCompliant);
    }

    public Role createRole(Person person) {
        Role role = new Role();
        role.setTitle("Actor");
        role.setRoleType(RoleType.LEAD);
        role.setDescription("Description test");
        role.setPersonId(person);
        role = roleRepository.save(role);
        return role;
    }

    public Role createRole(Person person, Movie movie) {
        Role role = new Role();
        role.setTitle("Actor");
        role.setRoleType(RoleType.LEAD);
        role.setDescription("Description test");
        role.setPersonId(person);
        role.setMovieId(movie);
        role = roleRepository.save(role);
        return role;
    }

    public RoleReview createRoleReview(PortalUser portalUser, Role role) {
        RoleReview roleReview = new RoleReview();
        roleReview.setUserId(portalUser);
        roleReview.setRoleId(role);
        roleReview.setTextReview("This role can be described as junk.");
        roleReview.setModeratedStatus(UserModeratedStatusType.SUCCESS);
        roleReview.setModeratorId(portalUser);
        roleReview = roleReviewRepository.save(roleReview);
        return roleReview;
    }

    public RoleReviewFeedback createRoleReviewFeedback(PortalUser portalUser, Role role, RoleReview roleReview) {
        RoleReviewFeedback roleReviewFeedback = new RoleReviewFeedback();
        roleReviewFeedback.setUserId(portalUser);
        roleReviewFeedback.setRoleId(role);
        roleReviewFeedback.setRoleReviewId(roleReview);
        roleReviewFeedback.setIsLiked(true);
        return roleReviewFeedbackRepository.save(roleReviewFeedback);
    }

    public RoleSpoilerData createRoleSpoilerData(RoleReview roleReview) {
        RoleSpoilerData roleSpoilerData = new RoleSpoilerData();
        roleSpoilerData.setRoleReviewId(roleReview);
        roleSpoilerData.setStartIndex(100);
        roleSpoilerData.setEndIndex(150);
        return roleSpoilerDataRepository.save(roleSpoilerData);
    }

    public RoleVote createRoleVote(PortalUser portalUser, Role role) {
        RoleVote roleVote = new RoleVote();
        roleVote.setRoleId(role);
        roleVote.setUserId(portalUser);
        roleVote.setRating(UserVoteRatingType.R9);
        return roleVoteRepository.save(roleVote);
    }

    public UserGrant createGrants(UserType userType, PortalUser portalUser) {
        UserGrant userGrant = new UserGrant();
        userGrant.setUserTypeId(userType);
        userGrant.setObjectName("Movie");
        userGrant.setUserPermission(UserPermType.READ);
        userGrant.setGrantedBy(portalUser);
        return userGrantRepository.save(userGrant);
    }

    public UserType createUserTypeWithGrants(Set<UserGrant> userGrantSet) {
        UserType userType = new UserType();
        userType.setUserGroup(UserGroupType.USER);
        userType.setUserGrants(userGrantSet);
        return userTypeRepository.save(userType);
    }

    public Crew createCrew(Person person, CrewType crewType, Movie movie, String description) {
        Crew crew = new Crew();
        crew.setPersonId(person);
        crew.setCrewTypeId(crewType);
        crew.setMovieId(movie);
        crew.setDescription(description);
        return crewRepository.save(crew);
    }

    public CrewType createCrewType(String typeName) {
        CrewType crewType = new CrewType();
        crewType.setName(typeName);
        crewType = crewTypeRepository.save(crewType);
        return crewType;
    }

    public UserGrant createUserGrant(UserType userType) {
        UserGrant userGrant = new UserGrant();
        userGrant.setUserTypeId(userType);
        userGrant.setObjectName("ObjectName");
        return userGrantRepository.save(userGrant);
    }

    public UserGrant createUserGrant(UserType userType, PortalUser grantedBy) {
        UserGrant userGrant = new UserGrant();
        userGrant.setUserTypeId(userType);
        userGrant.setObjectName("ObjectName");
        userGrant.setGrantedBy(grantedBy);
        return userGrantRepository.save(userGrant);
    }

    public Instant createInstant(int hour) {
        return LocalDateTime.of(2019, 12, 23, hour, 0).toInstant(ZoneOffset.UTC);
    }

    public void inTranaction(Runnable runnable) {
        transactionTemplate.executeWithoutResult(status -> {
            runnable.run();
        });
    }
}
