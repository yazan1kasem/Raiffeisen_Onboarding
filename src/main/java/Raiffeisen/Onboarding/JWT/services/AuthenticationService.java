package Raiffeisen.Onboarding.JWT.services;

import Raiffeisen.Onboarding.Entities.User;
import Raiffeisen.Onboarding.JWT.dtos.*;
import Raiffeisen.Onboarding.Repository.UserRepository;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    public AuthenticationService(UserRepository userRepository,
                                 AuthenticationManager authenticationManager,
                                 PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User signup(RegisterUserDto input) {
        if(userRepository.existsByUsername(input.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }else {
            User user = new User().builder()
                    .username(input.getUsername())
                    .password(passwordEncoder.encode(input.getPassword()))
                    .role(User.Role.USER)
                    .build();
            return userRepository.save(user);
        }

    }

    public User authenticate(LoginUserDto input) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.getUsername(),
                        input.getPassword()
                )
        );

        return userRepository.findByUsername(input.getUsername())
                .orElseThrow();
    }
}
