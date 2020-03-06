package org.ums.web.rest.extended;

import io.github.jhipster.web.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.ums.service.SemesterQueryService;
import org.ums.service.SemesterService;
import org.ums.service.dto.SemesterDTO;
import org.ums.service.extended.SemesterExtendedService;
import org.ums.web.rest.SemesterResource;
import org.ums.web.rest.errors.BadRequestAlertException;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

@RestController
@RequestMapping("/api/extended")
public class SemesterExtendedResource extends SemesterResource {
    private final Logger log = LoggerFactory.getLogger(SemesterExtendedResource.class);

    private static final String ENTITY_NAME = "semester";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SemesterExtendedService semesterService;

    private final SemesterQueryService semesterQueryService;

    public SemesterExtendedResource(SemesterService semesterService, SemesterQueryService semesterQueryService, SemesterExtendedService semesterService1, SemesterQueryService semesterQueryService1) {
        super(semesterService, semesterQueryService);
        this.semesterService = semesterService1;
        this.semesterQueryService = semesterQueryService1;
    }

    @PostMapping("/semesters")
    public ResponseEntity<SemesterDTO> createSemester(@Valid @RequestBody SemesterDTO semesterDTO) throws URISyntaxException {
        log.debug("REST request to save Semester : {}", semesterDTO);
        if (semesterDTO.getId() != null) {
            throw new BadRequestAlertException("A new semester cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SemesterDTO result = semesterService.save(semesterDTO);
        return ResponseEntity.created(new URI("/api/semesters/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PutMapping("/semesters")
    public ResponseEntity<SemesterDTO> updateSemester(@Valid @RequestBody SemesterDTO semesterDTO) throws URISyntaxException {
        log.debug("REST request to update Semester : {}", semesterDTO);
        if (semesterDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        SemesterDTO result = semesterService.save(semesterDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, semesterDTO.getId().toString()))
            .body(result);
    }
}
