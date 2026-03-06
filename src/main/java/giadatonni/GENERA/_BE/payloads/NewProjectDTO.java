package giadatonni.GENERA._BE.payloads;

import java.util.UUID;

public record NewProjectDTO(

        UUID categoryId,

        UUID authorId

) {
}
