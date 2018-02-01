package com.biobelt.biobeltapi.models.bouteilles;

import com.biobelt.biobeltapi.models.interventions.InterventionV2;
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
public interface BouteilleDao extends JpaRepository<Bouteille, UUID> {

    Bouteille findById(UUID id);

    List<Bouteille> findByInterventionV2(InterventionV2 interventionV2);

    List<Bouteille> findByTypeBouteille(TypeBouteille typeBouteille);

}
