package com.Projekat.Knjizara.models;

import com.Projekat.Knjizara.models.enums.EStatus;
import com.Projekat.Knjizara.models.enums.EType;
import com.sun.istack.internal.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @NotBlank(message = "Ne treba da bude prazno polje")
    @Size(min=1, max=30)
    private String userName;

    @NotBlank(message = "Ne treba da bude prazno polje")
    @Size(min=1, max=30)
    private String password;

    @NotBlank(message = "Ne treba da bude prazno polje")
    private String email;

    @NotBlank(message = "Ne treba da bude prazno polje")
    @Size(min=1, max=30)
    private String name;

    @NotBlank(message = "Ne treba da bude prazno polje")
    @Size(min=1, max=30)
    private String lastName;

    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private Date dateOfBirth;

    @NotBlank(message = "Ne treba da bude prazno polje")
    @Size(min=1, max=30)
    private String address;

    @NotBlank(message = "Ne treba da bude prazno polje")
    @Size(min=9)
    private String phone;

    private String dateOfRegistration;
    private EType userType;
    private boolean active;
    private EStatus loyaltyCard;
    private int points;

    public boolean isAdmin(){
        if (userType == EType.ADMIN)
            return true;
        return false;
    }
}
