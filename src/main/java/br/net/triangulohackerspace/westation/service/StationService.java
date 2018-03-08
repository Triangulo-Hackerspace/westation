package br.net.triangulohackerspace.westation.service;

import br.net.triangulohackerspace.westation.domain.Station;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing Station.
 */
public interface StationService {

    /**
     * Save a station.
     *
     * @param station the entity to save
     * @return the persisted entity
     */
    Station save(Station station);

    /**
     * Get all the stations.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<Station> findAll(Pageable pageable);

    /**
     * Get the "id" station.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Station findOne(Long id);

    /**
     * Delete the "id" station.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
