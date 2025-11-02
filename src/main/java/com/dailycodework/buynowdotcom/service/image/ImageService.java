package com.dailycodework.buynowdotcom.service.image;

import com.dailycodework.buynowdotcom.dtos.ImageDto;
import com.dailycodework.buynowdotcom.model.Image;
import com.dailycodework.buynowdotcom.model.Product;
import com.dailycodework.buynowdotcom.repository.ImageRepository;
import com.dailycodework.buynowdotcom.service.product.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ImageService implements IImageService {

    private final ImageRepository imageRepository; // Repository for Image entity
    private final IProductService productService; // Service for Product entity

    @Override
    public Image getImageById(Long imageId) { // Retrieve an image by its ID
        return imageRepository.findById(imageId) // Find image in the repository
                .orElseThrow(() -> new RuntimeException("Image not found")); // Throw exception if not found
    }

    @Override
    public void deleteImageById(Long imageId) { // Delete an image by its ID
        imageRepository.findById(imageId).ifPresentOrElse(imageRepository::delete, () -> { // Delete if found
            throw new RuntimeException("Image not found"); // Throw exception if not found
        });
    }

    @Override
    public void updateImage(MultipartFile file, Long imageId) { // Update an existing image
        Image image = getImageById(imageId); // Retrieve the image to be updated
        try {
            image.setFileName(file.getOriginalFilename()); // Update file name
            image.setFileType(file.getContentType()); // Update file type
            image.setImage(new SerialBlob(file.getBytes())); // Update image data
            imageRepository.save(image); // Save the updated image
        } catch (IOException | SQLException e) {
            throw new RuntimeException(e.getMessage()); // Handle exceptions
        }
    }

    @Override
    public List<ImageDto> saveImages(Long productId, List<MultipartFile> files) { // Save multiple images for a product
        Product product = productService.getProductById(productId); // Retrieve the associated product

        List<ImageDto> savedImages = new ArrayList<>(); // List to hold saved image DTOs
        for (MultipartFile file : files) { // Iterate over each file
            try {
                Image image = new Image(); // Create a new Image entity
                image.setFileName(file.getOriginalFilename()); // Set file name
                image.setFileType(file.getContentType()); // Set file type
                image.setImage(new SerialBlob(file.getBytes())); // Set image data
                image.setProduct(product); // Associate image with the product

                String buildDownloaderUrl = "/api/v1/images/download/"; // Base URL for downloading images
                String downloadUrl = buildDownloaderUrl + image.getId(); // Construct download URL
                image.setDownloadUrl(downloadUrl); // Set download URL
                Image savedImage = imageRepository.save(image); // Save the image entity
                savedImage.setDownloadUrl(buildDownloaderUrl + savedImage.getId()); // Update download URL with saved image ID
                imageRepository.save(savedImage); // Save the image entity again with updated download URL

                ImageDto imageDto = new ImageDto(); // Create a new Image DTO
                imageDto.setId(savedImage.getId()); // Set ID
                imageDto.setFileName(savedImage.getFileName()); // Set file name
                imageDto.setDownloadUrl(savedImage.getDownloadUrl()); // Set download URL
                savedImages.add(imageDto);// Add DTO to the list

            } catch (IOException | SQLException e) {
                throw new RuntimeException(e.getMessage()); // Handle exceptions
            }
        }
        return savedImages; // Return the list of saved image DTOs
    }
}
