package com.dailycodework.buynowdotcom.repository;

import com.dailycodework.buynowdotcom.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
    // Custom method to find cart items by product ID, returning a list of cart items
}
