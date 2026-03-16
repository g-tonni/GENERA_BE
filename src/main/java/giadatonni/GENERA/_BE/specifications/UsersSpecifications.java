package giadatonni.GENERA._BE.specifications;

import giadatonni.GENERA._BE.entities.User;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class UsersSpecifications {

    public Specification<User> partialNameEqualsTo(String partialName) {

        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + partialName.toLowerCase() + "%");
    }

}
