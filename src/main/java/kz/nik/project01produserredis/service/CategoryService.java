package kz.nik.project01produserredis.service;

import kz.nik.project01produserredis.mapper.UserMapper;
import kz.nik.project01produserredis.model.Category;
import kz.nik.project01produserredis.repository.CategoryRepository;
import kz.nik.project01produserredis.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final CacheService cacheService;



    public Category createCategory(String name) {
        if (categoryRepository.findByName(name).isPresent()) {
            throw new IllegalArgumentException("Category already exists");
        }
        return categoryRepository.save(Category.builder().name(name).build());
    }


    public List<Category> getAllCategories() {
        String cacheKey = "all_categories";
        List<Category> cachedCategories = (List<Category>) cacheService.getCachedObject(cacheKey);
        if (cachedCategories != null) {
            return cachedCategories;
        }
        List<Category> allCategories = categoryRepository.findAll();
        cacheService.cacheObject(cacheKey, allCategories, 1, TimeUnit.DAYS);
        return allCategories;
    }


    public void deleteCategory(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new IllegalArgumentException("Category not found");
        }
        categoryRepository.deleteById(id);
    }


    public Category getCategoryByName(String name) {
        String cacheKey = "category_" + name;
        Category cachedCategory = (Category) cacheService.getCachedObject(cacheKey);
        if (cachedCategory != null) {
            return cachedCategory;
        }

        Category category = categoryRepository.findByName(name).orElse(null);
        if (category != null) {
            cacheService.cacheObject(cacheKey, category, 1, TimeUnit.DAYS);
        }
        return category;
    }


    public Category updateCategory(Long id, String name) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));

        category.setName(name);
        return categoryRepository.save(category);
    }
}
