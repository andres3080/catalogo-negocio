package com.catalogo.negocio.controller;

import com.catalogo.negocio.model.Product;
import com.catalogo.negocio.model.ProductCategory;
import com.catalogo.negocio.service.ImageStorageService;
import com.catalogo.negocio.service.ProductService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class AdminController {

    private final ProductService productService;
    private final ImageStorageService imageStorageService;

    public AdminController(ProductService productService, ImageStorageService imageStorageService) {
        this.productService = productService;
        this.imageStorageService = imageStorageService;
    }

    @GetMapping("/admin/products")
    public String adminProducts(Model model) {
        model.addAttribute("products", productService.findAll());
        model.addAttribute("productForm", new ProductForm());
        model.addAttribute("categories", java.util.Arrays.stream(ProductCategory.values())
                .filter(ProductCategory::isVisible)
                .toList());
        return "admin-products";
    }

    @PostMapping("/admin/products")
    public String createProduct(@Valid @ModelAttribute("productForm") ProductForm form,
                                BindingResult bindingResult,
                                @RequestParam("imageFile") MultipartFile imageFile) {
        if (bindingResult.hasErrors()) {
            return "redirect:/admin/products?error";
        }

        Product product = new Product();
        product.setName(form.getName());
        product.setCategory(form.getCategory());
        product.setDescription(form.getDescription());
        product.setWhatsappMessage(form.getWhatsappMessage());
        product.setImagePath(imageStorageService.save(imageFile));
        productService.save(product);
        return "redirect:/admin/products?created";
    }

    @GetMapping("/admin/products/{id}/edit")
    public String editProductPage(@PathVariable Long id, Model model) {
        Product product = productService.findById(id);

        ProductForm form = new ProductForm();
        form.setName(product.getName());
        form.setCategory(product.getCategory() != null ? product.getCategory() : ProductCategory.PUERTAS_ALUMINIO);
        form.setDescription(product.getDescription());
        form.setWhatsappMessage(product.getWhatsappMessage());

        model.addAttribute("productId", product.getId());
        model.addAttribute("productImagePath", product.getImagePath());
        model.addAttribute("productForm", form);
        model.addAttribute("categories", java.util.Arrays.stream(ProductCategory.values())
                .filter(ProductCategory::isVisible)
                .toList());
        return "edit-product";
    }

    @PostMapping("/admin/products/{id}/edit")
    public String updateProduct(@PathVariable Long id,
                                @Valid @ModelAttribute("productForm") ProductForm form,
                                BindingResult bindingResult,
                                @RequestParam("imageFile") MultipartFile imageFile) {
        if (bindingResult.hasErrors()) {
            return "redirect:/admin/products/" + id + "/edit?error";
        }

        Product product = productService.findById(id);
        product.setName(form.getName());
        product.setCategory(form.getCategory());
        product.setDescription(form.getDescription());
        product.setWhatsappMessage(form.getWhatsappMessage());

        String newImagePath = imageStorageService.save(imageFile);
        if (newImagePath != null) {
            product.setImagePath(newImagePath);
        }

        productService.save(product);
        return "redirect:/admin/products?updated";
    }

    @PostMapping("/admin/products/{id}/delete")
    public String deleteProduct(@PathVariable Long id) {
        productService.deleteById(id);
        return "redirect:/admin/products?deleted";
    }

    public static class ProductForm {
        @NotBlank
        @Size(max = 120)
        private String name = "";

        private ProductCategory category = ProductCategory.PUERTAS_ALUMINIO;

        @Size(max = 400)
        private String description = "";

        @Size(max = 255)
        private String whatsappMessage = "";

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public ProductCategory getCategory() {
            return category;
        }

        public void setCategory(ProductCategory category) {
            this.category = category;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getWhatsappMessage() {
            return whatsappMessage;
        }

        public void setWhatsappMessage(String whatsappMessage) {
            this.whatsappMessage = whatsappMessage;
        }
    }
}
