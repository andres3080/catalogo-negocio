package com.catalogo.negocio.model;

public enum ProductCategory {
    PUERTAS_ALUMINIO("Puertas abatibles"),
    PUERTAS_CORREDIZAS("Puertas corredizas"),
    BARANDAS("Barandas en acero"),
    VENTANAS("Ventanas"),
    MAMPARAS_BANO("Mamparas de bano"),
    MUEBLES_COCINA("Muebles para cocina"),
    ESPEJOS("Espejos"),
    ESTRUCTURAS_METALICAS("Estructuras metalicas"),
    VENTANAS_CORREDIZAS("Ventanas corredizas", false);

    private final String displayName;
    private final boolean visible;

    ProductCategory(String displayName) {
        this(displayName, true);
    }

    ProductCategory(String displayName, boolean visible) {
        this.displayName = displayName;
        this.visible = visible;
    }

    public String getDisplayName() {
        return displayName;
    }

    public boolean isVisible() {
        return visible;
    }
}
