package com.ngulik.resto_app.service;

import com.ngulik.resto_app.dto.UserDto;
import com.ngulik.resto_app.dto.UserProfileDto;
import com.ngulik.resto_app.dto.UserUpdateDto;
import com.ngulik.resto_app.entity.User;
import com.ngulik.resto_app.enums.UserRole;
import com.ngulik.resto_app.enums.UserStatus;
import com.ngulik.resto_app.mapper.UserMapper;
import com.ngulik.resto_app.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Transactional
    public User registerUser(UserDto userDto) {
        User user = userMapper.toEntity(userDto);
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setRole(UserRole.STAFF); // default role for registration
        user.setStatus(UserStatus.ACTIVE); // default status
        return userRepository.save(user);
    }

    public UserProfileDto getUserProfile(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return userMapper.toUserProfileDto(user);
    }

    public Page<User> getAllUsers(int page, int size, String name, String email, UserRole role, UserStatus status, String sortBy, String sortDir) {
        // Validasi sortBy (hanya kolom yang diizinkan)
        String validSortBy = validateSortBy(sortBy);

        // Tentukan arah sorting
        Sort.Direction direction = sortDir.equalsIgnoreCase("DESC") ?
                Sort.Direction.DESC : Sort.Direction.ASC;

        // Buat sorting
        Sort sort = Sort.by(direction, validSortBy);

        // Buat Pageable dengan sorting
        Pageable pageable = PageRequest.of(page, size, sort);

        log.info("Searching users - name: {}, email: {}, role: {}, status: {}", name, email, role, status);

        return userRepository.search(name, email, role, status, pageable);
    }

    // Tambahkan method validasi
    private String validateSortBy(String sortBy) {
        if (sortBy == null || sortBy.isEmpty()) {
            return "id";
        }

        // Hanya izinkan kolom-kolom ini
        switch (sortBy.toLowerCase()) {
            case "id":
                return "id";
            case "name":
                return "name";
            case "email":
                return "email";
            default:
                return "id"; // Default jika kolom tidak dikenal
        }
    }

    public UserDto createUser(UserDto userDto) {
        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        // Convert to Entity
        User user = new User();
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setRole(userDto.getRole());
        user.setStatus(userDto.getStatus());

        // Save
        User savedUser = userRepository.save(user);

        // Return DTO
        return UserDto.builder()
                .id(savedUser.getId())
                .name(savedUser.getName())
                .email(savedUser.getEmail())
                .role(savedUser.getRole())
                .status(savedUser.getStatus())
                .build();
    }

    public UserDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole())
                .status(user.getStatus())
                .build();
    }

    public UserDto getUserByEmail(String email) {
        log.info("Fetching user with email: {}", email);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));

        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole())
                .status(user.getStatus())
                .build();
    }

    public UserDto updateUser(Long id, UserUpdateDto updateRequest) {
        log.info("Updating user with id: {}", id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        // Cek email duplikat (Jika email berubah)
        if (!user.getEmail().equals(updateRequest.getEmail())) {
            if (userRepository.existsByEmail(updateRequest.getEmail())) {
                throw new RuntimeException("Email " + updateRequest.getEmail() + " already exists");
            }
            user.setEmail(updateRequest.getEmail());
        }

        // Update field yang diizinkan
        user.setName(updateRequest.getName());

        if (updateRequest.getStatus() != null) {
            user.setStatus(updateRequest.getStatus());
        }

        // Password dan ROLE tidak diupdate
        User savedUser = userRepository.save(user);

        return UserDto.builder()
                .id(savedUser.getId())
                .name(savedUser.getName())
                .email(savedUser.getEmail())
                .role(savedUser.getRole())
                .status(savedUser.getStatus())
                .build();
    }

    // DELETE User (Soft Delete)
    @Transactional
    public void deleteUser(Long id) {
        log.info("Soft deleting user with id: {}", id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        // Cek apakah sudah di delete sebelumnya
        if (user.getDeletedAt() != null) {
            throw new RuntimeException("User already deleted");
        }

        // Soft Delete: set deleteAt dengan waktu sekarang
        user.setDeletedAt(LocalDateTime.now());

        userRepository.save(user);

        log.info("User with id: {} has been soft deleted", id);
    }
}
