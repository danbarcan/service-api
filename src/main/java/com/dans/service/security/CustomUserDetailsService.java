package com.dans.service.security;

import com.dans.service.entities.User;
import com.dans.service.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String byEmail)
            throws UsernameNotFoundException {
        // Let people login with either username or email
        User user = userRepository.findByEmail(byEmail)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found with  email : " + byEmail)
                );

        return UserPrincipal.create(user);
    }

    @Transactional
    public UserDetails loadUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("User not found with id : " + id)
        );

        return UserPrincipal.create(user);
    }
}