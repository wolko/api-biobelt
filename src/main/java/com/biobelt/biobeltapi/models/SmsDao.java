package com.biobelt.biobeltapi.models;

import com.biobelt.biobeltapi.models.sites.Ceinture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

/**
 * @author damy
 * @since 16/04/2017
 * @version 1.0.1
 */

public interface SmsDao extends JpaRepository<Sms, UUID> {

    Sms findById(UUID id);

    List<Sms> findByCeinture(Ceinture ceinture);

    @Query(value = "SELECT * FROM sms WHERE ceinture_id = ?1 AND (date_reception BETWEEN ?2 AND ?3) AND (contenu = 'CO2 open' OR contenu = 'CO2 close')", nativeQuery = true)
    List<Sms> findByCeintureForActivity(UUID ceintureId, String dateAvant, String dateApres);

    @Query(value = "SELECT * FROM sms WHERE ceinture_id = ?1 ORDER BY date DESC LIMIT 1 ", nativeQuery = true)
    Sms findLastOfCeinture(UUID ceintureId);

}
