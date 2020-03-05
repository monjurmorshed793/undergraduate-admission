package org.ums.web.rest;

import org.ums.UgAdmissionApp;
import org.ums.domain.Faculty;
import org.ums.repository.FacultyRepository;
import org.ums.service.FacultyService;
import org.ums.service.dto.FacultyDTO;
import org.ums.service.mapper.FacultyMapper;
import org.ums.web.rest.errors.ExceptionTranslator;
import org.ums.service.dto.FacultyCriteria;
import org.ums.service.FacultyQueryService;

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
 * Integration tests for the {@link FacultyResource} REST controller.
 */
@SpringBootTest(classes = UgAdmissionApp.class)
public class FacultyResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_SHORT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_SHORT_NAME = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_CREATED_ON = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATED_ON = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_CREATED_ON = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_MODIFIED_ON = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_MODIFIED_ON = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_MODIFIED_ON = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_MODIFIED_BY = "AAAAAAAAAA";
    private static final String UPDATED_MODIFIED_BY = "BBBBBBBBBB";

    @Autowired
    private FacultyRepository facultyRepository;

    @Autowired
    private FacultyMapper facultyMapper;

    @Autowired
    private FacultyService facultyService;

    @Autowired
    private FacultyQueryService facultyQueryService;

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

    private MockMvc restFacultyMockMvc;

    private Faculty faculty;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final FacultyResource facultyResource = new FacultyResource(facultyService, facultyQueryService);
        this.restFacultyMockMvc = MockMvcBuilders.standaloneSetup(facultyResource)
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
    public static Faculty createEntity(EntityManager em) {
        Faculty faculty = new Faculty()
            .name(DEFAULT_NAME)
            .shortName(DEFAULT_SHORT_NAME)
            .createdOn(DEFAULT_CREATED_ON)
            .modifiedOn(DEFAULT_MODIFIED_ON)
            .modifiedBy(DEFAULT_MODIFIED_BY);
        return faculty;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Faculty createUpdatedEntity(EntityManager em) {
        Faculty faculty = new Faculty()
            .name(UPDATED_NAME)
            .shortName(UPDATED_SHORT_NAME)
            .createdOn(UPDATED_CREATED_ON)
            .modifiedOn(UPDATED_MODIFIED_ON)
            .modifiedBy(UPDATED_MODIFIED_BY);
        return faculty;
    }

    @BeforeEach
    public void initTest() {
        faculty = createEntity(em);
    }

    @Test
    @Transactional
    public void createFaculty() throws Exception {
        int databaseSizeBeforeCreate = facultyRepository.findAll().size();

        // Create the Faculty
        FacultyDTO facultyDTO = facultyMapper.toDto(faculty);
        restFacultyMockMvc.perform(post("/api/faculties")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(facultyDTO)))
            .andExpect(status().isCreated());

        // Validate the Faculty in the database
        List<Faculty> facultyList = facultyRepository.findAll();
        assertThat(facultyList).hasSize(databaseSizeBeforeCreate + 1);
        Faculty testFaculty = facultyList.get(facultyList.size() - 1);
        assertThat(testFaculty.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testFaculty.getShortName()).isEqualTo(DEFAULT_SHORT_NAME);
        assertThat(testFaculty.getCreatedOn()).isEqualTo(DEFAULT_CREATED_ON);
        assertThat(testFaculty.getModifiedOn()).isEqualTo(DEFAULT_MODIFIED_ON);
        assertThat(testFaculty.getModifiedBy()).isEqualTo(DEFAULT_MODIFIED_BY);
    }

    @Test
    @Transactional
    public void createFacultyWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = facultyRepository.findAll().size();

        // Create the Faculty with an existing ID
        faculty.setId(1L);
        FacultyDTO facultyDTO = facultyMapper.toDto(faculty);

        // An entity with an existing ID cannot be created, so this API call must fail
        restFacultyMockMvc.perform(post("/api/faculties")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(facultyDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Faculty in the database
        List<Faculty> facultyList = facultyRepository.findAll();
        assertThat(facultyList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = facultyRepository.findAll().size();
        // set the field null
        faculty.setName(null);

        // Create the Faculty, which fails.
        FacultyDTO facultyDTO = facultyMapper.toDto(faculty);

        restFacultyMockMvc.perform(post("/api/faculties")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(facultyDTO)))
            .andExpect(status().isBadRequest());

        List<Faculty> facultyList = facultyRepository.findAll();
        assertThat(facultyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkShortNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = facultyRepository.findAll().size();
        // set the field null
        faculty.setShortName(null);

        // Create the Faculty, which fails.
        FacultyDTO facultyDTO = facultyMapper.toDto(faculty);

        restFacultyMockMvc.perform(post("/api/faculties")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(facultyDTO)))
            .andExpect(status().isBadRequest());

        List<Faculty> facultyList = facultyRepository.findAll();
        assertThat(facultyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCreatedOnIsRequired() throws Exception {
        int databaseSizeBeforeTest = facultyRepository.findAll().size();
        // set the field null
        faculty.setCreatedOn(null);

        // Create the Faculty, which fails.
        FacultyDTO facultyDTO = facultyMapper.toDto(faculty);

        restFacultyMockMvc.perform(post("/api/faculties")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(facultyDTO)))
            .andExpect(status().isBadRequest());

        List<Faculty> facultyList = facultyRepository.findAll();
        assertThat(facultyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllFaculties() throws Exception {
        // Initialize the database
        facultyRepository.saveAndFlush(faculty);

        // Get all the facultyList
        restFacultyMockMvc.perform(get("/api/faculties?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(faculty.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].shortName").value(hasItem(DEFAULT_SHORT_NAME)))
            .andExpect(jsonPath("$.[*].createdOn").value(hasItem(DEFAULT_CREATED_ON.toString())))
            .andExpect(jsonPath("$.[*].modifiedOn").value(hasItem(DEFAULT_MODIFIED_ON.toString())))
            .andExpect(jsonPath("$.[*].modifiedBy").value(hasItem(DEFAULT_MODIFIED_BY)));
    }
    
    @Test
    @Transactional
    public void getFaculty() throws Exception {
        // Initialize the database
        facultyRepository.saveAndFlush(faculty);

        // Get the faculty
        restFacultyMockMvc.perform(get("/api/faculties/{id}", faculty.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(faculty.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.shortName").value(DEFAULT_SHORT_NAME))
            .andExpect(jsonPath("$.createdOn").value(DEFAULT_CREATED_ON.toString()))
            .andExpect(jsonPath("$.modifiedOn").value(DEFAULT_MODIFIED_ON.toString()))
            .andExpect(jsonPath("$.modifiedBy").value(DEFAULT_MODIFIED_BY));
    }


    @Test
    @Transactional
    public void getFacultiesByIdFiltering() throws Exception {
        // Initialize the database
        facultyRepository.saveAndFlush(faculty);

        Long id = faculty.getId();

        defaultFacultyShouldBeFound("id.equals=" + id);
        defaultFacultyShouldNotBeFound("id.notEquals=" + id);

        defaultFacultyShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultFacultyShouldNotBeFound("id.greaterThan=" + id);

        defaultFacultyShouldBeFound("id.lessThanOrEqual=" + id);
        defaultFacultyShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllFacultiesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        facultyRepository.saveAndFlush(faculty);

        // Get all the facultyList where name equals to DEFAULT_NAME
        defaultFacultyShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the facultyList where name equals to UPDATED_NAME
        defaultFacultyShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllFacultiesByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        facultyRepository.saveAndFlush(faculty);

        // Get all the facultyList where name not equals to DEFAULT_NAME
        defaultFacultyShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the facultyList where name not equals to UPDATED_NAME
        defaultFacultyShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllFacultiesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        facultyRepository.saveAndFlush(faculty);

        // Get all the facultyList where name in DEFAULT_NAME or UPDATED_NAME
        defaultFacultyShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the facultyList where name equals to UPDATED_NAME
        defaultFacultyShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllFacultiesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        facultyRepository.saveAndFlush(faculty);

        // Get all the facultyList where name is not null
        defaultFacultyShouldBeFound("name.specified=true");

        // Get all the facultyList where name is null
        defaultFacultyShouldNotBeFound("name.specified=false");
    }
                @Test
    @Transactional
    public void getAllFacultiesByNameContainsSomething() throws Exception {
        // Initialize the database
        facultyRepository.saveAndFlush(faculty);

        // Get all the facultyList where name contains DEFAULT_NAME
        defaultFacultyShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the facultyList where name contains UPDATED_NAME
        defaultFacultyShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllFacultiesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        facultyRepository.saveAndFlush(faculty);

        // Get all the facultyList where name does not contain DEFAULT_NAME
        defaultFacultyShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the facultyList where name does not contain UPDATED_NAME
        defaultFacultyShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }


    @Test
    @Transactional
    public void getAllFacultiesByShortNameIsEqualToSomething() throws Exception {
        // Initialize the database
        facultyRepository.saveAndFlush(faculty);

        // Get all the facultyList where shortName equals to DEFAULT_SHORT_NAME
        defaultFacultyShouldBeFound("shortName.equals=" + DEFAULT_SHORT_NAME);

        // Get all the facultyList where shortName equals to UPDATED_SHORT_NAME
        defaultFacultyShouldNotBeFound("shortName.equals=" + UPDATED_SHORT_NAME);
    }

    @Test
    @Transactional
    public void getAllFacultiesByShortNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        facultyRepository.saveAndFlush(faculty);

        // Get all the facultyList where shortName not equals to DEFAULT_SHORT_NAME
        defaultFacultyShouldNotBeFound("shortName.notEquals=" + DEFAULT_SHORT_NAME);

        // Get all the facultyList where shortName not equals to UPDATED_SHORT_NAME
        defaultFacultyShouldBeFound("shortName.notEquals=" + UPDATED_SHORT_NAME);
    }

    @Test
    @Transactional
    public void getAllFacultiesByShortNameIsInShouldWork() throws Exception {
        // Initialize the database
        facultyRepository.saveAndFlush(faculty);

        // Get all the facultyList where shortName in DEFAULT_SHORT_NAME or UPDATED_SHORT_NAME
        defaultFacultyShouldBeFound("shortName.in=" + DEFAULT_SHORT_NAME + "," + UPDATED_SHORT_NAME);

        // Get all the facultyList where shortName equals to UPDATED_SHORT_NAME
        defaultFacultyShouldNotBeFound("shortName.in=" + UPDATED_SHORT_NAME);
    }

    @Test
    @Transactional
    public void getAllFacultiesByShortNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        facultyRepository.saveAndFlush(faculty);

        // Get all the facultyList where shortName is not null
        defaultFacultyShouldBeFound("shortName.specified=true");

        // Get all the facultyList where shortName is null
        defaultFacultyShouldNotBeFound("shortName.specified=false");
    }
                @Test
    @Transactional
    public void getAllFacultiesByShortNameContainsSomething() throws Exception {
        // Initialize the database
        facultyRepository.saveAndFlush(faculty);

        // Get all the facultyList where shortName contains DEFAULT_SHORT_NAME
        defaultFacultyShouldBeFound("shortName.contains=" + DEFAULT_SHORT_NAME);

        // Get all the facultyList where shortName contains UPDATED_SHORT_NAME
        defaultFacultyShouldNotBeFound("shortName.contains=" + UPDATED_SHORT_NAME);
    }

    @Test
    @Transactional
    public void getAllFacultiesByShortNameNotContainsSomething() throws Exception {
        // Initialize the database
        facultyRepository.saveAndFlush(faculty);

        // Get all the facultyList where shortName does not contain DEFAULT_SHORT_NAME
        defaultFacultyShouldNotBeFound("shortName.doesNotContain=" + DEFAULT_SHORT_NAME);

        // Get all the facultyList where shortName does not contain UPDATED_SHORT_NAME
        defaultFacultyShouldBeFound("shortName.doesNotContain=" + UPDATED_SHORT_NAME);
    }


    @Test
    @Transactional
    public void getAllFacultiesByCreatedOnIsEqualToSomething() throws Exception {
        // Initialize the database
        facultyRepository.saveAndFlush(faculty);

        // Get all the facultyList where createdOn equals to DEFAULT_CREATED_ON
        defaultFacultyShouldBeFound("createdOn.equals=" + DEFAULT_CREATED_ON);

        // Get all the facultyList where createdOn equals to UPDATED_CREATED_ON
        defaultFacultyShouldNotBeFound("createdOn.equals=" + UPDATED_CREATED_ON);
    }

    @Test
    @Transactional
    public void getAllFacultiesByCreatedOnIsNotEqualToSomething() throws Exception {
        // Initialize the database
        facultyRepository.saveAndFlush(faculty);

        // Get all the facultyList where createdOn not equals to DEFAULT_CREATED_ON
        defaultFacultyShouldNotBeFound("createdOn.notEquals=" + DEFAULT_CREATED_ON);

        // Get all the facultyList where createdOn not equals to UPDATED_CREATED_ON
        defaultFacultyShouldBeFound("createdOn.notEquals=" + UPDATED_CREATED_ON);
    }

    @Test
    @Transactional
    public void getAllFacultiesByCreatedOnIsInShouldWork() throws Exception {
        // Initialize the database
        facultyRepository.saveAndFlush(faculty);

        // Get all the facultyList where createdOn in DEFAULT_CREATED_ON or UPDATED_CREATED_ON
        defaultFacultyShouldBeFound("createdOn.in=" + DEFAULT_CREATED_ON + "," + UPDATED_CREATED_ON);

        // Get all the facultyList where createdOn equals to UPDATED_CREATED_ON
        defaultFacultyShouldNotBeFound("createdOn.in=" + UPDATED_CREATED_ON);
    }

    @Test
    @Transactional
    public void getAllFacultiesByCreatedOnIsNullOrNotNull() throws Exception {
        // Initialize the database
        facultyRepository.saveAndFlush(faculty);

        // Get all the facultyList where createdOn is not null
        defaultFacultyShouldBeFound("createdOn.specified=true");

        // Get all the facultyList where createdOn is null
        defaultFacultyShouldNotBeFound("createdOn.specified=false");
    }

    @Test
    @Transactional
    public void getAllFacultiesByCreatedOnIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        facultyRepository.saveAndFlush(faculty);

        // Get all the facultyList where createdOn is greater than or equal to DEFAULT_CREATED_ON
        defaultFacultyShouldBeFound("createdOn.greaterThanOrEqual=" + DEFAULT_CREATED_ON);

        // Get all the facultyList where createdOn is greater than or equal to UPDATED_CREATED_ON
        defaultFacultyShouldNotBeFound("createdOn.greaterThanOrEqual=" + UPDATED_CREATED_ON);
    }

    @Test
    @Transactional
    public void getAllFacultiesByCreatedOnIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        facultyRepository.saveAndFlush(faculty);

        // Get all the facultyList where createdOn is less than or equal to DEFAULT_CREATED_ON
        defaultFacultyShouldBeFound("createdOn.lessThanOrEqual=" + DEFAULT_CREATED_ON);

        // Get all the facultyList where createdOn is less than or equal to SMALLER_CREATED_ON
        defaultFacultyShouldNotBeFound("createdOn.lessThanOrEqual=" + SMALLER_CREATED_ON);
    }

    @Test
    @Transactional
    public void getAllFacultiesByCreatedOnIsLessThanSomething() throws Exception {
        // Initialize the database
        facultyRepository.saveAndFlush(faculty);

        // Get all the facultyList where createdOn is less than DEFAULT_CREATED_ON
        defaultFacultyShouldNotBeFound("createdOn.lessThan=" + DEFAULT_CREATED_ON);

        // Get all the facultyList where createdOn is less than UPDATED_CREATED_ON
        defaultFacultyShouldBeFound("createdOn.lessThan=" + UPDATED_CREATED_ON);
    }

    @Test
    @Transactional
    public void getAllFacultiesByCreatedOnIsGreaterThanSomething() throws Exception {
        // Initialize the database
        facultyRepository.saveAndFlush(faculty);

        // Get all the facultyList where createdOn is greater than DEFAULT_CREATED_ON
        defaultFacultyShouldNotBeFound("createdOn.greaterThan=" + DEFAULT_CREATED_ON);

        // Get all the facultyList where createdOn is greater than SMALLER_CREATED_ON
        defaultFacultyShouldBeFound("createdOn.greaterThan=" + SMALLER_CREATED_ON);
    }


    @Test
    @Transactional
    public void getAllFacultiesByModifiedOnIsEqualToSomething() throws Exception {
        // Initialize the database
        facultyRepository.saveAndFlush(faculty);

        // Get all the facultyList where modifiedOn equals to DEFAULT_MODIFIED_ON
        defaultFacultyShouldBeFound("modifiedOn.equals=" + DEFAULT_MODIFIED_ON);

        // Get all the facultyList where modifiedOn equals to UPDATED_MODIFIED_ON
        defaultFacultyShouldNotBeFound("modifiedOn.equals=" + UPDATED_MODIFIED_ON);
    }

    @Test
    @Transactional
    public void getAllFacultiesByModifiedOnIsNotEqualToSomething() throws Exception {
        // Initialize the database
        facultyRepository.saveAndFlush(faculty);

        // Get all the facultyList where modifiedOn not equals to DEFAULT_MODIFIED_ON
        defaultFacultyShouldNotBeFound("modifiedOn.notEquals=" + DEFAULT_MODIFIED_ON);

        // Get all the facultyList where modifiedOn not equals to UPDATED_MODIFIED_ON
        defaultFacultyShouldBeFound("modifiedOn.notEquals=" + UPDATED_MODIFIED_ON);
    }

    @Test
    @Transactional
    public void getAllFacultiesByModifiedOnIsInShouldWork() throws Exception {
        // Initialize the database
        facultyRepository.saveAndFlush(faculty);

        // Get all the facultyList where modifiedOn in DEFAULT_MODIFIED_ON or UPDATED_MODIFIED_ON
        defaultFacultyShouldBeFound("modifiedOn.in=" + DEFAULT_MODIFIED_ON + "," + UPDATED_MODIFIED_ON);

        // Get all the facultyList where modifiedOn equals to UPDATED_MODIFIED_ON
        defaultFacultyShouldNotBeFound("modifiedOn.in=" + UPDATED_MODIFIED_ON);
    }

    @Test
    @Transactional
    public void getAllFacultiesByModifiedOnIsNullOrNotNull() throws Exception {
        // Initialize the database
        facultyRepository.saveAndFlush(faculty);

        // Get all the facultyList where modifiedOn is not null
        defaultFacultyShouldBeFound("modifiedOn.specified=true");

        // Get all the facultyList where modifiedOn is null
        defaultFacultyShouldNotBeFound("modifiedOn.specified=false");
    }

    @Test
    @Transactional
    public void getAllFacultiesByModifiedOnIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        facultyRepository.saveAndFlush(faculty);

        // Get all the facultyList where modifiedOn is greater than or equal to DEFAULT_MODIFIED_ON
        defaultFacultyShouldBeFound("modifiedOn.greaterThanOrEqual=" + DEFAULT_MODIFIED_ON);

        // Get all the facultyList where modifiedOn is greater than or equal to UPDATED_MODIFIED_ON
        defaultFacultyShouldNotBeFound("modifiedOn.greaterThanOrEqual=" + UPDATED_MODIFIED_ON);
    }

    @Test
    @Transactional
    public void getAllFacultiesByModifiedOnIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        facultyRepository.saveAndFlush(faculty);

        // Get all the facultyList where modifiedOn is less than or equal to DEFAULT_MODIFIED_ON
        defaultFacultyShouldBeFound("modifiedOn.lessThanOrEqual=" + DEFAULT_MODIFIED_ON);

        // Get all the facultyList where modifiedOn is less than or equal to SMALLER_MODIFIED_ON
        defaultFacultyShouldNotBeFound("modifiedOn.lessThanOrEqual=" + SMALLER_MODIFIED_ON);
    }

    @Test
    @Transactional
    public void getAllFacultiesByModifiedOnIsLessThanSomething() throws Exception {
        // Initialize the database
        facultyRepository.saveAndFlush(faculty);

        // Get all the facultyList where modifiedOn is less than DEFAULT_MODIFIED_ON
        defaultFacultyShouldNotBeFound("modifiedOn.lessThan=" + DEFAULT_MODIFIED_ON);

        // Get all the facultyList where modifiedOn is less than UPDATED_MODIFIED_ON
        defaultFacultyShouldBeFound("modifiedOn.lessThan=" + UPDATED_MODIFIED_ON);
    }

    @Test
    @Transactional
    public void getAllFacultiesByModifiedOnIsGreaterThanSomething() throws Exception {
        // Initialize the database
        facultyRepository.saveAndFlush(faculty);

        // Get all the facultyList where modifiedOn is greater than DEFAULT_MODIFIED_ON
        defaultFacultyShouldNotBeFound("modifiedOn.greaterThan=" + DEFAULT_MODIFIED_ON);

        // Get all the facultyList where modifiedOn is greater than SMALLER_MODIFIED_ON
        defaultFacultyShouldBeFound("modifiedOn.greaterThan=" + SMALLER_MODIFIED_ON);
    }


    @Test
    @Transactional
    public void getAllFacultiesByModifiedByIsEqualToSomething() throws Exception {
        // Initialize the database
        facultyRepository.saveAndFlush(faculty);

        // Get all the facultyList where modifiedBy equals to DEFAULT_MODIFIED_BY
        defaultFacultyShouldBeFound("modifiedBy.equals=" + DEFAULT_MODIFIED_BY);

        // Get all the facultyList where modifiedBy equals to UPDATED_MODIFIED_BY
        defaultFacultyShouldNotBeFound("modifiedBy.equals=" + UPDATED_MODIFIED_BY);
    }

    @Test
    @Transactional
    public void getAllFacultiesByModifiedByIsNotEqualToSomething() throws Exception {
        // Initialize the database
        facultyRepository.saveAndFlush(faculty);

        // Get all the facultyList where modifiedBy not equals to DEFAULT_MODIFIED_BY
        defaultFacultyShouldNotBeFound("modifiedBy.notEquals=" + DEFAULT_MODIFIED_BY);

        // Get all the facultyList where modifiedBy not equals to UPDATED_MODIFIED_BY
        defaultFacultyShouldBeFound("modifiedBy.notEquals=" + UPDATED_MODIFIED_BY);
    }

    @Test
    @Transactional
    public void getAllFacultiesByModifiedByIsInShouldWork() throws Exception {
        // Initialize the database
        facultyRepository.saveAndFlush(faculty);

        // Get all the facultyList where modifiedBy in DEFAULT_MODIFIED_BY or UPDATED_MODIFIED_BY
        defaultFacultyShouldBeFound("modifiedBy.in=" + DEFAULT_MODIFIED_BY + "," + UPDATED_MODIFIED_BY);

        // Get all the facultyList where modifiedBy equals to UPDATED_MODIFIED_BY
        defaultFacultyShouldNotBeFound("modifiedBy.in=" + UPDATED_MODIFIED_BY);
    }

    @Test
    @Transactional
    public void getAllFacultiesByModifiedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        facultyRepository.saveAndFlush(faculty);

        // Get all the facultyList where modifiedBy is not null
        defaultFacultyShouldBeFound("modifiedBy.specified=true");

        // Get all the facultyList where modifiedBy is null
        defaultFacultyShouldNotBeFound("modifiedBy.specified=false");
    }
                @Test
    @Transactional
    public void getAllFacultiesByModifiedByContainsSomething() throws Exception {
        // Initialize the database
        facultyRepository.saveAndFlush(faculty);

        // Get all the facultyList where modifiedBy contains DEFAULT_MODIFIED_BY
        defaultFacultyShouldBeFound("modifiedBy.contains=" + DEFAULT_MODIFIED_BY);

        // Get all the facultyList where modifiedBy contains UPDATED_MODIFIED_BY
        defaultFacultyShouldNotBeFound("modifiedBy.contains=" + UPDATED_MODIFIED_BY);
    }

    @Test
    @Transactional
    public void getAllFacultiesByModifiedByNotContainsSomething() throws Exception {
        // Initialize the database
        facultyRepository.saveAndFlush(faculty);

        // Get all the facultyList where modifiedBy does not contain DEFAULT_MODIFIED_BY
        defaultFacultyShouldNotBeFound("modifiedBy.doesNotContain=" + DEFAULT_MODIFIED_BY);

        // Get all the facultyList where modifiedBy does not contain UPDATED_MODIFIED_BY
        defaultFacultyShouldBeFound("modifiedBy.doesNotContain=" + UPDATED_MODIFIED_BY);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultFacultyShouldBeFound(String filter) throws Exception {
        restFacultyMockMvc.perform(get("/api/faculties?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(faculty.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].shortName").value(hasItem(DEFAULT_SHORT_NAME)))
            .andExpect(jsonPath("$.[*].createdOn").value(hasItem(DEFAULT_CREATED_ON.toString())))
            .andExpect(jsonPath("$.[*].modifiedOn").value(hasItem(DEFAULT_MODIFIED_ON.toString())))
            .andExpect(jsonPath("$.[*].modifiedBy").value(hasItem(DEFAULT_MODIFIED_BY)));

        // Check, that the count call also returns 1
        restFacultyMockMvc.perform(get("/api/faculties/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultFacultyShouldNotBeFound(String filter) throws Exception {
        restFacultyMockMvc.perform(get("/api/faculties?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restFacultyMockMvc.perform(get("/api/faculties/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingFaculty() throws Exception {
        // Get the faculty
        restFacultyMockMvc.perform(get("/api/faculties/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFaculty() throws Exception {
        // Initialize the database
        facultyRepository.saveAndFlush(faculty);

        int databaseSizeBeforeUpdate = facultyRepository.findAll().size();

        // Update the faculty
        Faculty updatedFaculty = facultyRepository.findById(faculty.getId()).get();
        // Disconnect from session so that the updates on updatedFaculty are not directly saved in db
        em.detach(updatedFaculty);
        updatedFaculty
            .name(UPDATED_NAME)
            .shortName(UPDATED_SHORT_NAME)
            .createdOn(UPDATED_CREATED_ON)
            .modifiedOn(UPDATED_MODIFIED_ON)
            .modifiedBy(UPDATED_MODIFIED_BY);
        FacultyDTO facultyDTO = facultyMapper.toDto(updatedFaculty);

        restFacultyMockMvc.perform(put("/api/faculties")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(facultyDTO)))
            .andExpect(status().isOk());

        // Validate the Faculty in the database
        List<Faculty> facultyList = facultyRepository.findAll();
        assertThat(facultyList).hasSize(databaseSizeBeforeUpdate);
        Faculty testFaculty = facultyList.get(facultyList.size() - 1);
        assertThat(testFaculty.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testFaculty.getShortName()).isEqualTo(UPDATED_SHORT_NAME);
        assertThat(testFaculty.getCreatedOn()).isEqualTo(UPDATED_CREATED_ON);
        assertThat(testFaculty.getModifiedOn()).isEqualTo(UPDATED_MODIFIED_ON);
        assertThat(testFaculty.getModifiedBy()).isEqualTo(UPDATED_MODIFIED_BY);
    }

    @Test
    @Transactional
    public void updateNonExistingFaculty() throws Exception {
        int databaseSizeBeforeUpdate = facultyRepository.findAll().size();

        // Create the Faculty
        FacultyDTO facultyDTO = facultyMapper.toDto(faculty);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFacultyMockMvc.perform(put("/api/faculties")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(facultyDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Faculty in the database
        List<Faculty> facultyList = facultyRepository.findAll();
        assertThat(facultyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteFaculty() throws Exception {
        // Initialize the database
        facultyRepository.saveAndFlush(faculty);

        int databaseSizeBeforeDelete = facultyRepository.findAll().size();

        // Delete the faculty
        restFacultyMockMvc.perform(delete("/api/faculties/{id}", faculty.getId())
            .accept(TestUtil.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Faculty> facultyList = facultyRepository.findAll();
        assertThat(facultyList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
