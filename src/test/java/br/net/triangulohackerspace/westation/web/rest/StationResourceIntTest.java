package br.net.triangulohackerspace.westation.web.rest;

import br.net.triangulohackerspace.westation.WestationApp;

import br.net.triangulohackerspace.westation.domain.Station;
import br.net.triangulohackerspace.westation.domain.Sensor;
import br.net.triangulohackerspace.westation.repository.StationRepository;
import br.net.triangulohackerspace.westation.service.StationService;
import br.net.triangulohackerspace.westation.web.rest.errors.ExceptionTranslator;
import br.net.triangulohackerspace.westation.service.StationQueryService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.List;

import static br.net.triangulohackerspace.westation.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the StationResource REST controller.
 *
 * @see StationResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = WestationApp.class)
public class StationResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_PORT = 0;
    private static final Integer UPDATED_PORT = 1;

    private static final String DEFAULT_SUFIX = "AAAAAAAAAA";
    private static final String UPDATED_SUFIX = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_LONGITUDE = new BigDecimal(1);
    private static final BigDecimal UPDATED_LONGITUDE = new BigDecimal(2);

    private static final BigDecimal DEFAULT_LATITUDE = new BigDecimal(1);
    private static final BigDecimal UPDATED_LATITUDE = new BigDecimal(2);

    private static final String DEFAULT_IP = "AAAAAAAAAA";
    private static final String UPDATED_IP = "BBBBBBBBBB";

    @Autowired
    private StationRepository stationRepository;

    @Autowired
    private StationService stationService;

    @Autowired
    private StationQueryService stationQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restStationMockMvc;

    private Station station;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final StationResource stationResource = new StationResource(stationService, stationQueryService);
        this.restStationMockMvc = MockMvcBuilders.standaloneSetup(stationResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Station createEntity(EntityManager em) {
        Station station = new Station()
            .name(DEFAULT_NAME)
            .port(DEFAULT_PORT)
            .sufix(DEFAULT_SUFIX)
            .longitude(DEFAULT_LONGITUDE)
            .latitude(DEFAULT_LATITUDE)
            .ip(DEFAULT_IP);
        // Add required entity
        Sensor sensor = SensorResourceIntTest.createEntity(em);
        em.persist(sensor);
        em.flush();
        station.getSensors().add(sensor);
        return station;
    }

    @Before
    public void initTest() {
        station = createEntity(em);
    }

    @Test
    @Transactional
    public void createStation() throws Exception {
        int databaseSizeBeforeCreate = stationRepository.findAll().size();

        // Create the Station
        restStationMockMvc.perform(post("/api/stations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(station)))
            .andExpect(status().isCreated());

        // Validate the Station in the database
        List<Station> stationList = stationRepository.findAll();
        assertThat(stationList).hasSize(databaseSizeBeforeCreate + 1);
        Station testStation = stationList.get(stationList.size() - 1);
        assertThat(testStation.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testStation.getPort()).isEqualTo(DEFAULT_PORT);
        assertThat(testStation.getSufix()).isEqualTo(DEFAULT_SUFIX);
        assertThat(testStation.getLongitude()).isEqualTo(DEFAULT_LONGITUDE);
        assertThat(testStation.getLatitude()).isEqualTo(DEFAULT_LATITUDE);
        assertThat(testStation.getIp()).isEqualTo(DEFAULT_IP);
    }

    @Test
    @Transactional
    public void createStationWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = stationRepository.findAll().size();

        // Create the Station with an existing ID
        station.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restStationMockMvc.perform(post("/api/stations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(station)))
            .andExpect(status().isBadRequest());

        // Validate the Station in the database
        List<Station> stationList = stationRepository.findAll();
        assertThat(stationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = stationRepository.findAll().size();
        // set the field null
        station.setName(null);

        // Create the Station, which fails.

        restStationMockMvc.perform(post("/api/stations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(station)))
            .andExpect(status().isBadRequest());

        List<Station> stationList = stationRepository.findAll();
        assertThat(stationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPortIsRequired() throws Exception {
        int databaseSizeBeforeTest = stationRepository.findAll().size();
        // set the field null
        station.setPort(null);

        // Create the Station, which fails.

        restStationMockMvc.perform(post("/api/stations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(station)))
            .andExpect(status().isBadRequest());

        List<Station> stationList = stationRepository.findAll();
        assertThat(stationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkIpIsRequired() throws Exception {
        int databaseSizeBeforeTest = stationRepository.findAll().size();
        // set the field null
        station.setIp(null);

        // Create the Station, which fails.

        restStationMockMvc.perform(post("/api/stations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(station)))
            .andExpect(status().isBadRequest());

        List<Station> stationList = stationRepository.findAll();
        assertThat(stationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllStations() throws Exception {
        // Initialize the database
        stationRepository.saveAndFlush(station);

        // Get all the stationList
        restStationMockMvc.perform(get("/api/stations?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(station.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].port").value(hasItem(DEFAULT_PORT)))
            .andExpect(jsonPath("$.[*].sufix").value(hasItem(DEFAULT_SUFIX.toString())))
            .andExpect(jsonPath("$.[*].longitude").value(hasItem(DEFAULT_LONGITUDE.intValue())))
            .andExpect(jsonPath("$.[*].latitude").value(hasItem(DEFAULT_LATITUDE.intValue())))
            .andExpect(jsonPath("$.[*].ip").value(hasItem(DEFAULT_IP.toString())));
    }

    @Test
    @Transactional
    public void getStation() throws Exception {
        // Initialize the database
        stationRepository.saveAndFlush(station);

        // Get the station
        restStationMockMvc.perform(get("/api/stations/{id}", station.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(station.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.port").value(DEFAULT_PORT))
            .andExpect(jsonPath("$.sufix").value(DEFAULT_SUFIX.toString()))
            .andExpect(jsonPath("$.longitude").value(DEFAULT_LONGITUDE.intValue()))
            .andExpect(jsonPath("$.latitude").value(DEFAULT_LATITUDE.intValue()))
            .andExpect(jsonPath("$.ip").value(DEFAULT_IP.toString()));
    }

    @Test
    @Transactional
    public void getAllStationsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        stationRepository.saveAndFlush(station);

        // Get all the stationList where name equals to DEFAULT_NAME
        defaultStationShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the stationList where name equals to UPDATED_NAME
        defaultStationShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllStationsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        stationRepository.saveAndFlush(station);

        // Get all the stationList where name in DEFAULT_NAME or UPDATED_NAME
        defaultStationShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the stationList where name equals to UPDATED_NAME
        defaultStationShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllStationsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        stationRepository.saveAndFlush(station);

        // Get all the stationList where name is not null
        defaultStationShouldBeFound("name.specified=true");

        // Get all the stationList where name is null
        defaultStationShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    public void getAllStationsByPortIsEqualToSomething() throws Exception {
        // Initialize the database
        stationRepository.saveAndFlush(station);

        // Get all the stationList where port equals to DEFAULT_PORT
        defaultStationShouldBeFound("port.equals=" + DEFAULT_PORT);

        // Get all the stationList where port equals to UPDATED_PORT
        defaultStationShouldNotBeFound("port.equals=" + UPDATED_PORT);
    }

    @Test
    @Transactional
    public void getAllStationsByPortIsInShouldWork() throws Exception {
        // Initialize the database
        stationRepository.saveAndFlush(station);

        // Get all the stationList where port in DEFAULT_PORT or UPDATED_PORT
        defaultStationShouldBeFound("port.in=" + DEFAULT_PORT + "," + UPDATED_PORT);

        // Get all the stationList where port equals to UPDATED_PORT
        defaultStationShouldNotBeFound("port.in=" + UPDATED_PORT);
    }

    @Test
    @Transactional
    public void getAllStationsByPortIsNullOrNotNull() throws Exception {
        // Initialize the database
        stationRepository.saveAndFlush(station);

        // Get all the stationList where port is not null
        defaultStationShouldBeFound("port.specified=true");

        // Get all the stationList where port is null
        defaultStationShouldNotBeFound("port.specified=false");
    }

    @Test
    @Transactional
    public void getAllStationsByPortIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        stationRepository.saveAndFlush(station);

        // Get all the stationList where port greater than or equals to DEFAULT_PORT
        defaultStationShouldBeFound("port.greaterOrEqualThan=" + DEFAULT_PORT);

        // Get all the stationList where port greater than or equals to (DEFAULT_PORT + 1)
        defaultStationShouldNotBeFound("port.greaterOrEqualThan=" + (DEFAULT_PORT + 1));
    }

    @Test
    @Transactional
    public void getAllStationsByPortIsLessThanSomething() throws Exception {
        // Initialize the database
        stationRepository.saveAndFlush(station);

        // Get all the stationList where port less than or equals to DEFAULT_PORT
        defaultStationShouldNotBeFound("port.lessThan=" + DEFAULT_PORT);

        // Get all the stationList where port less than or equals to (DEFAULT_PORT + 1)
        defaultStationShouldBeFound("port.lessThan=" + (DEFAULT_PORT + 1));
    }


    @Test
    @Transactional
    public void getAllStationsBySufixIsEqualToSomething() throws Exception {
        // Initialize the database
        stationRepository.saveAndFlush(station);

        // Get all the stationList where sufix equals to DEFAULT_SUFIX
        defaultStationShouldBeFound("sufix.equals=" + DEFAULT_SUFIX);

        // Get all the stationList where sufix equals to UPDATED_SUFIX
        defaultStationShouldNotBeFound("sufix.equals=" + UPDATED_SUFIX);
    }

    @Test
    @Transactional
    public void getAllStationsBySufixIsInShouldWork() throws Exception {
        // Initialize the database
        stationRepository.saveAndFlush(station);

        // Get all the stationList where sufix in DEFAULT_SUFIX or UPDATED_SUFIX
        defaultStationShouldBeFound("sufix.in=" + DEFAULT_SUFIX + "," + UPDATED_SUFIX);

        // Get all the stationList where sufix equals to UPDATED_SUFIX
        defaultStationShouldNotBeFound("sufix.in=" + UPDATED_SUFIX);
    }

    @Test
    @Transactional
    public void getAllStationsBySufixIsNullOrNotNull() throws Exception {
        // Initialize the database
        stationRepository.saveAndFlush(station);

        // Get all the stationList where sufix is not null
        defaultStationShouldBeFound("sufix.specified=true");

        // Get all the stationList where sufix is null
        defaultStationShouldNotBeFound("sufix.specified=false");
    }

    @Test
    @Transactional
    public void getAllStationsByLongitudeIsEqualToSomething() throws Exception {
        // Initialize the database
        stationRepository.saveAndFlush(station);

        // Get all the stationList where longitude equals to DEFAULT_LONGITUDE
        defaultStationShouldBeFound("longitude.equals=" + DEFAULT_LONGITUDE);

        // Get all the stationList where longitude equals to UPDATED_LONGITUDE
        defaultStationShouldNotBeFound("longitude.equals=" + UPDATED_LONGITUDE);
    }

    @Test
    @Transactional
    public void getAllStationsByLongitudeIsInShouldWork() throws Exception {
        // Initialize the database
        stationRepository.saveAndFlush(station);

        // Get all the stationList where longitude in DEFAULT_LONGITUDE or UPDATED_LONGITUDE
        defaultStationShouldBeFound("longitude.in=" + DEFAULT_LONGITUDE + "," + UPDATED_LONGITUDE);

        // Get all the stationList where longitude equals to UPDATED_LONGITUDE
        defaultStationShouldNotBeFound("longitude.in=" + UPDATED_LONGITUDE);
    }

    @Test
    @Transactional
    public void getAllStationsByLongitudeIsNullOrNotNull() throws Exception {
        // Initialize the database
        stationRepository.saveAndFlush(station);

        // Get all the stationList where longitude is not null
        defaultStationShouldBeFound("longitude.specified=true");

        // Get all the stationList where longitude is null
        defaultStationShouldNotBeFound("longitude.specified=false");
    }

    @Test
    @Transactional
    public void getAllStationsByLatitudeIsEqualToSomething() throws Exception {
        // Initialize the database
        stationRepository.saveAndFlush(station);

        // Get all the stationList where latitude equals to DEFAULT_LATITUDE
        defaultStationShouldBeFound("latitude.equals=" + DEFAULT_LATITUDE);

        // Get all the stationList where latitude equals to UPDATED_LATITUDE
        defaultStationShouldNotBeFound("latitude.equals=" + UPDATED_LATITUDE);
    }

    @Test
    @Transactional
    public void getAllStationsByLatitudeIsInShouldWork() throws Exception {
        // Initialize the database
        stationRepository.saveAndFlush(station);

        // Get all the stationList where latitude in DEFAULT_LATITUDE or UPDATED_LATITUDE
        defaultStationShouldBeFound("latitude.in=" + DEFAULT_LATITUDE + "," + UPDATED_LATITUDE);

        // Get all the stationList where latitude equals to UPDATED_LATITUDE
        defaultStationShouldNotBeFound("latitude.in=" + UPDATED_LATITUDE);
    }

    @Test
    @Transactional
    public void getAllStationsByLatitudeIsNullOrNotNull() throws Exception {
        // Initialize the database
        stationRepository.saveAndFlush(station);

        // Get all the stationList where latitude is not null
        defaultStationShouldBeFound("latitude.specified=true");

        // Get all the stationList where latitude is null
        defaultStationShouldNotBeFound("latitude.specified=false");
    }

    @Test
    @Transactional
    public void getAllStationsByIpIsEqualToSomething() throws Exception {
        // Initialize the database
        stationRepository.saveAndFlush(station);

        // Get all the stationList where ip equals to DEFAULT_IP
        defaultStationShouldBeFound("ip.equals=" + DEFAULT_IP);

        // Get all the stationList where ip equals to UPDATED_IP
        defaultStationShouldNotBeFound("ip.equals=" + UPDATED_IP);
    }

    @Test
    @Transactional
    public void getAllStationsByIpIsInShouldWork() throws Exception {
        // Initialize the database
        stationRepository.saveAndFlush(station);

        // Get all the stationList where ip in DEFAULT_IP or UPDATED_IP
        defaultStationShouldBeFound("ip.in=" + DEFAULT_IP + "," + UPDATED_IP);

        // Get all the stationList where ip equals to UPDATED_IP
        defaultStationShouldNotBeFound("ip.in=" + UPDATED_IP);
    }

    @Test
    @Transactional
    public void getAllStationsByIpIsNullOrNotNull() throws Exception {
        // Initialize the database
        stationRepository.saveAndFlush(station);

        // Get all the stationList where ip is not null
        defaultStationShouldBeFound("ip.specified=true");

        // Get all the stationList where ip is null
        defaultStationShouldNotBeFound("ip.specified=false");
    }

    @Test
    @Transactional
    public void getAllStationsBySensorIsEqualToSomething() throws Exception {
        // Initialize the database
        Sensor sensor = SensorResourceIntTest.createEntity(em);
        em.persist(sensor);
        em.flush();
        station.addSensor(sensor);
        stationRepository.saveAndFlush(station);
        Long sensorId = sensor.getId();

        // Get all the stationList where sensor equals to sensorId
        defaultStationShouldBeFound("sensorId.equals=" + sensorId);

        // Get all the stationList where sensor equals to sensorId + 1
        defaultStationShouldNotBeFound("sensorId.equals=" + (sensorId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultStationShouldBeFound(String filter) throws Exception {
        restStationMockMvc.perform(get("/api/stations?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(station.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].port").value(hasItem(DEFAULT_PORT)))
            .andExpect(jsonPath("$.[*].sufix").value(hasItem(DEFAULT_SUFIX.toString())))
            .andExpect(jsonPath("$.[*].longitude").value(hasItem(DEFAULT_LONGITUDE.intValue())))
            .andExpect(jsonPath("$.[*].latitude").value(hasItem(DEFAULT_LATITUDE.intValue())))
            .andExpect(jsonPath("$.[*].ip").value(hasItem(DEFAULT_IP.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultStationShouldNotBeFound(String filter) throws Exception {
        restStationMockMvc.perform(get("/api/stations?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingStation() throws Exception {
        // Get the station
        restStationMockMvc.perform(get("/api/stations/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateStation() throws Exception {
        // Initialize the database
        stationService.save(station);

        int databaseSizeBeforeUpdate = stationRepository.findAll().size();

        // Update the station
        Station updatedStation = stationRepository.findOne(station.getId());
        // Disconnect from session so that the updates on updatedStation are not directly saved in db
        em.detach(updatedStation);
        updatedStation
            .name(UPDATED_NAME)
            .port(UPDATED_PORT)
            .sufix(UPDATED_SUFIX)
            .longitude(UPDATED_LONGITUDE)
            .latitude(UPDATED_LATITUDE)
            .ip(UPDATED_IP);

        restStationMockMvc.perform(put("/api/stations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedStation)))
            .andExpect(status().isOk());

        // Validate the Station in the database
        List<Station> stationList = stationRepository.findAll();
        assertThat(stationList).hasSize(databaseSizeBeforeUpdate);
        Station testStation = stationList.get(stationList.size() - 1);
        assertThat(testStation.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testStation.getPort()).isEqualTo(UPDATED_PORT);
        assertThat(testStation.getSufix()).isEqualTo(UPDATED_SUFIX);
        assertThat(testStation.getLongitude()).isEqualTo(UPDATED_LONGITUDE);
        assertThat(testStation.getLatitude()).isEqualTo(UPDATED_LATITUDE);
        assertThat(testStation.getIp()).isEqualTo(UPDATED_IP);
    }

    @Test
    @Transactional
    public void updateNonExistingStation() throws Exception {
        int databaseSizeBeforeUpdate = stationRepository.findAll().size();

        // Create the Station

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restStationMockMvc.perform(put("/api/stations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(station)))
            .andExpect(status().isCreated());

        // Validate the Station in the database
        List<Station> stationList = stationRepository.findAll();
        assertThat(stationList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteStation() throws Exception {
        // Initialize the database
        stationService.save(station);

        int databaseSizeBeforeDelete = stationRepository.findAll().size();

        // Get the station
        restStationMockMvc.perform(delete("/api/stations/{id}", station.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Station> stationList = stationRepository.findAll();
        assertThat(stationList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Station.class);
        Station station1 = new Station();
        station1.setId(1L);
        Station station2 = new Station();
        station2.setId(station1.getId());
        assertThat(station1).isEqualTo(station2);
        station2.setId(2L);
        assertThat(station1).isNotEqualTo(station2);
        station1.setId(null);
        assertThat(station1).isNotEqualTo(station2);
    }
}
