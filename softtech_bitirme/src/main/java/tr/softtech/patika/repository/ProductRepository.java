package tr.softtech.patika.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import tr.softtech.patika.model.CategoryKdv;
import tr.softtech.patika.model.Product;

import java.math.BigDecimal;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product,String> {

    boolean existsByBarcode(String barcode);

    boolean existsByProductId(String product_id);

    Page findAllByCategoryKdv(Pageable pageable, CategoryKdv categoryKdv);

    Page findAllByPriceWithKdvBetween(Pageable pageable, BigDecimal minPrice, BigDecimal macPrice);

    Page findAllByCategoryKdvAndPriceWithKdvBetween(Pageable pageable, CategoryKdv categoryKdv, BigDecimal minPrice, BigDecimal macPrice);
}
