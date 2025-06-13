package org.example.flyora_backend.controller;

import java.util.List;

import org.example.flyora_backend.model.Product;
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

    @GetMapping("/category/{category}")
    public ResponseEntity<?> getProductByCategory(@PathVariable Product.ProductCategory category) 
    {
        List<Product> products = productService.getProductByCategory(category);
        if(products.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not Exist Category " + category);
        }
        return ResponseEntity.ok(products);
    }
    
    @GetMapping("")
    public ResponseEntity<ResponseObject> getAllProducts() {
        try {
            return ResponseObject.APIResponse(400, "Get Product Success !", HttpStatus.OK, productService.getAllProducts());
        } catch (Exception e) {
            return ResponseObject.APIResponse(400, "Get Product failed !", HttpStatus.BAD_REQUEST, null);
        }
    }

    @GetMapping("/one")
    public ResponseEntity<ResponseObject> getOneProduct(@RequestParam int id) {
        try {
            return ResponseObject.APIResponse(400,"Get One Product Success !",HttpStatus.OK, productService.getOneProduct(id));
        } catch (Exception e) {
            return ResponseObject.APIResponse(400,"Get One Product failed !",HttpStatus.BAD_REQUEST, null);
        }

    }

    //Tao 1 san pham vao database
    @PostMapping("")
    public ResponseEntity<ResponseObject> addProduct(@RequestBody Product product) {
        try {
            return ResponseObject.APIResponse(400, "Add Product Success", HttpStatus.OK, productService.addProduct(product));
        } catch (Exception e) {
            return ResponseObject.APIResponse(400, "Add Product failed !",HttpStatus.BAD_REQUEST, null);
        }
    }

    //Xoa san pham dua vao id
    @DeleteMapping("")
    public ResponseEntity<ResponseObject> deleteProductById(@RequestParam int id) {
        try {
            return ResponseObject.APIResponse(400, "Delete Product By ID Success", HttpStatus.OK, productService.deleteProductById(id));
        } catch (Exception e) {
            return ResponseObject.APIResponse(400, "Delete Product By ID failed", HttpStatus.OK, productService.deleteProductById(id));
        }
    }
}
