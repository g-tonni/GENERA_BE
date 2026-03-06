package giadatonni.GENERA._BE.payloads;

import jakarta.validation.constraints.*;

public record UserDTO(

        @NotBlank(message = "Name must be entered")
        @Size(min = 2, max = 20, message = "The name must be between 2 and 20 characters")
        String name,

        @Size(min = 10, max = 500, message = "The bio must be between 10 and 500 characters")
        String bio,

        @Size(min = 2, max = 20, message = "The location must be between 2 and 20 characters")
        String location,

        @Size(min = 10, max = 50, message = "The website must be between 10 and 50 characters")
        String website,

        @NotNull(message = "Profile image must be entered")
        String profileImage,

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
