package giadatonni.GENERA._BE.specifications;

import giadatonni.GENERA._BE.entities.Appreciation;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class AppreciationsSpecifications {


    public Specification<Appreciation> userIdEquals(UUID userId) {
        return (root, query, cb) -> {
            return cb.equal(root.join("user").get("userId"), userId);
        };
    }

    public Specification<Appreciation> projectPartialTitleEqualsTo(String partialTitle) {

        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(criteriaBuilder.lower(root.get("project").get("title")), "%" + partialTitle.toLowerCase() + "%");
    }

}

