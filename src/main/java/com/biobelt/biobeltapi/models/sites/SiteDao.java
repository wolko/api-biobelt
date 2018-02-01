package com.biobelt.biobeltapi.models.sites;

import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.UUID;

/**
 * @author Simon WOLKIEWIEZ
 * @since 16/04/2017
 * @version 1.0.1
 */

@Transactional
public interface SiteDao extends JpaRepository<Site, Integer> {

    Site findById(UUID id);

}
