package giadatonni.GENERA._BE.payloads;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record AppreciationDTO(

        @NotNull(message = "Project id must be entered")
        UUID projectId
) {
}
