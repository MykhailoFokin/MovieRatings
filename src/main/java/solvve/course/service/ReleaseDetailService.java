package solvve.course.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import solvve.course.domain.ReleaseDetail;
import solvve.course.dto.ReleaseDetailCreateDTO;
import solvve.course.dto.ReleaseDetailPatchDTO;
import solvve.course.dto.ReleaseDetailPutDTO;
import solvve.course.dto.ReleaseDetailReadDTO;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.repository.ReleaseDetailRepository;

import java.util.UUID;

@Service
public class ReleaseDetailService {

    @Autowired
    private ReleaseDetailRepository releaseDetailRepository;

    @Autowired
    private TranslationService translationService;

    @Transactional(readOnly = true)
    public ReleaseDetailReadDTO getReleaseDetails(UUID id) {
        ReleaseDetail releaseDetail = getReleaseDetailsRequired(id);
        return translationService.translate(releaseDetail, ReleaseDetailReadDTO.class);
    }

    public ReleaseDetailReadDTO createReleaseDetails(ReleaseDetailCreateDTO create) {
        ReleaseDetail releaseDetail = translationService.translate(create, ReleaseDetail.class);

        releaseDetail = releaseDetailRepository.save(releaseDetail);
        return translationService.translate(releaseDetail, ReleaseDetailReadDTO.class);
    }

    public ReleaseDetailReadDTO patchReleaseDetails(UUID id, ReleaseDetailPatchDTO patch) {
        ReleaseDetail releaseDetail = getReleaseDetailsRequired(id);

        translationService.map(patch, releaseDetail);

        releaseDetail = releaseDetailRepository.save(releaseDetail);
        return translationService.translate(releaseDetail, ReleaseDetailReadDTO.class);
    }

    private ReleaseDetail getReleaseDetailsRequired(UUID id) {
        return releaseDetailRepository.findById(id).orElseThrow(() -> {
            throw new EntityNotFoundException(ReleaseDetail.class, id);
        });
    }

    public void deleteReleaseDetails(UUID id) {
        releaseDetailRepository.delete(getReleaseDetailsRequired(id));
    }

    public ReleaseDetailReadDTO updateReleaseDetails(UUID id, ReleaseDetailPutDTO put) {
        ReleaseDetail releaseDetail = getReleaseDetailsRequired(id);

        translationService.updateEntity(put, releaseDetail);

        releaseDetail = releaseDetailRepository.save(releaseDetail);
        return translationService.translate(releaseDetail, ReleaseDetailReadDTO.class);
    }
}
