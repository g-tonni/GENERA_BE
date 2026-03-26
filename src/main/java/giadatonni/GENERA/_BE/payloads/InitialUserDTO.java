package giadatonni.GENERA._BE.payloads;

public record InitialUserDTO(
        String name,

        String bio,

        String location,

        String website,

        String profileCoverSketch,

        String email,

        String password
) {
}
