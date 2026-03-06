package giadatonni.GENERA._BE.payloads;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegisterDTO(

        @NotBlank(message = "Name must be entered")
        @Size(min = 2, max = 20, message = "The name must be between 2 and 20 characters")
        String name,

        @NotBlank(message = "Email must be entered")
        @Email(message = "Invalid email address")
        String email,

        @NotBlank(message = "Password must be entered")
        @Pattern(
                regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
                message = "Password must have at least 8 characters, one uppercase letter, one lowercase letter, one number and one special character"
        )
        String password
) {

}
