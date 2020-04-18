package solvve.course.utils;

import org.bitbucket.brunneng.br.Configuration;
import org.bitbucket.brunneng.br.RandomObjectGenerator;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collector;
import java.util.stream.Collectors;

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

    @Autowired
    private NewsUserReviewRepository newsUserReviewRepository;

    @Autowired
    private NewsUserReviewNoteRepository newsUserReviewNoteRepository;

    @Autowired
    private UserTypoRequestRepository userTypoRequestRepository;

    private RandomObjectGenerator generator = new RandomObjectGenerator();

    private <T extends AbstractEntity> T generateEntityWithoutId(Class<T> entityClass) {
        T entity = generator.generateRandomObject(entityClass);
        entity.setId(null);
        return entity;
    }

    public <T> T generateObject(Class<T> entityClass) {
        return generator.generateRandomObject(entityClass);
    }

    private RandomObjectGenerator flatGenerator;
    {
        Configuration c = new Configuration();
        c.setFlatMode(true);
        flatGenerator = new RandomObjectGenerator(c);
    }

    public <T extends AbstractEntity> T generateFlatEntityWithoutId(Class<T> entityClass) {
        T entity = flatGenerator.generateRandomObject(entityClass);
        entity.setId(null);
        return entity;
    }

    public Movie createMovie() {
        Movie movie = generateFlatEntityWithoutId(Movie.class);
        movie.setAverageRating(null);
        return movieRepository.save(movie);
    }

    public Movie createMovieWithRating(Double rating) {
        Movie movie = generateFlatEntityWithoutId(Movie.class);
        movie.setAverageRating(rating);
        return movieRepository.save(movie);
    }

    public Movie createMovie(Set<Country> countries, String title, Short year, String aspectRatio,
                             String camera, String colour, String critique, String description,
                             String laboratory, String soundMix, Boolean isPublished,
                             Set<MovieCompany> movieCompanies, Set<Language> languages) {
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
        movie.setMovieProdCountries(countries);
        movie.setMovieProdLanguages(languages);
        movie.setMovieProdCompanies(movieCompanies);
        return movieRepository.save(movie);
    }

    public Movie createMovieWithLanguages(List<UUID> languageIds) {
        Set<Language> languages =
                languageIds.stream().map(e -> languageRepository.findById(e).get()).collect(Collectors.toSet());
        Movie movie = generateFlatEntityWithoutId(Movie.class);
        movie.setAverageRating(null);
        movie.setMovieProdLanguages(languages);
        return movieRepository.save(movie);
    }

    public Movie createMovieWithCountries(List<UUID> countriesIds) {
        Set<Country> countries =
                countriesIds.stream().map(e -> countryRepository.findById(e).get()).collect(Collectors.toSet());
        Movie movie = generateFlatEntityWithoutId(Movie.class);
        movie.setAverageRating(null);
        movie.setMovieProdCountries(countries);
        return movieRepository.save(movie);
    }

    public Movie createMovieWithCompanies(List<UUID> companiesIds) {
        Set<MovieCompany> movieCompanies =
                companiesIds.stream().map(e -> movieCompanyRepository.findById(e).get()).collect(Collectors.toSet());
        Movie movie = generateFlatEntityWithoutId(Movie.class);
        movie.setAverageRating(null);
        movie.setMovieProdCompanies(movieCompanies);
        return movieRepository.save(movie);
    }

    public Movie createMovie(Set<Country> countries) {
        Movie movie = generateFlatEntityWithoutId(Movie.class);
        movie.setAverageRating(null);
        movie.setMovieProdCountries(countries);
        return movieRepository.save(movie);
    }

    public Set<Country> createCountries() {
        Country c = generateFlatEntityWithoutId(Country.class);
        c = countryRepository.save(c);
        Set<Country> sc = new HashSet<>();
        sc.add(c);
        return sc;
    }

    public Country createCountry() {
        Country country = generateFlatEntityWithoutId(Country.class);
        return countryRepository.save(country);
    }

    public Country createCountry(String name) {
        Country country = generateFlatEntityWithoutId(Country.class);
        country.setName(name);
        return countryRepository.save(country);
    }

    public MovieCreateDTO createMovieCreateDTO() {
        MovieCreateDTO create = generateObject(MovieCreateDTO.class);
        return create;
    }

    public MoviePatchDTO createMoviePatchDTO() {
        return generateObject(MoviePatchDTO.class);
    }

    public MoviePutDTO createMoviePutDTO() {
        return generateObject(MoviePutDTO.class);
    }

    public Genre createGenre(Movie movie) {
        Genre genre = generateFlatEntityWithoutId(Genre.class);
        genre.setMovie(movie);
        return genreRepository.save(genre);
    }

    public Genre createGenre() {
        Genre genre = generateFlatEntityWithoutId(Genre.class);
        return genreRepository.save(genre);
    }

    public Genre createGenre(Movie movie, MovieGenreType movieGenreType) {
        Genre genre = new Genre();
        genre.setMovie(movie);
        genre.setName(movieGenreType);
        return genreRepository.save(genre);
    }

    public GenreReadDTO createGenresRead() {
        return generateObject(GenreReadDTO.class);
    }

    public GenreCreateDTO createGenreCreateDTO() {
        return generateObject(GenreCreateDTO.class);
    }

    public GenrePatchDTO createGenrePatchDTO() {
        return generateObject(GenrePatchDTO.class);
    }

    public GenrePutDTO createGenrePutDTO() {
        return generateObject(GenrePutDTO.class);
    }

    public CompanyDetails createCompanyDetails() {
        CompanyDetails companyDetails = generateFlatEntityWithoutId(CompanyDetails.class);
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
        return generateObject(CompanyDetailsCreateDTO.class);
    }

    public CompanyDetailsPatchDTO createCompanyDetailsPatchDTO() {
        return generateObject(CompanyDetailsPatchDTO.class);
    }

    public CompanyDetailsPutDTO createCompanyDetailsPutDTO() {
        return generateObject(CompanyDetailsPutDTO.class);
    }

    public MovieCompany createMovieCompany(CompanyDetails companyDetails, MovieProductionType movieProductionType) {
        MovieCompany movieCompany = generateFlatEntityWithoutId(MovieCompany.class);
        movieCompany.setCompanyDetails(companyDetails);
        movieCompany.setMovieProductionType(movieProductionType);
        movieCompany.setDescription("Desc");
        return movieCompanyRepository.save(movieCompany);
    }

    public MovieCompanyCreateDTO createMovieCompanyCreateDTO(UUID companyDetailId,
                                                             MovieProductionType movieProductionType) {
        MovieCompanyCreateDTO dto = generateObject(MovieCompanyCreateDTO.class);
        dto.setCompanyDetailsId(companyDetailId);
        dto.setMovieProductionType(movieProductionType);
        return dto;
    }

    public MovieCompanyPatchDTO createMovieCompanyPatchDTO(UUID companyDetailId,
                                                           MovieProductionType movieProductionType) {
        MovieCompanyPatchDTO dto = generateObject(MovieCompanyPatchDTO.class);
        dto.setCompanyDetailsId(companyDetailId);
        dto.setMovieProductionType(movieProductionType);
        return dto;
    }

    public MovieCompanyPutDTO createMovieCompanyPutDTO(UUID companyDetailId,
                                                       MovieProductionType movieProductionType) {
        MovieCompanyPutDTO dto = generateObject(MovieCompanyPutDTO.class);
        dto.setCompanyDetailsId(companyDetailId);
        dto.setMovieProductionType(movieProductionType);
        return dto;
    }

    public Language createLanguage() {
        Language language = generateFlatEntityWithoutId(Language.class);
        return languageRepository.save(language);
    }

    public Language createLanguage(LanguageType languageType) {
        Language language = new Language();
        language.setName(languageType);
        return languageRepository.save(language);
    }

    public LanguageReadDTO createLanguageReadDTO() {
        return generateObject(LanguageReadDTO.class);
    }

    public LanguagePatchDTO createLanguagePatchDTO() {
        return generateObject(LanguagePatchDTO.class);
    }

    public LanguagePutDTO createLanguagePutDTO() {
        return generateObject(LanguagePutDTO.class);
    }

    public LanguageCreateDTO createLanguageCreateDTO() {
        return generateObject(LanguageCreateDTO.class);
    }

    public void inTransaction(Runnable runnable) {
        transactionTemplate.executeWithoutResult(status -> {
            runnable.run();
        });
    }

    public PortalUser createPortalUser() {
        UserType userType = generateFlatEntityWithoutId(UserType.class);
        userType = userTypeRepository.save(userType);

        PortalUser portalUser = generateFlatEntityWithoutId(PortalUser.class);
        portalUser.setUserType(userType);
        portalUser = portalUserRepository.save(portalUser);

        return portalUser;
    }

    public Visit createVisit(PortalUser portalUser) {
        Visit visit = generateFlatEntityWithoutId(Visit.class);
        visit.setPortalUser(portalUser);
        return visitRepository.save(visit);
    }

    public Visit createVisit(PortalUser portalUser, VisitStatus visitStatus) {
        Visit visit = generateFlatEntityWithoutId(Visit.class);
        visit.setPortalUser(portalUser);
        visit.setStatus(visitStatus);
        return visitRepository.save(visit);
    }

    public Visit createVisit(PortalUser portalUser, VisitStatus visitStatus, Instant startAt) {
        Visit visit = generateFlatEntityWithoutId(Visit.class);
        visit.setPortalUser(portalUser);
        visit.setStartAt(startAt);
        visit.setStatus(visitStatus);
        return visitRepository.save(visit);
    }

    public VisitCreateDTO createVisitCreateDTO(PortalUser portalUser) {
        VisitCreateDTO create = generateObject(VisitCreateDTO.class);
        create.setPortalUserId(portalUser.getId());
        return create;
    }

    public VisitPatchDTO createVisitPatchDTO(PortalUser portalUser) {
        VisitPatchDTO patch = generateObject(VisitPatchDTO.class);
        patch.setPortalUserId(portalUser.getId());
        return patch;
    }

    public VisitPutDTO createVisitPutDTO(PortalUser portalUser) {
        VisitPutDTO put = generateObject(VisitPutDTO.class);
        put.setPortalUserId(portalUser.getId());
        return put;
    }

    public CrewType createCrewType() {
        CrewType crewType = generateFlatEntityWithoutId(CrewType.class);
        return crewTypeRepository.save(crewType);
    }

    public Person createPerson() {
        Person person = generateFlatEntityWithoutId(Person.class);
        return personRepository.save(person);
    }

    public Crew createCrew(Person person, CrewType crewType, Movie movie) {
        Crew crew = generateFlatEntityWithoutId(Crew.class);
        crew.setPerson(person);
        crew.setCrewType(crewType);
        crew.setMovie(movie);
        return crewRepository.save(crew);
    }

    public MovieReviewCompliant createMovieReviewCompliant(PortalUser portalUser,
                                                           Movie movie,
                                                           MovieReview movieReview) {
        MovieReviewCompliant movieReviewCompliant = generateFlatEntityWithoutId(MovieReviewCompliant.class);
        movieReviewCompliant.setPortalUser(portalUser);
        movieReviewCompliant.setMovie(movie);
        movieReviewCompliant.setMovieReview(movieReview);
        movieReviewCompliant.setModerator(portalUser);
        return movieReviewCompliantRepository.save(movieReviewCompliant);
    }

    public MovieReview createMovieReview(PortalUser portalUser, Movie movie) {
        MovieReview movieReview = generateFlatEntityWithoutId(MovieReview.class);
        movieReview.setPortalUser(portalUser);
        movieReview.setMovie(movie);
        movieReview.setModerator(portalUser);
        movieReview = movieReviewRepository.save(movieReview);
        return movieReview;
    }

    public MovieReviewFeedback createMovieReviewFeedback(PortalUser portalUser,
                                                         Movie movie,
                                                         MovieReview movieReview) {
        MovieReviewFeedback movieReviewFeedback = generateFlatEntityWithoutId(MovieReviewFeedback.class);
        movieReviewFeedback.setPortalUser(portalUser);
        movieReviewFeedback.setMovie(movie);
        movieReviewFeedback.setMovieReview(movieReview);
        return movieReviewFeedbackRepository.save(movieReviewFeedback);
    }

    public MovieSpoilerData createMovieSpoilerData(MovieReview movieReview) {
        MovieSpoilerData movieSpoilerData = generateFlatEntityWithoutId(MovieSpoilerData.class);
        movieSpoilerData.setMovieReview(movieReview);
        return movieSpoilerDataRepository.save(movieSpoilerData);
    }

    public MovieVote createMovieVote(PortalUser portalUser, Movie movie) {
        MovieVote movieVote = generateFlatEntityWithoutId(MovieVote.class);
        movieVote.setMovie(movie);
        movieVote.setPortalUser(portalUser);
        return movieVoteRepository.save(movieVote);
    }

    public MovieVote createMovieVote(PortalUser portalUser, Movie movie, UserVoteRatingType userVoteRatingType) {
        MovieVote movieVote = new MovieVote();
        movieVote.setMovie(movie);
        movieVote.setPortalUser(portalUser);
        movieVote.setRating(userVoteRatingType);
        return movieVoteRepository.save(movieVote);
    }

    public MovieVote createMovieVote(PortalUser portalUser, Movie movie, Boolean withMark) {
        MovieVote movieVote = new MovieVote();
        movieVote.setMovie(movie);
        movieVote.setPortalUser(portalUser);
        if (withMark) {
            movieVote.setRating(UserVoteRatingType.R5);
        } else {
            movieVote.setRating(UserVoteRatingType.R1);
        }
        return movieVoteRepository.save(movieVote);
    }

    public News createNews(PortalUser portalUser) {
        News news = generateFlatEntityWithoutId(News.class);
        news.setPublisher(portalUser);
        return newsRepository.save(news);
    }

    public News createNews(PortalUser portalUser, String newsText) {
        News news = generateFlatEntityWithoutId(News.class);
        news.setPublisher(portalUser);
        news.setDescription(newsText);
        return newsRepository.save(news);
    }

    public UserType createUserType() {
        UserType userType = generateFlatEntityWithoutId(UserType.class);
        return userTypeRepository.save(userType);
    }

    public PortalUser createPortalUser(UserType userType) {
        PortalUser portalUser = generateFlatEntityWithoutId(PortalUser.class);
        portalUser.setUserType(userType);
        return portalUserRepository.save(portalUser);
    }

    public ReleaseDetail createReleaseDetail(Movie movie, Country country) {
        ReleaseDetail releaseDetail = generateFlatEntityWithoutId(ReleaseDetail.class);
        releaseDetail.setMovie(movie);
        releaseDetail.setCountry(country);
        return releaseDetailRepository.save(releaseDetail);
    }

    public RoleReviewCompliant createRoleReviewCompliant(PortalUser portalUser, Role role, RoleReview roleReview) {
        RoleReviewCompliant roleReviewCompliant = generateFlatEntityWithoutId(RoleReviewCompliant.class);
        roleReviewCompliant.setPortalUser(portalUser);
        roleReviewCompliant.setRole(role);
        roleReviewCompliant.setRoleReview(roleReview);
        roleReviewCompliant.setModerator(portalUser);
        return roleReviewCompliantRepository.save(roleReviewCompliant);
    }

    public Role createRole(Person person) {
        Role role = generateFlatEntityWithoutId(Role.class);
        role.setPerson(person);
        role.setAverageRating(null);
        return roleRepository.save(role);
    }

    public Role createRole(Person person, Movie movie) {
        Role role = generateFlatEntityWithoutId(Role.class);
        role.setPerson(person);
        role.setMovie(movie);
        role.setAverageRating(null);
        return roleRepository.save(role);
    }

    public Role createRoleWithRating(Person person, Movie movie, Double rating) {
        Role role = generateFlatEntityWithoutId(Role.class);
        role.setPerson(person);
        role.setMovie(movie);
        role.setAverageRating(rating);
        return roleRepository.save(role);
    }

    public RoleReview createRoleReview(PortalUser portalUser, Role role) {
        RoleReview roleReview = generateFlatEntityWithoutId(RoleReview.class);
        roleReview.setPortalUser(portalUser);
        roleReview.setRole(role);
        roleReview.setModerator(portalUser);
        return roleReviewRepository.save(roleReview);
    }

    public RoleReviewFeedback createRoleReviewFeedback(PortalUser portalUser, Role role, RoleReview roleReview) {
        RoleReviewFeedback roleReviewFeedback = generateFlatEntityWithoutId(RoleReviewFeedback.class);
        roleReviewFeedback.setPortalUser(portalUser);
        roleReviewFeedback.setRole(role);
        roleReviewFeedback.setRoleReview(roleReview);
        return roleReviewFeedbackRepository.save(roleReviewFeedback);
    }

    public RoleSpoilerData createRoleSpoilerData(RoleReview roleReview) {
        RoleSpoilerData roleSpoilerData = generateFlatEntityWithoutId(RoleSpoilerData.class);
        roleSpoilerData.setRoleReview(roleReview);
        return roleSpoilerDataRepository.save(roleSpoilerData);
    }

    public RoleVote createRoleVote(PortalUser portalUser, Role role) {
        RoleVote roleVote = generateFlatEntityWithoutId(RoleVote.class);
        roleVote.setRole(role);
        roleVote.setPortalUser(portalUser);
        return roleVoteRepository.save(roleVote);
    }

    public RoleVote createRoleVote(PortalUser portalUser, Role role, UserVoteRatingType userVoteRatingType) {
        RoleVote roleVote = new RoleVote();
        roleVote.setRole(role);
        roleVote.setPortalUser(portalUser);
        roleVote.setRating(userVoteRatingType);
        return roleVoteRepository.save(roleVote);
    }

    public RoleVote createRoleVote(PortalUser portalUser, Role role, Boolean withRating) {
        RoleVote roleVote = new RoleVote();
        roleVote.setRole(role);
        roleVote.setPortalUser(portalUser);
        if (withRating) {
            roleVote.setRating(UserVoteRatingType.R5);
        } else {
            roleVote.setRating(UserVoteRatingType.R1);
        }
        return roleVoteRepository.save(roleVote);
    }

    public UserGrant createGrants(UserType userType, PortalUser portalUser) {
        UserGrant userGrant = generateFlatEntityWithoutId(UserGrant.class);
        userGrant.setUserType(userType);
        userGrant.setGrantedBy(portalUser);
        return userGrantRepository.save(userGrant);
    }

    public UserType createUserTypeWithGrants(Set<UserGrant> userGrants) {
        UserType userType = generateFlatEntityWithoutId(UserType.class);
        userType.setUserGrants(userGrants);
        return userTypeRepository.save(userType);
    }

    public Crew createCrew(Person person, CrewType crewType, Movie movie, String description) {
        Crew crew = new Crew();
        crew.setPerson(person);
        crew.setCrewType(crewType);
        crew.setMovie(movie);
        crew.setDescription(description);
        return crewRepository.save(crew);
    }

    public CrewType createCrewType(String typeName) {
        CrewType crewType = new CrewType();
        crewType.setName(typeName);
        return crewTypeRepository.save(crewType);
    }

    public UserGrant createUserGrant(UserType userType) {
        UserGrant userGrant = generateFlatEntityWithoutId(UserGrant.class);
        userGrant.setUserType(userType);
        return userGrantRepository.save(userGrant);
    }

    public UserGrant createUserGrant(UserType userType, PortalUser grantedBy) {
        UserGrant userGrant = generateFlatEntityWithoutId(UserGrant.class);
        userGrant.setUserType(userType);
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

    public NewsUserReview createNewsUserReview(PortalUser portalUser, News news, PortalUser moderator,
                                               ModeratorTypoReviewStatusType moderatorTypoReviewStatusType) {
        NewsUserReview newsUserReview = new NewsUserReview();
        newsUserReview.setPortalUser(portalUser);
        newsUserReview.setNews(news);
        newsUserReview.setModeratorTypoReviewStatusType(moderatorTypoReviewStatusType);
        newsUserReview.setModerator(moderator);
        return newsUserReviewRepository.save(newsUserReview);
    }

    public NewsUserReviewNote createNewsUserReviewNote(PortalUser moderator, NewsUserReview newsUserReview,
                                                       Integer startIndex, Integer endIndex, String proposedText,
                                                       ModeratorTypoReviewStatusType moderatorTypoReviewStatusType,
                                                       News news, String sourceText) {
        NewsUserReviewNote newsUserReviewNote = generateFlatEntityWithoutId(NewsUserReviewNote.class);
        newsUserReviewNote.setModerator(moderator);
        newsUserReviewNote.setNewsUserReview(newsUserReview);
        newsUserReviewNote.setStartIndex(startIndex);
        newsUserReviewNote.setEndIndex(endIndex);
        newsUserReviewNote.setProposedText(proposedText);
        newsUserReviewNote.setModeratorTypoReviewStatusType(moderatorTypoReviewStatusType);
        newsUserReviewNote.setNews(news);
        newsUserReviewNote.setSourceText(sourceText);
        return newsUserReviewNoteRepository.save(newsUserReviewNote);
    }

    public CountryCreateDTO createCountryCreateDTO() {
        return generateObject(CountryCreateDTO.class);
    }

    public CountryPatchDTO createCountryPatchDTO() {
        return generateObject(CountryPatchDTO.class);
    }

    public CountryPutDTO createCountryPutDTO() {
        return generateObject(CountryPutDTO.class);
    }

    public CrewCreateDTO createCrewCreateDTO() {
        return generateObject(CrewCreateDTO.class);
    }

    public CrewPatchDTO createCrewPatchDTO() {
        return generateObject(CrewPatchDTO.class);
    }

    public CrewPutDTO createCrewPutDTO() {
        return generateObject(CrewPutDTO.class);
    }

    public CrewTypeCreateDTO createCrewTypeCreateDTO() {
        return generateObject(CrewTypeCreateDTO.class);
    }

    public CrewTypePatchDTO createCrewTypePatchDTO() {
        return generateObject(CrewTypePatchDTO.class);
    }

    public CrewTypePutDTO createCrewTypePutDTO() {
        return generateObject(CrewTypePutDTO.class);
    }

    public MovieReviewCompliantCreateDTO createMovieReviewCompliantCreateDTO() {
        return generateObject(MovieReviewCompliantCreateDTO.class);
    }

    public MovieReviewCompliantPatchDTO createMovieReviewCompliantPatchDTO() {
        return generateObject(MovieReviewCompliantPatchDTO.class);
    }

    public MovieReviewCompliantPutDTO createMovieReviewCompliantPutDTO() {
        return generateObject(MovieReviewCompliantPutDTO.class);
    }

    public MovieReviewFeedbackCreateDTO createMovieReviewFeedbackCreateDTO() {
        return generateObject(MovieReviewFeedbackCreateDTO.class);
    }

    public MovieReviewFeedbackPatchDTO createMovieReviewFeedbackPatchDTO() {
        return generateObject(MovieReviewFeedbackPatchDTO.class);
    }

    public MovieReviewFeedbackPutDTO createMovieReviewFeedbackPutDTO() {
        return generateObject(MovieReviewFeedbackPutDTO.class);
    }

    public MovieReviewCreateDTO createMovieReviewCreateDTO() {
        return generateObject(MovieReviewCreateDTO.class);
    }

    public MovieReviewPatchDTO createMovieReviewPatchDTO() {
        return generateObject(MovieReviewPatchDTO.class);
    }

    public MovieReviewPutDTO createMovieReviewPutDTO() {
        return generateObject(MovieReviewPutDTO.class);
    }

    public MovieSpoilerDataCreateDTO createMovieSpoilerDataCreateDTO() {
        return generateObject(MovieSpoilerDataCreateDTO.class);
    }

    public MovieSpoilerDataPatchDTO createMovieSpoilerDataPatchDTO() {
        return generateObject(MovieSpoilerDataPatchDTO.class);
    }

    public MovieSpoilerDataPutDTO createMovieSpoilerDataPutDTO() {
        return generateObject(MovieSpoilerDataPutDTO.class);
    }

    public MovieVoteCreateDTO createMovieVoteCreateDTO() {
        return generateObject(MovieVoteCreateDTO.class);
    }

    public MovieVotePatchDTO createMovieVotePatchDTO() {
        return generateObject(MovieVotePatchDTO.class);
    }

    public MovieVotePutDTO createMovieVotePutDTO() {
        return generateObject(MovieVotePutDTO.class);
    }

    public NewsCreateDTO createNewsCreateDTO() {
        return generateObject(NewsCreateDTO.class);
    }

    public NewsPatchDTO createNewsPatchDTO() {
        return generateObject(NewsPatchDTO.class);
    }

    public NewsPutDTO createNewsPutDTO() {
        return generateObject(NewsPutDTO.class);
    }

    public NewsUserReviewNoteCreateDTO createNewsUserReviewNoteCreateDTO() {
        return generateObject(NewsUserReviewNoteCreateDTO.class);
    }

    public NewsUserReviewNotePatchDTO createNewsUserReviewNotePatchDTO() {
        return generateObject(NewsUserReviewNotePatchDTO.class);
    }

    public NewsUserReviewNotePutDTO createNewsUserReviewNotePutDTO() {
        return generateObject(NewsUserReviewNotePutDTO.class);
    }

    public NewsUserReviewCreateDTO createNewsUserReviewCreateDTO() {
        return generateObject(NewsUserReviewCreateDTO.class);
    }

    public NewsUserReviewPatchDTO createNewsUserReviewPatchDTO() {
        return generateObject(NewsUserReviewPatchDTO.class);
    }

    public NewsUserReviewPutDTO createNewsUserReviewPutDTO() {
        return generateObject(NewsUserReviewPutDTO.class);
    }

    public PersonCreateDTO createPersonCreateDTO() {
        return generateObject(PersonCreateDTO.class);
    }

    public PersonPatchDTO createPersonPatchDTO() {
        return generateObject(PersonPatchDTO.class);
    }

    public PersonPutDTO createPersonPutDTO() {
        return generateObject(PersonPutDTO.class);
    }

    public RoleReviewCreateDTO createRoleReviewCreateDTO() {
        return generateObject(RoleReviewCreateDTO.class);
    }

    public RoleReviewPatchDTO createRoleReviewPatchDTO() {
        return generateObject(RoleReviewPatchDTO.class);
    }

    public RoleReviewPutDTO createRoleReviewPutDTO() {
        return generateObject(RoleReviewPutDTO.class);
    }

    public PortalUserCreateDTO createPortalUserCreateDTO() {
        return generateObject(PortalUserCreateDTO.class);
    }

    public PortalUserPatchDTO createPortalUserPatchDTO() {
        return generateObject(PortalUserPatchDTO.class);
    }

    public PortalUserPutDTO createPortalUserPutDTO() {
        return generateObject(PortalUserPutDTO.class);
    }

    public ReleaseDetailCreateDTO createReleaseDetailCreateDTO() {
        return generateObject(ReleaseDetailCreateDTO.class);
    }

    public ReleaseDetailPatchDTO createReleaseDetailPatchDTO() {
        return generateObject(ReleaseDetailPatchDTO.class);
    }

    public ReleaseDetailPutDTO createReleaseDetailPutDTO() {
        return generateObject(ReleaseDetailPutDTO.class);
    }

    public RoleReviewCompliantCreateDTO createRoleReviewCompliantCreateDTO() {
        return generateObject(RoleReviewCompliantCreateDTO.class);
    }

    public RoleReviewCompliantPatchDTO createRoleReviewCompliantPatchDTO() {
        return generateObject(RoleReviewCompliantPatchDTO.class);
    }

    public RoleReviewCompliantPutDTO createRoleReviewCompliantPutDTO() {
        return generateObject(RoleReviewCompliantPutDTO.class);
    }

    public RoleReviewFeedbackCreateDTO createRoleReviewFeedbackCreateDTO() {
        return generateObject(RoleReviewFeedbackCreateDTO.class);
    }

    public RoleReviewFeedbackPatchDTO createRoleReviewFeedbackPatchDTO() {
        return generateObject(RoleReviewFeedbackPatchDTO.class);
    }

    public RoleReviewFeedbackPutDTO createRoleReviewFeedbackPutDTO() {
        return generateObject(RoleReviewFeedbackPutDTO.class);
    }

    public RoleSpoilerDataCreateDTO createRoleSpoilerDataCreateDTO() {
        return generateObject(RoleSpoilerDataCreateDTO.class);
    }

    public RoleSpoilerDataPatchDTO createRoleSpoilerDataPatchDTO() {
        return generateObject(RoleSpoilerDataPatchDTO.class);
    }

    public RoleSpoilerDataPutDTO createRoleSpoilerDataPutDTO() {
        return generateObject(RoleSpoilerDataPutDTO.class);
    }

    public RoleCreateDTO createRoleCreateDTO() {
        return generateObject(RoleCreateDTO.class);
    }

    public RolePatchDTO createRolePatchDTO() {
        return generateObject(RolePatchDTO.class);
    }

    public RolePutDTO createRolePutDTO() {
        return generateObject(RolePutDTO.class);
    }

    public RoleVoteCreateDTO createRoleVoteCreateDTO() {
        return generateObject(RoleVoteCreateDTO.class);
    }

    public RoleVotePatchDTO createRoleVotePatchDTO() {
        return generateObject(RoleVotePatchDTO.class);
    }

    public RoleVotePutDTO createRoleVotePutDTO() {
        return generateObject(RoleVotePutDTO.class);
    }

    public UserGrantCreateDTO createUserGrantCreateDTO() {
        return generateObject(UserGrantCreateDTO.class);
    }

    public UserGrantPatchDTO createUserGrantPatchDTO() {
        return generateObject(UserGrantPatchDTO.class);
    }

    public UserGrantPutDTO createUserGrantPutDTO() {
        return generateObject(UserGrantPutDTO.class);
    }

    public UserTypeCreateDTO createUserTypeCreateDTO() {
        return generateObject(UserTypeCreateDTO.class);
    }

    public UserTypePatchDTO createUserTypePatchDTO() {
        return generateObject(UserTypePatchDTO.class);
    }

    public UserTypePutDTO createUserTypePutDTO() {
        return generateObject(UserTypePutDTO.class);
    }
}
