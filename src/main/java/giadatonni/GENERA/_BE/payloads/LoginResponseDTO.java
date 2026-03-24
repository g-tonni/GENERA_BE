package giadatonni.GENERA._BE.payloads;

import java.util.UUID;

public record LoginResponseDTO(
        String token,
        UUID userId
) {
}
