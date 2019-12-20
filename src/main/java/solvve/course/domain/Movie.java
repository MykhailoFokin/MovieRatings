package solvve.course.domain;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.UUID;

@Data
@Entity
public class Movie {

    @Id
    @GeneratedValue()
    private UUID id;

    private String title;

    private Short year; // year of production

    private String genres; // type of genres

    @Column(name = "releasedates")
    private String releaseDates; // different release dates depending on country

    private String description; // short movie description

    // Movie Crew (one entity for link between entity crew (its list of people) and movie itself
    // because movie related to crew as many to many we need entity between them
    // possible types:
    /*directors
    writers
    producers
    musicby
    cinematographyby
    operators
    specialeffectscrew
    othercrew*/

    private String crew;

    // Company Credits
    // Same as Crew. Entity Companies and Entity for linkage
    // possible types:
    /*Production Companies
    Distributors
    Special Effects
    Other Companies*/
    private String companies;

    // Technical Specifications
    // it will be dictionaries of appropriate information
    @Column(name = "soundmix")
    private String soundMix; // sound technologies used in movie
    private String colour; // optional for color scheme
    @Column(name = "aspectratio")
    private String aspectRatio; // examples: 1.43 : 1 (some scenes: 70mm IMAX); 1.90 : 1 (some scenes: Digital IMAX); 2.39 : 1;
    private String camera; // example: Arriflex 235, Panavision Primo Lenses; Arriflex 435, Panavision Primo Lenses; IMAX MSM 9802, Hasselblad Lenses (some scenes)
    private String laboratory; // post production companies, etc

    // Details
    private String countries; // production by
    private String languages; // original production language
    @Column(name = "filminglocations")
    private String filminglocations; // many-to-many. addresses to movies.

    private String critique; // movie 1-8 critique 1-1(1-8) crew

}
