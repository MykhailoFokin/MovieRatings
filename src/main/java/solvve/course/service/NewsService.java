package solvve.course.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import solvve.course.domain.News;
import solvve.course.dto.NewsCreateDTO;
import solvve.course.dto.NewsPatchDTO;
import solvve.course.dto.NewsPutDTO;
import solvve.course.dto.NewsReadDTO;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.repository.NewsFeedbackRepository;
import solvve.course.repository.NewsRepository;

import java.util.UUID;

@Slf4j
@Service
public class NewsService extends AbstractService {

    @Autowired
    private NewsRepository newsRepository;

    @Autowired
    private NewsFeedbackRepository newsFeedbackRepository;

    @Transactional(readOnly = true)
    public NewsReadDTO getNews(UUID id) {
        News news = repositoryHelper.getByIdRequired(News.class, id);
        return translationService.translate(news, NewsReadDTO.class);
    }

    public NewsReadDTO createNews(NewsCreateDTO create) {
        News news = translationService.translate(create, News.class);

        news.setLikesCount(0);
        news.setNewsRating(0.0);

        news = newsRepository.save(news);
        return translationService.translate(news, NewsReadDTO.class);
    }

    public NewsReadDTO patchNews(UUID id, NewsPatchDTO patch) {
        News news = repositoryHelper.getByIdRequired(News.class, id);

        translationService.map(patch, news);

        news = newsRepository.save(news);
        return translationService.translate(news, NewsReadDTO.class);
    }

    public void deleteNews(UUID id) {
        newsRepository.delete(repositoryHelper.getByIdRequired(News.class, id));
    }

    public NewsReadDTO updateNews(UUID id, NewsPutDTO put) {
        News news = repositoryHelper.getByIdRequired(News.class, id);

        translationService.updateEntity(put, news);

        news = newsRepository.save(news);
        return translationService.translate(news, NewsReadDTO.class);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updateAverageRatingOfNews(UUID newsId) {
        Integer totalFeedbacksCount = newsFeedbackRepository.countByNewsId(newsId);
        Integer likesCount = newsFeedbackRepository.countByNewsIdAndIsLikedTrue(newsId);
        Double averageRating = likesCount.doubleValue() / totalFeedbacksCount;
        News news = newsRepository.findById(newsId).orElseThrow(
                () -> new EntityNotFoundException(News.class, newsId));

        log.info("Setting new average rating of news: {}. Old value: {}, new value: {}", newsId,
                news.getNewsRating(), averageRating);
        news.setNewsRating(averageRating);
        log.info("Setting new count of likes for news: {}. Old value: {}, new value: {}", newsId,
                news.getLikesCount(), likesCount);
        news.setLikesCount(likesCount);
        news.setDislikesCount(totalFeedbacksCount - likesCount);
        newsRepository.save(news);
    }
}
