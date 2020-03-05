package org.ums.web.rest;

import org.ums.UgAdmissionApp;
import org.ums.domain.TotalSeat;
import org.ums.domain.FaProgram;
import org.ums.repository.TotalSeatRepository;
import org.ums.service.TotalSeatService;
import org.ums.service.dto.TotalSeatDTO;
import org.ums.service.mapper.TotalSeatMapper;
import org.ums.web.rest.errors.ExceptionTranslator;
import org.ums.service.dto.TotalSeatCriteria;
import org.ums.service.TotalSeatQueryService;

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
 * Integration tests for the {@link TotalSeatResource} REST controller.
 */
@SpringBootTest(classes = UgAdmissionApp.class)
public class TotalSeatResourceIT {

    private static final Integer DEFAULT_SEAT = 1;
    private static final Integer UPDATED_SEAT = 2;
    private static final Integer SMALLER_SEAT = 1 - 1;

    private static final LocalDate DEFAULT_CREATED_ON = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATED_ON = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_CREATED_ON = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_MODIFIED_ON = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_MODIFIED_ON = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_MODIFIED_ON = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_MODIFIED_BY = "AAAAAAAAAA";
    private static final String UPDATED_MODIFIED_BY = "BBBBBBBBBB";

    @Autowired
    private TotalSeatRepository totalSeatRepository;

    @Autowired
    private TotalSeatMapper totalSeatMapper;

    @Autowired
    private TotalSeatService totalSeatService;

    @Autowired
    private TotalSeatQueryService totalSeatQueryService;

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

    private MockMvc restTotalSeatMockMvc;

    private TotalSeat totalSeat;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TotalSeatResource totalSeatResource = new TotalSeatResource(totalSeatService, totalSeatQueryService);
        this.restTotalSeatMockMvc = MockMvcBuilders.standaloneSetup(totalSeatResource)
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
    public static TotalSeat createEntity(EntityManager em) {
        TotalSeat totalSeat = new TotalSeat()
            .seat(DEFAULT_SEAT)
            .createdOn(DEFAULT_CREATED_ON)
            .modifiedOn(DEFAULT_MODIFIED_ON)
            .modifiedBy(DEFAULT_MODIFIED_BY);
        return totalSeat;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TotalSeat createUpdatedEntity(EntityManager em) {
        TotalSeat totalSeat = new TotalSeat()
            .seat(UPDATED_SEAT)
            .createdOn(UPDATED_CREATED_ON)
            .modifiedOn(UPDATED_MODIFIED_ON)
            .modifiedBy(UPDATED_MODIFIED_BY);
        return totalSeat;
    }

    @BeforeEach
    public void initTest() {
        totalSeat = createEntity(em);
    }

    @Test
    @Transactional
    public void createTotalSeat() throws Exception {
        int databaseSizeBeforeCreate = totalSeatRepository.findAll().size();

        // Create the TotalSeat
        TotalSeatDTO totalSeatDTO = totalSeatMapper.toDto(totalSeat);
        restTotalSeatMockMvc.perform(post("/api/total-seats")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(totalSeatDTO)))
            .andExpect(status().isCreated());

        // Validate the TotalSeat in the database
        List<TotalSeat> totalSeatList = totalSeatRepository.findAll();
        assertThat(totalSeatList).hasSize(databaseSizeBeforeCreate + 1);
        TotalSeat testTotalSeat = totalSeatList.get(totalSeatList.size() - 1);
        assertThat(testTotalSeat.getSeat()).isEqualTo(DEFAULT_SEAT);
        assertThat(testTotalSeat.getCreatedOn()).isEqualTo(DEFAULT_CREATED_ON);
        assertThat(testTotalSeat.getModifiedOn()).isEqualTo(DEFAULT_MODIFIED_ON);
        assertThat(testTotalSeat.getModifiedBy()).isEqualTo(DEFAULT_MODIFIED_BY);
    }

    @Test
    @Transactional
    public void createTotalSeatWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = totalSeatRepository.findAll().size();

        // Create the TotalSeat with an existing ID
        totalSeat.setId(1L);
        TotalSeatDTO totalSeatDTO = totalSeatMapper.toDto(totalSeat);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTotalSeatMockMvc.perform(post("/api/total-seats")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(totalSeatDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TotalSeat in the database
        List<TotalSeat> totalSeatList = totalSeatRepository.findAll();
        assertThat(totalSeatList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkSeatIsRequired() throws Exception {
        int databaseSizeBeforeTest = totalSeatRepository.findAll().size();
        // set the field null
        totalSeat.setSeat(null);

        // Create the TotalSeat, which fails.
        TotalSeatDTO totalSeatDTO = totalSeatMapper.toDto(totalSeat);

        restTotalSeatMockMvc.perform(post("/api/total-seats")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(totalSeatDTO)))
            .andExpect(status().isBadRequest());

        List<TotalSeat> totalSeatList = totalSeatRepository.findAll();
        assertThat(totalSeatList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTotalSeats() throws Exception {
        // Initialize the database
        totalSeatRepository.saveAndFlush(totalSeat);

        // Get all the totalSeatList
        restTotalSeatMockMvc.perform(get("/api/total-seats?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(totalSeat.getId().intValue())))
            .andExpect(jsonPath("$.[*].seat").value(hasItem(DEFAULT_SEAT)))
            .andExpect(jsonPath("$.[*].createdOn").value(hasItem(DEFAULT_CREATED_ON.toString())))
            .andExpect(jsonPath("$.[*].modifiedOn").value(hasItem(DEFAULT_MODIFIED_ON.toString())))
            .andExpect(jsonPath("$.[*].modifiedBy").value(hasItem(DEFAULT_MODIFIED_BY)));
    }
    
    @Test
    @Transactional
    public void getTotalSeat() throws Exception {
        // Initialize the database
        totalSeatRepository.saveAndFlush(totalSeat);

        // Get the totalSeat
        restTotalSeatMockMvc.perform(get("/api/total-seats/{id}", totalSeat.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(totalSeat.getId().intValue()))
            .andExpect(jsonPath("$.seat").value(DEFAULT_SEAT))
            .andExpect(jsonPath("$.createdOn").value(DEFAULT_CREATED_ON.toString()))
            .andExpect(jsonPath("$.modifiedOn").value(DEFAULT_MODIFIED_ON.toString()))
            .andExpect(jsonPath("$.modifiedBy").value(DEFAULT_MODIFIED_BY));
    }


    @Test
    @Transactional
    public void getTotalSeatsByIdFiltering() throws Exception {
        // Initialize the database
        totalSeatRepository.saveAndFlush(totalSeat);

        Long id = totalSeat.getId();

        defaultTotalSeatShouldBeFound("id.equals=" + id);
        defaultTotalSeatShouldNotBeFound("id.notEquals=" + id);

        defaultTotalSeatShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTotalSeatShouldNotBeFound("id.greaterThan=" + id);

        defaultTotalSeatShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTotalSeatShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllTotalSeatsBySeatIsEqualToSomething() throws Exception {
        // Initialize the database
        totalSeatRepository.saveAndFlush(totalSeat);

        // Get all the totalSeatList where seat equals to DEFAULT_SEAT
        defaultTotalSeatShouldBeFound("seat.equals=" + DEFAULT_SEAT);

        // Get all the totalSeatList where seat equals to UPDATED_SEAT
        defaultTotalSeatShouldNotBeFound("seat.equals=" + UPDATED_SEAT);
    }

    @Test
    @Transactional
    public void getAllTotalSeatsBySeatIsNotEqualToSomething() throws Exception {
        // Initialize the database
        totalSeatRepository.saveAndFlush(totalSeat);

        // Get all the totalSeatList where seat not equals to DEFAULT_SEAT
        defaultTotalSeatShouldNotBeFound("seat.notEquals=" + DEFAULT_SEAT);

        // Get all the totalSeatList where seat not equals to UPDATED_SEAT
        defaultTotalSeatShouldBeFound("seat.notEquals=" + UPDATED_SEAT);
    }

    @Test
    @Transactional
    public void getAllTotalSeatsBySeatIsInShouldWork() throws Exception {
        // Initialize the database
        totalSeatRepository.saveAndFlush(totalSeat);

        // Get all the totalSeatList where seat in DEFAULT_SEAT or UPDATED_SEAT
        defaultTotalSeatShouldBeFound("seat.in=" + DEFAULT_SEAT + "," + UPDATED_SEAT);

        // Get all the totalSeatList where seat equals to UPDATED_SEAT
        defaultTotalSeatShouldNotBeFound("seat.in=" + UPDATED_SEAT);
    }

    @Test
    @Transactional
    public void getAllTotalSeatsBySeatIsNullOrNotNull() throws Exception {
        // Initialize the database
        totalSeatRepository.saveAndFlush(totalSeat);

        // Get all the totalSeatList where seat is not null
        defaultTotalSeatShouldBeFound("seat.specified=true");

        // Get all the totalSeatList where seat is null
        defaultTotalSeatShouldNotBeFound("seat.specified=false");
    }

    @Test
    @Transactional
    public void getAllTotalSeatsBySeatIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        totalSeatRepository.saveAndFlush(totalSeat);

        // Get all the totalSeatList where seat is greater than or equal to DEFAULT_SEAT
        defaultTotalSeatShouldBeFound("seat.greaterThanOrEqual=" + DEFAULT_SEAT);

        // Get all the totalSeatList where seat is greater than or equal to UPDATED_SEAT
        defaultTotalSeatShouldNotBeFound("seat.greaterThanOrEqual=" + UPDATED_SEAT);
    }

    @Test
    @Transactional
    public void getAllTotalSeatsBySeatIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        totalSeatRepository.saveAndFlush(totalSeat);

        // Get all the totalSeatList where seat is less than or equal to DEFAULT_SEAT
        defaultTotalSeatShouldBeFound("seat.lessThanOrEqual=" + DEFAULT_SEAT);

        // Get all the totalSeatList where seat is less than or equal to SMALLER_SEAT
        defaultTotalSeatShouldNotBeFound("seat.lessThanOrEqual=" + SMALLER_SEAT);
    }

    @Test
    @Transactional
    public void getAllTotalSeatsBySeatIsLessThanSomething() throws Exception {
        // Initialize the database
        totalSeatRepository.saveAndFlush(totalSeat);

        // Get all the totalSeatList where seat is less than DEFAULT_SEAT
        defaultTotalSeatShouldNotBeFound("seat.lessThan=" + DEFAULT_SEAT);

        // Get all the totalSeatList where seat is less than UPDATED_SEAT
        defaultTotalSeatShouldBeFound("seat.lessThan=" + UPDATED_SEAT);
    }

    @Test
    @Transactional
    public void getAllTotalSeatsBySeatIsGreaterThanSomething() throws Exception {
        // Initialize the database
        totalSeatRepository.saveAndFlush(totalSeat);

        // Get all the totalSeatList where seat is greater than DEFAULT_SEAT
        defaultTotalSeatShouldNotBeFound("seat.greaterThan=" + DEFAULT_SEAT);

        // Get all the totalSeatList where seat is greater than SMALLER_SEAT
        defaultTotalSeatShouldBeFound("seat.greaterThan=" + SMALLER_SEAT);
    }


    @Test
    @Transactional
    public void getAllTotalSeatsByCreatedOnIsEqualToSomething() throws Exception {
        // Initialize the database
        totalSeatRepository.saveAndFlush(totalSeat);

        // Get all the totalSeatList where createdOn equals to DEFAULT_CREATED_ON
        defaultTotalSeatShouldBeFound("createdOn.equals=" + DEFAULT_CREATED_ON);

        // Get all the totalSeatList where createdOn equals to UPDATED_CREATED_ON
        defaultTotalSeatShouldNotBeFound("createdOn.equals=" + UPDATED_CREATED_ON);
    }

    @Test
    @Transactional
    public void getAllTotalSeatsByCreatedOnIsNotEqualToSomething() throws Exception {
        // Initialize the database
        totalSeatRepository.saveAndFlush(totalSeat);

        // Get all the totalSeatList where createdOn not equals to DEFAULT_CREATED_ON
        defaultTotalSeatShouldNotBeFound("createdOn.notEquals=" + DEFAULT_CREATED_ON);

        // Get all the totalSeatList where createdOn not equals to UPDATED_CREATED_ON
        defaultTotalSeatShouldBeFound("createdOn.notEquals=" + UPDATED_CREATED_ON);
    }

    @Test
    @Transactional
    public void getAllTotalSeatsByCreatedOnIsInShouldWork() throws Exception {
        // Initialize the database
        totalSeatRepository.saveAndFlush(totalSeat);

        // Get all the totalSeatList where createdOn in DEFAULT_CREATED_ON or UPDATED_CREATED_ON
        defaultTotalSeatShouldBeFound("createdOn.in=" + DEFAULT_CREATED_ON + "," + UPDATED_CREATED_ON);

        // Get all the totalSeatList where createdOn equals to UPDATED_CREATED_ON
        defaultTotalSeatShouldNotBeFound("createdOn.in=" + UPDATED_CREATED_ON);
    }

    @Test
    @Transactional
    public void getAllTotalSeatsByCreatedOnIsNullOrNotNull() throws Exception {
        // Initialize the database
        totalSeatRepository.saveAndFlush(totalSeat);

        // Get all the totalSeatList where createdOn is not null
        defaultTotalSeatShouldBeFound("createdOn.specified=true");

        // Get all the totalSeatList where createdOn is null
        defaultTotalSeatShouldNotBeFound("createdOn.specified=false");
    }

    @Test
    @Transactional
    public void getAllTotalSeatsByCreatedOnIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        totalSeatRepository.saveAndFlush(totalSeat);

        // Get all the totalSeatList where createdOn is greater than or equal to DEFAULT_CREATED_ON
        defaultTotalSeatShouldBeFound("createdOn.greaterThanOrEqual=" + DEFAULT_CREATED_ON);

        // Get all the totalSeatList where createdOn is greater than or equal to UPDATED_CREATED_ON
        defaultTotalSeatShouldNotBeFound("createdOn.greaterThanOrEqual=" + UPDATED_CREATED_ON);
    }

    @Test
    @Transactional
    public void getAllTotalSeatsByCreatedOnIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        totalSeatRepository.saveAndFlush(totalSeat);

        // Get all the totalSeatList where createdOn is less than or equal to DEFAULT_CREATED_ON
        defaultTotalSeatShouldBeFound("createdOn.lessThanOrEqual=" + DEFAULT_CREATED_ON);

        // Get all the totalSeatList where createdOn is less than or equal to SMALLER_CREATED_ON
        defaultTotalSeatShouldNotBeFound("createdOn.lessThanOrEqual=" + SMALLER_CREATED_ON);
    }

    @Test
    @Transactional
    public void getAllTotalSeatsByCreatedOnIsLessThanSomething() throws Exception {
        // Initialize the database
        totalSeatRepository.saveAndFlush(totalSeat);

        // Get all the totalSeatList where createdOn is less than DEFAULT_CREATED_ON
        defaultTotalSeatShouldNotBeFound("createdOn.lessThan=" + DEFAULT_CREATED_ON);

        // Get all the totalSeatList where createdOn is less than UPDATED_CREATED_ON
        defaultTotalSeatShouldBeFound("createdOn.lessThan=" + UPDATED_CREATED_ON);
    }

    @Test
    @Transactional
    public void getAllTotalSeatsByCreatedOnIsGreaterThanSomething() throws Exception {
        // Initialize the database
        totalSeatRepository.saveAndFlush(totalSeat);

        // Get all the totalSeatList where createdOn is greater than DEFAULT_CREATED_ON
        defaultTotalSeatShouldNotBeFound("createdOn.greaterThan=" + DEFAULT_CREATED_ON);

        // Get all the totalSeatList where createdOn is greater than SMALLER_CREATED_ON
        defaultTotalSeatShouldBeFound("createdOn.greaterThan=" + SMALLER_CREATED_ON);
    }


    @Test
    @Transactional
    public void getAllTotalSeatsByModifiedOnIsEqualToSomething() throws Exception {
        // Initialize the database
        totalSeatRepository.saveAndFlush(totalSeat);

        // Get all the totalSeatList where modifiedOn equals to DEFAULT_MODIFIED_ON
        defaultTotalSeatShouldBeFound("modifiedOn.equals=" + DEFAULT_MODIFIED_ON);

        // Get all the totalSeatList where modifiedOn equals to UPDATED_MODIFIED_ON
        defaultTotalSeatShouldNotBeFound("modifiedOn.equals=" + UPDATED_MODIFIED_ON);
    }

    @Test
    @Transactional
    public void getAllTotalSeatsByModifiedOnIsNotEqualToSomething() throws Exception {
        // Initialize the database
        totalSeatRepository.saveAndFlush(totalSeat);

        // Get all the totalSeatList where modifiedOn not equals to DEFAULT_MODIFIED_ON
        defaultTotalSeatShouldNotBeFound("modifiedOn.notEquals=" + DEFAULT_MODIFIED_ON);

        // Get all the totalSeatList where modifiedOn not equals to UPDATED_MODIFIED_ON
        defaultTotalSeatShouldBeFound("modifiedOn.notEquals=" + UPDATED_MODIFIED_ON);
    }

    @Test
    @Transactional
    public void getAllTotalSeatsByModifiedOnIsInShouldWork() throws Exception {
        // Initialize the database
        totalSeatRepository.saveAndFlush(totalSeat);

        // Get all the totalSeatList where modifiedOn in DEFAULT_MODIFIED_ON or UPDATED_MODIFIED_ON
        defaultTotalSeatShouldBeFound("modifiedOn.in=" + DEFAULT_MODIFIED_ON + "," + UPDATED_MODIFIED_ON);

        // Get all the totalSeatList where modifiedOn equals to UPDATED_MODIFIED_ON
        defaultTotalSeatShouldNotBeFound("modifiedOn.in=" + UPDATED_MODIFIED_ON);
    }

    @Test
    @Transactional
    public void getAllTotalSeatsByModifiedOnIsNullOrNotNull() throws Exception {
        // Initialize the database
        totalSeatRepository.saveAndFlush(totalSeat);

        // Get all the totalSeatList where modifiedOn is not null
        defaultTotalSeatShouldBeFound("modifiedOn.specified=true");

        // Get all the totalSeatList where modifiedOn is null
        defaultTotalSeatShouldNotBeFound("modifiedOn.specified=false");
    }

    @Test
    @Transactional
    public void getAllTotalSeatsByModifiedOnIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        totalSeatRepository.saveAndFlush(totalSeat);

        // Get all the totalSeatList where modifiedOn is greater than or equal to DEFAULT_MODIFIED_ON
        defaultTotalSeatShouldBeFound("modifiedOn.greaterThanOrEqual=" + DEFAULT_MODIFIED_ON);

        // Get all the totalSeatList where modifiedOn is greater than or equal to UPDATED_MODIFIED_ON
        defaultTotalSeatShouldNotBeFound("modifiedOn.greaterThanOrEqual=" + UPDATED_MODIFIED_ON);
    }

    @Test
    @Transactional
    public void getAllTotalSeatsByModifiedOnIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        totalSeatRepository.saveAndFlush(totalSeat);

        // Get all the totalSeatList where modifiedOn is less than or equal to DEFAULT_MODIFIED_ON
        defaultTotalSeatShouldBeFound("modifiedOn.lessThanOrEqual=" + DEFAULT_MODIFIED_ON);

        // Get all the totalSeatList where modifiedOn is less than or equal to SMALLER_MODIFIED_ON
        defaultTotalSeatShouldNotBeFound("modifiedOn.lessThanOrEqual=" + SMALLER_MODIFIED_ON);
    }

    @Test
    @Transactional
    public void getAllTotalSeatsByModifiedOnIsLessThanSomething() throws Exception {
        // Initialize the database
        totalSeatRepository.saveAndFlush(totalSeat);

        // Get all the totalSeatList where modifiedOn is less than DEFAULT_MODIFIED_ON
        defaultTotalSeatShouldNotBeFound("modifiedOn.lessThan=" + DEFAULT_MODIFIED_ON);

        // Get all the totalSeatList where modifiedOn is less than UPDATED_MODIFIED_ON
        defaultTotalSeatShouldBeFound("modifiedOn.lessThan=" + UPDATED_MODIFIED_ON);
    }

    @Test
    @Transactional
    public void getAllTotalSeatsByModifiedOnIsGreaterThanSomething() throws Exception {
        // Initialize the database
        totalSeatRepository.saveAndFlush(totalSeat);

        // Get all the totalSeatList where modifiedOn is greater than DEFAULT_MODIFIED_ON
        defaultTotalSeatShouldNotBeFound("modifiedOn.greaterThan=" + DEFAULT_MODIFIED_ON);

        // Get all the totalSeatList where modifiedOn is greater than SMALLER_MODIFIED_ON
        defaultTotalSeatShouldBeFound("modifiedOn.greaterThan=" + SMALLER_MODIFIED_ON);
    }


    @Test
    @Transactional
    public void getAllTotalSeatsByModifiedByIsEqualToSomething() throws Exception {
        // Initialize the database
        totalSeatRepository.saveAndFlush(totalSeat);

        // Get all the totalSeatList where modifiedBy equals to DEFAULT_MODIFIED_BY
        defaultTotalSeatShouldBeFound("modifiedBy.equals=" + DEFAULT_MODIFIED_BY);

        // Get all the totalSeatList where modifiedBy equals to UPDATED_MODIFIED_BY
        defaultTotalSeatShouldNotBeFound("modifiedBy.equals=" + UPDATED_MODIFIED_BY);
    }

    @Test
    @Transactional
    public void getAllTotalSeatsByModifiedByIsNotEqualToSomething() throws Exception {
        // Initialize the database
        totalSeatRepository.saveAndFlush(totalSeat);

        // Get all the totalSeatList where modifiedBy not equals to DEFAULT_MODIFIED_BY
        defaultTotalSeatShouldNotBeFound("modifiedBy.notEquals=" + DEFAULT_MODIFIED_BY);

        // Get all the totalSeatList where modifiedBy not equals to UPDATED_MODIFIED_BY
        defaultTotalSeatShouldBeFound("modifiedBy.notEquals=" + UPDATED_MODIFIED_BY);
    }

    @Test
    @Transactional
    public void getAllTotalSeatsByModifiedByIsInShouldWork() throws Exception {
        // Initialize the database
        totalSeatRepository.saveAndFlush(totalSeat);

        // Get all the totalSeatList where modifiedBy in DEFAULT_MODIFIED_BY or UPDATED_MODIFIED_BY
        defaultTotalSeatShouldBeFound("modifiedBy.in=" + DEFAULT_MODIFIED_BY + "," + UPDATED_MODIFIED_BY);

        // Get all the totalSeatList where modifiedBy equals to UPDATED_MODIFIED_BY
        defaultTotalSeatShouldNotBeFound("modifiedBy.in=" + UPDATED_MODIFIED_BY);
    }

    @Test
    @Transactional
    public void getAllTotalSeatsByModifiedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        totalSeatRepository.saveAndFlush(totalSeat);

        // Get all the totalSeatList where modifiedBy is not null
        defaultTotalSeatShouldBeFound("modifiedBy.specified=true");

        // Get all the totalSeatList where modifiedBy is null
        defaultTotalSeatShouldNotBeFound("modifiedBy.specified=false");
    }
                @Test
    @Transactional
    public void getAllTotalSeatsByModifiedByContainsSomething() throws Exception {
        // Initialize the database
        totalSeatRepository.saveAndFlush(totalSeat);

        // Get all the totalSeatList where modifiedBy contains DEFAULT_MODIFIED_BY
        defaultTotalSeatShouldBeFound("modifiedBy.contains=" + DEFAULT_MODIFIED_BY);

        // Get all the totalSeatList where modifiedBy contains UPDATED_MODIFIED_BY
        defaultTotalSeatShouldNotBeFound("modifiedBy.contains=" + UPDATED_MODIFIED_BY);
    }

    @Test
    @Transactional
    public void getAllTotalSeatsByModifiedByNotContainsSomething() throws Exception {
        // Initialize the database
        totalSeatRepository.saveAndFlush(totalSeat);

        // Get all the totalSeatList where modifiedBy does not contain DEFAULT_MODIFIED_BY
        defaultTotalSeatShouldNotBeFound("modifiedBy.doesNotContain=" + DEFAULT_MODIFIED_BY);

        // Get all the totalSeatList where modifiedBy does not contain UPDATED_MODIFIED_BY
        defaultTotalSeatShouldBeFound("modifiedBy.doesNotContain=" + UPDATED_MODIFIED_BY);
    }


    @Test
    @Transactional
    public void getAllTotalSeatsByFacultyProgramIsEqualToSomething() throws Exception {
        // Initialize the database
        totalSeatRepository.saveAndFlush(totalSeat);
        FaProgram facultyProgram = FaProgramResourceIT.createEntity(em);
        em.persist(facultyProgram);
        em.flush();
        totalSeat.setFacultyProgram(facultyProgram);
        totalSeatRepository.saveAndFlush(totalSeat);
        Long facultyProgramId = facultyProgram.getId();

        // Get all the totalSeatList where facultyProgram equals to facultyProgramId
        defaultTotalSeatShouldBeFound("facultyProgramId.equals=" + facultyProgramId);

        // Get all the totalSeatList where facultyProgram equals to facultyProgramId + 1
        defaultTotalSeatShouldNotBeFound("facultyProgramId.equals=" + (facultyProgramId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTotalSeatShouldBeFound(String filter) throws Exception {
        restTotalSeatMockMvc.perform(get("/api/total-seats?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(totalSeat.getId().intValue())))
            .andExpect(jsonPath("$.[*].seat").value(hasItem(DEFAULT_SEAT)))
            .andExpect(jsonPath("$.[*].createdOn").value(hasItem(DEFAULT_CREATED_ON.toString())))
            .andExpect(jsonPath("$.[*].modifiedOn").value(hasItem(DEFAULT_MODIFIED_ON.toString())))
            .andExpect(jsonPath("$.[*].modifiedBy").value(hasItem(DEFAULT_MODIFIED_BY)));

        // Check, that the count call also returns 1
        restTotalSeatMockMvc.perform(get("/api/total-seats/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTotalSeatShouldNotBeFound(String filter) throws Exception {
        restTotalSeatMockMvc.perform(get("/api/total-seats?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTotalSeatMockMvc.perform(get("/api/total-seats/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingTotalSeat() throws Exception {
        // Get the totalSeat
        restTotalSeatMockMvc.perform(get("/api/total-seats/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTotalSeat() throws Exception {
        // Initialize the database
        totalSeatRepository.saveAndFlush(totalSeat);

        int databaseSizeBeforeUpdate = totalSeatRepository.findAll().size();

        // Update the totalSeat
        TotalSeat updatedTotalSeat = totalSeatRepository.findById(totalSeat.getId()).get();
        // Disconnect from session so that the updates on updatedTotalSeat are not directly saved in db
        em.detach(updatedTotalSeat);
        updatedTotalSeat
            .seat(UPDATED_SEAT)
            .createdOn(UPDATED_CREATED_ON)
            .modifiedOn(UPDATED_MODIFIED_ON)
            .modifiedBy(UPDATED_MODIFIED_BY);
        TotalSeatDTO totalSeatDTO = totalSeatMapper.toDto(updatedTotalSeat);

        restTotalSeatMockMvc.perform(put("/api/total-seats")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(totalSeatDTO)))
            .andExpect(status().isOk());

        // Validate the TotalSeat in the database
        List<TotalSeat> totalSeatList = totalSeatRepository.findAll();
        assertThat(totalSeatList).hasSize(databaseSizeBeforeUpdate);
        TotalSeat testTotalSeat = totalSeatList.get(totalSeatList.size() - 1);
        assertThat(testTotalSeat.getSeat()).isEqualTo(UPDATED_SEAT);
        assertThat(testTotalSeat.getCreatedOn()).isEqualTo(UPDATED_CREATED_ON);
        assertThat(testTotalSeat.getModifiedOn()).isEqualTo(UPDATED_MODIFIED_ON);
        assertThat(testTotalSeat.getModifiedBy()).isEqualTo(UPDATED_MODIFIED_BY);
    }

    @Test
    @Transactional
    public void updateNonExistingTotalSeat() throws Exception {
        int databaseSizeBeforeUpdate = totalSeatRepository.findAll().size();

        // Create the TotalSeat
        TotalSeatDTO totalSeatDTO = totalSeatMapper.toDto(totalSeat);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTotalSeatMockMvc.perform(put("/api/total-seats")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(totalSeatDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TotalSeat in the database
        List<TotalSeat> totalSeatList = totalSeatRepository.findAll();
        assertThat(totalSeatList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteTotalSeat() throws Exception {
        // Initialize the database
        totalSeatRepository.saveAndFlush(totalSeat);

        int databaseSizeBeforeDelete = totalSeatRepository.findAll().size();

        // Delete the totalSeat
        restTotalSeatMockMvc.perform(delete("/api/total-seats/{id}", totalSeat.getId())
            .accept(TestUtil.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TotalSeat> totalSeatList = totalSeatRepository.findAll();
        assertThat(totalSeatList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
