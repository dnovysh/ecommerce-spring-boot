package ru.shopocon.ecommerce.identity.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignUpRequest {

    @NotBlank
    @Length(min = 1, max = 100)
    private String userAlias;

    @NotBlank
    @Length(min = 3, max = 60)
    @Email(message = "Valid e-mail address required")
    private String username;

    @NotBlank
    @Pattern(regexp = "^\\S*$", message = "Spaces are not allowed in the password")
    private String password;
}
