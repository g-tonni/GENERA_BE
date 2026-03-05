package giadatonni.GENERA._BE.entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "connections")
public class Connection {

    @Id
    @GeneratedValue
    private UUID connectionId;

    @Column(nullable = false)
    private LocalDateTime followedAt;

    @ManyToOne
    @JoinColumn(name = "follower_id", nullable = false)
    private User follower;

    @ManyToOne
    @JoinColumn(name = "followed_id", nullable = false)
    private User followed;

    public Connection() {
    }

    public Connection(User follower, User followed) {
        this.followedAt = LocalDateTime.now();
        this.follower = follower;
        this.followed = followed;
    }

    public UUID getConnectionId() {
        return connectionId;
    }

    public LocalDateTime getFollowedAt() {
        return followedAt;
    }

    public User getFollower() {
        return follower;
    }

    public User getFollowed() {
        return followed;
    }

    @Override
    public String toString() {
        return "Connection { " +
                " connectionId = " + connectionId +
                ", followedAt = " + followedAt +
                ", follower = " + follower.getUserId() +
                ", followed = " + followed.getUserId() +
                " }";
    }
}
