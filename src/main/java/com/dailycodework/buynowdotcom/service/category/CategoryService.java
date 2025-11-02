package com.dailycodework.buynowdotcom.service.category;

import com.dailycodework.buynowdotcom.model.Category;
import com.dailycodework.buynowdotcom.repository.CategoryRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService implements ICategoryService {

    private final CategoryRepository categoryRepository; // Injecting the CategoryRepository, though not used in this stub implementation

    @Override
    public Category addCategory(Category category) { // Implementing the addCategory method
        return Optional.of(category).filter(c-> !categoryRepository.existsByName(c.getName())) // Check if category with the same name already exists
                .map(categoryRepository::save) // Save the new category if it doesn't exist
                .orElseThrow(()-> new RuntimeException(category.getName() + "Category already exists")); // Throw an exception if the category already exists
    }

    @Override
    public Category updateCategory(Category category, Long categoryId) { // Implementing the updateCategory method
        return Optional.ofNullable(findCategoryById(categoryId)).map(oldCategory -> { // Find the existing category by ID
            oldCategory.setName(category.getName()); // Update the name of the existing category
            return categoryRepository.save(oldCategory); // Save the updated category
        }).orElseThrow(() -> new EntityNotFoundException("Category not found")); // Throw an exception if the category is not found
    }

    @Override
    public void deleteCategory(Long categoryId) { // Implementing the deleteCategory method
        categoryRepository
                .findById(categoryId) // Find the category by ID
                .ifPresentOrElse(categoryRepository :: delete, ()->{ // Delete the category if found
                    throw new EntityNotFoundException("Category not found"); // Throw an exception if the category is not found
                });
    }

    @Override
    public List<Category> getAllCategories() { // Implementing the getAllCategories method
        return categoryRepository.findAll(); // Retrieve and return all categories from the repository
    }

    @Override
    public Category findCategoryByName(String name) { // Implementing the findCategoryByName method
        return categoryRepository.findByName(name); // Find and return the category by its name
    }

    @Override
    public Category findCategoryById(Long categoryId) { // Implementing the findCategoryById method
        return categoryRepository
                .findById(categoryId) // Find the category by ID
                .orElseThrow(()-> new RuntimeException("Category not found")); // Throw an exception if the category is not found
    }
}
