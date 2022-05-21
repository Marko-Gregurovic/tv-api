package fer.hr.tvapi.service.impl;

import fer.hr.tvapi.dto.CategoryDto;
import fer.hr.tvapi.dto.CreateCategoryDto;
import fer.hr.tvapi.entity.Category;
import fer.hr.tvapi.entity.Role;
import fer.hr.tvapi.entity.Users;
import fer.hr.tvapi.exception.ConflictException;
import fer.hr.tvapi.repository.CategoryRepository;
import fer.hr.tvapi.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
class CategoryServiceImplTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private UserService userService;

    @Mock
    Principal principal;

    CategoryServiceImpl categoryService;

    @BeforeEach
    public void setUp() {
        categoryService = new CategoryServiceImpl(categoryRepository, userService);
    }

    @Test
    public void Given_Valid_Input_When_Create_Category_Should_Return_Category() {
        // Given
        CategoryDto expected = CategoryDto
                .builder()
                .categoryId(3L)
                .name("cat3")
                .description("desc")
                .build();

        List<Category> categoryList = new ArrayList<>();
        Category cat1 = mock(Category.class);
        when(cat1.getName()).thenReturn("cat1");
        Category cat2 = mock(Category.class);
        when(cat2.getName()).thenReturn("cat2");
        categoryList.add(cat1);
        categoryList.add(cat2);
        when(categoryRepository.findAll()).thenReturn(categoryList);

        CreateCategoryDto createCategoryDto = mock(CreateCategoryDto.class);
        when(createCategoryDto.getName()).thenReturn("cat3");
        when(createCategoryDto.getDescription()).thenReturn("desc");

        Category c3 = mock(Category.class);
        when(categoryRepository.save(any())).thenReturn(c3);
        when(c3.getName()).thenReturn("cat3");
        when(c3.getDescription()).thenReturn("desc");
        when(c3.getId()).thenReturn(3L);

        // When
        CategoryDto actual = categoryService.createCategory(principal, createCategoryDto);

        // Then
        assertEquals(expected, actual);
        verify(categoryRepository, times(1)).findAll();
        verify(categoryRepository, times(1)).save(any());
        verifyNoMoreInteractions(principal);
        verifyNoMoreInteractions(categoryRepository);
    }

    @Test
    public void When_Get_All_Categories_Should_Return_Empty(){
        // Given
        when(categoryRepository.findAll()).thenReturn(new ArrayList<>());

        // When

        List<CategoryDto> actual = categoryService.getAllCategories();

        // Then

        assertEquals(0, actual.size());
    }

    @Test
    public void When_Get_All_Categories_Should_Return_List(){
        // Given
        List<CategoryDto> excepted = new ArrayList<>();
        excepted.add(
                CategoryDto.builder()
                        .categoryId(1L)
                        .name("cat1")
                        .description("desc1")
                        .build()
        );
        excepted.add(
                CategoryDto.builder()
                        .categoryId(2L)
                        .name("cat2")
                        .description("desc2")
                        .build()
        );

        List<Category> categoryList = new ArrayList<>();
        categoryList.add(
                Category.builder()
                        .id(1L)
                        .name("cat1")
                        .description("desc1")
                        .build()
        );
        categoryList.add(
                Category.builder()
                        .id(2L)
                        .name("cat2")
                        .description("desc2")
                        .build()
        );
        when(categoryRepository.findAll()).thenReturn(categoryList);

        // When

        List<CategoryDto> actual = categoryService.getAllCategories();

        // Then

        assertEquals(excepted, actual);
    }

    @Test
    public void Given_Search_Param_When_Search_Categories_Should_Return_Empty(){
        // Given
        when(categoryRepository.findAll()).thenReturn(new ArrayList<>());

        // When

        List<CategoryDto> actual = categoryService.searchCategories("cat");

        // Then

        assertEquals(0, actual.size());
    }

    @Test
    public void Given_Search_Param_When_Search_Categories_Should_Return_Filtered_Empty(){
        // Given
        List<CategoryDto> excepted = new ArrayList<>();
        excepted.add(
                CategoryDto.builder()
                        .categoryId(1L)
                        .name("cat1")
                        .description("desc1")
                        .build()
        );
        excepted.add(
                CategoryDto.builder()
                        .categoryId(2L)
                        .name("cat2")
                        .description("desc2")
                        .build()
        );

        List<Category> categoryList = new ArrayList<>();
        categoryList.add(
                Category.builder()
                        .id(1L)
                        .name("cat1")
                        .description("desc1")
                        .build()
        );
        categoryList.add(
                Category.builder()
                        .id(2L)
                        .name("cat2")
                        .description("desc2")
                        .build()
        );
        when(categoryRepository.findAll()).thenReturn(categoryList);

        // When

        List<CategoryDto> actual = categoryService.searchCategories("nothing");

        // Then

        assertEquals(0, actual.size());
    }

    @Test
    public void Given_Search_Param_When_Search_Categories_Should_Return_Non_Empty_1(){
        // Given
        List<CategoryDto> excepted = new ArrayList<>();
        excepted.add(
                CategoryDto.builder()
                        .categoryId(1L)
                        .name("cat1")
                        .description("desc1")
                        .build()
        );
        excepted.add(
                CategoryDto.builder()
                        .categoryId(2L)
                        .name("cat2")
                        .description("desc2")
                        .build()
        );

        List<Category> categoryList = new ArrayList<>();
        categoryList.add(
                Category.builder()
                        .id(1L)
                        .name("cat1")
                        .description("desc1")
                        .build()
        );
        categoryList.add(
                Category.builder()
                        .id(2L)
                        .name("cat2")
                        .description("desc2")
                        .build()
        );
        when(categoryRepository.findAll()).thenReturn(categoryList);

        // When

        List<CategoryDto> actual = categoryService.searchCategories("ca");

        // Then

        assertEquals(excepted, actual);
    }

    @Test
    public void Given_Search_Param_When_Search_Categories_Should_Return_Non_Empty_2(){
        // Given
        List<CategoryDto> excepted = new ArrayList<>();
        excepted.add(
                CategoryDto.builder()
                        .categoryId(1L)
                        .name("cat1")
                        .description("desc1")
                        .build()
        );
        excepted.add(
                CategoryDto.builder()
                        .categoryId(2L)
                        .name("cat2")
                        .description("desc2")
                        .build()
        );

        List<Category> categoryList = new ArrayList<>();
        categoryList.add(
                Category.builder()
                        .id(1L)
                        .name("cat1")
                        .description("desc1")
                        .build()
        );
        categoryList.add(
                Category.builder()
                        .id(2L)
                        .name("cat2")
                        .description("desc2")
                        .build()
        );
        when(categoryRepository.findAll()).thenReturn(categoryList);

        // When

        List<CategoryDto> actual = categoryService.searchCategories("at");

        // Then

        assertEquals(excepted, actual);
    }

    @Test
    public void Given_Search_Param_When_Search_Categories_Should_Return_Non_Empty_3(){
        // Given
        List<CategoryDto> excepted = new ArrayList<>();
        excepted.add(
                CategoryDto.builder()
                        .categoryId(2L)
                        .name("cat2")
                        .description("desc2")
                        .build()
        );

        List<Category> categoryList = new ArrayList<>();
        categoryList.add(
                Category.builder()
                        .id(1L)
                        .name("cat1")
                        .description("desc1")
                        .build()
        );
        categoryList.add(
                Category.builder()
                        .id(2L)
                        .name("cat2")
                        .description("desc2")
                        .build()
        );
        when(categoryRepository.findAll()).thenReturn(categoryList);

        // When

        List<CategoryDto> actual = categoryService.searchCategories("2");

        // Then

        assertEquals(excepted, actual);
    }

}