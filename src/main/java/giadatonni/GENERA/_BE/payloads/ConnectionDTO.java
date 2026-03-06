package giadatonni.GENERA._BE.payloads;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record ConnectionDTO(
        @NotNull(message = "Follower id must be entered")
        UUID followerId,

        @NotNull(message = "Followed id must be entered")
        UUID followedId
) {
}
