package tr.softtech.patika.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Check;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import java.math.BigDecimal;

@Entity
@Table(name = "PRODUCT")
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Check(constraints = "price_without_kdv >= 0") //check price to can not be lower then zero
public class Product extends BaseEntity{

    @Id
    @Column(name = "product_id")
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private String productId;

    @Column(name = "name",nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "barcode",nullable = false)
    private String barcode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoryKdv_id",referencedColumnName = "categoryKdv_id")
    private CategoryKdv categoryKdv;

    @DecimalMin(value = "0.0")
    @Column(name = "price_without_kdv",nullable = false,precision = 19,scale = 3)
    private BigDecimal priceWithoutKdv;

    @DecimalMin(value = "0.0")
    @Column(name = "price_with_kdv",nullable = false,precision = 19,scale = 3)
    private BigDecimal priceWithKdv;

    @Column(name = "stock",precision = 19,scale = 3)
    private BigDecimal stock;


}
