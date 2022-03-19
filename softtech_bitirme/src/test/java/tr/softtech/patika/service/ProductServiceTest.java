package tr.softtech.patika.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import tr.softtech.patika.converter.ProductConverter;
import tr.softtech.patika.dto.AddNewProductRequestDto;
import tr.softtech.patika.dto.CategoryDto;
import tr.softtech.patika.dto.ProductDto;
import tr.softtech.patika.dto.UpdateProductRequestDto;
import tr.softtech.patika.enums.CategoryEnum;
import tr.softtech.patika.exception.ItemAlreadyExistException;
import tr.softtech.patika.exception.ItemNotFoundException;
import tr.softtech.patika.model.Category;
import tr.softtech.patika.model.Product;
import tr.softtech.patika.repository.ProductRepository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ProductServiceTest {

    private Authentication authentication;
    private ProductRepository productRepository;
    private ProductConverter productConverter;
    private CategoryService categoryService;
    private BaseEntityFieldService baseEntityFieldService;

    private ProductService productService;

    @BeforeEach
    void setUp() {
        authentication = Mockito.mock(Authentication.class);
        productRepository = Mockito.mock(ProductRepository.class);
        productConverter = Mockito.mock(ProductConverter.class);
        categoryService = Mockito.mock(CategoryService.class);
        baseEntityFieldService = Mockito.mock(BaseEntityFieldService.class);

        productService = new ProductService(productRepository,productConverter,categoryService,baseEntityFieldService);

        SecurityContextHolder.getContext().setAuthentication(authentication);

    }

    @Test
    public void testAddNewProduct_whenBarcodeAlreadyExist_shouldThrowItemAlreadyExistException(){
        AddNewProductRequestDto addNewProductRequestDto = getAddNewProdutRequestDto();

        Mockito.when(productRepository.existsByBarcode(addNewProductRequestDto.getBarcode())).thenReturn(true);

        assertThrows(ItemAlreadyExistException.class, ()-> productService.addNewProduct(addNewProductRequestDto));

        Mockito.verify(productRepository).existsByBarcode(addNewProductRequestDto.getBarcode());
        Mockito.verifyNoInteractions(productConverter);
        Mockito.verifyNoInteractions(baseEntityFieldService);
    }

    @Test
    public void testAddNewProduct_whenAddNewProductDtoCategoryTypeNotExist_shouldThrowItemNotFoundException(){
        AddNewProductRequestDto addNewProductRequestDto = getAddNewProdutRequestDto();

        Mockito.when(productRepository.existsByBarcode(addNewProductRequestDto.getBarcode())).thenReturn(false);
        Mockito.when(productConverter.addNewProductRequestDtoToProduct(addNewProductRequestDto))
                .thenThrow(ItemNotFoundException.class);

        assertThrows(ItemNotFoundException.class, ()-> productService.addNewProduct(addNewProductRequestDto));

        Mockito.verify(productRepository).existsByBarcode(addNewProductRequestDto.getBarcode());
        Mockito.verify(productConverter).addNewProductRequestDtoToProduct(addNewProductRequestDto);
        Mockito.verifyNoInteractions(baseEntityFieldService);
    }

    @Test
    public void testAddNewProduct_whenAddNewProductDtoIsValid_shouldReturnProductDto(){
        AddNewProductRequestDto addNewProductRequestDto = getAddNewProdutRequestDto();
        Category category = getCategory();
        Product product = getProduct(category);
        ProductDto expectedResult = getProductDto(category);

        Mockito.when(productRepository.existsByBarcode(addNewProductRequestDto.getBarcode())).thenReturn(false);
        Mockito.when(productConverter.addNewProductRequestDtoToProduct(addNewProductRequestDto))
                .thenReturn(product);
        Mockito.when(productRepository.save(product)).thenReturn(product);

        ProductDto result = productService.addNewProduct(addNewProductRequestDto);

        assertEquals(expectedResult,result);

        Mockito.verify(productRepository).existsByBarcode(addNewProductRequestDto.getBarcode());
        Mockito.verify(productConverter).addNewProductRequestDtoToProduct(addNewProductRequestDto);
        Mockito.verify(baseEntityFieldService).addBaseEntityProperties(product);
    }

    @Test
    public void testDeleteProduct_whenProductIdNotExist_shouldThrowItemNotFoundException(){
        String productId = "productId";

        Mockito.when(productRepository.existsByProductId(productId)).thenThrow(ItemNotFoundException.class);

        assertThrows(ItemNotFoundException.class, ()-> productService.deleteProduct(productId));

        Mockito.verify(productRepository).existsByProductId(productId);
    }

    @Test
    public void testDeleteProduct_whenProductIdIsExist_shouldDeleteProduct(){
        String productId = "productId";

        Mockito.when(productRepository.existsByProductId(productId)).thenReturn(true);

        productService.deleteProduct(productId);

        Mockito.verify(productRepository).existsByProductId(productId);
        Mockito.verify(productRepository).deleteById(productId);
    }

    @Test
    public void testUpdateProduct_whenUpdateProductRequestDtoCategoryTypeNotExist_shouldThrowItemNotFoundException(){
        UpdateProductRequestDto updateProductRequestDto = getUpdateProductRequestDto();

        Mockito.when(productConverter.updateProductRequestDtoToProduct(updateProductRequestDto))
                .thenThrow(ItemNotFoundException.class);

        assertThrows(ItemNotFoundException.class, ()-> productService.updateProduct(updateProductRequestDto));

        Mockito.verify(productConverter).updateProductRequestDtoToProduct(updateProductRequestDto);
        Mockito.verifyNoInteractions(baseEntityFieldService);
    }

    @Test
    public void testUpdateProduct_whenProductNotExist_shouldThrowItemNotFoundException(){
        UpdateProductRequestDto updateProductRequestDto = getUpdateProductRequestDto();
        Category category = getCategory();
        Product product = getProduct(category);

        Mockito.when(productConverter.updateProductRequestDtoToProduct(updateProductRequestDto)).thenReturn(product);
        Mockito.when(productRepository.findById(updateProductRequestDto.getProductId()))
                .thenThrow(ItemNotFoundException.class);

        assertThrows(ItemNotFoundException.class, ()-> productService.updateProduct(updateProductRequestDto));

        Mockito.verify(productConverter).updateProductRequestDtoToProduct(updateProductRequestDto);
        Mockito.verify(productRepository).findById(updateProductRequestDto.getProductId());
        Mockito.verifyNoInteractions(baseEntityFieldService);
    }

    @Test
    public void testUpdateProduct_whenUpdateProductRequestDtoBarcodeAlreadyExistAndNotEqualProductBarcode_shouldThrowItemAlreadyExistException(){
        UpdateProductRequestDto updateProductRequestDto = getUpdateProductRequestDto();
        updateProductRequestDto.setBarcode("diffirentBarcode");
        Category category = getCategory();
        Product product = getProduct(category);

        Mockito.when(productConverter.updateProductRequestDtoToProduct(updateProductRequestDto)).thenReturn(product);
        Mockito.when(productRepository.findById(updateProductRequestDto.getProductId()))
                .thenReturn(Optional.ofNullable(product));
        Mockito.when(productRepository.existsByBarcode(updateProductRequestDto.getBarcode())).thenReturn(true);

        assertThrows(ItemAlreadyExistException.class, ()-> productService.updateProduct(updateProductRequestDto));

        Mockito.verify(productConverter).updateProductRequestDtoToProduct(updateProductRequestDto);
        Mockito.verify(productRepository).findById(updateProductRequestDto.getProductId());
        Mockito.verify(productRepository).existsByBarcode(updateProductRequestDto.getBarcode());
        Mockito.verifyNoInteractions(baseEntityFieldService);
    }
    @Test
    public void testUpdateProduct_whenUpdateProductRequestDtoIsValid_shouldReturnProductDto(){
        UpdateProductRequestDto updateProductRequestDto = getUpdateProductRequestDto();
        Category category = getCategory();
        Product product = getProduct(category);
        ProductDto exceptedResult = getProductDto(category);

        Mockito.when(productConverter.updateProductRequestDtoToProduct(updateProductRequestDto)).thenReturn(product);
        Mockito.when(productRepository.findById(updateProductRequestDto.getProductId()))
                .thenReturn(Optional.ofNullable(product));
        Mockito.when(productRepository.existsByBarcode(updateProductRequestDto.getBarcode())).thenReturn(false);
        Mockito.when(productRepository.save(product)).thenReturn(product);

        ProductDto result = productService.updateProduct(updateProductRequestDto);

        assertEquals(exceptedResult,result);

        Mockito.verify(productConverter).updateProductRequestDtoToProduct(updateProductRequestDto);
        Mockito.verify(productRepository).findById(updateProductRequestDto.getProductId());
        Mockito.verify(productRepository).existsByBarcode(updateProductRequestDto.getBarcode());
        Mockito.verify(productRepository).save(product);
        Mockito.verify(baseEntityFieldService).addBaseEntityProperties(product);
    }

    @Test
    public void testUpdateProductPrice_whenProductIdNotExist_shouldThrowItemNotFoundException(){
        String productId = "productId";
        BigDecimal price = new BigDecimal(100);

        Mockito.when(productRepository.existsByProductId(productId)).thenReturn(false);

        assertThrows(ItemNotFoundException.class, ()-> productService.updateProductPrice(productId,price));

        Mockito.verify(productRepository).existsByProductId(productId);
        Mockito.verifyNoInteractions(baseEntityFieldService);
    }

    @Test
    public void testUpdateProductPrice_whenProductExist_shouldReturnProductDto(){
        String productId = "productId";
        BigDecimal price = new BigDecimal(100);
        Category category = getCategory();
        Product product = getProduct(category);
        ProductDto expectedResult = getProductDto(category);

        Mockito.when(productRepository.existsByProductId(productId)).thenReturn(true);
        Mockito.when(productRepository.getById(productId)).thenReturn(product);
        Mockito.when(productRepository.save(product)).thenReturn(product);

        ProductDto result = productService.updateProductPrice(productId,price);

        assertEquals(expectedResult,result);

        Mockito.verify(productRepository).existsByProductId(productId);
        Mockito.verify(productRepository).getById(productId);
        Mockito.verify(baseEntityFieldService).addBaseEntityProperties(product);
        Mockito.verify(productRepository).save(product);

    }

    @Test
    public void testUpdateProductStock_whenProductIdNotExist_shouldThrowItemNotFoundException(){
        String productId = "productId";
        BigDecimal stock = new BigDecimal(100);

        Mockito.when(productRepository.existsByProductId(productId)).thenReturn(false);

        assertThrows(ItemNotFoundException.class, ()-> productService.updateProductStock(productId,stock));

        Mockito.verify(productRepository).existsByProductId(productId);
        Mockito.verifyNoInteractions(baseEntityFieldService);
    }

    @Test
    public void testUpdateProductStock_whenProductExist_shouldReturnProductDto(){
        String productId = "productId";
        BigDecimal stock = new BigDecimal(100);
        Category category = getCategory();
        Product product = getProduct(category);
        ProductDto expectedResult = getProductDto(category);

        Mockito.when(productRepository.existsByProductId(productId)).thenReturn(true);
        Mockito.when(productRepository.getById(productId)).thenReturn(product);
        Mockito.when(productRepository.save(product)).thenReturn(product);

        ProductDto result = productService.updateProductStock(productId,stock);

        assertEquals(expectedResult,result);

        Mockito.verify(productRepository).existsByProductId(productId);
        Mockito.verify(productRepository).getById(productId);
        Mockito.verify(baseEntityFieldService).addBaseEntityProperties(product);
        Mockito.verify(productRepository).save(product);
    }

    @Test
    public void testGetAllProductByCategory_whenCategoryTypeNotExist_shouldThrowItemNotFoundException(){
        String categoryType = "categoryType";
        int pageNumber = 5;
        int pageSize = 5;

        Mockito.when(categoryService.getCategoryByCategoryType(categoryType)).thenThrow(ItemNotFoundException.class);

        assertThrows(ItemNotFoundException.class,
                ()-> productService.getAllProductByCategory(pageNumber,pageSize,categoryType));

        Mockito.verify(categoryService).getCategoryByCategoryType(categoryType);
        Mockito.verifyNoInteractions(productRepository);
    }

    @Test
    public void testGetAllProductByCategory_whenCategoryTypeExist_shouldReturnPage(){
        String categoryType = "categoryType";
        int pageNumber = 5;
        int pageSize = 5;
        Category category = getCategory();
        Pageable pageable = PageRequest.of(pageNumber,pageSize);
        Page page = new PageImpl(new ArrayList<>());

        Mockito.when(categoryService.getCategoryByCategoryType(categoryType)).thenReturn(category);
        Mockito.when(productRepository.findAllByCategory(pageable,category)).thenReturn(page);

        Page result = productService.getAllProductByCategory(pageNumber,pageSize,categoryType);

        assertEquals(page,result);

        Mockito.verify(categoryService).getCategoryByCategoryType(categoryType);
        Mockito.verify(productRepository).findAllByCategory(pageable,category);
    }

    @Test
    public void testGetAllProductByPriceAndCategory_whenCategoryTypeNotExist_shouldThrowItemNotFoundException(){
        String categoryType = "categoryType";
        int pageNumber = 5;
        int pageSize = 5;
        BigDecimal minPrice = new BigDecimal(10);
        BigDecimal maxPrice = new BigDecimal(100);

        Mockito.when(categoryService.getCategoryByCategoryType(categoryType)).thenThrow(ItemNotFoundException.class);

        assertThrows(ItemNotFoundException.class,
                ()-> productService.getAllProductByPriceAndCategory(pageNumber,pageSize,categoryType,minPrice,maxPrice));

        Mockito.verify(categoryService).getCategoryByCategoryType(categoryType);
        Mockito.verifyNoInteractions(productRepository);
    }

    @Test
    public void testGetAllProductByPriceAndCategory_whenCategoryTypeExist_shouldReturnPage(){
        String categoryType = "categoryType";
        int pageNumber = 5;
        int pageSize = 5;
        BigDecimal minPrice = new BigDecimal(10);
        BigDecimal maxPrice = new BigDecimal(100);
        Category category = getCategory();
        Pageable pageable = PageRequest.of(pageNumber,pageSize);
        Page page = new PageImpl(new ArrayList<>());

        Mockito.when(categoryService.getCategoryByCategoryType(categoryType)).thenReturn(category);
        Mockito.when(productRepository.findAllByCategoryAndPriceWithKdvBetween(pageable,category,minPrice,maxPrice))
                .thenReturn(page);

        Page result = productService.getAllProductByPriceAndCategory(pageNumber,pageSize,categoryType,minPrice,maxPrice);

        assertEquals(page,result);

        Mockito.verify(categoryService).getCategoryByCategoryType(categoryType);
        Mockito.verify(productRepository).findAllByCategoryAndPriceWithKdvBetween(pageable,category,minPrice,maxPrice);
    }

    @Test
    public void testUpdateKdvRate_whenCategoryTypeNotExist_shouldThrowItemNotFoundException(){
        String categoryType = "categoryType";
        BigDecimal kdvRate = new BigDecimal(0.01);

        Mockito.when(categoryService.updateKdvRate(categoryType,kdvRate)).thenThrow(ItemNotFoundException.class);

        assertThrows(ItemNotFoundException.class, ()-> productService.updateKdvRate(categoryType,kdvRate));

        Mockito.verify(categoryService).updateKdvRate(categoryType,kdvRate);
        Mockito.verifyNoInteractions(productRepository);
    }

    @Test
    public void testUpdateKdvRate_whenCategoryTypeExist_shouldReturnCategoryDto(){
        String categoryType = "categoryType";
        BigDecimal kdvRate = new BigDecimal(0.01);
        Category category = getCategory();
        CategoryDto categoryDto = CategoryDto.builder()
                .categoryId("categoryId")
                .categoryType(CategoryEnum.GIDA)
                .kdv_rate(new BigDecimal(0.01))
                .build();

        Mockito.when(categoryService.updateKdvRate(categoryType,kdvRate)).thenReturn(categoryDto);
        Mockito.when(categoryService.getCategoryByCategoryType(categoryType)).thenReturn(category);
        Mockito.when(productRepository.findAllByCategory(category)).thenReturn(new ArrayList<>());
        Mockito.when(productRepository.saveAll(new ArrayList<>())).thenReturn(new ArrayList<>());

        CategoryDto result = productService.updateKdvRate(categoryType,kdvRate);

        assertEquals(categoryDto,result);

        Mockito.verify(categoryService).updateKdvRate(categoryType,kdvRate);
        Mockito.verify(categoryService).getCategoryByCategoryType(categoryType);
        Mockito.verify(productRepository).findAllByCategory(category);
        Mockito.verify(productRepository).saveAll(new ArrayList<>());
    }

    private UpdateProductRequestDto getUpdateProductRequestDto() {
        return UpdateProductRequestDto.builder()
                .productId("productId")
                .name("name")
                .description("description")
                .barcode("barcode")
                .categoryKdvType("categoryKdvType")
                .priceWithoutKdv(new BigDecimal(100))
                .stock(new BigDecimal(100))
                .build();
    }

    private Product getProduct(Category category) {
        return Product.builder()
                .productId("productId")
                .name("name")
                .description("description")
                .barcode("barcode")
                .category(category)
                .priceWithoutKdv(new BigDecimal(100))
                .priceWithKdv(new BigDecimal(200))
                .stock(new BigDecimal(100))
                .build();
    }

    private ProductDto getProductDto(Category category) {
        return ProductDto.builder()
                .productId("productId")
                .name("name")
                .description("description")
                .barcode("barcode")
                .category(category)
                .priceWithKdv(new BigDecimal(200))
                .stock(new BigDecimal(100))
                .build();
    }

    private Category getCategory() {
        return Category.builder()
                .categoryId("categoryId")
                .categoryType(CategoryEnum.GIDA)
                .kdv_rate(new BigDecimal(1))
                .build();
    }

    private AddNewProductRequestDto getAddNewProdutRequestDto() {
        return AddNewProductRequestDto.builder()
                .name("name")
                .description("description")
                .barcode("barcode")
                .categoryKdvType("categoryKdvType")
                .priceWithoutKdv(new BigDecimal(100))
                .stock(new BigDecimal(100))
                .build();
    }

}