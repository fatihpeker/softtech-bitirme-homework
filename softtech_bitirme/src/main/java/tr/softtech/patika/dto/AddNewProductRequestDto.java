package tr.softtech.patika.dto;

import lombok.Data;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;

@Data
public class AddNewProductRequestDto {

    @NotBlank(message = "Name may not be blank")
    private String name;
    private String description;
    @NotBlank(message = "Barcode may not be blank")
    private String barcode;
    @NotBlank(message = "Category may not be blank")
    private String categoryKdvType;
    @DecimalMin(value = "0.1",message = "Price cannot be negative")
    private BigDecimal priceWithoutKdv;
    @DecimalMin(value = "0.0",message = "Stock cannot be negative")
    private BigDecimal stock;
}
