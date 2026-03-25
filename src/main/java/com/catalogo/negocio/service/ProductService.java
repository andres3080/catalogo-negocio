package com.catalogo.negocio.service;

import com.catalogo.negocio.model.Product;
import com.catalogo.negocio.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    @Transactional
    public Product save(Product product) {
        return productRepository.saveAndFlush(product);
    }

    public Product findById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Producto no encontrado"));
    }

    @Transactional
    public Product updateProduct(Long id, Product updatedProduct) {
        Product existingProduct = findById(id);
        existingProduct.setName(updatedProduct.getName());
        existingProduct.setCategory(updatedProduct.getCategory());
        existingProduct.setDescription(updatedProduct.getDescription());
        existingProduct.setWhatsappMessage(updatedProduct.getWhatsappMessage());

        if (updatedProduct.getImagePath() != null) {
            existingProduct.setImagePath(updatedProduct.getImagePath());
        }

        return productRepository.saveAndFlush(existingProduct);
    }

    public void deleteById(Long id) {
        productRepository.deleteById(id);
    }
}
