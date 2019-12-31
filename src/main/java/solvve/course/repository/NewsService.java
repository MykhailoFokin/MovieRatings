package solvve.course.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import solvve.course.domain.News;
import solvve.course.dto.NewsCreateDTO;
import solvve.course.dto.NewsReadDTO;
import solvve.course.exception.EntityNotFoundException;

import java.util.UUID;

@Service
public class NewsService {

    @Autowired
    private NewsRepository newsRepository;

    @Transactional(readOnly = true)
    public NewsReadDTO getNews(UUID id) {
        News news = newsRepository.findById(id).orElseThrow(() -> {
            throw new EntityNotFoundException(News.class, id);
        });
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
}
