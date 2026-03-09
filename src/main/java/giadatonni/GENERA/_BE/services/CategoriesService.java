package giadatonni.GENERA._BE.services;

import giadatonni.GENERA._BE.entities.Category;
import giadatonni.GENERA._BE.exceptions.BadRequestException;
import giadatonni.GENERA._BE.exceptions.NotFoundException;
import giadatonni.GENERA._BE.payloads.CategoryDTO;
import giadatonni.GENERA._BE.repositories.CategoriesRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoriesService {

    private final CategoriesRepository categoriesRepository;

    public CategoriesService(CategoriesRepository categoriesRepository) {
        this.categoriesRepository = categoriesRepository;
    }

    public List<Category> findAllCategories() {
        return this.categoriesRepository.findAll();
    }

    public Category findCategoryById(String category) {
        return this.categoriesRepository.findById(category).orElseThrow(() -> new NotFoundException(category));
    }

    public Category addCategory(CategoryDTO body) {
        if (this.categoriesRepository.existsById(body.category())) throw new BadRequestException("Existing category");

        Category newCategory = new Category(body.category());

        this.categoriesRepository.save(newCategory);

        System.out.println("Added category: " + newCategory.getCategory());

        return newCategory;
    }

    public void deleteCategory(String categoryId) {
        Category category = this.findCategoryById(categoryId);

        this.categoriesRepository.delete(category);

        System.out.println("Category deleted");
    }
}
