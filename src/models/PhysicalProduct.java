package models;

public class PhysicalProduct extends Product {
    private final double pesoKg;
    private final double costeEnvio;

    public PhysicalProduct(String id, String nombre, double precio, int stock, double pesoKg, double costeEnvio) {
        super(id, nombre, precio, stock);
        this.pesoKg = pesoKg;
        this.costeEnvio = costeEnvio;
    }

    @Override
    public double calcularPrecioFinal() {
        return getPrecio() + costeEnvio;
    }

    @Override
    public String toString() {
        return super.toString() + " | Peso: " + pesoKg + "kg | Envío: " + String.format("%.2f", costeEnvio) + " EUR (Físico)";
    }
}