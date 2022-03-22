package tr.softtech.patika.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import tr.softtech.patika.IntegrationTestSupport;
import tr.softtech.patika.dto.AddNewProductRequestDto;
import tr.softtech.patika.dto.UpdateProductRequestDto;
import tr.softtech.patika.enums.CategoryEnum;
import tr.softtech.patika.model.Category;
import tr.softtech.patika.model.Product;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Slf4j
class ProductControllerIT extends IntegrationTestSupport {

    private final String baseUrl = "http://localhost:8080/api/v1/product";

    @Test
    public void testGetAllProduct_whenRequestIsValid_shouldReturnPage() throws Exception {
        Category category = getCategory();
        category = categoryRepository.save(category);
        Product product = getProduct(category);
        product = productRepository.save(product);

        this.mockMvc.perform(get(baseUrl +"?pageNumber=0&pageSize=5").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.data.content[0].productId",is(product.getProductId())))
                .andExpect(jsonPath("$.data.content[0].name",is(product.getName())))
                .andExpect(jsonPath("$.data.content[0].description",is(product.getDescription())))
                .andExpect(jsonPath("$.data.content[0].barcode",is(product.getBarcode())));
    }

    @Test
    public void testGetAllProductByCategory_whenCategoryNotExist_shouldReturn404ItemNotFound() throws Exception {
        Category category = getCategory();
        category = categoryRepository.save(category);
        Product product = getProduct(category);
        productRepository.save(product);
        String categoryType = "KOZMETIK";

        this.mockMvc.perform(get(baseUrl + "/category" + "?pageNumber=0&pageSize=5&categoryType=" + categoryType)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void testGetAllProductByCategory_whenCategoryExist_shouldReturnPage() throws Exception {
        Category category = getCategory();
        category = categoryRepository.save(category);
        Product product = getProduct(category);
        product = productRepository.save(product);
        String categoryType = "GIDA";

        this.mockMvc.perform(get(baseUrl + "/category" + "?pageNumber=0&pageSize=5&categoryType=" + categoryType)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.data.content[0].productId",is(product.getProductId())))
                .andExpect(jsonPath("$.data.content[0].name",is(product.getName())))
                .andExpect(jsonPath("$.data.content[0].description",is(product.getDescription())))
                .andExpect(jsonPath("$.data.content[0].barcode",is(product.getBarcode())));
    }

    @Test
    public void testGetAllProductByPrice_whenMinMaxPriceNotExist_shouldReturn404NotFound() throws Exception {
        Category category = getCategory();
        category = categoryRepository.save(category);
        Product product = getProduct(category);
        productRepository.save(product);

        this.mockMvc.perform(get(baseUrl + "/price" + "pageNumber=0&pageSize=5").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void testGetAllProductByCategory_whenMinMaxPriceExist_shouldReturnPage() throws Exception {
        Category category = getCategory();
        category = categoryRepository.save(category);
        Product product = getProduct(category);
        product = productRepository.save(product);
        int minPrice = 0;
        int maxPrice = 500;

        this.mockMvc.perform(get(baseUrl + "/price"
                        + "?pageNumber=0&pageSize=5&minPrice="+ minPrice + "&maxPrice=" + maxPrice)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.data.content[0].productId",is(product.getProductId())))
                .andExpect(jsonPath("$.data.content[0].name",is(product.getName())))
                .andExpect(jsonPath("$.data.content[0].description",is(product.getDescription())))
                .andExpect(jsonPath("$.data.content[0].barcode",is(product.getBarcode())));
    }

    @Test
    public void testGetAllProductByPriceAndCategory_whenCategoryNotExist_shouldReturn400BadRequest() throws Exception {
        Category category = getCategory();
        category = categoryRepository.save(category);
        Product product = getProduct(category);
        productRepository.save(product);
        String categoryType = "anonym";
        int minPrice = 0;
        int maxPrice = 500;

        this.mockMvc.perform(get(baseUrl
                        + "/priceAndCategory"
                        + "?pageNumber=0&pageSize=5&categoryType=" + categoryType + "&minPrice="+ minPrice + "&maxPrice=" + maxPrice)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void testGetAllProductByPriceAndCategory_whenMinMaxPriceNotExist_shouldReturn500ServerEror() throws Exception {
        Category category = getCategory();
        category = categoryRepository.save(category);
        Product product = getProduct(category);
        productRepository.save(product);
        String categoryType = "GIDA";

        this.mockMvc.perform(get(baseUrl + "/priceAndCategory" + "?pageNumber=0&pageSize=5&categoryType=" + categoryType)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is5xxServerError());
    }

    @Test
    public void testGetAllProductByPriceAndCategory_whenMinMaxPriceAndCategoryExist_shouldReturnPage() throws Exception {
        Category category = getCategory();
        category = categoryRepository.save(category);
        Product product = getProduct(category);
        product = productRepository.save(product);
        String categoryType = "GIDA";
        int minPrice = 0;
        int maxPrice = 500;

        this.mockMvc.perform(get(baseUrl + "/priceAndCategory"
                        + "?pageNumber=0&pageSize=5&categoryType=" + categoryType + "&minPrice=" +minPrice
                        + "&maxPrice=" + maxPrice)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.data.content[0].productId",is(product.getProductId())))
                .andExpect(jsonPath("$.data.content[0].name",is(product.getName())))
                .andExpect(jsonPath("$.data.content[0].description",is(product.getDescription())))
                .andExpect(jsonPath("$.data.content[0].barcode",is(product.getBarcode())));
    }

    @Test
    public void testGetSummuryByCategory_whenRequestIsValid_shouldReturnSummaryDtoList() throws Exception {
        Category category = getCategory();
        category = categoryRepository.save(category);
        Product product = getProduct(category);
        product = productRepository.save(product);

        this.mockMvc.perform(get(baseUrl + "/summary")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.data[0].categoryType",is(category.getCategoryType().toString())))
                .andExpect(jsonPath("$.data[0].kdvRate",is(category.getKdv_rate().doubleValue())))
                .andExpect(jsonPath("$.data[0].minPrice",is(product.getPriceWithKdv().doubleValue())))
                .andExpect(jsonPath("$.data[0].maxPrice",is(product.getPriceWithKdv().doubleValue())))
                .andExpect(jsonPath("$.data[0].stock",is(product.getStock().doubleValue())));
    }

    @Test
    public void testAddNewProduct_whenAddNewProductRequestIsInvalid_shouldNotCreateProductAndReturn400BadRequest() throws Exception {
        AddNewProductRequestDto request = AddNewProductRequestDto.builder()
                .name("")
                .description("")
                .barcode("")
                .categoryKdvType("")
                .priceWithoutKdv(null)
                .stock(null)
                .build();

        this.mockMvc.perform(post(baseUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writer().withDefaultPrettyPrinter().writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        List<Product> productList = productRepository.findAll();
        assertEquals(0,productList.size());
    }

    @Test
    public void testAddNewProduct_whenAddNewProductRequestIsValid_shouldReturnProductDto() throws Exception {
        Category category = getCategory();
        categoryRepository.save(category);
        AddNewProductRequestDto request = getAddNewProdutRequestDto();

        this.mockMvc.perform(post(baseUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writer().withDefaultPrettyPrinter().writeValueAsString(request)))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data.name",is(request.getName())))
                .andExpect(jsonPath("$.data.description",is(request.getDescription())))
                .andExpect(jsonPath("$.data.barcode",is(request.getBarcode())));

        List<Product> productList = productRepository.findAll();
        assertEquals(1,productList.size());
    }

    @Test
    public void testDeleteProduct_whenProductIdNotExist_shouldNotDeleteAndThrow404NotFound() throws Exception {
        Category category = getCategory();
        category = categoryRepository.save(category);
        Product product = getProduct(category);
        product = productRepository.save(product);
        String productId = "anonym";

        this.mockMvc.perform(delete(baseUrl + "?product_id=" + productId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.data.message",is("product not found")))
                .andExpect(jsonPath("$.data.description",is("uri=/api/v1/product")));

        List<Product> productList = productRepository.findAll();
        assertEquals(1,productList.size());
    }

    @Test
    public void testDeleteProduct_whenProductIdExist_shouldNotDeleteProduct() throws Exception {
        Category category = getCategory();
        category = categoryRepository.save(category);
        Product product = getProduct(category);
        product = productRepository.save(product);

        this.mockMvc.perform(delete(baseUrl + "?product_id=" + product.getProductId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.data",is("void")))
                .andExpect(jsonPath("$.messages",is("product deleted successfully")))
                .andExpect(jsonPath("$.success",is(true)));

        List<Product> productList = productRepository.findAll();
        assertEquals(0,productList.size());
    }

    @Test
    public void testUpdateProduct_whenUpdateProductIdNotExist_shouldNotUpdateAndThrow404ItemNotFoundException() throws Exception {
        Category category = getCategory();
        category = categoryRepository.save(category);
        Product product = getProduct(category);
        product = productRepository.save(product);
        UpdateProductRequestDto request = getUpdateProductRequestDto();
        request.setProductId("anonym");

        this.mockMvc.perform(put(baseUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writer().withDefaultPrettyPrinter().writeValueAsString(request)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.data.message",is("Product not found")))
                .andExpect(jsonPath("$.data.description",is("uri=/api/v1/product")));
    }

    @Test
    public void testUpdateProduct_whenUpdateProductRequestDtoIsInvalid_shouldNotUpdateAndThrow400BadRequest() throws Exception {
        Category category = getCategory();
        category = categoryRepository.save(category);
        Product product = getProduct(category);
        product = productRepository.save(product);
        UpdateProductRequestDto request = getUpdateProductRequestDto();
        request.setCategoryKdvType("anonym");

        this.mockMvc.perform(put(baseUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writer().withDefaultPrettyPrinter().writeValueAsString(request)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.success",is(false)))
                .andExpect(jsonPath("$.data.description",is("uri=/api/v1/product")));
    }

    @Test
    public void testUpdateProduct_whenUpdateProductRequestDtoIsValid_shouldUpdateAndReturnProductDto() throws Exception {
        Category category = getCategory();
        category = categoryRepository.save(category);
        Product product = getProduct(category);
        product = productRepository.save(product);
        UpdateProductRequestDto request = getUpdateProductRequestDto();
        request.setProductId(product.getProductId());

        this.mockMvc.perform(put(baseUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writer().withDefaultPrettyPrinter().writeValueAsString(request)))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.data.productId",is(request.getProductId())))
                .andExpect(jsonPath("$.data.name",is(request.getName())))
                .andExpect(jsonPath("$.data.barcode",is(request.getBarcode())))
                .andExpect(jsonPath("$.data.description",is("description")))
                .andExpect(jsonPath("$.success",is(true)));

        Product productForAssert = productRepository.getById(request.getProductId());
        assertNotNull(productForAssert);
    }

    @Test
    public void testUpdateProductPrice_whenPriceIsInValid_shouldNotUpdateAndThrow500ServerError() throws Exception {
        Category category = getCategory();
        category = categoryRepository.save(category);
        Product product = getProduct(category);
        product = productRepository.save(product);
        BigDecimal price = new BigDecimal(-100);

        this.mockMvc.perform(patch(baseUrl + "/price" + "?productId=" + product.getProductId() + "&price=" + price)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is5xxServerError())
                .andExpect(jsonPath("$.data.description",is("uri=/api/v1/product/price")))
                .andExpect(jsonPath("$.success",is(false)));
    }


    @Test
    public void testUpdateProductPrice_whenProductIdNotExist_shouldNotUpdateAndThrow404ItemNotFoundException() throws Exception {
        Category category = getCategory();
        category = categoryRepository.save(category);
        Product product = getProduct(category);
        product = productRepository.save(product);
        String productId = "anonym";

        this.mockMvc.perform(patch(baseUrl + "/price" + "?productId=" + productId + "&price=100")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.data.message",is("product not found")))
                .andExpect(jsonPath("$.data.description",is("uri=/api/v1/product/price")))
                .andExpect(jsonPath("$.success",is(false)));
    }

    @Test
    public void testUpdateProductPrice_whenProductIdAndPriceIsValid_shouldUpdateAndReturnProductDto() throws Exception {
        Category category = getCategory();
        category = categoryRepository.save(category);
        Product product = getProduct(category);
        product = productRepository.save(product);
        BigDecimal price = new BigDecimal(100);

        this.mockMvc.perform(patch(baseUrl + "/price" + "?productId=" + product.getProductId() + "&price=" + price)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.data.productId",is(product.getProductId())))
                .andExpect(jsonPath("$.data.name",is(product.getName())))
                .andExpect(jsonPath("$.data.description",is(product.getDescription())))
                .andExpect(jsonPath("$.data.stock",is(price.doubleValue())))
                .andExpect(jsonPath("$.success",is(true)));
    }

    @Test
    public void testUpdateProductStock_whenProductIdNotExist_shouldNotUpdateAndThrow404ItemNotFoundException() throws Exception {
        Category category = getCategory();
        category = categoryRepository.save(category);
        Product product = getProduct(category);
        product = productRepository.save(product);
        String productId = "anonym";

        this.mockMvc.perform(patch(baseUrl + "/stock" + "?productId=" + productId + "&stock=100")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.data.message",is("product not found")))
                .andExpect(jsonPath("$.data.description",is("uri=/api/v1/product/stock")))
                .andExpect(jsonPath("$.success",is(false)));
    }

    @Test
    public void testUpdateProductStock_whenProductIdAndStockIsValid_shouldUpdateAndReturnProductDto() throws Exception {
        Category category = getCategory();
        category = categoryRepository.save(category);
        Product product = getProduct(category);
        product = productRepository.save(product);
        BigDecimal stock = new BigDecimal(100);

        this.mockMvc.perform(patch(baseUrl + "/stock" + "?productId=" + product.getProductId() + "&stock=" + stock)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.data.productId",is(product.getProductId())))
                .andExpect(jsonPath("$.data.name",is(product.getName())))
                .andExpect(jsonPath("$.data.description",is(product.getDescription())))
                .andExpect(jsonPath("$.success",is(true)));
    }

    @Test
    public void testUpdateKdvRate_whenCategoryNotExist_shouldReturn404ItemNotFound() throws Exception {
        Category category = getCategory();
        category = categoryRepository.save(category);
        String categoryType = "KOZMETIK";
        BigDecimal kdvRate = new BigDecimal(1);

        this.mockMvc.perform(patch(baseUrl
                        + "/kdvRate?categoryType=" + categoryType + "&kdvRate=" + kdvRate)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void testUpdateKdvRate_whenCategoryIsValid_shouldUpdateAndReturnCategoryDto() throws Exception {
        Category category = getCategory();
        category = categoryRepository.save(category);
        BigDecimal kdvRate = new BigDecimal(1);

        this.mockMvc.perform(patch(baseUrl + "/kdvRate?categoryType=" + category.getCategoryType() + "&kdvRate=" + kdvRate)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.data.categoryId",is(category.getCategoryId())))
                .andExpect(jsonPath("$.messages",is("kdv rate aupdated")))
                .andExpect(jsonPath("$.success",is(true)));
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
                .categoryKdvType("GIDA")
                .priceWithoutKdv(new BigDecimal(100))
                .stock(new BigDecimal(100))
                .build();
    }

    private UpdateProductRequestDto getUpdateProductRequestDto() {
        return UpdateProductRequestDto.builder()
                .productId("productId")
                .name("name")
                .description("description")
                .barcode("barcode")
                .categoryKdvType("GIDA")
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

}