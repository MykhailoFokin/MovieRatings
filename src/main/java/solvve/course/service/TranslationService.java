package solvve.course.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import solvve.course.domain.*;
import solvve.course.dto.*;
import solvve.course.repository.*;

@Service
public class TranslationService {

    @Autowired
    private RepositoryHelper repositoryHelper;

    public VisitReadExtendedDTO toReadExtended(Visit visit) {
        VisitReadExtendedDTO dto = new VisitReadExtendedDTO();
        dto.setId(visit.getId());
        dto.setPortalUser(toRead(visit.getPortalUser()));
        dto.setStartAt(visit.getStartAt());
        dto.setFinishAt(visit.getFinishAt());
        dto.setStatus(visit.getStatus());
        dto.setCreatedAt(visit.getCreatedAt());
        dto.setUpdatedAt(visit.getUpdatedAt());
        return dto;
    }

    public CrewReadExtendedDTO toReadExtended(Crew crew) {
        CrewReadExtendedDTO dto = new CrewReadExtendedDTO();
        dto.setId(crew.getId());
        dto.setMovie(toRead(crew.getMovie()));
        dto.setPerson(toRead(crew.getPerson()));
        dto.setCrewType(toRead(crew.getCrewType()));
        dto.setDescription(crew.getDescription());
        dto.setCreatedAt(crew.getCreatedAt());
        dto.setUpdatedAt(crew.getUpdatedAt());
        return dto;
    }

    public MovieReadExtendedDTO toReadExtended(Movie movie) {
        MovieReadExtendedDTO extendedDto = new MovieReadExtendedDTO();
        extendedDto.setId(movie.getId());
        extendedDto.setTitle(movie.getTitle());
        extendedDto.setYear(movie.getYear());
        extendedDto.setDescription(movie.getDescription());
        extendedDto.setSoundMix(movie.getSoundMix());
        extendedDto.setColour(movie.getColour());
        extendedDto.setAspectRatio(movie.getAspectRatio());
        extendedDto.setCamera(movie.getCamera());
        extendedDto.setLaboratory(movie.getLaboratory());
        extendedDto.setCritique(movie.getCritique());
        extendedDto.setIsPublished(movie.getIsPublished());
        extendedDto.setMovieCompanies(movie.getMovieProdCompanies());
        extendedDto.setLanguages(movie.getMovieProdLanguages());
        extendedDto.setMovieProdCountries(movie.getMovieProdCountries());
        extendedDto.setMovieReview(movie.getMovieReview());
        extendedDto.setMovieReviewCompliants(movie.getMovieReviewCompliants());
        extendedDto.setMovieReviewFeedbacks(movie.getMovieReviewFeedbacks());
        extendedDto.setCrews(movie.getCrews());
        extendedDto.setReleaseDetails(movie.getReleaseDetails());
        extendedDto.setMovieVotes(movie.getMovieVotes());
        extendedDto.setCreatedAt(movie.getCreatedAt());
        extendedDto.setUpdatedAt(movie.getUpdatedAt());
        extendedDto.setAverageRating(movie.getAverageRating());
        return extendedDto;
    }

    public VisitReadDTO toRead(Visit visit) {
        VisitReadDTO dto = new VisitReadDTO();
        dto.setId(visit.getId());
        dto.setPortalUserId(visit.getPortalUser().getId());
        dto.setStartAt(visit.getStartAt());
        dto.setFinishAt(visit.getFinishAt());
        dto.setStatus(visit.getStatus());
        dto.setCreatedAt(visit.getCreatedAt());
        dto.setUpdatedAt(visit.getUpdatedAt());
        return dto;
    }

    public CompanyDetailsReadDTO toRead(CompanyDetails companyDetails) {
        CompanyDetailsReadDTO dto = new CompanyDetailsReadDTO();
        dto.setId(companyDetails.getId());
        dto.setName(companyDetails.getName());
        dto.setOverview(companyDetails.getOverview());
        dto.setYearOfFoundation(companyDetails.getYearOfFoundation());
        dto.setCreatedAt(companyDetails.getCreatedAt());
        dto.setUpdatedAt(companyDetails.getUpdatedAt());
        return dto;
    }

    public CountryReadDTO toRead(Country country) {
        CountryReadDTO dto = new CountryReadDTO();
        dto.setId(country.getId());
        dto.setName(country.getName());
        dto.setCreatedAt(country.getCreatedAt());
        dto.setUpdatedAt(country.getUpdatedAt());
        return dto;
    }

    public CrewReadDTO toRead(Crew crew) {
        CrewReadDTO dto = new CrewReadDTO();
        dto.setId(crew.getId());
        dto.setMovieId(crew.getMovie().getId());
        dto.setPersonId(crew.getPerson().getId());
        if (crew.getCrewType() != null) {
            dto.setCrewTypeId(crew.getCrewType().getId());
        }
        dto.setDescription(crew.getDescription());
        dto.setCreatedAt(crew.getCreatedAt());
        dto.setUpdatedAt(crew.getUpdatedAt());
        return dto;
    }

    public CrewTypeReadDTO toRead(CrewType crewType) {
        CrewTypeReadDTO dto = new CrewTypeReadDTO();
        dto.setId(crewType.getId());
        dto.setName(crewType.getName());
        dto.setCreatedAt(crewType.getCreatedAt());
        dto.setUpdatedAt(crewType.getUpdatedAt());
        return dto;
    }

    public GenreReadDTO toRead(Genre genre) {
        GenreReadDTO dto = new GenreReadDTO();
        dto.setId(genre.getId());
        dto.setMovieId(genre.getMovie().getId());
        dto.setName(genre.getName());
        dto.setCreatedAt(genre.getCreatedAt());
        dto.setUpdatedAt(genre.getUpdatedAt());
        return dto;
    }

    public LanguageReadDTO toRead(Language language) {
        LanguageReadDTO dto = new LanguageReadDTO();
        dto.setId(language.getId());
        dto.setName(language.getName());
        dto.setCreatedAt(language.getCreatedAt());
        dto.setUpdatedAt(language.getUpdatedAt());
        return dto;
    }

    public UserGrantReadDTO toRead(UserGrant userGrant) {
        UserGrantReadDTO dto = new UserGrantReadDTO();
        dto.setId(userGrant.getId());
        dto.setUserTypeId(userGrant.getUserType().getId());
        dto.setUserPermission(userGrant.getUserPermission());
        dto.setObjectName(userGrant.getObjectName());
        dto.setGrantedById(userGrant.getGrantedBy().getId());
        dto.setCreatedAt(userGrant.getCreatedAt());
        dto.setUpdatedAt(userGrant.getUpdatedAt());
        return dto;
    }

    public MovieCompanyReadDTO toRead(MovieCompany movieCompany) {
        MovieCompanyReadDTO dto = new MovieCompanyReadDTO();
        dto.setId(movieCompany.getId());
        dto.setCompanyDetailsId(movieCompany.getCompanyDetails().getId());
        dto.setMovieProductionType(movieCompany.getMovieProductionType());
        dto.setDescription(movieCompany.getDescription());
        dto.setCreatedAt(movieCompany.getCreatedAt());
        dto.setUpdatedAt(movieCompany.getUpdatedAt());
        return dto;
    }

    public MovieReviewCompliantReadDTO toRead(MovieReviewCompliant movieReviewCompliant) {
        MovieReviewCompliantReadDTO dto = new MovieReviewCompliantReadDTO();
        dto.setId(movieReviewCompliant.getId());
        dto.setPortalUserId(movieReviewCompliant.getPortalUser().getId());
        dto.setMovieId(movieReviewCompliant.getMovie().getId());
        dto.setMovieReviewId(movieReviewCompliant.getMovieReview().getId());
        dto.setDescription(movieReviewCompliant.getDescription());
        dto.setModeratedStatus(movieReviewCompliant.getModeratedStatus());
        if (movieReviewCompliant.getModerator() != null) {
            dto.setModeratorId(movieReviewCompliant.getModerator().getId());
        }
        dto.setCreatedAt(movieReviewCompliant.getCreatedAt());
        dto.setUpdatedAt(movieReviewCompliant.getUpdatedAt());
        return dto;
    }

    public MovieReviewFeedbackReadDTO toRead(MovieReviewFeedback movieReviewFeedback) {
        MovieReviewFeedbackReadDTO dto = new MovieReviewFeedbackReadDTO();
        dto.setId(movieReviewFeedback.getId());
        dto.setPortalUserId(movieReviewFeedback.getPortalUser().getId());
        dto.setMovieId(movieReviewFeedback.getMovie().getId());
        dto.setMovieReviewId(movieReviewFeedback.getMovieReview().getId());
        dto.setIsLiked(movieReviewFeedback.getIsLiked());
        dto.setCreatedAt(movieReviewFeedback.getCreatedAt());
        dto.setUpdatedAt(movieReviewFeedback.getUpdatedAt());
        return dto;
    }

    public MovieReviewReadDTO toRead(MovieReview movieReview) {
        MovieReviewReadDTO dto = new MovieReviewReadDTO();
        dto.setId(movieReview.getId());
        dto.setMovieId(movieReview.getMovie().getId());
        dto.setPortalUserId(movieReview.getPortalUser().getId());
        dto.setTextReview(movieReview.getTextReview());
        dto.setModeratedStatus(movieReview.getModeratedStatus());
        if (movieReview.getModerator() != null) {
            dto.setModeratorId(movieReview.getModerator().getId());
        }
        dto.setCreatedAt(movieReview.getCreatedAt());
        dto.setUpdatedAt(movieReview.getUpdatedAt());
        return dto;
    }

    public MovieReadDTO toRead(Movie movie) {
        MovieReadDTO dto = new MovieReadDTO();
        dto.setId(movie.getId());
        dto.setTitle(movie.getTitle());
        dto.setYear(movie.getYear());
        dto.setDescription(movie.getDescription());
        dto.setSoundMix(movie.getSoundMix());
        dto.setColour(movie.getColour());
        dto.setAspectRatio(movie.getAspectRatio());
        dto.setCamera(movie.getCamera());
        dto.setLaboratory(movie.getLaboratory());
        dto.setCritique(movie.getCritique());
        dto.setIsPublished(movie.getIsPublished());
        dto.setCreatedAt(movie.getCreatedAt());
        dto.setUpdatedAt(movie.getUpdatedAt());
        dto.setAverageRating(movie.getAverageRating());
        return dto;
    }

    public MovieSpoilerDataReadDTO toRead(MovieSpoilerData movieSpoilerData) {
        MovieSpoilerDataReadDTO dto = new MovieSpoilerDataReadDTO();
        dto.setId(movieSpoilerData.getId());
        dto.setMovieReviewId(movieSpoilerData.getMovieReview().getId());
        dto.setStartIndex(movieSpoilerData.getStartIndex());
        dto.setEndIndex(movieSpoilerData.getEndIndex());
        dto.setCreatedAt(movieSpoilerData.getCreatedAt());
        dto.setUpdatedAt(movieSpoilerData.getUpdatedAt());
        return dto;
    }

    public MovieVoteReadDTO toRead(MovieVote movieVote) {
        MovieVoteReadDTO dto = new MovieVoteReadDTO();
        dto.setId(movieVote.getId());
        dto.setPortalUserId(movieVote.getPortalUser().getId());
        dto.setMovieId(movieVote.getMovie().getId());
        dto.setRating(movieVote.getRating());
        dto.setCreatedAt(movieVote.getCreatedAt());
        dto.setUpdatedAt(movieVote.getUpdatedAt());
        return dto;
    }

    public NewsReadDTO toRead(News news) {
        NewsReadDTO dto = new NewsReadDTO();
        dto.setId(news.getId());
        dto.setPublisherId(news.getPublisher().getId());
        dto.setPublished(news.getPublished());
        dto.setTopic(news.getTopic());
        dto.setDescription(news.getDescription());
        dto.setCreatedAt(news.getCreatedAt());
        dto.setUpdatedAt(news.getUpdatedAt());
        return dto;
    }

    public PersonReadDTO toRead(Person person) {
        PersonReadDTO dto = new PersonReadDTO();
        dto.setId(person.getId());
        dto.setSurname(person.getSurname());
        dto.setName(person.getName());
        dto.setMiddleName(person.getMiddleName());
        dto.setCreatedAt(person.getCreatedAt());
        dto.setUpdatedAt(person.getUpdatedAt());
        return dto;
    }

    public PortalUserReadDTO toRead(PortalUser portalUser) {
        PortalUserReadDTO dto = new PortalUserReadDTO();
        dto.setId(portalUser.getId());
        dto.setLogin(portalUser.getLogin());
        dto.setSurname(portalUser.getSurname());
        dto.setName(portalUser.getName());
        dto.setMiddleName(portalUser.getMiddleName());
        dto.setUserTypeId(portalUser.getUserType().getId());
        dto.setUserConfidence(portalUser.getUserConfidence());
        dto.setCreatedAt(portalUser.getCreatedAt());
        dto.setUpdatedAt(portalUser.getUpdatedAt());
        return dto;
    }

    public ReleaseDetailReadDTO toRead(ReleaseDetail releaseDetail) {
        ReleaseDetailReadDTO dto = new ReleaseDetailReadDTO();
        dto.setId(releaseDetail.getId());
        dto.setMovieId(releaseDetail.getMovie().getId());
        dto.setReleaseDate(releaseDetail.getReleaseDate());
        dto.setCountryId(releaseDetail.getCountry().getId());
        dto.setCreatedAt(releaseDetail.getCreatedAt());
        dto.setUpdatedAt(releaseDetail.getUpdatedAt());
        return dto;
    }

    public RoleReviewCompliantReadDTO toRead(RoleReviewCompliant roleReviewCompliant) {
        RoleReviewCompliantReadDTO dto = new RoleReviewCompliantReadDTO();
        dto.setId(roleReviewCompliant.getId());
        dto.setPortalUserId(roleReviewCompliant.getPortalUser().getId());
        dto.setRoleId(roleReviewCompliant.getRole().getId());
        dto.setRoleReviewId(roleReviewCompliant.getRoleReview().getId());
        dto.setDescription(roleReviewCompliant.getDescription());
        dto.setModeratedStatus(roleReviewCompliant.getModeratedStatus());
        if (roleReviewCompliant.getModerator() != null) {
            dto.setModeratorId(roleReviewCompliant.getModerator().getId());
        }
        dto.setCreatedAt(roleReviewCompliant.getCreatedAt());
        dto.setUpdatedAt(roleReviewCompliant.getUpdatedAt());
        return dto;
    }

    public RoleReviewFeedbackReadDTO toRead(RoleReviewFeedback roleReviewFeedback) {
        RoleReviewFeedbackReadDTO dto = new RoleReviewFeedbackReadDTO();
        dto.setId(roleReviewFeedback.getId());
        dto.setPortalUserId(roleReviewFeedback.getPortalUser().getId());
        dto.setRoleId(roleReviewFeedback.getRole().getId());
        dto.setRoleReviewId(roleReviewFeedback.getRoleReview().getId());
        dto.setIsLiked(roleReviewFeedback.getIsLiked());
        dto.setCreatedAt(roleReviewFeedback.getCreatedAt());
        dto.setUpdatedAt(roleReviewFeedback.getUpdatedAt());
        return dto;
    }

    public RoleReviewReadDTO toRead(RoleReview roleReview) {
        RoleReviewReadDTO dto = new RoleReviewReadDTO();
        dto.setId(roleReview.getId());
        dto.setPortalUserId(roleReview.getPortalUser().getId());
        dto.setRoleId(roleReview.getRole().getId());
        dto.setTextReview(roleReview.getTextReview());
        dto.setModeratedStatus(roleReview.getModeratedStatus());
        if (roleReview.getModerator() != null) {
            dto.setModeratorId(roleReview.getModerator().getId());
        }
        dto.setCreatedAt(roleReview.getCreatedAt());
        dto.setUpdatedAt(roleReview.getUpdatedAt());
        return dto;
    }

    public RoleReadDTO toRead(Role role) {
        RoleReadDTO dto = new RoleReadDTO();
        dto.setId(role.getId());
        dto.setTitle(role.getTitle());
        dto.setRoleType(role.getRoleType());
        dto.setDescription(role.getDescription());
        if (role.getPerson() != null) {
            dto.setPersonId(role.getPerson().getId());
        }
        dto.setMovieId(role.getMovie().getId());
        dto.setCreatedAt(role.getCreatedAt());
        dto.setUpdatedAt(role.getUpdatedAt());
        dto.setAverageRating(role.getAverageRating());
        return dto;
    }

    public RoleSpoilerDataReadDTO toRead(RoleSpoilerData roleSpoilerData) {
        RoleSpoilerDataReadDTO dto = new RoleSpoilerDataReadDTO();
        dto.setId(roleSpoilerData.getId());
        dto.setRoleReviewId(roleSpoilerData.getRoleReview().getId());
        dto.setStartIndex(roleSpoilerData.getStartIndex());
        dto.setEndIndex(roleSpoilerData.getEndIndex());
        dto.setCreatedAt(roleSpoilerData.getCreatedAt());
        dto.setUpdatedAt(roleSpoilerData.getUpdatedAt());
        return dto;
    }

    public RoleVoteReadDTO toRead(RoleVote roleVote) {
        RoleVoteReadDTO dto = new RoleVoteReadDTO();
        dto.setId(roleVote.getId());
        dto.setPortalUserId(roleVote.getPortalUser().getId());
        dto.setRoleId(roleVote.getRole().getId());
        dto.setRating(roleVote.getRating());
        dto.setCreatedAt(roleVote.getCreatedAt());
        dto.setUpdatedAt(roleVote.getUpdatedAt());
        return dto;
    }

    public UserTypeReadDTO toRead(UserType userType) {
        UserTypeReadDTO dto = new UserTypeReadDTO();
        dto.setId(userType.getId());
        dto.setUserGroup(userType.getUserGroup());
        dto.setCreatedAt(userType.getCreatedAt());
        dto.setUpdatedAt(userType.getUpdatedAt());
        return dto;
    }

    public Visit toEntity(VisitCreateDTO create) {
        Visit visit = new Visit();
        visit.setPortalUser(repositoryHelper.getReferenceIfExists(PortalUser.class, create.getPortalUserId()));
        visit.setStartAt(create.getStartAt());
        visit.setFinishAt(create.getFinishAt());
        visit.setStatus(create.getStatus());
        return visit;
    }

    public CompanyDetails toEntity(CompanyDetailsCreateDTO create) {
        CompanyDetails companyDetails = new CompanyDetails();
        companyDetails.setName(create.getName());
        companyDetails.setOverview(create.getOverview());
        companyDetails.setYearOfFoundation(create.getYearOfFoundation());
        return companyDetails;
    }

    public Country toEntity(CountryCreateDTO create) {
        Country country = new Country();
        country.setName(create.getName());
        return country;
    }

    public Crew toEntity(CrewCreateDTO create) {
        Crew crew = new Crew();
        crew.setMovie(repositoryHelper.getReferenceIfExists(Movie.class, create.getMovieId()));
        crew.setPerson(repositoryHelper.getReferenceIfExists(Person.class, create.getPersonId()));
        crew.setCrewType(repositoryHelper.getReferenceIfExists(CrewType.class, create.getCrewTypeId()));
        crew.setDescription(create.getDescription());
        return crew;
    }

    public CrewType toEntity(CrewTypeCreateDTO create) {
        CrewType crewType = new CrewType();
        crewType.setName(create.getName());
        return crewType;
    }

    public Genre toEntity(GenreCreateDTO create) {
        Genre genre = new Genre();
        genre.setMovie(repositoryHelper.getReferenceIfExists(Movie.class, create.getMovieId()));
        genre.setName(create.getName());
        return genre;
    }

    public UserGrant toEntity(UserGrantCreateDTO create) {
        UserGrant userGrant = new UserGrant();
        userGrant.setUserType(repositoryHelper.getReferenceIfExists(UserType.class, create.getUserTypeId()));
        userGrant.setUserPermission(create.getUserPermission());
        userGrant.setObjectName(create.getObjectName());
        userGrant.setGrantedBy(repositoryHelper.getReferenceIfExists(PortalUser.class, create.getGrantedById()));
        return userGrant;
    }

    public Language toEntity(LanguageCreateDTO create) {
        Language language = new Language();
        language.setName(create.getName());
        return language;
    }

    public MovieCompany toEntity(MovieCompanyCreateDTO create) {
        MovieCompany movieCompany = new MovieCompany();
        movieCompany.setCompanyDetails(repositoryHelper
                .getReferenceIfExists(CompanyDetails.class, create.getCompanyDetailsId()));
        movieCompany.setMovieProductionType(create.getMovieProductionType());
        movieCompany.setDescription(create.getDescription());
        return movieCompany;
    }

    public MovieReviewCompliant toEntity(MovieReviewCompliantCreateDTO create) {
        MovieReviewCompliant movieReviewCompliant = new MovieReviewCompliant();
        movieReviewCompliant.setPortalUser(repositoryHelper.getReferenceIfExists(PortalUser.class,
                create.getPortalUserId()));
        movieReviewCompliant.setMovie(repositoryHelper.getReferenceIfExists(Movie.class, create.getMovieId()));
        movieReviewCompliant.setMovieReview(repositoryHelper
                .getReferenceIfExists(MovieReview.class, create.getMovieReviewId()));
        movieReviewCompliant.setDescription(create.getDescription());
        movieReviewCompliant.setModeratedStatus(create.getModeratedStatus());
        movieReviewCompliant.setModerator(repositoryHelper
                .getReferenceIfExists(PortalUser.class, create.getModeratorId()));
        return movieReviewCompliant;
    }

    public MovieReviewFeedback toEntity(MovieReviewFeedbackCreateDTO create) {
        MovieReviewFeedback movieReviewFeedback = new MovieReviewFeedback();
        movieReviewFeedback.setPortalUser(repositoryHelper
                .getReferenceIfExists(PortalUser.class, create.getPortalUserId()));
        movieReviewFeedback.setMovie(repositoryHelper.getReferenceIfExists(Movie.class, create.getMovieId()));
        movieReviewFeedback.setMovieReview(repositoryHelper
                .getReferenceIfExists(MovieReview.class, create.getMovieReviewId()));
        movieReviewFeedback.setIsLiked(create.getIsLiked());
        return movieReviewFeedback;
    }

    public MovieReview toEntity(MovieReviewCreateDTO create) {
        MovieReview movieReview = new MovieReview();
        movieReview.setMovie(repositoryHelper.getReferenceIfExists(Movie.class, create.getMovieId()));
        movieReview.setPortalUser(repositoryHelper.getReferenceIfExists(PortalUser.class, create.getPortalUserId()));
        movieReview.setTextReview(create.getTextReview());
        movieReview.setModeratedStatus(create.getModeratedStatus());
        movieReview.setModerator(repositoryHelper.getReferenceIfExists(PortalUser.class, create.getModeratorId()));
        return movieReview;
    }

    public Movie toEntity(MovieCreateDTO create) {
        Movie movie = new Movie();
        movie.setTitle(create.getTitle());
        movie.setYear(create.getYear());
        movie.setDescription(create.getDescription());
        movie.setSoundMix(create.getSoundMix());
        movie.setColour(create.getColour());
        movie.setAspectRatio(create.getAspectRatio());
        movie.setCamera(create.getCamera());
        movie.setLaboratory(create.getLaboratory());
        movie.setCritique(create.getCritique());
        movie.setIsPublished(create.getIsPublished());
        return movie;
    }

    public MovieSpoilerData toEntity(MovieSpoilerDataCreateDTO create) {
        MovieSpoilerData movieSpoilerData = new MovieSpoilerData();
        movieSpoilerData.setMovieReview(repositoryHelper
                .getReferenceIfExists(MovieReview.class, create.getMovieReviewId()));
        movieSpoilerData.setStartIndex(create.getStartIndex());
        movieSpoilerData.setEndIndex(create.getEndIndex());
        return movieSpoilerData;
    }

    public MovieVote toEntity(MovieVoteCreateDTO create) {
        MovieVote movieVote = new MovieVote();
        movieVote.setPortalUser(repositoryHelper.getReferenceIfExists(PortalUser.class, create.getPortalUserId()));
        movieVote.setMovie(repositoryHelper.getReferenceIfExists(Movie.class, create.getMovieId()));
        movieVote.setRating(create.getRating());
        return movieVote;
    }

    public News toEntity(NewsCreateDTO create) {
        News news = new News();
        news.setPublisher(repositoryHelper.getReferenceIfExists(PortalUser.class, create.getPublisherId()));
        news.setPublished(create.getPublished());
        news.setTopic(create.getTopic());
        news.setDescription(create.getDescription());
        return news;
    }

    public Person toEntity(PersonCreateDTO create) {
        Person person = new Person();
        person.setSurname(create.getSurname());
        person.setName(create.getName());
        person.setMiddleName(create.getMiddleName());
        return person;
    }

    public PortalUser toEntity(PortalUserCreateDTO create) {
        PortalUser portalUser = new PortalUser();
        portalUser.setLogin(create.getLogin());
        portalUser.setSurname(create.getSurname());
        portalUser.setName(create.getName());
        portalUser.setMiddleName(create.getMiddleName());
        portalUser.setUserType(repositoryHelper.getReferenceIfExists(UserType.class, create.getUserTypeId()));
        portalUser.setUserConfidence(create.getUserConfidence());
        return portalUser;
    }

    public ReleaseDetail toEntity(ReleaseDetailCreateDTO create) {
        ReleaseDetail releaseDetail = new ReleaseDetail();
        releaseDetail.setMovie(repositoryHelper.getReferenceIfExists(Movie.class, create.getMovieId()));
        releaseDetail.setReleaseDate(create.getReleaseDate());
        releaseDetail.setCountry(repositoryHelper.getReferenceIfExists(Country.class, create.getCountryId()));
        return releaseDetail;
    }

    public RoleReviewCompliant toEntity(RoleReviewCompliantCreateDTO create) {
        RoleReviewCompliant roleReviewCompliant = new RoleReviewCompliant();
        roleReviewCompliant.setPortalUser(repositoryHelper
                .getReferenceIfExists(PortalUser.class, create.getPortalUserId()));
        roleReviewCompliant.setRole(repositoryHelper.getReferenceIfExists(Role.class, create.getRoleId()));
        roleReviewCompliant.setRoleReview(repositoryHelper
                .getReferenceIfExists(RoleReview.class, create.getRoleReviewId()));
        roleReviewCompliant.setDescription(create.getDescription());
        roleReviewCompliant.setModeratedStatus(create.getModeratedStatus());
        roleReviewCompliant.setModerator(repositoryHelper
                .getReferenceIfExists(PortalUser.class, create.getModeratorId()));
        return roleReviewCompliant;
    }

    public RoleReviewFeedback toEntity(RoleReviewFeedbackCreateDTO create) {
        RoleReviewFeedback roleReviewFeedback = new RoleReviewFeedback();
        roleReviewFeedback.setPortalUser(repositoryHelper
                .getReferenceIfExists(PortalUser.class, create.getPortalUserId()));
        roleReviewFeedback.setRole(repositoryHelper.getReferenceIfExists(Role.class, create.getRoleId()));
        roleReviewFeedback.setRoleReview(repositoryHelper
                .getReferenceIfExists(RoleReview.class, create.getRoleReviewId()));
        roleReviewFeedback.setIsLiked(create.getIsLiked());
        return roleReviewFeedback;
    }

    public RoleReview toEntity(RoleReviewCreateDTO create) {
        RoleReview roleReview = new RoleReview();
        roleReview.setPortalUser(repositoryHelper.getReferenceIfExists(PortalUser.class, create.getPortalUserId()));
        roleReview.setRole(repositoryHelper.getReferenceIfExists(Role.class, create.getRoleId()));
        roleReview.setTextReview(create.getTextReview());
        roleReview.setModeratedStatus(create.getModeratedStatus());
        roleReview.setModerator(repositoryHelper.getReferenceIfExists(PortalUser.class, create.getModeratorId()));
        return roleReview;
    }

    public Role toEntity(RoleCreateDTO create) {
        Role role = new Role();
        role.setTitle(create.getTitle());
        role.setRoleType(create.getRoleType());
        role.setDescription(create.getDescription());
        role.setPerson(repositoryHelper.getReferenceIfExists(Person.class, create.getPersonId()));
        role.setMovie(repositoryHelper.getReferenceIfExists(Movie.class, create.getMovieId()));
        return role;
    }

    public RoleSpoilerData toEntity(RoleSpoilerDataCreateDTO create) {
        RoleSpoilerData roleSpoilerData = new RoleSpoilerData();
        roleSpoilerData.setRoleReview(repositoryHelper
                .getReferenceIfExists(RoleReview.class, create.getRoleReviewId()));
        roleSpoilerData.setStartIndex(create.getStartIndex());
        roleSpoilerData.setEndIndex(create.getEndIndex());
        return roleSpoilerData;
    }

    public RoleVote toEntity(RoleVoteCreateDTO create) {
        RoleVote roleVote = new RoleVote();
        roleVote.setPortalUser(repositoryHelper.getReferenceIfExists(PortalUser.class, create.getPortalUserId()));
        roleVote.setRole(repositoryHelper.getReferenceIfExists(Role.class, create.getRoleId()));
        roleVote.setRating(create.getRating());
        return roleVote;
    }

    public UserType toEntity(UserTypeCreateDTO create) {
        UserType userType = new UserType();
        userType.setUserGroup(create.getUserGroup());
        return userType;
    }

    public void patchEntity(CompanyDetailsPatchDTO patch, CompanyDetails companyDetails) {
        if (patch.getName() != null) {
            companyDetails.setName(patch.getName());
        }
        if (patch.getOverview() != null) {
            companyDetails.setOverview(patch.getOverview());
        }
        if (patch.getYearOfFoundation() != null) {
            companyDetails.setYearOfFoundation(patch.getYearOfFoundation());
        }
    }

    public void patchEntity(VisitPatchDTO patch, Visit visit) {
        if (patch.getPortalUserId() != null) {
            visit.setPortalUser(repositoryHelper.getReferenceIfExists(PortalUser.class, patch.getPortalUserId()));
        }
        if (patch.getStartAt() != null) {
            visit.setStartAt(patch.getStartAt());
        }
        if (patch.getFinishAt() != null) {
            visit.setFinishAt(patch.getFinishAt());
        }
        if (patch.getStatus() != null) {
            visit.setStatus(patch.getStatus());
        }
    }

    public void patchEntity(CountryPatchDTO patch, Country country) {
        if (patch.getName() != null) {
            country.setName(patch.getName());
        }
    }

    public void patchEntity(CrewPatchDTO patch, Crew crew) {
        if (patch.getMovieId() != null) {
            crew.setMovie(repositoryHelper.getReferenceIfExists(Movie.class, patch.getMovieId()));
        }
        if (patch.getPersonId() != null) {
            crew.setPerson(repositoryHelper.getReferenceIfExists(Person.class, patch.getPersonId()));
        }
        if (patch.getCrewTypeId() != null) {
            crew.setCrewType(repositoryHelper.getReferenceIfExists(CrewType.class, patch.getCrewTypeId()));
        }
        if (patch.getDescription() != null) {
            crew.setDescription(patch.getDescription());
        }
    }

    public void patchEntity(CrewTypePatchDTO patch, CrewType crewType) {
        if (patch.getName() != null) {
            crewType.setName(patch.getName());
        }
    }

    public void patchEntity(GenrePatchDTO patch, Genre genre) {
        if (patch.getMovieId() != null) {
            genre.setMovie(repositoryHelper.getReferenceIfExists(Movie.class, patch.getMovieId()));
        }
        if (patch.getName() != null) {
            genre.setName(patch.getName());
        }
    }

    public void patchEntity(UserGrantPatchDTO patch, UserGrant userGrant) {
        if (patch.getUserTypeId() != null) {
            userGrant.setUserType(repositoryHelper.getReferenceIfExists(UserType.class, patch.getUserTypeId()));
        }
        if (patch.getObjectName() != null) {
            userGrant.setObjectName(patch.getObjectName());
        }
        if (patch.getUserPermission() != null) {
            userGrant.setUserPermission(patch.getUserPermission());
        }
        if (patch.getGrantedById() != null) {
            userGrant.setGrantedBy(repositoryHelper.getReferenceIfExists(PortalUser.class, patch.getGrantedById()));
        }
    }

    public void patchEntity(LanguagePatchDTO patch, Language language) {
        if (patch.getName() != null) {
            language.setName(patch.getName());
        }
    }

    public void patchEntity(MovieCompanyPatchDTO patch, MovieCompany movieCompany) {
        if (patch.getCompanyDetailsId() != null) {
            movieCompany.setCompanyDetails(repositoryHelper
                    .getReferenceIfExists(CompanyDetails.class, patch.getCompanyDetailsId()));
        }
        if (patch.getMovieProductionType() != null) {
            movieCompany.setMovieProductionType(patch.getMovieProductionType());
        }
        if (patch.getDescription() != null) {
            movieCompany.setDescription(patch.getDescription());
        }
    }

    public void patchEntity(MovieReviewCompliantPatchDTO patch, MovieReviewCompliant movieReviewCompliant) {
        if (patch.getPortalUserId() != null) {
            movieReviewCompliant.setPortalUser(repositoryHelper
                    .getReferenceIfExists(PortalUser.class, patch.getPortalUserId()));
        }
        if (patch.getMovieId() != null) {
            movieReviewCompliant.setMovie(repositoryHelper.getReferenceIfExists(Movie.class, patch.getMovieId()));
        }
        if (patch.getMovieReviewId() != null) {
            movieReviewCompliant.setMovieReview(repositoryHelper
                    .getReferenceIfExists(MovieReview.class, patch.getMovieReviewId()));
        }
        if (patch.getDescription() != null) {
            movieReviewCompliant.setDescription(patch.getDescription());
        }
        if (patch.getModeratedStatus() != null) {
            movieReviewCompliant.setModeratedStatus(patch.getModeratedStatus());
        }
        if (patch.getModeratorId() != null) {
            movieReviewCompliant.setModerator(repositoryHelper
                    .getReferenceIfExists(PortalUser.class, patch.getModeratorId()));
        }
    }

    public void patchEntity(MovieReviewFeedbackPatchDTO patch, MovieReviewFeedback movieReviewFeedback) {
        if (patch.getMovieId() != null) {
            movieReviewFeedback.setMovie(repositoryHelper.getReferenceIfExists(Movie.class, patch.getMovieId()));
        }
        if (patch.getMovieReviewId() != null) {
            movieReviewFeedback.setMovieReview(repositoryHelper
                    .getReferenceIfExists(MovieReview.class, patch.getMovieReviewId()));
        }
        if (patch.getPortalUserId() != null) {
            movieReviewFeedback.setPortalUser(repositoryHelper
                    .getReferenceIfExists(PortalUser.class, patch.getPortalUserId()));
        }
        if (patch.getIsLiked() != null) {
            movieReviewFeedback.setIsLiked(patch.getIsLiked());
        }
    }

    public void patchEntity(MovieReviewPatchDTO patch, MovieReview movieReview) {
        if (patch.getPortalUserId() != null) {
            movieReview.setPortalUser(repositoryHelper.getReferenceIfExists(PortalUser.class, patch.getPortalUserId()));
        }
        if (patch.getMovieId() != null) {
            movieReview.setMovie(repositoryHelper.getReferenceIfExists(Movie.class, patch.getMovieId()));
        }
        if (patch.getTextReview() != null) {
            movieReview.setTextReview(patch.getTextReview());
        }
        if (patch.getModeratedStatus() != null) {
            movieReview.setModeratedStatus(patch.getModeratedStatus());
        }
        if (patch.getModeratorId() != null) {
            movieReview.setModerator(repositoryHelper.getReferenceIfExists(PortalUser.class, patch.getModeratorId()));
        }
    }

    public void patchEntity(MoviePatchDTO patch, Movie movie) {
        if (patch.getTitle() != null) {
            movie.setTitle(patch.getTitle());
        }
        if (patch.getYear() != null) {
            movie.setYear(patch.getYear());
        }
        if (patch.getDescription() != null) {
            movie.setDescription(patch.getDescription());
        }
        if (patch.getSoundMix() != null) {
            movie.setSoundMix(patch.getSoundMix());
        }
        if (patch.getColour() != null) {
            movie.setColour(patch.getColour());
        }
        if (patch.getAspectRatio() != null) {
            movie.setAspectRatio(patch.getAspectRatio());
        }
        if (patch.getCamera() != null) {
            movie.setCamera(patch.getCamera());
        }
        if (patch.getLaboratory() != null) {
            movie.setLaboratory(patch.getLaboratory());
        }
        if (patch.getCritique() != null) {
            movie.setCritique(patch.getCritique());
        }
        if (patch.getIsPublished() != null) {
            movie.setIsPublished(patch.getIsPublished());
        }
    }

    public void patchEntity(MovieSpoilerDataPatchDTO patch, MovieSpoilerData movieSpoilerData) {
        if (patch.getMovieReviewId() != null) {
            movieSpoilerData.setMovieReview(repositoryHelper
                    .getReferenceIfExists(MovieReview.class, patch.getMovieReviewId()));
        }
        if (patch.getStartIndex() != null) {
            movieSpoilerData.setStartIndex(patch.getStartIndex());
        }
        if (patch.getEndIndex() != null) {
            movieSpoilerData.setEndIndex(patch.getEndIndex());
        }
    }

    public void patchEntity(MovieVotePatchDTO patch, MovieVote movieVote) {
        if (patch.getPortalUserId() != null) {
            movieVote.setPortalUser(repositoryHelper.getReferenceIfExists(PortalUser.class, patch.getPortalUserId()));
        }
        if (patch.getMovieId() != null) {
            movieVote.setMovie(repositoryHelper.getReferenceIfExists(Movie.class, patch.getMovieId()));
        }
        if (patch.getRating() != null) {
            movieVote.setRating(patch.getRating());
        }
    }

    public void patchEntity(NewsPatchDTO patch, News news) {
        if (patch.getPublisherId() != null) {
            news.setPublisher(repositoryHelper.getReferenceIfExists(PortalUser.class, patch.getPublisherId()));
        }
        if (patch.getPublished() != null) {
            news.setPublished(patch.getPublished());
        }
        if (patch.getTopic() != null) {
            news.setTopic(patch.getTopic());
        }
        if (patch.getDescription() != null) {
            news.setDescription(patch.getDescription());
        }
    }

    public void patchEntity(PersonPatchDTO patch, Person person) {
        if (patch.getName() != null) {
            person.setName(patch.getName());
        }
        if (patch.getMiddleName() != null) {
            person.setMiddleName(patch.getMiddleName());
        }
        if (patch.getSurname() != null) {
            person.setSurname(patch.getSurname());
        }
    }

    public void patchEntity(PortalUserPatchDTO patch, PortalUser portalUser) {
        if (patch.getLogin() != null) {
            portalUser.setLogin(patch.getLogin());
        }
        if (patch.getSurname() != null) {
            portalUser.setSurname(patch.getSurname());
        }
        if (patch.getName() != null) {
            portalUser.setName(patch.getName());
        }
        if (patch.getMiddleName() != null) {
            portalUser.setMiddleName(patch.getMiddleName());
        }
        if (patch.getUserTypeId() != null) {
            portalUser.setUserType(repositoryHelper.getReferenceIfExists(UserType.class, patch.getUserTypeId()));
        }
        if (patch.getUserConfidence() != null) {
            portalUser.setUserConfidence(patch.getUserConfidence());
        }
    }

    public void patchEntity(ReleaseDetailPatchDTO patch, ReleaseDetail releaseDetail) {
        if (patch.getMovieId() != null) {
            releaseDetail.setMovie(repositoryHelper.getReferenceIfExists(Movie.class, patch.getMovieId()));
        }
        if (patch.getReleaseDate() != null) {
            releaseDetail.setReleaseDate(patch.getReleaseDate());
        }
        if (patch.getCountryId() != null) {
            releaseDetail.setCountry(repositoryHelper.getReferenceIfExists(Country.class, patch.getCountryId()));
        }
    }

    public void patchEntity(RoleReviewCompliantPatchDTO patch, RoleReviewCompliant roleReviewCompliant) {
        if (patch.getPortalUserId() != null) {
            roleReviewCompliant.setPortalUser(repositoryHelper.getReferenceIfExists(PortalUser.class,
                    patch.getPortalUserId()));
        }
        if (patch.getRoleId() != null) {
            roleReviewCompliant.setRole(repositoryHelper.getReferenceIfExists(Role.class, patch.getRoleId()));
        }
        if (patch.getRoleReviewId() != null) {
            roleReviewCompliant.setRoleReview(repositoryHelper
                    .getReferenceIfExists(RoleReview.class, patch.getRoleReviewId()));
        }
        if (patch.getDescription() != null) {
            roleReviewCompliant.setDescription(patch.getDescription());
        }
        if (patch.getModeratedStatus() != null) {
            roleReviewCompliant.setModeratedStatus(patch.getModeratedStatus());
        }
        if (patch.getModeratorId() != null) {
            roleReviewCompliant.setModerator(repositoryHelper
                    .getReferenceIfExists(PortalUser.class, patch.getModeratorId()));
        }
    }

    public void patchEntity(RoleReviewFeedbackPatchDTO patch, RoleReviewFeedback roleReviewFeedback) {
        if (patch.getPortalUserId() != null) {
            roleReviewFeedback.setPortalUser(repositoryHelper.getReferenceIfExists(PortalUser.class,
                    patch.getPortalUserId()));
        }
        if (patch.getRoleId() != null) {
            roleReviewFeedback.setRole(repositoryHelper.getReferenceIfExists(Role.class, patch.getRoleId()));
        }
        if (patch.getRoleReviewId() != null) {
            roleReviewFeedback.setRoleReview(repositoryHelper
                    .getReferenceIfExists(RoleReview.class, patch.getRoleReviewId()));
        }
        if (patch.getIsLiked() != null) {
            roleReviewFeedback.setIsLiked(patch.getIsLiked());
        }
    }

    public void patchEntity(RoleReviewPatchDTO patch, RoleReview roleReview) {
        if (patch.getPortalUserId() != null) {
            roleReview.setPortalUser(repositoryHelper.getReferenceIfExists(PortalUser.class, patch.getPortalUserId()));
        }
        if (patch.getRoleId() != null) {
            roleReview.setRole(repositoryHelper.getReferenceIfExists(Role.class, patch.getRoleId()));
        }
        if (patch.getTextReview() != null) {
            roleReview.setTextReview(patch.getTextReview());
        }
        if (patch.getModeratedStatus() != null) {
            roleReview.setModeratedStatus(patch.getModeratedStatus());
        }
        if (patch.getModeratorId() != null) {
            roleReview.setModerator(repositoryHelper.getReferenceIfExists(PortalUser.class, patch.getModeratorId()));
        }
    }

    public void patchEntity(RolePatchDTO patch, Role role) {
        if (patch.getTitle() != null) {
            role.setTitle(patch.getTitle());
        }
        if (patch.getRoleType() != null) {
            role.setRoleType(patch.getRoleType());
        }
        if (patch.getDescription() != null) {
            role.setDescription(patch.getDescription());
        }
        if (patch.getPersonId() != null) {
            role.setPerson(repositoryHelper.getReferenceIfExists(Person.class, patch.getPersonId()));
        }
        if (patch.getMovieId() != null) {
            role.setMovie(repositoryHelper.getReferenceIfExists(Movie.class, patch.getMovieId()));
        }
    }

    public void patchEntity(RoleSpoilerDataPatchDTO patch, RoleSpoilerData roleSpoilerData) {
        if (patch.getRoleReviewId() != null) {
            roleSpoilerData.setRoleReview(repositoryHelper
                    .getReferenceIfExists(RoleReview.class, patch.getRoleReviewId()));
        }
        if (patch.getStartIndex() != null) {
            roleSpoilerData.setStartIndex(patch.getStartIndex());
        }
        if (patch.getEndIndex() != null) {
            roleSpoilerData.setEndIndex(patch.getEndIndex());
        }
    }

    public void patchEntity(RoleVotePatchDTO patch, RoleVote roleVote) {
        if (patch.getPortalUserId() != null) {
            roleVote.setPortalUser(repositoryHelper.getReferenceIfExists(PortalUser.class, patch.getPortalUserId()));
        }
        if (patch.getRoleId() != null) {
            roleVote.setRole(repositoryHelper.getReferenceIfExists(Role.class, patch.getRoleId()));
        }
        if (patch.getRating() != null) {
            roleVote.setRating(patch.getRating());
        }
    }

    public void patchEntity(UserTypePatchDTO patch, UserType userType) {
        if (patch.getUserGroup() != null) {
            userType.setUserGroup(patch.getUserGroup());
        }
    }

    public void updateEntity(CompanyDetailsPutDTO put, CompanyDetails companyDetails) {
        companyDetails.setName(put.getName());
        companyDetails.setOverview(put.getOverview());
        companyDetails.setYearOfFoundation(put.getYearOfFoundation());
    }

    public void updateEntity(VisitPutDTO put, Visit visit) {
        if (put.getPortalUserId() != null) {
            visit.setPortalUser(repositoryHelper.getReferenceIfExists(PortalUser.class, put.getPortalUserId()));
        }
        visit.setStartAt(put.getStartAt());
        visit.setFinishAt(put.getFinishAt());
        visit.setStatus(put.getStatus());
    }

    public void updateEntity(CrewPutDTO put, Crew crew) {
        if (put.getMovieId() != null) {
            crew.setMovie(repositoryHelper.getReferenceIfExists(Movie.class, put.getMovieId()));
        }
        if (put.getPersonId() != null) {
            crew.setPerson(repositoryHelper.getReferenceIfExists(Person.class, put.getPersonId()));
        }
        if (put.getCrewTypeId() != null) {
            crew.setCrewType(repositoryHelper.getReferenceIfExists(CrewType.class, put.getCrewTypeId()));
        } else {
            crew.setCrewType(null);
        }
        crew.setDescription(put.getDescription());
    }

    public void updateEntity(CountryPutDTO put, Country country) {
        country.setName(put.getName());
    }

    public void updateEntity(CrewTypePutDTO put, CrewType crewType) {
        crewType.setName(put.getName());
    }

    public void updateEntity(GenrePutDTO put, Genre genre) {
        if (put.getMovieId() != null) {
            genre.setMovie(repositoryHelper.getReferenceIfExists(Movie.class, put.getMovieId()));
        }
        genre.setName(put.getName());
    }

    public void updateEntity(UserGrantPutDTO put, UserGrant userGrant) {
        if (put.getUserTypeId() != null) {
            userGrant.setUserType(repositoryHelper.getReferenceIfExists(UserType.class, put.getUserTypeId()));
        }
        userGrant.setObjectName(put.getObjectName());
        userGrant.setUserPermission(put.getUserPermission());
        if (put.getGrantedById() != null) {
            userGrant.setGrantedBy(repositoryHelper.getReferenceIfExists(PortalUser.class, put.getGrantedById()));
        }
    }

    public void updateEntity(LanguagePutDTO put, Language language) {
        language.setName(put.getName());
    }

    public void updateEntity(MovieCompanyPutDTO put, MovieCompany movieCompany) {
        if (put.getCompanyDetailsId() != null) {
            movieCompany.setCompanyDetails(repositoryHelper
                    .getReferenceIfExists(CompanyDetails.class, put.getCompanyDetailsId()));
        }
        movieCompany.setMovieProductionType(put.getMovieProductionType());
        movieCompany.setDescription(put.getDescription());
    }

    public void updateEntity(MovieReviewCompliantPutDTO put, MovieReviewCompliant movieReviewCompliant) {
        if (put.getPortalUserId() != null) {
            movieReviewCompliant.setPortalUser(repositoryHelper
                    .getReferenceIfExists(PortalUser.class, put.getPortalUserId()));
        }
        if (put.getMovieId() != null) {
            movieReviewCompliant.setMovie(repositoryHelper.getReferenceIfExists(Movie.class, put.getMovieId()));
        }
        if (put.getMovieReviewId() != null) {
            movieReviewCompliant.setMovieReview(repositoryHelper
                    .getReferenceIfExists(MovieReview.class, put.getMovieReviewId()));
        }
        movieReviewCompliant.setDescription(put.getDescription());
        movieReviewCompliant.setModeratedStatus(put.getModeratedStatus());
        if (put.getModeratorId() != null) {
            movieReviewCompliant.setModerator(repositoryHelper
                    .getReferenceIfExists(PortalUser.class, put.getModeratorId()));
        } else {
            movieReviewCompliant.setModerator(null);
        }
    }

    public void updateEntity(MovieReviewFeedbackPutDTO put, MovieReviewFeedback movieReviewFeedback) {
        if (put.getMovieId() != null) {
            movieReviewFeedback.setMovie(repositoryHelper.getReferenceIfExists(Movie.class, put.getMovieId()));
        }
        if (put.getMovieReviewId() != null) {
            movieReviewFeedback.setMovieReview(repositoryHelper
                    .getReferenceIfExists(MovieReview.class, put.getMovieReviewId()));
        }
        if (put.getPortalUserId() != null) {
            movieReviewFeedback.setPortalUser(repositoryHelper
                    .getReferenceIfExists(PortalUser.class, put.getPortalUserId()));
        }
        movieReviewFeedback.setIsLiked(put.getIsLiked());
    }

    public void updateEntity(MovieReviewPutDTO put, MovieReview movieReview) {
        if (put.getPortalUserId() != null) {
            movieReview.setPortalUser(repositoryHelper.getReferenceIfExists(PortalUser.class, put.getPortalUserId()));
        }
        if (put.getMovieId() != null) {
            movieReview.setMovie(repositoryHelper.getReferenceIfExists(Movie.class, put.getMovieId()));
        }
        movieReview.setTextReview(put.getTextReview());
        movieReview.setModeratedStatus(put.getModeratedStatus());
        if (put.getModeratorId() != null) {
            movieReview.setModerator(repositoryHelper.getReferenceIfExists(PortalUser.class, put.getModeratorId()));
        } else {
            movieReview.setModerator(null);
        }
    }

    public void updateEntity(MoviePutDTO put, Movie movie) {
        movie.setTitle(put.getTitle());
        movie.setYear(put.getYear());
        movie.setDescription(put.getDescription());
        movie.setSoundMix(put.getSoundMix());
        movie.setColour(put.getColour());
        movie.setAspectRatio(put.getAspectRatio());
        movie.setCamera(put.getCamera());
        movie.setLaboratory(put.getLaboratory());
        movie.setCritique(put.getCritique());
        movie.setIsPublished(put.getIsPublished());
    }

    public void updateEntity(MovieSpoilerDataPutDTO put, MovieSpoilerData movieSpoilerData) {
        if (put.getMovieReviewId() != null) {
            movieSpoilerData.setMovieReview(repositoryHelper
                    .getReferenceIfExists(MovieReview.class, put.getMovieReviewId()));
        }
        movieSpoilerData.setStartIndex(put.getStartIndex());
        movieSpoilerData.setEndIndex(put.getEndIndex());
    }

    public void updateEntity(MovieVotePutDTO put, MovieVote movieVote) {
        if (put.getPortalUserId() != null) {
            movieVote.setPortalUser(repositoryHelper.getReferenceIfExists(PortalUser.class, put.getPortalUserId()));
        }
        if (put.getMovieId() != null) {
            movieVote.setMovie(repositoryHelper.getReferenceIfExists(Movie.class, put.getMovieId()));
        }
        movieVote.setRating(put.getRating());
    }

    public void updateEntity(NewsPutDTO put, News news) {
        if (put.getPublisherId() != null) {
            news.setPublisher(repositoryHelper.getReferenceIfExists(PortalUser.class, put.getPublisherId()));
        }
        news.setPublished(put.getPublished());
        news.setTopic(put.getTopic());
        news.setDescription(put.getDescription());
    }

    public void updateEntity(PersonPutDTO put, Person person) {
        person.setName(put.getName());
        person.setMiddleName(put.getMiddleName());
        person.setSurname(put.getSurname());
    }

    public void updateEntity(PortalUserPutDTO put, PortalUser portalUser) {
        portalUser.setLogin(put.getLogin());
        portalUser.setSurname(put.getSurname());
        portalUser.setName(put.getName());
        portalUser.setMiddleName(put.getMiddleName());
        if (put.getUserTypeId() != null) {
            portalUser.setUserType(repositoryHelper.getReferenceIfExists(UserType.class, put.getUserTypeId()));
        }
        portalUser.setUserConfidence(put.getUserConfidence());
    }

    public void updateEntity(ReleaseDetailPutDTO put, ReleaseDetail releaseDetail) {
        if (put.getMovieId() != null) {
            releaseDetail.setMovie(repositoryHelper.getReferenceIfExists(Movie.class, put.getMovieId()));
        }
        releaseDetail.setReleaseDate(put.getReleaseDate());
        if (put.getCountryId() != null) {
            releaseDetail.setCountry(repositoryHelper.getReferenceIfExists(Country.class, put.getCountryId()));
        }
    }

    public void updateEntity(RoleReviewCompliantPutDTO put, RoleReviewCompliant roleReviewCompliant) {
        if (put.getPortalUserId() != null) {
            roleReviewCompliant.setPortalUser(repositoryHelper.getReferenceIfExists(PortalUser.class,
                    put.getPortalUserId()));
        }
        if (put.getRoleId() != null) {
            roleReviewCompliant.setRole(repositoryHelper.getReferenceIfExists(Role.class, put.getRoleId()));
        }
        if (put.getRoleReviewId() != null) {
            roleReviewCompliant.setRoleReview(repositoryHelper
                    .getReferenceIfExists(RoleReview.class, put.getRoleReviewId()));
        }
        roleReviewCompliant.setDescription(put.getDescription());
        roleReviewCompliant.setModeratedStatus(put.getModeratedStatus());
        if (put.getModeratorId() != null) {
            roleReviewCompliant.setModerator(repositoryHelper
                    .getReferenceIfExists(PortalUser.class, put.getModeratorId()));
        } else {
            roleReviewCompliant.setModerator(null);
        }
    }

    public void updateEntity(RoleReviewFeedbackPutDTO put, RoleReviewFeedback roleReviewFeedback) {
        if (put.getPortalUserId() != null) {
            roleReviewFeedback.setPortalUser(repositoryHelper.getReferenceIfExists(PortalUser.class,
                    put.getPortalUserId()));
        }
        if (put.getRoleId() != null) {
            roleReviewFeedback.setRole(repositoryHelper.getReferenceIfExists(Role.class, put.getRoleId()));
        }
        if (put.getRoleReviewId() != null) {
            roleReviewFeedback.setRoleReview(repositoryHelper
                    .getReferenceIfExists(RoleReview.class, put.getRoleReviewId()));
        }
        roleReviewFeedback.setIsLiked(put.getIsLiked());
    }

    public void updateEntity(RoleReviewPutDTO put, RoleReview roleReview) {
        if (put.getPortalUserId() != null) {
            roleReview.setPortalUser(repositoryHelper.getReferenceIfExists(PortalUser.class, put.getPortalUserId()));
        }
        if (put.getRoleId() != null) {
            roleReview.setRole(repositoryHelper.getReferenceIfExists(Role.class, put.getRoleId()));
        }
        roleReview.setTextReview(put.getTextReview());
        roleReview.setModeratedStatus(put.getModeratedStatus());
        if (put.getModeratorId() != null) {
            roleReview.setModerator(repositoryHelper.getReferenceIfExists(PortalUser.class, put.getModeratorId()));
        } else {
            roleReview.setModerator(null);
        }
    }

    public void updateEntity(RolePutDTO put, Role role) {
        role.setTitle(put.getTitle());
        role.setRoleType(put.getRoleType());
        role.setDescription(put.getDescription());
        if (put.getPersonId() != null) {
            role.setPerson(repositoryHelper.getReferenceIfExists(Person.class, put.getPersonId()));
        } else {
            role.setPerson(null);
        }
        if (put.getMovieId() != null) {
            role.setMovie(repositoryHelper.getReferenceIfExists(Movie.class, put.getMovieId()));
        }
    }

    public void updateEntity(RoleSpoilerDataPutDTO put, RoleSpoilerData roleSpoilerData) {
        if (put.getRoleReviewId() != null) {
            roleSpoilerData.setRoleReview(repositoryHelper
                    .getReferenceIfExists(RoleReview.class, put.getRoleReviewId()));
        }
        roleSpoilerData.setStartIndex(put.getStartIndex());
        roleSpoilerData.setEndIndex(put.getEndIndex());
    }

    public void updateEntity(RoleVotePutDTO put, RoleVote roleVote) {
        if (put.getPortalUserId() != null) {
            roleVote.setPortalUser(repositoryHelper.getReferenceIfExists(PortalUser.class, put.getPortalUserId()));
        }
        if (put.getRoleId() != null) {
            roleVote.setRole(repositoryHelper.getReferenceIfExists(Role.class, put.getRoleId()));
        }
        roleVote.setRating(put.getRating());
    }

    public void updateEntity(UserTypePutDTO put, UserType userType) {
        userType.setUserGroup(put.getUserGroup());
    }
}
