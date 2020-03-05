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

import org.ums.domain.AdmissionDesignation;
import org.ums.domain.*; // for static metamodels
import org.ums.repository.AdmissionDesignationRepository;
import org.ums.service.dto.AdmissionDesignationCriteria;
import org.ums.service.dto.AdmissionDesignationDTO;
import org.ums.service.mapper.AdmissionDesignationMapper;

/**
 * Service for executing complex queries for {@link AdmissionDesignation} entities in the database.
 * The main input is a {@link AdmissionDesignationCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link AdmissionDesignationDTO} or a {@link Page} of {@link AdmissionDesignationDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AdmissionDesignationQueryService extends QueryService<AdmissionDesignation> {

    private final Logger log = LoggerFactory.getLogger(AdmissionDesignationQueryService.class);

    private final AdmissionDesignationRepository admissionDesignationRepository;

    private final AdmissionDesignationMapper admissionDesignationMapper;

    public AdmissionDesignationQueryService(AdmissionDesignationRepository admissionDesignationRepository, AdmissionDesignationMapper admissionDesignationMapper) {
        this.admissionDesignationRepository = admissionDesignationRepository;
        this.admissionDesignationMapper = admissionDesignationMapper;
    }

    /**
     * Return a {@link List} of {@link AdmissionDesignationDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<AdmissionDesignationDTO> findByCriteria(AdmissionDesignationCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<AdmissionDesignation> specification = createSpecification(criteria);
        return admissionDesignationMapper.toDto(admissionDesignationRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link AdmissionDesignationDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AdmissionDesignationDTO> findByCriteria(AdmissionDesignationCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<AdmissionDesignation> specification = createSpecification(criteria);
        return admissionDesignationRepository.findAll(specification, page)
            .map(admissionDesignationMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AdmissionDesignationCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<AdmissionDesignation> specification = createSpecification(criteria);
        return admissionDesignationRepository.count(specification);
    }

    /**
     * Function to convert {@link AdmissionDesignationCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<AdmissionDesignation> createSpecification(AdmissionDesignationCriteria criteria) {
        Specification<AdmissionDesignation> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), AdmissionDesignation_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), AdmissionDesignation_.name));
            }
            if (criteria.getCreatedOn() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedOn(), AdmissionDesignation_.createdOn));
            }
            if (criteria.getModifiedOn() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getModifiedOn(), AdmissionDesignation_.modifiedOn));
            }
            if (criteria.getModifiedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getModifiedBy(), AdmissionDesignation_.modifiedBy));
            }
        }
        return specification;
    }
}
