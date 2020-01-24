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
        return translationService.toRead(releaseDetail);
    }

    public ReleaseDetailReadDTO createReleaseDetails(ReleaseDetailCreateDTO create) {
        ReleaseDetail releaseDetail = translationService.toEntity(create);

        releaseDetail = releaseDetailRepository.save(releaseDetail);
        return translationService.toRead(releaseDetail);
    }

    public ReleaseDetailReadDTO patchReleaseDetails(UUID id, ReleaseDetailPatchDTO patch) {
        ReleaseDetail releaseDetail = getReleaseDetailsRequired(id);

        translationService.patchEntity(patch, releaseDetail);

        releaseDetail = releaseDetailRepository.save(releaseDetail);
        return translationService.toRead(releaseDetail);
    }

    private ReleaseDetail getReleaseDetailsRequired(UUID id) {
        return releaseDetailRepository.findById(id).orElseThrow(() -> {
            throw new EntityNotFoundException(ReleaseDetail.class, id);
        });
    }

    public void deleteReleaseDetails(UUID id) {
        releaseDetailRepository.delete(getReleaseDetailsRequired(id));
    }

    public ReleaseDetailReadDTO putReleaseDetails(UUID id, ReleaseDetailPutDTO put) {
        ReleaseDetail releaseDetail = getReleaseDetailsRequired(id);

        translationService.putEntity(put, releaseDetail);

        releaseDetail = releaseDetailRepository.save(releaseDetail);
        return translationService.toRead(releaseDetail);
    }
}
