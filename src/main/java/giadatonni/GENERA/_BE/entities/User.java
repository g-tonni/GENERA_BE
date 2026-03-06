package giadatonni.GENERA._BE.entities;


import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue
    private UUID userId;

    @Column(nullable = false)
    private String name;

    @Column(length = 500)
    private String bio;

    private String location;

    private String website;

    @Column(nullable = false)
    private String profileImage;

    @Column(nullable = false)
    private String profileCoverSketch;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @OneToOne
    @JoinColumn(name = "role", nullable = false)
    private Role role;

    public User() {
    }

    public User(String name, String email, String password, Role role) {
        this.name = name;
        this.email = email;
        this.profileImage = "https://ui-avatars.com/api/?name=" + name;
        this.profileCoverSketch = "";
        this.password = password;
        this.createdAt = LocalDateTime.now();
        this.role = role;
    }

    public UUID getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getProfileCoverSketch() {
        return profileCoverSketch;
    }

    public void setProfileCoverSketch(String profileCoverSketch) {
        this.profileCoverSketch = profileCoverSketch;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    @Override
    public String toString() {
        return "User {" +
                " userId =" + userId +
                ", name = " + name +
                ", bio = " + bio +
                ", location = " + location +
                ", website = " + website +
                ", email = " + email +
                ", createdAt = " + createdAt +
                ", role = " + role +
                " }";
    }
}
