package fer.hr.tvapi.service;

import fer.hr.tvapi.dto.CategoryDto;
import fer.hr.tvapi.dto.CreateCategoryDto;
import org.springframework.http.ResponseEntity;

import java.security.Principal;
import java.util.List;

public interface CategoryService {
    CategoryDto createCategory(Principal principal, CreateCategoryDto createCategoryDto);

    List<CategoryDto> getAllCategories();

    List<CategoryDto> searchCategories(String categoryName);

    CategoryDto updateCategory(Long categoryId, CreateCategoryDto createCategoryDto);

}
