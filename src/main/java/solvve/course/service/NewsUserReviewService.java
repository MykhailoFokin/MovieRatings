package solvve.course.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import solvve.course.domain.NewsUserReview;
import solvve.course.dto.NewsUserReviewCreateDTO;
import solvve.course.dto.NewsUserReviewPatchDTO;
import solvve.course.dto.NewsUserReviewPutDTO;
import solvve.course.dto.NewsUserReviewReadDTO;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.repository.NewsUserReviewRepository;

import java.util.UUID;

@Service
public class NewsUserReviewService {

    @Autowired
    private NewsUserReviewRepository newsUserReviewRepository;

    @Autowired
    private TranslationService translationService;

    @Transactional(readOnly = true)
    public NewsUserReviewReadDTO getNewsUserReview(UUID id) {
        NewsUserReview newsUserReview = getNewsUserReviewsRequired(id);
        return translationService.translate(newsUserReview, NewsUserReviewReadDTO.class);
    }

    public NewsUserReviewReadDTO createNewsUserReview(NewsUserReviewCreateDTO create) {
        NewsUserReview newsUserReview = translationService.translate(create, NewsUserReview.class);

        newsUserReview = newsUserReviewRepository.save(newsUserReview);
        return translationService.translate(newsUserReview, NewsUserReviewReadDTO.class);
    }

    public NewsUserReviewReadDTO patchNewsUserReview(UUID id, NewsUserReviewPatchDTO patch) {
        NewsUserReview newsUserReview = getNewsUserReviewsRequired(id);

        translationService.map(patch, newsUserReview);

        newsUserReview = newsUserReviewRepository.save(newsUserReview);
        return translationService.translate(newsUserReview, NewsUserReviewReadDTO.class);
    }

    public void deleteNewsUserReview(UUID id) {
        newsUserReviewRepository.delete(getNewsUserReviewsRequired(id));
    }

    public NewsUserReviewReadDTO updateNewsUserReview(UUID id, NewsUserReviewPutDTO put) {
        NewsUserReview newsUserReview = getNewsUserReviewsRequired(id);

        translationService.updateEntity(put, newsUserReview);

        newsUserReview = newsUserReviewRepository.save(newsUserReview);
        return translationService.translate(newsUserReview, NewsUserReviewReadDTO.class);
    }

    private NewsUserReview getNewsUserReviewsRequired(UUID id) {
        return newsUserReviewRepository.findById(id).orElseThrow(() -> {
            throw new EntityNotFoundException(NewsUserReview.class, id);
        });
    }
}
