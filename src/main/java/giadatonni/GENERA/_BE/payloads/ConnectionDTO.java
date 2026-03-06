package giadatonni.GENERA._BE.payloads;

import java.util.UUID;

public record ConnectionDTO(

        UUID followerId,

        UUID followedId
) {
}
