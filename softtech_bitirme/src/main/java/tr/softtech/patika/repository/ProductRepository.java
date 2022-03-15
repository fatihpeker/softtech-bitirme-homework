package tr.softtech.patika.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tr.softtech.patika.model.Category;
import tr.softtech.patika.model.Product;

import java.math.BigDecimal;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product,String> {

    boolean existsByBarcode(String barcode);

    boolean existsByProductId(String product_id);

    Page findAllByCategory(Pageable pageable, Category category);

    Page findAllByPriceWithKdvBetween(Pageable pageable, BigDecimal minPrice, BigDecimal macPrice);

    Page findAllByCategoryAndPriceWithKdvBetween(Pageable pageable, Category category, BigDecimal minPrice, BigDecimal macPrice);

    @Query(value = "select avg(p.price_with_kdv) from Product p where p.category_id = :#{#category.categoryId} ",nativeQuery = true)
    BigDecimal getAvarageForPrice(@Param("category") Category category);

    @Query(value = "select max(p.price_with_kdv) from Product p where p.category_id = :#{#category.categoryId}",nativeQuery = true)
    BigDecimal getMaxValueByCategory(@Param("category") Category category);

    @Query(value = "select min(p.price_with_kdv) from Product p where p.category_id = :#{#category.categoryId}",nativeQuery = true)
    BigDecimal getMinValueByCategory(@Param("category") Category category);

    @Query(value = "select sum(p.stock) from Product p where p.category_id = :#{#category.categoryId}",nativeQuery = true)
    BigDecimal getStockSumCategory(@Param("category") Category category);

    List<Product> findAllByCategory(Category category);
}
