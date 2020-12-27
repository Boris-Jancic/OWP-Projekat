package com.Projekat.Knjizara.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
public class User {
    private String userName;
    private String password;
    private String email;
    private String name;
    private String lastName;
    private LocalDateTime dateOfBirth;
    private String address;
    private String phone;
    private LocalDateTime dateOfRegistration;
    private List<String> roles;
}
