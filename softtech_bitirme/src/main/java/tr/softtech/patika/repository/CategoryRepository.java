package tr.softtech.patika.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tr.softtech.patika.enums.CategoryEnum;
import tr.softtech.patika.model.Category;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category,String> {

    boolean existsByCategoryType(CategoryEnum categoryEnum);

    Optional<Category> findByCategoryType(CategoryEnum categoryType);

}
