package giadatonni.GENERA._BE.payloads;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record NewProjectDTO(

        @NotNull(message = "Category must be entered")
        UUID categoryId,

        @NotNull(message = "Author must be entered")
        UUID authorId

) {
}
