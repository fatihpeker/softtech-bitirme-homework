package tr.softtech.patika.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.GenericGenerator;
import tr.softtech.patika.enums.CategoryEnum;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "CATEGORY")
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Category extends BaseEntity{

    @Id
    @Column(name = "category_id")
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private String categoryId;

    //TODO : yeni kategoriler eklemek gerekiyor ise enum kullanmak efektif değil gibi. Düşün bunu!!!!
    @Enumerated(EnumType.STRING)
    @Column(name = "categoryType",length = 30,unique = true)
    private CategoryEnum categoryType;

    @Column(name = "kdv_rate",precision = 19,scale = 3)
    private BigDecimal kdv_rate;

//    @OneToMany(mappedBy = "categoryKdv",fetch = FetchType.LAZY)
//    private Set<Product> productSet = new HashSet<>();
}
