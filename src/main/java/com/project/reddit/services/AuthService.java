package com.project.reddit.services;

import com.project.reddit.dto.RegisterRequest;
import com.project.reddit.models.User;
import com.project.reddit.models.VerificationToken;
import com.project.reddit.repositories.UserRepository;
import com.project.reddit.repositories.VerificationTokenRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    private final VerificationTokenRepository  verificationTokenRepository;

    @Transactional
    public void signup(RegisterRequest registerRequest){
        User user=new User();
        user.setUsername(registerRequest.getUsername());
        user.setEmail((registerRequest.getEmail()));
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
//        user.setCreatedDate(Instant.now());
//        user.setEnabled(false);
        userRepository.save(user);

        String token=generateVerificationToken(user);
    }

    private String generateVerificationToken(User user) {
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);

        verificationTokenRepository.save(verificationToken);
        return token;
    }

    public String addUser(User user)
    {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return "New User added in system";
    }
}
