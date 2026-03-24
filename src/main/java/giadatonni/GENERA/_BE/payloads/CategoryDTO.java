package giadatonni.GENERA._BE.payloads;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CategoryDTO(
        @NotBlank(message = "Category must be entered")
        @Size(min = 2, max = 20, message = "The category must be between 2 and 20 characters")
        String category
) {
}
