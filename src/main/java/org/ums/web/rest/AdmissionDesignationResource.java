package org.ums.web.rest;

import org.ums.service.AdmissionDesignationService;
import org.ums.web.rest.errors.BadRequestAlertException;
import org.ums.service.dto.AdmissionDesignationDTO;
import org.ums.service.dto.AdmissionDesignationCriteria;
import org.ums.service.AdmissionDesignationQueryService;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link org.ums.domain.AdmissionDesignation}.
 */
@RestController
@RequestMapping("/api")
public class AdmissionDesignationResource {

    private final Logger log = LoggerFactory.getLogger(AdmissionDesignationResource.class);

    private static final String ENTITY_NAME = "admissionDesignation";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AdmissionDesignationService admissionDesignationService;

    private final AdmissionDesignationQueryService admissionDesignationQueryService;

    public AdmissionDesignationResource(AdmissionDesignationService admissionDesignationService, AdmissionDesignationQueryService admissionDesignationQueryService) {
        this.admissionDesignationService = admissionDesignationService;
        this.admissionDesignationQueryService = admissionDesignationQueryService;
    }

    /**
     * {@code POST  /admission-designations} : Create a new admissionDesignation.
     *
     * @param admissionDesignationDTO the admissionDesignationDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new admissionDesignationDTO, or with status {@code 400 (Bad Request)} if the admissionDesignation has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/admission-designations")
    public ResponseEntity<AdmissionDesignationDTO> createAdmissionDesignation(@Valid @RequestBody AdmissionDesignationDTO admissionDesignationDTO) throws URISyntaxException {
        log.debug("REST request to save AdmissionDesignation : {}", admissionDesignationDTO);
        if (admissionDesignationDTO.getId() != null) {
            throw new BadRequestAlertException("A new admissionDesignation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AdmissionDesignationDTO result = admissionDesignationService.save(admissionDesignationDTO);
        return ResponseEntity.created(new URI("/api/admission-designations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /admission-designations} : Updates an existing admissionDesignation.
     *
     * @param admissionDesignationDTO the admissionDesignationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated admissionDesignationDTO,
     * or with status {@code 400 (Bad Request)} if the admissionDesignationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the admissionDesignationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/admission-designations")
    public ResponseEntity<AdmissionDesignationDTO> updateAdmissionDesignation(@Valid @RequestBody AdmissionDesignationDTO admissionDesignationDTO) throws URISyntaxException {
        log.debug("REST request to update AdmissionDesignation : {}", admissionDesignationDTO);
        if (admissionDesignationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        AdmissionDesignationDTO result = admissionDesignationService.save(admissionDesignationDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, admissionDesignationDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /admission-designations} : get all the admissionDesignations.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of admissionDesignations in body.
     */
    @GetMapping("/admission-designations")
    public ResponseEntity<List<AdmissionDesignationDTO>> getAllAdmissionDesignations(AdmissionDesignationCriteria criteria, Pageable pageable) {
        log.debug("REST request to get AdmissionDesignations by criteria: {}", criteria);
        Page<AdmissionDesignationDTO> page = admissionDesignationQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /admission-designations/count} : count all the admissionDesignations.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/admission-designations/count")
    public ResponseEntity<Long> countAdmissionDesignations(AdmissionDesignationCriteria criteria) {
        log.debug("REST request to count AdmissionDesignations by criteria: {}", criteria);
        return ResponseEntity.ok().body(admissionDesignationQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /admission-designations/:id} : get the "id" admissionDesignation.
     *
     * @param id the id of the admissionDesignationDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the admissionDesignationDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/admission-designations/{id}")
    public ResponseEntity<AdmissionDesignationDTO> getAdmissionDesignation(@PathVariable Long id) {
        log.debug("REST request to get AdmissionDesignation : {}", id);
        Optional<AdmissionDesignationDTO> admissionDesignationDTO = admissionDesignationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(admissionDesignationDTO);
    }

    /**
     * {@code DELETE  /admission-designations/:id} : delete the "id" admissionDesignation.
     *
     * @param id the id of the admissionDesignationDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/admission-designations/{id}")
    public ResponseEntity<Void> deleteAdmissionDesignation(@PathVariable Long id) {
        log.debug("REST request to delete AdmissionDesignation : {}", id);
        admissionDesignationService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
