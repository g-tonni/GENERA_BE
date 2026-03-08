package giadatonni.GENERA._BE.repositories;

import giadatonni.GENERA._BE.entities.Comment;
import giadatonni.GENERA._BE.entities.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CommentsRepository extends JpaRepository<Comment, UUID> {

    List<Comment> findByProject(Project project);
}
