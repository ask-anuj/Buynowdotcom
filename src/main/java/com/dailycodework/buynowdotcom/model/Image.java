package com.dailycodework.buynowdotcom.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Blob;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String fileName;
    private  String fileType;
    @Lob // Large Object annotation for binary data, used for storing images
    private Blob image; // Blob to store binary data

    private String downloadUrl;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product; // Many-to-one relationship with Product
}
