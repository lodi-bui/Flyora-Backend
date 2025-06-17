package org.example.flyora_backend.controller;

import java.util.List;

import org.example.flyora_backend.model.Product;
import org.example.flyora_backend.model.ProductCategory;
import org.example.flyora_backend.model.response.ResponseObject;
import org.example.flyora_backend.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1/products")
@RestController
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/best-sellers/top2")
    public ResponseEntity<ResponseObject> getTop2BestSellersEachCategory() {
        List<Product> topProducts = productService.getTopBestSellersEachCategory(2);
        return ResponseObject.APIResponse(200, "Top 2 best-sellers for each category", HttpStatus.OK, topProducts);
    }

    @GetMapping("/api/v1/category/{categoryId}")
    public ResponseEntity<?> getProductByCategory(@PathVariable Integer categoryId) {
        ProductCategory category = ProductCategory.builder().id(categoryId).build();
        List<Product> products = productService.getProductByCategory(category);
        if (products.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Category not found.");
        }
        return ResponseEntity.ok(products);
    }

    @GetMapping("")
    public ResponseEntity<ResponseObject> getAllProducts() {
        return ResponseObject.APIResponse(
                200, "Get Products Success!", HttpStatus.OK, productService.getAllProducts());
    }

    @GetMapping("/one")
    public ResponseEntity<ResponseObject> getOneProduct(@RequestParam int id) {
        return ResponseObject.APIResponse(
                200, "Get One Product Success!", HttpStatus.OK, productService.getOneProduct(id));
    }

    @PostMapping("")
    public ResponseEntity<ResponseObject> addProduct(@RequestBody Product product) {
        return ResponseObject.APIResponse(
                201, "Add Product Success", HttpStatus.CREATED, productService.addProduct(product));
    }

    @DeleteMapping("")
    public ResponseEntity<ResponseObject> deleteProductById(@RequestParam int id) {
        productService.deleteProductById(id);
        return ResponseObject.APIResponse(204, "Delete Product Success", HttpStatus.NO_CONTENT, null);
    }
}
