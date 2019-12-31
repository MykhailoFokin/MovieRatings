package solvve.course.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import solvve.course.domain.ReleaseDetails;
import solvve.course.dto.ReleaseDetailsCreateDTO;
import solvve.course.dto.ReleaseDetailsReadDTO;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.repository.ReleaseDetailsRepository;

import java.util.UUID;

@Service
public class ReleaseDetailsService {

    @Autowired
    private ReleaseDetailsRepository releaseDetailsRepository;

    @Transactional(readOnly = true)
    public ReleaseDetailsReadDTO getReleaseDetails(UUID id) {
        ReleaseDetails releaseDetails = releaseDetailsRepository.findById(id).orElseThrow(() -> {
            throw new EntityNotFoundException(ReleaseDetails.class, id);
        });
        return toRead(releaseDetails);
    }

    private ReleaseDetailsReadDTO toRead(ReleaseDetails releaseDetails) {
        ReleaseDetailsReadDTO dto = new ReleaseDetailsReadDTO();
        dto.setId(releaseDetails.getId());
        dto.setMovieId(releaseDetails.getMovieId());
        dto.setReleaseDate(releaseDetails.getReleaseDate());
        dto.setCountryId(releaseDetails.getCountryId());
        return dto;
    }

    public ReleaseDetailsReadDTO createReleaseDetails(ReleaseDetailsCreateDTO create) {
        ReleaseDetails releaseDetails = new ReleaseDetails();
        releaseDetails.setMovieId(create.getMovieId());
        releaseDetails.setReleaseDate(create.getReleaseDate());
        releaseDetails.setCountryId(create.getCountryId());

        releaseDetails = releaseDetailsRepository.save(releaseDetails);
        return toRead(releaseDetails);
    }
}
