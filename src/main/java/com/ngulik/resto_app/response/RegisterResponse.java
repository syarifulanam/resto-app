package com.ngulik.resto_app.response;

import com.ngulik.resto_app.enums.UserRole;
import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterResponse {
    private long id;
    private String name;
    private String email;
    private UserRole role;
}
