package solvve.course.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import solvve.course.domain.ReleaseDetail;
import solvve.course.dto.ReleaseDetailCreateDTO;
import solvve.course.dto.ReleaseDetailPatchDTO;
import solvve.course.dto.ReleaseDetailReadDTO;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.repository.ReleaseDetailRepository;

import java.util.UUID;

@Service
public class ReleaseDetailService {

    @Autowired
    private ReleaseDetailRepository releaseDetailRepository;

    @Transactional(readOnly = true)
    public ReleaseDetailReadDTO getReleaseDetails(UUID id) {
        ReleaseDetail releaseDetail = getReleaseDetailsRequired(id);
        return toRead(releaseDetail);
    }

    private ReleaseDetailReadDTO toRead(ReleaseDetail releaseDetail) {
        ReleaseDetailReadDTO dto = new ReleaseDetailReadDTO();
        dto.setId(releaseDetail.getId());
        dto.setMovieId(releaseDetail.getMovieId());
        dto.setReleaseDate(releaseDetail.getReleaseDate());
        dto.setCountryId(releaseDetail.getCountryId());
        return dto;
    }

    public ReleaseDetailReadDTO createReleaseDetails(ReleaseDetailCreateDTO create) {
        ReleaseDetail releaseDetail = new ReleaseDetail();
        releaseDetail.setMovieId(create.getMovieId());
        releaseDetail.setReleaseDate(create.getReleaseDate());
        releaseDetail.setCountryId(create.getCountryId());

        releaseDetail = releaseDetailRepository.save(releaseDetail);
        return toRead(releaseDetail);
    }

    public ReleaseDetailReadDTO patchReleaseDetails(UUID id, ReleaseDetailPatchDTO patch) {
        ReleaseDetail releaseDetail = getReleaseDetailsRequired(id);

        if (patch.getMovieId()!=null) {
            releaseDetail.setMovieId(patch.getMovieId());
        }
        if (patch.getReleaseDate()!=null) {
            releaseDetail.setReleaseDate(patch.getReleaseDate());
        }
        if (patch.getCountryId()!=null) {
            releaseDetail.setCountryId(patch.getCountryId());
        }
        releaseDetail = releaseDetailRepository.save(releaseDetail);
        return toRead(releaseDetail);
    }

    private ReleaseDetail getReleaseDetailsRequired(UUID id) {
        return releaseDetailRepository.findById(id).orElseThrow(() -> {
            throw new EntityNotFoundException(ReleaseDetail.class, id);
        });
    }

    public void deleteReleaseDetails(UUID id) {
        releaseDetailRepository.delete(getReleaseDetailsRequired(id));
    }
}
