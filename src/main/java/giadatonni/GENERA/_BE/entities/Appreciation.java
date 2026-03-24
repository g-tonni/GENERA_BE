package giadatonni.GENERA._BE.entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "appreciations")
public class Appreciation {

    @Id
    @GeneratedValue
    private UUID appreciationId;

    @Column(nullable = false)
    private LocalDateTime likedAt;

    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Appreciation() {
    }

    public Appreciation(Project project, User user) {
        this.likedAt = LocalDateTime.now();
        this.project = project;
        this.user = user;
    }

    public UUID getAppreciationId() {
        return appreciationId;
    }

    public LocalDateTime getLikedAt() {
        return likedAt;
    }

    public Project getProject() {
        return project;
    }

    public User getUser() {
        return user;
    }

    @Override
    public String toString() {
        return "Appreciation { " +
                " appreciationId = " + appreciationId +
                ", likedAt = " + likedAt +
                ", project = " + project.getProjectId() +
                ", user = " + user.getUserId() +
                " }";
    }
}
