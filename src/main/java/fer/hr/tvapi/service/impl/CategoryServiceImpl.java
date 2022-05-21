package fer.hr.tvapi.service.impl;

import fer.hr.tvapi.dto.CategoryDto;
import fer.hr.tvapi.dto.CreateCategoryDto;
import fer.hr.tvapi.entity.Category;
import fer.hr.tvapi.entity.Users;
import fer.hr.tvapi.exception.ConflictException;
import fer.hr.tvapi.exception.ForbiddenException;
import fer.hr.tvapi.mapper.CategoryMapper;
import fer.hr.tvapi.repository.CategoryRepository;
import fer.hr.tvapi.service.CategoryService;
import fer.hr.tvapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final UserService userService;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository, UserService userService) {
        this.categoryRepository = categoryRepository;
        this.userService = userService;
    }

    @Override
    public CategoryDto createCategory(Principal principal, CreateCategoryDto createCategoryDto) {
        List<Category> allCategories = categoryRepository.findAll();
        if (allCategories.stream().anyMatch(category -> category.getName().equals(createCategoryDto.getName()))) {
            throw new ConflictException(String.format("Category with name %s, already exists", createCategoryDto.getName()));
        }

        Category categoryToSave = Category.builder()
                .name(createCategoryDto.getName())
                .description(createCategoryDto.getDescription())
                .build();

        Category savedCategory = categoryRepository.save(categoryToSave);

        return CategoryMapper.mapToCategoryDto(savedCategory);
    }

    @Override
    public List<CategoryDto> getAllCategories() {
        return CategoryMapper.mapToCategoryDtosList(categoryRepository.findAll());
    }

    @Override
    public List<CategoryDto> searchCategories(String categoryName) {
        List<CategoryDto> categoryDtoList = getAllCategories();

        return categoryDtoList
                .stream()
                .filter(channelDto -> channelDto.getName().toLowerCase().contains(categoryName.toLowerCase()))
                .collect(Collectors.toList());
    }
}
