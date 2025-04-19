package org.example.model;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class User {
    private String id;
    private String login;
    private String password;
    private String role;
}
