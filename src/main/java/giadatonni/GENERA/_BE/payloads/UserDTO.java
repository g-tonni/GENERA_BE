package giadatonni.GENERA._BE.payloads;

public record UserDTO(
        String name,

        String bio,

        String location,

        String website,

        String profileImage,

        String email,

        String password

) {
}
