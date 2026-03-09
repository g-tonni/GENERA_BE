package giadatonni.GENERA._BE.controllers;

import giadatonni.GENERA._BE.entities.Category;
import giadatonni.GENERA._BE.exceptions.ValidationException;
import giadatonni.GENERA._BE.payloads.CategoryDTO;
import giadatonni.GENERA._BE.services.CategoriesService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoriesController {

    private final CategoriesService categoriesService;

    public CategoriesController(CategoriesService categoriesService) {
        this.categoriesService = categoriesService;
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPER_ADMIN')")
    public List<Category> findAllCategories() {
        return this.categoriesService.findAllCategories();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPER_ADMIN')")
    public Category addCategory(@RequestBody @Validated CategoryDTO body, BindingResult validationResults) {
        if (validationResults.hasErrors()) {
            List<String> errorsList = validationResults
                    .getFieldErrors()
                    .stream()
                    .map(error -> error.getDefaultMessage())
                    .toList();
            throw new ValidationException(errorsList);
        } else {
            return this.categoriesService.addCategory(body);
        }
    }

    @DeleteMapping("/{categoryId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPER_ADMIN')")
    public void deleteCategory(@PathVariable String categoryId) {
        this.categoriesService.deleteCategory(categoryId);
    }
}
