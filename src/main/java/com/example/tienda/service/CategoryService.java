package com.example.tienda.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.tienda.dto.CategoryRequest;
import com.example.tienda.dto.CategoryResponse;
import com.example.tienda.exception.ResourceNotFoundException;
import com.example.tienda.model.Category;
import com.example.tienda.repository.CategoryRepository;

@Service
public class CategoryService {

    private CategoryRepository repo;

    public CategoryService(CategoryRepository repo) {
        this.repo = repo;
    }

    public List<CategoryResponse> getAll() {
        List<Category> categories = repo.findAll();
        List<CategoryResponse> responses = new ArrayList<>();

        for (Category category : categories) {
            responses.add(mapToResponse(category));
        }

        return responses;
    }

    public Optional<CategoryResponse> findById(Long id) {
        Optional<Category> optionalCategory = repo.findById(id);

        if (optionalCategory.isPresent()) {
            Category category = optionalCategory.get();
            return Optional.of(mapToResponse(category));
        }

        return Optional.empty();
    }

    public CategoryResponse save(CategoryRequest request) {
        Category category = new Category();
        category.setName(request.name());

        Category savedCategory = repo.save(category);
        return mapToResponse(savedCategory);
    }

    public CategoryResponse update(long id, CategoryRequest request) {
        Category categoryDb = repo.findById(id).orElseThrow(
            () -> new ResourceNotFoundException("Category not found with id: " + id)
        );

        categoryDb.setName(request.name());
        return mapToResponse(repo.save(categoryDb));
    }

    public Boolean delete(long id) {
        if (!repo.existsById(id)) {
            return false;
        }
        repo.deleteById(id);
        return true;
    }

    // Método privado para mapear Entity a DTO
    private CategoryResponse mapToResponse(Category category) {
        return new CategoryResponse(
            category.getId(),
            category.getName()
        );
    }
}