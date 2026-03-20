package com.catalogo.negocio.repository;

import com.catalogo.negocio.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
