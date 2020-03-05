package org.ums.service;

import java.util.List;

import javax.persistence.criteria.JoinType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import org.ums.domain.TotalSeat;
import org.ums.domain.*; // for static metamodels
import org.ums.repository.TotalSeatRepository;
import org.ums.service.dto.TotalSeatCriteria;
import org.ums.service.dto.TotalSeatDTO;
import org.ums.service.mapper.TotalSeatMapper;

/**
 * Service for executing complex queries for {@link TotalSeat} entities in the database.
 * The main input is a {@link TotalSeatCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link TotalSeatDTO} or a {@link Page} of {@link TotalSeatDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TotalSeatQueryService extends QueryService<TotalSeat> {

    private final Logger log = LoggerFactory.getLogger(TotalSeatQueryService.class);

    private final TotalSeatRepository totalSeatRepository;

    private final TotalSeatMapper totalSeatMapper;

    public TotalSeatQueryService(TotalSeatRepository totalSeatRepository, TotalSeatMapper totalSeatMapper) {
        this.totalSeatRepository = totalSeatRepository;
        this.totalSeatMapper = totalSeatMapper;
    }

    /**
     * Return a {@link List} of {@link TotalSeatDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TotalSeatDTO> findByCriteria(TotalSeatCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<TotalSeat> specification = createSpecification(criteria);
        return totalSeatMapper.toDto(totalSeatRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link TotalSeatDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TotalSeatDTO> findByCriteria(TotalSeatCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<TotalSeat> specification = createSpecification(criteria);
        return totalSeatRepository.findAll(specification, page)
            .map(totalSeatMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TotalSeatCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<TotalSeat> specification = createSpecification(criteria);
        return totalSeatRepository.count(specification);
    }

    /**
     * Function to convert {@link TotalSeatCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<TotalSeat> createSpecification(TotalSeatCriteria criteria) {
        Specification<TotalSeat> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), TotalSeat_.id));
            }
            if (criteria.getSeat() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getSeat(), TotalSeat_.seat));
            }
            if (criteria.getCreatedOn() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedOn(), TotalSeat_.createdOn));
            }
            if (criteria.getModifiedOn() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getModifiedOn(), TotalSeat_.modifiedOn));
            }
            if (criteria.getModifiedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getModifiedBy(), TotalSeat_.modifiedBy));
            }
            if (criteria.getFacultyProgramId() != null) {
                specification = specification.and(buildSpecification(criteria.getFacultyProgramId(),
                    root -> root.join(TotalSeat_.facultyProgram, JoinType.LEFT).get(FaProgram_.id)));
            }
        }
        return specification;
    }
}
