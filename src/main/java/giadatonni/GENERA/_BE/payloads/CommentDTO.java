package giadatonni.GENERA._BE.payloads;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record CommentDTO(
        @NotBlank(message = "Content must be entered")
        @Size(min = 1, max = 1000, message = "The content must be between 1 and 1000 characters")
        String content,

        @NotNull(message = "Project id must be entered")
        UUID projectId,

        @NotNull(message = "User id must be entered")
        UUID userId
) {

}
