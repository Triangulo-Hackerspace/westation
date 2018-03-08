package br.net.triangulohackerspace.westation.service.impl;

import br.net.triangulohackerspace.westation.service.SensorService;
import br.net.triangulohackerspace.westation.domain.Sensor;
import br.net.triangulohackerspace.westation.repository.SensorRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing Sensor.
 */
@Service
@Transactional
public class SensorServiceImpl implements SensorService {

    private final Logger log = LoggerFactory.getLogger(SensorServiceImpl.class);

    private final SensorRepository sensorRepository;

    public SensorServiceImpl(SensorRepository sensorRepository) {
        this.sensorRepository = sensorRepository;
    }

    /**
     * Save a sensor.
     *
     * @param sensor the entity to save
     * @return the persisted entity
     */
    @Override
    public Sensor save(Sensor sensor) {
        log.debug("Request to save Sensor : {}", sensor);
        return sensorRepository.save(sensor);
    }

    /**
     * Get all the sensors.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Sensor> findAll(Pageable pageable) {
        log.debug("Request to get all Sensors");
        return sensorRepository.findAll(pageable);
    }

    /**
     * Get one sensor by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Sensor findOne(Long id) {
        log.debug("Request to get Sensor : {}", id);
        return sensorRepository.findOne(id);
    }

    /**
     * Delete the sensor by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Sensor : {}", id);
        sensorRepository.delete(id);
    }
}
