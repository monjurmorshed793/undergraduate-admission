package org.ums.web.rest;

import org.ums.UgAdmissionApp;
import org.ums.domain.Program;
import org.ums.repository.ProgramRepository;
import org.ums.service.ProgramService;
import org.ums.service.dto.ProgramDTO;
import org.ums.service.mapper.ProgramMapper;
import org.ums.web.rest.errors.ExceptionTranslator;
import org.ums.service.dto.ProgramCriteria;
import org.ums.service.ProgramQueryService;

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
 * Integration tests for the {@link ProgramResource} REST controller.
 */
@SpringBootTest(classes = UgAdmissionApp.class)
public class ProgramResourceIT {

    private static final Integer DEFAULT_PROGRAM_ID = 1;
    private static final Integer UPDATED_PROGRAM_ID = 2;
    private static final Integer SMALLER_PROGRAM_ID = 1 - 1;

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_CREATED_ON = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATED_ON = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_CREATED_ON = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_MODIFIED_ON = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_MODIFIED_ON = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_MODIFIED_ON = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_MODIFIED_BY = "AAAAAAAAAA";
    private static final String UPDATED_MODIFIED_BY = "BBBBBBBBBB";

    @Autowired
    private ProgramRepository programRepository;

    @Autowired
    private ProgramMapper programMapper;

    @Autowired
    private ProgramService programService;

    @Autowired
    private ProgramQueryService programQueryService;

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

    private MockMvc restProgramMockMvc;

    private Program program;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ProgramResource programResource = new ProgramResource(programService, programQueryService);
        this.restProgramMockMvc = MockMvcBuilders.standaloneSetup(programResource)
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
    public static Program createEntity(EntityManager em) {
        Program program = new Program()
            .programId(DEFAULT_PROGRAM_ID)
            .name(DEFAULT_NAME)
            .createdOn(DEFAULT_CREATED_ON)
            .modifiedOn(DEFAULT_MODIFIED_ON)
            .modifiedBy(DEFAULT_MODIFIED_BY);
        return program;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Program createUpdatedEntity(EntityManager em) {
        Program program = new Program()
            .programId(UPDATED_PROGRAM_ID)
            .name(UPDATED_NAME)
            .createdOn(UPDATED_CREATED_ON)
            .modifiedOn(UPDATED_MODIFIED_ON)
            .modifiedBy(UPDATED_MODIFIED_BY);
        return program;
    }

    @BeforeEach
    public void initTest() {
        program = createEntity(em);
    }

    @Test
    @Transactional
    public void createProgram() throws Exception {
        int databaseSizeBeforeCreate = programRepository.findAll().size();

        // Create the Program
        ProgramDTO programDTO = programMapper.toDto(program);
        restProgramMockMvc.perform(post("/api/programs")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(programDTO)))
            .andExpect(status().isCreated());

        // Validate the Program in the database
        List<Program> programList = programRepository.findAll();
        assertThat(programList).hasSize(databaseSizeBeforeCreate + 1);
        Program testProgram = programList.get(programList.size() - 1);
        assertThat(testProgram.getProgramId()).isEqualTo(DEFAULT_PROGRAM_ID);
        assertThat(testProgram.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testProgram.getCreatedOn()).isEqualTo(DEFAULT_CREATED_ON);
        assertThat(testProgram.getModifiedOn()).isEqualTo(DEFAULT_MODIFIED_ON);
        assertThat(testProgram.getModifiedBy()).isEqualTo(DEFAULT_MODIFIED_BY);
    }

    @Test
    @Transactional
    public void createProgramWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = programRepository.findAll().size();

        // Create the Program with an existing ID
        program.setId(1L);
        ProgramDTO programDTO = programMapper.toDto(program);

        // An entity with an existing ID cannot be created, so this API call must fail
        restProgramMockMvc.perform(post("/api/programs")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(programDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Program in the database
        List<Program> programList = programRepository.findAll();
        assertThat(programList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkProgramIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = programRepository.findAll().size();
        // set the field null
        program.setProgramId(null);

        // Create the Program, which fails.
        ProgramDTO programDTO = programMapper.toDto(program);

        restProgramMockMvc.perform(post("/api/programs")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(programDTO)))
            .andExpect(status().isBadRequest());

        List<Program> programList = programRepository.findAll();
        assertThat(programList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = programRepository.findAll().size();
        // set the field null
        program.setName(null);

        // Create the Program, which fails.
        ProgramDTO programDTO = programMapper.toDto(program);

        restProgramMockMvc.perform(post("/api/programs")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(programDTO)))
            .andExpect(status().isBadRequest());

        List<Program> programList = programRepository.findAll();
        assertThat(programList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPrograms() throws Exception {
        // Initialize the database
        programRepository.saveAndFlush(program);

        // Get all the programList
        restProgramMockMvc.perform(get("/api/programs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(program.getId().intValue())))
            .andExpect(jsonPath("$.[*].programId").value(hasItem(DEFAULT_PROGRAM_ID)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].createdOn").value(hasItem(DEFAULT_CREATED_ON.toString())))
            .andExpect(jsonPath("$.[*].modifiedOn").value(hasItem(DEFAULT_MODIFIED_ON.toString())))
            .andExpect(jsonPath("$.[*].modifiedBy").value(hasItem(DEFAULT_MODIFIED_BY)));
    }
    
    @Test
    @Transactional
    public void getProgram() throws Exception {
        // Initialize the database
        programRepository.saveAndFlush(program);

        // Get the program
        restProgramMockMvc.perform(get("/api/programs/{id}", program.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(program.getId().intValue()))
            .andExpect(jsonPath("$.programId").value(DEFAULT_PROGRAM_ID))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.createdOn").value(DEFAULT_CREATED_ON.toString()))
            .andExpect(jsonPath("$.modifiedOn").value(DEFAULT_MODIFIED_ON.toString()))
            .andExpect(jsonPath("$.modifiedBy").value(DEFAULT_MODIFIED_BY));
    }


    @Test
    @Transactional
    public void getProgramsByIdFiltering() throws Exception {
        // Initialize the database
        programRepository.saveAndFlush(program);

        Long id = program.getId();

        defaultProgramShouldBeFound("id.equals=" + id);
        defaultProgramShouldNotBeFound("id.notEquals=" + id);

        defaultProgramShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultProgramShouldNotBeFound("id.greaterThan=" + id);

        defaultProgramShouldBeFound("id.lessThanOrEqual=" + id);
        defaultProgramShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllProgramsByProgramIdIsEqualToSomething() throws Exception {
        // Initialize the database
        programRepository.saveAndFlush(program);

        // Get all the programList where programId equals to DEFAULT_PROGRAM_ID
        defaultProgramShouldBeFound("programId.equals=" + DEFAULT_PROGRAM_ID);

        // Get all the programList where programId equals to UPDATED_PROGRAM_ID
        defaultProgramShouldNotBeFound("programId.equals=" + UPDATED_PROGRAM_ID);
    }

    @Test
    @Transactional
    public void getAllProgramsByProgramIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        programRepository.saveAndFlush(program);

        // Get all the programList where programId not equals to DEFAULT_PROGRAM_ID
        defaultProgramShouldNotBeFound("programId.notEquals=" + DEFAULT_PROGRAM_ID);

        // Get all the programList where programId not equals to UPDATED_PROGRAM_ID
        defaultProgramShouldBeFound("programId.notEquals=" + UPDATED_PROGRAM_ID);
    }

    @Test
    @Transactional
    public void getAllProgramsByProgramIdIsInShouldWork() throws Exception {
        // Initialize the database
        programRepository.saveAndFlush(program);

        // Get all the programList where programId in DEFAULT_PROGRAM_ID or UPDATED_PROGRAM_ID
        defaultProgramShouldBeFound("programId.in=" + DEFAULT_PROGRAM_ID + "," + UPDATED_PROGRAM_ID);

        // Get all the programList where programId equals to UPDATED_PROGRAM_ID
        defaultProgramShouldNotBeFound("programId.in=" + UPDATED_PROGRAM_ID);
    }

    @Test
    @Transactional
    public void getAllProgramsByProgramIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        programRepository.saveAndFlush(program);

        // Get all the programList where programId is not null
        defaultProgramShouldBeFound("programId.specified=true");

        // Get all the programList where programId is null
        defaultProgramShouldNotBeFound("programId.specified=false");
    }

    @Test
    @Transactional
    public void getAllProgramsByProgramIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        programRepository.saveAndFlush(program);

        // Get all the programList where programId is greater than or equal to DEFAULT_PROGRAM_ID
        defaultProgramShouldBeFound("programId.greaterThanOrEqual=" + DEFAULT_PROGRAM_ID);

        // Get all the programList where programId is greater than or equal to UPDATED_PROGRAM_ID
        defaultProgramShouldNotBeFound("programId.greaterThanOrEqual=" + UPDATED_PROGRAM_ID);
    }

    @Test
    @Transactional
    public void getAllProgramsByProgramIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        programRepository.saveAndFlush(program);

        // Get all the programList where programId is less than or equal to DEFAULT_PROGRAM_ID
        defaultProgramShouldBeFound("programId.lessThanOrEqual=" + DEFAULT_PROGRAM_ID);

        // Get all the programList where programId is less than or equal to SMALLER_PROGRAM_ID
        defaultProgramShouldNotBeFound("programId.lessThanOrEqual=" + SMALLER_PROGRAM_ID);
    }

    @Test
    @Transactional
    public void getAllProgramsByProgramIdIsLessThanSomething() throws Exception {
        // Initialize the database
        programRepository.saveAndFlush(program);

        // Get all the programList where programId is less than DEFAULT_PROGRAM_ID
        defaultProgramShouldNotBeFound("programId.lessThan=" + DEFAULT_PROGRAM_ID);

        // Get all the programList where programId is less than UPDATED_PROGRAM_ID
        defaultProgramShouldBeFound("programId.lessThan=" + UPDATED_PROGRAM_ID);
    }

    @Test
    @Transactional
    public void getAllProgramsByProgramIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        programRepository.saveAndFlush(program);

        // Get all the programList where programId is greater than DEFAULT_PROGRAM_ID
        defaultProgramShouldNotBeFound("programId.greaterThan=" + DEFAULT_PROGRAM_ID);

        // Get all the programList where programId is greater than SMALLER_PROGRAM_ID
        defaultProgramShouldBeFound("programId.greaterThan=" + SMALLER_PROGRAM_ID);
    }


    @Test
    @Transactional
    public void getAllProgramsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        programRepository.saveAndFlush(program);

        // Get all the programList where name equals to DEFAULT_NAME
        defaultProgramShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the programList where name equals to UPDATED_NAME
        defaultProgramShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllProgramsByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        programRepository.saveAndFlush(program);

        // Get all the programList where name not equals to DEFAULT_NAME
        defaultProgramShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the programList where name not equals to UPDATED_NAME
        defaultProgramShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllProgramsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        programRepository.saveAndFlush(program);

        // Get all the programList where name in DEFAULT_NAME or UPDATED_NAME
        defaultProgramShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the programList where name equals to UPDATED_NAME
        defaultProgramShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllProgramsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        programRepository.saveAndFlush(program);

        // Get all the programList where name is not null
        defaultProgramShouldBeFound("name.specified=true");

        // Get all the programList where name is null
        defaultProgramShouldNotBeFound("name.specified=false");
    }
                @Test
    @Transactional
    public void getAllProgramsByNameContainsSomething() throws Exception {
        // Initialize the database
        programRepository.saveAndFlush(program);

        // Get all the programList where name contains DEFAULT_NAME
        defaultProgramShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the programList where name contains UPDATED_NAME
        defaultProgramShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllProgramsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        programRepository.saveAndFlush(program);

        // Get all the programList where name does not contain DEFAULT_NAME
        defaultProgramShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the programList where name does not contain UPDATED_NAME
        defaultProgramShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }


    @Test
    @Transactional
    public void getAllProgramsByCreatedOnIsEqualToSomething() throws Exception {
        // Initialize the database
        programRepository.saveAndFlush(program);

        // Get all the programList where createdOn equals to DEFAULT_CREATED_ON
        defaultProgramShouldBeFound("createdOn.equals=" + DEFAULT_CREATED_ON);

        // Get all the programList where createdOn equals to UPDATED_CREATED_ON
        defaultProgramShouldNotBeFound("createdOn.equals=" + UPDATED_CREATED_ON);
    }

    @Test
    @Transactional
    public void getAllProgramsByCreatedOnIsNotEqualToSomething() throws Exception {
        // Initialize the database
        programRepository.saveAndFlush(program);

        // Get all the programList where createdOn not equals to DEFAULT_CREATED_ON
        defaultProgramShouldNotBeFound("createdOn.notEquals=" + DEFAULT_CREATED_ON);

        // Get all the programList where createdOn not equals to UPDATED_CREATED_ON
        defaultProgramShouldBeFound("createdOn.notEquals=" + UPDATED_CREATED_ON);
    }

    @Test
    @Transactional
    public void getAllProgramsByCreatedOnIsInShouldWork() throws Exception {
        // Initialize the database
        programRepository.saveAndFlush(program);

        // Get all the programList where createdOn in DEFAULT_CREATED_ON or UPDATED_CREATED_ON
        defaultProgramShouldBeFound("createdOn.in=" + DEFAULT_CREATED_ON + "," + UPDATED_CREATED_ON);

        // Get all the programList where createdOn equals to UPDATED_CREATED_ON
        defaultProgramShouldNotBeFound("createdOn.in=" + UPDATED_CREATED_ON);
    }

    @Test
    @Transactional
    public void getAllProgramsByCreatedOnIsNullOrNotNull() throws Exception {
        // Initialize the database
        programRepository.saveAndFlush(program);

        // Get all the programList where createdOn is not null
        defaultProgramShouldBeFound("createdOn.specified=true");

        // Get all the programList where createdOn is null
        defaultProgramShouldNotBeFound("createdOn.specified=false");
    }

    @Test
    @Transactional
    public void getAllProgramsByCreatedOnIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        programRepository.saveAndFlush(program);

        // Get all the programList where createdOn is greater than or equal to DEFAULT_CREATED_ON
        defaultProgramShouldBeFound("createdOn.greaterThanOrEqual=" + DEFAULT_CREATED_ON);

        // Get all the programList where createdOn is greater than or equal to UPDATED_CREATED_ON
        defaultProgramShouldNotBeFound("createdOn.greaterThanOrEqual=" + UPDATED_CREATED_ON);
    }

    @Test
    @Transactional
    public void getAllProgramsByCreatedOnIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        programRepository.saveAndFlush(program);

        // Get all the programList where createdOn is less than or equal to DEFAULT_CREATED_ON
        defaultProgramShouldBeFound("createdOn.lessThanOrEqual=" + DEFAULT_CREATED_ON);

        // Get all the programList where createdOn is less than or equal to SMALLER_CREATED_ON
        defaultProgramShouldNotBeFound("createdOn.lessThanOrEqual=" + SMALLER_CREATED_ON);
    }

    @Test
    @Transactional
    public void getAllProgramsByCreatedOnIsLessThanSomething() throws Exception {
        // Initialize the database
        programRepository.saveAndFlush(program);

        // Get all the programList where createdOn is less than DEFAULT_CREATED_ON
        defaultProgramShouldNotBeFound("createdOn.lessThan=" + DEFAULT_CREATED_ON);

        // Get all the programList where createdOn is less than UPDATED_CREATED_ON
        defaultProgramShouldBeFound("createdOn.lessThan=" + UPDATED_CREATED_ON);
    }

    @Test
    @Transactional
    public void getAllProgramsByCreatedOnIsGreaterThanSomething() throws Exception {
        // Initialize the database
        programRepository.saveAndFlush(program);

        // Get all the programList where createdOn is greater than DEFAULT_CREATED_ON
        defaultProgramShouldNotBeFound("createdOn.greaterThan=" + DEFAULT_CREATED_ON);

        // Get all the programList where createdOn is greater than SMALLER_CREATED_ON
        defaultProgramShouldBeFound("createdOn.greaterThan=" + SMALLER_CREATED_ON);
    }


    @Test
    @Transactional
    public void getAllProgramsByModifiedOnIsEqualToSomething() throws Exception {
        // Initialize the database
        programRepository.saveAndFlush(program);

        // Get all the programList where modifiedOn equals to DEFAULT_MODIFIED_ON
        defaultProgramShouldBeFound("modifiedOn.equals=" + DEFAULT_MODIFIED_ON);

        // Get all the programList where modifiedOn equals to UPDATED_MODIFIED_ON
        defaultProgramShouldNotBeFound("modifiedOn.equals=" + UPDATED_MODIFIED_ON);
    }

    @Test
    @Transactional
    public void getAllProgramsByModifiedOnIsNotEqualToSomething() throws Exception {
        // Initialize the database
        programRepository.saveAndFlush(program);

        // Get all the programList where modifiedOn not equals to DEFAULT_MODIFIED_ON
        defaultProgramShouldNotBeFound("modifiedOn.notEquals=" + DEFAULT_MODIFIED_ON);

        // Get all the programList where modifiedOn not equals to UPDATED_MODIFIED_ON
        defaultProgramShouldBeFound("modifiedOn.notEquals=" + UPDATED_MODIFIED_ON);
    }

    @Test
    @Transactional
    public void getAllProgramsByModifiedOnIsInShouldWork() throws Exception {
        // Initialize the database
        programRepository.saveAndFlush(program);

        // Get all the programList where modifiedOn in DEFAULT_MODIFIED_ON or UPDATED_MODIFIED_ON
        defaultProgramShouldBeFound("modifiedOn.in=" + DEFAULT_MODIFIED_ON + "," + UPDATED_MODIFIED_ON);

        // Get all the programList where modifiedOn equals to UPDATED_MODIFIED_ON
        defaultProgramShouldNotBeFound("modifiedOn.in=" + UPDATED_MODIFIED_ON);
    }

    @Test
    @Transactional
    public void getAllProgramsByModifiedOnIsNullOrNotNull() throws Exception {
        // Initialize the database
        programRepository.saveAndFlush(program);

        // Get all the programList where modifiedOn is not null
        defaultProgramShouldBeFound("modifiedOn.specified=true");

        // Get all the programList where modifiedOn is null
        defaultProgramShouldNotBeFound("modifiedOn.specified=false");
    }

    @Test
    @Transactional
    public void getAllProgramsByModifiedOnIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        programRepository.saveAndFlush(program);

        // Get all the programList where modifiedOn is greater than or equal to DEFAULT_MODIFIED_ON
        defaultProgramShouldBeFound("modifiedOn.greaterThanOrEqual=" + DEFAULT_MODIFIED_ON);

        // Get all the programList where modifiedOn is greater than or equal to UPDATED_MODIFIED_ON
        defaultProgramShouldNotBeFound("modifiedOn.greaterThanOrEqual=" + UPDATED_MODIFIED_ON);
    }

    @Test
    @Transactional
    public void getAllProgramsByModifiedOnIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        programRepository.saveAndFlush(program);

        // Get all the programList where modifiedOn is less than or equal to DEFAULT_MODIFIED_ON
        defaultProgramShouldBeFound("modifiedOn.lessThanOrEqual=" + DEFAULT_MODIFIED_ON);

        // Get all the programList where modifiedOn is less than or equal to SMALLER_MODIFIED_ON
        defaultProgramShouldNotBeFound("modifiedOn.lessThanOrEqual=" + SMALLER_MODIFIED_ON);
    }

    @Test
    @Transactional
    public void getAllProgramsByModifiedOnIsLessThanSomething() throws Exception {
        // Initialize the database
        programRepository.saveAndFlush(program);

        // Get all the programList where modifiedOn is less than DEFAULT_MODIFIED_ON
        defaultProgramShouldNotBeFound("modifiedOn.lessThan=" + DEFAULT_MODIFIED_ON);

        // Get all the programList where modifiedOn is less than UPDATED_MODIFIED_ON
        defaultProgramShouldBeFound("modifiedOn.lessThan=" + UPDATED_MODIFIED_ON);
    }

    @Test
    @Transactional
    public void getAllProgramsByModifiedOnIsGreaterThanSomething() throws Exception {
        // Initialize the database
        programRepository.saveAndFlush(program);

        // Get all the programList where modifiedOn is greater than DEFAULT_MODIFIED_ON
        defaultProgramShouldNotBeFound("modifiedOn.greaterThan=" + DEFAULT_MODIFIED_ON);

        // Get all the programList where modifiedOn is greater than SMALLER_MODIFIED_ON
        defaultProgramShouldBeFound("modifiedOn.greaterThan=" + SMALLER_MODIFIED_ON);
    }


    @Test
    @Transactional
    public void getAllProgramsByModifiedByIsEqualToSomething() throws Exception {
        // Initialize the database
        programRepository.saveAndFlush(program);

        // Get all the programList where modifiedBy equals to DEFAULT_MODIFIED_BY
        defaultProgramShouldBeFound("modifiedBy.equals=" + DEFAULT_MODIFIED_BY);

        // Get all the programList where modifiedBy equals to UPDATED_MODIFIED_BY
        defaultProgramShouldNotBeFound("modifiedBy.equals=" + UPDATED_MODIFIED_BY);
    }

    @Test
    @Transactional
    public void getAllProgramsByModifiedByIsNotEqualToSomething() throws Exception {
        // Initialize the database
        programRepository.saveAndFlush(program);

        // Get all the programList where modifiedBy not equals to DEFAULT_MODIFIED_BY
        defaultProgramShouldNotBeFound("modifiedBy.notEquals=" + DEFAULT_MODIFIED_BY);

        // Get all the programList where modifiedBy not equals to UPDATED_MODIFIED_BY
        defaultProgramShouldBeFound("modifiedBy.notEquals=" + UPDATED_MODIFIED_BY);
    }

    @Test
    @Transactional
    public void getAllProgramsByModifiedByIsInShouldWork() throws Exception {
        // Initialize the database
        programRepository.saveAndFlush(program);

        // Get all the programList where modifiedBy in DEFAULT_MODIFIED_BY or UPDATED_MODIFIED_BY
        defaultProgramShouldBeFound("modifiedBy.in=" + DEFAULT_MODIFIED_BY + "," + UPDATED_MODIFIED_BY);

        // Get all the programList where modifiedBy equals to UPDATED_MODIFIED_BY
        defaultProgramShouldNotBeFound("modifiedBy.in=" + UPDATED_MODIFIED_BY);
    }

    @Test
    @Transactional
    public void getAllProgramsByModifiedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        programRepository.saveAndFlush(program);

        // Get all the programList where modifiedBy is not null
        defaultProgramShouldBeFound("modifiedBy.specified=true");

        // Get all the programList where modifiedBy is null
        defaultProgramShouldNotBeFound("modifiedBy.specified=false");
    }
                @Test
    @Transactional
    public void getAllProgramsByModifiedByContainsSomething() throws Exception {
        // Initialize the database
        programRepository.saveAndFlush(program);

        // Get all the programList where modifiedBy contains DEFAULT_MODIFIED_BY
        defaultProgramShouldBeFound("modifiedBy.contains=" + DEFAULT_MODIFIED_BY);

        // Get all the programList where modifiedBy contains UPDATED_MODIFIED_BY
        defaultProgramShouldNotBeFound("modifiedBy.contains=" + UPDATED_MODIFIED_BY);
    }

    @Test
    @Transactional
    public void getAllProgramsByModifiedByNotContainsSomething() throws Exception {
        // Initialize the database
        programRepository.saveAndFlush(program);

        // Get all the programList where modifiedBy does not contain DEFAULT_MODIFIED_BY
        defaultProgramShouldNotBeFound("modifiedBy.doesNotContain=" + DEFAULT_MODIFIED_BY);

        // Get all the programList where modifiedBy does not contain UPDATED_MODIFIED_BY
        defaultProgramShouldBeFound("modifiedBy.doesNotContain=" + UPDATED_MODIFIED_BY);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultProgramShouldBeFound(String filter) throws Exception {
        restProgramMockMvc.perform(get("/api/programs?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(program.getId().intValue())))
            .andExpect(jsonPath("$.[*].programId").value(hasItem(DEFAULT_PROGRAM_ID)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].createdOn").value(hasItem(DEFAULT_CREATED_ON.toString())))
            .andExpect(jsonPath("$.[*].modifiedOn").value(hasItem(DEFAULT_MODIFIED_ON.toString())))
            .andExpect(jsonPath("$.[*].modifiedBy").value(hasItem(DEFAULT_MODIFIED_BY)));

        // Check, that the count call also returns 1
        restProgramMockMvc.perform(get("/api/programs/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultProgramShouldNotBeFound(String filter) throws Exception {
        restProgramMockMvc.perform(get("/api/programs?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restProgramMockMvc.perform(get("/api/programs/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingProgram() throws Exception {
        // Get the program
        restProgramMockMvc.perform(get("/api/programs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProgram() throws Exception {
        // Initialize the database
        programRepository.saveAndFlush(program);

        int databaseSizeBeforeUpdate = programRepository.findAll().size();

        // Update the program
        Program updatedProgram = programRepository.findById(program.getId()).get();
        // Disconnect from session so that the updates on updatedProgram are not directly saved in db
        em.detach(updatedProgram);
        updatedProgram
            .programId(UPDATED_PROGRAM_ID)
            .name(UPDATED_NAME)
            .createdOn(UPDATED_CREATED_ON)
            .modifiedOn(UPDATED_MODIFIED_ON)
            .modifiedBy(UPDATED_MODIFIED_BY);
        ProgramDTO programDTO = programMapper.toDto(updatedProgram);

        restProgramMockMvc.perform(put("/api/programs")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(programDTO)))
            .andExpect(status().isOk());

        // Validate the Program in the database
        List<Program> programList = programRepository.findAll();
        assertThat(programList).hasSize(databaseSizeBeforeUpdate);
        Program testProgram = programList.get(programList.size() - 1);
        assertThat(testProgram.getProgramId()).isEqualTo(UPDATED_PROGRAM_ID);
        assertThat(testProgram.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testProgram.getCreatedOn()).isEqualTo(UPDATED_CREATED_ON);
        assertThat(testProgram.getModifiedOn()).isEqualTo(UPDATED_MODIFIED_ON);
        assertThat(testProgram.getModifiedBy()).isEqualTo(UPDATED_MODIFIED_BY);
    }

    @Test
    @Transactional
    public void updateNonExistingProgram() throws Exception {
        int databaseSizeBeforeUpdate = programRepository.findAll().size();

        // Create the Program
        ProgramDTO programDTO = programMapper.toDto(program);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProgramMockMvc.perform(put("/api/programs")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(programDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Program in the database
        List<Program> programList = programRepository.findAll();
        assertThat(programList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteProgram() throws Exception {
        // Initialize the database
        programRepository.saveAndFlush(program);

        int databaseSizeBeforeDelete = programRepository.findAll().size();

        // Delete the program
        restProgramMockMvc.perform(delete("/api/programs/{id}", program.getId())
            .accept(TestUtil.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Program> programList = programRepository.findAll();
        assertThat(programList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
