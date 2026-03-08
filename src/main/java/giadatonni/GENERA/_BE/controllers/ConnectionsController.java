package giadatonni.GENERA._BE.controllers;

import giadatonni.GENERA._BE.entities.Connection;
import giadatonni.GENERA._BE.entities.User;
import giadatonni.GENERA._BE.services.ConnectionsService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
public class ConnectionsController {

    private final ConnectionsService connectionsService;

    public ConnectionsController(ConnectionsService connectionsService) {
        this.connectionsService = connectionsService;
    }

    @GetMapping("/users/me/connections")
    public List<User> findUsersFollowedByFollower(@AuthenticationPrincipal User follower) {
        return this.connectionsService.findUsersFollowedByFollower(follower);
    }

    @GetMapping("/users/{followerId}/connections")
    public List<User> findUsersFollowedByFollower(@PathVariable UUID followerId) {
        return this.connectionsService.findUsersFollowedByFollowerId(followerId);
    }

    @GetMapping("/users/me/supporters")
    public List<User> findSupportersByFollowed(@AuthenticationPrincipal User followed) {
        return this.connectionsService.findSupportersByFollowed(followed);
    }

    @GetMapping("/users/{followedId}/supporters")
    public List<User> findSupportersByFollowedId(@PathVariable UUID followedId) {
        return this.connectionsService.findSupportersByFollowedId(followedId);
    }

    @PostMapping("/users/{followedId}/connections")
    @ResponseStatus(HttpStatus.CREATED)
    public Connection addConnection(@AuthenticationPrincipal User follower, @PathVariable UUID followedId) {
        return this.connectionsService.addConnection(follower, followedId);
    }

    @DeleteMapping("/users/{followedId}/connections")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteConnection(@AuthenticationPrincipal User follower, @PathVariable UUID followedId) {
        this.connectionsService.deleteConnection(follower, followedId);
    }

}
