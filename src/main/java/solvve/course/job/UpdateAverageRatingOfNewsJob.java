package solvve.course.job;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import solvve.course.repository.NewsRepository;
import solvve.course.service.NewsService;

@Slf4j
@Component
public class UpdateAverageRatingOfNewsJob {

    @Autowired
    private NewsRepository newsRepository;

    @Autowired
    private NewsService newsService;

    @Transactional(readOnly = true)
    @Scheduled(cron = "${update.average.rating.of.news.job.cron}")
    public void updateAverageRatingOfNews() {
        log.info("Job started");

        newsRepository.getIdsOfNews().forEach(newsId -> {
            try {
                newsService.updateAverageRatingOfNews(newsId);
            }
            catch (Exception e) {
                log.error("Failed to update average rating for news: {}", newsId, e);
            }
        });

        log.info("Job finished");
    }
}
