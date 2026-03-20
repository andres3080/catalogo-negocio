package com.catalogo.negocio.model;

public enum ProductCategory {
    PUERTAS_ALUMINIO("Puertas de aluminio"),
    VENTANAS("Ventanas"),
    VENTANAS_CORREDIZAS("Ventanas corredizas"),
    BARANDAS("Barandas en acero inoxidable y vidrio templado"),
    MAMPARAS_BANO("Mamparas de bano"),
    MUEBLES_COCINA("Muebles para cocina"),
    ESTRUCTURAS_METALICAS("Estructuras metalicas");

    private final String displayName;

    ProductCategory(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
