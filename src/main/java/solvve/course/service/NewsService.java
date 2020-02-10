package solvve.course.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import solvve.course.domain.News;
import solvve.course.dto.NewsCreateDTO;
import solvve.course.dto.NewsPatchDTO;
import solvve.course.dto.NewsPutDTO;
import solvve.course.dto.NewsReadDTO;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.repository.NewsRepository;

import java.util.UUID;

@Service
public class NewsService {

    @Autowired
    private NewsRepository newsRepository;

    @Autowired
    private TranslationService translationService;

    @Transactional(readOnly = true)
    public NewsReadDTO getNews(UUID id) {
        News news = getNewsRequired(id);
        return translationService.toRead(news);
    }

    public NewsReadDTO createNews(NewsCreateDTO create) {
        News news = translationService.toEntity(create);

        news = newsRepository.save(news);
        return translationService.toRead(news);
    }

    public NewsReadDTO patchNews(UUID id, NewsPatchDTO patch) {
        News news = getNewsRequired(id);

        translationService.patchEntity(patch, news);

        news = newsRepository.save(news);
        return translationService.toRead(news);
    }

    private News getNewsRequired(UUID id) {
        return newsRepository.findById(id).orElseThrow(() -> {
            throw new EntityNotFoundException(News.class, id);
        });
    }

    public void deleteNews(UUID id) {
        newsRepository.delete(getNewsRequired(id));
    }

    public NewsReadDTO updateNews(UUID id, NewsPutDTO put) {
        News news = getNewsRequired(id);

        translationService.updateEntity(put, news);

        news = newsRepository.save(news);
        return translationService.toRead(news);
    }
}
