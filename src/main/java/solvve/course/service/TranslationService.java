package solvve.course.service;

import lombok.extern.slf4j.Slf4j;
import org.bitbucket.brunneng.ot.Configuration;
import org.bitbucket.brunneng.ot.ObjectTranslator;
import org.bitbucket.brunneng.ot.exceptions.TranslationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import solvve.course.domain.*;
import solvve.course.dto.*;
import solvve.course.repository.*;

import java.util.UUID;

@Slf4j
@Service
public class TranslationService {

    @Autowired
    private RepositoryHelper repositoryHelper;

    private ObjectTranslator objectTranslator;

    public TranslationService() {
        objectTranslator = new ObjectTranslator(createConfiguration());
    }

    private Configuration createConfiguration() {
        Configuration c = new Configuration();
        configureForAbstractEntity(c);
        configureForCompanyDetail(c);
        configureForCountry(c);
        configureForCrew(c);
        configureForCrewType(c);
        configureForGenre(c);
        configureForLanguage(c);
        configureForMovie(c);
        configureForMovieCompany(c);
        configureForMovieReview(c);
        configureForMovieReviewCompliant(c);
        configureForMovieReviewFeedback(c);
        configureForMovieSpoilerData(c);
        configureForMovieVote(c);
        configureForNews(c);
        configureForNewsUserReview(c);
        configureForNewsUserReviewNote(c);
        configureForPerson(c);
        configureForPortalUser(c);
        configureForReleaseDetail(c);
        configureForRole(c);
        configureForRoleReview(c);
        configureForRoleReviewCompliant(c);
        configureForRoleReviewFeedback(c);
        configureForRoleSpoilerData(c);
        configureForRoleVote(c);
        configureForUserGrant(c);
        configureForUserType(c);
        configureForUserTypoRequest(c);
        configureForVisit(c);

        return c;
    }

    private void configureForAbstractEntity(Configuration c) {
        c.beanOfClass(AbstractEntity.class).setIdentifierProperty("id");
        c.beanOfClass(AbstractEntity.class).setBeanFinder(
                (beanClass, id) -> repositoryHelper.getReferenceIfExists(beanClass, (UUID) id)
        );
    }

    private void configureForCompanyDetail(Configuration c) {
        c.beanOfClass(CompanyDetailsPatchDTO.class).translationTo(CompanyDetails.class).mapOnlyNotNullProperties();
    }

    private void configureForCountry(Configuration c) {
        c.beanOfClass(CountryPatchDTO.class).translationTo(Country.class).mapOnlyNotNullProperties();
    }

    private void configureForCrew(Configuration c) {
        Configuration.Translation t = c.beanOfClass(Crew.class).translationTo(CrewReadDTO.class);
        t.srcProperty("person.id").translatesTo("personId");
        t.srcProperty("movie.id").translatesTo("movieId");
        t.srcProperty("crewType.id").translatesTo("crewTypeId");

        Configuration.Translation fromCreateToEntity = c.beanOfClass(CrewCreateDTO.class).translationTo(Crew.class);
        fromCreateToEntity.srcProperty("personId").translatesTo("person.id");
        fromCreateToEntity.srcProperty("movieId").translatesTo("movie.id");
        fromCreateToEntity.srcProperty("crewTypeId").translatesTo("crewType.id");

        Configuration.Translation fromPatchToEntity =
                c.beanOfClass(CrewPatchDTO.class).translationTo(Crew.class);
        fromPatchToEntity.srcProperty("personId").translatesTo("person.id");
        fromPatchToEntity.srcProperty("movieId").translatesTo("movie.id");
        fromPatchToEntity.srcProperty("crewTypeId").translatesTo("crewType.id");
        c.beanOfClass(CrewPatchDTO.class).translationTo(Crew.class).mapOnlyNotNullProperties();
    }

    private void configureForCrewType(Configuration c) {
        c.beanOfClass(CrewTypePatchDTO.class).translationTo(CrewType.class).mapOnlyNotNullProperties();
    }

    private void configureForGenre(Configuration c) {
        Configuration.Translation t = c.beanOfClass(Genre.class).translationTo(GenreReadDTO.class);
        t.srcProperty("movie.id").translatesTo("movieId");

        Configuration.Translation fromCreateToEntity = c.beanOfClass(GenreCreateDTO.class).translationTo(Genre.class);
        fromCreateToEntity.srcProperty("movieId").translatesTo("movie.id");

        Configuration.Translation fromPatchToEntity = c.beanOfClass(GenrePatchDTO.class).translationTo(Genre.class);
        fromPatchToEntity.srcProperty("movieId").translatesTo("movie.id");
        c.beanOfClass(GenrePatchDTO.class).translationTo(Genre.class).mapOnlyNotNullProperties();
    }

    private void configureForLanguage(Configuration c) {
        c.beanOfClass(LanguagePatchDTO.class).translationTo(Language.class).mapOnlyNotNullProperties();
    }

    private void configureForMovie(Configuration c) {
        c.beanOfClass(MoviePatchDTO.class).translationTo(Movie.class).mapOnlyNotNullProperties();
    }

    private void configureForMovieCompany(Configuration c) {
        Configuration.Translation t = c.beanOfClass(MovieCompany.class).translationTo(MovieCompanyReadDTO.class);
        t.srcProperty("companyDetails.id").translatesTo("companyDetailsId");

        Configuration.Translation fromCreateToEntity =
                c.beanOfClass(MovieCompanyCreateDTO.class).translationTo(MovieCompany.class);
        fromCreateToEntity.srcProperty("companyDetailsId").translatesTo("companyDetails.id");

        Configuration.Translation fromPatchToEntity =
                c.beanOfClass(MovieCompanyPatchDTO.class).translationTo(MovieCompany.class);
        fromPatchToEntity.srcProperty("companyDetailsId").translatesTo("companyDetails.id");
        c.beanOfClass(MovieCompanyPatchDTO.class).translationTo(MovieCompany.class).mapOnlyNotNullProperties();
    }

    private void configureForMovieReview(Configuration c) {
        Configuration.Translation t = c.beanOfClass(MovieReview.class).translationTo(MovieReviewReadDTO.class);
        t.srcProperty("portalUser.id").translatesTo("portalUserId");
        t.srcProperty("movie.id").translatesTo("movieId");
        t.srcProperty("moderator.id").translatesTo("moderatorId");

        Configuration.Translation fromCreateToEntity =
                c.beanOfClass(MovieReviewCreateDTO.class).translationTo(MovieReview.class);
        fromCreateToEntity.srcProperty("portalUserId").translatesTo("portalUser.id");
        fromCreateToEntity.srcProperty("movieId").translatesTo("movie.id");
        fromCreateToEntity.srcProperty("moderatorId").translatesTo("moderator.id");

        Configuration.Translation fromPatchToEntity =
                c.beanOfClass(MovieReviewPatchDTO.class).translationTo(MovieReview.class);
        fromPatchToEntity.srcProperty("portalUserId").translatesTo("portalUser.id");
        fromPatchToEntity.srcProperty("movieId").translatesTo("movie.id");
        fromPatchToEntity.srcProperty("moderatorId").translatesTo("moderator.id");
        c.beanOfClass(MovieReviewPatchDTO.class).translationTo(MovieReview.class).mapOnlyNotNullProperties();
    }

    private void configureForMovieReviewCompliant(Configuration c) {
        Configuration.Translation t =
                c.beanOfClass(MovieReviewCompliant.class).translationTo(MovieReviewCompliantReadDTO.class);
        t.srcProperty("portalUser.id").translatesTo("portalUserId");
        t.srcProperty("movie.id").translatesTo("movieId");
        t.srcProperty("movieReview.id").translatesTo("movieReviewId");
        t.srcProperty("moderator.id").translatesTo("moderatorId");

        Configuration.Translation fromCreateToEntity =
                c.beanOfClass(MovieReviewCompliantCreateDTO.class).translationTo(MovieReviewCompliant.class);
        fromCreateToEntity.srcProperty("portalUserId").translatesTo("portalUser.id");
        fromCreateToEntity.srcProperty("movieId").translatesTo("movie.id");
        fromCreateToEntity.srcProperty("movieReviewId").translatesTo("movieReview.id");
        fromCreateToEntity.srcProperty("moderatorId").translatesTo("moderator.id");

        Configuration.Translation fromPatchToEntity =
                c.beanOfClass(MovieReviewCompliantPatchDTO.class).translationTo(MovieReviewCompliant.class);
        fromPatchToEntity.srcProperty("portalUserId").translatesTo("portalUser.id");
        fromPatchToEntity.srcProperty("movieId").translatesTo("movie.id");
        fromPatchToEntity.srcProperty("movieReviewId").translatesTo("movieReview.id");
        fromPatchToEntity.srcProperty("moderatorId").translatesTo("moderator.id");
        c.beanOfClass(MovieReviewCompliantPatchDTO.class)
                .translationTo(MovieReviewCompliant.class).mapOnlyNotNullProperties();
    }

    private void configureForMovieReviewFeedback(Configuration c) {
        Configuration.Translation t =
                c.beanOfClass(MovieReviewFeedback.class).translationTo(MovieReviewFeedbackReadDTO.class);
        t.srcProperty("portalUser.id").translatesTo("portalUserId");
        t.srcProperty("movie.id").translatesTo("movieId");
        t.srcProperty("movieReview.id").translatesTo("movieReviewId");

        Configuration.Translation fromCreateToEntity =
                c.beanOfClass(MovieReviewFeedbackCreateDTO.class).translationTo(MovieReviewFeedback.class);
        fromCreateToEntity.srcProperty("portalUserId").translatesTo("portalUser.id");
        fromCreateToEntity.srcProperty("movieId").translatesTo("movie.id");
        fromCreateToEntity.srcProperty("movieReviewId").translatesTo("movieReview.id");

        Configuration.Translation fromPatchToEntity =
                c.beanOfClass(MovieReviewFeedbackPatchDTO.class).translationTo(MovieReviewFeedback.class);
        fromPatchToEntity.srcProperty("portalUserId").translatesTo("portalUser.id");
        fromPatchToEntity.srcProperty("movieId").translatesTo("movie.id");
        fromPatchToEntity.srcProperty("movieReviewId").translatesTo("movieReview.id");
        c.beanOfClass(MovieReviewFeedbackPatchDTO.class)
                .translationTo(MovieReviewFeedback.class).mapOnlyNotNullProperties();
    }

    private void configureForMovieSpoilerData(Configuration c) {
        Configuration.Translation t =
                c.beanOfClass(MovieSpoilerData.class).translationTo(MovieSpoilerDataReadDTO.class);
        t.srcProperty("movieReview.id").translatesTo("movieReviewId");

        Configuration.Translation fromCreateToEntity =
                c.beanOfClass(MovieSpoilerDataCreateDTO.class).translationTo(MovieSpoilerData.class);
        fromCreateToEntity.srcProperty("movieReviewId").translatesTo("movieReview.id");

        Configuration.Translation fromPatchToEntity =
                c.beanOfClass(MovieSpoilerDataPatchDTO.class).translationTo(MovieSpoilerData.class);
        fromPatchToEntity.srcProperty("movieReviewId").translatesTo("movieReview.id");
        c.beanOfClass(MovieSpoilerDataPatchDTO.class).translationTo(MovieSpoilerData.class).mapOnlyNotNullProperties();
    }

    private void configureForMovieVote(Configuration c) {
        Configuration.Translation t = c.beanOfClass(MovieVote.class).translationTo(MovieVoteReadDTO.class);
        t.srcProperty("portalUser.id").translatesTo("portalUserId");
        t.srcProperty("movie.id").translatesTo("movieId");

        Configuration.Translation fromCreateToEntity =
                c.beanOfClass(MovieVoteCreateDTO.class).translationTo(MovieVote.class);
        fromCreateToEntity.srcProperty("portalUserId").translatesTo("portalUser.id");
        fromCreateToEntity.srcProperty("movieId").translatesTo("movie.id");

        Configuration.Translation fromPatchToEntity =
                c.beanOfClass(MovieVotePatchDTO.class).translationTo(MovieVote.class);
        fromPatchToEntity.srcProperty("portalUserId").translatesTo("portalUser.id");
        fromPatchToEntity.srcProperty("movieId").translatesTo("movie.id");
        c.beanOfClass(MovieVotePatchDTO.class).translationTo(MovieVote.class).mapOnlyNotNullProperties();
    }

    private void configureForNews(Configuration c) {
        Configuration.Translation t = c.beanOfClass(News.class).translationTo(NewsReadDTO.class);
        t.srcProperty("publisher.id").translatesTo("publisherId");

        Configuration.Translation fromCreateToEntity =
                c.beanOfClass(NewsCreateDTO.class).translationTo(News.class);
        fromCreateToEntity.srcProperty("publisherId").translatesTo("publisher.id");

        Configuration.Translation fromPatchToEntity =
                c.beanOfClass(NewsPatchDTO.class).translationTo(News.class);
        fromPatchToEntity.srcProperty("publisherId").translatesTo("publisher.id");
        c.beanOfClass(NewsPatchDTO.class).translationTo(News.class).mapOnlyNotNullProperties();
    }

    private void configureForNewsUserReview(Configuration c) {
        Configuration.Translation t = c.beanOfClass(NewsUserReview.class).translationTo(NewsUserReviewReadDTO.class);
        t.srcProperty("portalUser.id").translatesTo("portalUserId");
        t.srcProperty("news.id").translatesTo("newsId");
        t.srcProperty("moderator.id").translatesTo("moderatorId");

        Configuration.Translation fromCreateToEntity =
                c.beanOfClass(NewsUserReviewCreateDTO.class).translationTo(NewsUserReview.class);
        fromCreateToEntity.srcProperty("portalUserId").translatesTo("portalUser.id");
        fromCreateToEntity.srcProperty("newsId").translatesTo("news.id");
        fromCreateToEntity.srcProperty("moderatorId").translatesTo("moderator.id");

        Configuration.Translation fromPatchToEntity =
                c.beanOfClass(NewsUserReviewPatchDTO.class).translationTo(NewsUserReview.class);
        fromPatchToEntity.srcProperty("portalUserId").translatesTo("portalUser.id");
        fromPatchToEntity.srcProperty("newsId").translatesTo("news.id");
        fromPatchToEntity.srcProperty("moderatorId").translatesTo("moderator.id");
        c.beanOfClass(NewsUserReviewPatchDTO.class).translationTo(NewsUserReview.class).mapOnlyNotNullProperties();
    }

    private void configureForNewsUserReviewNote(Configuration c) {
        Configuration.Translation t =
                c.beanOfClass(NewsUserReviewNote.class).translationTo(NewsUserReviewNoteReadDTO.class);
        t.srcProperty("moderator.id").translatesTo("moderatorId");
        t.srcProperty("newsUserReview.id").translatesTo("newsUserReviewId");
        t.srcProperty("news.id").translatesTo("newsId");

        Configuration.Translation fromCreateToEntity =
                c.beanOfClass(NewsUserReviewNoteCreateDTO.class).translationTo(NewsUserReviewNote.class);
        fromCreateToEntity.srcProperty("newsUserReviewId").translatesTo("newsUserReview.id");
        fromCreateToEntity.srcProperty("moderatorId").translatesTo("moderator.id");
        fromCreateToEntity.srcProperty("newsId").translatesTo("news.id");

        Configuration.Translation fromPatchToEntity =
                c.beanOfClass(NewsUserReviewNotePatchDTO.class).translationTo(NewsUserReviewNote.class);
        fromPatchToEntity.srcProperty("newsUserReviewId").translatesTo("newsUserReview.id");
        fromPatchToEntity.srcProperty("moderatorId").translatesTo("moderator.id");
        fromPatchToEntity.srcProperty("newsId").translatesTo("news.id");
        c.beanOfClass(NewsUserReviewNotePatchDTO.class)
                .translationTo(NewsUserReviewNote.class).mapOnlyNotNullProperties();
    }

    private void configureForPerson(Configuration c) {
        c.beanOfClass(PersonPatchDTO.class).translationTo(Person.class).mapOnlyNotNullProperties();
    }

    private void configureForPortalUser(Configuration c) {
        Configuration.Translation t =
                c.beanOfClass(PortalUser.class).translationTo(PortalUserReadDTO.class);
        t.srcProperty("userType.id").translatesTo("userTypeId");

        Configuration.Translation fromCreateToEntity =
                c.beanOfClass(PortalUserCreateDTO.class).translationTo(PortalUser.class);
        fromCreateToEntity.srcProperty("userTypeId").translatesTo("userType.id");

        Configuration.Translation fromPatchToEntity =
                c.beanOfClass(PortalUserPatchDTO.class).translationTo(PortalUser.class);
        fromPatchToEntity.srcProperty("userTypeId").translatesTo("userType.id");
        c.beanOfClass(PortalUserPatchDTO.class).translationTo(PortalUser.class).mapOnlyNotNullProperties();
    }

    private void configureForReleaseDetail(Configuration c) {
        Configuration.Translation t = c.beanOfClass(ReleaseDetail.class).translationTo(ReleaseDetailReadDTO.class);
        t.srcProperty("movie.id").translatesTo("movieId");
        t.srcProperty("country.id").translatesTo("countryId");

        Configuration.Translation fromCreateToEntity =
                c.beanOfClass(ReleaseDetailCreateDTO.class).translationTo(ReleaseDetail.class);
        fromCreateToEntity.srcProperty("movieId").translatesTo("movie.id");
        fromCreateToEntity.srcProperty("countryId").translatesTo("country.id");

        Configuration.Translation fromPatchToEntity =
                c.beanOfClass(ReleaseDetailPatchDTO.class).translationTo(ReleaseDetail.class);
        fromPatchToEntity.srcProperty("movieId").translatesTo("movie.id");
        fromPatchToEntity.srcProperty("countryId").translatesTo("country.id");
        c.beanOfClass(ReleaseDetailPatchDTO.class).translationTo(ReleaseDetail.class).mapOnlyNotNullProperties();
    }

    private void configureForRole(Configuration c) {
        Configuration.Translation t = c.beanOfClass(Role.class).translationTo(RoleReadDTO.class);
        t.srcProperty("movie.id").translatesTo("movieId");
        t.srcProperty("person.id").translatesTo("personId");

        Configuration.Translation fromCreateToEntity =
                c.beanOfClass(RoleCreateDTO.class).translationTo(Role.class);
        fromCreateToEntity.srcProperty("movieId").translatesTo("movie.id");
        fromCreateToEntity.srcProperty("personId").translatesTo("person.id");

        Configuration.Translation fromPatchToEntity =
                c.beanOfClass(RolePatchDTO.class).translationTo(Role.class);
        fromPatchToEntity.srcProperty("movieId").translatesTo("movie.id");
        fromPatchToEntity.srcProperty("personId").translatesTo("person.id");
        c.beanOfClass(RolePatchDTO.class).translationTo(Role.class).mapOnlyNotNullProperties();
    }

    private void configureForRoleReview(Configuration c) {
        Configuration.Translation t = c.beanOfClass(RoleReview.class).translationTo(RoleReviewReadDTO.class);
        t.srcProperty("portalUser.id").translatesTo("portalUserId");
        t.srcProperty("role.id").translatesTo("roleId");
        t.srcProperty("moderator.id").translatesTo("moderatorId");

        Configuration.Translation fromCreateToEntity =
                c.beanOfClass(RoleReviewCreateDTO.class).translationTo(RoleReview.class);
        fromCreateToEntity.srcProperty("portalUserId").translatesTo("portalUser.id");
        fromCreateToEntity.srcProperty("roleId").translatesTo("role.id");
        fromCreateToEntity.srcProperty("moderatorId").translatesTo("moderator.id");

        Configuration.Translation fromPatchToEntity =
                c.beanOfClass(RoleReviewPatchDTO.class).translationTo(RoleReview.class);
        fromPatchToEntity.srcProperty("portalUserId").translatesTo("portalUser.id");
        fromPatchToEntity.srcProperty("roleId").translatesTo("role.id");
        fromPatchToEntity.srcProperty("moderatorId").translatesTo("moderator.id");
        c.beanOfClass(RoleReviewPatchDTO.class).translationTo(RoleReview.class).mapOnlyNotNullProperties();
    }

    private void configureForRoleReviewCompliant(Configuration c) {
        Configuration.Translation t =
                c.beanOfClass(RoleReviewCompliant.class).translationTo(RoleReviewCompliantReadDTO.class);
        t.srcProperty("portalUser.id").translatesTo("portalUserId");
        t.srcProperty("role.id").translatesTo("roleId");
        t.srcProperty("roleReview.id").translatesTo("roleReviewId");
        t.srcProperty("moderator.id").translatesTo("moderatorId");

        Configuration.Translation fromCreateToEntity =
                c.beanOfClass(RoleReviewCompliantCreateDTO.class).translationTo(RoleReviewCompliant.class);
        fromCreateToEntity.srcProperty("portalUserId").translatesTo("portalUser.id");
        fromCreateToEntity.srcProperty("roleId").translatesTo("role.id");
        fromCreateToEntity.srcProperty("roleReviewId").translatesTo("roleReview.id");
        fromCreateToEntity.srcProperty("moderatorId").translatesTo("moderator.id");

        Configuration.Translation fromPatchToEntity =
                c.beanOfClass(RoleReviewCompliantPatchDTO.class).translationTo(RoleReviewCompliant.class);
        fromPatchToEntity.srcProperty("portalUserId").translatesTo("portalUser.id");
        fromPatchToEntity.srcProperty("roleId").translatesTo("role.id");
        fromPatchToEntity.srcProperty("roleReviewId").translatesTo("roleReview.id");
        fromPatchToEntity.srcProperty("moderatorId").translatesTo("moderator.id");
        c.beanOfClass(RoleReviewCompliantPatchDTO.class)
                .translationTo(RoleReviewCompliant.class).mapOnlyNotNullProperties();
    }

    private void configureForRoleReviewFeedback(Configuration c) {
        Configuration.Translation t =
                c.beanOfClass(RoleReviewFeedback.class).translationTo(RoleReviewFeedbackReadDTO.class);
        t.srcProperty("portalUser.id").translatesTo("portalUserId");
        t.srcProperty("role.id").translatesTo("roleId");
        t.srcProperty("roleReview.id").translatesTo("roleReviewId");

        Configuration.Translation fromCreateToEntity =
                c.beanOfClass(RoleReviewFeedbackCreateDTO.class).translationTo(RoleReviewFeedback.class);
        fromCreateToEntity.srcProperty("portalUserId").translatesTo("portalUser.id");
        fromCreateToEntity.srcProperty("roleId").translatesTo("role.id");
        fromCreateToEntity.srcProperty("roleReviewId").translatesTo("roleReview.id");

        Configuration.Translation fromPatchToEntity =
                c.beanOfClass(RoleReviewFeedbackPatchDTO.class).translationTo(RoleReviewFeedback.class);
        fromPatchToEntity.srcProperty("portalUserId").translatesTo("portalUser.id");
        fromPatchToEntity.srcProperty("roleId").translatesTo("role.id");
        fromPatchToEntity.srcProperty("roleReviewId").translatesTo("roleReview.id");
        c.beanOfClass(RoleReviewFeedbackPatchDTO.class)
                .translationTo(RoleReviewFeedback.class).mapOnlyNotNullProperties();
    }

    private void configureForRoleSpoilerData(Configuration c) {
        Configuration.Translation t = c.beanOfClass(RoleSpoilerData.class).translationTo(RoleSpoilerDataReadDTO.class);
        t.srcProperty("roleReview.id").translatesTo("roleReviewId");

        Configuration.Translation fromCreateToEntity =
                c.beanOfClass(RoleSpoilerDataCreateDTO.class).translationTo(RoleSpoilerData.class);
        fromCreateToEntity.srcProperty("roleReviewId").translatesTo("roleReview.id");

        Configuration.Translation fromPatchToEntity =
                c.beanOfClass(RoleSpoilerDataPatchDTO.class).translationTo(RoleSpoilerData.class);
        fromPatchToEntity.srcProperty("roleReviewId").translatesTo("roleReview.id");
        c.beanOfClass(RoleSpoilerDataPatchDTO.class).translationTo(RoleSpoilerData.class).mapOnlyNotNullProperties();
    }

    private void configureForRoleVote(Configuration c) {
        Configuration.Translation t = c.beanOfClass(RoleVote.class).translationTo(RoleVoteReadDTO.class);
        t.srcProperty("portalUser.id").translatesTo("portalUserId");
        t.srcProperty("role.id").translatesTo("roleId");

        Configuration.Translation fromCreateToEntity =
                c.beanOfClass(RoleVoteCreateDTO.class).translationTo(RoleVote.class);
        fromCreateToEntity.srcProperty("portalUserId").translatesTo("portalUser.id");
        fromCreateToEntity.srcProperty("roleId").translatesTo("role.id");

        Configuration.Translation fromPatchToEntity =
                c.beanOfClass(RoleVotePatchDTO.class).translationTo(RoleVote.class);
        fromPatchToEntity.srcProperty("portalUserId").translatesTo("portalUser.id");
        fromPatchToEntity.srcProperty("roleId").translatesTo("role.id");
        c.beanOfClass(RoleVotePatchDTO.class).translationTo(RoleVote.class).mapOnlyNotNullProperties();
    }

    private void configureForUserGrant(Configuration c) {
        Configuration.Translation t = c.beanOfClass(UserGrant.class).translationTo(UserGrantReadDTO.class);
        t.srcProperty("userType.id").translatesTo("userTypeId");
        t.srcProperty("grantedBy.id").translatesTo("grantedById");

        Configuration.Translation fromCreateToEntity =
                c.beanOfClass(UserGrantCreateDTO.class).translationTo(UserGrant.class);
        fromCreateToEntity.srcProperty("userTypeId").translatesTo("userType.id");
        fromCreateToEntity.srcProperty("grantedById").translatesTo("grantedBy.id");

        Configuration.Translation fromPatchToEntity =
                c.beanOfClass(UserGrantPatchDTO.class).translationTo(UserGrant.class);
        fromPatchToEntity.srcProperty("userTypeId").translatesTo("userType.id");
        fromPatchToEntity.srcProperty("grantedById").translatesTo("grantedBy.id");
        c.beanOfClass(UserGrantPatchDTO.class).translationTo(UserGrant.class).mapOnlyNotNullProperties();
    }

    private void configureForUserType(Configuration c) {
        Configuration.Translation t = c.beanOfClass(UserType.class).translationTo(UserTypeReadDTO.class);
        t.srcProperty("portalUser.id").translatesTo("portalUserId");

        Configuration.Translation fromCreateToEntity =
                c.beanOfClass(UserTypeCreateDTO.class).translationTo(UserType.class);
        fromCreateToEntity.srcProperty("portalUserId").translatesTo("portalUser.id");

        Configuration.Translation fromPatchToEntity =
                c.beanOfClass(UserTypePatchDTO.class).translationTo(UserType.class);
        fromPatchToEntity.srcProperty("portalUserId").translatesTo("portalUser.id");
        c.beanOfClass(UserTypePatchDTO.class).translationTo(UserType.class).mapOnlyNotNullProperties();
    }

    private void configureForUserTypoRequest(Configuration c) {
        Configuration.Translation t = c.beanOfClass(UserTypoRequest.class).translationTo(UserTypoRequestReadDTO.class);
        t.srcProperty("requester.id").translatesTo("requesterId");
        t.srcProperty("moderator.id").translatesTo("moderatorId");
        t.srcProperty("news.id").translatesTo("newsId");
        t.srcProperty("movie.id").translatesTo("movieId");
        t.srcProperty("role.id").translatesTo("roleId");

        Configuration.Translation fromCreateToEntity =
                c.beanOfClass(UserTypoRequestCreateDTO.class).translationTo(UserTypoRequest.class);
        fromCreateToEntity.srcProperty("requesterId").translatesTo("requester.id");
        fromCreateToEntity.srcProperty("newsId").translatesTo("news.id");
        fromCreateToEntity.srcProperty("movieId").translatesTo("movie.id");
        fromCreateToEntity.srcProperty("roleId").translatesTo("role.id");

        c.beanOfClass(UserTypoRequestPatchDTO.class).translationTo(UserTypoRequest.class).mapOnlyNotNullProperties();
    }

    private void configureForVisit(Configuration c) {
        Configuration.Translation t = c.beanOfClass(Visit.class).translationTo(VisitReadDTO.class);
        t.srcProperty("portalUser.id").translatesTo("portalUserId");

        Configuration.Translation fromCreateToEntity =
                c.beanOfClass(VisitCreateDTO.class).translationTo(Visit.class);
        fromCreateToEntity.srcProperty("portalUserId").translatesTo("portalUser.id");

        Configuration.Translation fromPatchToEntity =
                c.beanOfClass(VisitPatchDTO.class).translationTo(Visit.class);
        fromPatchToEntity.srcProperty("portalUserId").translatesTo("portalUser.id");
        c.beanOfClass(VisitPatchDTO.class).translationTo(Visit.class).mapOnlyNotNullProperties();
    }

    public <T> T translate(Object srcObject, Class<T> targetClass) {
        try {
            return objectTranslator.translate(srcObject, targetClass);
        }
        catch (TranslationException e) {
            log.warn(e.getMessage());
            throw (RuntimeException) e.getCause();
        }
    }

    public <T> void map(Object srcObject, Object destObject) {
        try {
            objectTranslator.mapBean(srcObject, destObject);
        }
        catch (TranslationException e) {
            log.warn(e.getMessage());
            throw (RuntimeException) e.getCause();
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

    public void updateEntity(NewsUserReviewPutDTO put, NewsUserReview newsUserReview) {
        if (put.getPortalUserId() != null) {
            newsUserReview.setPortalUser(repositoryHelper.getReferenceIfExists(PortalUser.class,
                    put.getPortalUserId()));
        }
        if (put.getNewsId() != null) {
            newsUserReview.setNews(repositoryHelper.getReferenceIfExists(News.class, put.getNewsId()));
        }
        if (put.getModeratorTypoReviewStatusType() != null) {
            newsUserReview.setModeratorTypoReviewStatusType(put.getModeratorTypoReviewStatusType());
        }
        if (put.getModeratorId() != null) {
            newsUserReview.setModerator(repositoryHelper.getReferenceIfExists(PortalUser.class, put.getModeratorId()));
        } else {
            newsUserReview.setModerator(null);
        }
    }

    public void updateEntity(NewsUserReviewNotePutDTO put, NewsUserReviewNote newsUserReviewNote) {
        if (put.getModeratorId() != null) {
            newsUserReviewNote.setModerator(repositoryHelper.getReferenceIfExists(PortalUser.class,
                    put.getModeratorId()));
        } else {
            newsUserReviewNote.setModerator(null);
        }
        if (put.getStartIndex() != null) {
            newsUserReviewNote.setStartIndex(put.getStartIndex());
        }
        if (put.getEndIndex() != null) {
            newsUserReviewNote.setEndIndex(put.getEndIndex());
        }
        newsUserReviewNote.setProposedText(put.getProposedText());
        if (put.getModeratorTypoReviewStatusType() != null) {
            newsUserReviewNote.setModeratorTypoReviewStatusType(put.getModeratorTypoReviewStatusType());
        }
        if (put.getNewsUserReviewId() != null) {
            newsUserReviewNote.setNewsUserReview(repositoryHelper.getReferenceIfExists(NewsUserReview.class,
                    put.getNewsUserReviewId()));
        }
        if (put.getNewsId() != null) {
            newsUserReviewNote.setNews(repositoryHelper.getReferenceIfExists(News.class, put.getNewsId()));
        }
    }

    public void updateEntity(UserTypoRequestPutDTO put, UserTypoRequest userTypoRequest) {
        if (put.getModeratorId() != null) {
            userTypoRequest.setModerator(repositoryHelper.getReferenceIfExists(PortalUser.class,
                    put.getModeratorId()));
        }
        if (put.getApprovedText() != null) {
            userTypoRequest.setApprovedText(put.getApprovedText());
        }
        if (put.getModeratorTypoReviewStatusType() != null) {
            userTypoRequest.setModeratorTypoReviewStatusType(put.getModeratorTypoReviewStatusType());
        }
        if (put.getFixAppliedDate() != null) {
            userTypoRequest.setFixAppliedDate(put.getFixAppliedDate());
        }
    }
}
