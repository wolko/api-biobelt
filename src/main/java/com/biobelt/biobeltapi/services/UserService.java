package com.biobelt.biobeltapi.services;

import com.biobelt.biobeltapi.models.user.User;
import com.biobelt.biobeltapi.models.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @author Simon WOLKIEWIEZ
 * @version 1.0.1
 * @since 28/09/2017
 */

@Service("userDetailsService")
public class UserService implements UserDetailsService {

    private static final String BASE_USERNAME = "admin@biobeltapi.com";
    private static final String BASE_PASSWORD = "$2a$10$D4OLKI6yy68crm.3imC9X.P2xqKHs5TloWUcr6z5XdOqnTrAK84ri";

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Ajout d'une utilisateur de base
        if (userRepository.findOneByUsername(BASE_USERNAME) == null) {
            User user = new User(BASE_USERNAME, BASE_PASSWORD);
            userRepository.save(user);
        }

        return userRepository.findOneByUsername(username);
    }
}