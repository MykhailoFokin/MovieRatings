package solvve.course.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import solvve.course.domain.Master;
import solvve.course.dto.MasterCreateDTO;
import solvve.course.dto.MasterPatchDTO;
import solvve.course.dto.MasterReadDTO;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.repository.MasterRepository;

import java.util.UUID;

@Service
public class MasterService {

    @Autowired
    private MasterRepository masterRepository;

    @Transactional(readOnly = true)
    public MasterReadDTO getMaster(UUID id) {
        Master master = getMasterRequired(id);
        return toRead(master);
    }

    private MasterReadDTO toRead(Master master) {
        MasterReadDTO dto = new MasterReadDTO();
        dto.setId(master.getId());
        dto.setName(master.getName());
        dto.setPhone(master.getPhone());
        dto.setAbout(master.getAbout());
        return dto;
    }

    public MasterReadDTO createMaster(MasterCreateDTO create) {
        Master master = new Master();
        master.setName(create.getName());
        master.setPhone(create.getPhone());
        master.setAbout(create.getAbout());

        master = masterRepository.save(master);
        return toRead(master);
    }

    public MasterReadDTO patchMaster(UUID id, MasterPatchDTO patch) {
        Master master = getMasterRequired(id);

        if (patch.getName()!=null) {
            master.setName(patch.getName());
        }
        if (patch.getPhone()!=null) {
            master.setPhone(patch.getPhone());
        }
        if (patch.getAbout()!=null) {
            master.setAbout(patch.getAbout());
        }
        master = masterRepository.save(master);
        return toRead(master);
    }

    private Master getMasterRequired(UUID id) {
        return masterRepository.findById(id).orElseThrow(() -> {
            throw new EntityNotFoundException(Master.class, id);
        });
    }

    public void deleteMaster(UUID id) {
        masterRepository.delete(getMasterRequired(id));
    }
}
