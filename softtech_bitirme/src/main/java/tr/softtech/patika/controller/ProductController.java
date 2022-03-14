package tr.softtech.patika.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tr.softtech.patika.dto.AddNewProductRequestDto;
import tr.softtech.patika.dto.GenericResponseDto;
import tr.softtech.patika.dto.UpdateProductRequestDto;
import tr.softtech.patika.service.ProductService;

import javax.validation.Valid;
import javax.validation.constraints.DecimalMin;
import java.math.BigDecimal;

@RestController
@RequestMapping("api/v1/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @Operation(tags = "Get All Product",
            description = "Get All Product as page")
    @GetMapping
    public ResponseEntity getAllProduct(@RequestParam(defaultValue = "0") int pageNumber,
                                        @RequestParam(defaultValue = "5") int pageSize){
        return ResponseEntity.ok(GenericResponseDto.of(productService.getAllProduct(pageNumber,pageSize),"Product Page Taken"));
    }

    @Operation(tags = "Get All Product By Category",
    description = "Get All Product By Category as page")
    @GetMapping("category")
    public ResponseEntity getAllProductByCategory(@RequestParam(defaultValue = "0") int pageNumber,
                                                  @RequestParam(defaultValue = "5") int pageSize,
                                                  @RequestParam String categoryType){
        return ResponseEntity.ok(GenericResponseDto.of(productService.getAllProductByCategory(pageNumber,pageSize,categoryType),
                "Product taken by category"));
    }

    @Operation(tags = "Get All Product By price",
    description = "Get All Product By price as page")
    @GetMapping("price")
    public ResponseEntity getAllProductByPrice(@RequestParam(defaultValue = "0") int pageNumber,
                                               @RequestParam(defaultValue = "5") int pageSize,
                                               @RequestParam BigDecimal minPrice,
                                               @RequestParam BigDecimal maxPrice){
        return ResponseEntity.ok(GenericResponseDto.of(productService.getAllProductPrice(pageNumber,pageSize,minPrice,maxPrice),
                "Product taken by price"));
    }

    @Operation(tags = "Get All Product By price and Category",
            description = "Get All Product By price and Categor as page")
    @GetMapping("priceAndCategory")
    public ResponseEntity getAllProductByPriceAndCategory(@RequestParam(defaultValue = "0") int pageNumber,
                                               @RequestParam(defaultValue = "5") int pageSize,
                                               @RequestParam String categoryType,
                                               @RequestParam BigDecimal minPrice,
                                               @RequestParam BigDecimal maxPrice){
        return ResponseEntity.ok(GenericResponseDto.of(productService.getAllProductByPriceAndCategory(pageNumber,pageSize,categoryType,minPrice,maxPrice),
                "Product taken by price and category"));
    }

    @Operation(tags = "Add New Product")
    @PostMapping
    public ResponseEntity addNewProduct(@Valid @RequestBody AddNewProductRequestDto addNewProductRequestDto){
        return new ResponseEntity<>(GenericResponseDto.of(productService.addNewProduct(addNewProductRequestDto),"product added successfully"), HttpStatus.CREATED);
    }

    @Operation(tags = "Delete Product")
    @DeleteMapping
    public ResponseEntity deleteProduct(@RequestParam String product_id){
        productService.deleteProduct(product_id);
        return ResponseEntity.ok(GenericResponseDto.of(Void.TYPE,"product deleted successfully"));
    }

    @Operation(tags = "Update Product")
    @PutMapping()
    public ResponseEntity updateProduct(@Valid @RequestBody UpdateProductRequestDto updateProductRequestDto){
        return ResponseEntity.ok(GenericResponseDto.of(productService.updateProduct(updateProductRequestDto),"product updated"));
    }

    @Operation(tags = "Update Product Price")
    @PatchMapping("price")
    public ResponseEntity updateProductPrice(@RequestParam String productId, @RequestParam @DecimalMin("0.0") BigDecimal price){
        return ResponseEntity.ok(GenericResponseDto.of(productService.updateProductPrice(productId,price),"price updated successfully"));
    }

    @Operation(tags = "Update Product Stock")
    @PatchMapping("stock")
    public ResponseEntity updateProductStock(@RequestParam String productId, @RequestParam @DecimalMin("0.0") BigDecimal stock){
        return ResponseEntity.ok(GenericResponseDto.of(productService.updateProductStock(productId,stock),"product stock updated successfully"));
    }




}
