//package com.dailycodework.buynowdotcom.repository;
//
//public interface ProductRepository {
//}
package com.dailycodework.buynowdotcom.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.dailycodework.buynowdotcom.model.Product;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCategoryNameAndBrand(String category, String brand); // Custom method to find products by category and brand, returning a list of products

    List<Product> findByCategoryName(String category);// Custom method to find products by category name, returning a list of products

    List<Product> findByNameAndBrand(String brand, String name); // Custom method to find products by name and brand, returning a list of products

    List<Product> findByBrand(String brand); // Custom method to find products by brand, returning a list of products

    @Query("SELECT p FROM Product p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))") // Custom query to find products by name using a case-insensitive search, searching for partial matches
    List<Product> findByName(String name); // Custom method to find products by name, returning a list of products

    boolean existsByNameAndBrand(String name, String brand); // Custom method to check if a product exists by name and brand, returning a boolean
}