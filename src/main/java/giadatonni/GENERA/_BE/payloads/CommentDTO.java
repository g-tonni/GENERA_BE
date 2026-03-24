package giadatonni.GENERA._BE.payloads;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CommentDTO(
        @NotBlank(message = "Content must be entered")
        @Size(min = 1, max = 1000, message = "The content must be between 1 and 1000 characters")
        String content
) {

}
