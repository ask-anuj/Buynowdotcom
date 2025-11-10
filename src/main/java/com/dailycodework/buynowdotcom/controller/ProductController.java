package com.dailycodework.buynowdotcom.controller;

import com.dailycodework.buynowdotcom.dtos.ProductDto;
import com.dailycodework.buynowdotcom.model.Product;
import com.dailycodework.buynowdotcom.request.AddProductRequest;
import com.dailycodework.buynowdotcom.request.ProductUpdateRequest;
import com.dailycodework.buynowdotcom.response.ApiResponse;
import com.dailycodework.buynowdotcom.service.product.IProductService;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/products")
public class ProductController {

    private final IProductService productService;

    //    @PostMapping("/add")
//    public ResponseEntity<ApiResponse> addProduct(@RequestBody AddProductRequest product){
//        try {
//            Product theProduct = productService.addProduct(product);
//            ProductDto productDto = productService.convertToDto(theProduct);
//            return ResponseEntity.ok(new ApiResponse("Add product success!", productDto));
//        } catch (EntityNotFoundException e) {
//            return ResponseEntity.status(CONFLICT).body(new ApiResponse(e.getMessage(), null));
//        }
//    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        List<ProductDto> convertedProducts = productService.getConvertedProducts(products);
        return ResponseEntity.ok(new ApiResponse("Found", convertedProducts));
    }

    @GetMapping("product/{productId}/product")
    public ResponseEntity<ApiResponse> getProductById(@PathVariable Long productId) {
        Product product = productService.getProductById(productId);
        ProductDto productDto = productService.convertToDto(product);
        return ResponseEntity.ok(new ApiResponse("Found!", productDto));
    }

//    @PostMapping("/add")
//    public ResponseEntity<ApiResponse> addProduct(@RequestBody AddProductRequest product) {
//        Product theProduct = productService.addProduct(product);
//        ProductDto productDto = productService.convertToDto(theProduct);
//        return ResponseEntity.ok(new ApiResponse("Add product success!", productDto));
//    }

//    @PostMapping("/add")
//    public ResponseEntity<ApiResponse> addProduct(@RequestBody AddProductRequest product){
//        try {
//            Product theProduct = productService.addProduct(product);
//            ProductDto productDto = productService.convertToDto(theProduct);
//            return ResponseEntity.ok(new ApiResponse("Add product success!", productDto));
//        } catch (EntityNotFoundException e) {
//            return ResponseEntity.status(CONFLICT).body(new ApiResponse(e.getMessage(), null));
//        }
//    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addProduct(@RequestBody AddProductRequest product) {
        try {
            Product theProduct = productService.addProduct(product);
            ProductDto productDto = productService.convertToDto(theProduct);
            return ResponseEntity.ok(new ApiResponse("Add product success!", productDto));

        } catch (EntityExistsException e) {
            // Handle duplicate product scenario
            return ResponseEntity.status(CONFLICT)
                    .body(new ApiResponse(e.getMessage(), null));

        } catch (Exception e) {
            // Catch-all for unexpected errors
            return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Something went wrong: " + e.getMessage(), null));
        }
    }


    @PutMapping("/product/{productId}/update")
    public ResponseEntity<ApiResponse> updateProduct(@RequestBody ProductUpdateRequest request, @PathVariable Long productId) {
        Product theProduct = productService.updateProduct(request, productId);
        ProductDto productDto = productService.convertToDto(theProduct);
        return ResponseEntity.ok(new ApiResponse("Update product success!", productDto));
    }

    @DeleteMapping("/product/{productId}/delete")
    public ResponseEntity<ApiResponse> deleteProduct(@PathVariable Long productId) {
        productService.deleteProductById(productId);
        return ResponseEntity.ok(new ApiResponse("Delete product success!", productId));
    }

    @GetMapping("/products/by/brand-and-name")
    public ResponseEntity<ApiResponse> getProductByBrandAndName(@RequestParam String brandName, @RequestParam String productName) {
        List<Product> products = productService.getProductsByBrandAndName(brandName, productName);
        List<ProductDto> convertedProducts = productService.getConvertedProducts(products);
        return ResponseEntity.ok(new ApiResponse("success", convertedProducts));
    }

    @GetMapping("/products/{name}/products")
    public ResponseEntity<ApiResponse> getProductByName(@PathVariable String name) {
        List<Product> products = productService.getProductsByName(name);
        List<ProductDto> convertedProducts = productService.getConvertedProducts(products);
        return ResponseEntity.ok(new ApiResponse("success", convertedProducts));
    }

}
