package giadatonni.GENERA._BE.payloads;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SketchDTO(
        @NotBlank(message = "Code must be entered")
        @Size(max = 20000, message = "The code can contain a maximum of 20.000 characters")
        String code
) {
}
