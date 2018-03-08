package br.net.triangulohackerspace.westation.web.rest;

import com.codahale.metrics.annotation.Timed;
import br.net.triangulohackerspace.westation.domain.Station;
import br.net.triangulohackerspace.westation.service.StationService;
import br.net.triangulohackerspace.westation.web.rest.errors.BadRequestAlertException;
import br.net.triangulohackerspace.westation.web.rest.util.HeaderUtil;
import br.net.triangulohackerspace.westation.web.rest.util.PaginationUtil;
import br.net.triangulohackerspace.westation.service.dto.StationCriteria;
import br.net.triangulohackerspace.westation.service.StationQueryService;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Station.
 */
@RestController
@RequestMapping("/api")
public class StationResource {

    private final Logger log = LoggerFactory.getLogger(StationResource.class);

    private static final String ENTITY_NAME = "station";

    private final StationService stationService;

    private final StationQueryService stationQueryService;

    public StationResource(StationService stationService, StationQueryService stationQueryService) {
        this.stationService = stationService;
        this.stationQueryService = stationQueryService;
    }

    /**
     * POST  /stations : Create a new station.
     *
     * @param station the station to create
     * @return the ResponseEntity with status 201 (Created) and with body the new station, or with status 400 (Bad Request) if the station has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/stations")
    @Timed
    public ResponseEntity<Station> createStation(@Valid @RequestBody Station station) throws URISyntaxException {
        log.debug("REST request to save Station : {}", station);
        if (station.getId() != null) {
            throw new BadRequestAlertException("A new station cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Station result = stationService.save(station);
        return ResponseEntity.created(new URI("/api/stations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /stations : Updates an existing station.
     *
     * @param station the station to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated station,
     * or with status 400 (Bad Request) if the station is not valid,
     * or with status 500 (Internal Server Error) if the station couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/stations")
    @Timed
    public ResponseEntity<Station> updateStation(@Valid @RequestBody Station station) throws URISyntaxException {
        log.debug("REST request to update Station : {}", station);
        if (station.getId() == null) {
            return createStation(station);
        }
        Station result = stationService.save(station);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, station.getId().toString()))
            .body(result);
    }

    /**
     * GET  /stations : get all the stations.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of stations in body
     */
    @GetMapping("/stations")
    @Timed
    public ResponseEntity<List<Station>> getAllStations(StationCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Stations by criteria: {}", criteria);
        Page<Station> page = stationQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/stations");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /stations/:id : get the "id" station.
     *
     * @param id the id of the station to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the station, or with status 404 (Not Found)
     */
    @GetMapping("/stations/{id}")
    @Timed
    public ResponseEntity<Station> getStation(@PathVariable Long id) {
        log.debug("REST request to get Station : {}", id);
        Station station = stationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(station));
    }

    /**
     * DELETE  /stations/:id : delete the "id" station.
     *
     * @param id the id of the station to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/stations/{id}")
    @Timed
    public ResponseEntity<Void> deleteStation(@PathVariable Long id) {
        log.debug("REST request to delete Station : {}", id);
        stationService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
