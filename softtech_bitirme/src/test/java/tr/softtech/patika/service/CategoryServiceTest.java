package tr.softtech.patika.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import tr.softtech.patika.dto.AddNewCategoryRequestDto;
import tr.softtech.patika.dto.CategoryDto;
import tr.softtech.patika.enums.CategoryEnum;
import tr.softtech.patika.exception.ItemAlreadyExistException;
import tr.softtech.patika.exception.ItemNotFoundException;
import tr.softtech.patika.model.Category;
import tr.softtech.patika.repository.CategoryRepository;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

class CategoryServiceTest {

    private Authentication authentication;
    private CategoryRepository categoryRepository;
    private BaseEntityFieldService baseEntityFieldService;

    private CategoryService categoryService;

    @BeforeEach
    void setUp() {
        authentication = Mockito.mock(Authentication.class);
        categoryRepository = Mockito.mock(CategoryRepository.class);
        baseEntityFieldService = Mockito.mock(BaseEntityFieldService.class);

        categoryService = new CategoryService(categoryRepository,baseEntityFieldService);

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Test
    public void testAddNewCategory_whenCategoryTypeAlreadyExist_shouldThrowItemAlreadyExistException(){
        AddNewCategoryRequestDto addNewCategoryRequestDto = AddNewCategoryRequestDto.builder()
                .categoryType(CategoryEnum.GIDA)
                .kdv_rate(new BigDecimal(0.1))
                .build();

        Mockito.when(categoryRepository.existsByCategoryType(addNewCategoryRequestDto.getCategoryType())).thenReturn(true);

        assertThrows(ItemAlreadyExistException.class, ()-> categoryService.addNewCategory(addNewCategoryRequestDto));

        Mockito.verify(categoryRepository).existsByCategoryType(addNewCategoryRequestDto.getCategoryType());
        Mockito.verifyNoInteractions(baseEntityFieldService);
    }

    @Test
    public void testAddNewCategory_whenCategoryTypeIsValid_shouldReturnCategoryDto(){
        AddNewCategoryRequestDto addNewCategoryRequestDto = AddNewCategoryRequestDto.builder()
                .categoryType(CategoryEnum.GIDA)
                .kdv_rate(new BigDecimal(1))
                .build();
        Category category = getCategory();
        CategoryDto expectedResult = getCategoryDto();

        Mockito.when(categoryRepository.existsByCategoryType(addNewCategoryRequestDto.getCategoryType())).thenReturn(false);
        Mockito.when(categoryRepository.save(any(Category.class))).thenReturn(category);

        CategoryDto result = categoryService.addNewCategory(addNewCategoryRequestDto);

        assertEquals(expectedResult,result);

        Mockito.verify(baseEntityFieldService).addBaseEntityProperties(any(Category.class));
        Mockito.verify(categoryRepository).save(any(Category.class));
    }

    @Test
    public void testDeleteCategory_whenCategoryNotExist_shouldThrowItemNotFoundException(){
        String categoryId = "categoryId";

        Mockito.when(categoryRepository.findById(categoryId)).thenThrow(ItemNotFoundException.class);

        assertThrows(ItemNotFoundException.class, ()-> categoryService.deleteCategory(categoryId));

        Mockito.verify(categoryRepository).findById(categoryId);
    }

    @Test
    public void testDeleteCategory_whenCategoryExist_shouldDeleteCategory(){
        String categoryId = "categoryId";
        Category category = getCategory();

        Mockito.when(categoryRepository.findById(categoryId)).thenReturn(Optional.ofNullable(category));

        categoryService.deleteCategory(categoryId);

        Mockito.verify(categoryRepository).findById(categoryId);
        Mockito.verify(categoryRepository).delete(category);
    }

    @Test
    public void testGetCategoryByCategoryType_whenCategoryNotExist_shouldThrowItemNotFoundException(){
        String categoryType = "GIDA";

        Mockito.when(categoryRepository.findByCategoryType(CategoryEnum.valueOf(categoryType)))
                .thenThrow(ItemNotFoundException.class);

        assertThrows(ItemNotFoundException.class, ()-> categoryService.getCategoryByCategoryType(categoryType));

        Mockito.verify(categoryRepository).findByCategoryType(CategoryEnum.valueOf(categoryType));
    }

    @Test
    public void testGetCategoryByCategoryType_whenCategoryExist_shouldReturnCategory(){
        String categoryType = "GIDA";
        Category category = getCategory();

        Mockito.when(categoryRepository.findByCategoryType(CategoryEnum.valueOf(categoryType)))
                .thenReturn(Optional.ofNullable(category));

        Category result = categoryService.getCategoryByCategoryType(categoryType);

        assertEquals(category,result);

        Mockito.verify(categoryRepository).findByCategoryType(CategoryEnum.valueOf(categoryType));
    }

    @Test
    public void testUpdateKdvRate_whenCategotyNotExist_shouldThrowItemNotFoundException(){
        String categoryType = "GIDA";
        BigDecimal kdvRate = new BigDecimal(1);

        Mockito.when(categoryRepository.findByCategoryType(CategoryEnum.valueOf(categoryType)))
                .thenThrow(ItemNotFoundException.class);

        assertThrows(ItemNotFoundException.class, ()-> categoryService.updateKdvRate(categoryType,kdvRate));

        Mockito.verify(categoryRepository).findByCategoryType(CategoryEnum.valueOf(categoryType));
        Mockito.verifyNoInteractions(baseEntityFieldService);
    }

    @Test
    public void testUpdateKdvRate_whenCategotyExist_shouldReturnCategoryDto(){
        String categoryType = "GIDA";
        BigDecimal kdvRate = new BigDecimal(1);
        CategoryDto expectedResult = getCategoryDto();
        Category category = getCategory();

        Mockito.when(categoryRepository.findByCategoryType(CategoryEnum.valueOf(categoryType)))
                .thenReturn(Optional.ofNullable(category));
        Mockito.when(categoryRepository.save(category)).thenReturn(category);

        CategoryDto result = categoryService.updateKdvRate(categoryType,kdvRate);

        assertEquals(expectedResult,result);

        Mockito.verify(categoryRepository).findByCategoryType(CategoryEnum.valueOf(categoryType));
        Mockito.verify(baseEntityFieldService).addBaseEntityProperties(category);
        Mockito.verify(categoryRepository).save(category);
    }

    private CategoryDto getCategoryDto() {
        return CategoryDto.builder()
                .categoryId("categoryId")
                .categoryType(CategoryEnum.GIDA)
                .kdv_rate(new BigDecimal(1))
                .build();
    }

    private Category getCategory() {
        return Category.builder()
                .categoryId("categoryId")
                .categoryType(CategoryEnum.GIDA)
                .kdv_rate(new BigDecimal(1))
                .createDate(Timestamp.from(Instant.parse("2020-01-01T17:10:10Z")))
                .updateDate(Timestamp.from(Instant.parse("2020-01-01T17:10:10Z")))
                .createdBy("createdBy")
                .updatedBy("updatedBy")
                .build();
    }

}