package tr.softtech.patika.dto;

import lombok.Builder;
import lombok.Data;
import tr.softtech.patika.model.Category;

import java.math.BigDecimal;

@Data
@Builder
public class ProductDto {
    private String productId;
    private String name;
    private String description;
    private String barcode;
    private Category category;
    private BigDecimal priceWithKdv;
    private BigDecimal stock;
}
