package tr.softtech.patika.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tr.softtech.patika.converter.CategoryKdvMapper;
import tr.softtech.patika.dto.AddNewCategoryRequestDto;
import tr.softtech.patika.dto.CategoryKdvDto;
import tr.softtech.patika.enums.CategoryEnum;
import tr.softtech.patika.exception.ItemAlreadyExistException;
import tr.softtech.patika.exception.ItemNotFoundException;
import tr.softtech.patika.model.CategoryKdv;
import tr.softtech.patika.repository.CategoryKdvRepository;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class CategoryKdvService {

    private final CategoryKdvRepository categoryKdvRepository;

    private final BaseEntityFieldService baseEntityFieldService;


    public CategoryKdvDto addNewCategory(AddNewCategoryRequestDto addNewCategoryRequestDto){
        if (existByCategory(addNewCategoryRequestDto.getCategoryType())){
            throw new ItemAlreadyExistException("This category already exist");
        }
        CategoryKdv categoryKdv = CategoryKdvMapper.INSTANCE.addNewCategoryRequestToCategoryKdv(addNewCategoryRequestDto);
        baseEntityFieldService.addBaseEntityProperties(categoryKdv);
        return CategoryKdvMapper.INSTANCE.categoryoKdvToCategoryKdvDto(categoryKdvRepository.save(categoryKdv));
    }

    public CategoryKdv getCategoryKdvByCategoryType(String categoryType){
        return categoryKdvRepository.findByCategoryType(CategoryEnum.valueOf(categoryType)).orElseThrow(()->new ItemNotFoundException("Category not found"));
    }

    public boolean existByCategory(CategoryEnum categoryEnum){
        return categoryKdvRepository.existsByCategoryType(categoryEnum);
    }

    public BigDecimal calculetPriceWithKdv(BigDecimal priceWithoutKdv,CategoryKdv categoryKdv){
        BigDecimal priceWithKdv = priceWithoutKdv.add(priceWithoutKdv.multiply(categoryKdv.getKdv_rate()));
        return priceWithKdv;
    }

}
