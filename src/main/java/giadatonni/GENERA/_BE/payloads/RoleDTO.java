package giadatonni.GENERA._BE.payloads;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RoleDTO(
        @NotBlank(message = "Role must be entered")
        @Size(min = 2, max = 20, message = "The role must be between 2 and 20 characters")
        String role
) {
}
