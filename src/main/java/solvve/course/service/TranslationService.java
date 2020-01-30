package solvve.course.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import solvve.course.domain.*;
import solvve.course.dto.*;
import solvve.course.repository.PortalUserRepository;

@Service
public class TranslationService {

    @Autowired
    private PortalUserService portalUserService;

    @Autowired
    private MasterService masterService;

    @Autowired
    private PersonService personService;

    @Autowired
    private CrewTypeService crewTypeService;

    @Autowired
    private MovieService movieService;

    @Autowired
    private PortalUserRepository portalUserRepository;

    public VisitReadExtendedDTO toReadExtended(Visit visit) {
        VisitReadExtendedDTO dto = new VisitReadExtendedDTO();
        dto.setId(visit.getId());
        dto.setMasterId(toRead(visit.getMasterId()));
        dto.setUserId(toRead(visit.getUserId()));
        dto.setStartAt(visit.getStartAt());
        dto.setFinishAt(visit.getFinishAt());
        dto.setStatus(visit.getStatus());
        return dto;
    }

    public VisitReadDTO toRead(Visit visit) {
        VisitReadDTO dto = new VisitReadDTO();
        dto.setId(visit.getId());
        dto.setMasterId(visit.getMasterId().getId());
        dto.setUserId(visit.getUserId().getId());
        dto.setStartAt(visit.getStartAt());
        dto.setFinishAt(visit.getFinishAt());
        dto.setStatus(visit.getStatus());
        return dto;
    }

    public Visit toEntity(VisitCreateDTO create) {
        Visit visit = new Visit();
        visit.setUserId(toEntity(portalUserService.getPortalUser(create.getUserId())));
        visit.setMasterId(toEntity(masterService.getMaster(create.getMasterId())));
        visit.setStartAt(create.getStartAt());
        visit.setFinishAt(create.getFinishAt());
        visit.setStatus(create.getStatus());
        return visit;
    }

    public Visit toEntity(VisitReadDTO dto) {
        Visit visit = new Visit();
        visit.setId(dto.getId());
        visit.setUserId(toEntity(portalUserService.getPortalUser(dto.getUserId())));
        visit.setMasterId(toEntity(masterService.getMaster(dto.getMasterId())));
        visit.setStartAt(dto.getStartAt());
        visit.setFinishAt(dto.getFinishAt());
        visit.setStatus(dto.getStatus());
        return visit;
    }

    public void patchEntity(VisitPatchDTO patch, Visit visit) {
        if (patch.getUserId()!=null) {
            visit.setUserId(toEntity(portalUserService.getPortalUser(patch.getUserId())));
        }
        if (patch.getMasterId()!=null) {
            visit.setMasterId(toEntity(masterService.getMaster(patch.getMasterId())));
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

    public void putEntity(VisitPutDTO put, Visit visit) {
        if (put.getUserId()!=null) {
            visit.setUserId(toEntity(portalUserService.getPortalUser(put.getUserId())));
        } else {
            visit.setUserId(new PortalUser());
        }
        if (put.getUserId()!=null) {
            visit.setMasterId(toEntity(masterService.getMaster(put.getMasterId())));
        } else {
            visit.setMasterId(new Master());
        }
        visit.setStartAt(put.getStartAt());
        visit.setFinishAt(put.getFinishAt());
        visit.setStatus(put.getStatus());
    }

    public CountryReadDTO toRead(Country country) {
        CountryReadDTO dto = new CountryReadDTO();
        dto.setId(country.getId());
        dto.setName(country.getName());
        return dto;
    }

    public Country toEntity(CountryCreateDTO create) {
        Country country = new Country();
        country.setName(create.getName());
        return country;
    }
    public Country toEntity(CountryReadDTO dto) {
        Country country = new Country();
        country.setId(dto.getId());
        country.setName(dto.getName());
        return country;
    }

    public void patchEntity(CountryPatchDTO patch, Country country) {
        if (patch.getName()!=null) {
            country.setName(patch.getName());
        }
    }

    public void putEntity(CountryPutDTO put, Country country) {
        country.setName(put.getName());
    }

    public CrewReadExtendedDTO toReadExtended(Crew crew) {
        CrewReadExtendedDTO dto = new CrewReadExtendedDTO();
        dto.setId(crew.getId());
        dto.setMovieId(toRead(crew.getMovieId()));
        dto.setPersonId(toRead(crew.getPersonId()));
        /*if (crew.getCrewType()!=null) {
            dto.setCrewType(toRead(crew.getCrewType()));
        }*/
        dto.setCrewType(toRead(crew.getCrewType()));
        dto.setDescription(crew.getDescription());
        return dto;
    }

    public CrewReadDTO toRead(Crew crew) {
        CrewReadDTO dto = new CrewReadDTO();
        dto.setId(crew.getId());
        dto.setMovieId(crew.getMovieId().getId());
        dto.setPersonId(crew.getPersonId().getId());
        dto.setCrewType(crew.getCrewType().getId());
        dto.setDescription(crew.getDescription());
        return dto;
    }

    public Crew toEntity(CrewCreateDTO create) {
        Crew crew = new Crew();
        crew.setMovieId(toEntity(movieService.getMovie(create.getMovieId())));
        crew.setPersonId(toEntity(personService.getPersons(create.getPersonId())));
        crew.setCrewType(toEntity(crewTypeService.getCrewType(create.getCrewType())));
        crew.setDescription(create.getDescription());
        return crew;
    }

    public Crew toEntity(CrewReadDTO dto) {
        Crew crew = new Crew();
        crew.setId(dto.getId());
        crew.setPersonId(toEntity(personService.getPersons(dto.getPersonId())));
        crew.setCrewType(toEntity(crewTypeService.getCrewType(dto.getCrewType())));
        crew.setDescription(dto.getDescription());
        crew.setMovieId(toEntity(movieService.getMovie(dto.getMovieId())));
        return crew;
    }

    public void patchEntity(CrewPatchDTO patch, Crew crew) {
        if (patch.getMovieId()!=null) {
            crew.setMovieId(toEntity(movieService.getMovie(patch.getMovieId())));
        }
        if (patch.getPersonId()!=null) {
            crew.setPersonId(toEntity(personService.getPersons(patch.getPersonId())));
        }
        if (patch.getCrewType()!=null) {
            crew.setCrewType(toEntity(crewTypeService.getCrewType(patch.getCrewType())));
        }
        if (patch.getDescription()!=null) {
            crew.setDescription(patch.getDescription());
        }
    }

    public void putEntity(CrewPutDTO put, Crew crew) {
        if (put.getMovieId()!=null) {
            crew.setMovieId(toEntity(movieService.getMovie(put.getMovieId())));
        }
        if (put.getPersonId()!=null) {
            crew.setPersonId(toEntity(personService.getPersons(put.getPersonId())));
        }
        if (put.getCrewType()!=null) {
            crew.setCrewType(toEntity(crewTypeService.getCrewType(put.getCrewType())));
        } else {
            crew.setCrewType(new CrewType());
        }
        crew.setDescription(put.getDescription());
    }

    public CrewTypeReadDTO toRead(CrewType crewType) {
        CrewTypeReadDTO dto = new CrewTypeReadDTO();
        dto.setId(crewType.getId());
        dto.setName(crewType.getName());
        return dto;
    }

    public CrewType toEntity(CrewTypeCreateDTO create) {
        CrewType crewType = new CrewType();
        crewType.setName(create.getName());
        return crewType;
    }

    public CrewType toEntity(CrewTypeReadDTO dto) {
        CrewType crewType = new CrewType();
        crewType.setId(dto.getId());
        crewType.setName(dto.getName());
        return crewType;
    }

    public void patchEntity(CrewTypePatchDTO patch, CrewType crewType) {
        if (patch.getName()!=null) {
            crewType.setName(patch.getName());
        }
    }

    public void putEntity(CrewTypePutDTO put, CrewType crewType) {
        crewType.setName(put.getName());
    }

    public UserGrantReadDTO toRead(UserGrant userGrant) {
        UserGrantReadDTO dto = new UserGrantReadDTO();
        dto.setId(userGrant.getId());
        dto.setUserTypeId(userGrant.getUserTypeId());
        dto.setUserPermission(userGrant.getUserPermission());
        dto.setObjectName(userGrant.getObjectName());
        dto.setGrantedBy(userGrant.getGrantedBy());
        return dto;
    }

    public UserGrant toEntity(UserGrantCreateDTO create) {
        UserGrant userGrant = new UserGrant();
        userGrant.setUserTypeId(create.getUserTypeId());
        userGrant.setUserPermission(create.getUserPermission());
        userGrant.setObjectName(create.getObjectName());
        userGrant.setGrantedBy(create.getGrantedBy());
        return userGrant;
    }

    public UserGrant toEntity(UserGrantReadDTO dto) {
        UserGrant userGrant = new UserGrant();
        userGrant.setId(dto.getId());
        userGrant.setUserTypeId(dto.getUserTypeId());
        userGrant.setUserPermission(dto.getUserPermission());
        userGrant.setObjectName(dto.getObjectName());
        userGrant.setGrantedBy(dto.getGrantedBy());
        return userGrant;
    }

    public void patchEntity(UserGrantPatchDTO patch, UserGrant userGrant) {
        if (patch.getUserTypeId()!=null) {
            userGrant.setUserTypeId(patch.getUserTypeId());
        }
        if (patch.getObjectName()!=null) {
            userGrant.setObjectName(patch.getObjectName());
        }
        if (patch.getUserPermission()!=null) {
            userGrant.setUserPermission(patch.getUserPermission());
        }
        if (patch.getGrantedBy()!=null) {
            userGrant.setGrantedBy(patch.getGrantedBy());
        }
    }

    public void putEntity(UserGrantPutDTO put, UserGrant userGrant) {
        userGrant.setUserTypeId(put.getUserTypeId());
        userGrant.setObjectName(put.getObjectName());
        userGrant.setUserPermission(put.getUserPermission());
        userGrant.setGrantedBy(put.getGrantedBy());
    }

    public MasterReadDTO toRead(Master master) {
        MasterReadDTO dto = new MasterReadDTO();
        dto.setId(master.getId());
        dto.setName(master.getName());
        dto.setPhone(master.getPhone());
        dto.setAbout(master.getAbout());
        return dto;
    }

    public Master toEntity(MasterCreateDTO create) {
        Master master = new Master();
        master.setName(create.getName());
        master.setPhone(create.getPhone());
        master.setAbout(create.getAbout());
        return master;
    }

    public Master toEntity(MasterReadDTO dto) {
        Master master = new Master();
        master.setId(dto.getId());
        master.setName(dto.getName());
        master.setPhone(dto.getPhone());
        master.setAbout(dto.getAbout());
        return master;
    }

    public void patchEntity(MasterPatchDTO patch, Master master) {
        if (patch.getName()!=null) {
            master.setName(patch.getName());
        }
        if (patch.getPhone()!=null) {
            master.setPhone(patch.getPhone());
        }
        if (patch.getAbout()!=null) {
            master.setAbout(patch.getAbout());
        }
    }

    public void putEntity(MasterPutDTO put, Master master) {
        master.setName(put.getName());
        master.setPhone(put.getPhone());
        master.setAbout(put.getAbout());
    }

    public MovieReviewCompliantReadDTO toRead(MovieReviewCompliant movieReviewCompliant) {
        MovieReviewCompliantReadDTO dto = new MovieReviewCompliantReadDTO();
        dto.setId(movieReviewCompliant.getId());
        dto.setUserId(movieReviewCompliant.getUserId());
        dto.setMovieId(movieReviewCompliant.getMovieId());
        dto.setMovieReviewId(movieReviewCompliant.getMovieReviewId());
        dto.setDescription(movieReviewCompliant.getDescription());
        dto.setModeratedStatus(movieReviewCompliant.getModeratedStatus());
        dto.setModeratorId(movieReviewCompliant.getModeratorId());
        return dto;
    }

    public MovieReviewCompliant toEntity(MovieReviewCompliantCreateDTO create) {
        MovieReviewCompliant movieReviewCompliant = new MovieReviewCompliant();
        movieReviewCompliant.setUserId(create.getUserId());
        movieReviewCompliant.setMovieId(create.getMovieId());
        movieReviewCompliant.setMovieReviewId(create.getMovieReviewId());
        movieReviewCompliant.setDescription(create.getDescription());
        movieReviewCompliant.setModeratedStatus(create.getModeratedStatus());
        movieReviewCompliant.setModeratorId(create.getModeratorId());
        return movieReviewCompliant;
    }

    public MovieReviewCompliant toEntity(MovieReviewCompliantReadDTO dto) {
        MovieReviewCompliant movieReviewCompliant = new MovieReviewCompliant();
        movieReviewCompliant.setId(dto.getId());
        movieReviewCompliant.setUserId(dto.getUserId());
        movieReviewCompliant.setMovieId(dto.getMovieId());
        movieReviewCompliant.setMovieReviewId(dto.getMovieReviewId());
        movieReviewCompliant.setDescription(dto.getDescription());
        movieReviewCompliant.setModeratedStatus(dto.getModeratedStatus());
        movieReviewCompliant.setModeratorId(dto.getModeratorId());
        return movieReviewCompliant;
    }

    public void patchEntity(MovieReviewCompliantPatchDTO patch, MovieReviewCompliant movieReviewCompliant) {
        if (patch.getUserId()!=null) {
            movieReviewCompliant.setUserId(patch.getUserId());
        }
        if (patch.getMovieId()!=null) {
            movieReviewCompliant.setMovieId(patch.getMovieId());
        }
        if (patch.getMovieReviewId()!=null) {
            movieReviewCompliant.setMovieReviewId(patch.getMovieReviewId());
        }
        if (patch.getDescription()!=null) {
            movieReviewCompliant.setDescription(patch.getDescription());
        }
        if (patch.getModeratedStatus()!=null) {
            movieReviewCompliant.setModeratedStatus(patch.getModeratedStatus());
        }
        if (patch.getModeratorId()!=null) {
            movieReviewCompliant.setModeratorId(patch.getModeratorId());
        }
    }

    public void putEntity(MovieReviewCompliantPutDTO put, MovieReviewCompliant movieReviewCompliant) {
        movieReviewCompliant.setUserId(put.getUserId());
        movieReviewCompliant.setMovieId(put.getMovieId());
        movieReviewCompliant.setMovieReviewId(put.getMovieReviewId());
        movieReviewCompliant.setDescription(put.getDescription());
        movieReviewCompliant.setModeratedStatus(put.getModeratedStatus());
        movieReviewCompliant.setModeratorId(put.getModeratorId());
    }

    public MovieReviewFeedbackReadDTO toRead(MovieReviewFeedback movieReviewFeedback) {
        MovieReviewFeedbackReadDTO dto = new MovieReviewFeedbackReadDTO();
        dto.setId(movieReviewFeedback.getId());
        dto.setUserId(movieReviewFeedback.getUserId());
        dto.setMovieId(movieReviewFeedback.getMovieId());
        dto.setMovieReviewId(movieReviewFeedback.getMovieReviewId());
        dto.setIsLiked(movieReviewFeedback.getIsLiked());
        return dto;
    }

    public MovieReviewFeedback toEntity(MovieReviewFeedbackCreateDTO create) {
        MovieReviewFeedback movieReviewFeedback = new MovieReviewFeedback();
        movieReviewFeedback.setUserId(create.getUserId());
        movieReviewFeedback.setMovieId(create.getMovieId());
        movieReviewFeedback.setMovieReviewId(create.getMovieReviewId());
        movieReviewFeedback.setIsLiked(create.getIsLiked());
        return movieReviewFeedback;
    }

    public MovieReviewFeedback toEntity(MovieReviewFeedbackReadDTO dto) {
        MovieReviewFeedback movieReviewFeedback = new MovieReviewFeedback();
        movieReviewFeedback.setId(dto.getId());
        movieReviewFeedback.setUserId(dto.getUserId());
        movieReviewFeedback.setMovieId(dto.getMovieId());
        movieReviewFeedback.setMovieReviewId(dto.getMovieReviewId());
        movieReviewFeedback.setIsLiked(dto.getIsLiked());
        return movieReviewFeedback;
    }

    public void patchEntity(MovieReviewFeedbackPatchDTO patch, MovieReviewFeedback movieReviewFeedback) {
        if (patch.getMovieId()!=null) {
            movieReviewFeedback.setMovieId(patch.getMovieId());
        }
        if (patch.getMovieReviewId()!=null) {
            movieReviewFeedback.setMovieReviewId(patch.getMovieReviewId());
        }
        if (patch.getUserId()!=null) {
            movieReviewFeedback.setUserId(patch.getUserId());
        }
        if (patch.getIsLiked()!=null) {
            movieReviewFeedback.setIsLiked(patch.getIsLiked());
        }
    }

    public void putEntity(MovieReviewFeedbackPutDTO put, MovieReviewFeedback movieReviewFeedback) {
        movieReviewFeedback.setMovieId(put.getMovieId());
        movieReviewFeedback.setMovieReviewId(put.getMovieReviewId());
        movieReviewFeedback.setUserId(put.getUserId());
        movieReviewFeedback.setIsLiked(put.getIsLiked());
    }

    public MovieReviewReadDTO toRead(MovieReview movieReview) {
        MovieReviewReadDTO dto = new MovieReviewReadDTO();
        dto.setId(movieReview.getId());
        dto.setMovieId(movieReview.getMovieId());
        dto.setUserId(movieReview.getUserId());
        dto.setTextReview(movieReview.getTextReview());
        dto.setModeratedStatus(movieReview.getModeratedStatus());
        dto.setModeratorId(movieReview.getModeratorId());
        return dto;
    }

    public MovieReview toEntity(MovieReviewCreateDTO create) {
        MovieReview movieReview = new MovieReview();
        movieReview.setMovieId(create.getMovieId());
        movieReview.setUserId(create.getUserId());
        movieReview.setTextReview(create.getTextReview());
        movieReview.setModeratedStatus(create.getModeratedStatus());
        movieReview.setModeratorId(create.getModeratorId());
        return movieReview;
    }

    public MovieReview toEntity(MovieReviewReadDTO dto) {
        MovieReview movieReview = new MovieReview();
        movieReview.setId(dto.getId());
        movieReview.setMovieId(dto.getMovieId());
        movieReview.setUserId(dto.getUserId());
        movieReview.setTextReview(dto.getTextReview());
        movieReview.setModeratedStatus(dto.getModeratedStatus());
        movieReview.setModeratorId(dto.getModeratorId());
        return movieReview;
    }

    public void patchEntity(MovieReviewPatchDTO patch, MovieReview movieReview) {
        if (patch.getUserId()!=null) {
            movieReview.setUserId(patch.getUserId());
        }
        if (patch.getMovieId()!=null) {
            movieReview.setMovieId(patch.getMovieId());
        }
        if (patch.getTextReview()!=null) {
            movieReview.setTextReview(patch.getTextReview());
        }
        if (patch.getModeratedStatus()!=null) {
            movieReview.setModeratedStatus(patch.getModeratedStatus());
        }
        if (patch.getModeratorId()!=null) {
            movieReview.setModeratorId(patch.getModeratorId());
        }
    }

    public void putEntity(MovieReviewPutDTO put, MovieReview movieReview) {
        movieReview.setUserId(put.getUserId());
        movieReview.setMovieId(put.getMovieId());
        movieReview.setTextReview(put.getTextReview());
        movieReview.setModeratedStatus(put.getModeratedStatus());
        movieReview.setModeratorId(put.getModeratorId());
    }

    public MovieReadDTO toRead(Movie movie) {
        MovieReadDTO dto = new MovieReadDTO();
        dto.setId(movie.getId());
        dto.setTitle(movie.getTitle());
        dto.setYear(movie.getYear());
        dto.setGenres(movie.getGenres());
        dto.setDescription(movie.getDescription());
        dto.setCompanies(movie.getCompanies());
        dto.setSoundMix(movie.getSoundMix());
        dto.setColour(movie.getColour());
        dto.setAspectRatio(movie.getAspectRatio());
        dto.setCamera(movie.getCamera());
        dto.setLaboratory(movie.getLaboratory());
        dto.setLanguages(movie.getLanguages());
        dto.setFilmingLocations(movie.getFilmingLocations());
        dto.setCritique(movie.getCritique());
        dto.setIsPublished(movie.getIsPublished());
        dto.setMovieProdCountries(movie.getMovieProdCountries());
        return dto;
    }

    public Movie toEntity(MovieCreateDTO create) {
        Movie movie = new Movie();
        movie.setTitle(create.getTitle());
        movie.setYear(create.getYear());
        movie.setGenres(create.getGenres());
        movie.setDescription(create.getDescription());
        movie.setCompanies(create.getCompanies());
        movie.setSoundMix(create.getSoundMix());
        movie.setColour(create.getColour());
        movie.setAspectRatio(create.getAspectRatio());
        movie.setCamera(create.getCamera());
        movie.setLaboratory(create.getLaboratory());
        movie.setLanguages(create.getLanguages());
        movie.setFilmingLocations(create.getFilmingLocations());
        movie.setCritique(create.getCritique());
        movie.setIsPublished(create.getIsPublished());
        movie.setMovieProdCountries(create.getMovieProdCountries());
        return movie;
    }

    public Movie toEntity(MovieReadDTO dto) {
        Movie movie = new Movie();
        movie.setId(dto.getId());
        movie.setTitle(dto.getTitle());
        movie.setYear(dto.getYear());
        movie.setGenres(dto.getGenres());
        movie.setDescription(dto.getDescription());
        movie.setCompanies(dto.getCompanies());
        movie.setSoundMix(dto.getSoundMix());
        movie.setColour(dto.getColour());
        movie.setAspectRatio(dto.getAspectRatio());
        movie.setCamera(dto.getCamera());
        movie.setLaboratory(dto.getLaboratory());
        movie.setLanguages(dto.getLanguages());
        movie.setFilmingLocations(dto.getFilmingLocations());
        movie.setCritique(dto.getCritique());
        movie.setIsPublished(dto.getIsPublished());
        movie.setMovieProdCountries(dto.getMovieProdCountries());
        return movie;
    }

    public void patchEntity(MoviePatchDTO patch, Movie movie) {
        if (patch.getTitle()!=null) {
            movie.setTitle(patch.getTitle());
        }
        if (patch.getYear()!=null) {
            movie.setYear(patch.getYear());
        }
        if (patch.getGenres()!=null) {
            movie.setGenres(patch.getGenres());
        }
        if (patch.getDescription()!=null) {
            movie.setDescription(patch.getDescription());
        }
        if (patch.getCompanies()!=null) {
            movie.setCompanies(patch.getCompanies());
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
        if (patch.getLanguages()!=null) {
            movie.setLanguages(patch.getLanguages());
        }
        if (patch.getFilmingLocations()!=null) {
            movie.setFilmingLocations(patch.getFilmingLocations());
        }
        if (patch.getCritique()!=null) {
            movie.setCritique(patch.getCritique());
        }
        if (patch.getIsPublished()!=null) {
            movie.setIsPublished(patch.getIsPublished());
        }
        if (patch.getMovieProdCountries()!=null) {
            movie.setMovieProdCountries(patch.getMovieProdCountries());
        }
    }

    public void putEntity(MoviePutDTO put, Movie movie) {
        movie.setTitle(put.getTitle());
        movie.setYear(put.getYear());
        movie.setGenres(put.getGenres());
        movie.setDescription(put.getDescription());
        movie.setCompanies(put.getCompanies());
        movie.setSoundMix(put.getSoundMix());
        movie.setColour(put.getColour());
        movie.setAspectRatio(put.getAspectRatio());
        movie.setCamera(put.getCamera());
        movie.setLaboratory(put.getLaboratory());
        movie.setLanguages(put.getLanguages());
        movie.setFilmingLocations(put.getFilmingLocations());
        movie.setCritique(put.getCritique());
        movie.setIsPublished(put.getIsPublished());
        movie.setMovieProdCountries(put.getMovieProdCountries());
    }

    public MovieSpoilerDataReadDTO toRead(MovieSpoilerData movieSpoilerData) {
        MovieSpoilerDataReadDTO dto = new MovieSpoilerDataReadDTO();
        dto.setId(movieSpoilerData.getId());
        dto.setMovieReviewId(movieSpoilerData.getMovieReviewId());
        dto.setStartIndex(movieSpoilerData.getStartIndex());
        dto.setEndIndex(movieSpoilerData.getEndIndex());
        return dto;
    }

    public MovieSpoilerData toEntity(MovieSpoilerDataCreateDTO create) {
        MovieSpoilerData movieSpoilerData = new MovieSpoilerData();
        movieSpoilerData.setMovieReviewId(create.getMovieReviewId());
        movieSpoilerData.setStartIndex(create.getStartIndex());
        movieSpoilerData.setEndIndex(create.getEndIndex());
        return movieSpoilerData;
    }

    public MovieSpoilerData toEntity(MovieSpoilerDataReadDTO dto) {
        MovieSpoilerData movieSpoilerData = new MovieSpoilerData();
        movieSpoilerData.setId(dto.getId());
        movieSpoilerData.setMovieReviewId(dto.getMovieReviewId());
        movieSpoilerData.setStartIndex(dto.getStartIndex());
        movieSpoilerData.setEndIndex(dto.getEndIndex());
        return movieSpoilerData;
    }

    public void patchEntity(MovieSpoilerDataPatchDTO patch, MovieSpoilerData movieSpoilerData) {
        if (patch.getMovieReviewId()!=null) {
            movieSpoilerData.setMovieReviewId(patch.getMovieReviewId());
        }
        if (patch.getStartIndex()!=null) {
            movieSpoilerData.setStartIndex(patch.getStartIndex());
        }
        if (patch.getEndIndex()!=null) {
            movieSpoilerData.setEndIndex(patch.getEndIndex());
        }
    }

    public void putEntity(MovieSpoilerDataPutDTO put, MovieSpoilerData movieSpoilerData) {
        movieSpoilerData.setMovieReviewId(put.getMovieReviewId());
        movieSpoilerData.setStartIndex(put.getStartIndex());
        movieSpoilerData.setEndIndex(put.getEndIndex());
    }

    public MovieVoteReadDTO toRead(MovieVote movieVote) {
        MovieVoteReadDTO dto = new MovieVoteReadDTO();
        dto.setId(movieVote.getId());
        dto.setUserId(movieVote.getUserId());
        dto.setMovieId(movieVote.getMovieId());
        dto.setRating(movieVote.getRating());
        return dto;
    }

    public MovieVote toEntity(MovieVoteCreateDTO create) {
        MovieVote movieVote = new MovieVote();
        movieVote.setUserId(create.getUserId());
        movieVote.setMovieId(create.getMovieId());
        movieVote.setRating(create.getRating());
        return movieVote;
    }

    public MovieVote toEntity(MovieVoteReadDTO dto) {
        MovieVote movieVote = new MovieVote();
        movieVote.setId(dto.getId());
        movieVote.setUserId(dto.getUserId());
        movieVote.setMovieId(dto.getMovieId());
        movieVote.setRating(dto.getRating());
        return movieVote;
    }

    public void patchEntity(MovieVotePatchDTO patch, MovieVote movieVote) {
        if (patch.getUserId()!=null) {
            movieVote.setUserId(patch.getUserId());
        }
        if (patch.getMovieId()!=null) {
            movieVote.setMovieId(patch.getMovieId());
        }
        if (patch.getRating()!=null) {
            movieVote.setRating(patch.getRating());
        }
    }

    public void putEntity(MovieVotePutDTO put, MovieVote movieVote) {
        movieVote.setUserId(put.getUserId());
        movieVote.setMovieId(put.getMovieId());
        movieVote.setRating(put.getRating());
    }

    public NewsReadDTO toRead(News news) {
        NewsReadDTO dto = new NewsReadDTO();
        dto.setId(news.getId());
        dto.setUserId(news.getUserId());
        dto.setPublished(news.getPublished());
        dto.setTopic(news.getTopic());
        dto.setDescription(news.getDescription());
        return dto;
    }

    public News toEntity(NewsCreateDTO create) {
        News news = new News();
        news.setUserId(create.getUserId());
        news.setPublished(create.getPublished());
        news.setTopic(create.getTopic());
        news.setDescription(create.getDescription());
        return news;
    }

    public News toEntity(NewsReadDTO dto) {
        News news = new News();
        news.setId(dto.getId());
        news.setUserId(dto.getUserId());
        news.setPublished(dto.getPublished());
        news.setTopic(dto.getTopic());
        news.setDescription(dto.getDescription());
        return news;
    }

    public void patchEntity(NewsPatchDTO patch, News news) {
        if (patch.getUserId()!=null) {
            news.setUserId(patch.getUserId());
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

    public void putEntity(NewsPutDTO put, News news) {
        news.setUserId(put.getUserId());
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
        return dto;
    }

    public Person toEntity(PersonCreateDTO create) {
        Person person = new Person();
        person.setSurname(create.getSurname());
        person.setName(create.getName());
        person.setMiddleName(create.getMiddleName());
        return person;
    }

    public Person toEntity(PersonReadDTO dto) {
        Person person = new Person();
        person.setId(dto.getId());
        person.setSurname(dto.getSurname());
        person.setName(dto.getName());
        person.setMiddleName(dto.getMiddleName());
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

    public void putEntity(PersonPutDTO put, Person person) {
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
        dto.setUserType(portalUser.getUserType());
        dto.setUserConfidence(portalUser.getUserConfidence());
        return dto;
    }

    public PortalUser toEntity(PortalUserCreateDTO create) {
        PortalUser portalUser = new PortalUser();
        portalUser.setLogin(create.getLogin());
        portalUser.setSurname(create.getSurname());
        portalUser.setName(create.getName());
        portalUser.setMiddleName(create.getMiddleName());
        portalUser.setUserType(create.getUserType());
        portalUser.setUserConfidence(create.getUserConfidence());
        return portalUser;
    }

    public PortalUser toEntity(PortalUserReadDTO dto) {
        PortalUser portalUser = new PortalUser();
        portalUser.setId(dto.getId());
        portalUser.setLogin(dto.getLogin());
        portalUser.setSurname(dto.getSurname());
        portalUser.setName(dto.getName());
        portalUser.setMiddleName(dto.getMiddleName());
        portalUser.setUserType(dto.getUserType());
        portalUser.setUserConfidence(dto.getUserConfidence());
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
        if (patch.getUserType()!=null) {
            portalUser.setUserType(patch.getUserType());
        }
        if (patch.getUserConfidence()!=null) {
            portalUser.setUserConfidence(patch.getUserConfidence());
        }
    }

    public void putEntity(PortalUserPutDTO put, PortalUser portalUser) {
        portalUser.setLogin(put.getLogin());
        portalUser.setSurname(put.getSurname());
        portalUser.setName(put.getName());
        portalUser.setMiddleName(put.getMiddleName());
        portalUser.setUserType(put.getUserType());
        portalUser.setUserConfidence(put.getUserConfidence());
    }

    public ReleaseDetailReadDTO toRead(ReleaseDetail releaseDetail) {
        ReleaseDetailReadDTO dto = new ReleaseDetailReadDTO();
        dto.setId(releaseDetail.getId());
        dto.setMovieId(releaseDetail.getMovieId());
        dto.setReleaseDate(releaseDetail.getReleaseDate());
        dto.setCountryId(releaseDetail.getCountryId());
        return dto;
    }

    public ReleaseDetail toEntity(ReleaseDetailCreateDTO create) {
        ReleaseDetail releaseDetail = new ReleaseDetail();
        releaseDetail.setMovieId(create.getMovieId());
        releaseDetail.setReleaseDate(create.getReleaseDate());
        releaseDetail.setCountryId(create.getCountryId());
        return releaseDetail;
    }

    public ReleaseDetail toEntity(ReleaseDetailReadDTO dto) {
        ReleaseDetail releaseDetail = new ReleaseDetail();
        releaseDetail.setId(dto.getId());
        releaseDetail.setMovieId(dto.getMovieId());
        releaseDetail.setReleaseDate(dto.getReleaseDate());
        releaseDetail.setCountryId(dto.getCountryId());
        return releaseDetail;
    }

    public void patchEntity(ReleaseDetailPatchDTO patch, ReleaseDetail releaseDetail) {
        if (patch.getMovieId()!=null) {
            releaseDetail.setMovieId(patch.getMovieId());
        }
        if (patch.getReleaseDate()!=null) {
            releaseDetail.setReleaseDate(patch.getReleaseDate());
        }
        if (patch.getCountryId()!=null) {
            releaseDetail.setCountryId(patch.getCountryId());
        }
    }

    public void putEntity(ReleaseDetailPutDTO put, ReleaseDetail releaseDetail) {
        releaseDetail.setMovieId(put.getMovieId());
        releaseDetail.setReleaseDate(put.getReleaseDate());
        releaseDetail.setCountryId(put.getCountryId());
    }

    public RoleReviewCompliantReadDTO toRead(RoleReviewCompliant roleReviewCompliant) {
        RoleReviewCompliantReadDTO dto = new RoleReviewCompliantReadDTO();
        dto.setId(roleReviewCompliant.getId());
        dto.setUserId(roleReviewCompliant.getUserId());
        dto.setRoleId(roleReviewCompliant.getRoleId());
        dto.setRoleReviewId(roleReviewCompliant.getRoleReviewId());
        dto.setDescription(roleReviewCompliant.getDescription());
        dto.setModeratedStatus(roleReviewCompliant.getModeratedStatus());
        dto.setModeratorId(roleReviewCompliant.getModeratorId());
        return dto;
    }

    public RoleReviewCompliant toEntity(RoleReviewCompliantCreateDTO create) {
        RoleReviewCompliant roleReviewCompliant = new RoleReviewCompliant();
        roleReviewCompliant.setUserId(create.getUserId());
        roleReviewCompliant.setRoleId(create.getRoleId());
        roleReviewCompliant.setRoleReviewId(create.getRoleReviewId());
        roleReviewCompliant.setDescription(create.getDescription());
        roleReviewCompliant.setModeratedStatus(create.getModeratedStatus());
        roleReviewCompliant.setModeratorId(create.getModeratorId());
        return roleReviewCompliant;
    }

    public RoleReviewCompliant toEntity(RoleReviewCompliantReadDTO dto) {
        RoleReviewCompliant roleReviewCompliant = new RoleReviewCompliant();
        roleReviewCompliant.setId(dto.getId());
        roleReviewCompliant.setUserId(dto.getUserId());
        roleReviewCompliant.setRoleId(dto.getRoleId());
        roleReviewCompliant.setRoleReviewId(dto.getRoleReviewId());
        roleReviewCompliant.setDescription(dto.getDescription());
        roleReviewCompliant.setModeratedStatus(dto.getModeratedStatus());
        roleReviewCompliant.setModeratorId(dto.getModeratorId());
        return roleReviewCompliant;
    }

    public void patchEntity(RoleReviewCompliantPatchDTO patch, RoleReviewCompliant roleReviewCompliant) {
        if (patch.getUserId()!=null) {
            roleReviewCompliant.setUserId(patch.getUserId());
        }
        if (patch.getRoleId()!=null) {
            roleReviewCompliant.setRoleId(patch.getRoleId());
        }
        if (patch.getRoleReviewId()!=null) {
            roleReviewCompliant.setRoleReviewId(patch.getRoleReviewId());
        }
        if (patch.getDescription()!=null) {
            roleReviewCompliant.setDescription(patch.getDescription());
        }
        if (patch.getModeratedStatus()!=null) {
            roleReviewCompliant.setModeratedStatus(patch.getModeratedStatus());
        }
        if (patch.getModeratorId()!=null) {
            roleReviewCompliant.setModeratorId(patch.getModeratorId());
        }
    }

    public void putEntity(RoleReviewCompliantPutDTO put, RoleReviewCompliant roleReviewCompliant) {
        roleReviewCompliant.setUserId(put.getUserId());
        roleReviewCompliant.setRoleId(put.getRoleId());
        roleReviewCompliant.setRoleReviewId(put.getRoleReviewId());
        roleReviewCompliant.setDescription(put.getDescription());
        roleReviewCompliant.setModeratedStatus(put.getModeratedStatus());
        roleReviewCompliant.setModeratorId(put.getModeratorId());
    }

    public RoleReviewFeedbackReadDTO toRead(RoleReviewFeedback roleReviewFeedback) {
        RoleReviewFeedbackReadDTO dto = new RoleReviewFeedbackReadDTO();
        dto.setId(roleReviewFeedback.getId());
        dto.setUserId(roleReviewFeedback.getUserId());
        dto.setRoleId(roleReviewFeedback.getRoleId());
        dto.setRoleReviewId(roleReviewFeedback.getRoleReviewId());
        dto.setIsLiked(roleReviewFeedback.getIsLiked());
        return dto;
    }

    public RoleReviewFeedback toEntity(RoleReviewFeedbackCreateDTO create) {
        RoleReviewFeedback roleReviewFeedback = new RoleReviewFeedback();
        roleReviewFeedback.setUserId(create.getUserId());
        roleReviewFeedback.setRoleId(create.getRoleId());
        roleReviewFeedback.setRoleReviewId(create.getRoleReviewId());
        roleReviewFeedback.setIsLiked(create.getIsLiked());
        return roleReviewFeedback;
    }

    public RoleReviewFeedback toEntity(RoleReviewFeedbackReadDTO dto) {
        RoleReviewFeedback roleReviewFeedback = new RoleReviewFeedback();
        roleReviewFeedback.setId(dto.getId());
        roleReviewFeedback.setUserId(dto.getUserId());
        roleReviewFeedback.setRoleId(dto.getRoleId());
        roleReviewFeedback.setRoleReviewId(dto.getRoleReviewId());
        roleReviewFeedback.setIsLiked(dto.getIsLiked());
        return roleReviewFeedback;
    }

    public void patchEntity(RoleReviewFeedbackPatchDTO patch, RoleReviewFeedback roleReviewFeedback) {
        if (patch.getUserId()!=null) {
            roleReviewFeedback.setUserId(patch.getUserId());
        }
        if (patch.getRoleId()!=null) {
            roleReviewFeedback.setRoleId(patch.getRoleId());
        }
        if (patch.getRoleReviewId()!=null) {
            roleReviewFeedback.setRoleReviewId(patch.getRoleReviewId());
        }
        if (patch.getIsLiked()!=null) {
            roleReviewFeedback.setIsLiked(patch.getIsLiked());
        }
    }

    public void putEntity(RoleReviewFeedbackPutDTO put, RoleReviewFeedback roleReviewFeedback) {
        roleReviewFeedback.setUserId(put.getUserId());
        roleReviewFeedback.setRoleId(put.getRoleId());
        roleReviewFeedback.setRoleReviewId(put.getRoleReviewId());
        roleReviewFeedback.setIsLiked(put.getIsLiked());
    }

    public RoleReviewReadDTO toRead(RoleReview roleReview) {
        RoleReviewReadDTO dto = new RoleReviewReadDTO();
        dto.setId(roleReview.getId());
        dto.setUserId(roleReview.getUserId());
        dto.setRoleId(roleReview.getRoleId());
        dto.setTextReview(roleReview.getTextReview());
        dto.setModeratedStatus(roleReview.getModeratedStatus());
        dto.setModeratorId(roleReview.getModeratorId());
        return dto;
    }

    public RoleReview toEntity(RoleReviewCreateDTO create) {
        RoleReview roleReview = new RoleReview();
        roleReview.setUserId(create.getUserId());
        roleReview.setRoleId(create.getRoleId());
        roleReview.setTextReview(create.getTextReview());
        roleReview.setModeratedStatus(create.getModeratedStatus());
        roleReview.setModeratorId(create.getModeratorId());
        return roleReview;
    }

    public RoleReview toEntity(RoleReviewReadDTO dto) {
        RoleReview roleReview = new RoleReview();
        roleReview.setId(dto.getId());
        roleReview.setUserId(dto.getUserId());
        roleReview.setRoleId(dto.getRoleId());
        roleReview.setTextReview(dto.getTextReview());
        roleReview.setModeratedStatus(dto.getModeratedStatus());
        roleReview.setModeratorId(dto.getModeratorId());
        return roleReview;
    }

    public void patchEntity(RoleReviewPatchDTO patch, RoleReview roleReview) {
        if (patch.getUserId()!=null) {
            roleReview.setUserId(patch.getUserId());
        }
        if (patch.getRoleId()!=null) {
            roleReview.setRoleId(patch.getRoleId());
        }
        if (patch.getTextReview()!=null) {
            roleReview.setTextReview(patch.getTextReview());
        }
        if (patch.getModeratedStatus()!=null) {
            roleReview.setModeratedStatus(patch.getModeratedStatus());
        }
        if (patch.getModeratorId()!=null) {
            roleReview.setModeratorId(patch.getModeratorId());
        }
    }

    public void putEntity(RoleReviewPutDTO put, RoleReview roleReview) {
        roleReview.setUserId(put.getUserId());
        roleReview.setRoleId(put.getRoleId());
        roleReview.setTextReview(put.getTextReview());
        roleReview.setModeratedStatus(put.getModeratedStatus());
        roleReview.setModeratorId(put.getModeratorId());
    }

    public RoleReadDTO toRead(Role role) {
        RoleReadDTO dto = new RoleReadDTO();
        dto.setId(role.getId());
        dto.setTitle(role.getTitle());
        dto.setRoleType(role.getRoleType());
        dto.setDescription(role.getDescription());
        dto.setPersonId(role.getPersonId());
        return dto;
    }

    public Role toEntity(RoleCreateDTO create) {
        Role role = new Role();
        role.setTitle(create.getTitle());
        role.setRoleType(create.getRoleType());
        role.setDescription(create.getDescription());
        role.setPersonId(create.getPersonId());
        return role;
    }

    public Role toEntity(RoleReadDTO dto) {
        Role role = new Role();
        role.setId(dto.getId());
        role.setTitle(dto.getTitle());
        role.setRoleType(dto.getRoleType());
        role.setDescription(dto.getDescription());
        role.setPersonId(dto.getPersonId());
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
            role.setPersonId(patch.getPersonId());
        }
    }

    public void putEntity(RolePutDTO put, Role role) {
        role.setTitle(put.getTitle());
        role.setRoleType(put.getRoleType());
        role.setDescription(put.getDescription());
        role.setPersonId(put.getPersonId());
    }

    public RoleSpoilerDataReadDTO toRead(RoleSpoilerData roleSpoilerData) {
        RoleSpoilerDataReadDTO dto = new RoleSpoilerDataReadDTO();
        dto.setId(roleSpoilerData.getId());
        dto.setRoleReviewId(roleSpoilerData.getRoleReviewId());
        dto.setStartIndex(roleSpoilerData.getStartIndex());
        dto.setEndIndex(roleSpoilerData.getEndIndex());
        return dto;
    }

    public RoleSpoilerData toEntity(RoleSpoilerDataCreateDTO create) {
        RoleSpoilerData roleSpoilerData = new RoleSpoilerData();
        roleSpoilerData.setRoleReviewId(create.getRoleReviewId());
        roleSpoilerData.setStartIndex(create.getStartIndex());
        roleSpoilerData.setEndIndex(create.getEndIndex());
        return roleSpoilerData;
    }

    public RoleSpoilerData toEntity(RoleSpoilerDataReadDTO dto) {
        RoleSpoilerData roleSpoilerData = new RoleSpoilerData();
        roleSpoilerData.setId(dto.getId());
        roleSpoilerData.setRoleReviewId(dto.getRoleReviewId());
        roleSpoilerData.setStartIndex(dto.getStartIndex());
        roleSpoilerData.setEndIndex(dto.getEndIndex());
        return roleSpoilerData;
    }

    public void patchEntity(RoleSpoilerDataPatchDTO patch, RoleSpoilerData roleSpoilerData) {
        if (patch.getRoleReviewId()!=null) {
            roleSpoilerData.setRoleReviewId(patch.getRoleReviewId());
        }
        if (patch.getStartIndex()!=null) {
            roleSpoilerData.setStartIndex(patch.getStartIndex());
        }
        if (patch.getEndIndex()!=null) {
            roleSpoilerData.setEndIndex(patch.getEndIndex());
        }
    }

    public void putEntity(RoleSpoilerDataPutDTO put, RoleSpoilerData roleSpoilerData) {
        roleSpoilerData.setRoleReviewId(put.getRoleReviewId());
        roleSpoilerData.setStartIndex(put.getStartIndex());
        roleSpoilerData.setEndIndex(put.getEndIndex());
    }

    public RoleVoteReadDTO toRead(RoleVote roleVote) {
        RoleVoteReadDTO dto = new RoleVoteReadDTO();
        dto.setId(roleVote.getId());
        dto.setUserId(roleVote.getUserId());
        dto.setRoleId(roleVote.getRoleId());
        dto.setRating(roleVote.getRating());
        return dto;
    }

    public RoleVote toEntity(RoleVoteCreateDTO create) {
        RoleVote roleVote = new RoleVote();
        roleVote.setUserId(create.getUserId());
        roleVote.setRoleId(create.getRoleId());
        roleVote.setRating(create.getRating());
        return roleVote;
    }

    public RoleVote toEntity(RoleVoteReadDTO dto) {
        RoleVote roleVote = new RoleVote();
        roleVote.setId(dto.getId());
        roleVote.setUserId(dto.getUserId());
        roleVote.setRoleId(dto.getRoleId());
        roleVote.setRating(dto.getRating());
        return roleVote;
    }

    public void patchEntity(RoleVotePatchDTO patch, RoleVote roleVote) {
        if (patch.getUserId()!=null) {
            roleVote.setUserId(patch.getUserId());
        }
        if (patch.getRoleId()!=null) {
            roleVote.setRoleId(patch.getRoleId());
        }
        if (patch.getRating()!=null) {
            roleVote.setRating(patch.getRating());
        }
    }

    public void putEntity(RoleVotePutDTO put, RoleVote roleVote) {
        roleVote.setUserId(put.getUserId());
        roleVote.setRoleId(put.getRoleId());
        roleVote.setRating(put.getRating());
    }

    public UserTypeReadDTO toRead(UserType userType) {
        UserTypeReadDTO dto = new UserTypeReadDTO();
        dto.setId(userType.getId());
        dto.setUserGroup(userType.getUserGroup());
        dto.setUserGrants(userType.getUserGrants());
        return dto;
    }

    public UserType toEntity(UserTypeCreateDTO create) {
        UserType userType = new UserType();
        userType.setUserGroup(create.getUserGroup());
        userType.setUserGrants(create.getUserGrants());
        return userType;
    }

    public UserType toEntity(UserTypeReadDTO dto) {
        UserType userType = new UserType();
        userType.setId(dto.getId());
        userType.setUserGroup(dto.getUserGroup());
        userType.setUserGrants(dto.getUserGrants());
        return userType;
    }

    public void patchEntity(UserTypePatchDTO patch, UserType userType) {
        if (patch.getUserGroup()!=null) {
            userType.setUserGroup(patch.getUserGroup());
        }
        if (patch.getUserGrants()!=null) {
            userType.setUserGrants(patch.getUserGrants());
        }
    }

    public void putEntity(UserTypePutDTO put, UserType userType) {
        userType.setUserGroup(put.getUserGroup());
        userType.setUserGrants(put.getUserGrants());
    }
}
