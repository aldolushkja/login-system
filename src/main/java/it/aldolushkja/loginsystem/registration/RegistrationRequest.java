package it.aldolushkja.loginsystem.registration;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class RegistrationRequest {
    @NotBlank
    @Size(max = 100)
    private final String firstName;
    @NotBlank
    @Size(max = 100)
    private final String lastName;
    @Email
    private final String email;
    @NotBlank
    @Size(min = 6, max = 16)
    private final String password;
}
