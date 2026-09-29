package models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Pedido {
    private static int contador = 1;
    private final String idPedido;
    private final LocalDateTime fecha;
    private final User usuario;
    // 1. Tipar la lista con LineaPedido
    private final List<LineaPedido> lineas;
    private final double total;

    // 2. Tipar el Map que recibe el constructor con Product e Integer
    public Pedido(User usuario, Map<Product, Integer> carritoItems) {
        this.idPedido = "PED-" + String.format("%04d", contador++);
        this.fecha = LocalDateTime.now();
        this.usuario = usuario;
        this.lineas = new ArrayList<>();

        // 3. Tipar la entrada del Map en el bucle
        for (Map.Entry<Product, Integer> entry : carritoItems.entrySet()) {
            this.lineas.add(new LineaPedido(entry.getKey(), entry.getValue()));
        }

        this.total = calcularTotalPedido();
    }

    private double calcularTotalPedido() {
        double total = 0.0;
        for (LineaPedido lp : lineas) {
            total += lp.getSubtotal();
        }
        return total;
    }

    public void imprimirFactura() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        System.out.println("==========================================================");
        System.out.println("                    FACTURA DE COMPRA                     ");
        System.out.println("==========================================================");
        System.out.println("Nº Pedido: " + idPedido);
        System.out.println("Fecha:     " + fecha.format(formatter));
        System.out.println("Cliente:   " + usuario.getNombre() + " (" + usuario.getEmail() + ")");
        System.out.println("----------------------------------------------------------");
        System.out.printf("%-10s %-22s %-6s %-10s %-10s%n", "ID", "Producto", "Cant.", "P.Unit", "Subtotal");
        System.out.println("----------------------------------------------------------");

        for (LineaPedido lp : lineas) {
            System.out.printf("%-10s %-22s %-6d %-10.2f %-10.2f%n",
                    lp.getProducto().getId(),
                    truncarTexto(lp.getProducto().getNombre()),
                    lp.getCantidad(),
                    lp.getPrecioUnitario(),
                    lp.getSubtotal());
        }

        System.out.println("----------------------------------------------------------");
        System.out.printf("TOTAL A PAGAR: %39.2f EUR%n", total);
        System.out.println("==========================================================\n");
    }

    private String truncarTexto(String texto) {
        if (texto.length() > 20) {
            return texto.substring(0, 20 - 3) + "...";
        }
        return texto;
    }
}