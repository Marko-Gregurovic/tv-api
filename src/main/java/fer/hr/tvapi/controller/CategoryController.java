package fer.hr.tvapi.controller;

import fer.hr.tvapi.dto.CategoryDto;
import fer.hr.tvapi.dto.CreateCategoryDto;
import fer.hr.tvapi.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity<List<CategoryDto>> getCategories() {
        return ResponseEntity.status(HttpStatus.OK).body(categoryService.getAllCategories());
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryDto> getCategory(@PathVariable Long categoryId) {
        return ResponseEntity.status(HttpStatus.OK).body(categoryService.getById(categoryId));
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long categoryId) {
        categoryService.deleteById(categoryId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PatchMapping("/{categoryId}")
    public ResponseEntity<CategoryDto> patchCategory(@PathVariable Long categoryId, @RequestBody CreateCategoryDto createCategoryDto) {
        return ResponseEntity.status(HttpStatus.OK).body(categoryService.updateCategory(categoryId, createCategoryDto));
    }

    @GetMapping("/search/{categoryName}")
    public ResponseEntity<List<CategoryDto>> getCategories(@PathVariable String categoryName) {
        return ResponseEntity.status(HttpStatus.OK).body(categoryService.searchCategories(categoryName));
    }

    @PostMapping
    public ResponseEntity<CategoryDto> createCategory(Principal principal, @RequestBody CreateCategoryDto createCategoryDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryService.createCategory(principal, createCategoryDto));
    }


}
