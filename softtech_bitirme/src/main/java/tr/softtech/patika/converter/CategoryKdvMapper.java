package tr.softtech.patika.converter;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import tr.softtech.patika.dto.AddNewCategoryRequestDto;
import tr.softtech.patika.dto.CategoryKdvDto;
import tr.softtech.patika.model.CategoryKdv;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CategoryKdvMapper {

    CategoryKdvMapper INSTANCE = Mappers.getMapper(CategoryKdvMapper.class);

    CategoryKdv addNewCategoryRequestToCategoryKdv(AddNewCategoryRequestDto addNewCategoryRequestDto);

    CategoryKdvDto categoryoKdvToCategoryKdvDto(CategoryKdv categoryKdv);

}
