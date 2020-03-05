package org.ums.web.rest;

import org.ums.UgAdmissionApp;
import org.ums.domain.Semester;
import org.ums.repository.SemesterRepository;
import org.ums.service.SemesterService;
import org.ums.service.dto.SemesterDTO;
import org.ums.service.mapper.SemesterMapper;
import org.ums.web.rest.errors.ExceptionTranslator;
import org.ums.service.dto.SemesterCriteria;
import org.ums.service.SemesterQueryService;

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

import org.ums.domain.enumeration.SemesterStatus;
/**
 * Integration tests for the {@link SemesterResource} REST controller.
 */
@SpringBootTest(classes = UgAdmissionApp.class)
public class SemesterResourceIT {

    private static final Integer DEFAULT_SEMESTER_ID = 1;
    private static final Integer UPDATED_SEMESTER_ID = 2;
    private static final Integer SMALLER_SEMESTER_ID = 1 - 1;

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_SHORT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_SHORT_NAME = "BBBBBBBBBB";

    private static final SemesterStatus DEFAULT_STATUS = SemesterStatus.ACTIVE;
    private static final SemesterStatus UPDATED_STATUS = SemesterStatus.INACTIVE;

    private static final LocalDate DEFAULT_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_START_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_START_DATE = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_END_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_END_DATE = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_CREATED_ON = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATED_ON = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_CREATED_ON = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_MODIFIED_ON = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_MODIFIED_ON = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_MODIFIED_ON = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_MODIFIED_BY = "AAAAAAAAAA";
    private static final String UPDATED_MODIFIED_BY = "BBBBBBBBBB";

    @Autowired
    private SemesterRepository semesterRepository;

    @Autowired
    private SemesterMapper semesterMapper;

    @Autowired
    private SemesterService semesterService;

    @Autowired
    private SemesterQueryService semesterQueryService;

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

    private MockMvc restSemesterMockMvc;

    private Semester semester;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SemesterResource semesterResource = new SemesterResource(semesterService, semesterQueryService);
        this.restSemesterMockMvc = MockMvcBuilders.standaloneSetup(semesterResource)
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
    public static Semester createEntity(EntityManager em) {
        Semester semester = new Semester()
            .semesterId(DEFAULT_SEMESTER_ID)
            .name(DEFAULT_NAME)
            .shortName(DEFAULT_SHORT_NAME)
            .status(DEFAULT_STATUS)
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE)
            .createdOn(DEFAULT_CREATED_ON)
            .modifiedOn(DEFAULT_MODIFIED_ON)
            .modifiedBy(DEFAULT_MODIFIED_BY);
        return semester;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Semester createUpdatedEntity(EntityManager em) {
        Semester semester = new Semester()
            .semesterId(UPDATED_SEMESTER_ID)
            .name(UPDATED_NAME)
            .shortName(UPDATED_SHORT_NAME)
            .status(UPDATED_STATUS)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .createdOn(UPDATED_CREATED_ON)
            .modifiedOn(UPDATED_MODIFIED_ON)
            .modifiedBy(UPDATED_MODIFIED_BY);
        return semester;
    }

    @BeforeEach
    public void initTest() {
        semester = createEntity(em);
    }

    @Test
    @Transactional
    public void createSemester() throws Exception {
        int databaseSizeBeforeCreate = semesterRepository.findAll().size();

        // Create the Semester
        SemesterDTO semesterDTO = semesterMapper.toDto(semester);
        restSemesterMockMvc.perform(post("/api/semesters")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(semesterDTO)))
            .andExpect(status().isCreated());

        // Validate the Semester in the database
        List<Semester> semesterList = semesterRepository.findAll();
        assertThat(semesterList).hasSize(databaseSizeBeforeCreate + 1);
        Semester testSemester = semesterList.get(semesterList.size() - 1);
        assertThat(testSemester.getSemesterId()).isEqualTo(DEFAULT_SEMESTER_ID);
        assertThat(testSemester.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testSemester.getShortName()).isEqualTo(DEFAULT_SHORT_NAME);
        assertThat(testSemester.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testSemester.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testSemester.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testSemester.getCreatedOn()).isEqualTo(DEFAULT_CREATED_ON);
        assertThat(testSemester.getModifiedOn()).isEqualTo(DEFAULT_MODIFIED_ON);
        assertThat(testSemester.getModifiedBy()).isEqualTo(DEFAULT_MODIFIED_BY);
    }

    @Test
    @Transactional
    public void createSemesterWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = semesterRepository.findAll().size();

        // Create the Semester with an existing ID
        semester.setId(1L);
        SemesterDTO semesterDTO = semesterMapper.toDto(semester);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSemesterMockMvc.perform(post("/api/semesters")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(semesterDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Semester in the database
        List<Semester> semesterList = semesterRepository.findAll();
        assertThat(semesterList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkSemesterIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = semesterRepository.findAll().size();
        // set the field null
        semester.setSemesterId(null);

        // Create the Semester, which fails.
        SemesterDTO semesterDTO = semesterMapper.toDto(semester);

        restSemesterMockMvc.perform(post("/api/semesters")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(semesterDTO)))
            .andExpect(status().isBadRequest());

        List<Semester> semesterList = semesterRepository.findAll();
        assertThat(semesterList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = semesterRepository.findAll().size();
        // set the field null
        semester.setName(null);

        // Create the Semester, which fails.
        SemesterDTO semesterDTO = semesterMapper.toDto(semester);

        restSemesterMockMvc.perform(post("/api/semesters")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(semesterDTO)))
            .andExpect(status().isBadRequest());

        List<Semester> semesterList = semesterRepository.findAll();
        assertThat(semesterList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkShortNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = semesterRepository.findAll().size();
        // set the field null
        semester.setShortName(null);

        // Create the Semester, which fails.
        SemesterDTO semesterDTO = semesterMapper.toDto(semester);

        restSemesterMockMvc.perform(post("/api/semesters")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(semesterDTO)))
            .andExpect(status().isBadRequest());

        List<Semester> semesterList = semesterRepository.findAll();
        assertThat(semesterList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = semesterRepository.findAll().size();
        // set the field null
        semester.setStatus(null);

        // Create the Semester, which fails.
        SemesterDTO semesterDTO = semesterMapper.toDto(semester);

        restSemesterMockMvc.perform(post("/api/semesters")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(semesterDTO)))
            .andExpect(status().isBadRequest());

        List<Semester> semesterList = semesterRepository.findAll();
        assertThat(semesterList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStartDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = semesterRepository.findAll().size();
        // set the field null
        semester.setStartDate(null);

        // Create the Semester, which fails.
        SemesterDTO semesterDTO = semesterMapper.toDto(semester);

        restSemesterMockMvc.perform(post("/api/semesters")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(semesterDTO)))
            .andExpect(status().isBadRequest());

        List<Semester> semesterList = semesterRepository.findAll();
        assertThat(semesterList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEndDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = semesterRepository.findAll().size();
        // set the field null
        semester.setEndDate(null);

        // Create the Semester, which fails.
        SemesterDTO semesterDTO = semesterMapper.toDto(semester);

        restSemesterMockMvc.perform(post("/api/semesters")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(semesterDTO)))
            .andExpect(status().isBadRequest());

        List<Semester> semesterList = semesterRepository.findAll();
        assertThat(semesterList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSemesters() throws Exception {
        // Initialize the database
        semesterRepository.saveAndFlush(semester);

        // Get all the semesterList
        restSemesterMockMvc.perform(get("/api/semesters?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(semester.getId().intValue())))
            .andExpect(jsonPath("$.[*].semesterId").value(hasItem(DEFAULT_SEMESTER_ID)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].shortName").value(hasItem(DEFAULT_SHORT_NAME)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdOn").value(hasItem(DEFAULT_CREATED_ON.toString())))
            .andExpect(jsonPath("$.[*].modifiedOn").value(hasItem(DEFAULT_MODIFIED_ON.toString())))
            .andExpect(jsonPath("$.[*].modifiedBy").value(hasItem(DEFAULT_MODIFIED_BY)));
    }
    
    @Test
    @Transactional
    public void getSemester() throws Exception {
        // Initialize the database
        semesterRepository.saveAndFlush(semester);

        // Get the semester
        restSemesterMockMvc.perform(get("/api/semesters/{id}", semester.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(semester.getId().intValue()))
            .andExpect(jsonPath("$.semesterId").value(DEFAULT_SEMESTER_ID))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.shortName").value(DEFAULT_SHORT_NAME))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.createdOn").value(DEFAULT_CREATED_ON.toString()))
            .andExpect(jsonPath("$.modifiedOn").value(DEFAULT_MODIFIED_ON.toString()))
            .andExpect(jsonPath("$.modifiedBy").value(DEFAULT_MODIFIED_BY));
    }


    @Test
    @Transactional
    public void getSemestersByIdFiltering() throws Exception {
        // Initialize the database
        semesterRepository.saveAndFlush(semester);

        Long id = semester.getId();

        defaultSemesterShouldBeFound("id.equals=" + id);
        defaultSemesterShouldNotBeFound("id.notEquals=" + id);

        defaultSemesterShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultSemesterShouldNotBeFound("id.greaterThan=" + id);

        defaultSemesterShouldBeFound("id.lessThanOrEqual=" + id);
        defaultSemesterShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllSemestersBySemesterIdIsEqualToSomething() throws Exception {
        // Initialize the database
        semesterRepository.saveAndFlush(semester);

        // Get all the semesterList where semesterId equals to DEFAULT_SEMESTER_ID
        defaultSemesterShouldBeFound("semesterId.equals=" + DEFAULT_SEMESTER_ID);

        // Get all the semesterList where semesterId equals to UPDATED_SEMESTER_ID
        defaultSemesterShouldNotBeFound("semesterId.equals=" + UPDATED_SEMESTER_ID);
    }

    @Test
    @Transactional
    public void getAllSemestersBySemesterIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        semesterRepository.saveAndFlush(semester);

        // Get all the semesterList where semesterId not equals to DEFAULT_SEMESTER_ID
        defaultSemesterShouldNotBeFound("semesterId.notEquals=" + DEFAULT_SEMESTER_ID);

        // Get all the semesterList where semesterId not equals to UPDATED_SEMESTER_ID
        defaultSemesterShouldBeFound("semesterId.notEquals=" + UPDATED_SEMESTER_ID);
    }

    @Test
    @Transactional
    public void getAllSemestersBySemesterIdIsInShouldWork() throws Exception {
        // Initialize the database
        semesterRepository.saveAndFlush(semester);

        // Get all the semesterList where semesterId in DEFAULT_SEMESTER_ID or UPDATED_SEMESTER_ID
        defaultSemesterShouldBeFound("semesterId.in=" + DEFAULT_SEMESTER_ID + "," + UPDATED_SEMESTER_ID);

        // Get all the semesterList where semesterId equals to UPDATED_SEMESTER_ID
        defaultSemesterShouldNotBeFound("semesterId.in=" + UPDATED_SEMESTER_ID);
    }

    @Test
    @Transactional
    public void getAllSemestersBySemesterIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        semesterRepository.saveAndFlush(semester);

        // Get all the semesterList where semesterId is not null
        defaultSemesterShouldBeFound("semesterId.specified=true");

        // Get all the semesterList where semesterId is null
        defaultSemesterShouldNotBeFound("semesterId.specified=false");
    }

    @Test
    @Transactional
    public void getAllSemestersBySemesterIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        semesterRepository.saveAndFlush(semester);

        // Get all the semesterList where semesterId is greater than or equal to DEFAULT_SEMESTER_ID
        defaultSemesterShouldBeFound("semesterId.greaterThanOrEqual=" + DEFAULT_SEMESTER_ID);

        // Get all the semesterList where semesterId is greater than or equal to UPDATED_SEMESTER_ID
        defaultSemesterShouldNotBeFound("semesterId.greaterThanOrEqual=" + UPDATED_SEMESTER_ID);
    }

    @Test
    @Transactional
    public void getAllSemestersBySemesterIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        semesterRepository.saveAndFlush(semester);

        // Get all the semesterList where semesterId is less than or equal to DEFAULT_SEMESTER_ID
        defaultSemesterShouldBeFound("semesterId.lessThanOrEqual=" + DEFAULT_SEMESTER_ID);

        // Get all the semesterList where semesterId is less than or equal to SMALLER_SEMESTER_ID
        defaultSemesterShouldNotBeFound("semesterId.lessThanOrEqual=" + SMALLER_SEMESTER_ID);
    }

    @Test
    @Transactional
    public void getAllSemestersBySemesterIdIsLessThanSomething() throws Exception {
        // Initialize the database
        semesterRepository.saveAndFlush(semester);

        // Get all the semesterList where semesterId is less than DEFAULT_SEMESTER_ID
        defaultSemesterShouldNotBeFound("semesterId.lessThan=" + DEFAULT_SEMESTER_ID);

        // Get all the semesterList where semesterId is less than UPDATED_SEMESTER_ID
        defaultSemesterShouldBeFound("semesterId.lessThan=" + UPDATED_SEMESTER_ID);
    }

    @Test
    @Transactional
    public void getAllSemestersBySemesterIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        semesterRepository.saveAndFlush(semester);

        // Get all the semesterList where semesterId is greater than DEFAULT_SEMESTER_ID
        defaultSemesterShouldNotBeFound("semesterId.greaterThan=" + DEFAULT_SEMESTER_ID);

        // Get all the semesterList where semesterId is greater than SMALLER_SEMESTER_ID
        defaultSemesterShouldBeFound("semesterId.greaterThan=" + SMALLER_SEMESTER_ID);
    }


    @Test
    @Transactional
    public void getAllSemestersByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        semesterRepository.saveAndFlush(semester);

        // Get all the semesterList where name equals to DEFAULT_NAME
        defaultSemesterShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the semesterList where name equals to UPDATED_NAME
        defaultSemesterShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllSemestersByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        semesterRepository.saveAndFlush(semester);

        // Get all the semesterList where name not equals to DEFAULT_NAME
        defaultSemesterShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the semesterList where name not equals to UPDATED_NAME
        defaultSemesterShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllSemestersByNameIsInShouldWork() throws Exception {
        // Initialize the database
        semesterRepository.saveAndFlush(semester);

        // Get all the semesterList where name in DEFAULT_NAME or UPDATED_NAME
        defaultSemesterShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the semesterList where name equals to UPDATED_NAME
        defaultSemesterShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllSemestersByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        semesterRepository.saveAndFlush(semester);

        // Get all the semesterList where name is not null
        defaultSemesterShouldBeFound("name.specified=true");

        // Get all the semesterList where name is null
        defaultSemesterShouldNotBeFound("name.specified=false");
    }
                @Test
    @Transactional
    public void getAllSemestersByNameContainsSomething() throws Exception {
        // Initialize the database
        semesterRepository.saveAndFlush(semester);

        // Get all the semesterList where name contains DEFAULT_NAME
        defaultSemesterShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the semesterList where name contains UPDATED_NAME
        defaultSemesterShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllSemestersByNameNotContainsSomething() throws Exception {
        // Initialize the database
        semesterRepository.saveAndFlush(semester);

        // Get all the semesterList where name does not contain DEFAULT_NAME
        defaultSemesterShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the semesterList where name does not contain UPDATED_NAME
        defaultSemesterShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }


    @Test
    @Transactional
    public void getAllSemestersByShortNameIsEqualToSomething() throws Exception {
        // Initialize the database
        semesterRepository.saveAndFlush(semester);

        // Get all the semesterList where shortName equals to DEFAULT_SHORT_NAME
        defaultSemesterShouldBeFound("shortName.equals=" + DEFAULT_SHORT_NAME);

        // Get all the semesterList where shortName equals to UPDATED_SHORT_NAME
        defaultSemesterShouldNotBeFound("shortName.equals=" + UPDATED_SHORT_NAME);
    }

    @Test
    @Transactional
    public void getAllSemestersByShortNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        semesterRepository.saveAndFlush(semester);

        // Get all the semesterList where shortName not equals to DEFAULT_SHORT_NAME
        defaultSemesterShouldNotBeFound("shortName.notEquals=" + DEFAULT_SHORT_NAME);

        // Get all the semesterList where shortName not equals to UPDATED_SHORT_NAME
        defaultSemesterShouldBeFound("shortName.notEquals=" + UPDATED_SHORT_NAME);
    }

    @Test
    @Transactional
    public void getAllSemestersByShortNameIsInShouldWork() throws Exception {
        // Initialize the database
        semesterRepository.saveAndFlush(semester);

        // Get all the semesterList where shortName in DEFAULT_SHORT_NAME or UPDATED_SHORT_NAME
        defaultSemesterShouldBeFound("shortName.in=" + DEFAULT_SHORT_NAME + "," + UPDATED_SHORT_NAME);

        // Get all the semesterList where shortName equals to UPDATED_SHORT_NAME
        defaultSemesterShouldNotBeFound("shortName.in=" + UPDATED_SHORT_NAME);
    }

    @Test
    @Transactional
    public void getAllSemestersByShortNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        semesterRepository.saveAndFlush(semester);

        // Get all the semesterList where shortName is not null
        defaultSemesterShouldBeFound("shortName.specified=true");

        // Get all the semesterList where shortName is null
        defaultSemesterShouldNotBeFound("shortName.specified=false");
    }
                @Test
    @Transactional
    public void getAllSemestersByShortNameContainsSomething() throws Exception {
        // Initialize the database
        semesterRepository.saveAndFlush(semester);

        // Get all the semesterList where shortName contains DEFAULT_SHORT_NAME
        defaultSemesterShouldBeFound("shortName.contains=" + DEFAULT_SHORT_NAME);

        // Get all the semesterList where shortName contains UPDATED_SHORT_NAME
        defaultSemesterShouldNotBeFound("shortName.contains=" + UPDATED_SHORT_NAME);
    }

    @Test
    @Transactional
    public void getAllSemestersByShortNameNotContainsSomething() throws Exception {
        // Initialize the database
        semesterRepository.saveAndFlush(semester);

        // Get all the semesterList where shortName does not contain DEFAULT_SHORT_NAME
        defaultSemesterShouldNotBeFound("shortName.doesNotContain=" + DEFAULT_SHORT_NAME);

        // Get all the semesterList where shortName does not contain UPDATED_SHORT_NAME
        defaultSemesterShouldBeFound("shortName.doesNotContain=" + UPDATED_SHORT_NAME);
    }


    @Test
    @Transactional
    public void getAllSemestersByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        semesterRepository.saveAndFlush(semester);

        // Get all the semesterList where status equals to DEFAULT_STATUS
        defaultSemesterShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the semesterList where status equals to UPDATED_STATUS
        defaultSemesterShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllSemestersByStatusIsNotEqualToSomething() throws Exception {
        // Initialize the database
        semesterRepository.saveAndFlush(semester);

        // Get all the semesterList where status not equals to DEFAULT_STATUS
        defaultSemesterShouldNotBeFound("status.notEquals=" + DEFAULT_STATUS);

        // Get all the semesterList where status not equals to UPDATED_STATUS
        defaultSemesterShouldBeFound("status.notEquals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllSemestersByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        semesterRepository.saveAndFlush(semester);

        // Get all the semesterList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultSemesterShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the semesterList where status equals to UPDATED_STATUS
        defaultSemesterShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllSemestersByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        semesterRepository.saveAndFlush(semester);

        // Get all the semesterList where status is not null
        defaultSemesterShouldBeFound("status.specified=true");

        // Get all the semesterList where status is null
        defaultSemesterShouldNotBeFound("status.specified=false");
    }

    @Test
    @Transactional
    public void getAllSemestersByStartDateIsEqualToSomething() throws Exception {
        // Initialize the database
        semesterRepository.saveAndFlush(semester);

        // Get all the semesterList where startDate equals to DEFAULT_START_DATE
        defaultSemesterShouldBeFound("startDate.equals=" + DEFAULT_START_DATE);

        // Get all the semesterList where startDate equals to UPDATED_START_DATE
        defaultSemesterShouldNotBeFound("startDate.equals=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    public void getAllSemestersByStartDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        semesterRepository.saveAndFlush(semester);

        // Get all the semesterList where startDate not equals to DEFAULT_START_DATE
        defaultSemesterShouldNotBeFound("startDate.notEquals=" + DEFAULT_START_DATE);

        // Get all the semesterList where startDate not equals to UPDATED_START_DATE
        defaultSemesterShouldBeFound("startDate.notEquals=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    public void getAllSemestersByStartDateIsInShouldWork() throws Exception {
        // Initialize the database
        semesterRepository.saveAndFlush(semester);

        // Get all the semesterList where startDate in DEFAULT_START_DATE or UPDATED_START_DATE
        defaultSemesterShouldBeFound("startDate.in=" + DEFAULT_START_DATE + "," + UPDATED_START_DATE);

        // Get all the semesterList where startDate equals to UPDATED_START_DATE
        defaultSemesterShouldNotBeFound("startDate.in=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    public void getAllSemestersByStartDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        semesterRepository.saveAndFlush(semester);

        // Get all the semesterList where startDate is not null
        defaultSemesterShouldBeFound("startDate.specified=true");

        // Get all the semesterList where startDate is null
        defaultSemesterShouldNotBeFound("startDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllSemestersByStartDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        semesterRepository.saveAndFlush(semester);

        // Get all the semesterList where startDate is greater than or equal to DEFAULT_START_DATE
        defaultSemesterShouldBeFound("startDate.greaterThanOrEqual=" + DEFAULT_START_DATE);

        // Get all the semesterList where startDate is greater than or equal to UPDATED_START_DATE
        defaultSemesterShouldNotBeFound("startDate.greaterThanOrEqual=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    public void getAllSemestersByStartDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        semesterRepository.saveAndFlush(semester);

        // Get all the semesterList where startDate is less than or equal to DEFAULT_START_DATE
        defaultSemesterShouldBeFound("startDate.lessThanOrEqual=" + DEFAULT_START_DATE);

        // Get all the semesterList where startDate is less than or equal to SMALLER_START_DATE
        defaultSemesterShouldNotBeFound("startDate.lessThanOrEqual=" + SMALLER_START_DATE);
    }

    @Test
    @Transactional
    public void getAllSemestersByStartDateIsLessThanSomething() throws Exception {
        // Initialize the database
        semesterRepository.saveAndFlush(semester);

        // Get all the semesterList where startDate is less than DEFAULT_START_DATE
        defaultSemesterShouldNotBeFound("startDate.lessThan=" + DEFAULT_START_DATE);

        // Get all the semesterList where startDate is less than UPDATED_START_DATE
        defaultSemesterShouldBeFound("startDate.lessThan=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    public void getAllSemestersByStartDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        semesterRepository.saveAndFlush(semester);

        // Get all the semesterList where startDate is greater than DEFAULT_START_DATE
        defaultSemesterShouldNotBeFound("startDate.greaterThan=" + DEFAULT_START_DATE);

        // Get all the semesterList where startDate is greater than SMALLER_START_DATE
        defaultSemesterShouldBeFound("startDate.greaterThan=" + SMALLER_START_DATE);
    }


    @Test
    @Transactional
    public void getAllSemestersByEndDateIsEqualToSomething() throws Exception {
        // Initialize the database
        semesterRepository.saveAndFlush(semester);

        // Get all the semesterList where endDate equals to DEFAULT_END_DATE
        defaultSemesterShouldBeFound("endDate.equals=" + DEFAULT_END_DATE);

        // Get all the semesterList where endDate equals to UPDATED_END_DATE
        defaultSemesterShouldNotBeFound("endDate.equals=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    public void getAllSemestersByEndDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        semesterRepository.saveAndFlush(semester);

        // Get all the semesterList where endDate not equals to DEFAULT_END_DATE
        defaultSemesterShouldNotBeFound("endDate.notEquals=" + DEFAULT_END_DATE);

        // Get all the semesterList where endDate not equals to UPDATED_END_DATE
        defaultSemesterShouldBeFound("endDate.notEquals=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    public void getAllSemestersByEndDateIsInShouldWork() throws Exception {
        // Initialize the database
        semesterRepository.saveAndFlush(semester);

        // Get all the semesterList where endDate in DEFAULT_END_DATE or UPDATED_END_DATE
        defaultSemesterShouldBeFound("endDate.in=" + DEFAULT_END_DATE + "," + UPDATED_END_DATE);

        // Get all the semesterList where endDate equals to UPDATED_END_DATE
        defaultSemesterShouldNotBeFound("endDate.in=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    public void getAllSemestersByEndDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        semesterRepository.saveAndFlush(semester);

        // Get all the semesterList where endDate is not null
        defaultSemesterShouldBeFound("endDate.specified=true");

        // Get all the semesterList where endDate is null
        defaultSemesterShouldNotBeFound("endDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllSemestersByEndDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        semesterRepository.saveAndFlush(semester);

        // Get all the semesterList where endDate is greater than or equal to DEFAULT_END_DATE
        defaultSemesterShouldBeFound("endDate.greaterThanOrEqual=" + DEFAULT_END_DATE);

        // Get all the semesterList where endDate is greater than or equal to UPDATED_END_DATE
        defaultSemesterShouldNotBeFound("endDate.greaterThanOrEqual=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    public void getAllSemestersByEndDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        semesterRepository.saveAndFlush(semester);

        // Get all the semesterList where endDate is less than or equal to DEFAULT_END_DATE
        defaultSemesterShouldBeFound("endDate.lessThanOrEqual=" + DEFAULT_END_DATE);

        // Get all the semesterList where endDate is less than or equal to SMALLER_END_DATE
        defaultSemesterShouldNotBeFound("endDate.lessThanOrEqual=" + SMALLER_END_DATE);
    }

    @Test
    @Transactional
    public void getAllSemestersByEndDateIsLessThanSomething() throws Exception {
        // Initialize the database
        semesterRepository.saveAndFlush(semester);

        // Get all the semesterList where endDate is less than DEFAULT_END_DATE
        defaultSemesterShouldNotBeFound("endDate.lessThan=" + DEFAULT_END_DATE);

        // Get all the semesterList where endDate is less than UPDATED_END_DATE
        defaultSemesterShouldBeFound("endDate.lessThan=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    public void getAllSemestersByEndDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        semesterRepository.saveAndFlush(semester);

        // Get all the semesterList where endDate is greater than DEFAULT_END_DATE
        defaultSemesterShouldNotBeFound("endDate.greaterThan=" + DEFAULT_END_DATE);

        // Get all the semesterList where endDate is greater than SMALLER_END_DATE
        defaultSemesterShouldBeFound("endDate.greaterThan=" + SMALLER_END_DATE);
    }


    @Test
    @Transactional
    public void getAllSemestersByCreatedOnIsEqualToSomething() throws Exception {
        // Initialize the database
        semesterRepository.saveAndFlush(semester);

        // Get all the semesterList where createdOn equals to DEFAULT_CREATED_ON
        defaultSemesterShouldBeFound("createdOn.equals=" + DEFAULT_CREATED_ON);

        // Get all the semesterList where createdOn equals to UPDATED_CREATED_ON
        defaultSemesterShouldNotBeFound("createdOn.equals=" + UPDATED_CREATED_ON);
    }

    @Test
    @Transactional
    public void getAllSemestersByCreatedOnIsNotEqualToSomething() throws Exception {
        // Initialize the database
        semesterRepository.saveAndFlush(semester);

        // Get all the semesterList where createdOn not equals to DEFAULT_CREATED_ON
        defaultSemesterShouldNotBeFound("createdOn.notEquals=" + DEFAULT_CREATED_ON);

        // Get all the semesterList where createdOn not equals to UPDATED_CREATED_ON
        defaultSemesterShouldBeFound("createdOn.notEquals=" + UPDATED_CREATED_ON);
    }

    @Test
    @Transactional
    public void getAllSemestersByCreatedOnIsInShouldWork() throws Exception {
        // Initialize the database
        semesterRepository.saveAndFlush(semester);

        // Get all the semesterList where createdOn in DEFAULT_CREATED_ON or UPDATED_CREATED_ON
        defaultSemesterShouldBeFound("createdOn.in=" + DEFAULT_CREATED_ON + "," + UPDATED_CREATED_ON);

        // Get all the semesterList where createdOn equals to UPDATED_CREATED_ON
        defaultSemesterShouldNotBeFound("createdOn.in=" + UPDATED_CREATED_ON);
    }

    @Test
    @Transactional
    public void getAllSemestersByCreatedOnIsNullOrNotNull() throws Exception {
        // Initialize the database
        semesterRepository.saveAndFlush(semester);

        // Get all the semesterList where createdOn is not null
        defaultSemesterShouldBeFound("createdOn.specified=true");

        // Get all the semesterList where createdOn is null
        defaultSemesterShouldNotBeFound("createdOn.specified=false");
    }

    @Test
    @Transactional
    public void getAllSemestersByCreatedOnIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        semesterRepository.saveAndFlush(semester);

        // Get all the semesterList where createdOn is greater than or equal to DEFAULT_CREATED_ON
        defaultSemesterShouldBeFound("createdOn.greaterThanOrEqual=" + DEFAULT_CREATED_ON);

        // Get all the semesterList where createdOn is greater than or equal to UPDATED_CREATED_ON
        defaultSemesterShouldNotBeFound("createdOn.greaterThanOrEqual=" + UPDATED_CREATED_ON);
    }

    @Test
    @Transactional
    public void getAllSemestersByCreatedOnIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        semesterRepository.saveAndFlush(semester);

        // Get all the semesterList where createdOn is less than or equal to DEFAULT_CREATED_ON
        defaultSemesterShouldBeFound("createdOn.lessThanOrEqual=" + DEFAULT_CREATED_ON);

        // Get all the semesterList where createdOn is less than or equal to SMALLER_CREATED_ON
        defaultSemesterShouldNotBeFound("createdOn.lessThanOrEqual=" + SMALLER_CREATED_ON);
    }

    @Test
    @Transactional
    public void getAllSemestersByCreatedOnIsLessThanSomething() throws Exception {
        // Initialize the database
        semesterRepository.saveAndFlush(semester);

        // Get all the semesterList where createdOn is less than DEFAULT_CREATED_ON
        defaultSemesterShouldNotBeFound("createdOn.lessThan=" + DEFAULT_CREATED_ON);

        // Get all the semesterList where createdOn is less than UPDATED_CREATED_ON
        defaultSemesterShouldBeFound("createdOn.lessThan=" + UPDATED_CREATED_ON);
    }

    @Test
    @Transactional
    public void getAllSemestersByCreatedOnIsGreaterThanSomething() throws Exception {
        // Initialize the database
        semesterRepository.saveAndFlush(semester);

        // Get all the semesterList where createdOn is greater than DEFAULT_CREATED_ON
        defaultSemesterShouldNotBeFound("createdOn.greaterThan=" + DEFAULT_CREATED_ON);

        // Get all the semesterList where createdOn is greater than SMALLER_CREATED_ON
        defaultSemesterShouldBeFound("createdOn.greaterThan=" + SMALLER_CREATED_ON);
    }


    @Test
    @Transactional
    public void getAllSemestersByModifiedOnIsEqualToSomething() throws Exception {
        // Initialize the database
        semesterRepository.saveAndFlush(semester);

        // Get all the semesterList where modifiedOn equals to DEFAULT_MODIFIED_ON
        defaultSemesterShouldBeFound("modifiedOn.equals=" + DEFAULT_MODIFIED_ON);

        // Get all the semesterList where modifiedOn equals to UPDATED_MODIFIED_ON
        defaultSemesterShouldNotBeFound("modifiedOn.equals=" + UPDATED_MODIFIED_ON);
    }

    @Test
    @Transactional
    public void getAllSemestersByModifiedOnIsNotEqualToSomething() throws Exception {
        // Initialize the database
        semesterRepository.saveAndFlush(semester);

        // Get all the semesterList where modifiedOn not equals to DEFAULT_MODIFIED_ON
        defaultSemesterShouldNotBeFound("modifiedOn.notEquals=" + DEFAULT_MODIFIED_ON);

        // Get all the semesterList where modifiedOn not equals to UPDATED_MODIFIED_ON
        defaultSemesterShouldBeFound("modifiedOn.notEquals=" + UPDATED_MODIFIED_ON);
    }

    @Test
    @Transactional
    public void getAllSemestersByModifiedOnIsInShouldWork() throws Exception {
        // Initialize the database
        semesterRepository.saveAndFlush(semester);

        // Get all the semesterList where modifiedOn in DEFAULT_MODIFIED_ON or UPDATED_MODIFIED_ON
        defaultSemesterShouldBeFound("modifiedOn.in=" + DEFAULT_MODIFIED_ON + "," + UPDATED_MODIFIED_ON);

        // Get all the semesterList where modifiedOn equals to UPDATED_MODIFIED_ON
        defaultSemesterShouldNotBeFound("modifiedOn.in=" + UPDATED_MODIFIED_ON);
    }

    @Test
    @Transactional
    public void getAllSemestersByModifiedOnIsNullOrNotNull() throws Exception {
        // Initialize the database
        semesterRepository.saveAndFlush(semester);

        // Get all the semesterList where modifiedOn is not null
        defaultSemesterShouldBeFound("modifiedOn.specified=true");

        // Get all the semesterList where modifiedOn is null
        defaultSemesterShouldNotBeFound("modifiedOn.specified=false");
    }

    @Test
    @Transactional
    public void getAllSemestersByModifiedOnIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        semesterRepository.saveAndFlush(semester);

        // Get all the semesterList where modifiedOn is greater than or equal to DEFAULT_MODIFIED_ON
        defaultSemesterShouldBeFound("modifiedOn.greaterThanOrEqual=" + DEFAULT_MODIFIED_ON);

        // Get all the semesterList where modifiedOn is greater than or equal to UPDATED_MODIFIED_ON
        defaultSemesterShouldNotBeFound("modifiedOn.greaterThanOrEqual=" + UPDATED_MODIFIED_ON);
    }

    @Test
    @Transactional
    public void getAllSemestersByModifiedOnIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        semesterRepository.saveAndFlush(semester);

        // Get all the semesterList where modifiedOn is less than or equal to DEFAULT_MODIFIED_ON
        defaultSemesterShouldBeFound("modifiedOn.lessThanOrEqual=" + DEFAULT_MODIFIED_ON);

        // Get all the semesterList where modifiedOn is less than or equal to SMALLER_MODIFIED_ON
        defaultSemesterShouldNotBeFound("modifiedOn.lessThanOrEqual=" + SMALLER_MODIFIED_ON);
    }

    @Test
    @Transactional
    public void getAllSemestersByModifiedOnIsLessThanSomething() throws Exception {
        // Initialize the database
        semesterRepository.saveAndFlush(semester);

        // Get all the semesterList where modifiedOn is less than DEFAULT_MODIFIED_ON
        defaultSemesterShouldNotBeFound("modifiedOn.lessThan=" + DEFAULT_MODIFIED_ON);

        // Get all the semesterList where modifiedOn is less than UPDATED_MODIFIED_ON
        defaultSemesterShouldBeFound("modifiedOn.lessThan=" + UPDATED_MODIFIED_ON);
    }

    @Test
    @Transactional
    public void getAllSemestersByModifiedOnIsGreaterThanSomething() throws Exception {
        // Initialize the database
        semesterRepository.saveAndFlush(semester);

        // Get all the semesterList where modifiedOn is greater than DEFAULT_MODIFIED_ON
        defaultSemesterShouldNotBeFound("modifiedOn.greaterThan=" + DEFAULT_MODIFIED_ON);

        // Get all the semesterList where modifiedOn is greater than SMALLER_MODIFIED_ON
        defaultSemesterShouldBeFound("modifiedOn.greaterThan=" + SMALLER_MODIFIED_ON);
    }


    @Test
    @Transactional
    public void getAllSemestersByModifiedByIsEqualToSomething() throws Exception {
        // Initialize the database
        semesterRepository.saveAndFlush(semester);

        // Get all the semesterList where modifiedBy equals to DEFAULT_MODIFIED_BY
        defaultSemesterShouldBeFound("modifiedBy.equals=" + DEFAULT_MODIFIED_BY);

        // Get all the semesterList where modifiedBy equals to UPDATED_MODIFIED_BY
        defaultSemesterShouldNotBeFound("modifiedBy.equals=" + UPDATED_MODIFIED_BY);
    }

    @Test
    @Transactional
    public void getAllSemestersByModifiedByIsNotEqualToSomething() throws Exception {
        // Initialize the database
        semesterRepository.saveAndFlush(semester);

        // Get all the semesterList where modifiedBy not equals to DEFAULT_MODIFIED_BY
        defaultSemesterShouldNotBeFound("modifiedBy.notEquals=" + DEFAULT_MODIFIED_BY);

        // Get all the semesterList where modifiedBy not equals to UPDATED_MODIFIED_BY
        defaultSemesterShouldBeFound("modifiedBy.notEquals=" + UPDATED_MODIFIED_BY);
    }

    @Test
    @Transactional
    public void getAllSemestersByModifiedByIsInShouldWork() throws Exception {
        // Initialize the database
        semesterRepository.saveAndFlush(semester);

        // Get all the semesterList where modifiedBy in DEFAULT_MODIFIED_BY or UPDATED_MODIFIED_BY
        defaultSemesterShouldBeFound("modifiedBy.in=" + DEFAULT_MODIFIED_BY + "," + UPDATED_MODIFIED_BY);

        // Get all the semesterList where modifiedBy equals to UPDATED_MODIFIED_BY
        defaultSemesterShouldNotBeFound("modifiedBy.in=" + UPDATED_MODIFIED_BY);
    }

    @Test
    @Transactional
    public void getAllSemestersByModifiedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        semesterRepository.saveAndFlush(semester);

        // Get all the semesterList where modifiedBy is not null
        defaultSemesterShouldBeFound("modifiedBy.specified=true");

        // Get all the semesterList where modifiedBy is null
        defaultSemesterShouldNotBeFound("modifiedBy.specified=false");
    }
                @Test
    @Transactional
    public void getAllSemestersByModifiedByContainsSomething() throws Exception {
        // Initialize the database
        semesterRepository.saveAndFlush(semester);

        // Get all the semesterList where modifiedBy contains DEFAULT_MODIFIED_BY
        defaultSemesterShouldBeFound("modifiedBy.contains=" + DEFAULT_MODIFIED_BY);

        // Get all the semesterList where modifiedBy contains UPDATED_MODIFIED_BY
        defaultSemesterShouldNotBeFound("modifiedBy.contains=" + UPDATED_MODIFIED_BY);
    }

    @Test
    @Transactional
    public void getAllSemestersByModifiedByNotContainsSomething() throws Exception {
        // Initialize the database
        semesterRepository.saveAndFlush(semester);

        // Get all the semesterList where modifiedBy does not contain DEFAULT_MODIFIED_BY
        defaultSemesterShouldNotBeFound("modifiedBy.doesNotContain=" + DEFAULT_MODIFIED_BY);

        // Get all the semesterList where modifiedBy does not contain UPDATED_MODIFIED_BY
        defaultSemesterShouldBeFound("modifiedBy.doesNotContain=" + UPDATED_MODIFIED_BY);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSemesterShouldBeFound(String filter) throws Exception {
        restSemesterMockMvc.perform(get("/api/semesters?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(semester.getId().intValue())))
            .andExpect(jsonPath("$.[*].semesterId").value(hasItem(DEFAULT_SEMESTER_ID)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].shortName").value(hasItem(DEFAULT_SHORT_NAME)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdOn").value(hasItem(DEFAULT_CREATED_ON.toString())))
            .andExpect(jsonPath("$.[*].modifiedOn").value(hasItem(DEFAULT_MODIFIED_ON.toString())))
            .andExpect(jsonPath("$.[*].modifiedBy").value(hasItem(DEFAULT_MODIFIED_BY)));

        // Check, that the count call also returns 1
        restSemesterMockMvc.perform(get("/api/semesters/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSemesterShouldNotBeFound(String filter) throws Exception {
        restSemesterMockMvc.perform(get("/api/semesters?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSemesterMockMvc.perform(get("/api/semesters/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingSemester() throws Exception {
        // Get the semester
        restSemesterMockMvc.perform(get("/api/semesters/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSemester() throws Exception {
        // Initialize the database
        semesterRepository.saveAndFlush(semester);

        int databaseSizeBeforeUpdate = semesterRepository.findAll().size();

        // Update the semester
        Semester updatedSemester = semesterRepository.findById(semester.getId()).get();
        // Disconnect from session so that the updates on updatedSemester are not directly saved in db
        em.detach(updatedSemester);
        updatedSemester
            .semesterId(UPDATED_SEMESTER_ID)
            .name(UPDATED_NAME)
            .shortName(UPDATED_SHORT_NAME)
            .status(UPDATED_STATUS)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .createdOn(UPDATED_CREATED_ON)
            .modifiedOn(UPDATED_MODIFIED_ON)
            .modifiedBy(UPDATED_MODIFIED_BY);
        SemesterDTO semesterDTO = semesterMapper.toDto(updatedSemester);

        restSemesterMockMvc.perform(put("/api/semesters")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(semesterDTO)))
            .andExpect(status().isOk());

        // Validate the Semester in the database
        List<Semester> semesterList = semesterRepository.findAll();
        assertThat(semesterList).hasSize(databaseSizeBeforeUpdate);
        Semester testSemester = semesterList.get(semesterList.size() - 1);
        assertThat(testSemester.getSemesterId()).isEqualTo(UPDATED_SEMESTER_ID);
        assertThat(testSemester.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSemester.getShortName()).isEqualTo(UPDATED_SHORT_NAME);
        assertThat(testSemester.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testSemester.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testSemester.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testSemester.getCreatedOn()).isEqualTo(UPDATED_CREATED_ON);
        assertThat(testSemester.getModifiedOn()).isEqualTo(UPDATED_MODIFIED_ON);
        assertThat(testSemester.getModifiedBy()).isEqualTo(UPDATED_MODIFIED_BY);
    }

    @Test
    @Transactional
    public void updateNonExistingSemester() throws Exception {
        int databaseSizeBeforeUpdate = semesterRepository.findAll().size();

        // Create the Semester
        SemesterDTO semesterDTO = semesterMapper.toDto(semester);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSemesterMockMvc.perform(put("/api/semesters")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(semesterDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Semester in the database
        List<Semester> semesterList = semesterRepository.findAll();
        assertThat(semesterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteSemester() throws Exception {
        // Initialize the database
        semesterRepository.saveAndFlush(semester);

        int databaseSizeBeforeDelete = semesterRepository.findAll().size();

        // Delete the semester
        restSemesterMockMvc.perform(delete("/api/semesters/{id}", semester.getId())
            .accept(TestUtil.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Semester> semesterList = semesterRepository.findAll();
        assertThat(semesterList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
