package com.biobelt.biobeltapi.models.interventions;

import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

/**
 * @author Simon WOLKIEWIEZ
 * @version 1.0.1
 * @since 23/06/2017
 */

@Transactional
public interface DebitPiegeDao extends JpaRepository<DebitPiege, Integer> {

    DebitPiege findById(UUID id);

    List<DebitPiege> findByInterventionV2(InterventionV2 interventionV2);

}
