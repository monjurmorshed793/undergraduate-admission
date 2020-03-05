package org.ums.service;

import org.ums.domain.Semester;
import org.ums.repository.SemesterRepository;
import org.ums.service.dto.SemesterDTO;
import org.ums.service.mapper.SemesterMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Semester}.
 */
@Service
@Transactional
public class SemesterService {

    private final Logger log = LoggerFactory.getLogger(SemesterService.class);

    private final SemesterRepository semesterRepository;

    private final SemesterMapper semesterMapper;

    public SemesterService(SemesterRepository semesterRepository, SemesterMapper semesterMapper) {
        this.semesterRepository = semesterRepository;
        this.semesterMapper = semesterMapper;
    }

    /**
     * Save a semester.
     *
     * @param semesterDTO the entity to save.
     * @return the persisted entity.
     */
    public SemesterDTO save(SemesterDTO semesterDTO) {
        log.debug("Request to save Semester : {}", semesterDTO);
        Semester semester = semesterMapper.toEntity(semesterDTO);
        semester = semesterRepository.save(semester);
        return semesterMapper.toDto(semester);
    }

    /**
     * Get all the semesters.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<SemesterDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Semesters");
        return semesterRepository.findAll(pageable)
            .map(semesterMapper::toDto);
    }

    /**
     * Get one semester by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SemesterDTO> findOne(Long id) {
        log.debug("Request to get Semester : {}", id);
        return semesterRepository.findById(id)
            .map(semesterMapper::toDto);
    }

    /**
     * Delete the semester by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Semester : {}", id);
        semesterRepository.deleteById(id);
    }
}
