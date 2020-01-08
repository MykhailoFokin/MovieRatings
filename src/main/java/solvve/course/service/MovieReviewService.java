package solvve.course.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import solvve.course.domain.MovieReview;
import solvve.course.dto.MovieReviewCreateDTO;
import solvve.course.dto.MovieReviewReadDTO;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.repository.MovieReviewRepository;

import java.util.UUID;

@Service
public class MovieReviewService {

    @Autowired
    private MovieReviewRepository movieReviewRepository;

    @Transactional(readOnly = true)
    public MovieReviewReadDTO getMovieReview(UUID id) {
        MovieReview movieReview = movieReviewRepository.findById(id).orElseThrow(() -> {
            throw new EntityNotFoundException(MovieReview.class, id);
        });
        return toRead(movieReview);
    }

    private MovieReviewReadDTO toRead(MovieReview movieReview) {
        MovieReviewReadDTO dto = new MovieReviewReadDTO();
        dto.setId(movieReview.getId());
        dto.setMovieId(movieReview.getMovieId());
        dto.setUserId(movieReview.getUserId());
        dto.setTextReview(movieReview.getTextReview());
        dto.setModeratedStatus(movieReview.getModeratedStatus());
        dto.setModeratorId(movieReview.getModeratorId());
        return dto;
    }

    public MovieReviewReadDTO createMovieReview(MovieReviewCreateDTO create) {
        MovieReview movieReview = new MovieReview();
        movieReview.setMovieId(create.getMovieId());
        movieReview.setUserId(create.getUserId());
        movieReview.setTextReview(create.getTextReview());
        movieReview.setModeratedStatus(create.getModeratedStatus());
        movieReview.setModeratorId(create.getModeratorId());

        movieReview = movieReviewRepository.save(movieReview);
        return toRead(movieReview);
    }
}
