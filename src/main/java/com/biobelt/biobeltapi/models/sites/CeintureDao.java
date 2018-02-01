package com.biobelt.biobeltapi.models.sites;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

/**
 * @author damy
 * @version 1.0.1
 * @since 16/04/2017
 */

@Transactional
public interface CeintureDao extends JpaRepository<Ceinture, UUID> {

    Ceinture findById(UUID id);

    List<Ceinture> findBySite(Site site);

    @Query(value = "SELECT * FROM ceinturev2", nativeQuery = true)
    List<CeintureV2> findAllCeinturesV2();

    @Query(value = "SELECT * FROM ceinturev2 WHERE numero_telephone = ?1 AND archive = 0", nativeQuery = true)
    CeintureV2 findCeintureV2ByTel(String numeroTelephone);

    CeintureV2 findByNumeroTelephone(String numeroTelephone);

}
