package giadatonni.GENERA._BE.entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue
    private UUID commentId;

    @Column(length = 1000, nullable = false)
    private String content;

    @Column(nullable = false)
    private LocalDateTime publishedAt;

    @Column(nullable = false)
    private boolean updated;

    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Comment() {
    }

    public Comment(String content, Project project, User user) {
        this.content = content;
        this.publishedAt = LocalDateTime.now();
        this.updated = false;
        this.project = project;
        this.user = user;
    }

    public UUID getCommentId() {
        return commentId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getPublishedAt() {
        return publishedAt;
    }

    public Project getProject() {
        return project;
    }

    public User getUser() {
        return user;
    }

    @Override
    public String toString() {
        return "Comment { " +
                " commentId = " + commentId +
                ", content = " + content +
                ", publishedAt = " + publishedAt +
                ", updated = " + updated +
                ", project = " + project.getProjectId() +
                ", user = " + user.getUserId() +
                " }";
    }
}
