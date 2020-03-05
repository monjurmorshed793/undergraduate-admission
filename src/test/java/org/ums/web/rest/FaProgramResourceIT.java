package org.ums.web.rest;

import org.ums.UgAdmissionApp;
import org.ums.domain.FaProgram;
import org.ums.domain.Semester;
import org.ums.domain.Faculty;
import org.ums.domain.Program;
import org.ums.repository.FaProgramRepository;
import org.ums.service.FaProgramService;
import org.ums.service.dto.FaProgramDTO;
import org.ums.service.mapper.FaProgramMapper;
import org.ums.web.rest.errors.ExceptionTranslator;
import org.ums.service.dto.FaProgramCriteria;
import org.ums.service.FaProgramQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.ums.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link FaProgramResource} REST controller.
 */
@SpringBootTest(classes = UgAdmissionApp.class)
public class FaProgramResourceIT {

    private static final LocalDate DEFAULT_CREATED_ON = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATED_ON = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_CREATED_ON = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_MODIFIED_ON = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_MODIFIED_ON = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_MODIFIED_ON = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_MODIFIED_BY = "AAAAAAAAAA";
    private static final String UPDATED_MODIFIED_BY = "BBBBBBBBBB";

    @Autowired
    private FaProgramRepository faProgramRepository;

    @Autowired
    private FaProgramMapper faProgramMapper;

    @Autowired
    private FaProgramService faProgramService;

    @Autowired
    private FaProgramQueryService faProgramQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restFaProgramMockMvc;

    private FaProgram faProgram;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final FaProgramResource faProgramResource = new FaProgramResource(faProgramService, faProgramQueryService);
        this.restFaProgramMockMvc = MockMvcBuilders.standaloneSetup(faProgramResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FaProgram createEntity(EntityManager em) {
        FaProgram faProgram = new FaProgram()
            .createdOn(DEFAULT_CREATED_ON)
            .modifiedOn(DEFAULT_MODIFIED_ON)
            .modifiedBy(DEFAULT_MODIFIED_BY);
        return faProgram;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FaProgram createUpdatedEntity(EntityManager em) {
        FaProgram faProgram = new FaProgram()
            .createdOn(UPDATED_CREATED_ON)
            .modifiedOn(UPDATED_MODIFIED_ON)
            .modifiedBy(UPDATED_MODIFIED_BY);
        return faProgram;
    }

    @BeforeEach
    public void initTest() {
        faProgram = createEntity(em);
    }

    @Test
    @Transactional
    public void createFaProgram() throws Exception {
        int databaseSizeBeforeCreate = faProgramRepository.findAll().size();

        // Create the FaProgram
        FaProgramDTO faProgramDTO = faProgramMapper.toDto(faProgram);
        restFaProgramMockMvc.perform(post("/api/fa-programs")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(faProgramDTO)))
            .andExpect(status().isCreated());

        // Validate the FaProgram in the database
        List<FaProgram> faProgramList = faProgramRepository.findAll();
        assertThat(faProgramList).hasSize(databaseSizeBeforeCreate + 1);
        FaProgram testFaProgram = faProgramList.get(faProgramList.size() - 1);
        assertThat(testFaProgram.getCreatedOn()).isEqualTo(DEFAULT_CREATED_ON);
        assertThat(testFaProgram.getModifiedOn()).isEqualTo(DEFAULT_MODIFIED_ON);
        assertThat(testFaProgram.getModifiedBy()).isEqualTo(DEFAULT_MODIFIED_BY);
    }

    @Test
    @Transactional
    public void createFaProgramWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = faProgramRepository.findAll().size();

        // Create the FaProgram with an existing ID
        faProgram.setId(1L);
        FaProgramDTO faProgramDTO = faProgramMapper.toDto(faProgram);

        // An entity with an existing ID cannot be created, so this API call must fail
        restFaProgramMockMvc.perform(post("/api/fa-programs")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(faProgramDTO)))
            .andExpect(status().isBadRequest());

        // Validate the FaProgram in the database
        List<FaProgram> faProgramList = faProgramRepository.findAll();
        assertThat(faProgramList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllFaPrograms() throws Exception {
        // Initialize the database
        faProgramRepository.saveAndFlush(faProgram);

        // Get all the faProgramList
        restFaProgramMockMvc.perform(get("/api/fa-programs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(faProgram.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdOn").value(hasItem(DEFAULT_CREATED_ON.toString())))
            .andExpect(jsonPath("$.[*].modifiedOn").value(hasItem(DEFAULT_MODIFIED_ON.toString())))
            .andExpect(jsonPath("$.[*].modifiedBy").value(hasItem(DEFAULT_MODIFIED_BY)));
    }
    
    @Test
    @Transactional
    public void getFaProgram() throws Exception {
        // Initialize the database
        faProgramRepository.saveAndFlush(faProgram);

        // Get the faProgram
        restFaProgramMockMvc.perform(get("/api/fa-programs/{id}", faProgram.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(faProgram.getId().intValue()))
            .andExpect(jsonPath("$.createdOn").value(DEFAULT_CREATED_ON.toString()))
            .andExpect(jsonPath("$.modifiedOn").value(DEFAULT_MODIFIED_ON.toString()))
            .andExpect(jsonPath("$.modifiedBy").value(DEFAULT_MODIFIED_BY));
    }


    @Test
    @Transactional
    public void getFaProgramsByIdFiltering() throws Exception {
        // Initialize the database
        faProgramRepository.saveAndFlush(faProgram);

        Long id = faProgram.getId();

        defaultFaProgramShouldBeFound("id.equals=" + id);
        defaultFaProgramShouldNotBeFound("id.notEquals=" + id);

        defaultFaProgramShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultFaProgramShouldNotBeFound("id.greaterThan=" + id);

        defaultFaProgramShouldBeFound("id.lessThanOrEqual=" + id);
        defaultFaProgramShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllFaProgramsByCreatedOnIsEqualToSomething() throws Exception {
        // Initialize the database
        faProgramRepository.saveAndFlush(faProgram);

        // Get all the faProgramList where createdOn equals to DEFAULT_CREATED_ON
        defaultFaProgramShouldBeFound("createdOn.equals=" + DEFAULT_CREATED_ON);

        // Get all the faProgramList where createdOn equals to UPDATED_CREATED_ON
        defaultFaProgramShouldNotBeFound("createdOn.equals=" + UPDATED_CREATED_ON);
    }

    @Test
    @Transactional
    public void getAllFaProgramsByCreatedOnIsNotEqualToSomething() throws Exception {
        // Initialize the database
        faProgramRepository.saveAndFlush(faProgram);

        // Get all the faProgramList where createdOn not equals to DEFAULT_CREATED_ON
        defaultFaProgramShouldNotBeFound("createdOn.notEquals=" + DEFAULT_CREATED_ON);

        // Get all the faProgramList where createdOn not equals to UPDATED_CREATED_ON
        defaultFaProgramShouldBeFound("createdOn.notEquals=" + UPDATED_CREATED_ON);
    }

    @Test
    @Transactional
    public void getAllFaProgramsByCreatedOnIsInShouldWork() throws Exception {
        // Initialize the database
        faProgramRepository.saveAndFlush(faProgram);

        // Get all the faProgramList where createdOn in DEFAULT_CREATED_ON or UPDATED_CREATED_ON
        defaultFaProgramShouldBeFound("createdOn.in=" + DEFAULT_CREATED_ON + "," + UPDATED_CREATED_ON);

        // Get all the faProgramList where createdOn equals to UPDATED_CREATED_ON
        defaultFaProgramShouldNotBeFound("createdOn.in=" + UPDATED_CREATED_ON);
    }

    @Test
    @Transactional
    public void getAllFaProgramsByCreatedOnIsNullOrNotNull() throws Exception {
        // Initialize the database
        faProgramRepository.saveAndFlush(faProgram);

        // Get all the faProgramList where createdOn is not null
        defaultFaProgramShouldBeFound("createdOn.specified=true");

        // Get all the faProgramList where createdOn is null
        defaultFaProgramShouldNotBeFound("createdOn.specified=false");
    }

    @Test
    @Transactional
    public void getAllFaProgramsByCreatedOnIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        faProgramRepository.saveAndFlush(faProgram);

        // Get all the faProgramList where createdOn is greater than or equal to DEFAULT_CREATED_ON
        defaultFaProgramShouldBeFound("createdOn.greaterThanOrEqual=" + DEFAULT_CREATED_ON);

        // Get all the faProgramList where createdOn is greater than or equal to UPDATED_CREATED_ON
        defaultFaProgramShouldNotBeFound("createdOn.greaterThanOrEqual=" + UPDATED_CREATED_ON);
    }

    @Test
    @Transactional
    public void getAllFaProgramsByCreatedOnIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        faProgramRepository.saveAndFlush(faProgram);

        // Get all the faProgramList where createdOn is less than or equal to DEFAULT_CREATED_ON
        defaultFaProgramShouldBeFound("createdOn.lessThanOrEqual=" + DEFAULT_CREATED_ON);

        // Get all the faProgramList where createdOn is less than or equal to SMALLER_CREATED_ON
        defaultFaProgramShouldNotBeFound("createdOn.lessThanOrEqual=" + SMALLER_CREATED_ON);
    }

    @Test
    @Transactional
    public void getAllFaProgramsByCreatedOnIsLessThanSomething() throws Exception {
        // Initialize the database
        faProgramRepository.saveAndFlush(faProgram);

        // Get all the faProgramList where createdOn is less than DEFAULT_CREATED_ON
        defaultFaProgramShouldNotBeFound("createdOn.lessThan=" + DEFAULT_CREATED_ON);

        // Get all the faProgramList where createdOn is less than UPDATED_CREATED_ON
        defaultFaProgramShouldBeFound("createdOn.lessThan=" + UPDATED_CREATED_ON);
    }

    @Test
    @Transactional
    public void getAllFaProgramsByCreatedOnIsGreaterThanSomething() throws Exception {
        // Initialize the database
        faProgramRepository.saveAndFlush(faProgram);

        // Get all the faProgramList where createdOn is greater than DEFAULT_CREATED_ON
        defaultFaProgramShouldNotBeFound("createdOn.greaterThan=" + DEFAULT_CREATED_ON);

        // Get all the faProgramList where createdOn is greater than SMALLER_CREATED_ON
        defaultFaProgramShouldBeFound("createdOn.greaterThan=" + SMALLER_CREATED_ON);
    }


    @Test
    @Transactional
    public void getAllFaProgramsByModifiedOnIsEqualToSomething() throws Exception {
        // Initialize the database
        faProgramRepository.saveAndFlush(faProgram);

        // Get all the faProgramList where modifiedOn equals to DEFAULT_MODIFIED_ON
        defaultFaProgramShouldBeFound("modifiedOn.equals=" + DEFAULT_MODIFIED_ON);

        // Get all the faProgramList where modifiedOn equals to UPDATED_MODIFIED_ON
        defaultFaProgramShouldNotBeFound("modifiedOn.equals=" + UPDATED_MODIFIED_ON);
    }

    @Test
    @Transactional
    public void getAllFaProgramsByModifiedOnIsNotEqualToSomething() throws Exception {
        // Initialize the database
        faProgramRepository.saveAndFlush(faProgram);

        // Get all the faProgramList where modifiedOn not equals to DEFAULT_MODIFIED_ON
        defaultFaProgramShouldNotBeFound("modifiedOn.notEquals=" + DEFAULT_MODIFIED_ON);

        // Get all the faProgramList where modifiedOn not equals to UPDATED_MODIFIED_ON
        defaultFaProgramShouldBeFound("modifiedOn.notEquals=" + UPDATED_MODIFIED_ON);
    }

    @Test
    @Transactional
    public void getAllFaProgramsByModifiedOnIsInShouldWork() throws Exception {
        // Initialize the database
        faProgramRepository.saveAndFlush(faProgram);

        // Get all the faProgramList where modifiedOn in DEFAULT_MODIFIED_ON or UPDATED_MODIFIED_ON
        defaultFaProgramShouldBeFound("modifiedOn.in=" + DEFAULT_MODIFIED_ON + "," + UPDATED_MODIFIED_ON);

        // Get all the faProgramList where modifiedOn equals to UPDATED_MODIFIED_ON
        defaultFaProgramShouldNotBeFound("modifiedOn.in=" + UPDATED_MODIFIED_ON);
    }

    @Test
    @Transactional
    public void getAllFaProgramsByModifiedOnIsNullOrNotNull() throws Exception {
        // Initialize the database
        faProgramRepository.saveAndFlush(faProgram);

        // Get all the faProgramList where modifiedOn is not null
        defaultFaProgramShouldBeFound("modifiedOn.specified=true");

        // Get all the faProgramList where modifiedOn is null
        defaultFaProgramShouldNotBeFound("modifiedOn.specified=false");
    }

    @Test
    @Transactional
    public void getAllFaProgramsByModifiedOnIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        faProgramRepository.saveAndFlush(faProgram);

        // Get all the faProgramList where modifiedOn is greater than or equal to DEFAULT_MODIFIED_ON
        defaultFaProgramShouldBeFound("modifiedOn.greaterThanOrEqual=" + DEFAULT_MODIFIED_ON);

        // Get all the faProgramList where modifiedOn is greater than or equal to UPDATED_MODIFIED_ON
        defaultFaProgramShouldNotBeFound("modifiedOn.greaterThanOrEqual=" + UPDATED_MODIFIED_ON);
    }

    @Test
    @Transactional
    public void getAllFaProgramsByModifiedOnIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        faProgramRepository.saveAndFlush(faProgram);

        // Get all the faProgramList where modifiedOn is less than or equal to DEFAULT_MODIFIED_ON
        defaultFaProgramShouldBeFound("modifiedOn.lessThanOrEqual=" + DEFAULT_MODIFIED_ON);

        // Get all the faProgramList where modifiedOn is less than or equal to SMALLER_MODIFIED_ON
        defaultFaProgramShouldNotBeFound("modifiedOn.lessThanOrEqual=" + SMALLER_MODIFIED_ON);
    }

    @Test
    @Transactional
    public void getAllFaProgramsByModifiedOnIsLessThanSomething() throws Exception {
        // Initialize the database
        faProgramRepository.saveAndFlush(faProgram);

        // Get all the faProgramList where modifiedOn is less than DEFAULT_MODIFIED_ON
        defaultFaProgramShouldNotBeFound("modifiedOn.lessThan=" + DEFAULT_MODIFIED_ON);

        // Get all the faProgramList where modifiedOn is less than UPDATED_MODIFIED_ON
        defaultFaProgramShouldBeFound("modifiedOn.lessThan=" + UPDATED_MODIFIED_ON);
    }

    @Test
    @Transactional
    public void getAllFaProgramsByModifiedOnIsGreaterThanSomething() throws Exception {
        // Initialize the database
        faProgramRepository.saveAndFlush(faProgram);

        // Get all the faProgramList where modifiedOn is greater than DEFAULT_MODIFIED_ON
        defaultFaProgramShouldNotBeFound("modifiedOn.greaterThan=" + DEFAULT_MODIFIED_ON);

        // Get all the faProgramList where modifiedOn is greater than SMALLER_MODIFIED_ON
        defaultFaProgramShouldBeFound("modifiedOn.greaterThan=" + SMALLER_MODIFIED_ON);
    }


    @Test
    @Transactional
    public void getAllFaProgramsByModifiedByIsEqualToSomething() throws Exception {
        // Initialize the database
        faProgramRepository.saveAndFlush(faProgram);

        // Get all the faProgramList where modifiedBy equals to DEFAULT_MODIFIED_BY
        defaultFaProgramShouldBeFound("modifiedBy.equals=" + DEFAULT_MODIFIED_BY);

        // Get all the faProgramList where modifiedBy equals to UPDATED_MODIFIED_BY
        defaultFaProgramShouldNotBeFound("modifiedBy.equals=" + UPDATED_MODIFIED_BY);
    }

    @Test
    @Transactional
    public void getAllFaProgramsByModifiedByIsNotEqualToSomething() throws Exception {
        // Initialize the database
        faProgramRepository.saveAndFlush(faProgram);

        // Get all the faProgramList where modifiedBy not equals to DEFAULT_MODIFIED_BY
        defaultFaProgramShouldNotBeFound("modifiedBy.notEquals=" + DEFAULT_MODIFIED_BY);

        // Get all the faProgramList where modifiedBy not equals to UPDATED_MODIFIED_BY
        defaultFaProgramShouldBeFound("modifiedBy.notEquals=" + UPDATED_MODIFIED_BY);
    }

    @Test
    @Transactional
    public void getAllFaProgramsByModifiedByIsInShouldWork() throws Exception {
        // Initialize the database
        faProgramRepository.saveAndFlush(faProgram);

        // Get all the faProgramList where modifiedBy in DEFAULT_MODIFIED_BY or UPDATED_MODIFIED_BY
        defaultFaProgramShouldBeFound("modifiedBy.in=" + DEFAULT_MODIFIED_BY + "," + UPDATED_MODIFIED_BY);

        // Get all the faProgramList where modifiedBy equals to UPDATED_MODIFIED_BY
        defaultFaProgramShouldNotBeFound("modifiedBy.in=" + UPDATED_MODIFIED_BY);
    }

    @Test
    @Transactional
    public void getAllFaProgramsByModifiedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        faProgramRepository.saveAndFlush(faProgram);

        // Get all the faProgramList where modifiedBy is not null
        defaultFaProgramShouldBeFound("modifiedBy.specified=true");

        // Get all the faProgramList where modifiedBy is null
        defaultFaProgramShouldNotBeFound("modifiedBy.specified=false");
    }
                @Test
    @Transactional
    public void getAllFaProgramsByModifiedByContainsSomething() throws Exception {
        // Initialize the database
        faProgramRepository.saveAndFlush(faProgram);

        // Get all the faProgramList where modifiedBy contains DEFAULT_MODIFIED_BY
        defaultFaProgramShouldBeFound("modifiedBy.contains=" + DEFAULT_MODIFIED_BY);

        // Get all the faProgramList where modifiedBy contains UPDATED_MODIFIED_BY
        defaultFaProgramShouldNotBeFound("modifiedBy.contains=" + UPDATED_MODIFIED_BY);
    }

    @Test
    @Transactional
    public void getAllFaProgramsByModifiedByNotContainsSomething() throws Exception {
        // Initialize the database
        faProgramRepository.saveAndFlush(faProgram);

        // Get all the faProgramList where modifiedBy does not contain DEFAULT_MODIFIED_BY
        defaultFaProgramShouldNotBeFound("modifiedBy.doesNotContain=" + DEFAULT_MODIFIED_BY);

        // Get all the faProgramList where modifiedBy does not contain UPDATED_MODIFIED_BY
        defaultFaProgramShouldBeFound("modifiedBy.doesNotContain=" + UPDATED_MODIFIED_BY);
    }


    @Test
    @Transactional
    public void getAllFaProgramsBySemesterIsEqualToSomething() throws Exception {
        // Initialize the database
        faProgramRepository.saveAndFlush(faProgram);
        Semester semester = SemesterResourceIT.createEntity(em);
        em.persist(semester);
        em.flush();
        faProgram.setSemester(semester);
        faProgramRepository.saveAndFlush(faProgram);
        Long semesterId = semester.getId();

        // Get all the faProgramList where semester equals to semesterId
        defaultFaProgramShouldBeFound("semesterId.equals=" + semesterId);

        // Get all the faProgramList where semester equals to semesterId + 1
        defaultFaProgramShouldNotBeFound("semesterId.equals=" + (semesterId + 1));
    }


    @Test
    @Transactional
    public void getAllFaProgramsByFacultyIsEqualToSomething() throws Exception {
        // Initialize the database
        faProgramRepository.saveAndFlush(faProgram);
        Faculty faculty = FacultyResourceIT.createEntity(em);
        em.persist(faculty);
        em.flush();
        faProgram.setFaculty(faculty);
        faProgramRepository.saveAndFlush(faProgram);
        Long facultyId = faculty.getId();

        // Get all the faProgramList where faculty equals to facultyId
        defaultFaProgramShouldBeFound("facultyId.equals=" + facultyId);

        // Get all the faProgramList where faculty equals to facultyId + 1
        defaultFaProgramShouldNotBeFound("facultyId.equals=" + (facultyId + 1));
    }


    @Test
    @Transactional
    public void getAllFaProgramsByProgramIsEqualToSomething() throws Exception {
        // Initialize the database
        faProgramRepository.saveAndFlush(faProgram);
        Program program = ProgramResourceIT.createEntity(em);
        em.persist(program);
        em.flush();
        faProgram.setProgram(program);
        faProgramRepository.saveAndFlush(faProgram);
        Long programId = program.getId();

        // Get all the faProgramList where program equals to programId
        defaultFaProgramShouldBeFound("programId.equals=" + programId);

        // Get all the faProgramList where program equals to programId + 1
        defaultFaProgramShouldNotBeFound("programId.equals=" + (programId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultFaProgramShouldBeFound(String filter) throws Exception {
        restFaProgramMockMvc.perform(get("/api/fa-programs?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(faProgram.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdOn").value(hasItem(DEFAULT_CREATED_ON.toString())))
            .andExpect(jsonPath("$.[*].modifiedOn").value(hasItem(DEFAULT_MODIFIED_ON.toString())))
            .andExpect(jsonPath("$.[*].modifiedBy").value(hasItem(DEFAULT_MODIFIED_BY)));

        // Check, that the count call also returns 1
        restFaProgramMockMvc.perform(get("/api/fa-programs/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultFaProgramShouldNotBeFound(String filter) throws Exception {
        restFaProgramMockMvc.perform(get("/api/fa-programs?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restFaProgramMockMvc.perform(get("/api/fa-programs/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingFaProgram() throws Exception {
        // Get the faProgram
        restFaProgramMockMvc.perform(get("/api/fa-programs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFaProgram() throws Exception {
        // Initialize the database
        faProgramRepository.saveAndFlush(faProgram);

        int databaseSizeBeforeUpdate = faProgramRepository.findAll().size();

        // Update the faProgram
        FaProgram updatedFaProgram = faProgramRepository.findById(faProgram.getId()).get();
        // Disconnect from session so that the updates on updatedFaProgram are not directly saved in db
        em.detach(updatedFaProgram);
        updatedFaProgram
            .createdOn(UPDATED_CREATED_ON)
            .modifiedOn(UPDATED_MODIFIED_ON)
            .modifiedBy(UPDATED_MODIFIED_BY);
        FaProgramDTO faProgramDTO = faProgramMapper.toDto(updatedFaProgram);

        restFaProgramMockMvc.perform(put("/api/fa-programs")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(faProgramDTO)))
            .andExpect(status().isOk());

        // Validate the FaProgram in the database
        List<FaProgram> faProgramList = faProgramRepository.findAll();
        assertThat(faProgramList).hasSize(databaseSizeBeforeUpdate);
        FaProgram testFaProgram = faProgramList.get(faProgramList.size() - 1);
        assertThat(testFaProgram.getCreatedOn()).isEqualTo(UPDATED_CREATED_ON);
        assertThat(testFaProgram.getModifiedOn()).isEqualTo(UPDATED_MODIFIED_ON);
        assertThat(testFaProgram.getModifiedBy()).isEqualTo(UPDATED_MODIFIED_BY);
    }

    @Test
    @Transactional
    public void updateNonExistingFaProgram() throws Exception {
        int databaseSizeBeforeUpdate = faProgramRepository.findAll().size();

        // Create the FaProgram
        FaProgramDTO faProgramDTO = faProgramMapper.toDto(faProgram);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFaProgramMockMvc.perform(put("/api/fa-programs")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(faProgramDTO)))
            .andExpect(status().isBadRequest());

        // Validate the FaProgram in the database
        List<FaProgram> faProgramList = faProgramRepository.findAll();
        assertThat(faProgramList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteFaProgram() throws Exception {
        // Initialize the database
        faProgramRepository.saveAndFlush(faProgram);

        int databaseSizeBeforeDelete = faProgramRepository.findAll().size();

        // Delete the faProgram
        restFaProgramMockMvc.perform(delete("/api/fa-programs/{id}", faProgram.getId())
            .accept(TestUtil.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<FaProgram> faProgramList = faProgramRepository.findAll();
        assertThat(faProgramList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
