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

import org.ums.domain.Program;
import org.ums.domain.*; // for static metamodels
import org.ums.repository.ProgramRepository;
import org.ums.service.dto.ProgramCriteria;
import org.ums.service.dto.ProgramDTO;
import org.ums.service.mapper.ProgramMapper;

/**
 * Service for executing complex queries for {@link Program} entities in the database.
 * The main input is a {@link ProgramCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ProgramDTO} or a {@link Page} of {@link ProgramDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ProgramQueryService extends QueryService<Program> {

    private final Logger log = LoggerFactory.getLogger(ProgramQueryService.class);

    private final ProgramRepository programRepository;

    private final ProgramMapper programMapper;

    public ProgramQueryService(ProgramRepository programRepository, ProgramMapper programMapper) {
        this.programRepository = programRepository;
        this.programMapper = programMapper;
    }

    /**
     * Return a {@link List} of {@link ProgramDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ProgramDTO> findByCriteria(ProgramCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Program> specification = createSpecification(criteria);
        return programMapper.toDto(programRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ProgramDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ProgramDTO> findByCriteria(ProgramCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Program> specification = createSpecification(criteria);
        return programRepository.findAll(specification, page)
            .map(programMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ProgramCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Program> specification = createSpecification(criteria);
        return programRepository.count(specification);
    }

    /**
     * Function to convert {@link ProgramCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Program> createSpecification(ProgramCriteria criteria) {
        Specification<Program> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Program_.id));
            }
            if (criteria.getProgramId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getProgramId(), Program_.programId));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Program_.name));
            }
            if (criteria.getCreatedOn() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedOn(), Program_.createdOn));
            }
            if (criteria.getModifiedOn() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getModifiedOn(), Program_.modifiedOn));
            }
            if (criteria.getModifiedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getModifiedBy(), Program_.modifiedBy));
            }
        }
        return specification;
    }
}
