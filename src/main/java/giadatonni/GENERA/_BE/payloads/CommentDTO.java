package giadatonni.GENERA._BE.payloads;

import java.util.UUID;

public record CommentDTO(

        String content,

        UUID projectId,

        UUID userId
) {

}
