package com.dailycodework.buynowdotcom.service.product;

import com.dailycodework.buynowdotcom.model.Product;
import com.dailycodework.buynowdotcom.request.AddProductRequest;
import com.dailycodework.buynowdotcom.request.ProductUpdateRequest;

import java.util.List;

public interface IProductService // Interface for Product Service
{
    Product addProduct(AddProductRequest product); // Method to add a new product
    Product updateProduct(ProductUpdateRequest product, Long productId); // Method to update an existing product
    Product getProductById(Long productId); // Method to get a product by its ID
    void deleteProductById(Long productId); // Method to delete a product by its ID

    List<Product> getAllProducts(); // Method to get all products
    List<Product> getProductsByCategoryAndBrand(String category, String brand); // Method to get products by category and brand
    List<Product> getProductsByCategory(String category); // Method to get products by category
    List<Product> getProductsByBrandAndName(String brand, String name); // Method to get products by brand and name
    List<Product> getProductsByBrand(String brand); // Method to get products by brand
    List<Product> getProductsByName(String name); // Method to get products by name


}
