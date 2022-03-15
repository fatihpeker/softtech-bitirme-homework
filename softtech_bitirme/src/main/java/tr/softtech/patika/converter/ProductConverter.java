package tr.softtech.patika.converter;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import tr.softtech.patika.dto.AddNewProductRequestDto;
import tr.softtech.patika.dto.UpdateProductRequestDto;
import tr.softtech.patika.model.Product;
import tr.softtech.patika.service.CategoryService;
import tr.softtech.patika.service.ProductService;

@Service
@RequiredArgsConstructor
public class ProductConverter {

    private final CategoryService categoryService;

    private  ProductService productService;

    @Autowired
    public ProductConverter(CategoryService categoryService, @Lazy ProductService productService) {
        this.categoryService = categoryService;
        this.productService = productService;
    }

    public Product addNewProductRequestDtoToProduct(AddNewProductRequestDto addNewProductRequestDto){
        Product product = Product.builder()
                .name(addNewProductRequestDto.getName())
                .description(addNewProductRequestDto.getDescription())
                .barcode(addNewProductRequestDto.getBarcode())
                .category(categoryService.getCategoryByCategoryType(addNewProductRequestDto.getCategoryKdvType()))
                .priceWithoutKdv(addNewProductRequestDto.getPriceWithoutKdv())
                .stock(addNewProductRequestDto.getStock())
                .build();
        return product;
    }

    public Product updateProductRequestDtoToProduct(UpdateProductRequestDto updateProductRequestDto){
        Product product = Product.builder()
                .productId(updateProductRequestDto.getProductId())
                .name(updateProductRequestDto.getName())
                .description(updateProductRequestDto.getDescription())
                .category(categoryService.getCategoryByCategoryType(updateProductRequestDto.getCategoryKdvType()))
                .priceWithoutKdv(updateProductRequestDto.getPriceWithoutKdv())
                .priceWithKdv(productService.calculetPriceWithKdv(
                        updateProductRequestDto.getPriceWithoutKdv(),
                        categoryService.getCategoryByCategoryType(updateProductRequestDto.getCategoryKdvType())
                ))
                .stock(updateProductRequestDto.getStock())
                .build();
        return product;
    }

}
