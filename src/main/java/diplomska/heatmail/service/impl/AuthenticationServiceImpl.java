package diplomska.heatmail.service.impl;

import diplomska.heatmail.dto.LoginUserDto;
import diplomska.heatmail.dto.RegisterUserDto;
import diplomska.heatmail.model.User;
import diplomska.heatmail.repository.UserRepository;
import diplomska.heatmail.service.AuthenticationService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;


    public AuthenticationServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public User signup(RegisterUserDto registerUserDto) {
        User user = new User()
                .setId(String.valueOf(UUID.randomUUID()))
                .setFullName(registerUserDto.getFullName())
                .setEmail(registerUserDto.getEmail())
                .setPassword(passwordEncoder.encode(registerUserDto.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public User authenticate(LoginUserDto loginUserDto) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginUserDto.getEmail(),
                        loginUserDto.getPassword()
                )
        );

        return userRepository.findByEmail(loginUserDto.getEmail())
                .orElseThrow();
    }
}
