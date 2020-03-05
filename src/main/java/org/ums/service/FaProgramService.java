package org.ums.service;

import org.ums.domain.FaProgram;
import org.ums.repository.FaProgramRepository;
import org.ums.service.dto.FaProgramDTO;
import org.ums.service.mapper.FaProgramMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link FaProgram}.
 */
@Service
@Transactional
public class FaProgramService {

    private final Logger log = LoggerFactory.getLogger(FaProgramService.class);

    private final FaProgramRepository faProgramRepository;

    private final FaProgramMapper faProgramMapper;

    public FaProgramService(FaProgramRepository faProgramRepository, FaProgramMapper faProgramMapper) {
        this.faProgramRepository = faProgramRepository;
        this.faProgramMapper = faProgramMapper;
    }

    /**
     * Save a faProgram.
     *
     * @param faProgramDTO the entity to save.
     * @return the persisted entity.
     */
    public FaProgramDTO save(FaProgramDTO faProgramDTO) {
        log.debug("Request to save FaProgram : {}", faProgramDTO);
        FaProgram faProgram = faProgramMapper.toEntity(faProgramDTO);
        faProgram = faProgramRepository.save(faProgram);
        return faProgramMapper.toDto(faProgram);
    }

    /**
     * Get all the faPrograms.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<FaProgramDTO> findAll(Pageable pageable) {
        log.debug("Request to get all FaPrograms");
        return faProgramRepository.findAll(pageable)
            .map(faProgramMapper::toDto);
    }

    /**
     * Get one faProgram by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<FaProgramDTO> findOne(Long id) {
        log.debug("Request to get FaProgram : {}", id);
        return faProgramRepository.findById(id)
            .map(faProgramMapper::toDto);
    }

    /**
     * Delete the faProgram by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete FaProgram : {}", id);
        faProgramRepository.deleteById(id);
    }
}
