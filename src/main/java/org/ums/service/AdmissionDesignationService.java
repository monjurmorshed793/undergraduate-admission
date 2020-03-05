package org.ums.service;

import org.ums.domain.AdmissionDesignation;
import org.ums.repository.AdmissionDesignationRepository;
import org.ums.service.dto.AdmissionDesignationDTO;
import org.ums.service.mapper.AdmissionDesignationMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link AdmissionDesignation}.
 */
@Service
@Transactional
public class AdmissionDesignationService {

    private final Logger log = LoggerFactory.getLogger(AdmissionDesignationService.class);

    private final AdmissionDesignationRepository admissionDesignationRepository;

    private final AdmissionDesignationMapper admissionDesignationMapper;

    public AdmissionDesignationService(AdmissionDesignationRepository admissionDesignationRepository, AdmissionDesignationMapper admissionDesignationMapper) {
        this.admissionDesignationRepository = admissionDesignationRepository;
        this.admissionDesignationMapper = admissionDesignationMapper;
    }

    /**
     * Save a admissionDesignation.
     *
     * @param admissionDesignationDTO the entity to save.
     * @return the persisted entity.
     */
    public AdmissionDesignationDTO save(AdmissionDesignationDTO admissionDesignationDTO) {
        log.debug("Request to save AdmissionDesignation : {}", admissionDesignationDTO);
        AdmissionDesignation admissionDesignation = admissionDesignationMapper.toEntity(admissionDesignationDTO);
        admissionDesignation = admissionDesignationRepository.save(admissionDesignation);
        return admissionDesignationMapper.toDto(admissionDesignation);
    }

    /**
     * Get all the admissionDesignations.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<AdmissionDesignationDTO> findAll(Pageable pageable) {
        log.debug("Request to get all AdmissionDesignations");
        return admissionDesignationRepository.findAll(pageable)
            .map(admissionDesignationMapper::toDto);
    }

    /**
     * Get one admissionDesignation by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AdmissionDesignationDTO> findOne(Long id) {
        log.debug("Request to get AdmissionDesignation : {}", id);
        return admissionDesignationRepository.findById(id)
            .map(admissionDesignationMapper::toDto);
    }

    /**
     * Delete the admissionDesignation by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete AdmissionDesignation : {}", id);
        admissionDesignationRepository.deleteById(id);
    }
}
