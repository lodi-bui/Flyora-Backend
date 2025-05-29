package org.example.flyora_backend.controller;

import org.example.flyora_backend.model.Product;
import org.example.flyora_backend.model.response.ResponseObject;
import org.example.flyora_backend.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/v1/products")
@RestController
public class ProductController {
    @Autowired
    private ProductService productService;

    @GetMapping("")
    public ResponseEntity<ResponseObject> getAllProducts() {
        try {
          return  ResponseObject.APIRepsonse(400, "Get Product Sucess !", HttpStatus.OK, productService.getAllProducts());
        } catch (Exception e) {
         return   ResponseObject.APIRepsonse(400, "Get Product failed !", HttpStatus.BAD_REQUEST, null);
        }
    }
    @PostMapping("")
    public ResponseEntity<ResponseObject> CreateProduct(@RequestBody
                                                            Product product) {
        return ResponseObject.APIRepsonse(200, "Create product", HttpStatus.OK, "");
    }
    @PutMapping("")
    public ResponseEntity<ResponseObject> UpdateProduct(@RequestBody Product product) {
        return ResponseObject.APIRepsonse(200, "Update product", HttpStatus.OK, "");
    }
    @DeleteMapping("")
    public ResponseEntity<ResponseObject> DeleteProduct(@RequestBody Product product) {
        return ResponseObject.APIRepsonse(200, "Delete product", HttpStatus.OK, "");
    }

}
