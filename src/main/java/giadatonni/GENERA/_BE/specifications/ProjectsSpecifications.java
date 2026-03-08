package giadatonni.GENERA._BE.specifications;

import giadatonni.GENERA._BE.entities.Project;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class ProjectsSpecifications {

    public Specification<Project> partialTitleEqualsTo(String partialTitle) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), "%" + partialTitle.toLowerCase() + "%");
    }

    public Specification<Project> categoryEqualsTo(String category) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(criteriaBuilder.lower(root.get("category").get("category")), category.toLowerCase());
    }


}
