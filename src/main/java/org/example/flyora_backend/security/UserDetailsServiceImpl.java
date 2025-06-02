package org.example.flyora_backend.security;

import java.util.List;

import org.example.flyora_backend.model.User;
import org.example.flyora_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepo;

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return new org.springframework.security.core.userdetails.User(
            user.getName(),
            user.getPassword(),
            List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()))
        );
    } 
}
