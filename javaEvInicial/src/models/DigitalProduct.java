package models;

public class DigitalProduct extends Product {
    private final double tamanoMB;
    private final String licencia;

    public DigitalProduct(String id, String nombre, double precio, int stock, double tamanoMB, String licencia) {
        super(id, nombre, precio, stock);
        this.tamanoMB = tamanoMB;
        this.licencia = licencia;
    }

    @Override
    public double calcularPrecioFinal() {
        return getPrecio();
    }

    @Override
    public String getTipo() {
        return "Digital";
    }

    @Override
    public String toString() {
        return super.toString() + " | Tamaño: " + tamanoMB + "MB | Licencia: " + licencia + " (Digital)";
    }
}