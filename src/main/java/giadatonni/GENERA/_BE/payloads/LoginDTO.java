package giadatonni.GENERA._BE.payloads;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginDTO(

        @NotBlank(message = "Email must be entered")
        @Email(message = "Invalid email address")
        String email,

        @NotBlank(message = "Password must be entered")
        String password
) {
}
