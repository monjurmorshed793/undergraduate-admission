package org.ums.web.rest;

import org.ums.service.ProgramService;
import org.ums.web.rest.errors.BadRequestAlertException;
import org.ums.service.dto.ProgramDTO;
import org.ums.service.dto.ProgramCriteria;
import org.ums.service.ProgramQueryService;

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
 * REST controller for managing {@link org.ums.domain.Program}.
 */
@RestController
@RequestMapping("/api")
public class ProgramResource {

    private final Logger log = LoggerFactory.getLogger(ProgramResource.class);

    private static final String ENTITY_NAME = "program";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProgramService programService;

    private final ProgramQueryService programQueryService;

    public ProgramResource(ProgramService programService, ProgramQueryService programQueryService) {
        this.programService = programService;
        this.programQueryService = programQueryService;
    }

    /**
     * {@code POST  /programs} : Create a new program.
     *
     * @param programDTO the programDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new programDTO, or with status {@code 400 (Bad Request)} if the program has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/programs")
    public ResponseEntity<ProgramDTO> createProgram(@Valid @RequestBody ProgramDTO programDTO) throws URISyntaxException {
        log.debug("REST request to save Program : {}", programDTO);
        if (programDTO.getId() != null) {
            throw new BadRequestAlertException("A new program cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ProgramDTO result = programService.save(programDTO);
        return ResponseEntity.created(new URI("/api/programs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /programs} : Updates an existing program.
     *
     * @param programDTO the programDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated programDTO,
     * or with status {@code 400 (Bad Request)} if the programDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the programDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/programs")
    public ResponseEntity<ProgramDTO> updateProgram(@Valid @RequestBody ProgramDTO programDTO) throws URISyntaxException {
        log.debug("REST request to update Program : {}", programDTO);
        if (programDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ProgramDTO result = programService.save(programDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, programDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /programs} : get all the programs.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of programs in body.
     */
    @GetMapping("/programs")
    public ResponseEntity<List<ProgramDTO>> getAllPrograms(ProgramCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Programs by criteria: {}", criteria);
        Page<ProgramDTO> page = programQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /programs/count} : count all the programs.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/programs/count")
    public ResponseEntity<Long> countPrograms(ProgramCriteria criteria) {
        log.debug("REST request to count Programs by criteria: {}", criteria);
        return ResponseEntity.ok().body(programQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /programs/:id} : get the "id" program.
     *
     * @param id the id of the programDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the programDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/programs/{id}")
    public ResponseEntity<ProgramDTO> getProgram(@PathVariable Long id) {
        log.debug("REST request to get Program : {}", id);
        Optional<ProgramDTO> programDTO = programService.findOne(id);
        return ResponseUtil.wrapOrNotFound(programDTO);
    }

    /**
     * {@code DELETE  /programs/:id} : delete the "id" program.
     *
     * @param id the id of the programDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/programs/{id}")
    public ResponseEntity<Void> deleteProgram(@PathVariable Long id) {
        log.debug("REST request to delete Program : {}", id);
        programService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
