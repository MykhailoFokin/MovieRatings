package solvve.course.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import solvve.course.domain.News;
import solvve.course.dto.NewsCreateDTO;
import solvve.course.dto.NewsPatchDTO;
import solvve.course.dto.NewsReadDTO;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.repository.NewsRepository;

import java.util.UUID;

@Service
public class NewsService {

    @Autowired
    private NewsRepository newsRepository;

    @Transactional(readOnly = true)
    public NewsReadDTO getNews(UUID id) {
        News news = getNewsRequired(id);
        return toRead(news);
    }

    private NewsReadDTO toRead(News news) {
        NewsReadDTO dto = new NewsReadDTO();
        dto.setId(news.getId());
        dto.setUserId(news.getUserId());
        dto.setPublished(news.getPublished());
        dto.setTopic(news.getTopic());
        dto.setDescription(news.getDescription());
        return dto;
    }

    public NewsReadDTO createNews(NewsCreateDTO create) {
        News news = new News();
        news.setUserId(create.getUserId());
        news.setPublished(create.getPublished());
        news.setTopic(create.getTopic());
        news.setDescription(create.getDescription());

        news = newsRepository.save(news);
        return toRead(news);
    }

    public NewsReadDTO patchNews(UUID id, NewsPatchDTO patch) {
        News news = getNewsRequired(id);

        if (patch.getUserId()!=null) {
            news.setUserId(patch.getUserId());
        }
        if (patch.getPublished()!=null) {
            news.setPublished(patch.getPublished());
        }
        if (patch.getTopic()!=null) {
            news.setTopic(patch.getTopic());
        }
        if (patch.getDescription()!=null) {
            news.setDescription(patch.getDescription());
        }
        news = newsRepository.save(news);
        return toRead(news);
    }

    private News getNewsRequired(UUID id) {
        return newsRepository.findById(id).orElseThrow(() -> {
            throw new EntityNotFoundException(News.class, id);
        });
    }

    public void deleteNews(UUID id) {
        newsRepository.delete(getNewsRequired(id));
    }
}
