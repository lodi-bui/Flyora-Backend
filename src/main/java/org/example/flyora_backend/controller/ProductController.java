package org.example.flyora_backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Product Controller")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("")
    public ResponseEntity<ResponseObject> getAllProducts() {
        try {
            return ResponseObject.APIResponse(400, "Get Product Success !", HttpStatus.OK, productService.getAllProducts());
        } catch (Exception e) {
            return ResponseObject.APIResponse(400, "Get Product failed !", HttpStatus.BAD_REQUEST, null);
        }
    }

    @Operation(summary = "Get 1 Product", description = "API get one product")
    @GetMapping("/one")
    public ResponseEntity<ResponseObject> getOneProduct(@RequestParam long id) {
        try {
            return ResponseObject.APIResponse(400,"Get One Product Success !",HttpStatus.OK, productService.getOneProduct(id));
        } catch (Exception e) {
            return ResponseObject.APIResponse(400,"Get One Product failed !",HttpStatus.BAD_REQUEST, null);
        }

    }

    //lay danh sach san pham available (status = true)
    @Operation(summary = "Get List", description = "API get list product")
    @GetMapping("/status")
    public ResponseEntity<ResponseObject> getProductByStatus() {
        try {
            return ResponseObject.APIResponse(400,"Get Available Product Success", HttpStatus.OK, productService.getProductByStatus());
        }catch (Exception e) {
            return ResponseObject.APIResponse(400,"Get Available Product failed !",HttpStatus.BAD_REQUEST, null);
        }
    }

    //Tao 1 san pham vao database
    @Operation(summary = "Add product", description = "API create new product")  //mo ta ten chuc nang
    @PostMapping("")
    public ResponseEntity<ResponseObject> addProduct(@RequestBody Product product) {
        try {
            return ResponseObject.APIResponse(400, "Add Product Success", HttpStatus.OK, productService.addProduct(product));
        } catch (Exception e) {
            return ResponseObject.APIResponse(400, "Add Product failed !",HttpStatus.BAD_REQUEST, null);
        }
    }

    //Xoa san pham dua vao id
    @Operation(summary = "Delete product", description = "API delete product")
    @DeleteMapping("")
    public ResponseEntity<ResponseObject> deleteProductById(@RequestParam int id) {
        try {
            return ResponseObject.APIResponse(400, "Delete Product By ID Success", HttpStatus.OK, productService.deleteProductById(id));
        } catch (Exception e) {
            return ResponseObject.APIResponse(400, "Delete Product By ID failed", HttpStatus.BAD_REQUEST, productService.deleteProductById(id));
        }
    }
}
