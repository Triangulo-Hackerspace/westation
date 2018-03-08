package br.net.triangulohackerspace.westation.service;


import br.net.triangulohackerspace.westation.domain.Sensor_;
import br.net.triangulohackerspace.westation.domain.Station;
import br.net.triangulohackerspace.westation.domain.Station_;
import br.net.triangulohackerspace.westation.repository.StationRepository;
import br.net.triangulohackerspace.westation.service.dto.StationCriteria;
import io.github.jhipster.service.QueryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * Service for executing complex queries for Station entities in the database.
 * The main input is a {@link StationCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Station} or a {@link Page} of {@link Station} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class StationQueryService extends QueryService<Station> {

    private final Logger log = LoggerFactory.getLogger(StationQueryService.class);


    private final StationRepository stationRepository;

    public StationQueryService(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    /**
     * Return a {@link List} of {@link Station} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Station> findByCriteria(StationCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<Station> specification = createSpecification(criteria);
        return stationRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Station} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Station> findByCriteria(StationCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<Station> specification = createSpecification(criteria);
        return stationRepository.findAll(specification, page);
    }

    /**
     * Function to convert StationCriteria to a {@link Specifications}
     */
    private Specifications<Station> createSpecification(StationCriteria criteria) {
        Specifications<Station> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Station_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Station_.name));
            }
            if (criteria.getPort() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPort(), Station_.port));
            }
            if (criteria.getSufix() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSufix(), Station_.sufix));
            }
            if (criteria.getLongitude() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLongitude(), Station_.longitude));
            }
            if (criteria.getLatitude() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLatitude(), Station_.latitude));
            }
            if (criteria.getIp() != null) {
                specification = specification.and(buildStringSpecification(criteria.getIp(), Station_.ip));
            }
            if (criteria.getSensorId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getSensorId(), Station_.sensors, Sensor_.id));
            }
        }
        return specification;
    }

}
