package solvve.course.dto;

import lombok.Data;
import solvve.course.domain.*;

import java.util.Set;

@Data
public class MoviePatchDTO {

    private String title;

    private Short year;

    private String genres;

    private String description;

    private String companies;

    private String soundMix;

    private String colour;

    private String aspectRatio;

    private String camera;

    private String laboratory;

    private String languages;

    private String filmingLocations;

    private String critique;

    private Boolean isPublished;

    private Set<Country> movieProdCountries;

    private Set<Crew> crews;

    private Set<MovieReview> movieReview;

    private Set<MovieReviewCompliant> movieReviewCompliants;

    private Set<MovieReviewFeedback> movieReviewFeedbacks;

    private Set<MovieVote> movieVotes;

    private Set<ReleaseDetail> releaseDetails;
}