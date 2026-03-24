package giadatonni.GENERA._BE.repositories;

import giadatonni.GENERA._BE.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoriesRepository extends JpaRepository<Category, String> {

    boolean existsById(String category);
}
