package tr.softtech.patika.dto;

import lombok.Data;
import tr.softtech.patika.enums.CategoryEnum;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.math.BigDecimal;

@Data
public class CategoryKdvDto {
    private String categoryKdvId;
    private CategoryEnum categoryType;
    private BigDecimal kdv_rate;
}
