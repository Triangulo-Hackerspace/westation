package br.net.triangulohackerspace.westation.web.rest;

import br.net.triangulohackerspace.westation.WestationApp;

import br.net.triangulohackerspace.westation.domain.Sensor;
import br.net.triangulohackerspace.westation.repository.SensorRepository;
import br.net.triangulohackerspace.westation.service.SensorService;
import br.net.triangulohackerspace.westation.web.rest.errors.ExceptionTranslator;
import br.net.triangulohackerspace.westation.service.SensorQueryService;

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
import java.util.List;

import static br.net.triangulohackerspace.westation.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the SensorResource REST controller.
 *
 * @see SensorResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = WestationApp.class)
public class SensorResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_TOPIC = "AAAAAAAAAA";
    private static final String UPDATED_TOPIC = "BBBBBBBBBB";

    private static final Integer DEFAULT_THRESHOLD = 1;
    private static final Integer UPDATED_THRESHOLD = 2;

    @Autowired
    private SensorRepository sensorRepository;

    @Autowired
    private SensorService sensorService;

    @Autowired
    private SensorQueryService sensorQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restSensorMockMvc;

    private Sensor sensor;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SensorResource sensorResource = new SensorResource(sensorService, sensorQueryService);
        this.restSensorMockMvc = MockMvcBuilders.standaloneSetup(sensorResource)
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
    public static Sensor createEntity(EntityManager em) {
        Sensor sensor = new Sensor()
            .name(DEFAULT_NAME)
            .topic(DEFAULT_TOPIC)
            .threshold(DEFAULT_THRESHOLD);
        return sensor;
    }

    @Before
    public void initTest() {
        sensor = createEntity(em);
    }

    @Test
    @Transactional
    public void createSensor() throws Exception {
        int databaseSizeBeforeCreate = sensorRepository.findAll().size();

        // Create the Sensor
        restSensorMockMvc.perform(post("/api/sensors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sensor)))
            .andExpect(status().isCreated());

        // Validate the Sensor in the database
        List<Sensor> sensorList = sensorRepository.findAll();
        assertThat(sensorList).hasSize(databaseSizeBeforeCreate + 1);
        Sensor testSensor = sensorList.get(sensorList.size() - 1);
        assertThat(testSensor.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testSensor.getTopic()).isEqualTo(DEFAULT_TOPIC);
        assertThat(testSensor.getThreshold()).isEqualTo(DEFAULT_THRESHOLD);
    }

    @Test
    @Transactional
    public void createSensorWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = sensorRepository.findAll().size();

        // Create the Sensor with an existing ID
        sensor.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSensorMockMvc.perform(post("/api/sensors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sensor)))
            .andExpect(status().isBadRequest());

        // Validate the Sensor in the database
        List<Sensor> sensorList = sensorRepository.findAll();
        assertThat(sensorList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = sensorRepository.findAll().size();
        // set the field null
        sensor.setName(null);

        // Create the Sensor, which fails.

        restSensorMockMvc.perform(post("/api/sensors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sensor)))
            .andExpect(status().isBadRequest());

        List<Sensor> sensorList = sensorRepository.findAll();
        assertThat(sensorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTopicIsRequired() throws Exception {
        int databaseSizeBeforeTest = sensorRepository.findAll().size();
        // set the field null
        sensor.setTopic(null);

        // Create the Sensor, which fails.

        restSensorMockMvc.perform(post("/api/sensors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sensor)))
            .andExpect(status().isBadRequest());

        List<Sensor> sensorList = sensorRepository.findAll();
        assertThat(sensorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSensors() throws Exception {
        // Initialize the database
        sensorRepository.saveAndFlush(sensor);

        // Get all the sensorList
        restSensorMockMvc.perform(get("/api/sensors?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sensor.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].topic").value(hasItem(DEFAULT_TOPIC.toString())))
            .andExpect(jsonPath("$.[*].threshold").value(hasItem(DEFAULT_THRESHOLD)));
    }

    @Test
    @Transactional
    public void getSensor() throws Exception {
        // Initialize the database
        sensorRepository.saveAndFlush(sensor);

        // Get the sensor
        restSensorMockMvc.perform(get("/api/sensors/{id}", sensor.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(sensor.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.topic").value(DEFAULT_TOPIC.toString()))
            .andExpect(jsonPath("$.threshold").value(DEFAULT_THRESHOLD));
    }

    @Test
    @Transactional
    public void getAllSensorsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        sensorRepository.saveAndFlush(sensor);

        // Get all the sensorList where name equals to DEFAULT_NAME
        defaultSensorShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the sensorList where name equals to UPDATED_NAME
        defaultSensorShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllSensorsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        sensorRepository.saveAndFlush(sensor);

        // Get all the sensorList where name in DEFAULT_NAME or UPDATED_NAME
        defaultSensorShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the sensorList where name equals to UPDATED_NAME
        defaultSensorShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllSensorsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        sensorRepository.saveAndFlush(sensor);

        // Get all the sensorList where name is not null
        defaultSensorShouldBeFound("name.specified=true");

        // Get all the sensorList where name is null
        defaultSensorShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    public void getAllSensorsByTopicIsEqualToSomething() throws Exception {
        // Initialize the database
        sensorRepository.saveAndFlush(sensor);

        // Get all the sensorList where topic equals to DEFAULT_TOPIC
        defaultSensorShouldBeFound("topic.equals=" + DEFAULT_TOPIC);

        // Get all the sensorList where topic equals to UPDATED_TOPIC
        defaultSensorShouldNotBeFound("topic.equals=" + UPDATED_TOPIC);
    }

    @Test
    @Transactional
    public void getAllSensorsByTopicIsInShouldWork() throws Exception {
        // Initialize the database
        sensorRepository.saveAndFlush(sensor);

        // Get all the sensorList where topic in DEFAULT_TOPIC or UPDATED_TOPIC
        defaultSensorShouldBeFound("topic.in=" + DEFAULT_TOPIC + "," + UPDATED_TOPIC);

        // Get all the sensorList where topic equals to UPDATED_TOPIC
        defaultSensorShouldNotBeFound("topic.in=" + UPDATED_TOPIC);
    }

    @Test
    @Transactional
    public void getAllSensorsByTopicIsNullOrNotNull() throws Exception {
        // Initialize the database
        sensorRepository.saveAndFlush(sensor);

        // Get all the sensorList where topic is not null
        defaultSensorShouldBeFound("topic.specified=true");

        // Get all the sensorList where topic is null
        defaultSensorShouldNotBeFound("topic.specified=false");
    }

    @Test
    @Transactional
    public void getAllSensorsByThresholdIsEqualToSomething() throws Exception {
        // Initialize the database
        sensorRepository.saveAndFlush(sensor);

        // Get all the sensorList where threshold equals to DEFAULT_THRESHOLD
        defaultSensorShouldBeFound("threshold.equals=" + DEFAULT_THRESHOLD);

        // Get all the sensorList where threshold equals to UPDATED_THRESHOLD
        defaultSensorShouldNotBeFound("threshold.equals=" + UPDATED_THRESHOLD);
    }

    @Test
    @Transactional
    public void getAllSensorsByThresholdIsInShouldWork() throws Exception {
        // Initialize the database
        sensorRepository.saveAndFlush(sensor);

        // Get all the sensorList where threshold in DEFAULT_THRESHOLD or UPDATED_THRESHOLD
        defaultSensorShouldBeFound("threshold.in=" + DEFAULT_THRESHOLD + "," + UPDATED_THRESHOLD);

        // Get all the sensorList where threshold equals to UPDATED_THRESHOLD
        defaultSensorShouldNotBeFound("threshold.in=" + UPDATED_THRESHOLD);
    }

    @Test
    @Transactional
    public void getAllSensorsByThresholdIsNullOrNotNull() throws Exception {
        // Initialize the database
        sensorRepository.saveAndFlush(sensor);

        // Get all the sensorList where threshold is not null
        defaultSensorShouldBeFound("threshold.specified=true");

        // Get all the sensorList where threshold is null
        defaultSensorShouldNotBeFound("threshold.specified=false");
    }

    @Test
    @Transactional
    public void getAllSensorsByThresholdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        sensorRepository.saveAndFlush(sensor);

        // Get all the sensorList where threshold greater than or equals to DEFAULT_THRESHOLD
        defaultSensorShouldBeFound("threshold.greaterOrEqualThan=" + DEFAULT_THRESHOLD);

        // Get all the sensorList where threshold greater than or equals to (DEFAULT_THRESHOLD + 1)
        defaultSensorShouldNotBeFound("threshold.greaterOrEqualThan=" + (DEFAULT_THRESHOLD + 1));
    }

    @Test
    @Transactional
    public void getAllSensorsByThresholdIsLessThanSomething() throws Exception {
        // Initialize the database
        sensorRepository.saveAndFlush(sensor);

        // Get all the sensorList where threshold less than or equals to DEFAULT_THRESHOLD
        defaultSensorShouldNotBeFound("threshold.lessThan=" + DEFAULT_THRESHOLD);

        // Get all the sensorList where threshold less than or equals to (DEFAULT_THRESHOLD + 1)
        defaultSensorShouldBeFound("threshold.lessThan=" + (DEFAULT_THRESHOLD + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultSensorShouldBeFound(String filter) throws Exception {
        restSensorMockMvc.perform(get("/api/sensors?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sensor.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].topic").value(hasItem(DEFAULT_TOPIC.toString())))
            .andExpect(jsonPath("$.[*].threshold").value(hasItem(DEFAULT_THRESHOLD)));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultSensorShouldNotBeFound(String filter) throws Exception {
        restSensorMockMvc.perform(get("/api/sensors?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingSensor() throws Exception {
        // Get the sensor
        restSensorMockMvc.perform(get("/api/sensors/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSensor() throws Exception {
        // Initialize the database
        sensorService.save(sensor);

        int databaseSizeBeforeUpdate = sensorRepository.findAll().size();

        // Update the sensor
        Sensor updatedSensor = sensorRepository.findOne(sensor.getId());
        // Disconnect from session so that the updates on updatedSensor are not directly saved in db
        em.detach(updatedSensor);
        updatedSensor
            .name(UPDATED_NAME)
            .topic(UPDATED_TOPIC)
            .threshold(UPDATED_THRESHOLD);

        restSensorMockMvc.perform(put("/api/sensors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedSensor)))
            .andExpect(status().isOk());

        // Validate the Sensor in the database
        List<Sensor> sensorList = sensorRepository.findAll();
        assertThat(sensorList).hasSize(databaseSizeBeforeUpdate);
        Sensor testSensor = sensorList.get(sensorList.size() - 1);
        assertThat(testSensor.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSensor.getTopic()).isEqualTo(UPDATED_TOPIC);
        assertThat(testSensor.getThreshold()).isEqualTo(UPDATED_THRESHOLD);
    }

    @Test
    @Transactional
    public void updateNonExistingSensor() throws Exception {
        int databaseSizeBeforeUpdate = sensorRepository.findAll().size();

        // Create the Sensor

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restSensorMockMvc.perform(put("/api/sensors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sensor)))
            .andExpect(status().isCreated());

        // Validate the Sensor in the database
        List<Sensor> sensorList = sensorRepository.findAll();
        assertThat(sensorList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteSensor() throws Exception {
        // Initialize the database
        sensorService.save(sensor);

        int databaseSizeBeforeDelete = sensorRepository.findAll().size();

        // Get the sensor
        restSensorMockMvc.perform(delete("/api/sensors/{id}", sensor.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Sensor> sensorList = sensorRepository.findAll();
        assertThat(sensorList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Sensor.class);
        Sensor sensor1 = new Sensor();
        sensor1.setId(1L);
        Sensor sensor2 = new Sensor();
        sensor2.setId(sensor1.getId());
        assertThat(sensor1).isEqualTo(sensor2);
        sensor2.setId(2L);
        assertThat(sensor1).isNotEqualTo(sensor2);
        sensor1.setId(null);
        assertThat(sensor1).isNotEqualTo(sensor2);
    }
}
