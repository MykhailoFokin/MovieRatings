package solvve.course.repository;

import solvve.course.domain.Movie;
import solvve.course.dto.MovieFilter;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

public class MovieRepositoryCustomImpl implements MovieRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Movie> findByFilter(MovieFilter filter) {
        StringBuilder sb = new StringBuilder();
        sb.append("select m from Movie m");
        if (filter.getGenres() != null) {
            sb.append(" join Genre g on g.movie.id = m.id");
        }
        if (filter.getCompanyTypes() != null
                || filter.getCompanyType() != null
                || filter.getCompanyName() != null) {
            sb.append(" join m.movieProdCompanies mpc ");
        }
        if (filter.getCompanyName() != null) {
            sb.append(" join mpc.companyDetails cd");
        }
        if (filter.getLanguage() != null || filter.getLanguages() != null) {
            sb.append(" join m.movieProdLanguages l");
        }
        sb.append(" where 1=1");
        if (filter.getTitle() != null) {
            sb.append(" and m.title = :title");
        }
        if (filter.getYear() != null) {
            sb.append(" and m.year = :year");
        }
        if (filter.getGenres() != null) {
            sb.append(" and g.name in (:movieGenreType)");
        }
        if (filter.getDescription() != null) {
            sb.append(" and m.description like '%'||:description||'%'");
        }
        if (filter.getCamera() != null) {
            sb.append(" and m.camera like '%'||:camera||'%'");
        }
        if (filter.getCompanyType() != null) {
            sb.append(" and mpc.movieProductionType = :movieProductionType");
        }
        if (filter.getCompanyTypes() != null) {
            sb.append(" and mpc.movieProductionType in (:movieProductionTypes)");
        }
        if (filter.getCompanyName() != null) {
            sb.append(" and cd.name in (:companyName)");
        }
        if (filter.getSoundMix() != null) {
            sb.append(" and m.soundMix like '%'||:soundMix||'%'");
        }
        if (filter.getColour() != null) {
            sb.append(" and m.colour like '%'||:colour||'%'");
        }
        if (filter.getAspectRatio() != null) {
            sb.append(" and m.aspectRatio like '%'||:aspectRatio||'%'");
        }
        if (filter.getLaboratory() != null) {
            sb.append(" and m.laboratory like '%'||:laboratory||'%'");
        }
        if (filter.getIsPublished() != null) {
            sb.append(" and m.isPublished = :isPublished");
        }
        if (filter.getLanguages() != null) {
            sb.append(" and l.name in (:languages)");
        }
        if (filter.getLanguage() != null) {
            sb.append(" and l.name = :language");
        }
        if (filter.getYears() != null) {
            sb.append(" and m.year in (:years)");
        }
        if (filter.getTitles() != null) {
            sb.append(" and m.title in (:titles)");
        }
        TypedQuery<Movie> query = entityManager.createQuery(sb.toString(), Movie.class);

        if (filter.getTitle() != null) {
            query.setParameter("title", filter.getTitle());
        }
        if (filter.getYear() != null) {
            query.setParameter("year", filter.getYear());
        }
        if (filter.getGenres() != null) {
            query.setParameter("movieGenreType", filter.getGenres());
        }
        if (filter.getDescription() != null) {
            query.setParameter("description", filter.getDescription());
        }
        if (filter.getCamera() != null) {
            query.setParameter("camera", filter.getCamera());
        }
        if (filter.getCompanyType() != null) {
            query.setParameter("movieProductionType", filter.getCompanyType());
        }
        if (filter.getCompanyTypes() != null) {
            query.setParameter("movieProductionTypes", filter.getCompanyTypes());
        }
        if (filter.getCompanyName() != null) {
            query.setParameter("companyName", filter.getCompanyName());
        }
        if (filter.getSoundMix() != null) {
            query.setParameter("soundMix", filter.getSoundMix());
        }
        if (filter.getColour() != null) {
            query.setParameter("colour", filter.getColour());
            sb.append(" and m.colour like '%:colour%'");
        }
        if (filter.getAspectRatio() != null) {
            query.setParameter("aspectRatio", filter.getAspectRatio());
        }
        if (filter.getLaboratory() != null) {
            query.setParameter("laboratory", filter.getLaboratory());
        }
        if (filter.getIsPublished() != null) {
            query.setParameter("isPublished", filter.getIsPublished());
        }
        if (filter.getLanguages() != null) {
            query.setParameter("languages", filter.getLanguages());
        }
        if (filter.getLanguage() != null) {
            query.setParameter("language", filter.getLanguage());
        }
        if (filter.getYears() != null) {
            query.setParameter("years", filter.getYears());
        }
        if (filter.getTitles() != null) {
            query.setParameter("titles", filter.getTitles());
        }

        return query.getResultList();
    }
}
