package solvve.course.dto;

import lombok.Data;
import solvve.course.domain.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
public class MovieReadExtendedDTO {

    private UUID id;

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

    private List<Country> movieProdCountries = new ArrayList<>();

    private List<Crew> crews = new ArrayList<>();

    private List<MovieReview> movieReview = new ArrayList<>();

    private List<MovieReviewCompliant> movieReviewCompliants = new ArrayList<>();

    private List<MovieReviewFeedback> movieReviewFeedbacks = new ArrayList<>();

    private List<MovieVote> movieVotes = new ArrayList<>();

    private List<ReleaseDetail> releaseDetails = new ArrayList<>();

    private List<Language> languages = new ArrayList<>();

    private List<MovieCompany> movieCompanies = new ArrayList<>();

    private Instant createdAt;

    private Instant updatedAt;

    private Double averageRating;
}
