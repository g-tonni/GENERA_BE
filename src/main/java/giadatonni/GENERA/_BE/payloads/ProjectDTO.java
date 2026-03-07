package giadatonni.GENERA._BE.payloads;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ProjectDTO(

        @NotBlank(message = "Title must be entered")
        @Size(min = 2, max = 20, message = "The title must be between 2 and 20 characters")
        String title,

        @Size(min = 10, max = 700, message = "The description must be between 10 and 700 characters")
        String description,

        @Size(min = 4, max = 50, message = "The instruction must be between 4 and 50 characters")
        String howToInteract,

        @NotNull(message = "Category must be entered")
        String category
) {
}
