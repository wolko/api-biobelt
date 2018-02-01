package com.biobelt.biobeltapi.models.interventions;

import com.biobelt.biobeltapi.models.sites.Ceinture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

/**
 * @author damy
 * @version 1.0.1
 * @since 16/04/2017
 */

public interface InterventionV2Dao extends JpaRepository<InterventionV2, UUID> {

    InterventionV2 findById(UUID id);

    List<InterventionV2> findByCeinture(Ceinture ceinture);

    @Query(value = "SELECT * FROM interventionv2 WHERE ceinture_id = ?1 ORDER BY date DESC LIMIT 1 ", nativeQuery = true)
    InterventionV2 findLastOfCeinture(UUID ceintureId);

    @Query(value = "SELECT * FROM interventionv2 WHERE ceinture_id = ?1 ORDER BY date DESC LIMIT 1,1 ", nativeQuery = true)
    InterventionV2 findSecondeToLastOfCeinture(UUID ceintureId);

}
