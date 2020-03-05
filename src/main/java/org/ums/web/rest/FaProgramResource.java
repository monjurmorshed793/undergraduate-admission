package org.ums.web.rest;

import org.ums.service.FaProgramService;
import org.ums.web.rest.errors.BadRequestAlertException;
import org.ums.service.dto.FaProgramDTO;
import org.ums.service.dto.FaProgramCriteria;
import org.ums.service.FaProgramQueryService;

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

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link org.ums.domain.FaProgram}.
 */
@RestController
@RequestMapping("/api")
public class FaProgramResource {

    private final Logger log = LoggerFactory.getLogger(FaProgramResource.class);

    private static final String ENTITY_NAME = "faProgram";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FaProgramService faProgramService;

    private final FaProgramQueryService faProgramQueryService;

    public FaProgramResource(FaProgramService faProgramService, FaProgramQueryService faProgramQueryService) {
        this.faProgramService = faProgramService;
        this.faProgramQueryService = faProgramQueryService;
    }

    /**
     * {@code POST  /fa-programs} : Create a new faProgram.
     *
     * @param faProgramDTO the faProgramDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new faProgramDTO, or with status {@code 400 (Bad Request)} if the faProgram has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/fa-programs")
    public ResponseEntity<FaProgramDTO> createFaProgram(@RequestBody FaProgramDTO faProgramDTO) throws URISyntaxException {
        log.debug("REST request to save FaProgram : {}", faProgramDTO);
        if (faProgramDTO.getId() != null) {
            throw new BadRequestAlertException("A new faProgram cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FaProgramDTO result = faProgramService.save(faProgramDTO);
        return ResponseEntity.created(new URI("/api/fa-programs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /fa-programs} : Updates an existing faProgram.
     *
     * @param faProgramDTO the faProgramDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated faProgramDTO,
     * or with status {@code 400 (Bad Request)} if the faProgramDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the faProgramDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/fa-programs")
    public ResponseEntity<FaProgramDTO> updateFaProgram(@RequestBody FaProgramDTO faProgramDTO) throws URISyntaxException {
        log.debug("REST request to update FaProgram : {}", faProgramDTO);
        if (faProgramDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        FaProgramDTO result = faProgramService.save(faProgramDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, faProgramDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /fa-programs} : get all the faPrograms.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of faPrograms in body.
     */
    @GetMapping("/fa-programs")
    public ResponseEntity<List<FaProgramDTO>> getAllFaPrograms(FaProgramCriteria criteria, Pageable pageable) {
        log.debug("REST request to get FaPrograms by criteria: {}", criteria);
        Page<FaProgramDTO> page = faProgramQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /fa-programs/count} : count all the faPrograms.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/fa-programs/count")
    public ResponseEntity<Long> countFaPrograms(FaProgramCriteria criteria) {
        log.debug("REST request to count FaPrograms by criteria: {}", criteria);
        return ResponseEntity.ok().body(faProgramQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /fa-programs/:id} : get the "id" faProgram.
     *
     * @param id the id of the faProgramDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the faProgramDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/fa-programs/{id}")
    public ResponseEntity<FaProgramDTO> getFaProgram(@PathVariable Long id) {
        log.debug("REST request to get FaProgram : {}", id);
        Optional<FaProgramDTO> faProgramDTO = faProgramService.findOne(id);
        return ResponseUtil.wrapOrNotFound(faProgramDTO);
    }

    /**
     * {@code DELETE  /fa-programs/:id} : delete the "id" faProgram.
     *
     * @param id the id of the faProgramDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/fa-programs/{id}")
    public ResponseEntity<Void> deleteFaProgram(@PathVariable Long id) {
        log.debug("REST request to delete FaProgram : {}", id);
        faProgramService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
