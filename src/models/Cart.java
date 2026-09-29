package models;

import java.util.HashMap;
import java.util.Map;

public class Cart {
    // 1. Especificar los tipos genéricos del Map
    private final Map<Product, Integer> items;

    public Cart() {
        this.items = new HashMap<>();
    }

    public void agregarProducto(Product producto, int cantidad) {
        items.put(producto, items.getOrDefault(producto, 0) + cantidad);
    }

    public boolean eliminarProducto(Product producto) {
        if (items.containsKey(producto)) {
            items.remove(producto);
            return true;
        }
        return false;
    }

    // 2. Retornar el Map con sus tipos genéricos
    public Map<Product, Integer> getItems() {
        return items;
    }

    public void vaciar() {
        items.clear();
    }

    public double calcularTotal() {
        double total = 0.0;
        // 3. Tipar el Map. Entry para que reconozca getKey() como Product y getValue() como Integer
        for (Map.Entry<Product, Integer> entry : items.entrySet()) {
            total += entry.getKey().calcularPrecioFinal() * entry.getValue();
        }
        return total;
    }

    public boolean estaVacio() {
        return items.isEmpty();
    }
}