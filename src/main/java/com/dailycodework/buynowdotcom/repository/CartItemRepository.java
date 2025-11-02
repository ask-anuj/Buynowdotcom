package com.dailycodework.buynowdotcom.repository;

import com.dailycodework.buynowdotcom.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findByProductId(Long productId); // Custom method to find cart items by product ID, returning a list of cart items

}
