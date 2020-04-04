package solvve.course.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import solvve.course.domain.NewsUserReview;
import solvve.course.dto.NewsUserReviewCreateDTO;
import solvve.course.dto.NewsUserReviewPatchDTO;
import solvve.course.dto.NewsUserReviewPutDTO;
import solvve.course.dto.NewsUserReviewReadDTO;
import solvve.course.repository.NewsUserReviewRepository;

import java.util.UUID;

@Service
public class NewsUserReviewService extends AbstractService {

    @Autowired
    private NewsUserReviewRepository newsUserReviewRepository;

    @Transactional(readOnly = true)
    public NewsUserReviewReadDTO getNewsUserReview(UUID id) {
        NewsUserReview newsUserReview = repositoryHelper.getByIdRequired(NewsUserReview.class, id);
        return translationService.translate(newsUserReview, NewsUserReviewReadDTO.class);
    }

    public NewsUserReviewReadDTO createNewsUserReview(NewsUserReviewCreateDTO create) {
        NewsUserReview newsUserReview = translationService.translate(create, NewsUserReview.class);

        newsUserReview = newsUserReviewRepository.save(newsUserReview);
        return translationService.translate(newsUserReview, NewsUserReviewReadDTO.class);
    }

    public NewsUserReviewReadDTO patchNewsUserReview(UUID id, NewsUserReviewPatchDTO patch) {
        NewsUserReview newsUserReview = repositoryHelper.getByIdRequired(NewsUserReview.class, id);

        translationService.map(patch, newsUserReview);

        newsUserReview = newsUserReviewRepository.save(newsUserReview);
        return translationService.translate(newsUserReview, NewsUserReviewReadDTO.class);
    }

    public void deleteNewsUserReview(UUID id) {
        newsUserReviewRepository.delete(repositoryHelper.getByIdRequired(NewsUserReview.class, id));
    }

    public NewsUserReviewReadDTO updateNewsUserReview(UUID id, NewsUserReviewPutDTO put) {
        NewsUserReview newsUserReview = repositoryHelper.getByIdRequired(NewsUserReview.class, id);

        translationService.updateEntity(put, newsUserReview);

        newsUserReview = newsUserReviewRepository.save(newsUserReview);
        return translationService.translate(newsUserReview, NewsUserReviewReadDTO.class);
    }
}
