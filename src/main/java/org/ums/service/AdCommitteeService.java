package org.ums.service;

import org.ums.domain.AdCommittee;
import org.ums.repository.AdCommitteeRepository;
import org.ums.service.dto.AdCommitteeDTO;
import org.ums.service.mapper.AdCommitteeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link AdCommittee}.
 */
@Service
@Transactional
public class AdCommitteeService {

    private final Logger log = LoggerFactory.getLogger(AdCommitteeService.class);

    private final AdCommitteeRepository adCommitteeRepository;

    private final AdCommitteeMapper adCommitteeMapper;

    public AdCommitteeService(AdCommitteeRepository adCommitteeRepository, AdCommitteeMapper adCommitteeMapper) {
        this.adCommitteeRepository = adCommitteeRepository;
        this.adCommitteeMapper = adCommitteeMapper;
    }

    /**
     * Save a adCommittee.
     *
     * @param adCommitteeDTO the entity to save.
     * @return the persisted entity.
     */
    public AdCommitteeDTO save(AdCommitteeDTO adCommitteeDTO) {
        log.debug("Request to save AdCommittee : {}", adCommitteeDTO);
        AdCommittee adCommittee = adCommitteeMapper.toEntity(adCommitteeDTO);
        adCommittee = adCommitteeRepository.save(adCommittee);
        return adCommitteeMapper.toDto(adCommittee);
    }

    /**
     * Get all the adCommittees.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<AdCommitteeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all AdCommittees");
        return adCommitteeRepository.findAll(pageable)
            .map(adCommitteeMapper::toDto);
    }

    /**
     * Get one adCommittee by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AdCommitteeDTO> findOne(Long id) {
        log.debug("Request to get AdCommittee : {}", id);
        return adCommitteeRepository.findById(id)
            .map(adCommitteeMapper::toDto);
    }

    /**
     * Delete the adCommittee by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete AdCommittee : {}", id);
        adCommitteeRepository.deleteById(id);
    }
}
