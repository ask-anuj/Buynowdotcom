package com.dailycodework.buynowdotcom.repository;

import com.dailycodework.buynowdotcom.model.CartItem;
import com.dailycodework.buynowdotcom.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    Category findByName(String name);

    List<CartItem> findByProductId(Long productId);
}
