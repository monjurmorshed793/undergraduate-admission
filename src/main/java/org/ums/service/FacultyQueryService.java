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

import org.ums.domain.Faculty;
import org.ums.domain.*; // for static metamodels
import org.ums.repository.FacultyRepository;
import org.ums.service.dto.FacultyCriteria;
import org.ums.service.dto.FacultyDTO;
import org.ums.service.mapper.FacultyMapper;

/**
 * Service for executing complex queries for {@link Faculty} entities in the database.
 * The main input is a {@link FacultyCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link FacultyDTO} or a {@link Page} of {@link FacultyDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class FacultyQueryService extends QueryService<Faculty> {

    private final Logger log = LoggerFactory.getLogger(FacultyQueryService.class);

    private final FacultyRepository facultyRepository;

    private final FacultyMapper facultyMapper;

    public FacultyQueryService(FacultyRepository facultyRepository, FacultyMapper facultyMapper) {
        this.facultyRepository = facultyRepository;
        this.facultyMapper = facultyMapper;
    }

    /**
     * Return a {@link List} of {@link FacultyDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<FacultyDTO> findByCriteria(FacultyCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Faculty> specification = createSpecification(criteria);
        return facultyMapper.toDto(facultyRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link FacultyDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<FacultyDTO> findByCriteria(FacultyCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Faculty> specification = createSpecification(criteria);
        return facultyRepository.findAll(specification, page)
            .map(facultyMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(FacultyCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Faculty> specification = createSpecification(criteria);
        return facultyRepository.count(specification);
    }

    /**
     * Function to convert {@link FacultyCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Faculty> createSpecification(FacultyCriteria criteria) {
        Specification<Faculty> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Faculty_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Faculty_.name));
            }
            if (criteria.getShortName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getShortName(), Faculty_.shortName));
            }
            if (criteria.getCreatedOn() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedOn(), Faculty_.createdOn));
            }
            if (criteria.getModifiedOn() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getModifiedOn(), Faculty_.modifiedOn));
            }
            if (criteria.getModifiedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getModifiedBy(), Faculty_.modifiedBy));
            }
        }
        return specification;
    }
}
