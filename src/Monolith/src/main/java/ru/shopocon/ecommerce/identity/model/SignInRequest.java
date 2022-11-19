package ru.shopocon.ecommerce.identity.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignInRequest {

    @NotBlank
    @Email(message = "Valid e-mail address required")
    private String username;

    @NotBlank
    @Pattern(regexp = "^\\S*$", message = "Spaces are not allowed in the password")
    private String password;

    private boolean rememberMe;
}
