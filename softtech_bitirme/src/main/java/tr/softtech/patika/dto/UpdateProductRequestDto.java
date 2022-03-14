package tr.softtech.patika.dto;

import lombok.Data;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;

@Data
public class UpdateProductRequestDto {
    @NotBlank(message = "id cannot be blank")
    private String productId;
    private String name;
    private String description;
    private String barcode;
    private String categoryKdvType;
    @DecimalMin(value = "0.0",message = "Price cannot be negative")
    private BigDecimal priceWithoutKdv;
    @DecimalMin(value = "0.0",message = "Stock cannot be negative")
    private BigDecimal stock;
}
