package giadatonni.GENERA._BE.repositories;


import giadatonni.GENERA._BE.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RolesRepository extends JpaRepository<Role, String> {
}
