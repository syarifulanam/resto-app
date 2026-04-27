package com.ngulik.resto_app.service;

import com.ngulik.resto_app.dto.UserDto;
import com.ngulik.resto_app.dto.UserProfileDto;
import com.ngulik.resto_app.entity.User;
import com.ngulik.resto_app.enums.UserRole;
import com.ngulik.resto_app.enums.UserStatus;
import com.ngulik.resto_app.mapper.UserMapper;
import com.ngulik.resto_app.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

    public Page<User> getAllUsers(int page, int size, String name, String email, UserRole role, UserStatus status, String sortDir, String sortBy) {
        // Validasi sortBy (hanya kolom yang diizinkan)
        String validSortBy = validateSortBy(sortBy);

        // Tentukan arah sorting
        Sort.Direction direction = sortDir.equalsIgnoreCase("DESC") ?
                Sort.Direction.DESC : Sort.Direction.ASC;

        // Buat sorting
        Sort sort = Sort.by(direction, validSortBy);

        // Buat Pageable dengan sorting
        Pageable pageable = PageRequest.of(page, size, sort);

        if (name != null && !name.isEmpty()) {
            return userRepository.findByNameContainingIgnoreCase(name, pageable);
        }

        if (email != null && !email.isEmpty()) {
            return userRepository.findByEmailContainingIgnoreCase(email, pageable);
        }

        if (role != null) {
            return userRepository.findByRole(role, pageable);
        }

        if (status != null) {
            return userRepository.findByStatus(status, pageable);
        }

        return userRepository.findAll(pageable);
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
}
