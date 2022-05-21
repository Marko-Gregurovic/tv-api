package fer.hr.tvapi.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import fer.hr.tvapi.TvApiApplication;
import fer.hr.tvapi.dto.CategoryDto;
import fer.hr.tvapi.dto.CreateCategoryDto;
import fer.hr.tvapi.service.CategoryService;
import fer.hr.tvapi.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TvApiApplication.class)
@AutoConfigureMockMvc(addFilters = false)
class CategoryControllerTest {

    @Autowired
    CategoryController categoryController;

    @MockBean
    UserService userService;

    @MockBean
    Principal principal;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void Get_Categoryes() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/categories"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        String jsonContent = mvcResult.getResponse().getContentAsString();
        List<CategoryDto> categoryResponse = objectMapper.readValue(jsonContent, new TypeReference<List<CategoryDto>>() {
        });

        assertNotNull(categoryResponse);
        assertFalse(categoryResponse.isEmpty());

        categoryResponse.forEach(categoryDto -> {
                    assertNotNull(categoryDto.getCategoryId());
                    assertNotNull(categoryDto.getName());
                }
        );

    }

    @Test
    void Create_Category() throws Exception {
        CreateCategoryDto createCategoryDto = CreateCategoryDto.builder()
                .name("new category")
                .description("description")
                .build();

        String jsonBodyRequest = objectMapper.writeValueAsString(createCategoryDto);

        MvcResult mvcResult = mockMvc.perform(post("/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBodyRequest)
                .characterEncoding(StandardCharsets.UTF_8)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();
        CategoryDto createCategoryResponse = objectMapper.readValue(jsonResponse, CategoryDto.class);
        assertNotNull(createCategoryResponse);
        assertNotNull(createCategoryResponse.getCategoryId());
        assertEquals("new category", createCategoryResponse.getName());
        assertEquals("description", createCategoryResponse.getDescription());
    }

}