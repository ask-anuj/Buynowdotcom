package com.dailycodework.buynowdotcom.service.product;


import com.dailycodework.buynowdotcom.dtos.ImageDto;
import com.dailycodework.buynowdotcom.dtos.ProductDto;
import com.dailycodework.buynowdotcom.model.*;
import com.dailycodework.buynowdotcom.repository.*;
import com.dailycodework.buynowdotcom.request.AddProductRequest;
import com.dailycodework.buynowdotcom.request.ProductUpdateRequest;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service // Annotation to indicate that this class is a service component in Spring
@RequiredArgsConstructor // Lombok annotation to generate a constructor with required arguments
public class ProductService implements IProductService {

    private final ProductRepository productRepository; // Injecting the ProductRepository dependency, marked as final for immutability
    private final CategoryRepository categoryRepository; // Injecting the CategoryRepository dependency
    private final CartItemRepository cartItemRepository; // Injecting the CartItemRepository dependency
    private final OrderItemRepository orderItemRepository; // Injecting the OrderItemRepository dependency
    private final ImageRepository imageRepository; // Injecting the ImageRepository dependency
    private final ModelMapper modelMapper; // Injecting the ModelMapper dependency for object mapping

    @Override
    public Product addProduct(AddProductRequest request) {
        if (productExists(request.getName(), request.getBrand())) {
            throw new EntityExistsException(request.getName() + " Already exists"); // Checking if product already exists, throwing exception if it does
        }
        // Handling category: checking if it exists, creating and saving if it doesn't
        // Using Optional to handle potential null values when fetching category by name
        Category category = Optional.ofNullable(categoryRepository.findByName(request.getCategory().getName()))
                .orElseGet(() -> {
                    Category newCategory = new Category(request.getCategory().getName());
                    return categoryRepository.save(newCategory);
                });
        request.setCategory(category);
        return productRepository.save(createProduct(request, category)); // Creating and saving the new product, returning the saved product
    }

    private boolean productExists(String name, String brand) {
        return productRepository.existsByNameAndBrand(name, brand); // Checking if a product exists by name and brand, returning a boolean
    }

    private Product createProduct(AddProductRequest request, Category category) {
        return new Product(
                request.getName(),
                request.getBrand(),
                request.getPrice(),
                request.getInventory(),
                request.getDescription(),
                category
        ); // Creating a new Product instance using the provided request data and category, returning the new product
    }

    @Override
    public Product updateProduct(ProductUpdateRequest request, Long productId) {
        return productRepository.findById(productId)
                .map(existingProduct -> updateExistingProduct(existingProduct, request))
                .map(productRepository::save).orElseThrow(() -> new EntityNotFoundException("Product not found")); // Updating an existing product, throwing exception if not found
    }

    private Product updateExistingProduct(Product existingProduct, ProductUpdateRequest request) { // Updating the fields of an existing product with data from the request, returning the updated product
        existingProduct.setName(request.getName());
        existingProduct.setBrand(request.getBrand());
        existingProduct.setPrice(request.getPrice());
        existingProduct.setInventory(request.getInventory());
        existingProduct.setDescription(request.getDescription());
        Category category = categoryRepository.findByName(request.getCategory().getName());
        existingProduct.setCategory(category);
        return existingProduct;
    }

    @Override
    public Product getProductById(Long productId) { // Getting a product by its ID
        return productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found")); // Fetching product by ID, throwing exception if not found
    }

    @Override
    public void deleteProductById(Long productId) { // Deleting a product by its ID
        productRepository.findById(productId) // Fetching product by ID
                .ifPresentOrElse(product -> { // If product is found, proceed with deletion
                    List<CartItem> cartItems = cartItemRepository.findByProductId(productId); // Fetching cart items associated with the product
                    cartItems.forEach(cartItem -> { // Iterating through each cart item
                        Cart cart = cartItem.getCart(); // Getting the cart associated with the cart item
                        cart.removeItem(cartItem); // Removing the cart item from the cart
                        cartItemRepository.delete(cartItem);// Deleting the cart item from the repository
                    });

                    List<OrderItem> orderItems = orderItemRepository.findByProductId(productId); // Fetching order items associated with the product
                    orderItems.forEach(orderItem -> { // Iterating through each order item
                        orderItem.setProduct(null); // Setting the product reference in the order item to null
                        orderItemRepository.save(orderItem); // Saving the updated order item back to the repository
                    });

                    Optional.ofNullable(product.getCategory()) // Using Optional to handle potential null category
                            .ifPresent(category -> category.getProducts().remove(product)); // Removing the product from its category's product list if category exists
                    product.setCategory(null); // Setting the product's category to null

                    productRepository.deleteById(product.getId()); // Deleting the product from the repository by its ID

                }, () -> { // If product is not found
                    throw new EntityNotFoundException("Product not found"); // If product is not found, throw an exception
                }); // Deleting a product by ID, handling related cart items and order items, throwing exception if not found
    }

    @Override
    public List<Product> getAllProducts() { // Getting all products
        return productRepository.findAll();// Fetching all products from the repository, returning as a list
    }

    @Override
    public List<Product> getProductsByCategoryAndBrand(String category, String brand) { // Getting products by category and brand
        return productRepository.findByCategoryNameAndBrand(category, brand); // Fetching products by category and brand from the repository, returning as a list
    }

    @Override
    public List<Product> getProductsByCategory(String category) { // Getting products by category
        return productRepository.findByCategoryName(category); // Fetching products by category name from the repository, returning as a list
    }

    @Override
    public List<Product> getProductsByBrandAndName(String brand, String name) { // Getting products by brand and name
        return productRepository.findByNameAndBrand(brand, name); // Fetching products by brand and name from the repository, returning as a list
    }

    @Override
    public List<Product> getProductsByBrand(String brand) { // Getting products by brand
        return productRepository.findByBrand(brand); // Fetching products by brand from the repository, returning as a list
    }

    @Override
    public List<Product> getProductsByName(String name) { // Getting products by name
        return productRepository.findByName(name); // Fetching products by name from the repository, returning as a list
    }

    @Override
    public List<ProductDto> getConvertedProducts(List<Product> products){
        return products.stream()
                .map(this::convertToDto)
                .toList();
    }

    @Override
    public ProductDto convertToDto(Product product){
        ProductDto productDto = modelMapper.map(product, ProductDto.class);
        List<Image> images = imageRepository.findByProductId(product.getId());
        List<ImageDto> imageDtos = images.stream()
                .map(image -> modelMapper.map(image, ImageDto.class)).toList();
        productDto.setImages(imageDtos);
        return productDto;
    }
}
