package tr.softtech.patika.dto;

import lombok.Data;
import tr.softtech.patika.enums.CategoryEnum;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;

@Data
public class AddNewCategoryRequestDto {
    @NotBlank(message = "Category type cannot be blank")
    private CategoryEnum categoryType;
    @DecimalMin(value = "0.0",message = "kdv cannot be negative")
    private BigDecimal kdv_rate;
}
