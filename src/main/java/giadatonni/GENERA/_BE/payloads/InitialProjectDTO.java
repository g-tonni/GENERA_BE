package giadatonni.GENERA._BE.payloads;

public record InitialProjectDTO(
        String title,
        String description,
        String howToInteract,
        String cover,
        String script,
        String category
) {
}
