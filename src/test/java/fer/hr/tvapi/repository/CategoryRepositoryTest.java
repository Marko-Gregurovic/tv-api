package fer.hr.tvapi.repository;

import fer.hr.tvapi.TvApiApplication;
import fer.hr.tvapi.entity.Category;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TvApiApplication.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CategoryRepositoryTest {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    public void Should_Find_Some_Categoryes() {
        List<Category> categoryList = categoryRepository.findAll();

        assertFalse(categoryList.isEmpty());
    }

    @Test
    public void Should_Create_Category() {
        Category category = categoryRepository.save(
                Category.builder()
                        .name("cat1")
                        .description("desc1")
                        .build()
        );

        assertEquals("cat1", category.getName());
        assertEquals("desc1", category.getDescription());
        assertNotNull(category.getId());
    }

}