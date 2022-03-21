package tr.softtech.patika.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tr.softtech.patika.converter.CategoryMapper;
import tr.softtech.patika.dto.AddNewCategoryRequestDto;
import tr.softtech.patika.dto.CategoryDto;
import tr.softtech.patika.enums.CategoryEnum;
import tr.softtech.patika.exception.ItemAlreadyExistException;
import tr.softtech.patika.exception.ItemNotFoundException;
import tr.softtech.patika.model.Category;
import tr.softtech.patika.repository.CategoryRepository;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryService {

    private final CategoryRepository categoryRepository;

    private final BaseEntityFieldService baseEntityFieldService;


    public CategoryDto addNewCategory(AddNewCategoryRequestDto addNewCategoryRequestDto){
        if (existByCategory(addNewCategoryRequestDto.getCategoryType())){
            throw new ItemAlreadyExistException("This category already exist");
        }
        Category category = CategoryMapper.INSTANCE.addNewCategoryRequestToCategory(addNewCategoryRequestDto);
        baseEntityFieldService.addBaseEntityProperties(category);
        return CategoryMapper.INSTANCE.categoryToCategoryDto(categoryRepository.save(category));
    }

    public void deleteCategory(String categoryId){
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(()->new ItemNotFoundException("Category Not Found"));
        categoryRepository.delete(category);
    }

    public Category getCategoryByCategoryType(String categoryType){
        return categoryRepository.findByCategoryType(CategoryEnum.valueOf(categoryType)).orElseThrow(()->new ItemNotFoundException("Category not found"));
    }

    public boolean existByCategory(CategoryEnum categoryEnum){
        return categoryRepository.existsByCategoryType(categoryEnum);
    }



    public List<Category> getAllCategory(){
        return categoryRepository.findAll();
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public CategoryDto updateKdvRate(String categoryType, BigDecimal kdvRate){
        Category category = getCategoryByCategoryType(categoryType);
        category.setKdv_rate(kdvRate);
        baseEntityFieldService.addBaseEntityProperties(category);
        return CategoryMapper.INSTANCE.categoryToCategoryDto(categoryRepository.save(category));
    }

}
