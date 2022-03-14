package tr.softtech.patika.converter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tr.softtech.patika.dto.AddNewProductRequestDto;
import tr.softtech.patika.dto.UpdateProductRequestDto;
import tr.softtech.patika.model.Product;
import tr.softtech.patika.service.CategoryKdvService;

@Service
@RequiredArgsConstructor
public class ProductConverter {

    private final CategoryKdvService categoryKdvService;

    public Product addNewProductRequestDtoToProduct(AddNewProductRequestDto addNewProductRequestDto){
        Product product = Product.builder()
                .name(addNewProductRequestDto.getName())
                .description(addNewProductRequestDto.getDescription())
                .barcode(addNewProductRequestDto.getBarcode())
                .categoryKdv(categoryKdvService.getCategoryKdvByCategoryType(addNewProductRequestDto.getCategoryKdvType()))
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
                .categoryKdv(categoryKdvService.getCategoryKdvByCategoryType(updateProductRequestDto.getCategoryKdvType()))
                .priceWithoutKdv(updateProductRequestDto.getPriceWithoutKdv())
                .priceWithKdv(categoryKdvService.calculetPriceWithKdv(
                        updateProductRequestDto.getPriceWithoutKdv(),
                        categoryKdvService.getCategoryKdvByCategoryType(updateProductRequestDto.getCategoryKdvType())
                ))
                .stock(updateProductRequestDto.getStock())
                .build();
        return product;
    }

}
