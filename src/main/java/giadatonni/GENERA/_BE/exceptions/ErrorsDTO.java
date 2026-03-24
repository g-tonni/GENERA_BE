package giadatonni.GENERA._BE.exceptions;

import java.util.List;

public record ErrorsDTO(
        List<String> errorsList
) {
}
