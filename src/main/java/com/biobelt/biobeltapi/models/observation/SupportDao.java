package com.biobelt.biobeltapi.models.observation;

import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

/**
 * @author Simon WOLKIEWIEZ
 * @version 1.0.1
 * @since 29/06/2017
 */

@Transactional
public interface SupportDao extends JpaRepository<Support, Integer> {

    Support findById(UUID id);

    List<Support> findByObservation(Observation observation);

    List<Support> findByTypeAndObservation(Support.Type type, Observation observation);

}
