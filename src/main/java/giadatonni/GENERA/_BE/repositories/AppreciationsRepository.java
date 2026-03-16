package giadatonni.GENERA._BE.repositories;

import giadatonni.GENERA._BE.entities.Appreciation;
import giadatonni.GENERA._BE.entities.Project;
import giadatonni.GENERA._BE.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AppreciationsRepository extends JpaRepository<Appreciation, UUID> {

    List<Appreciation> findByUser(User user);

    List<Appreciation> findByProject(Project project);

    Appreciation findByUserAndProject(User user, Project project);

    boolean existsByUserAndProject(User user, Project project);

    @Query("SELECT a.project FROM Appreciation a WHERE a.user.userId = :userId")
    Page<Project> findProjectsByUserId(UUID userId, Pageable pageable);

}
