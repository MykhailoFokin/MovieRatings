package solvve.course.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import solvve.course.domain.ReleaseDetail;
import solvve.course.dto.ReleaseDetailCreateDTO;
import solvve.course.dto.ReleaseDetailPatchDTO;
import solvve.course.dto.ReleaseDetailPutDTO;
import solvve.course.dto.ReleaseDetailReadDTO;
import solvve.course.repository.ReleaseDetailRepository;

import java.util.UUID;

@Service
public class ReleaseDetailService extends AbstractService {

    @Autowired
    private ReleaseDetailRepository releaseDetailRepository;

    @Transactional(readOnly = true)
    public ReleaseDetailReadDTO getReleaseDetails(UUID id) {
        ReleaseDetail releaseDetail = repositoryHelper.getByIdRequired(ReleaseDetail.class, id);
        return translationService.translate(releaseDetail, ReleaseDetailReadDTO.class);
    }

    public ReleaseDetailReadDTO createReleaseDetails(ReleaseDetailCreateDTO create) {
        ReleaseDetail releaseDetail = translationService.translate(create, ReleaseDetail.class);

        releaseDetail = releaseDetailRepository.save(releaseDetail);
        return translationService.translate(releaseDetail, ReleaseDetailReadDTO.class);
    }

    public ReleaseDetailReadDTO patchReleaseDetails(UUID id, ReleaseDetailPatchDTO patch) {
        ReleaseDetail releaseDetail = repositoryHelper.getByIdRequired(ReleaseDetail.class, id);

        translationService.map(patch, releaseDetail);

        releaseDetail = releaseDetailRepository.save(releaseDetail);
        return translationService.translate(releaseDetail, ReleaseDetailReadDTO.class);
    }

    public void deleteReleaseDetails(UUID id) {
        releaseDetailRepository.delete(repositoryHelper.getByIdRequired(ReleaseDetail.class, id));
    }

    public ReleaseDetailReadDTO updateReleaseDetails(UUID id, ReleaseDetailPutDTO put) {
        ReleaseDetail releaseDetail = repositoryHelper.getByIdRequired(ReleaseDetail.class, id);

        translationService.updateEntity(put, releaseDetail);

        releaseDetail = releaseDetailRepository.save(releaseDetail);
        return translationService.translate(releaseDetail, ReleaseDetailReadDTO.class);
    }
}
