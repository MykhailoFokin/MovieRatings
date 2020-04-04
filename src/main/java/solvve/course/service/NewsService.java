package solvve.course.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import solvve.course.domain.News;
import solvve.course.dto.NewsCreateDTO;
import solvve.course.dto.NewsPatchDTO;
import solvve.course.dto.NewsPutDTO;
import solvve.course.dto.NewsReadDTO;
import solvve.course.repository.NewsRepository;

import java.util.UUID;

@Service
public class NewsService extends AbstractService {

    @Autowired
    private NewsRepository newsRepository;

    @Transactional(readOnly = true)
    public NewsReadDTO getNews(UUID id) {
        News news = repositoryHelper.getByIdRequired(News.class, id);
        return translationService.translate(news, NewsReadDTO.class);
    }

    public NewsReadDTO createNews(NewsCreateDTO create) {
        News news = translationService.translate(create, News.class);

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
}
