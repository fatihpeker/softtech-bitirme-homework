package tr.softtech.patika.dto;

import lombok.Builder;
import lombok.Data;
import tr.softtech.patika.enums.CategoryEnum;

import java.math.BigDecimal;

@Data
@Builder
public class CategoryDto {
    private String categoryId;
    private CategoryEnum categoryType;
    private BigDecimal kdv_rate;
}
