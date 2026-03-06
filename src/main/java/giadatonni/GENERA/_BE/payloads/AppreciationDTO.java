package giadatonni.GENERA._BE.payloads;

import java.util.UUID;

public record AppreciationDTO(

        UUID projectId,

        UUID userId
) {
}
