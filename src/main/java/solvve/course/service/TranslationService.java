package solvve.course.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import solvve.course.domain.*;
import solvve.course.dto.*;
import solvve.course.repository.*;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TranslationService {

    @Autowired
    private RepositoryHelper repositoryHelper;

    public VisitReadExtendedDTO toReadExtended(Visit visit) {
        VisitReadExtendedDTO dto = new VisitReadExtendedDTO();
        dto.setId(visit.getId());
        dto.setUserId(toRead(visit.getUserId()));
        dto.setStartAt(visit.getStartAt());
        dto.setFinishAt(visit.getFinishAt());
        dto.setStatus(visit.getStatus());
        dto.setCreatedAt(visit.getCreatedAt());
        dto.setUpdatedAt(visit.getUpdatedAt());
        return dto;
    }

    public VisitReadDTO toRead(Visit visit) {
        VisitReadDTO dto = new VisitReadDTO();
        dto.setId(visit.getId());
        dto.setUserId(visit.getUserId().getId());
        dto.setStartAt(visit.getStartAt());
        dto.setFinishAt(visit.getFinishAt());
        dto.setStatus(visit.getStatus());
        dto.setCreatedAt(visit.getCreatedAt());
        dto.setUpdatedAt(visit.getUpdatedAt());
        return dto;
    }

    public Set<VisitReadDTO> toRead(Set<Visit> visitSet) {
        return visitSet.stream().map(visit -> {
            VisitReadDTO visitReadDTO = new VisitReadDTO();
            visitReadDTO.setUserId(visit.getUserId().getId());
            visitReadDTO.setStatus(visit.getStatus());
            visitReadDTO.setStartAt(visit.getStartAt());
            visitReadDTO.setFinishAt(visit.getFinishAt());
            visitReadDTO.setCreatedAt(visit.getCreatedAt());
            visitReadDTO.setUpdatedAt(visit.getUpdatedAt());
            return visitReadDTO;
        }).collect(Collectors.toSet());
    }

    public Visit toEntity(VisitCreateDTO create) {
        Visit visit = new Visit();
        visit.setUserId(repositoryHelper.getReferenceIfExists(PortalUser.class, create.getUserId()));
        visit.setStartAt(create.getStartAt());
        visit.setFinishAt(create.getFinishAt());
        visit.setStatus(create.getStatus());
        return visit;
    }

    public void patchEntity(VisitPatchDTO patch, Visit visit) {
        if (patch.getUserId()!=null) {
            visit.setUserId(repositoryHelper.getReferenceIfExists(PortalUser.class, patch.getUserId()));
        }
        if (patch.getStartAt()!=null) {
            visit.setStartAt(patch.getStartAt());
        }
        if (patch.getFinishAt()!=null) {
            visit.setFinishAt(patch.getFinishAt());
        }
        if (patch.getStatus()!=null) {
            visit.setStatus(patch.getStatus());
        }
    }

    public void updateEntity(VisitPutDTO put, Visit visit) {
        if (put.getUserId()!=null) {
            visit.setUserId(repositoryHelper.getReferenceIfExists(PortalUser.class, put.getUserId()));
        }
        visit.setStartAt(put.getStartAt());
        visit.setFinishAt(put.getFinishAt());
        visit.setStatus(put.getStatus());
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

    public CompanyDetails toEntity(CompanyDetailsCreateDTO create) {
        CompanyDetails companyDetails = new CompanyDetails();
        companyDetails.setName(create.getName());
        companyDetails.setOverview(create.getOverview());
        companyDetails.setYearOfFoundation(create.getYearOfFoundation());
        return companyDetails;
    }

    public void patchEntity(CompanyDetailsPatchDTO patch, CompanyDetails companyDetails) {
        if (patch.getName()!=null) {
            companyDetails.setName(patch.getName());
        }
        if (patch.getOverview()!=null) {
            companyDetails.setOverview(patch.getOverview());
        }
        if (patch.getYearOfFoundation()!=null) {
            companyDetails.setYearOfFoundation(patch.getYearOfFoundation());
        }
    }

    public void updateEntity(CompanyDetailsPutDTO put, CompanyDetails companyDetails) {
        companyDetails.setName(put.getName());
        companyDetails.setOverview(put.getOverview());
        companyDetails.setYearOfFoundation(put.getYearOfFoundation());
    }

    public CountryReadDTO toRead(Country country) {
        CountryReadDTO dto = new CountryReadDTO();
        dto.setId(country.getId());
        dto.setName(country.getName());
        dto.setCreatedAt(country.getCreatedAt());
        dto.setUpdatedAt(country.getUpdatedAt());
        return dto;
    }

    public Country toEntity(CountryCreateDTO create) {
        Country country = new Country();
        country.setName(create.getName());
        return country;
    }

    public void patchEntity(CountryPatchDTO patch, Country country) {
        if (patch.getName()!=null) {
            country.setName(patch.getName());
        }
    }

    public void updateEntity(CountryPutDTO put, Country country) {
        country.setName(put.getName());
    }

    public CrewReadExtendedDTO toReadExtended(Crew crew) {
        CrewReadExtendedDTO dto = new CrewReadExtendedDTO();
        dto.setId(crew.getId());
        dto.setMovieId(toRead(crew.getMovieId()));
        dto.setPersonId(toRead(crew.getPersonId()));
        dto.setCrewTypeId(toRead(crew.getCrewTypeId()));
        dto.setDescription(crew.getDescription());
        dto.setCreatedAt(crew.getCreatedAt());
        dto.setUpdatedAt(crew.getUpdatedAt());
        return dto;
    }

    public CrewReadDTO toRead(Crew crew) {
        CrewReadDTO dto = new CrewReadDTO();
        dto.setId(crew.getId());
        dto.setMovieId(crew.getMovieId().getId());
        dto.setPersonId(crew.getPersonId().getId());
        if (crew.getCrewTypeId() != null) {
            dto.setCrewTypeId(crew.getCrewTypeId().getId());
        }
        dto.setDescription(crew.getDescription());
        dto.setCreatedAt(crew.getCreatedAt());
        dto.setUpdatedAt(crew.getUpdatedAt());
        return dto;
    }

    public Crew toEntity(CrewCreateDTO create) {
        Crew crew = new Crew();
        crew.setMovieId(repositoryHelper.getReferenceIfExists(Movie.class, create.getMovieId()));
        crew.setPersonId(repositoryHelper.getReferenceIfExists(Person.class, create.getPersonId()));
        crew.setCrewTypeId(repositoryHelper.getReferenceIfExists(CrewType.class, create.getCrewTypeId()));
        crew.setDescription(create.getDescription());
        return crew;
    }

    public void patchEntity(CrewPatchDTO patch, Crew crew) {
        if (patch.getMovieId()!=null) {
            crew.setMovieId(repositoryHelper.getReferenceIfExists(Movie.class, patch.getMovieId()));
        }
        if (patch.getPersonId()!=null) {
            crew.setPersonId(repositoryHelper.getReferenceIfExists(Person.class, patch.getPersonId()));
        }
        if (patch.getCrewTypeId()!=null) {
            crew.setCrewTypeId(repositoryHelper.getReferenceIfExists(CrewType.class, patch.getCrewTypeId()));
        }
        if (patch.getDescription()!=null) {
            crew.setDescription(patch.getDescription());
        }
    }

    public void updateEntity(CrewPutDTO put, Crew crew) {
        if (put.getMovieId()!=null) {
            crew.setMovieId(repositoryHelper.getReferenceIfExists(Movie.class, put.getMovieId()));
        }
        if (put.getPersonId()!=null) {
            crew.setPersonId(repositoryHelper.getReferenceIfExists(Person.class, put.getPersonId()));
        }
        if (put.getCrewTypeId()!=null) {
            crew.setCrewTypeId(repositoryHelper.getReferenceIfExists(CrewType.class, put.getCrewTypeId()));
        } else {
            crew.setCrewTypeId(new CrewType());
        }
        crew.setDescription(put.getDescription());
    }

    public CrewTypeReadDTO toRead(CrewType crewType) {
        CrewTypeReadDTO dto = new CrewTypeReadDTO();
        dto.setId(crewType.getId());
        dto.setName(crewType.getName());
        dto.setCreatedAt(crewType.getCreatedAt());
        dto.setUpdatedAt(crewType.getUpdatedAt());
        return dto;
    }

    public CrewType toEntity(CrewTypeCreateDTO create) {
        CrewType crewType = new CrewType();
        crewType.setName(create.getName());
        return crewType;
    }

    public void patchEntity(CrewTypePatchDTO patch, CrewType crewType) {
        if (patch.getName()!=null) {
            crewType.setName(patch.getName());
        }
    }

    public void updateEntity(CrewTypePutDTO put, CrewType crewType) {
        crewType.setName(put.getName());
    }

    public GenreReadDTO toRead(Genre genre) {
        GenreReadDTO dto = new GenreReadDTO();
        dto.setId(genre.getId());
        dto.setMovieId(genre.getMovieId().getId());
        dto.setName(genre.getName());
        dto.setCreatedAt(genre.getCreatedAt());
        dto.setUpdatedAt(genre.getUpdatedAt());
        return dto;
    }

    public Genre toEntity(GenreCreateDTO create) {
        Genre genre = new Genre();
        genre.setMovieId(repositoryHelper.getReferenceIfExists(Movie.class, create.getMovieId()));
        genre.setName(create.getName());
        return genre;
    }

    public void patchEntity(GenrePatchDTO patch, Genre genre) {
        if (patch.getMovieId()!=null) {
            genre.setMovieId(repositoryHelper.getReferenceIfExists(Movie.class, patch.getMovieId()));
        }
        if (patch.getName()!=null) {
            genre.setName(patch.getName());
        }
    }

    public void updateEntity(GenrePutDTO put, Genre Genre) {
        if (put.getMovieId()!=null) {
            Genre.setMovieId(repositoryHelper.getReferenceIfExists(Movie.class, put.getMovieId()));
        } else {
            Genre.setMovieId(new Movie());
        }
        Genre.setName(put.getName());
    }

    public UserGrantReadDTO toRead(UserGrant userGrant) {
        UserGrantReadDTO dto = new UserGrantReadDTO();
        dto.setId(userGrant.getId());
        dto.setUserTypeId(userGrant.getUserTypeId().getId());
        dto.setUserPermission(userGrant.getUserPermission());
        dto.setObjectName(userGrant.getObjectName());
        dto.setGrantedBy(userGrant.getGrantedBy().getId());
        dto.setCreatedAt(userGrant.getCreatedAt());
        dto.setUpdatedAt(userGrant.getUpdatedAt());
        return dto;
    }

    public UserGrant toEntity(UserGrantCreateDTO create) {
        UserGrant userGrant = new UserGrant();
        userGrant.setUserTypeId(repositoryHelper.getReferenceIfExists(UserType.class, create.getUserTypeId()));
        userGrant.setUserPermission(create.getUserPermission());
        userGrant.setObjectName(create.getObjectName());
        userGrant.setGrantedBy(repositoryHelper.getReferenceIfExists(PortalUser.class, create.getGrantedBy()));
        return userGrant;
    }

    public void patchEntity(UserGrantPatchDTO patch, UserGrant userGrant) {
        if (patch.getUserTypeId()!=null) {
            userGrant.setUserTypeId(repositoryHelper.getReferenceIfExists(UserType.class, patch.getUserTypeId()));
        }
        if (patch.getObjectName()!=null) {
            userGrant.setObjectName(patch.getObjectName());
        }
        if (patch.getUserPermission()!=null) {
            userGrant.setUserPermission(patch.getUserPermission());
        }
        if (patch.getGrantedBy()!=null) {
            userGrant.setGrantedBy(repositoryHelper.getReferenceIfExists(PortalUser.class, patch.getGrantedBy()));
        }
    }

    public void updateEntity(UserGrantPutDTO put, UserGrant userGrant) {
        if (put.getUserTypeId()!=null) {
            userGrant.setUserTypeId(repositoryHelper.getReferenceIfExists(UserType.class, put.getUserTypeId()));
        }
        userGrant.setObjectName(put.getObjectName());
        userGrant.setUserPermission(put.getUserPermission());
        if (put.getGrantedBy()!=null) {
            userGrant.setGrantedBy(repositoryHelper.getReferenceIfExists(PortalUser.class, put.getGrantedBy()));
        }
    }

    public LanguageReadDTO toRead(Language language) {
        LanguageReadDTO dto = new LanguageReadDTO();
        dto.setId(language.getId());
        dto.setName(language.getName());
        dto.setCreatedAt(language.getCreatedAt());
        dto.setUpdatedAt(language.getUpdatedAt());
        return dto;
    }

    public Language toEntity(LanguageCreateDTO create) {
        Language language = new Language();
        language.setName(create.getName());
        return language;
    }

    public void patchEntity(LanguagePatchDTO patch, Language language) {
        if (patch.getName()!=null) {
            language.setName(patch.getName());
        }
    }

    public void updateEntity(LanguagePutDTO put, Language language) {
        language.setName(put.getName());
    }

    public MovieCompanyReadDTO toRead(MovieCompany movieCompany) {
        MovieCompanyReadDTO dto = new MovieCompanyReadDTO();
        dto.setId(movieCompany.getId());
        dto.setCompanyId(movieCompany.getCompanyId().getId());
        dto.setMovieProductionType(movieCompany.getMovieProductionType());
        dto.setDescription(movieCompany.getDescription());
        dto.setCreatedAt(movieCompany.getCreatedAt());
        dto.setUpdatedAt(movieCompany.getUpdatedAt());
        return dto;
    }

    public MovieCompany toEntity(MovieCompanyCreateDTO create) {
        MovieCompany movieCompany = new MovieCompany();
        movieCompany.setCompanyId(repositoryHelper.getReferenceIfExists(CompanyDetails.class, create.getCompanyId()));
        movieCompany.setMovieProductionType(create.getMovieProductionType());
        movieCompany.setDescription(create.getDescription());
        return movieCompany;
    }

    public void patchEntity(MovieCompanyPatchDTO patch, MovieCompany movieCompany) {
        if (patch.getCompanyId()!=null) {
            movieCompany.setCompanyId(repositoryHelper.getReferenceIfExists(CompanyDetails.class, patch.getCompanyId()));
        }
        if (patch.getMovieProductionType()!=null) {
            movieCompany.setMovieProductionType(patch.getMovieProductionType());
        }
        if (patch.getDescription()!=null) {
            movieCompany.setDescription(patch.getDescription());
        }
    }

    public void updateEntity(MovieCompanyPutDTO put, MovieCompany movieCompany) {
        if (put.getCompanyId()!=null) {
            movieCompany.setCompanyId(repositoryHelper.getReferenceIfExists(CompanyDetails.class, put.getCompanyId()));
        }
        movieCompany.setMovieProductionType(put.getMovieProductionType());
        movieCompany.setDescription(put.getDescription());
    }

    public MovieReviewCompliantReadDTO toRead(MovieReviewCompliant movieReviewCompliant) {
        MovieReviewCompliantReadDTO dto = new MovieReviewCompliantReadDTO();
        dto.setId(movieReviewCompliant.getId());
        dto.setUserId(movieReviewCompliant.getUserId().getId());
        dto.setMovieId(movieReviewCompliant.getMovieId().getId());
        dto.setMovieReviewId(movieReviewCompliant.getMovieReviewId().getId());
        dto.setDescription(movieReviewCompliant.getDescription());
        dto.setModeratedStatus(movieReviewCompliant.getModeratedStatus());
        dto.setModeratorId(movieReviewCompliant.getModeratorId().getId());
        dto.setCreatedAt(movieReviewCompliant.getCreatedAt());
        dto.setUpdatedAt(movieReviewCompliant.getUpdatedAt());
        return dto;
    }

    public MovieReviewCompliant toEntity(MovieReviewCompliantCreateDTO create) {
        MovieReviewCompliant movieReviewCompliant = new MovieReviewCompliant();
        movieReviewCompliant.setUserId(repositoryHelper.getReferenceIfExists(PortalUser.class, create.getUserId()));
        movieReviewCompliant.setMovieId(repositoryHelper.getReferenceIfExists(Movie.class, create.getMovieId()));
        movieReviewCompliant.setMovieReviewId(repositoryHelper.getReferenceIfExists(MovieReview.class, create.getMovieReviewId()));
        movieReviewCompliant.setDescription(create.getDescription());
        movieReviewCompliant.setModeratedStatus(create.getModeratedStatus());
        movieReviewCompliant.setModeratorId(repositoryHelper.getReferenceIfExists(PortalUser.class, create.getModeratorId()));
        return movieReviewCompliant;
    }

    public void patchEntity(MovieReviewCompliantPatchDTO patch, MovieReviewCompliant movieReviewCompliant) {
        if (patch.getUserId()!=null) {
            movieReviewCompliant.setUserId(repositoryHelper.getReferenceIfExists(PortalUser.class, patch.getUserId()));
        }
        if (patch.getMovieId()!=null) {
            movieReviewCompliant.setMovieId(repositoryHelper.getReferenceIfExists(Movie.class, patch.getMovieId()));
        }
        if (patch.getMovieReviewId()!=null) {
            movieReviewCompliant.setMovieReviewId(repositoryHelper.getReferenceIfExists(MovieReview.class, patch.getMovieReviewId()));
        }
        if (patch.getDescription()!=null) {
            movieReviewCompliant.setDescription(patch.getDescription());
        }
        if (patch.getModeratedStatus()!=null) {
            movieReviewCompliant.setModeratedStatus(patch.getModeratedStatus());
        }
        if (patch.getModeratorId()!=null) {
            movieReviewCompliant.setModeratorId(repositoryHelper.getReferenceIfExists(PortalUser.class, patch.getModeratorId()));
        }
    }

    public void updateEntity(MovieReviewCompliantPutDTO put, MovieReviewCompliant movieReviewCompliant) {
        if (put.getUserId()!=null) {
            movieReviewCompliant.setUserId(repositoryHelper.getReferenceIfExists(PortalUser.class, put.getUserId()));
        } else {
            movieReviewCompliant.setUserId(new PortalUser());
        }
        if (put.getMovieId()!=null) {
            movieReviewCompliant.setMovieId(repositoryHelper.getReferenceIfExists(Movie.class, put.getMovieId()));
        } else {
            movieReviewCompliant.setMovieId(new Movie());
        }
        if (put.getMovieReviewId()!=null) {
            movieReviewCompliant.setMovieReviewId(repositoryHelper.getReferenceIfExists(MovieReview.class, put.getMovieReviewId()));
        } else {
            movieReviewCompliant.setMovieReviewId(new MovieReview());
        }
        movieReviewCompliant.setDescription(put.getDescription());
        movieReviewCompliant.setModeratedStatus(put.getModeratedStatus());
        if (put.getModeratorId()!=null) {
            movieReviewCompliant.setModeratorId(repositoryHelper.getReferenceIfExists(PortalUser.class, put.getModeratorId()));
        } else {
            movieReviewCompliant.setModeratorId(new PortalUser());
        }
    }

    public MovieReviewFeedbackReadDTO toRead(MovieReviewFeedback movieReviewFeedback) {
        MovieReviewFeedbackReadDTO dto = new MovieReviewFeedbackReadDTO();
        dto.setId(movieReviewFeedback.getId());
        dto.setUserId(movieReviewFeedback.getUserId().getId());
        dto.setMovieId(movieReviewFeedback.getMovieId().getId());
        dto.setMovieReviewId(movieReviewFeedback.getMovieReviewId().getId());
        dto.setIsLiked(movieReviewFeedback.getIsLiked());
        dto.setCreatedAt(movieReviewFeedback.getCreatedAt());
        dto.setUpdatedAt(movieReviewFeedback.getUpdatedAt());
        return dto;
    }

    public MovieReviewFeedback toEntity(MovieReviewFeedbackCreateDTO create) {
        MovieReviewFeedback movieReviewFeedback = new MovieReviewFeedback();
        movieReviewFeedback.setUserId(repositoryHelper.getReferenceIfExists(PortalUser.class, create.getUserId()));
        movieReviewFeedback.setMovieId(repositoryHelper.getReferenceIfExists(Movie.class, create.getMovieId()));
        movieReviewFeedback.setMovieReviewId(repositoryHelper.getReferenceIfExists(MovieReview.class, create.getMovieReviewId()));
        movieReviewFeedback.setIsLiked(create.getIsLiked());
        return movieReviewFeedback;
    }

    public void patchEntity(MovieReviewFeedbackPatchDTO patch, MovieReviewFeedback movieReviewFeedback) {
        if (patch.getMovieId()!=null) {
            movieReviewFeedback.setMovieId(repositoryHelper.getReferenceIfExists(Movie.class, patch.getMovieId()));
        }
        if (patch.getMovieReviewId()!=null) {
            movieReviewFeedback.setMovieReviewId(repositoryHelper.getReferenceIfExists(MovieReview.class, patch.getMovieReviewId()));
        }
        if (patch.getUserId()!=null) {
            movieReviewFeedback.setUserId(repositoryHelper.getReferenceIfExists(PortalUser.class, patch.getUserId()));
        }
        if (patch.getIsLiked()!=null) {
            movieReviewFeedback.setIsLiked(patch.getIsLiked());
        }
    }

    public void updateEntity(MovieReviewFeedbackPutDTO put, MovieReviewFeedback movieReviewFeedback) {
        if (put.getMovieId()!=null) {
            movieReviewFeedback.setMovieId(repositoryHelper.getReferenceIfExists(Movie.class, put.getMovieId()));
        }
        if (put.getMovieReviewId()!=null) {
            movieReviewFeedback.setMovieReviewId(repositoryHelper.getReferenceIfExists(MovieReview.class, put.getMovieReviewId()));
        }
        if (put.getUserId()!=null) {
            movieReviewFeedback.setUserId(repositoryHelper.getReferenceIfExists(PortalUser.class, put.getUserId()));
        }
        movieReviewFeedback.setIsLiked(put.getIsLiked());
    }

    public MovieReviewReadDTO toRead(MovieReview movieReview) {
        MovieReviewReadDTO dto = new MovieReviewReadDTO();
        dto.setId(movieReview.getId());
        dto.setMovieId(movieReview.getMovieId().getId());
        dto.setUserId(movieReview.getUserId().getId());
        dto.setTextReview(movieReview.getTextReview());
        dto.setModeratedStatus(movieReview.getModeratedStatus());
        dto.setModeratorId(movieReview.getModeratorId().getId());
        dto.setCreatedAt(movieReview.getCreatedAt());
        dto.setUpdatedAt(movieReview.getUpdatedAt());
        return dto;
    }

    public MovieReview toEntity(MovieReviewCreateDTO create) {
        MovieReview movieReview = new MovieReview();
        movieReview.setMovieId(repositoryHelper.getReferenceIfExists(Movie.class, create.getMovieId()));
        movieReview.setUserId(repositoryHelper.getReferenceIfExists(PortalUser.class, create.getUserId()));
        movieReview.setTextReview(create.getTextReview());
        movieReview.setModeratedStatus(create.getModeratedStatus());
        movieReview.setModeratorId(repositoryHelper.getReferenceIfExists(PortalUser.class, create.getModeratorId()));
        return movieReview;
    }

    public void patchEntity(MovieReviewPatchDTO patch, MovieReview movieReview) {
        if (patch.getUserId()!=null) {
            movieReview.setUserId(repositoryHelper.getReferenceIfExists(PortalUser.class, patch.getUserId()));
        }
        if (patch.getMovieId()!=null) {
            movieReview.setMovieId(repositoryHelper.getReferenceIfExists(Movie.class, patch.getMovieId()));
        }
        if (patch.getTextReview()!=null) {
            movieReview.setTextReview(patch.getTextReview());
        }
        if (patch.getModeratedStatus()!=null) {
            movieReview.setModeratedStatus(patch.getModeratedStatus());
        }
        if (patch.getModeratorId()!=null) {
            movieReview.setModeratorId(repositoryHelper.getReferenceIfExists(PortalUser.class, patch.getModeratorId()));
        }
    }

    public void updateEntity(MovieReviewPutDTO put, MovieReview movieReview) {
        if (put.getUserId()!=null) {
            movieReview.setUserId(repositoryHelper.getReferenceIfExists(PortalUser.class, put.getUserId()));
        } else {
            movieReview.setUserId(new PortalUser());
        }
        if (put.getMovieId()!=null) {
            movieReview.setMovieId(repositoryHelper.getReferenceIfExists(Movie.class, put.getMovieId()));
        } else {
            movieReview.setMovieId(new Movie());
        }
        movieReview.setTextReview(put.getTextReview());
        movieReview.setModeratedStatus(put.getModeratedStatus());
        if (put.getModeratorId()!=null) {
            movieReview.setModeratorId(repositoryHelper.getReferenceIfExists(PortalUser.class, put.getModeratorId()));
        } else {
            movieReview.setModeratorId(new PortalUser());
        }
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
        return extendedDto;
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

    public void patchEntity(MoviePatchDTO patch, Movie movie) {
        if (patch.getTitle()!=null) {
            movie.setTitle(patch.getTitle());
        }
        if (patch.getYear()!=null) {
            movie.setYear(patch.getYear());
        }
        if (patch.getDescription()!=null) {
            movie.setDescription(patch.getDescription());
        }
        if (patch.getSoundMix()!=null) {
            movie.setSoundMix(patch.getSoundMix());
        }
        if (patch.getColour()!=null) {
            movie.setColour(patch.getColour());
        }
        if (patch.getAspectRatio()!=null) {
            movie.setAspectRatio(patch.getAspectRatio());
        }
        if (patch.getCamera()!=null) {
            movie.setCamera(patch.getCamera());
        }
        if (patch.getLaboratory()!=null) {
            movie.setLaboratory(patch.getLaboratory());
        }
        if (patch.getCritique()!=null) {
            movie.setCritique(patch.getCritique());
        }
        if (patch.getIsPublished()!=null) {
            movie.setIsPublished(patch.getIsPublished());
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

    public MovieSpoilerDataReadDTO toRead(MovieSpoilerData movieSpoilerData) {
        MovieSpoilerDataReadDTO dto = new MovieSpoilerDataReadDTO();
        dto.setId(movieSpoilerData.getId());
        dto.setMovieReviewId(movieSpoilerData.getMovieReviewId().getId());
        dto.setStartIndex(movieSpoilerData.getStartIndex());
        dto.setEndIndex(movieSpoilerData.getEndIndex());
        dto.setCreatedAt(movieSpoilerData.getCreatedAt());
        dto.setUpdatedAt(movieSpoilerData.getUpdatedAt());
        return dto;
    }

    public MovieSpoilerData toEntity(MovieSpoilerDataCreateDTO create) {
        MovieSpoilerData movieSpoilerData = new MovieSpoilerData();
        movieSpoilerData.setMovieReviewId(repositoryHelper.getReferenceIfExists(MovieReview.class, create.getMovieReviewId()));
        movieSpoilerData.setStartIndex(create.getStartIndex());
        movieSpoilerData.setEndIndex(create.getEndIndex());
        return movieSpoilerData;
    }

    public void patchEntity(MovieSpoilerDataPatchDTO patch, MovieSpoilerData movieSpoilerData) {
        if (patch.getMovieReviewId()!=null) {
            movieSpoilerData.setMovieReviewId(repositoryHelper.getReferenceIfExists(MovieReview.class, patch.getMovieReviewId()));
        }
        if (patch.getStartIndex()!=null) {
            movieSpoilerData.setStartIndex(patch.getStartIndex());
        }
        if (patch.getEndIndex()!=null) {
            movieSpoilerData.setEndIndex(patch.getEndIndex());
        }
    }

    public void updateEntity(MovieSpoilerDataPutDTO put, MovieSpoilerData movieSpoilerData) {
        if (put.getMovieReviewId()!=null) {
            movieSpoilerData.setMovieReviewId(repositoryHelper.getReferenceIfExists(MovieReview.class, put.getMovieReviewId()));
        }
        movieSpoilerData.setStartIndex(put.getStartIndex());
        movieSpoilerData.setEndIndex(put.getEndIndex());
    }

    public MovieVoteReadDTO toRead(MovieVote movieVote) {
        MovieVoteReadDTO dto = new MovieVoteReadDTO();
        dto.setId(movieVote.getId());
        dto.setUserId(movieVote.getUserId().getId());
        dto.setMovieId(movieVote.getMovieId().getId());
        dto.setRating(movieVote.getRating());
        dto.setCreatedAt(movieVote.getCreatedAt());
        dto.setUpdatedAt(movieVote.getUpdatedAt());
        return dto;
    }

    public MovieVote toEntity(MovieVoteCreateDTO create) {
        MovieVote movieVote = new MovieVote();
        movieVote.setUserId(repositoryHelper.getReferenceIfExists(PortalUser.class, create.getUserId()));
        movieVote.setMovieId(repositoryHelper.getReferenceIfExists(Movie.class, create.getMovieId()));
        movieVote.setRating(create.getRating());
        return movieVote;
    }

    public void patchEntity(MovieVotePatchDTO patch, MovieVote movieVote) {
        if (patch.getUserId()!=null) {
            movieVote.setUserId(repositoryHelper.getReferenceIfExists(PortalUser.class, patch.getUserId()));
        }
        if (patch.getMovieId()!=null) {
            movieVote.setMovieId(repositoryHelper.getReferenceIfExists(Movie.class, patch.getMovieId()));
        }
        if (patch.getRating()!=null) {
            movieVote.setRating(patch.getRating());
        }
    }

    public void updateEntity(MovieVotePutDTO put, MovieVote movieVote) {
        if (put.getUserId()!=null) {
            movieVote.setUserId(repositoryHelper.getReferenceIfExists(PortalUser.class, put.getUserId()));
        } else {
            movieVote.setUserId(new PortalUser());
        }
        if (put.getMovieId()!=null) {
            movieVote.setMovieId(repositoryHelper.getReferenceIfExists(Movie.class, put.getMovieId()));
        } else {
            movieVote.setMovieId(new Movie());
        }
        movieVote.setRating(put.getRating());
    }

    public NewsReadDTO toRead(News news) {
        NewsReadDTO dto = new NewsReadDTO();
        dto.setId(news.getId());
        dto.setPublisher(news.getPublisher().getId());
        dto.setPublished(news.getPublished());
        dto.setTopic(news.getTopic());
        dto.setDescription(news.getDescription());
        dto.setCreatedAt(news.getCreatedAt());
        dto.setUpdatedAt(news.getUpdatedAt());
        return dto;
    }

    public News toEntity(NewsCreateDTO create) {
        News news = new News();
        news.setPublisher(repositoryHelper.getReferenceIfExists(PortalUser.class, create.getPublisher()));
        news.setPublished(create.getPublished());
        news.setTopic(create.getTopic());
        news.setDescription(create.getDescription());
        return news;
    }

    public void patchEntity(NewsPatchDTO patch, News news) {
        if (patch.getPublisher()!=null) {
            news.setPublisher(repositoryHelper.getReferenceIfExists(PortalUser.class, patch.getPublisher()));
        }
        if (patch.getPublished()!=null) {
            news.setPublished(patch.getPublished());
        }
        if (patch.getTopic()!=null) {
            news.setTopic(patch.getTopic());
        }
        if (patch.getDescription()!=null) {
            news.setDescription(patch.getDescription());
        }
    }

    public void updateEntity(NewsPutDTO put, News news) {
        if (put.getPublisher()!=null) {
            news.setPublisher(repositoryHelper.getReferenceIfExists(PortalUser.class, put.getPublisher()));
        } else {
            news.setPublisher(new PortalUser());
        }
        news.setPublished(put.getPublished());
        news.setTopic(put.getTopic());
        news.setDescription(put.getDescription());
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

    public Person toEntity(PersonCreateDTO create) {
        Person person = new Person();
        person.setSurname(create.getSurname());
        person.setName(create.getName());
        person.setMiddleName(create.getMiddleName());
        return person;
    }

    public void patchEntity(PersonPatchDTO patch, Person person) {
        if (patch.getName()!=null) {
            person.setName(patch.getName());
        }
        if (patch.getMiddleName()!=null) {
            person.setMiddleName(patch.getMiddleName());
        }
        if (patch.getSurname()!=null) {
            person.setSurname(patch.getSurname());
        }
    }

    public void updateEntity(PersonPutDTO put, Person person) {
        person.setName(put.getName());
        person.setMiddleName(put.getMiddleName());
        person.setSurname(put.getSurname());
    }

    public PortalUserReadDTO toRead(PortalUser portalUser) {
        PortalUserReadDTO dto = new PortalUserReadDTO();
        dto.setId(portalUser.getId());
        dto.setLogin(portalUser.getLogin());
        dto.setSurname(portalUser.getSurname());
        dto.setName(portalUser.getName());
        dto.setMiddleName(portalUser.getMiddleName());
        dto.setUserTypeId(portalUser.getUserTypeId().getId());
        dto.setUserConfidence(portalUser.getUserConfidence());
        dto.setCreatedAt(portalUser.getCreatedAt());
        dto.setUpdatedAt(portalUser.getUpdatedAt());
        return dto;
    }

    public PortalUser toEntity(PortalUserCreateDTO create) {
        PortalUser portalUser = new PortalUser();
        portalUser.setLogin(create.getLogin());
        portalUser.setSurname(create.getSurname());
        portalUser.setName(create.getName());
        portalUser.setMiddleName(create.getMiddleName());
        portalUser.setUserTypeId(repositoryHelper.getReferenceIfExists(UserType.class, create.getUserTypeId()));
        portalUser.setUserConfidence(create.getUserConfidence());
        return portalUser;
    }

    public void patchEntity(PortalUserPatchDTO patch, PortalUser portalUser) {
        if (patch.getLogin()!=null) {
            portalUser.setLogin(patch.getLogin());
        }
        if (patch.getSurname()!=null) {
            portalUser.setSurname(patch.getSurname());
        }
        if (patch.getName()!=null) {
            portalUser.setName(patch.getName());
        }
        if (patch.getMiddleName()!=null) {
            portalUser.setMiddleName(patch.getMiddleName());
        }
        if (patch.getUserTypeId()!=null) {
            portalUser.setUserTypeId(repositoryHelper.getReferenceIfExists(UserType.class, patch.getUserTypeId()));
        }
        if (patch.getUserConfidence()!=null) {
            portalUser.setUserConfidence(patch.getUserConfidence());
        }
    }

    public void updateEntity(PortalUserPutDTO put, PortalUser portalUser) {
        portalUser.setLogin(put.getLogin());
        portalUser.setSurname(put.getSurname());
        portalUser.setName(put.getName());
        portalUser.setMiddleName(put.getMiddleName());
        if (put.getUserTypeId()!=null) {
            portalUser.setUserTypeId(repositoryHelper.getReferenceIfExists(UserType.class, put.getUserTypeId()));
        }
        portalUser.setUserConfidence(put.getUserConfidence());
    }

    public ReleaseDetailReadDTO toRead(ReleaseDetail releaseDetail) {
        ReleaseDetailReadDTO dto = new ReleaseDetailReadDTO();
        dto.setId(releaseDetail.getId());
        dto.setMovieId(releaseDetail.getMovieId().getId());
        dto.setReleaseDate(releaseDetail.getReleaseDate());
        dto.setCountryId(releaseDetail.getCountryId().getId());
        dto.setCreatedAt(releaseDetail.getCreatedAt());
        dto.setUpdatedAt(releaseDetail.getUpdatedAt());
        return dto;
    }

    public ReleaseDetail toEntity(ReleaseDetailCreateDTO create) {
        ReleaseDetail releaseDetail = new ReleaseDetail();
        releaseDetail.setMovieId(repositoryHelper.getReferenceIfExists(Movie.class, create.getMovieId()));
        releaseDetail.setReleaseDate(create.getReleaseDate());
        releaseDetail.setCountryId(repositoryHelper.getReferenceIfExists(Country.class, create.getCountryId()));
        return releaseDetail;
    }

    public void patchEntity(ReleaseDetailPatchDTO patch, ReleaseDetail releaseDetail) {
        if (patch.getMovieId()!=null) {
            releaseDetail.setMovieId(repositoryHelper.getReferenceIfExists(Movie.class, patch.getMovieId()));
        }
        if (patch.getReleaseDate()!=null) {
            releaseDetail.setReleaseDate(patch.getReleaseDate());
        }
        if (patch.getCountryId()!=null) {
            releaseDetail.setCountryId(repositoryHelper.getReferenceIfExists(Country.class, patch.getCountryId()));
        }
    }

    public void updateEntity(ReleaseDetailPutDTO put, ReleaseDetail releaseDetail) {
        if (put.getMovieId()!=null) {
            releaseDetail.setMovieId(repositoryHelper.getReferenceIfExists(Movie.class, put.getMovieId()));
        }
        releaseDetail.setReleaseDate(put.getReleaseDate());
        if (put.getCountryId()!=null) {
            releaseDetail.setCountryId(repositoryHelper.getReferenceIfExists(Country.class, put.getCountryId()));
        }
    }

    public RoleReviewCompliantReadDTO toRead(RoleReviewCompliant roleReviewCompliant) {
        RoleReviewCompliantReadDTO dto = new RoleReviewCompliantReadDTO();
        dto.setId(roleReviewCompliant.getId());
        dto.setUserId(roleReviewCompliant.getUserId().getId());
        dto.setRoleId(roleReviewCompliant.getRoleId().getId());
        dto.setRoleReviewId(roleReviewCompliant.getRoleReviewId().getId());
        dto.setDescription(roleReviewCompliant.getDescription());
        dto.setModeratedStatus(roleReviewCompliant.getModeratedStatus());
        dto.setModeratorId(roleReviewCompliant.getModeratorId().getId());
        dto.setCreatedAt(roleReviewCompliant.getCreatedAt());
        dto.setUpdatedAt(roleReviewCompliant.getUpdatedAt());
        return dto;
    }

    public RoleReviewCompliant toEntity(RoleReviewCompliantCreateDTO create) {
        RoleReviewCompliant roleReviewCompliant = new RoleReviewCompliant();
        roleReviewCompliant.setUserId(repositoryHelper.getReferenceIfExists(PortalUser.class, create.getUserId()));
        roleReviewCompliant.setRoleId(repositoryHelper.getReferenceIfExists(Role.class, create.getRoleId()));
        roleReviewCompliant.setRoleReviewId(repositoryHelper.getReferenceIfExists(RoleReview.class, create.getRoleReviewId()));
        roleReviewCompliant.setDescription(create.getDescription());
        roleReviewCompliant.setModeratedStatus(create.getModeratedStatus());
        roleReviewCompliant.setModeratorId(repositoryHelper.getReferenceIfExists(PortalUser.class, create.getModeratorId()));
        return roleReviewCompliant;
    }

    public void patchEntity(RoleReviewCompliantPatchDTO patch, RoleReviewCompliant roleReviewCompliant) {
        if (patch.getUserId()!=null) {
            roleReviewCompliant.setUserId(repositoryHelper.getReferenceIfExists(PortalUser.class, patch.getUserId()));
        }
        if (patch.getRoleId()!=null) {
            roleReviewCompliant.setRoleId(repositoryHelper.getReferenceIfExists(Role.class, patch.getRoleId()));
        }
        if (patch.getRoleReviewId()!=null) {
            roleReviewCompliant.setRoleReviewId(repositoryHelper.getReferenceIfExists(RoleReview.class, patch.getRoleReviewId()));
        }
        if (patch.getDescription()!=null) {
            roleReviewCompliant.setDescription(patch.getDescription());
        }
        if (patch.getModeratedStatus()!=null) {
            roleReviewCompliant.setModeratedStatus(patch.getModeratedStatus());
        }
        if (patch.getModeratorId()!=null) {
            roleReviewCompliant.setModeratorId(repositoryHelper.getReferenceIfExists(PortalUser.class, patch.getModeratorId()));
        }
    }

    public void updateEntity(RoleReviewCompliantPutDTO put, RoleReviewCompliant roleReviewCompliant) {
        if (put.getUserId()!=null) {
            roleReviewCompliant.setUserId(repositoryHelper.getReferenceIfExists(PortalUser.class, put.getUserId()));
        }
        if (put.getRoleId()!=null) {
            roleReviewCompliant.setRoleId(repositoryHelper.getReferenceIfExists(Role.class, put.getRoleId()));
        }
        if (put.getRoleReviewId()!=null) {
            roleReviewCompliant.setRoleReviewId(repositoryHelper.getReferenceIfExists(RoleReview.class, put.getRoleReviewId()));
        }
        roleReviewCompliant.setDescription(put.getDescription());
        roleReviewCompliant.setModeratedStatus(put.getModeratedStatus());
        if (put.getModeratorId()!=null) {
            roleReviewCompliant.setModeratorId(repositoryHelper.getReferenceIfExists(PortalUser.class, put.getModeratorId()));
        } else if (roleReviewCompliant.getModeratorId() != null) {
            roleReviewCompliant.setModeratorId(new PortalUser());

            //PortalUser portalUser = repositoryHelper.getReferenceIfExists(PortalUser.class,
            //        roleReviewCompliant.getModeratorId().getId());
            //portalUser.getRoleReviewCompliants().remove(roleReviewCompliant);
            //portalUserRepository.save(portalUser);
            //roleReviewCompliant.getModeratorId().getRoleReviewCompliants().remove(roleReviewCompliant);
        }
    }

    public RoleReviewFeedbackReadDTO toRead(RoleReviewFeedback roleReviewFeedback) {
        RoleReviewFeedbackReadDTO dto = new RoleReviewFeedbackReadDTO();
        dto.setId(roleReviewFeedback.getId());
        dto.setUserId(roleReviewFeedback.getUserId().getId());
        dto.setRoleId(roleReviewFeedback.getRoleId().getId());
        dto.setRoleReviewId(roleReviewFeedback.getRoleReviewId().getId());
        dto.setIsLiked(roleReviewFeedback.getIsLiked());
        dto.setCreatedAt(roleReviewFeedback.getCreatedAt());
        dto.setUpdatedAt(roleReviewFeedback.getUpdatedAt());
        return dto;
    }

    public RoleReviewFeedback toEntity(RoleReviewFeedbackCreateDTO create) {
        RoleReviewFeedback roleReviewFeedback = new RoleReviewFeedback();
        roleReviewFeedback.setUserId(repositoryHelper.getReferenceIfExists(PortalUser.class, create.getUserId()));
        roleReviewFeedback.setRoleId(repositoryHelper.getReferenceIfExists(Role.class, create.getRoleId()));
        roleReviewFeedback.setRoleReviewId(repositoryHelper.getReferenceIfExists(RoleReview.class, create.getRoleReviewId()));
        roleReviewFeedback.setIsLiked(create.getIsLiked());
        return roleReviewFeedback;
    }

    public void patchEntity(RoleReviewFeedbackPatchDTO patch, RoleReviewFeedback roleReviewFeedback) {
        if (patch.getUserId()!=null) {
            roleReviewFeedback.setUserId(repositoryHelper.getReferenceIfExists(PortalUser.class, patch.getUserId()));
        }
        if (patch.getRoleId()!=null) {
            roleReviewFeedback.setRoleId(repositoryHelper.getReferenceIfExists(Role.class, patch.getRoleId()));
        }
        if (patch.getRoleReviewId()!=null) {
            roleReviewFeedback.setRoleReviewId(repositoryHelper.getReferenceIfExists(RoleReview.class, patch.getRoleReviewId()));
        }
        if (patch.getIsLiked()!=null) {
            roleReviewFeedback.setIsLiked(patch.getIsLiked());
        }
    }

    public void updateEntity(RoleReviewFeedbackPutDTO put, RoleReviewFeedback roleReviewFeedback) {
        if (put.getUserId()!=null) {
            roleReviewFeedback.setUserId(repositoryHelper.getReferenceIfExists(PortalUser.class, put.getUserId()));
        } else {
            roleReviewFeedback.setUserId(new PortalUser());
        }
        if (put.getRoleId()!=null) {
            roleReviewFeedback.setRoleId(repositoryHelper.getReferenceIfExists(Role.class, put.getRoleId()));
        } else {
            roleReviewFeedback.setRoleId(new Role());
        }
        if (put.getRoleReviewId()!=null) {
            roleReviewFeedback.setRoleReviewId(repositoryHelper.getReferenceIfExists(RoleReview.class, put.getRoleReviewId()));
        } else {
            roleReviewFeedback.setRoleReviewId(new RoleReview());
        }
        roleReviewFeedback.setIsLiked(put.getIsLiked());
    }

    public RoleReviewReadDTO toRead(RoleReview roleReview) {
        RoleReviewReadDTO dto = new RoleReviewReadDTO();
        dto.setId(roleReview.getId());
        dto.setUserId(roleReview.getUserId().getId());
        dto.setRoleId(roleReview.getRoleId().getId());
        dto.setTextReview(roleReview.getTextReview());
        dto.setModeratedStatus(roleReview.getModeratedStatus());
        dto.setModeratorId(roleReview.getModeratorId().getId());
        dto.setCreatedAt(roleReview.getCreatedAt());
        dto.setUpdatedAt(roleReview.getUpdatedAt());
        return dto;
    }

    public RoleReview toEntity(RoleReviewCreateDTO create) {
        RoleReview roleReview = new RoleReview();
        roleReview.setUserId(repositoryHelper.getReferenceIfExists(PortalUser.class, create.getUserId()));
        roleReview.setRoleId(repositoryHelper.getReferenceIfExists(Role.class, create.getRoleId()));
        roleReview.setTextReview(create.getTextReview());
        roleReview.setModeratedStatus(create.getModeratedStatus());
        roleReview.setModeratorId(repositoryHelper.getReferenceIfExists(PortalUser.class, create.getModeratorId()));
        return roleReview;
    }

    public void patchEntity(RoleReviewPatchDTO patch, RoleReview roleReview) {
        if (patch.getUserId()!=null) {
            roleReview.setUserId(repositoryHelper.getReferenceIfExists(PortalUser.class, patch.getUserId()));
        }
        if (patch.getRoleId()!=null) {
            roleReview.setRoleId(repositoryHelper.getReferenceIfExists(Role.class, patch.getRoleId()));
        }
        if (patch.getTextReview()!=null) {
            roleReview.setTextReview(patch.getTextReview());
        }
        if (patch.getModeratedStatus()!=null) {
            roleReview.setModeratedStatus(patch.getModeratedStatus());
        }
        if (patch.getModeratorId()!=null) {
            roleReview.setModeratorId(repositoryHelper.getReferenceIfExists(PortalUser.class, patch.getModeratorId()));
        }
    }

    public void updateEntity(RoleReviewPutDTO put, RoleReview roleReview) {
        if (put.getUserId()!=null) {
            roleReview.setUserId(repositoryHelper.getReferenceIfExists(PortalUser.class, put.getUserId()));
        } else {
            roleReview.setUserId(new PortalUser());
        }
        if (put.getRoleId()!=null) {
            roleReview.setRoleId(repositoryHelper.getReferenceIfExists(Role.class, put.getRoleId()));
        } else {
            roleReview.setRoleId(new Role());
        }
        roleReview.setTextReview(put.getTextReview());
        roleReview.setModeratedStatus(put.getModeratedStatus());
        if (put.getModeratorId()!=null) {
            roleReview.setModeratorId(repositoryHelper.getReferenceIfExists(PortalUser.class, put.getModeratorId()));
        } else {
            roleReview.setModeratorId(new PortalUser());
        }
    }

    public RoleReadDTO toRead(Role role) {
        RoleReadDTO dto = new RoleReadDTO();
        dto.setId(role.getId());
        dto.setTitle(role.getTitle());
        dto.setRoleType(role.getRoleType());
        dto.setDescription(role.getDescription());
        dto.setPersonId(role.getPersonId().getId());
        dto.setMovieId(role.getMovieId().getId());
        dto.setCreatedAt(role.getCreatedAt());
        dto.setUpdatedAt(role.getUpdatedAt());
        return dto;
    }

    public Role toEntity(RoleCreateDTO create) {
        Role role = new Role();
        role.setTitle(create.getTitle());
        role.setRoleType(create.getRoleType());
        role.setDescription(create.getDescription());
        role.setPersonId(repositoryHelper.getReferenceIfExists(Person.class, create.getPersonId()));
        role.setMovieId(repositoryHelper.getReferenceIfExists(Movie.class, create.getMovieId()));
        return role;
    }

    public void patchEntity(RolePatchDTO patch, Role role) {
        if (patch.getTitle()!=null) {
            role.setTitle(patch.getTitle());
        }
        if (patch.getRoleType()!=null) {
            role.setRoleType(patch.getRoleType());
        }
        if (patch.getDescription()!=null) {
            role.setDescription(patch.getDescription());
        }
        if (patch.getPersonId()!=null) {
            role.setPersonId(repositoryHelper.getReferenceIfExists(Person.class, patch.getPersonId()));
        }
        if (patch.getMovieId()!=null) {
            role.setMovieId(repositoryHelper.getReferenceIfExists(Movie.class, patch.getMovieId()));
        }
    }

    public void updateEntity(RolePutDTO put, Role role) {
        role.setTitle(put.getTitle());
        role.setRoleType(put.getRoleType());
        role.setDescription(put.getDescription());
        if (put.getPersonId()!=null) {
            role.setPersonId(repositoryHelper.getReferenceIfExists(Person.class, put.getPersonId()));
        } else {
            role.setPersonId(new Person());
        }
        if (put.getMovieId()!=null) {
            role.setMovieId(repositoryHelper.getReferenceIfExists(Movie.class, put.getMovieId()));
        }
    }

    public RoleSpoilerDataReadDTO toRead(RoleSpoilerData roleSpoilerData) {
        RoleSpoilerDataReadDTO dto = new RoleSpoilerDataReadDTO();
        dto.setId(roleSpoilerData.getId());
        dto.setRoleReviewId(roleSpoilerData.getRoleReviewId().getId());
        dto.setStartIndex(roleSpoilerData.getStartIndex());
        dto.setEndIndex(roleSpoilerData.getEndIndex());
        dto.setCreatedAt(roleSpoilerData.getCreatedAt());
        dto.setUpdatedAt(roleSpoilerData.getUpdatedAt());
        return dto;
    }

    public RoleSpoilerData toEntity(RoleSpoilerDataCreateDTO create) {
        RoleSpoilerData roleSpoilerData = new RoleSpoilerData();
        roleSpoilerData.setRoleReviewId(repositoryHelper.getReferenceIfExists(RoleReview.class, create.getRoleReviewId()));
        roleSpoilerData.setStartIndex(create.getStartIndex());
        roleSpoilerData.setEndIndex(create.getEndIndex());
        return roleSpoilerData;
    }

    public void patchEntity(RoleSpoilerDataPatchDTO patch, RoleSpoilerData roleSpoilerData) {
        if (patch.getRoleReviewId()!=null) {
            roleSpoilerData.setRoleReviewId(repositoryHelper.getReferenceIfExists(RoleReview.class, patch.getRoleReviewId()));
        }
        if (patch.getStartIndex()!=null) {
            roleSpoilerData.setStartIndex(patch.getStartIndex());
        }
        if (patch.getEndIndex()!=null) {
            roleSpoilerData.setEndIndex(patch.getEndIndex());
        }
    }

    public void updateEntity(RoleSpoilerDataPutDTO put, RoleSpoilerData roleSpoilerData) {
        if (put.getRoleReviewId()!=null) {
            roleSpoilerData.setRoleReviewId(repositoryHelper.getReferenceIfExists(RoleReview.class, put.getRoleReviewId()));
        }
        roleSpoilerData.setStartIndex(put.getStartIndex());
        roleSpoilerData.setEndIndex(put.getEndIndex());
    }

    public RoleVoteReadDTO toRead(RoleVote roleVote) {
        RoleVoteReadDTO dto = new RoleVoteReadDTO();
        dto.setId(roleVote.getId());
        dto.setUserId(roleVote.getUserId().getId());
        dto.setRoleId(roleVote.getRoleId().getId());
        dto.setRating(roleVote.getRating());
        dto.setCreatedAt(roleVote.getCreatedAt());
        dto.setUpdatedAt(roleVote.getUpdatedAt());
        return dto;
    }

    public RoleVote toEntity(RoleVoteCreateDTO create) {
        RoleVote roleVote = new RoleVote();
        roleVote.setUserId(repositoryHelper.getReferenceIfExists(PortalUser.class, create.getUserId()));
        roleVote.setRoleId(repositoryHelper.getReferenceIfExists(Role.class, create.getRoleId()));
        roleVote.setRating(create.getRating());
        return roleVote;
    }

    public void patchEntity(RoleVotePatchDTO patch, RoleVote roleVote) {
        if (patch.getUserId()!=null) {
            roleVote.setUserId(repositoryHelper.getReferenceIfExists(PortalUser.class, patch.getUserId()));
        }
        if (patch.getRoleId()!=null) {
            roleVote.setRoleId(repositoryHelper.getReferenceIfExists(Role.class, patch.getRoleId()));
        }
        if (patch.getRating()!=null) {
            roleVote.setRating(patch.getRating());
        }
    }

    public void updateEntity(RoleVotePutDTO put, RoleVote roleVote) {
        if (put.getUserId()!=null) {
            roleVote.setUserId(repositoryHelper.getReferenceIfExists(PortalUser.class, put.getUserId()));
        }
        if (put.getRoleId()!=null) {
            roleVote.setRoleId(repositoryHelper.getReferenceIfExists(Role.class, put.getRoleId()));
        }
        roleVote.setRating(put.getRating());
    }

    public UserTypeReadDTO toRead(UserType userType) {
        UserTypeReadDTO dto = new UserTypeReadDTO();
        dto.setId(userType.getId());
        dto.setUserGroup(userType.getUserGroup());
        dto.setCreatedAt(userType.getCreatedAt());
        dto.setUpdatedAt(userType.getUpdatedAt());
        return dto;
    }

    public UserType toEntity(UserTypeCreateDTO create) {
        UserType userType = new UserType();
        userType.setUserGroup(create.getUserGroup());
        return userType;
    }

    public void patchEntity(UserTypePatchDTO patch, UserType userType) {
        if (patch.getUserGroup()!=null) {
            userType.setUserGroup(patch.getUserGroup());
        }
    }

    public void updateEntity(UserTypePutDTO put, UserType userType) {
        userType.setUserGroup(put.getUserGroup());
    }
}
