package giadatonni.GENERA._BE.services;

import giadatonni.GENERA._BE.entities.Connection;
import giadatonni.GENERA._BE.entities.User;
import giadatonni.GENERA._BE.exceptions.BadRequestException;
import giadatonni.GENERA._BE.repositories.ConnectionsRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ConnectionsService {

    private final ConnectionsRepository connectionsRepository;
    private final UsersService usersService;

    public ConnectionsService(ConnectionsRepository connectionsRepository, UsersService usersService) {
        this.connectionsRepository = connectionsRepository;
        this.usersService = usersService;
    }

    public List<User> findUsersFollowedByFollower(User follower) {
        return this.connectionsRepository.findByFollower(follower)
                .stream()
                .map(connection -> connection.getFollowed())
                .toList();
    }

    public List<User> findUsersFollowedByFollowerId(UUID followerId) {
        User follower = this.usersService.findUserById(followerId);

        return this.connectionsRepository.findByFollower(follower)
                .stream()
                .map(connection -> connection.getFollowed())
                .toList();
    }

    public List<User> findSupportersByFollowed(User followed) {
        return this.connectionsRepository.findByFollowed(followed)
                .stream()
                .map(connection -> connection.getFollowed())
                .toList();
    }

    public List<User> findSupportersByFollowedId(UUID followedId) {
        User followed = this.usersService.findUserById(followedId);

        return this.connectionsRepository.findByFollowed(followed)
                .stream()
                .map(connection -> connection.getFollowed())
                .toList();
    }

    public Connection addConnection(User follower, UUID followedId) {
        User followed = this.usersService.findUserById(followedId);

        if (this.connectionsRepository.existsByFollowerAndFollowed(follower, followed))
            throw new BadRequestException("A user can support another user only once");

        Connection newConnection = new Connection(follower, followed);

        this.connectionsRepository.save(newConnection);

        System.out.println("Added connection");

        return newConnection;
    }

    public void deleteConnection(User follower, UUID followedId) {
        User followed = this.usersService.findUserById(followedId);

        Connection connection = this.connectionsRepository.findByFollowerAndFollowed(follower, followed);

        if (connection == null) throw new BadRequestException("There is no connection between the two users");

        this.connectionsRepository.delete(connection);

        System.out.println("Connection deleted");
    }
}
