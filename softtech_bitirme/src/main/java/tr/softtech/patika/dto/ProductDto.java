package tr.softtech.patika.dto;

import lombok.Data;
import tr.softtech.patika.model.CategoryKdv;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.DecimalMin;
import java.math.BigDecimal;

@Data
public class ProductDto {
    private String productId;
    private String name;
    private String description;
    private String barcode;
    private CategoryKdv categoryKdv;
    private BigDecimal priceWithKdv;
    private BigDecimal stock;
}
