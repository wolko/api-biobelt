package com.biobelt.biobeltapi.models.user;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Simon WOLKIEWIEZ
 * @version 1.0.0
 * @since 05/09/2017
 */

public interface UserRepository extends JpaRepository<User, Long> {
    User findOneByUsername(String username);
}
