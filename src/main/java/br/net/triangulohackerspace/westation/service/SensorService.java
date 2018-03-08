package br.net.triangulohackerspace.westation.service;

import br.net.triangulohackerspace.westation.domain.Sensor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing Sensor.
 */
public interface SensorService {

    /**
     * Save a sensor.
     *
     * @param sensor the entity to save
     * @return the persisted entity
     */
    Sensor save(Sensor sensor);

    /**
     * Get all the sensors.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<Sensor> findAll(Pageable pageable);

    /**
     * Get the "id" sensor.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Sensor findOne(Long id);

    /**
     * Delete the "id" sensor.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
