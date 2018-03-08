package br.net.triangulohackerspace.westation.service;


import br.net.triangulohackerspace.westation.domain.Sensor;
import br.net.triangulohackerspace.westation.domain.Sensor_;
import br.net.triangulohackerspace.westation.repository.SensorRepository;
import br.net.triangulohackerspace.westation.service.dto.SensorCriteria;
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
 * Service for executing complex queries for Sensor entities in the database.
 * The main input is a {@link SensorCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Sensor} or a {@link Page} of {@link Sensor} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SensorQueryService extends QueryService<Sensor> {

    private final Logger log = LoggerFactory.getLogger(SensorQueryService.class);


    private final SensorRepository sensorRepository;

    public SensorQueryService(SensorRepository sensorRepository) {
        this.sensorRepository = sensorRepository;
    }

    /**
     * Return a {@link List} of {@link Sensor} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Sensor> findByCriteria(SensorCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<Sensor> specification = createSpecification(criteria);
        return sensorRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Sensor} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Sensor> findByCriteria(SensorCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<Sensor> specification = createSpecification(criteria);
        return sensorRepository.findAll(specification, page);
    }

    /**
     * Function to convert SensorCriteria to a {@link Specifications}
     */
    private Specifications<Sensor> createSpecification(SensorCriteria criteria) {
        Specifications<Sensor> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Sensor_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Sensor_.name));
            }
            if (criteria.getTopic() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTopic(), Sensor_.topic));
            }
            if (criteria.getThreshold() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getThreshold(), Sensor_.threshold));
            }
        }
        return specification;
    }

}
