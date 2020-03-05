package org.ums.web.rest;

import org.ums.UgAdmissionApp;
import org.ums.domain.AdmissionDesignation;
import org.ums.repository.AdmissionDesignationRepository;
import org.ums.service.AdmissionDesignationService;
import org.ums.service.dto.AdmissionDesignationDTO;
import org.ums.service.mapper.AdmissionDesignationMapper;
import org.ums.web.rest.errors.ExceptionTranslator;
import org.ums.service.dto.AdmissionDesignationCriteria;
import org.ums.service.AdmissionDesignationQueryService;

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
import org.springframework.util.Base64Utils;
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
 * Integration tests for the {@link AdmissionDesignationResource} REST controller.
 */
@SpringBootTest(classes = UgAdmissionApp.class)
public class AdmissionDesignationResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_CREATED_ON = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATED_ON = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_CREATED_ON = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_MODIFIED_ON = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_MODIFIED_ON = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_MODIFIED_ON = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_MODIFIED_BY = "AAAAAAAAAA";
    private static final String UPDATED_MODIFIED_BY = "BBBBBBBBBB";

    @Autowired
    private AdmissionDesignationRepository admissionDesignationRepository;

    @Autowired
    private AdmissionDesignationMapper admissionDesignationMapper;

    @Autowired
    private AdmissionDesignationService admissionDesignationService;

    @Autowired
    private AdmissionDesignationQueryService admissionDesignationQueryService;

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

    private MockMvc restAdmissionDesignationMockMvc;

    private AdmissionDesignation admissionDesignation;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final AdmissionDesignationResource admissionDesignationResource = new AdmissionDesignationResource(admissionDesignationService, admissionDesignationQueryService);
        this.restAdmissionDesignationMockMvc = MockMvcBuilders.standaloneSetup(admissionDesignationResource)
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
    public static AdmissionDesignation createEntity(EntityManager em) {
        AdmissionDesignation admissionDesignation = new AdmissionDesignation()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .createdOn(DEFAULT_CREATED_ON)
            .modifiedOn(DEFAULT_MODIFIED_ON)
            .modifiedBy(DEFAULT_MODIFIED_BY);
        return admissionDesignation;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AdmissionDesignation createUpdatedEntity(EntityManager em) {
        AdmissionDesignation admissionDesignation = new AdmissionDesignation()
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .createdOn(UPDATED_CREATED_ON)
            .modifiedOn(UPDATED_MODIFIED_ON)
            .modifiedBy(UPDATED_MODIFIED_BY);
        return admissionDesignation;
    }

    @BeforeEach
    public void initTest() {
        admissionDesignation = createEntity(em);
    }

    @Test
    @Transactional
    public void createAdmissionDesignation() throws Exception {
        int databaseSizeBeforeCreate = admissionDesignationRepository.findAll().size();

        // Create the AdmissionDesignation
        AdmissionDesignationDTO admissionDesignationDTO = admissionDesignationMapper.toDto(admissionDesignation);
        restAdmissionDesignationMockMvc.perform(post("/api/admission-designations")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(admissionDesignationDTO)))
            .andExpect(status().isCreated());

        // Validate the AdmissionDesignation in the database
        List<AdmissionDesignation> admissionDesignationList = admissionDesignationRepository.findAll();
        assertThat(admissionDesignationList).hasSize(databaseSizeBeforeCreate + 1);
        AdmissionDesignation testAdmissionDesignation = admissionDesignationList.get(admissionDesignationList.size() - 1);
        assertThat(testAdmissionDesignation.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testAdmissionDesignation.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testAdmissionDesignation.getCreatedOn()).isEqualTo(DEFAULT_CREATED_ON);
        assertThat(testAdmissionDesignation.getModifiedOn()).isEqualTo(DEFAULT_MODIFIED_ON);
        assertThat(testAdmissionDesignation.getModifiedBy()).isEqualTo(DEFAULT_MODIFIED_BY);
    }

    @Test
    @Transactional
    public void createAdmissionDesignationWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = admissionDesignationRepository.findAll().size();

        // Create the AdmissionDesignation with an existing ID
        admissionDesignation.setId(1L);
        AdmissionDesignationDTO admissionDesignationDTO = admissionDesignationMapper.toDto(admissionDesignation);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAdmissionDesignationMockMvc.perform(post("/api/admission-designations")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(admissionDesignationDTO)))
            .andExpect(status().isBadRequest());

        // Validate the AdmissionDesignation in the database
        List<AdmissionDesignation> admissionDesignationList = admissionDesignationRepository.findAll();
        assertThat(admissionDesignationList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = admissionDesignationRepository.findAll().size();
        // set the field null
        admissionDesignation.setName(null);

        // Create the AdmissionDesignation, which fails.
        AdmissionDesignationDTO admissionDesignationDTO = admissionDesignationMapper.toDto(admissionDesignation);

        restAdmissionDesignationMockMvc.perform(post("/api/admission-designations")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(admissionDesignationDTO)))
            .andExpect(status().isBadRequest());

        List<AdmissionDesignation> admissionDesignationList = admissionDesignationRepository.findAll();
        assertThat(admissionDesignationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllAdmissionDesignations() throws Exception {
        // Initialize the database
        admissionDesignationRepository.saveAndFlush(admissionDesignation);

        // Get all the admissionDesignationList
        restAdmissionDesignationMockMvc.perform(get("/api/admission-designations?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(admissionDesignation.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].createdOn").value(hasItem(DEFAULT_CREATED_ON.toString())))
            .andExpect(jsonPath("$.[*].modifiedOn").value(hasItem(DEFAULT_MODIFIED_ON.toString())))
            .andExpect(jsonPath("$.[*].modifiedBy").value(hasItem(DEFAULT_MODIFIED_BY)));
    }
    
    @Test
    @Transactional
    public void getAdmissionDesignation() throws Exception {
        // Initialize the database
        admissionDesignationRepository.saveAndFlush(admissionDesignation);

        // Get the admissionDesignation
        restAdmissionDesignationMockMvc.perform(get("/api/admission-designations/{id}", admissionDesignation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(admissionDesignation.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.createdOn").value(DEFAULT_CREATED_ON.toString()))
            .andExpect(jsonPath("$.modifiedOn").value(DEFAULT_MODIFIED_ON.toString()))
            .andExpect(jsonPath("$.modifiedBy").value(DEFAULT_MODIFIED_BY));
    }


    @Test
    @Transactional
    public void getAdmissionDesignationsByIdFiltering() throws Exception {
        // Initialize the database
        admissionDesignationRepository.saveAndFlush(admissionDesignation);

        Long id = admissionDesignation.getId();

        defaultAdmissionDesignationShouldBeFound("id.equals=" + id);
        defaultAdmissionDesignationShouldNotBeFound("id.notEquals=" + id);

        defaultAdmissionDesignationShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultAdmissionDesignationShouldNotBeFound("id.greaterThan=" + id);

        defaultAdmissionDesignationShouldBeFound("id.lessThanOrEqual=" + id);
        defaultAdmissionDesignationShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllAdmissionDesignationsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        admissionDesignationRepository.saveAndFlush(admissionDesignation);

        // Get all the admissionDesignationList where name equals to DEFAULT_NAME
        defaultAdmissionDesignationShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the admissionDesignationList where name equals to UPDATED_NAME
        defaultAdmissionDesignationShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllAdmissionDesignationsByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        admissionDesignationRepository.saveAndFlush(admissionDesignation);

        // Get all the admissionDesignationList where name not equals to DEFAULT_NAME
        defaultAdmissionDesignationShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the admissionDesignationList where name not equals to UPDATED_NAME
        defaultAdmissionDesignationShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllAdmissionDesignationsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        admissionDesignationRepository.saveAndFlush(admissionDesignation);

        // Get all the admissionDesignationList where name in DEFAULT_NAME or UPDATED_NAME
        defaultAdmissionDesignationShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the admissionDesignationList where name equals to UPDATED_NAME
        defaultAdmissionDesignationShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllAdmissionDesignationsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        admissionDesignationRepository.saveAndFlush(admissionDesignation);

        // Get all the admissionDesignationList where name is not null
        defaultAdmissionDesignationShouldBeFound("name.specified=true");

        // Get all the admissionDesignationList where name is null
        defaultAdmissionDesignationShouldNotBeFound("name.specified=false");
    }
                @Test
    @Transactional
    public void getAllAdmissionDesignationsByNameContainsSomething() throws Exception {
        // Initialize the database
        admissionDesignationRepository.saveAndFlush(admissionDesignation);

        // Get all the admissionDesignationList where name contains DEFAULT_NAME
        defaultAdmissionDesignationShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the admissionDesignationList where name contains UPDATED_NAME
        defaultAdmissionDesignationShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllAdmissionDesignationsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        admissionDesignationRepository.saveAndFlush(admissionDesignation);

        // Get all the admissionDesignationList where name does not contain DEFAULT_NAME
        defaultAdmissionDesignationShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the admissionDesignationList where name does not contain UPDATED_NAME
        defaultAdmissionDesignationShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }


    @Test
    @Transactional
    public void getAllAdmissionDesignationsByCreatedOnIsEqualToSomething() throws Exception {
        // Initialize the database
        admissionDesignationRepository.saveAndFlush(admissionDesignation);

        // Get all the admissionDesignationList where createdOn equals to DEFAULT_CREATED_ON
        defaultAdmissionDesignationShouldBeFound("createdOn.equals=" + DEFAULT_CREATED_ON);

        // Get all the admissionDesignationList where createdOn equals to UPDATED_CREATED_ON
        defaultAdmissionDesignationShouldNotBeFound("createdOn.equals=" + UPDATED_CREATED_ON);
    }

    @Test
    @Transactional
    public void getAllAdmissionDesignationsByCreatedOnIsNotEqualToSomething() throws Exception {
        // Initialize the database
        admissionDesignationRepository.saveAndFlush(admissionDesignation);

        // Get all the admissionDesignationList where createdOn not equals to DEFAULT_CREATED_ON
        defaultAdmissionDesignationShouldNotBeFound("createdOn.notEquals=" + DEFAULT_CREATED_ON);

        // Get all the admissionDesignationList where createdOn not equals to UPDATED_CREATED_ON
        defaultAdmissionDesignationShouldBeFound("createdOn.notEquals=" + UPDATED_CREATED_ON);
    }

    @Test
    @Transactional
    public void getAllAdmissionDesignationsByCreatedOnIsInShouldWork() throws Exception {
        // Initialize the database
        admissionDesignationRepository.saveAndFlush(admissionDesignation);

        // Get all the admissionDesignationList where createdOn in DEFAULT_CREATED_ON or UPDATED_CREATED_ON
        defaultAdmissionDesignationShouldBeFound("createdOn.in=" + DEFAULT_CREATED_ON + "," + UPDATED_CREATED_ON);

        // Get all the admissionDesignationList where createdOn equals to UPDATED_CREATED_ON
        defaultAdmissionDesignationShouldNotBeFound("createdOn.in=" + UPDATED_CREATED_ON);
    }

    @Test
    @Transactional
    public void getAllAdmissionDesignationsByCreatedOnIsNullOrNotNull() throws Exception {
        // Initialize the database
        admissionDesignationRepository.saveAndFlush(admissionDesignation);

        // Get all the admissionDesignationList where createdOn is not null
        defaultAdmissionDesignationShouldBeFound("createdOn.specified=true");

        // Get all the admissionDesignationList where createdOn is null
        defaultAdmissionDesignationShouldNotBeFound("createdOn.specified=false");
    }

    @Test
    @Transactional
    public void getAllAdmissionDesignationsByCreatedOnIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        admissionDesignationRepository.saveAndFlush(admissionDesignation);

        // Get all the admissionDesignationList where createdOn is greater than or equal to DEFAULT_CREATED_ON
        defaultAdmissionDesignationShouldBeFound("createdOn.greaterThanOrEqual=" + DEFAULT_CREATED_ON);

        // Get all the admissionDesignationList where createdOn is greater than or equal to UPDATED_CREATED_ON
        defaultAdmissionDesignationShouldNotBeFound("createdOn.greaterThanOrEqual=" + UPDATED_CREATED_ON);
    }

    @Test
    @Transactional
    public void getAllAdmissionDesignationsByCreatedOnIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        admissionDesignationRepository.saveAndFlush(admissionDesignation);

        // Get all the admissionDesignationList where createdOn is less than or equal to DEFAULT_CREATED_ON
        defaultAdmissionDesignationShouldBeFound("createdOn.lessThanOrEqual=" + DEFAULT_CREATED_ON);

        // Get all the admissionDesignationList where createdOn is less than or equal to SMALLER_CREATED_ON
        defaultAdmissionDesignationShouldNotBeFound("createdOn.lessThanOrEqual=" + SMALLER_CREATED_ON);
    }

    @Test
    @Transactional
    public void getAllAdmissionDesignationsByCreatedOnIsLessThanSomething() throws Exception {
        // Initialize the database
        admissionDesignationRepository.saveAndFlush(admissionDesignation);

        // Get all the admissionDesignationList where createdOn is less than DEFAULT_CREATED_ON
        defaultAdmissionDesignationShouldNotBeFound("createdOn.lessThan=" + DEFAULT_CREATED_ON);

        // Get all the admissionDesignationList where createdOn is less than UPDATED_CREATED_ON
        defaultAdmissionDesignationShouldBeFound("createdOn.lessThan=" + UPDATED_CREATED_ON);
    }

    @Test
    @Transactional
    public void getAllAdmissionDesignationsByCreatedOnIsGreaterThanSomething() throws Exception {
        // Initialize the database
        admissionDesignationRepository.saveAndFlush(admissionDesignation);

        // Get all the admissionDesignationList where createdOn is greater than DEFAULT_CREATED_ON
        defaultAdmissionDesignationShouldNotBeFound("createdOn.greaterThan=" + DEFAULT_CREATED_ON);

        // Get all the admissionDesignationList where createdOn is greater than SMALLER_CREATED_ON
        defaultAdmissionDesignationShouldBeFound("createdOn.greaterThan=" + SMALLER_CREATED_ON);
    }


    @Test
    @Transactional
    public void getAllAdmissionDesignationsByModifiedOnIsEqualToSomething() throws Exception {
        // Initialize the database
        admissionDesignationRepository.saveAndFlush(admissionDesignation);

        // Get all the admissionDesignationList where modifiedOn equals to DEFAULT_MODIFIED_ON
        defaultAdmissionDesignationShouldBeFound("modifiedOn.equals=" + DEFAULT_MODIFIED_ON);

        // Get all the admissionDesignationList where modifiedOn equals to UPDATED_MODIFIED_ON
        defaultAdmissionDesignationShouldNotBeFound("modifiedOn.equals=" + UPDATED_MODIFIED_ON);
    }

    @Test
    @Transactional
    public void getAllAdmissionDesignationsByModifiedOnIsNotEqualToSomething() throws Exception {
        // Initialize the database
        admissionDesignationRepository.saveAndFlush(admissionDesignation);

        // Get all the admissionDesignationList where modifiedOn not equals to DEFAULT_MODIFIED_ON
        defaultAdmissionDesignationShouldNotBeFound("modifiedOn.notEquals=" + DEFAULT_MODIFIED_ON);

        // Get all the admissionDesignationList where modifiedOn not equals to UPDATED_MODIFIED_ON
        defaultAdmissionDesignationShouldBeFound("modifiedOn.notEquals=" + UPDATED_MODIFIED_ON);
    }

    @Test
    @Transactional
    public void getAllAdmissionDesignationsByModifiedOnIsInShouldWork() throws Exception {
        // Initialize the database
        admissionDesignationRepository.saveAndFlush(admissionDesignation);

        // Get all the admissionDesignationList where modifiedOn in DEFAULT_MODIFIED_ON or UPDATED_MODIFIED_ON
        defaultAdmissionDesignationShouldBeFound("modifiedOn.in=" + DEFAULT_MODIFIED_ON + "," + UPDATED_MODIFIED_ON);

        // Get all the admissionDesignationList where modifiedOn equals to UPDATED_MODIFIED_ON
        defaultAdmissionDesignationShouldNotBeFound("modifiedOn.in=" + UPDATED_MODIFIED_ON);
    }

    @Test
    @Transactional
    public void getAllAdmissionDesignationsByModifiedOnIsNullOrNotNull() throws Exception {
        // Initialize the database
        admissionDesignationRepository.saveAndFlush(admissionDesignation);

        // Get all the admissionDesignationList where modifiedOn is not null
        defaultAdmissionDesignationShouldBeFound("modifiedOn.specified=true");

        // Get all the admissionDesignationList where modifiedOn is null
        defaultAdmissionDesignationShouldNotBeFound("modifiedOn.specified=false");
    }

    @Test
    @Transactional
    public void getAllAdmissionDesignationsByModifiedOnIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        admissionDesignationRepository.saveAndFlush(admissionDesignation);

        // Get all the admissionDesignationList where modifiedOn is greater than or equal to DEFAULT_MODIFIED_ON
        defaultAdmissionDesignationShouldBeFound("modifiedOn.greaterThanOrEqual=" + DEFAULT_MODIFIED_ON);

        // Get all the admissionDesignationList where modifiedOn is greater than or equal to UPDATED_MODIFIED_ON
        defaultAdmissionDesignationShouldNotBeFound("modifiedOn.greaterThanOrEqual=" + UPDATED_MODIFIED_ON);
    }

    @Test
    @Transactional
    public void getAllAdmissionDesignationsByModifiedOnIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        admissionDesignationRepository.saveAndFlush(admissionDesignation);

        // Get all the admissionDesignationList where modifiedOn is less than or equal to DEFAULT_MODIFIED_ON
        defaultAdmissionDesignationShouldBeFound("modifiedOn.lessThanOrEqual=" + DEFAULT_MODIFIED_ON);

        // Get all the admissionDesignationList where modifiedOn is less than or equal to SMALLER_MODIFIED_ON
        defaultAdmissionDesignationShouldNotBeFound("modifiedOn.lessThanOrEqual=" + SMALLER_MODIFIED_ON);
    }

    @Test
    @Transactional
    public void getAllAdmissionDesignationsByModifiedOnIsLessThanSomething() throws Exception {
        // Initialize the database
        admissionDesignationRepository.saveAndFlush(admissionDesignation);

        // Get all the admissionDesignationList where modifiedOn is less than DEFAULT_MODIFIED_ON
        defaultAdmissionDesignationShouldNotBeFound("modifiedOn.lessThan=" + DEFAULT_MODIFIED_ON);

        // Get all the admissionDesignationList where modifiedOn is less than UPDATED_MODIFIED_ON
        defaultAdmissionDesignationShouldBeFound("modifiedOn.lessThan=" + UPDATED_MODIFIED_ON);
    }

    @Test
    @Transactional
    public void getAllAdmissionDesignationsByModifiedOnIsGreaterThanSomething() throws Exception {
        // Initialize the database
        admissionDesignationRepository.saveAndFlush(admissionDesignation);

        // Get all the admissionDesignationList where modifiedOn is greater than DEFAULT_MODIFIED_ON
        defaultAdmissionDesignationShouldNotBeFound("modifiedOn.greaterThan=" + DEFAULT_MODIFIED_ON);

        // Get all the admissionDesignationList where modifiedOn is greater than SMALLER_MODIFIED_ON
        defaultAdmissionDesignationShouldBeFound("modifiedOn.greaterThan=" + SMALLER_MODIFIED_ON);
    }


    @Test
    @Transactional
    public void getAllAdmissionDesignationsByModifiedByIsEqualToSomething() throws Exception {
        // Initialize the database
        admissionDesignationRepository.saveAndFlush(admissionDesignation);

        // Get all the admissionDesignationList where modifiedBy equals to DEFAULT_MODIFIED_BY
        defaultAdmissionDesignationShouldBeFound("modifiedBy.equals=" + DEFAULT_MODIFIED_BY);

        // Get all the admissionDesignationList where modifiedBy equals to UPDATED_MODIFIED_BY
        defaultAdmissionDesignationShouldNotBeFound("modifiedBy.equals=" + UPDATED_MODIFIED_BY);
    }

    @Test
    @Transactional
    public void getAllAdmissionDesignationsByModifiedByIsNotEqualToSomething() throws Exception {
        // Initialize the database
        admissionDesignationRepository.saveAndFlush(admissionDesignation);

        // Get all the admissionDesignationList where modifiedBy not equals to DEFAULT_MODIFIED_BY
        defaultAdmissionDesignationShouldNotBeFound("modifiedBy.notEquals=" + DEFAULT_MODIFIED_BY);

        // Get all the admissionDesignationList where modifiedBy not equals to UPDATED_MODIFIED_BY
        defaultAdmissionDesignationShouldBeFound("modifiedBy.notEquals=" + UPDATED_MODIFIED_BY);
    }

    @Test
    @Transactional
    public void getAllAdmissionDesignationsByModifiedByIsInShouldWork() throws Exception {
        // Initialize the database
        admissionDesignationRepository.saveAndFlush(admissionDesignation);

        // Get all the admissionDesignationList where modifiedBy in DEFAULT_MODIFIED_BY or UPDATED_MODIFIED_BY
        defaultAdmissionDesignationShouldBeFound("modifiedBy.in=" + DEFAULT_MODIFIED_BY + "," + UPDATED_MODIFIED_BY);

        // Get all the admissionDesignationList where modifiedBy equals to UPDATED_MODIFIED_BY
        defaultAdmissionDesignationShouldNotBeFound("modifiedBy.in=" + UPDATED_MODIFIED_BY);
    }

    @Test
    @Transactional
    public void getAllAdmissionDesignationsByModifiedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        admissionDesignationRepository.saveAndFlush(admissionDesignation);

        // Get all the admissionDesignationList where modifiedBy is not null
        defaultAdmissionDesignationShouldBeFound("modifiedBy.specified=true");

        // Get all the admissionDesignationList where modifiedBy is null
        defaultAdmissionDesignationShouldNotBeFound("modifiedBy.specified=false");
    }
                @Test
    @Transactional
    public void getAllAdmissionDesignationsByModifiedByContainsSomething() throws Exception {
        // Initialize the database
        admissionDesignationRepository.saveAndFlush(admissionDesignation);

        // Get all the admissionDesignationList where modifiedBy contains DEFAULT_MODIFIED_BY
        defaultAdmissionDesignationShouldBeFound("modifiedBy.contains=" + DEFAULT_MODIFIED_BY);

        // Get all the admissionDesignationList where modifiedBy contains UPDATED_MODIFIED_BY
        defaultAdmissionDesignationShouldNotBeFound("modifiedBy.contains=" + UPDATED_MODIFIED_BY);
    }

    @Test
    @Transactional
    public void getAllAdmissionDesignationsByModifiedByNotContainsSomething() throws Exception {
        // Initialize the database
        admissionDesignationRepository.saveAndFlush(admissionDesignation);

        // Get all the admissionDesignationList where modifiedBy does not contain DEFAULT_MODIFIED_BY
        defaultAdmissionDesignationShouldNotBeFound("modifiedBy.doesNotContain=" + DEFAULT_MODIFIED_BY);

        // Get all the admissionDesignationList where modifiedBy does not contain UPDATED_MODIFIED_BY
        defaultAdmissionDesignationShouldBeFound("modifiedBy.doesNotContain=" + UPDATED_MODIFIED_BY);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAdmissionDesignationShouldBeFound(String filter) throws Exception {
        restAdmissionDesignationMockMvc.perform(get("/api/admission-designations?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(admissionDesignation.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].createdOn").value(hasItem(DEFAULT_CREATED_ON.toString())))
            .andExpect(jsonPath("$.[*].modifiedOn").value(hasItem(DEFAULT_MODIFIED_ON.toString())))
            .andExpect(jsonPath("$.[*].modifiedBy").value(hasItem(DEFAULT_MODIFIED_BY)));

        // Check, that the count call also returns 1
        restAdmissionDesignationMockMvc.perform(get("/api/admission-designations/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAdmissionDesignationShouldNotBeFound(String filter) throws Exception {
        restAdmissionDesignationMockMvc.perform(get("/api/admission-designations?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAdmissionDesignationMockMvc.perform(get("/api/admission-designations/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingAdmissionDesignation() throws Exception {
        // Get the admissionDesignation
        restAdmissionDesignationMockMvc.perform(get("/api/admission-designations/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAdmissionDesignation() throws Exception {
        // Initialize the database
        admissionDesignationRepository.saveAndFlush(admissionDesignation);

        int databaseSizeBeforeUpdate = admissionDesignationRepository.findAll().size();

        // Update the admissionDesignation
        AdmissionDesignation updatedAdmissionDesignation = admissionDesignationRepository.findById(admissionDesignation.getId()).get();
        // Disconnect from session so that the updates on updatedAdmissionDesignation are not directly saved in db
        em.detach(updatedAdmissionDesignation);
        updatedAdmissionDesignation
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .createdOn(UPDATED_CREATED_ON)
            .modifiedOn(UPDATED_MODIFIED_ON)
            .modifiedBy(UPDATED_MODIFIED_BY);
        AdmissionDesignationDTO admissionDesignationDTO = admissionDesignationMapper.toDto(updatedAdmissionDesignation);

        restAdmissionDesignationMockMvc.perform(put("/api/admission-designations")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(admissionDesignationDTO)))
            .andExpect(status().isOk());

        // Validate the AdmissionDesignation in the database
        List<AdmissionDesignation> admissionDesignationList = admissionDesignationRepository.findAll();
        assertThat(admissionDesignationList).hasSize(databaseSizeBeforeUpdate);
        AdmissionDesignation testAdmissionDesignation = admissionDesignationList.get(admissionDesignationList.size() - 1);
        assertThat(testAdmissionDesignation.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testAdmissionDesignation.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testAdmissionDesignation.getCreatedOn()).isEqualTo(UPDATED_CREATED_ON);
        assertThat(testAdmissionDesignation.getModifiedOn()).isEqualTo(UPDATED_MODIFIED_ON);
        assertThat(testAdmissionDesignation.getModifiedBy()).isEqualTo(UPDATED_MODIFIED_BY);
    }

    @Test
    @Transactional
    public void updateNonExistingAdmissionDesignation() throws Exception {
        int databaseSizeBeforeUpdate = admissionDesignationRepository.findAll().size();

        // Create the AdmissionDesignation
        AdmissionDesignationDTO admissionDesignationDTO = admissionDesignationMapper.toDto(admissionDesignation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAdmissionDesignationMockMvc.perform(put("/api/admission-designations")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(admissionDesignationDTO)))
            .andExpect(status().isBadRequest());

        // Validate the AdmissionDesignation in the database
        List<AdmissionDesignation> admissionDesignationList = admissionDesignationRepository.findAll();
        assertThat(admissionDesignationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteAdmissionDesignation() throws Exception {
        // Initialize the database
        admissionDesignationRepository.saveAndFlush(admissionDesignation);

        int databaseSizeBeforeDelete = admissionDesignationRepository.findAll().size();

        // Delete the admissionDesignation
        restAdmissionDesignationMockMvc.perform(delete("/api/admission-designations/{id}", admissionDesignation.getId())
            .accept(TestUtil.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AdmissionDesignation> admissionDesignationList = admissionDesignationRepository.findAll();
        assertThat(admissionDesignationList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
