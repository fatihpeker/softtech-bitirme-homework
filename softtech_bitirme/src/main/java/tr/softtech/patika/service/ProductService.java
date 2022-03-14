package tr.softtech.patika.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import tr.softtech.patika.converter.ProductConverter;
import tr.softtech.patika.converter.ProductMapper;
import tr.softtech.patika.dto.AddNewProductRequestDto;
import tr.softtech.patika.dto.ProductDto;
import tr.softtech.patika.dto.UpdateProductRequestDto;
import tr.softtech.patika.exception.ItemAlreadyExistException;
import tr.softtech.patika.exception.ItemNotFoundException;
import tr.softtech.patika.model.CategoryKdv;
import tr.softtech.patika.model.Product;
import tr.softtech.patika.repository.ProductRepository;

import java.math.BigDecimal;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    private final ProductConverter productConverter;

    private final CategoryKdvService categoryKdvService;

    private final BaseEntityFieldService baseEntityFieldService;

    public ProductDto addNewProduct(AddNewProductRequestDto addNewProductRequestDto){
        if (isExistByBarcode(addNewProductRequestDto.getBarcode())){
            throw new ItemAlreadyExistException("Product already exist");
        }
        Product product = productConverter.addNewProductRequestDtoToProduct(addNewProductRequestDto);
        product.setPriceWithKdv(categoryKdvService.calculetPriceWithKdv(product.getPriceWithoutKdv(),product.getCategoryKdv()));
        baseEntityFieldService.addBaseEntityProperties(product);
        return ProductMapper.INSTANCE.productToProductDto(productRepository.save(product));
    }

    public void deleteProduct(String product_id){
        if (!productRepository.existsByProductId(product_id)) {
           throw new ItemNotFoundException("product not found");
        }
        productRepository.deleteById(product_id);
    }

    public ProductDto updateProduct(UpdateProductRequestDto updateProductRequestDto){
        Product product = productConverter.updateProductRequestDtoToProduct(updateProductRequestDto);
        Product productFromRepository = productRepository.getById(updateProductRequestDto.getProductId());
        if (updateProductRequestDto.getBarcode()!=null){
            //Eğer barkod numarası eskisi ile aynı değil ve başka bir ürünün barkod numarasını gösteriyor ise hata fırlat
            if (isExistByBarcode(updateProductRequestDto.getBarcode()) && !Objects.equals(productFromRepository.getBarcode(), updateProductRequestDto.getBarcode())){
                throw new ItemAlreadyExistException("barcode already exist");
            }
            product.setBarcode(updateProductRequestDto.getBarcode());
        }
        baseEntityFieldService.addBaseEntityProperties(product);
        return ProductMapper.INSTANCE.productToProductDto(productRepository.save(product));
    }

    public ProductDto updateProductPrice(String productId,BigDecimal price){
        if (!productRepository.existsByProductId(productId)) {
            throw new ItemNotFoundException("product not found");
        }
        Product product = productRepository.getById(productId);
        product.setPriceWithoutKdv(price);
        product.setPriceWithKdv(categoryKdvService.calculetPriceWithKdv(price,product.getCategoryKdv()));
        baseEntityFieldService.addBaseEntityProperties(product);
        return ProductMapper.INSTANCE.productToProductDto(productRepository.save(product));
    }

    public ProductDto updateProductStock(String productId,BigDecimal stock){
        if (!productRepository.existsByProductId(productId)) {
            throw new ItemNotFoundException("product not found");
        }
        Product product = productRepository.getById(productId);
        product.setStock(stock);
        baseEntityFieldService.addBaseEntityProperties(product);
        return ProductMapper.INSTANCE.productToProductDto(productRepository.save(product));
    }

    public Page getAllProduct(int pageNumber, int pageSize){
        Pageable pageable = PageRequest.of(pageNumber,pageSize);
        Page<Product> page = productRepository.findAll(pageable);
        Page<ProductDto> dtoPage = page.map(ProductMapper.INSTANCE::productToProductDto);
        return dtoPage;
    }

    public Page getAllProductByCategory(int pageNumber, int pageSize, String categoryType){
        CategoryKdv categoryKdv = categoryKdvService.getCategoryKdvByCategoryType(categoryType);
        Pageable pageable = PageRequest.of(pageNumber,pageSize);
        Page<Product> page = productRepository.findAllByCategoryKdv(pageable,categoryKdv);
        Page<ProductDto> dtoPage = page.map(ProductMapper.INSTANCE::productToProductDto);
        return dtoPage;
    }

    public Page getAllProductPrice(int pageNumber, int pageSize, BigDecimal minPrice, BigDecimal macPrice){
        Pageable pageable = PageRequest.of(pageNumber,pageSize);
        Page<Product> page = productRepository.findAllByPriceWithKdvBetween(pageable,minPrice,macPrice);
        Page<ProductDto> dtoPage = page.map(ProductMapper.INSTANCE::productToProductDto);
        return dtoPage;
    }

    public Page getAllProductByPriceAndCategory(int pageNumber, int pageSize, String categoryType, BigDecimal minPrice, BigDecimal macPrice){
        CategoryKdv categoryKdv = categoryKdvService.getCategoryKdvByCategoryType(categoryType);
        Pageable pageable = PageRequest.of(pageNumber,pageSize);
        Page<Product> page = productRepository.findAllByCategoryKdvAndPriceWithKdvBetween(pageable,categoryKdv,minPrice,macPrice);
        Page<ProductDto> dtoPage = page.map(ProductMapper.INSTANCE::productToProductDto);
        return dtoPage;
    }


    public boolean isExistByBarcode(String barcode){
        return productRepository.existsByBarcode(barcode);
    }

}
