package giadatonni.GENERA._BE.repositories;

import giadatonni.GENERA._BE.entities.Appreciation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AppreciationsRepository extends JpaRepository<Appreciation, UUID> {
}
