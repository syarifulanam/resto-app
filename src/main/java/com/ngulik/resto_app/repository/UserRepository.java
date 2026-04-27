package com.ngulik.resto_app.repository;

import com.ngulik.resto_app.entity.User;
import com.ngulik.resto_app.enums.UserRole;
import com.ngulik.resto_app.enums.UserStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Page<User> findByNameContainingIgnoreCase(String name, Pageable pageable);

    @Query("SELECT u FROM User u WHERE " +
            "(:name IS NULL OR LOWER(u.name) LIKE LOWER(CONCAT('%', :name, '%'))) AND " +
            "(:email IS NULL OR LOWER(u.email) LIKE LOWER(CONCAT('%', :email, '%'))) AND " +
            "(:role IS NULL OR u.role = :role) AND " +
            "(:status IS NULL OR u.status = :status)")
    Page<User> search(@Param("name") String name,
                      @Param("email") String email,
                      @Param("role") UserRole role,
                      @Param("status") UserStatus status,
                      Pageable pageable);

}

