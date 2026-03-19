package giadatonni.GENERA._BE.specifications;

import giadatonni.GENERA._BE.entities.Connection;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.UUID;


@Component
public class ConnectionsSpecifications {

    public Specification<Connection> followedIdEquals(UUID followedId) {
        return (root, query, cb) -> {
            return cb.equal(root.join("followed").get("userId"), followedId);
        };
    }

    public Specification<Connection> followerIdEquals(UUID followerId) {
        return (root, query, cb) -> {
            return cb.equal(root.join("follower").get("userId"), followerId);
        };
    }

    public Specification<Connection> followerPartialNameEqualsTo(String partialName) {

        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(criteriaBuilder.lower(root.get("follower").get("name")), "%" + partialName.toLowerCase() + "%");
    }

    public Specification<Connection> followedPartialNameEqualsTo(String partialName) {

        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(criteriaBuilder.lower(root.get("followed").get("name")), "%" + partialName.toLowerCase() + "%");
    }
}
