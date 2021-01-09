package com.Projekat.Knjizara.models;

import com.Projekat.Knjizara.models.enums.EType;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
public class User {
    private String userName;
    private String password;
    private String email;
    private String name;
    private String lastName;
    private Date dateOfBirth;
    private String address;
    private String phone;
    private Date dateOfRegistration;
    private EType userType;
    private boolean active;
}
