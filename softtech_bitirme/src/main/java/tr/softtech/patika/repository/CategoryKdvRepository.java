package tr.softtech.patika.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tr.softtech.patika.enums.CategoryEnum;
import tr.softtech.patika.model.CategoryKdv;

import java.util.Optional;

public interface CategoryKdvRepository extends JpaRepository<CategoryKdv,String> {

    boolean existsByCategoryType(CategoryEnum categoryEnum);

    Optional<CategoryKdv> findByCategoryType(CategoryEnum categoryType);

}
