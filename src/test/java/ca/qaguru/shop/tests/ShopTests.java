package ca.qaguru.shop.tests;

import ca.qaguru.shop.models.Product;
import ca.qaguru.shop.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpStatus;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;



public class ShopTests {

    private ProductService service;
    ObjectMapper mapper;

    @BeforeTest
    public void setup(){
        service = new ProductService();
        mapper = new ObjectMapper();
    }
    @Test
    public void saveNewProduct() throws IOException {
        Product product = mapper.readValue(new File("src/test/resources/product.json"),Product.class);
        service.saveNewRecord(product);
    }

    @Test
    public void findProductById() throws IOException {
        Product expProduct = mapper.readValue(new File("src/test/resources/product.json"),Product.class);
        String id = service.saveNewRecord(expProduct);
        expProduct.setId(id);
        service.findRecordById(id,expProduct,HttpStatus.SC_OK);
    }
    @Test
    public void findAllProducts() throws IOException {
        List<Product> expProducts = Arrays.asList(mapper.readValue(new File("src/test/resources/productarray.json"),Product[].class));
        for(Product product:expProducts){
            String id = service.saveNewRecord(product);
            product.setId(id);
        }
       service.findAllProducts(expProducts);

    }
    @Test
    public void updateARecord() throws IOException {
        Product product = mapper.readValue(new File("src/test/resources/product.json"),Product.class);
        String id = service.saveNewRecord(product);
        product.setId(id);
        product.setName("Updated Name");
        product.setDescription("Updated description");
        product.setPrice(200.23f);
        service.updateRecord(id,product);
        service.findRecordById(id,product,HttpStatus.SC_OK);
    }

    @Test
    public void deleteARecord() throws IOException {
        Product product = mapper.readValue(new File("src/test/resources/product.json"),Product.class);
        String id = service.saveNewRecord(product);
        service.deleteRecordById(id);
        service.findRecordById(id,null,HttpStatus.SC_NOT_FOUND);
    }

}
