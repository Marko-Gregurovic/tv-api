package fer.hr.tvapi.mapper;

import fer.hr.tvapi.dto.CategoryDto;
import fer.hr.tvapi.entity.Category;

import java.util.List;
import java.util.stream.Collectors;

public class CategoryMapper {

    public static CategoryDto mapToCategoryDto(Category category) {
        return CategoryDto.builder()
                .categoryId(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .build();
    }

    public static List<CategoryDto> mapToCategoryDtosList(List<Category> categoryList){
        return categoryList.stream().map(CategoryMapper::mapToCategoryDto).collect(Collectors.toList());
    }

}
