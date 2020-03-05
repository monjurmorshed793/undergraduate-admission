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

import org.ums.domain.AdCommittee;
import org.ums.domain.*; // for static metamodels
import org.ums.repository.AdCommitteeRepository;
import org.ums.service.dto.AdCommitteeCriteria;
import org.ums.service.dto.AdCommitteeDTO;
import org.ums.service.mapper.AdCommitteeMapper;

/**
 * Service for executing complex queries for {@link AdCommittee} entities in the database.
 * The main input is a {@link AdCommitteeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link AdCommitteeDTO} or a {@link Page} of {@link AdCommitteeDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AdCommitteeQueryService extends QueryService<AdCommittee> {

    private final Logger log = LoggerFactory.getLogger(AdCommitteeQueryService.class);

    private final AdCommitteeRepository adCommitteeRepository;

    private final AdCommitteeMapper adCommitteeMapper;

    public AdCommitteeQueryService(AdCommitteeRepository adCommitteeRepository, AdCommitteeMapper adCommitteeMapper) {
        this.adCommitteeRepository = adCommitteeRepository;
        this.adCommitteeMapper = adCommitteeMapper;
    }

    /**
     * Return a {@link List} of {@link AdCommitteeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<AdCommitteeDTO> findByCriteria(AdCommitteeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<AdCommittee> specification = createSpecification(criteria);
        return adCommitteeMapper.toDto(adCommitteeRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link AdCommitteeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AdCommitteeDTO> findByCriteria(AdCommitteeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<AdCommittee> specification = createSpecification(criteria);
        return adCommitteeRepository.findAll(specification, page)
            .map(adCommitteeMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AdCommitteeCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<AdCommittee> specification = createSpecification(criteria);
        return adCommitteeRepository.count(specification);
    }

    /**
     * Function to convert {@link AdCommitteeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<AdCommittee> createSpecification(AdCommitteeCriteria criteria) {
        Specification<AdCommittee> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), AdCommittee_.id));
            }
            if (criteria.getCreatedOn() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedOn(), AdCommittee_.createdOn));
            }
            if (criteria.getModifiedOn() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getModifiedOn(), AdCommittee_.modifiedOn));
            }
            if (criteria.getModifiedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getModifiedBy(), AdCommittee_.modifiedBy));
            }
            if (criteria.getSemesterId() != null) {
                specification = specification.and(buildSpecification(criteria.getSemesterId(),
                    root -> root.join(AdCommittee_.semester, JoinType.LEFT).get(Semester_.id)));
            }
            if (criteria.getFacultyId() != null) {
                specification = specification.and(buildSpecification(criteria.getFacultyId(),
                    root -> root.join(AdCommittee_.faculty, JoinType.LEFT).get(Faculty_.id)));
            }
            if (criteria.getDesignationId() != null) {
                specification = specification.and(buildSpecification(criteria.getDesignationId(),
                    root -> root.join(AdCommittee_.designation, JoinType.LEFT).get(AdmissionDesignation_.id)));
            }
            if (criteria.getUserId() != null) {
                specification = specification.and(buildSpecification(criteria.getUserId(),
                    root -> root.join(AdCommittee_.user, JoinType.LEFT).get(User_.id)));
            }
        }
        return specification;
    }
}
