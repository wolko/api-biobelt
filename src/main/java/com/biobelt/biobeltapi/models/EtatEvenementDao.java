package com.biobelt.biobeltapi.models;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

/**
 * @author damy
 * @since 16/04/2017
 * @version 1.0.1
 */

public interface EtatEvenementDao extends JpaRepository<EtatEvenement, UUID> {

    EtatEvenement findById(UUID id);

    List<EtatEvenement> findAll();

    EtatEvenement findByEvenement(Evenement evenement);

    EtatEvenement findByEvenementId(UUID evenement_id);

    @Query(value = "SELECT * FROM etat_evenement as e JOIN sms as s ON e.evenement_id = s.id WHERE ceinture_id = ?1 ORDER BY date DESC LIMIT 1", nativeQuery = true)
    EtatEvenement findLastOfCeinture(UUID ceintureId);

    @Query(value = "SELECT * FROM etat_evenement as e JOIN sms as s ON e.evenement_id = s.id WHERE ceinture_id = ?1 ORDER by date DESC", nativeQuery = true)
    List<EtatEvenement> findOfCeinture(UUID ceintureId);

}
