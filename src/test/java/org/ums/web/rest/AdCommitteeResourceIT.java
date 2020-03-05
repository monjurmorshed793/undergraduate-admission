package org.ums.web.rest;

import org.ums.UgAdmissionApp;
import org.ums.domain.AdCommittee;
import org.ums.domain.Semester;
import org.ums.domain.Faculty;
import org.ums.domain.AdmissionDesignation;
import org.ums.domain.User;
import org.ums.repository.AdCommitteeRepository;
import org.ums.service.AdCommitteeService;
import org.ums.service.dto.AdCommitteeDTO;
import org.ums.service.mapper.AdCommitteeMapper;
import org.ums.web.rest.errors.ExceptionTranslator;
import org.ums.service.dto.AdCommitteeCriteria;
import org.ums.service.AdCommitteeQueryService;

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
 * Integration tests for the {@link AdCommitteeResource} REST controller.
 */
@SpringBootTest(classes = UgAdmissionApp.class)
public class AdCommitteeResourceIT {

    private static final LocalDate DEFAULT_CREATED_ON = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATED_ON = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_CREATED_ON = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_MODIFIED_ON = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_MODIFIED_ON = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_MODIFIED_ON = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_MODIFIED_BY = "AAAAAAAAAA";
    private static final String UPDATED_MODIFIED_BY = "BBBBBBBBBB";

    @Autowired
    private AdCommitteeRepository adCommitteeRepository;

    @Autowired
    private AdCommitteeMapper adCommitteeMapper;

    @Autowired
    private AdCommitteeService adCommitteeService;

    @Autowired
    private AdCommitteeQueryService adCommitteeQueryService;

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

    private MockMvc restAdCommitteeMockMvc;

    private AdCommittee adCommittee;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final AdCommitteeResource adCommitteeResource = new AdCommitteeResource(adCommitteeService, adCommitteeQueryService);
        this.restAdCommitteeMockMvc = MockMvcBuilders.standaloneSetup(adCommitteeResource)
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
    public static AdCommittee createEntity(EntityManager em) {
        AdCommittee adCommittee = new AdCommittee()
            .createdOn(DEFAULT_CREATED_ON)
            .modifiedOn(DEFAULT_MODIFIED_ON)
            .modifiedBy(DEFAULT_MODIFIED_BY);
        // Add required entity
        Semester semester;
        if (TestUtil.findAll(em, Semester.class).isEmpty()) {
            semester = SemesterResourceIT.createEntity(em);
            em.persist(semester);
            em.flush();
        } else {
            semester = TestUtil.findAll(em, Semester.class).get(0);
        }
        adCommittee.setSemester(semester);
        // Add required entity
        Faculty faculty;
        if (TestUtil.findAll(em, Faculty.class).isEmpty()) {
            faculty = FacultyResourceIT.createEntity(em);
            em.persist(faculty);
            em.flush();
        } else {
            faculty = TestUtil.findAll(em, Faculty.class).get(0);
        }
        adCommittee.setFaculty(faculty);
        // Add required entity
        AdmissionDesignation admissionDesignation;
        if (TestUtil.findAll(em, AdmissionDesignation.class).isEmpty()) {
            admissionDesignation = AdmissionDesignationResourceIT.createEntity(em);
            em.persist(admissionDesignation);
            em.flush();
        } else {
            admissionDesignation = TestUtil.findAll(em, AdmissionDesignation.class).get(0);
        }
        adCommittee.setDesignation(admissionDesignation);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        adCommittee.setUser(user);
        return adCommittee;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AdCommittee createUpdatedEntity(EntityManager em) {
        AdCommittee adCommittee = new AdCommittee()
            .createdOn(UPDATED_CREATED_ON)
            .modifiedOn(UPDATED_MODIFIED_ON)
            .modifiedBy(UPDATED_MODIFIED_BY);
        // Add required entity
        Semester semester;
        if (TestUtil.findAll(em, Semester.class).isEmpty()) {
            semester = SemesterResourceIT.createUpdatedEntity(em);
            em.persist(semester);
            em.flush();
        } else {
            semester = TestUtil.findAll(em, Semester.class).get(0);
        }
        adCommittee.setSemester(semester);
        // Add required entity
        Faculty faculty;
        if (TestUtil.findAll(em, Faculty.class).isEmpty()) {
            faculty = FacultyResourceIT.createUpdatedEntity(em);
            em.persist(faculty);
            em.flush();
        } else {
            faculty = TestUtil.findAll(em, Faculty.class).get(0);
        }
        adCommittee.setFaculty(faculty);
        // Add required entity
        AdmissionDesignation admissionDesignation;
        if (TestUtil.findAll(em, AdmissionDesignation.class).isEmpty()) {
            admissionDesignation = AdmissionDesignationResourceIT.createUpdatedEntity(em);
            em.persist(admissionDesignation);
            em.flush();
        } else {
            admissionDesignation = TestUtil.findAll(em, AdmissionDesignation.class).get(0);
        }
        adCommittee.setDesignation(admissionDesignation);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        adCommittee.setUser(user);
        return adCommittee;
    }

    @BeforeEach
    public void initTest() {
        adCommittee = createEntity(em);
    }

    @Test
    @Transactional
    public void createAdCommittee() throws Exception {
        int databaseSizeBeforeCreate = adCommitteeRepository.findAll().size();

        // Create the AdCommittee
        AdCommitteeDTO adCommitteeDTO = adCommitteeMapper.toDto(adCommittee);
        restAdCommitteeMockMvc.perform(post("/api/ad-committees")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(adCommitteeDTO)))
            .andExpect(status().isCreated());

        // Validate the AdCommittee in the database
        List<AdCommittee> adCommitteeList = adCommitteeRepository.findAll();
        assertThat(adCommitteeList).hasSize(databaseSizeBeforeCreate + 1);
        AdCommittee testAdCommittee = adCommitteeList.get(adCommitteeList.size() - 1);
        assertThat(testAdCommittee.getCreatedOn()).isEqualTo(DEFAULT_CREATED_ON);
        assertThat(testAdCommittee.getModifiedOn()).isEqualTo(DEFAULT_MODIFIED_ON);
        assertThat(testAdCommittee.getModifiedBy()).isEqualTo(DEFAULT_MODIFIED_BY);
    }

    @Test
    @Transactional
    public void createAdCommitteeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = adCommitteeRepository.findAll().size();

        // Create the AdCommittee with an existing ID
        adCommittee.setId(1L);
        AdCommitteeDTO adCommitteeDTO = adCommitteeMapper.toDto(adCommittee);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAdCommitteeMockMvc.perform(post("/api/ad-committees")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(adCommitteeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the AdCommittee in the database
        List<AdCommittee> adCommitteeList = adCommitteeRepository.findAll();
        assertThat(adCommitteeList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllAdCommittees() throws Exception {
        // Initialize the database
        adCommitteeRepository.saveAndFlush(adCommittee);

        // Get all the adCommitteeList
        restAdCommitteeMockMvc.perform(get("/api/ad-committees?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(adCommittee.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdOn").value(hasItem(DEFAULT_CREATED_ON.toString())))
            .andExpect(jsonPath("$.[*].modifiedOn").value(hasItem(DEFAULT_MODIFIED_ON.toString())))
            .andExpect(jsonPath("$.[*].modifiedBy").value(hasItem(DEFAULT_MODIFIED_BY)));
    }
    
    @Test
    @Transactional
    public void getAdCommittee() throws Exception {
        // Initialize the database
        adCommitteeRepository.saveAndFlush(adCommittee);

        // Get the adCommittee
        restAdCommitteeMockMvc.perform(get("/api/ad-committees/{id}", adCommittee.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(adCommittee.getId().intValue()))
            .andExpect(jsonPath("$.createdOn").value(DEFAULT_CREATED_ON.toString()))
            .andExpect(jsonPath("$.modifiedOn").value(DEFAULT_MODIFIED_ON.toString()))
            .andExpect(jsonPath("$.modifiedBy").value(DEFAULT_MODIFIED_BY));
    }


    @Test
    @Transactional
    public void getAdCommitteesByIdFiltering() throws Exception {
        // Initialize the database
        adCommitteeRepository.saveAndFlush(adCommittee);

        Long id = adCommittee.getId();

        defaultAdCommitteeShouldBeFound("id.equals=" + id);
        defaultAdCommitteeShouldNotBeFound("id.notEquals=" + id);

        defaultAdCommitteeShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultAdCommitteeShouldNotBeFound("id.greaterThan=" + id);

        defaultAdCommitteeShouldBeFound("id.lessThanOrEqual=" + id);
        defaultAdCommitteeShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllAdCommitteesByCreatedOnIsEqualToSomething() throws Exception {
        // Initialize the database
        adCommitteeRepository.saveAndFlush(adCommittee);

        // Get all the adCommitteeList where createdOn equals to DEFAULT_CREATED_ON
        defaultAdCommitteeShouldBeFound("createdOn.equals=" + DEFAULT_CREATED_ON);

        // Get all the adCommitteeList where createdOn equals to UPDATED_CREATED_ON
        defaultAdCommitteeShouldNotBeFound("createdOn.equals=" + UPDATED_CREATED_ON);
    }

    @Test
    @Transactional
    public void getAllAdCommitteesByCreatedOnIsNotEqualToSomething() throws Exception {
        // Initialize the database
        adCommitteeRepository.saveAndFlush(adCommittee);

        // Get all the adCommitteeList where createdOn not equals to DEFAULT_CREATED_ON
        defaultAdCommitteeShouldNotBeFound("createdOn.notEquals=" + DEFAULT_CREATED_ON);

        // Get all the adCommitteeList where createdOn not equals to UPDATED_CREATED_ON
        defaultAdCommitteeShouldBeFound("createdOn.notEquals=" + UPDATED_CREATED_ON);
    }

    @Test
    @Transactional
    public void getAllAdCommitteesByCreatedOnIsInShouldWork() throws Exception {
        // Initialize the database
        adCommitteeRepository.saveAndFlush(adCommittee);

        // Get all the adCommitteeList where createdOn in DEFAULT_CREATED_ON or UPDATED_CREATED_ON
        defaultAdCommitteeShouldBeFound("createdOn.in=" + DEFAULT_CREATED_ON + "," + UPDATED_CREATED_ON);

        // Get all the adCommitteeList where createdOn equals to UPDATED_CREATED_ON
        defaultAdCommitteeShouldNotBeFound("createdOn.in=" + UPDATED_CREATED_ON);
    }

    @Test
    @Transactional
    public void getAllAdCommitteesByCreatedOnIsNullOrNotNull() throws Exception {
        // Initialize the database
        adCommitteeRepository.saveAndFlush(adCommittee);

        // Get all the adCommitteeList where createdOn is not null
        defaultAdCommitteeShouldBeFound("createdOn.specified=true");

        // Get all the adCommitteeList where createdOn is null
        defaultAdCommitteeShouldNotBeFound("createdOn.specified=false");
    }

    @Test
    @Transactional
    public void getAllAdCommitteesByCreatedOnIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        adCommitteeRepository.saveAndFlush(adCommittee);

        // Get all the adCommitteeList where createdOn is greater than or equal to DEFAULT_CREATED_ON
        defaultAdCommitteeShouldBeFound("createdOn.greaterThanOrEqual=" + DEFAULT_CREATED_ON);

        // Get all the adCommitteeList where createdOn is greater than or equal to UPDATED_CREATED_ON
        defaultAdCommitteeShouldNotBeFound("createdOn.greaterThanOrEqual=" + UPDATED_CREATED_ON);
    }

    @Test
    @Transactional
    public void getAllAdCommitteesByCreatedOnIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        adCommitteeRepository.saveAndFlush(adCommittee);

        // Get all the adCommitteeList where createdOn is less than or equal to DEFAULT_CREATED_ON
        defaultAdCommitteeShouldBeFound("createdOn.lessThanOrEqual=" + DEFAULT_CREATED_ON);

        // Get all the adCommitteeList where createdOn is less than or equal to SMALLER_CREATED_ON
        defaultAdCommitteeShouldNotBeFound("createdOn.lessThanOrEqual=" + SMALLER_CREATED_ON);
    }

    @Test
    @Transactional
    public void getAllAdCommitteesByCreatedOnIsLessThanSomething() throws Exception {
        // Initialize the database
        adCommitteeRepository.saveAndFlush(adCommittee);

        // Get all the adCommitteeList where createdOn is less than DEFAULT_CREATED_ON
        defaultAdCommitteeShouldNotBeFound("createdOn.lessThan=" + DEFAULT_CREATED_ON);

        // Get all the adCommitteeList where createdOn is less than UPDATED_CREATED_ON
        defaultAdCommitteeShouldBeFound("createdOn.lessThan=" + UPDATED_CREATED_ON);
    }

    @Test
    @Transactional
    public void getAllAdCommitteesByCreatedOnIsGreaterThanSomething() throws Exception {
        // Initialize the database
        adCommitteeRepository.saveAndFlush(adCommittee);

        // Get all the adCommitteeList where createdOn is greater than DEFAULT_CREATED_ON
        defaultAdCommitteeShouldNotBeFound("createdOn.greaterThan=" + DEFAULT_CREATED_ON);

        // Get all the adCommitteeList where createdOn is greater than SMALLER_CREATED_ON
        defaultAdCommitteeShouldBeFound("createdOn.greaterThan=" + SMALLER_CREATED_ON);
    }


    @Test
    @Transactional
    public void getAllAdCommitteesByModifiedOnIsEqualToSomething() throws Exception {
        // Initialize the database
        adCommitteeRepository.saveAndFlush(adCommittee);

        // Get all the adCommitteeList where modifiedOn equals to DEFAULT_MODIFIED_ON
        defaultAdCommitteeShouldBeFound("modifiedOn.equals=" + DEFAULT_MODIFIED_ON);

        // Get all the adCommitteeList where modifiedOn equals to UPDATED_MODIFIED_ON
        defaultAdCommitteeShouldNotBeFound("modifiedOn.equals=" + UPDATED_MODIFIED_ON);
    }

    @Test
    @Transactional
    public void getAllAdCommitteesByModifiedOnIsNotEqualToSomething() throws Exception {
        // Initialize the database
        adCommitteeRepository.saveAndFlush(adCommittee);

        // Get all the adCommitteeList where modifiedOn not equals to DEFAULT_MODIFIED_ON
        defaultAdCommitteeShouldNotBeFound("modifiedOn.notEquals=" + DEFAULT_MODIFIED_ON);

        // Get all the adCommitteeList where modifiedOn not equals to UPDATED_MODIFIED_ON
        defaultAdCommitteeShouldBeFound("modifiedOn.notEquals=" + UPDATED_MODIFIED_ON);
    }

    @Test
    @Transactional
    public void getAllAdCommitteesByModifiedOnIsInShouldWork() throws Exception {
        // Initialize the database
        adCommitteeRepository.saveAndFlush(adCommittee);

        // Get all the adCommitteeList where modifiedOn in DEFAULT_MODIFIED_ON or UPDATED_MODIFIED_ON
        defaultAdCommitteeShouldBeFound("modifiedOn.in=" + DEFAULT_MODIFIED_ON + "," + UPDATED_MODIFIED_ON);

        // Get all the adCommitteeList where modifiedOn equals to UPDATED_MODIFIED_ON
        defaultAdCommitteeShouldNotBeFound("modifiedOn.in=" + UPDATED_MODIFIED_ON);
    }

    @Test
    @Transactional
    public void getAllAdCommitteesByModifiedOnIsNullOrNotNull() throws Exception {
        // Initialize the database
        adCommitteeRepository.saveAndFlush(adCommittee);

        // Get all the adCommitteeList where modifiedOn is not null
        defaultAdCommitteeShouldBeFound("modifiedOn.specified=true");

        // Get all the adCommitteeList where modifiedOn is null
        defaultAdCommitteeShouldNotBeFound("modifiedOn.specified=false");
    }

    @Test
    @Transactional
    public void getAllAdCommitteesByModifiedOnIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        adCommitteeRepository.saveAndFlush(adCommittee);

        // Get all the adCommitteeList where modifiedOn is greater than or equal to DEFAULT_MODIFIED_ON
        defaultAdCommitteeShouldBeFound("modifiedOn.greaterThanOrEqual=" + DEFAULT_MODIFIED_ON);

        // Get all the adCommitteeList where modifiedOn is greater than or equal to UPDATED_MODIFIED_ON
        defaultAdCommitteeShouldNotBeFound("modifiedOn.greaterThanOrEqual=" + UPDATED_MODIFIED_ON);
    }

    @Test
    @Transactional
    public void getAllAdCommitteesByModifiedOnIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        adCommitteeRepository.saveAndFlush(adCommittee);

        // Get all the adCommitteeList where modifiedOn is less than or equal to DEFAULT_MODIFIED_ON
        defaultAdCommitteeShouldBeFound("modifiedOn.lessThanOrEqual=" + DEFAULT_MODIFIED_ON);

        // Get all the adCommitteeList where modifiedOn is less than or equal to SMALLER_MODIFIED_ON
        defaultAdCommitteeShouldNotBeFound("modifiedOn.lessThanOrEqual=" + SMALLER_MODIFIED_ON);
    }

    @Test
    @Transactional
    public void getAllAdCommitteesByModifiedOnIsLessThanSomething() throws Exception {
        // Initialize the database
        adCommitteeRepository.saveAndFlush(adCommittee);

        // Get all the adCommitteeList where modifiedOn is less than DEFAULT_MODIFIED_ON
        defaultAdCommitteeShouldNotBeFound("modifiedOn.lessThan=" + DEFAULT_MODIFIED_ON);

        // Get all the adCommitteeList where modifiedOn is less than UPDATED_MODIFIED_ON
        defaultAdCommitteeShouldBeFound("modifiedOn.lessThan=" + UPDATED_MODIFIED_ON);
    }

    @Test
    @Transactional
    public void getAllAdCommitteesByModifiedOnIsGreaterThanSomething() throws Exception {
        // Initialize the database
        adCommitteeRepository.saveAndFlush(adCommittee);

        // Get all the adCommitteeList where modifiedOn is greater than DEFAULT_MODIFIED_ON
        defaultAdCommitteeShouldNotBeFound("modifiedOn.greaterThan=" + DEFAULT_MODIFIED_ON);

        // Get all the adCommitteeList where modifiedOn is greater than SMALLER_MODIFIED_ON
        defaultAdCommitteeShouldBeFound("modifiedOn.greaterThan=" + SMALLER_MODIFIED_ON);
    }


    @Test
    @Transactional
    public void getAllAdCommitteesByModifiedByIsEqualToSomething() throws Exception {
        // Initialize the database
        adCommitteeRepository.saveAndFlush(adCommittee);

        // Get all the adCommitteeList where modifiedBy equals to DEFAULT_MODIFIED_BY
        defaultAdCommitteeShouldBeFound("modifiedBy.equals=" + DEFAULT_MODIFIED_BY);

        // Get all the adCommitteeList where modifiedBy equals to UPDATED_MODIFIED_BY
        defaultAdCommitteeShouldNotBeFound("modifiedBy.equals=" + UPDATED_MODIFIED_BY);
    }

    @Test
    @Transactional
    public void getAllAdCommitteesByModifiedByIsNotEqualToSomething() throws Exception {
        // Initialize the database
        adCommitteeRepository.saveAndFlush(adCommittee);

        // Get all the adCommitteeList where modifiedBy not equals to DEFAULT_MODIFIED_BY
        defaultAdCommitteeShouldNotBeFound("modifiedBy.notEquals=" + DEFAULT_MODIFIED_BY);

        // Get all the adCommitteeList where modifiedBy not equals to UPDATED_MODIFIED_BY
        defaultAdCommitteeShouldBeFound("modifiedBy.notEquals=" + UPDATED_MODIFIED_BY);
    }

    @Test
    @Transactional
    public void getAllAdCommitteesByModifiedByIsInShouldWork() throws Exception {
        // Initialize the database
        adCommitteeRepository.saveAndFlush(adCommittee);

        // Get all the adCommitteeList where modifiedBy in DEFAULT_MODIFIED_BY or UPDATED_MODIFIED_BY
        defaultAdCommitteeShouldBeFound("modifiedBy.in=" + DEFAULT_MODIFIED_BY + "," + UPDATED_MODIFIED_BY);

        // Get all the adCommitteeList where modifiedBy equals to UPDATED_MODIFIED_BY
        defaultAdCommitteeShouldNotBeFound("modifiedBy.in=" + UPDATED_MODIFIED_BY);
    }

    @Test
    @Transactional
    public void getAllAdCommitteesByModifiedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        adCommitteeRepository.saveAndFlush(adCommittee);

        // Get all the adCommitteeList where modifiedBy is not null
        defaultAdCommitteeShouldBeFound("modifiedBy.specified=true");

        // Get all the adCommitteeList where modifiedBy is null
        defaultAdCommitteeShouldNotBeFound("modifiedBy.specified=false");
    }
                @Test
    @Transactional
    public void getAllAdCommitteesByModifiedByContainsSomething() throws Exception {
        // Initialize the database
        adCommitteeRepository.saveAndFlush(adCommittee);

        // Get all the adCommitteeList where modifiedBy contains DEFAULT_MODIFIED_BY
        defaultAdCommitteeShouldBeFound("modifiedBy.contains=" + DEFAULT_MODIFIED_BY);

        // Get all the adCommitteeList where modifiedBy contains UPDATED_MODIFIED_BY
        defaultAdCommitteeShouldNotBeFound("modifiedBy.contains=" + UPDATED_MODIFIED_BY);
    }

    @Test
    @Transactional
    public void getAllAdCommitteesByModifiedByNotContainsSomething() throws Exception {
        // Initialize the database
        adCommitteeRepository.saveAndFlush(adCommittee);

        // Get all the adCommitteeList where modifiedBy does not contain DEFAULT_MODIFIED_BY
        defaultAdCommitteeShouldNotBeFound("modifiedBy.doesNotContain=" + DEFAULT_MODIFIED_BY);

        // Get all the adCommitteeList where modifiedBy does not contain UPDATED_MODIFIED_BY
        defaultAdCommitteeShouldBeFound("modifiedBy.doesNotContain=" + UPDATED_MODIFIED_BY);
    }


    @Test
    @Transactional
    public void getAllAdCommitteesBySemesterIsEqualToSomething() throws Exception {
        // Get already existing entity
        Semester semester = adCommittee.getSemester();
        adCommitteeRepository.saveAndFlush(adCommittee);
        Long semesterId = semester.getId();

        // Get all the adCommitteeList where semester equals to semesterId
        defaultAdCommitteeShouldBeFound("semesterId.equals=" + semesterId);

        // Get all the adCommitteeList where semester equals to semesterId + 1
        defaultAdCommitteeShouldNotBeFound("semesterId.equals=" + (semesterId + 1));
    }


    @Test
    @Transactional
    public void getAllAdCommitteesByFacultyIsEqualToSomething() throws Exception {
        // Get already existing entity
        Faculty faculty = adCommittee.getFaculty();
        adCommitteeRepository.saveAndFlush(adCommittee);
        Long facultyId = faculty.getId();

        // Get all the adCommitteeList where faculty equals to facultyId
        defaultAdCommitteeShouldBeFound("facultyId.equals=" + facultyId);

        // Get all the adCommitteeList where faculty equals to facultyId + 1
        defaultAdCommitteeShouldNotBeFound("facultyId.equals=" + (facultyId + 1));
    }


    @Test
    @Transactional
    public void getAllAdCommitteesByDesignationIsEqualToSomething() throws Exception {
        // Get already existing entity
        AdmissionDesignation designation = adCommittee.getDesignation();
        adCommitteeRepository.saveAndFlush(adCommittee);
        Long designationId = designation.getId();

        // Get all the adCommitteeList where designation equals to designationId
        defaultAdCommitteeShouldBeFound("designationId.equals=" + designationId);

        // Get all the adCommitteeList where designation equals to designationId + 1
        defaultAdCommitteeShouldNotBeFound("designationId.equals=" + (designationId + 1));
    }


    @Test
    @Transactional
    public void getAllAdCommitteesByUserIsEqualToSomething() throws Exception {
        // Get already existing entity
        User user = adCommittee.getUser();
        adCommitteeRepository.saveAndFlush(adCommittee);
        Long userId = user.getId();

        // Get all the adCommitteeList where user equals to userId
        defaultAdCommitteeShouldBeFound("userId.equals=" + userId);

        // Get all the adCommitteeList where user equals to userId + 1
        defaultAdCommitteeShouldNotBeFound("userId.equals=" + (userId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAdCommitteeShouldBeFound(String filter) throws Exception {
        restAdCommitteeMockMvc.perform(get("/api/ad-committees?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(adCommittee.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdOn").value(hasItem(DEFAULT_CREATED_ON.toString())))
            .andExpect(jsonPath("$.[*].modifiedOn").value(hasItem(DEFAULT_MODIFIED_ON.toString())))
            .andExpect(jsonPath("$.[*].modifiedBy").value(hasItem(DEFAULT_MODIFIED_BY)));

        // Check, that the count call also returns 1
        restAdCommitteeMockMvc.perform(get("/api/ad-committees/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAdCommitteeShouldNotBeFound(String filter) throws Exception {
        restAdCommitteeMockMvc.perform(get("/api/ad-committees?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAdCommitteeMockMvc.perform(get("/api/ad-committees/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingAdCommittee() throws Exception {
        // Get the adCommittee
        restAdCommitteeMockMvc.perform(get("/api/ad-committees/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAdCommittee() throws Exception {
        // Initialize the database
        adCommitteeRepository.saveAndFlush(adCommittee);

        int databaseSizeBeforeUpdate = adCommitteeRepository.findAll().size();

        // Update the adCommittee
        AdCommittee updatedAdCommittee = adCommitteeRepository.findById(adCommittee.getId()).get();
        // Disconnect from session so that the updates on updatedAdCommittee are not directly saved in db
        em.detach(updatedAdCommittee);
        updatedAdCommittee
            .createdOn(UPDATED_CREATED_ON)
            .modifiedOn(UPDATED_MODIFIED_ON)
            .modifiedBy(UPDATED_MODIFIED_BY);
        AdCommitteeDTO adCommitteeDTO = adCommitteeMapper.toDto(updatedAdCommittee);

        restAdCommitteeMockMvc.perform(put("/api/ad-committees")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(adCommitteeDTO)))
            .andExpect(status().isOk());

        // Validate the AdCommittee in the database
        List<AdCommittee> adCommitteeList = adCommitteeRepository.findAll();
        assertThat(adCommitteeList).hasSize(databaseSizeBeforeUpdate);
        AdCommittee testAdCommittee = adCommitteeList.get(adCommitteeList.size() - 1);
        assertThat(testAdCommittee.getCreatedOn()).isEqualTo(UPDATED_CREATED_ON);
        assertThat(testAdCommittee.getModifiedOn()).isEqualTo(UPDATED_MODIFIED_ON);
        assertThat(testAdCommittee.getModifiedBy()).isEqualTo(UPDATED_MODIFIED_BY);
    }

    @Test
    @Transactional
    public void updateNonExistingAdCommittee() throws Exception {
        int databaseSizeBeforeUpdate = adCommitteeRepository.findAll().size();

        // Create the AdCommittee
        AdCommitteeDTO adCommitteeDTO = adCommitteeMapper.toDto(adCommittee);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAdCommitteeMockMvc.perform(put("/api/ad-committees")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(adCommitteeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the AdCommittee in the database
        List<AdCommittee> adCommitteeList = adCommitteeRepository.findAll();
        assertThat(adCommitteeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteAdCommittee() throws Exception {
        // Initialize the database
        adCommitteeRepository.saveAndFlush(adCommittee);

        int databaseSizeBeforeDelete = adCommitteeRepository.findAll().size();

        // Delete the adCommittee
        restAdCommitteeMockMvc.perform(delete("/api/ad-committees/{id}", adCommittee.getId())
            .accept(TestUtil.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AdCommittee> adCommitteeList = adCommitteeRepository.findAll();
        assertThat(adCommitteeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
