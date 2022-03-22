package tr.softtech.patika.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import tr.softtech.patika.IntegrationTestSupport;
import tr.softtech.patika.dto.AddNewCategoryRequestDto;
import tr.softtech.patika.enums.CategoryEnum;
import tr.softtech.patika.model.Category;


import java.math.BigDecimal;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CategoryControllerIT extends IntegrationTestSupport {

    private final String baseUrl = "http://localhost:8080/api/v1/category";

    @Test
    public void testAddNewCategory_whenCategoryAlreadyExist_shouldThrow409ItemAlreadyExistException() throws Exception {
        Category category = getCategory();
        categoryRepository.save(category);
        AddNewCategoryRequestDto request = AddNewCategoryRequestDto.builder()
                .categoryType(CategoryEnum.GIDA)
                .kdv_rate(new BigDecimal(1))
                .build();

        this.mockMvc.perform(post(baseUrl).contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writer().withDefaultPrettyPrinter().writeValueAsString(request)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.data.description",is("uri=/api/v1/category")))
                .andExpect(jsonPath("$.messages",is("This category already exist")))
                .andExpect(jsonPath("$.success",is(false)));

        List<Category> categoryList = categoryRepository.findAll();
        assertEquals(1,categoryList.size());
    }

    @Test
    public void testAddNewCategory_whenCategoryIsValid_shouldAddCategoryAndReturnCategoryDto() throws Exception {
        AddNewCategoryRequestDto request = AddNewCategoryRequestDto.builder()
                .categoryType(CategoryEnum.GIDA)
                .kdv_rate(new BigDecimal(1))
                .build();

        this.mockMvc.perform(post(baseUrl).contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writer().withDefaultPrettyPrinter().writeValueAsString(request)))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.data.categoryType",is(request.getCategoryType().toString())))
                .andExpect(jsonPath("$.success",is(true)));

        List<Category> categoryList = categoryRepository.findAll();
        assertEquals(1,categoryList.size());
    }

    @Test
    public void testDeleteCategory_whenCategoryNotExist_shouldThrow404ItemNotFoundException() throws Exception {
        Category category = getCategory();
        categoryRepository.save(category);
        String categoryId = "anonym";

        this.mockMvc.perform(delete(baseUrl + "?categoryId=" + categoryId ).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.data.description",is("uri=/api/v1/category")))
                .andExpect(jsonPath("$.messages",is("Category Not Found")))
                .andExpect(jsonPath("$.success",is(false)));

        List<Category> categoryList = categoryRepository.findAll();
        assertEquals(1,categoryList.size());
    }

    @Test
    public void testDeleteCategory_whenCategoryExist_shouldDeleteCategory() throws Exception {
        Category category = getCategory();
        Category save = categoryRepository.save(category);

        this.mockMvc.perform(delete(baseUrl + "?categoryId=" + save.getCategoryId() ).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.data",is("void")))
                .andExpect(jsonPath("$.messages",is("Category deleted successfully")))
                .andExpect(jsonPath("$.success",is(true)));

        List<Category> categoryList = categoryRepository.findAll();
        assertEquals(0,categoryList.size());
    }

    private Category getCategory() {
        return Category.builder()
                .categoryId("categoryId")
                .categoryType(CategoryEnum.GIDA)
                .kdv_rate(new BigDecimal(1))
                .build();
    }

}