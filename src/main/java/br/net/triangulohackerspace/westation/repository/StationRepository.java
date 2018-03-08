package br.net.triangulohackerspace.westation.repository;

import br.net.triangulohackerspace.westation.domain.Station;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.util.List;

/**
 * Spring Data JPA repository for the Station entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StationRepository extends JpaRepository<Station, Long>, JpaSpecificationExecutor<Station> {
    @Query("select distinct station from Station station left join fetch station.sensors")
    List<Station> findAllWithEagerRelationships();

    @Query("select station from Station station left join fetch station.sensors where station.id =:id")
    Station findOneWithEagerRelationships(@Param("id") Long id);

}
