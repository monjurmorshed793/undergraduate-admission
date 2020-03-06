package org.ums.service.extended;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.ums.repository.SemesterRepository;
import org.ums.security.SecurityUtils;
import org.ums.service.SemesterService;
import org.ums.service.dto.SemesterDTO;
import org.ums.service.mapper.SemesterMapper;

import java.time.LocalDate;

@Service
@Transactional
public class SemesterExtendedService extends SemesterService {

    public SemesterExtendedService(SemesterRepository semesterRepository, SemesterMapper semesterMapper) {
        super(semesterRepository, semesterMapper);
    }

    @Override
    public SemesterDTO save(SemesterDTO semesterDTO) {
        semesterDTO.setCreatedOn(semesterDTO.getCreatedOn()==null? LocalDate.now(): semesterDTO.getCreatedOn());
        semesterDTO.setModifiedOn(LocalDate.now());
        semesterDTO.setModifiedBy(SecurityUtils.getCurrentUserLogin().get());
        return super.save(semesterDTO);
    }
}
