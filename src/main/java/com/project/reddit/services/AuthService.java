package com.project.reddit.services;

import com.project.reddit.Dto.AuthenticationResponse;
import com.project.reddit.Dto.LoginRequest;
import com.project.reddit.Dto.RegisterRequest;
import com.project.reddit.models.NotificationEmail;
import com.project.reddit.models.User;
import com.project.reddit.models.VerificationToken;
import com.project.reddit.repositories.UserRepository;
import com.project.reddit.repositories.VerificationTokenRepository;
import com.project.reddit.exceptions.SpringRedditException;
import com.project.reddit.services.security.JwtProvider;
import com.project.reddit.services.mail.MailService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;


@Service
@AllArgsConstructor
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final MailService mailService;
    private final VerificationTokenRepository verificationTokenRepository;

    private final AuthenticationManager authenticationManager;

    private final JwtProvider jwtProvider;


    @Transactional//used because we are interacting with relational database
    public void signup(RegisterRequest registerRequest){
        User user=new User();
        user.setUsername(registerRequest.getUsername());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setEmail(registerRequest.getEmail());
        user.setCreated(Instant.now());
        user.setEnabled(false);
        userRepository.save(user);
        String token=generateVerificationToken(user);
        //check for the token in our database in verificationToken Table and fetch the user and enable them
        mailService.sendMail(new NotificationEmail(user.getEmail(),"Please Activate your Account",
                "please click here: "+"http://localhost:8080/api/auth/accountVerification/"+token));
    }
    //generates Random Token
    private String generateVerificationToken(User user){
        String token=UUID.randomUUID().toString();
        VerificationToken verificationToken=new VerificationToken();
        verificationToken.setUser(user);
        verificationToken.setToken(token);
        verificationTokenRepository.save(verificationToken);
        return token;
    }

    public void verifyAccount(String token) {
        Optional<VerificationToken> verificationToken=verificationTokenRepository.findByToken(token);
        verificationToken.orElseThrow(()-> new SpringRedditException("Invalid Token"));
        fetchUserAndEnable(verificationToken.get());
    }
    //Optional<> obj.get() gives the object

    @Transactional
        private void fetchUserAndEnable(VerificationToken verificationToken) {
        String username=verificationToken.getUser().getUsername();
        User user= userRepository.findByUsername(username)
                .orElseThrow(()-> new SpringRedditException("Cannot find user with username"+ username));
        user.setEnabled(true);
        userRepository.save(user);
    }

    public AuthenticationResponse login(LoginRequest loginRequest) {
        Authentication authenticate = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        //if you want ot check if a user is logged in or not, just check the security context and
        //if authenticate object is present user in logged in
        //A SecurityContext instance is stored in the session once the user has been authenticated
        SecurityContextHolder.getContext().setAuthentication(authenticate);
        String token=jwtProvider.generateToken(authenticate);
        return new AuthenticationResponse(token, loginRequest.getUsername());
    }
}
