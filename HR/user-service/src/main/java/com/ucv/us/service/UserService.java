package com.ucv.us.service;

import com.ucv.us.dto.CandidateCreatedEvent;
import com.ucv.us.dto.CreateUserDTO;
import com.ucv.us.dto.EmployeeCreatedEvent;
import com.ucv.us.dto.UpdateUserDTO;
import com.ucv.us.dto.UserDTO;
import com.ucv.us.dto.UserUpdateResponse;
import com.ucv.us.entity.Role;
import com.ucv.us.entity.User;
import com.ucv.us.exception.DuplicateEmailException;
import com.ucv.us.exception.ResourceNotFoundException;
import com.ucv.us.mapper.UserMapper;
import com.ucv.us.repository.RoleRepository;
import com.ucv.us.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final KafkaProducerService kafkaProducerService;

    @Autowired
    public UserService(UserRepository userRepository, RoleRepository roleRepository,
                       UserMapper userMapper, PasswordEncoder passwordEncoder,
                       KafkaProducerService kafkaProducerService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.kafkaProducerService = kafkaProducerService;
    }

    // Register a new user
    public UserDTO registerUser(CreateUserDTO createUserDTO) {
        if (userRepository.existsByEmail(createUserDTO.getEmail())) {
            throw new DuplicateEmailException("Email is already in use");
        }

        Role role = roleRepository.findById(createUserDTO.getRoleId())
                .orElseThrow(() -> new ResourceNotFoundException("Role not found"));

        User user = userMapper.toEntity(createUserDTO, role);
        user.setPassword(passwordEncoder.encode(createUserDTO.getPassword()));

        User savedUser = userRepository.save(user);

        // Send Kafka event if role is CANDIDATE
        if ("CANDIDATE".equalsIgnoreCase(role.getName())) {
            CandidateCreatedEvent event = new CandidateCreatedEvent(
                    savedUser.getId().toString(),
                    savedUser.getEmail(),
                    savedUser.getUsername()
            );
            kafkaProducerService.sendCandidateCreatedEvent(event);
        }

        if ("EMPLOYEE".equalsIgnoreCase(role.getName())) {
            EmployeeCreatedEvent event = new EmployeeCreatedEvent(
                    savedUser.getId().toString(),
                    savedUser.getEmail(),
                    savedUser.getUsername(),
                    "Software Engineer",  // Placeholder
                    "Engineering",        // Placeholder
                    "FULL_TIME",          // Placeholder
                    "2025-05-15",         // You can use LocalDate.now().toString() if dynamic
                    "Cluj-Napoca"         // Placeholder
            );
            kafkaProducerService.sendEmployeeCreatedEvent(event);
        }

        return userMapper.toDto(savedUser);
    }

    public UserUpdateResponse updateUserByEmail(String email, UpdateUserDTO dto) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User with email not found: " + email));
        return updateUser(user.getId(), dto); // reuse your existing update logic
    }


    public UserUpdateResponse updateUser(Long id, UpdateUserDTO dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        boolean emailChanged = false;

        if (dto.getUsername() != null) {
            user.setUsername(dto.getUsername());
        }

        if (dto.getEmail() != null && !dto.getEmail().equals(user.getEmail())) {
            if (userRepository.existsByEmail(dto.getEmail())) {
                throw new DuplicateEmailException("Email already exists");
            }
            user.setEmail(dto.getEmail());
            emailChanged = true;
        }

        if (dto.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        User updated = userRepository.save(user);
        return new UserUpdateResponse(userMapper.toDto(updated), emailChanged);
    }


    // Fetch all users
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }

    // Fetch user by ID
    public UserDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));
        return userMapper.toDto(user);
    }

    // Fetch user by email
    public UserDTO getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
        return userMapper.toDto(user);
    }

    // Delete user by ID
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User not found with ID: " + id);
        }
        userRepository.deleteById(id);
    }
}