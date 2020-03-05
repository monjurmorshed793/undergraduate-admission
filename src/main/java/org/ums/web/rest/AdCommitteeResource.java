package org.ums.web.rest;

import org.ums.service.AdCommitteeService;
import org.ums.web.rest.errors.BadRequestAlertException;
import org.ums.service.dto.AdCommitteeDTO;
import org.ums.service.dto.AdCommitteeCriteria;
import org.ums.service.AdCommitteeQueryService;

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
 * REST controller for managing {@link org.ums.domain.AdCommittee}.
 */
@RestController
@RequestMapping("/api")
public class AdCommitteeResource {

    private final Logger log = LoggerFactory.getLogger(AdCommitteeResource.class);

    private static final String ENTITY_NAME = "adCommittee";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AdCommitteeService adCommitteeService;

    private final AdCommitteeQueryService adCommitteeQueryService;

    public AdCommitteeResource(AdCommitteeService adCommitteeService, AdCommitteeQueryService adCommitteeQueryService) {
        this.adCommitteeService = adCommitteeService;
        this.adCommitteeQueryService = adCommitteeQueryService;
    }

    /**
     * {@code POST  /ad-committees} : Create a new adCommittee.
     *
     * @param adCommitteeDTO the adCommitteeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new adCommitteeDTO, or with status {@code 400 (Bad Request)} if the adCommittee has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/ad-committees")
    public ResponseEntity<AdCommitteeDTO> createAdCommittee(@Valid @RequestBody AdCommitteeDTO adCommitteeDTO) throws URISyntaxException {
        log.debug("REST request to save AdCommittee : {}", adCommitteeDTO);
        if (adCommitteeDTO.getId() != null) {
            throw new BadRequestAlertException("A new adCommittee cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AdCommitteeDTO result = adCommitteeService.save(adCommitteeDTO);
        return ResponseEntity.created(new URI("/api/ad-committees/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /ad-committees} : Updates an existing adCommittee.
     *
     * @param adCommitteeDTO the adCommitteeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated adCommitteeDTO,
     * or with status {@code 400 (Bad Request)} if the adCommitteeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the adCommitteeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/ad-committees")
    public ResponseEntity<AdCommitteeDTO> updateAdCommittee(@Valid @RequestBody AdCommitteeDTO adCommitteeDTO) throws URISyntaxException {
        log.debug("REST request to update AdCommittee : {}", adCommitteeDTO);
        if (adCommitteeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        AdCommitteeDTO result = adCommitteeService.save(adCommitteeDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, adCommitteeDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /ad-committees} : get all the adCommittees.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of adCommittees in body.
     */
    @GetMapping("/ad-committees")
    public ResponseEntity<List<AdCommitteeDTO>> getAllAdCommittees(AdCommitteeCriteria criteria, Pageable pageable) {
        log.debug("REST request to get AdCommittees by criteria: {}", criteria);
        Page<AdCommitteeDTO> page = adCommitteeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /ad-committees/count} : count all the adCommittees.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/ad-committees/count")
    public ResponseEntity<Long> countAdCommittees(AdCommitteeCriteria criteria) {
        log.debug("REST request to count AdCommittees by criteria: {}", criteria);
        return ResponseEntity.ok().body(adCommitteeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /ad-committees/:id} : get the "id" adCommittee.
     *
     * @param id the id of the adCommitteeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the adCommitteeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/ad-committees/{id}")
    public ResponseEntity<AdCommitteeDTO> getAdCommittee(@PathVariable Long id) {
        log.debug("REST request to get AdCommittee : {}", id);
        Optional<AdCommitteeDTO> adCommitteeDTO = adCommitteeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(adCommitteeDTO);
    }

    /**
     * {@code DELETE  /ad-committees/:id} : delete the "id" adCommittee.
     *
     * @param id the id of the adCommitteeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/ad-committees/{id}")
    public ResponseEntity<Void> deleteAdCommittee(@PathVariable Long id) {
        log.debug("REST request to delete AdCommittee : {}", id);
        adCommitteeService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
