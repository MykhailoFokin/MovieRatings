package solvve.course.dto;

import lombok.Data;
import solvve.course.domain.*;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
public class MovieReadExtendedDTO {

    private UUID Id;

    private String title;

    private Short year;

    private String description;

    private String soundMix;

    private String colour;

    private String aspectRatio;

    private String camera;

    private String laboratory;

    private String critique;

    private Boolean isPublished;

    private Set<Country> movieProdCountries = new HashSet<Country>();

    private Set<Crew> crews = new HashSet<Crew>();

    private Set<MovieReview> movieReview = new HashSet<MovieReview>();

    private Set<MovieReviewCompliant> movieReviewCompliants = new HashSet<MovieReviewCompliant>();

    private Set<MovieReviewFeedback> movieReviewFeedbacks = new HashSet<MovieReviewFeedback>();

    private Set<MovieVote> movieVotes = new HashSet<MovieVote>();

    private Set<ReleaseDetail> releaseDetails = new HashSet<ReleaseDetail>();

    private Set<Language> languages = new HashSet<Language>();

    private Set<MovieCompany> movieCompanies = new HashSet<MovieCompany>();
}
