package com.catalogo.negocio.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class ImageStorageService {

    private final Path uploadPath;
    private final boolean cloudinaryEnabled;
    private final String cloudinaryFolder;
    private final Cloudinary cloudinary;

    public ImageStorageService(@Value("${app.upload-dir:uploads}") String uploadDir,
                               @Value("${app.cloudinary.enabled:false}") boolean cloudinaryEnabled,
                               @Value("${app.cloudinary.cloud-name:}") String cloudName,
                               @Value("${app.cloudinary.api-key:}") String apiKey,
                               @Value("${app.cloudinary.api-secret:}") String apiSecret,
                               @Value("${app.cloudinary.folder:catalogo-negocio}") String cloudinaryFolder) throws IOException {
        this.uploadPath = Paths.get(uploadDir).toAbsolutePath().normalize();
        Files.createDirectories(uploadPath);
        this.cloudinaryFolder = cloudinaryFolder;

        boolean hasCloudinaryCredentials = StringUtils.hasText(cloudName)
                && StringUtils.hasText(apiKey)
                && StringUtils.hasText(apiSecret);
        this.cloudinaryEnabled = cloudinaryEnabled && hasCloudinaryCredentials;

        if (this.cloudinaryEnabled) {
            Map<String, String> config = new HashMap<>();
            config.put("cloud_name", cloudName);
            config.put("api_key", apiKey);
            config.put("api_secret", apiSecret);
            this.cloudinary = new Cloudinary(config);
        } else {
            this.cloudinary = null;
        }
    }

    public String save(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return null;
        }

        if (cloudinaryEnabled) {
            return saveToCloudinary(file);
        }

        return saveLocally(file);
    }

    private String saveToCloudinary(MultipartFile file) {
        try {
            Map<?, ?> result = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap(
                    "folder", cloudinaryFolder,
                    "resource_type", "image"));
            Object secureUrl = result.get("secure_url");
            if (secureUrl == null) {
                throw new IllegalStateException("Cloudinary no devolvio una URL valida");
            }
            return secureUrl.toString();
        } catch (IOException e) {
            throw new IllegalStateException("No se pudo subir la imagen a Cloudinary", e);
        }
    }

    private String saveLocally(MultipartFile file) {
        String originalName = StringUtils.cleanPath(file.getOriginalFilename() == null ? "" : file.getOriginalFilename());
        String extension = "";
        int dotIndex = originalName.lastIndexOf('.');
        if (dotIndex >= 0) {
            extension = originalName.substring(dotIndex);
        }

        String filename = UUID.randomUUID() + extension;
        Path destination = uploadPath.resolve(filename);
        try {
            Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new IllegalStateException("No se pudo guardar la imagen", e);
        }
        return "/uploads/" + filename;
    }
}
