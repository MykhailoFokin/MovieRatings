package solvve.course.repository;

import org.bitbucket.brunneng.qb.JpaQueryBuilder;
import org.bitbucket.brunneng.qb.SpringQueryBuilderUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import solvve.course.domain.Movie;
import solvve.course.dto.MovieFilter;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class MovieRepositoryCustomImpl implements MovieRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<Movie> findByFilter(MovieFilter filter, Pageable pageable) {
        JpaQueryBuilder qb = new JpaQueryBuilder(entityManager);
        qb.append("select m from Movie m");
        if (filter.getGenres() != null) {
            qb.append(" join Genre g on g.movie.id = m.id");
        }
        if (filter.getCompanyTypes() != null
                || filter.getCompanyType() != null
                || filter.getCompanyName() != null) {
            qb.append(" join m.movieProdCompanies mpc ");
        }
        if (filter.getCompanyName() != null) {
            qb.append(" join mpc.companyDetails cd");
        }
        if (filter.getLanguage() != null || filter.getLanguages() != null) {
            qb.append(" join m.movieProdLanguages l");
        }
        qb.append(" where 1=1");
        qb.append(" and m.title = :title", filter.getTitle());
        qb.append(" and m.year = :year", filter.getYear());
        qb.append(" and g.name in (:movieGenreType)", filter.getGenres());
        qb.appendLike(" and m.description like :v", filter.getDescription());
        qb.appendLike(" and m.camera like :v", filter.getCamera());
        qb.append(" and mpc.movieProductionType = :movieProductionType", filter.getCompanyType());
        qb.append(" and mpc.movieProductionType in (:movieProductionTypes)", filter.getCompanyTypes());
        qb.append(" and cd.name in (:companyName)", filter.getCompanyName());
        qb.appendLike(" and m.soundMix like :v", filter.getSoundMix());
        qb.appendLike(" and m.colour like :v", filter.getColour());
        qb.appendLike(" and m.aspectRatio like :v", filter.getAspectRatio());
        qb.appendLike(" and m.laboratory like :v", filter.getLaboratory());
        qb.append(" and m.isPublished = :isPublished", filter.getIsPublished());
        qb.append(" and l.name in (:languages)", filter.getLanguages());
        qb.append(" and l.name = :language", filter.getLanguage());
        qb.append(" and m.year in (:years)", filter.getYears());
        qb.append(" and m.title in (:titles)", filter.getTitles());

        return SpringQueryBuilderUtils.loadPage(qb, pageable, "id");
    }
}
