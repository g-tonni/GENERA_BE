package giadatonni.GENERA._BE.entities;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "users")
@JsonIgnoreProperties({"password", "role", "profileCoverSketch", "accountNonExpired", "accountNonLocked", "authorities", "credentialsNonExpired", "enabled"})
public class User implements UserDetails {

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

    @ManyToOne
    @JoinColumn(name = "role", nullable = false)
    private Role role;

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Project> projects;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Comment> comments;

    @OneToMany(mappedBy = "follower", cascade = CascadeType.ALL)
    private List<Connection> following;

    @OneToMany(mappedBy = "followed", cascade = CascadeType.ALL)
    private List<Connection> followers;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Appreciation> appreciations;

    public User() {
    }

    public User(String name, String email, String password, Role role) {
        this.name = name;
        this.email = email;
        this.profileImage = "https://res.cloudinary.com/cloudgiada/image/upload/v1772897891/immagini_default-19_wycc5e.png";
        this.profileCoverSketch = "\n" +
                "function setup() {\n" +
                "  createCanvas(windowWidth, windowHeight);\n" +
                "  background(100);\n" +
                "}\n" +
                "\n" +
                "let largx,largy,tra;\n" +
                "let distanza=10;\n" +
                "\n" +
                "function draw() {\n" +
                "  fill(100,100);\n" +
                "  noStroke();\n" +
                "  rect(0,0,width,height);\n" +
                "  for (let i=distanza/2; i<width; i+=distanza){\n" +
                "    for (let j=distanza/2; j<height; j+=distanza){\n" +
                "    cerchio(i,j);\n" +
                "  }\n" +
                "}\n" +
                "  \n" +
                "}\n" +
                "\n" +
                "function cerchio(x, y){\n" +
                "  noStroke();\n" +
                "  let d=dist(mouseX,mouseY,x,y);\n" +
                "  largx=map(d,0,800,10,0);\n" +
                "  let t=dist(mouseX,mouseY,x,y);\n" +
                "  tra=map(t,0,500,100,30);\n" +
                "  fill(0,tra);\n" +
                "  ellipse(x,y,largx,largx);\n" +
                "}";
        this.password = password;
        this.createdAt = LocalDateTime.now();
        this.role = role;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(this.role.getRole()));
    }

    @Override
    public @Nullable String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String getUsername() {
        return this.name;
    }

    public UUID getUserId() {
        return userId;
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
