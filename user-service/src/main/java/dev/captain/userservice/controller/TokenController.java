package dev.captain.userservice.controller;


import dev.captain.userservice.config.JwtTokenProvider;
import dev.captain.userservice.model.LoginRequest;
import dev.captain.userservice.model.LoginResponse;
import dev.captain.userservice.model.tables.AppUser;
import dev.captain.userservice.repo.UserRepo;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/user-service/authenticate")
@RequiredArgsConstructor
public class TokenController {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepo userRepo;


    @PostMapping("")
    public ResponseEntity<?> authenticate(@Valid @RequestBody LoginRequest loginRequest) {

        String userEmail = loginRequest.getUsername();

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );

            System.out.println("Authentication: " + authentication);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtTokenProvider.generateToken(authentication);
            System.out.println("JWT: " + jwt);
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            System.out.println("UserDetails: " + userDetails.getUsername() + " " + userDetails.getPassword());

            Optional<AppUser> user = userRepo.findByEmail(userEmail);

            if (user.isEmpty()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found");
            }


            return ResponseEntity.ok(new LoginResponse(jwt, user.get()));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Incorrect username or password");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Authentication failed");
        }
    }
}


