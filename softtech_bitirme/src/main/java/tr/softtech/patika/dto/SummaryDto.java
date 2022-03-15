package tr.softtech.patika.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class SummaryDto {
    private String categoryType;
    private BigDecimal kdvRate;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private BigDecimal avaragePrice;
    private BigDecimal Stock;
}
