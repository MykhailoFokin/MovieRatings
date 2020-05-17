package solvve.course.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import solvve.course.controller.documentation.ApiPageable;
import solvve.course.controller.security.Admin;
import solvve.course.controller.security.AdminOrContentManager;
import solvve.course.controller.validation.ControllerValidationUtil;
import solvve.course.dto.*;
import solvve.course.service.VisitService;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/visits")
public class VisitController {

    @Autowired
    private VisitService visitService;

    @AdminOrContentManager
    @GetMapping("/{id}")
    public VisitReadExtendedDTO getVisit(@PathVariable UUID id) {
        return visitService.getVisit(id);
    }

    @Admin
    @PostMapping
    public VisitReadDTO createVisit(@RequestBody @Valid VisitCreateDTO createDTO) {
        ControllerValidationUtil.validateLessThan(createDTO.getStartAt(), createDTO.getFinishAt(), "startAt",
                "finishAt");
        return visitService.createVisit(createDTO);
    }

    @Admin
    @PatchMapping("/{id}")
    public VisitReadDTO patchVisit(@PathVariable UUID id,
                                   @RequestBody VisitPatchDTO patch) {
        ControllerValidationUtil.validateLessThan(patch.getStartAt(), patch.getFinishAt(), "startAt",
                "finishAt");
        return visitService.patchVisit(id, patch);
    }

    @Admin
    @DeleteMapping("/{id}")
    public void deleteVisit(@PathVariable UUID id) {
        visitService.deleteVisit(id);
    }

    @Admin
    @PutMapping("/{id}")
    public VisitReadDTO putVisit(@PathVariable UUID id,
                                 @RequestBody @Valid VisitPutDTO put) {
        ControllerValidationUtil.validateLessThan(put.getStartAt(), put.getFinishAt(), "startAt",
                "finishAt");
        return visitService.updateVisit(id, put);
    }

    @ApiPageable
    @AdminOrContentManager
    @GetMapping
    public PageResult<VisitReadDTO> getVisits(VisitFilter filter, @ApiIgnore Pageable pageable) {
        return visitService.getVisits(filter, pageable);
    }
}
