package com.riya.shopsphere.service;
import com.riya.shopsphere.dto.LoginResponseDTO;
import com.riya.shopsphere.entity.User;
import com.riya.shopsphere.repository.UserRepository;
import com.riya.shopsphere.security.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.riya.shopsphere.dto.UserResponseDTO;
@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    private UserResponseDTO convertToDTO(User user) {
        return new UserResponseDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole().toString()
        );
    }

    public UserResponseDTO registerUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userRepository.save(user);
        return convertToDTO(savedUser);
    }

    public LoginResponseDTO loginUser(String email, String password) {
        User user = userRepository.findByEmail(email);

        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            String token = jwtUtil.generateToken(user.getEmail(), user.getRole().toString());            UserResponseDTO userDTO = convertToDTO(user);
            return new LoginResponseDTO(token, userDTO);
        }

        return null;
    }
}