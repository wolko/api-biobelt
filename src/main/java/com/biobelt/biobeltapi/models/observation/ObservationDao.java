package com.biobelt.biobeltapi.models.observation;

import com.biobelt.biobeltapi.models.interventions.InterventionV2;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

/**
 * @author Simon WOLKIEWIEZ
 * @version 1.0.1
 * @since 21/06/2017
 */

@Transactional
public interface ObservationDao extends JpaRepository<Observation, Integer> {

    Observation findById(UUID id);

    List<Observation> findByInterventionV2(InterventionV2 intervention);

}
