package com.catalogo.negocio.controller;

import com.catalogo.negocio.model.Product;
import com.catalogo.negocio.model.ProductCategory;
import com.catalogo.negocio.service.ProductService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class CatalogController {

    private final ProductService productService;

    @Value("${app.whatsapp-number}")
    private String whatsappNumber;

    @Value("${app.business-name}")
    private String businessName;

    @Value("${app.logo-path:/img/logo.jpg}")
    private String logoPath;

    @Value("${app.social-instagram:}")
    private String instagramUrl;

    @Value("${app.social-facebook:}")
    private String facebookUrl;

    @Value("${app.social-tiktok:}")
    private String tiktokUrl;

    @Value("${app.about-title}")
    private String aboutTitle;

    @Value("${app.about-text}")
    private String aboutText;

    public CatalogController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping({"/", "/catalog"})
    public String catalog(Model model) {
        List<CatalogItemView> items = productService.findAll().stream()
                .map(this::toCatalogItem)
                .toList();
        Map<ProductCategory, List<CatalogItemView>> itemsByCategory = items.stream()
                .collect(Collectors.groupingBy(CatalogItemView::category));

        List<CategorySectionView> categorySections = Arrays.stream(ProductCategory.values())
                .filter(itemsByCategory::containsKey)
                .map(category -> new CategorySectionView(
                        category.name(),
                        category.getDisplayName(),
                        itemsByCategory.get(category)))
                .toList();

        model.addAttribute("categorySections", categorySections);
        model.addAttribute("businessName", businessName);
        model.addAttribute("logoPath", logoPath);
        model.addAttribute("instagramUrl", instagramUrl);
        model.addAttribute("facebookUrl", facebookUrl);
        model.addAttribute("tiktokUrl", tiktokUrl);
        model.addAttribute("aboutTitle", aboutTitle);
        model.addAttribute("aboutText", aboutText);
        model.addAttribute("floatingWhatsappLink", "https://wa.me/" + whatsappNumber + "?text="
                + URLEncoder.encode("Hola, quiero una cotizacion para un trabajo en aluminio o acero.", StandardCharsets.UTF_8));
        model.addAttribute("heroWhatsappLink", "https://wa.me/" + whatsappNumber + "?text="
                + URLEncoder.encode("Hola, quiero asesoramiento para un proyecto en aluminio o acero.", StandardCharsets.UTF_8));
        return "catalog";
    }

    private CatalogItemView toCatalogItem(Product product) {
        ProductCategory category = product.getCategory() != null ? product.getCategory() : ProductCategory.PUERTAS_ALUMINIO;
        String message = product.getWhatsappMessage();
        if (message == null || message.isBlank()) {
            message = "Hola, quiero cotizar este producto: " + product.getName();
        }
        String encodedMessage = URLEncoder.encode(message, StandardCharsets.UTF_8);
        String link = "https://wa.me/" + whatsappNumber + "?text=" + encodedMessage;
        return new CatalogItemView(product.getId(), product.getName(), category, product.getImagePath(), link, product.getDescription());
    }

    public record CatalogItemView(Long id, String name, ProductCategory category, String imagePath, String whatsappLink,
                                  String description) {
    }

    public record CategorySectionView(String slug, String title, List<CatalogItemView> items) {
    }
}
