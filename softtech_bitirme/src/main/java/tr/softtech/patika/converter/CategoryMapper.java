package tr.softtech.patika.converter;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import tr.softtech.patika.dto.AddNewCategoryRequestDto;
import tr.softtech.patika.dto.CategoryDto;
import tr.softtech.patika.model.Category;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,componentModel = "spring")
public interface CategoryMapper {

    CategoryMapper INSTANCE = Mappers.getMapper(CategoryMapper.class);

    Category addNewCategoryRequestToCategoryKdv(AddNewCategoryRequestDto addNewCategoryRequestDto);

    CategoryDto categoryToCategoryDto(Category category);

}
