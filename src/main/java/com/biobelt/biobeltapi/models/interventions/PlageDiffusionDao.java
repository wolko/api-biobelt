package com.biobelt.biobeltapi.models.interventions;

import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

/**
 * @author Simon WOLKIEWIEZ
 * @version 1.0.1
 * @since 22/06/2017
 */

@Transactional
public interface PlageDiffusionDao extends JpaRepository<PlageDiffusion, Integer> {

    PlageDiffusion findById(UUID id);

    List<PlageDiffusion> findByInterventionV2(InterventionV2 interventionV2);

}