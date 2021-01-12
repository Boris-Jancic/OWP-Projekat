package com.Projekat.Knjizara.models;

import com.Projekat.Knjizara.models.enums.EType;
import com.sun.istack.internal.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
public class User {
    @NotNull
    private String userName;
    @NotNull
    private String password;
    @NotNull
    private String email;
    @NotNull
    private String name;
    @NotNull
    private String lastName;
    @NotNull
    private Date dateOfBirth;
    @NotNull
    private String address;
    @NotNull
    private String phone;
    @NotNull
    private String dateOfRegistration;
    @NotNull
    private EType userType;
    private boolean active;

    public boolean isAdmin(){
        if (userType == EType.ADMIN)
            return true;
        return false;
    }
}
