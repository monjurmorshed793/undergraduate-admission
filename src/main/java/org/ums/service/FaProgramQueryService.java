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

import org.ums.domain.FaProgram;
import org.ums.domain.*; // for static metamodels
import org.ums.repository.FaProgramRepository;
import org.ums.service.dto.FaProgramCriteria;
import org.ums.service.dto.FaProgramDTO;
import org.ums.service.mapper.FaProgramMapper;

/**
 * Service for executing complex queries for {@link FaProgram} entities in the database.
 * The main input is a {@link FaProgramCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link FaProgramDTO} or a {@link Page} of {@link FaProgramDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class FaProgramQueryService extends QueryService<FaProgram> {

    private final Logger log = LoggerFactory.getLogger(FaProgramQueryService.class);

    private final FaProgramRepository faProgramRepository;

    private final FaProgramMapper faProgramMapper;

    public FaProgramQueryService(FaProgramRepository faProgramRepository, FaProgramMapper faProgramMapper) {
        this.faProgramRepository = faProgramRepository;
        this.faProgramMapper = faProgramMapper;
    }

    /**
     * Return a {@link List} of {@link FaProgramDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<FaProgramDTO> findByCriteria(FaProgramCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<FaProgram> specification = createSpecification(criteria);
        return faProgramMapper.toDto(faProgramRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link FaProgramDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<FaProgramDTO> findByCriteria(FaProgramCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<FaProgram> specification = createSpecification(criteria);
        return faProgramRepository.findAll(specification, page)
            .map(faProgramMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(FaProgramCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<FaProgram> specification = createSpecification(criteria);
        return faProgramRepository.count(specification);
    }

    /**
     * Function to convert {@link FaProgramCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<FaProgram> createSpecification(FaProgramCriteria criteria) {
        Specification<FaProgram> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), FaProgram_.id));
            }
            if (criteria.getCreatedOn() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedOn(), FaProgram_.createdOn));
            }
            if (criteria.getModifiedOn() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getModifiedOn(), FaProgram_.modifiedOn));
            }
            if (criteria.getModifiedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getModifiedBy(), FaProgram_.modifiedBy));
            }
            if (criteria.getSemesterId() != null) {
                specification = specification.and(buildSpecification(criteria.getSemesterId(),
                    root -> root.join(FaProgram_.semester, JoinType.LEFT).get(Semester_.id)));
            }
            if (criteria.getFacultyId() != null) {
                specification = specification.and(buildSpecification(criteria.getFacultyId(),
                    root -> root.join(FaProgram_.faculty, JoinType.LEFT).get(Faculty_.id)));
            }
            if (criteria.getProgramId() != null) {
                specification = specification.and(buildSpecification(criteria.getProgramId(),
                    root -> root.join(FaProgram_.program, JoinType.LEFT).get(Program_.id)));
            }
        }
        return specification;
    }
}
