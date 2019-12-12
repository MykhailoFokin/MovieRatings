package solvve.course.domain;

import org.hibernate.annotations.GenericGenerator;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.UUID;
import lombok.Data;

@Entity
@Data
public class Movie {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    private String title;

    private Short year; // year of production

    private String genres; // type of genres

    private String release_dates; // different release dates depending on country

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
    private String sound_mix; // sound technologies used in movie
    private String colour; // optional for color scheme
    private String aspect_ratio; // examples: 1.43 : 1 (some scenes: 70mm IMAX); 1.90 : 1 (some scenes: Digital IMAX); 2.39 : 1;
    private String camera; // example: Arriflex 235, Panavision Primo Lenses; Arriflex 435, Panavision Primo Lenses; IMAX MSM 9802, Hasselblad Lenses (some scenes)
    private String laboratory; // post production companies, etc
    private String negative_format; // example: 35 mm; 65 mm;
    private String cinematographic_process; // example: Digital Intermediate (4K) (master format); Dolby Vision; IMAX (source format); Super 35 (source format);
    private String printed_film_format; // example: 35 mm (anamorphic) (Kodak Vision 2383); 70 mm (horizontal) (IMAX DMR blow-up) (Kodak Vision 2383); D-Cinema (also 3-D version);

    // Details
    private String countries; // production by
    private String languages; // original production language
    private String filming_locations; // many-to-many. addresses to movies.

    private String critique; // movie 1-8 critique 1-1(1-8) crew

}
