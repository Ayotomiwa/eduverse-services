package dev.captain.userservice.service;

import dev.captain.userservice.model.tables.AppUser;
import dev.captain.userservice.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        System.out.println("Email: " + email);
        AppUser user = userRepo.findByEmail(email).orElseThrow(
                () -> new UsernameNotFoundException("User not found with username: " + email));
        return new org.springframework.security.core.userdetails.User(user.getUsername(),
                user.getPassword(), new ArrayList<>());
    }
}
