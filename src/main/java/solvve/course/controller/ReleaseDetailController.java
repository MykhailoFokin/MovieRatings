package solvve.course.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import solvve.course.controller.security.AdminOrContentManager;
import solvve.course.controller.security.PublicAccess;
import solvve.course.dto.ReleaseDetailCreateDTO;
import solvve.course.dto.ReleaseDetailPatchDTO;
import solvve.course.dto.ReleaseDetailPutDTO;
import solvve.course.dto.ReleaseDetailReadDTO;
import solvve.course.service.ReleaseDetailService;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/releasedetails")
public class ReleaseDetailController {

    @Autowired
    private ReleaseDetailService releaseDetailService;

    @PublicAccess
    @GetMapping("/{id}")
    public ReleaseDetailReadDTO getReleaseDetails(@PathVariable UUID id) {
        return releaseDetailService.getReleaseDetails(id);
    }

    @AdminOrContentManager
    @PostMapping
    public ReleaseDetailReadDTO createReleaseDetails(@RequestBody @Valid ReleaseDetailCreateDTO createDTO) {
        return releaseDetailService.createReleaseDetails(createDTO);
    }

    @AdminOrContentManager
    @PatchMapping("/{id}")
    public ReleaseDetailReadDTO patchReleaseDetails(@PathVariable UUID id,
                                                    @RequestBody ReleaseDetailPatchDTO patch) {
        return releaseDetailService.patchReleaseDetails(id, patch);
    }

    @AdminOrContentManager
    @DeleteMapping("/{id}")
    public void deleteReleaseDetails(@PathVariable UUID id) {
        releaseDetailService.deleteReleaseDetails(id);
    }

    @AdminOrContentManager
    @PutMapping("/{id}")
    public ReleaseDetailReadDTO putReleaseDetails(@PathVariable UUID id, @RequestBody @Valid ReleaseDetailPutDTO put) {
        return releaseDetailService.updateReleaseDetails(id, put);
    }
}
