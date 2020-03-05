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

import org.ums.domain.Semester;
import org.ums.domain.*; // for static metamodels
import org.ums.repository.SemesterRepository;
import org.ums.service.dto.SemesterCriteria;
import org.ums.service.dto.SemesterDTO;
import org.ums.service.mapper.SemesterMapper;

/**
 * Service for executing complex queries for {@link Semester} entities in the database.
 * The main input is a {@link SemesterCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link SemesterDTO} or a {@link Page} of {@link SemesterDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SemesterQueryService extends QueryService<Semester> {

    private final Logger log = LoggerFactory.getLogger(SemesterQueryService.class);

    private final SemesterRepository semesterRepository;

    private final SemesterMapper semesterMapper;

    public SemesterQueryService(SemesterRepository semesterRepository, SemesterMapper semesterMapper) {
        this.semesterRepository = semesterRepository;
        this.semesterMapper = semesterMapper;
    }

    /**
     * Return a {@link List} of {@link SemesterDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<SemesterDTO> findByCriteria(SemesterCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Semester> specification = createSpecification(criteria);
        return semesterMapper.toDto(semesterRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link SemesterDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SemesterDTO> findByCriteria(SemesterCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Semester> specification = createSpecification(criteria);
        return semesterRepository.findAll(specification, page)
            .map(semesterMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SemesterCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Semester> specification = createSpecification(criteria);
        return semesterRepository.count(specification);
    }

    /**
     * Function to convert {@link SemesterCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Semester> createSpecification(SemesterCriteria criteria) {
        Specification<Semester> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Semester_.id));
            }
            if (criteria.getSemesterId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getSemesterId(), Semester_.semesterId));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Semester_.name));
            }
            if (criteria.getShortName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getShortName(), Semester_.shortName));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getStatus(), Semester_.status));
            }
            if (criteria.getStartDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getStartDate(), Semester_.startDate));
            }
            if (criteria.getEndDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getEndDate(), Semester_.endDate));
            }
            if (criteria.getCreatedOn() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedOn(), Semester_.createdOn));
            }
            if (criteria.getModifiedOn() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getModifiedOn(), Semester_.modifiedOn));
            }
            if (criteria.getModifiedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getModifiedBy(), Semester_.modifiedBy));
            }
        }
        return specification;
    }
}
