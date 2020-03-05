package org.ums.service;

import org.ums.domain.TotalSeat;
import org.ums.repository.TotalSeatRepository;
import org.ums.service.dto.TotalSeatDTO;
import org.ums.service.mapper.TotalSeatMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link TotalSeat}.
 */
@Service
@Transactional
public class TotalSeatService {

    private final Logger log = LoggerFactory.getLogger(TotalSeatService.class);

    private final TotalSeatRepository totalSeatRepository;

    private final TotalSeatMapper totalSeatMapper;

    public TotalSeatService(TotalSeatRepository totalSeatRepository, TotalSeatMapper totalSeatMapper) {
        this.totalSeatRepository = totalSeatRepository;
        this.totalSeatMapper = totalSeatMapper;
    }

    /**
     * Save a totalSeat.
     *
     * @param totalSeatDTO the entity to save.
     * @return the persisted entity.
     */
    public TotalSeatDTO save(TotalSeatDTO totalSeatDTO) {
        log.debug("Request to save TotalSeat : {}", totalSeatDTO);
        TotalSeat totalSeat = totalSeatMapper.toEntity(totalSeatDTO);
        totalSeat = totalSeatRepository.save(totalSeat);
        return totalSeatMapper.toDto(totalSeat);
    }

    /**
     * Get all the totalSeats.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<TotalSeatDTO> findAll(Pageable pageable) {
        log.debug("Request to get all TotalSeats");
        return totalSeatRepository.findAll(pageable)
            .map(totalSeatMapper::toDto);
    }

    /**
     * Get one totalSeat by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TotalSeatDTO> findOne(Long id) {
        log.debug("Request to get TotalSeat : {}", id);
        return totalSeatRepository.findById(id)
            .map(totalSeatMapper::toDto);
    }

    /**
     * Delete the totalSeat by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete TotalSeat : {}", id);
        totalSeatRepository.deleteById(id);
    }
}
