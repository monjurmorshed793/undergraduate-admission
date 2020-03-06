package org.ums.service.extended;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.ums.repository.FacultyRepository;
import org.ums.security.SecurityUtils;
import org.ums.service.FacultyService;
import org.ums.service.dto.FacultyDTO;
import org.ums.service.mapper.FacultyMapper;

import java.time.LocalDate;

@Service
@Transactional
public class FacultyExtendedService extends FacultyService {
    public FacultyExtendedService(FacultyRepository facultyRepository, FacultyMapper facultyMapper) {
        super(facultyRepository, facultyMapper);
    }

    @Override
    public FacultyDTO save(FacultyDTO facultyDTO) {
        facultyDTO.setCreatedOn(facultyDTO.getCreatedOn()==null? LocalDate.now(): facultyDTO.getCreatedOn());
        facultyDTO.setModifiedOn(LocalDate.now());
        facultyDTO.setModifiedBy(SecurityUtils.getCurrentUserLogin().get());
        return super.save(facultyDTO);
    }
}
