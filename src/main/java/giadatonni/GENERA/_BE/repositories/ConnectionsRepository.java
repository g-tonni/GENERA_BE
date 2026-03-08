package giadatonni.GENERA._BE.repositories;

import giadatonni.GENERA._BE.entities.Connection;
import giadatonni.GENERA._BE.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ConnectionsRepository extends JpaRepository<Connection, UUID> {

    boolean existsByFollowerAndFollowed(User follower, User followed);

    Connection findByFollowerAndFollowed(User follower, User followed);

    List<Connection> findByFollower(User follower);

    List<Connection> findByFollowed(User followed);
}
