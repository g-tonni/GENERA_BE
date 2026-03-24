package giadatonni.GENERA._BE.services;

import giadatonni.GENERA._BE.entities.Connection;
import giadatonni.GENERA._BE.entities.User;
import giadatonni.GENERA._BE.exceptions.BadRequestException;
import giadatonni.GENERA._BE.repositories.ConnectionsRepository;
import giadatonni.GENERA._BE.specifications.ConnectionsSpecifications;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ConnectionsService {

    private final ConnectionsRepository connectionsRepository;
    private final UsersService usersService;
    private final ConnectionsSpecifications connectionsSpecifications;

    public ConnectionsService(ConnectionsRepository connectionsRepository, UsersService usersService, ConnectionsSpecifications connectionsSpecifications) {
        this.connectionsRepository = connectionsRepository;
        this.usersService = usersService;
        this.connectionsSpecifications = connectionsSpecifications;
    }

    public List<User> findUsersFollowedByFollower(User follower) {
        return this.connectionsRepository.findByFollower(follower)
                .stream()
                .map(connection -> connection.getFollowed())
                .toList();
    }

    public Page<Connection> findUsersFollowedByFollowerId(int page, int size, String partialName, UUID followerId) {
        if (size > 20 || size < 0) size = 10;
        if (page < 0) page = 0;
        Pageable pageable = PageRequest.of(page, size);

        Specification<Connection> spec = connectionsSpecifications.followerIdEquals(followerId);

        if (partialName != null) {
            spec = spec.and(connectionsSpecifications.followedPartialNameEqualsTo(partialName));
        }

        return this.connectionsRepository.findAll(spec, pageable);
    }

    public List<User> findSupportersByFollowed(User followed) {
        return this.connectionsRepository.findByFollowed(followed)
                .stream()
                .map(connection -> connection.getFollower())
                .toList();
    }

    public Page<Connection> findSupportersByFollowedId(int page, int size, String partialName, UUID followedId) {
        if (size > 20 || size < 0) size = 10;
        if (page < 0) page = 0;
        Pageable pageable = PageRequest.of(page, size);

        Specification<Connection> spec = connectionsSpecifications.followedIdEquals(followedId);

        if (partialName != null) {
            spec = spec.and(connectionsSpecifications.followerPartialNameEqualsTo(partialName));
        }

        return this.connectionsRepository.findAll(spec, pageable);
    }

    public Connection addConnection(User follower, UUID followedId) {
        if (follower.getUserId().equals(followedId))
            throw new BadRequestException("A user cannot follow himself");

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
