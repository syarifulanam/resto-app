package com.ngulik.resto_app.mapper;

import com.ngulik.resto_app.dto.UserDto;
import com.ngulik.resto_app.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public User toEntity(UserDto dto) {
        if (dto == null) {
            return null;
        }

        User user = new User();
        user.setId(dto.getId());
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        user.setRole(dto.getRole());
        user.setStatus(dto.getStatus());
        return user;
    }
}
