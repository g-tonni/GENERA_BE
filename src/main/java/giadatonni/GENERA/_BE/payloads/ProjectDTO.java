package giadatonni.GENERA._BE.payloads;

import java.util.UUID;

public record ProjectDTO(
        String title,

        String description,

        String howToInteract,

        String cover,

        UUID categoryId
) {
}
