package view;

import controller.Controller;
import models.*;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Main {
    private static Controller controller = new Controller();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        boolean salir = false;
        while (!salir) {
            mostrarMenuPrincipal();
            String opcion = scanner.nextLine().trim();
            System.out.println();
            switch (opcion) {
                case "1":
                    menuGestionProductos();
                    break;
                case "2":
                    menuGestionUsuarios();
                    break;
                case "3":
                    menuGestionCarrito();
                    break;
                case "4":
                    cerrarPedidoYFacturar();
                    break;
                case "5":
                    consultarHistorialPedidos();
                    break;
                case "6":
                    exportarHistorialCSV();
                    break;
                case "0":
                    salir = true;
                    System.out.println("Saliendo de la aplicacion...");
                    break;
                default:
                    System.out.println("Opcion no valida. Intente de nuevo.");
            }
            System.out.println();
        }
        scanner.close();
    }

    private static void mostrarMenuPrincipal() {
        System.out.println("========================================");
        System.out.println("    SISTEMA DE GESTION DE INVENTARIO");
        System.out.println("========================================");
        System.out.println("1. Gestionar productos");
        System.out.println("2. Gestionar usuarios");
        System.out.println("3. Gestionar carrito de compras");
        System.out.println("4. Cerrar pedido / Generar factura");
        System.out.println("5. Consultar historial de pedidos");
        System.out.println("6. Exportar historial de pedidos a CSV (Excel)");
        System.out.println("0. Salir");
        System.out.print("Seleccione una opcion: ");
    }

    private static void menuGestionProductos() {
        System.out.println("--- GESTION DE PRODUCTOS ---");
        System.out.println("1. Dar de alta producto");
        System.out.println("2. Dar de baja producto");
        System.out.println("3. Listar todos los productos");
        System.out.println("4. Buscar productos por nombre");
        System.out.print("Seleccione una opcion: ");
        String op = scanner.nextLine().trim();

        switch (op) {
            case "1":
                altaProducto();
                break;
            case "2":
                bajaProducto();
                break;
            case "3":
                listarProductos();
                break;
            case "4":
                buscarProductos();
                break;
            default:
                System.out.println("Opcion invalida.");
        }
    }

    private static void altaProducto() {
        try {
            System.out.print("Tipo de producto (1: Fisico, 2: Digital): ");
            String tipo = scanner.nextLine().trim();
            System.out.print("ID: ");
            String id = scanner.nextLine().trim();
            System.out.print("Nombre: ");
            String nombre = scanner.nextLine().trim();
            System.out.print("Precio base: ");
            double precio = Double.parseDouble(scanner.nextLine().trim());
            System.out.print("Stock: ");
            int stock = Integer.parseInt(scanner.nextLine().trim());

            boolean exito = false;
            if (tipo.equals("1")) {
                System.out.print("Peso (kg): ");
                double peso = Double.parseDouble(scanner.nextLine().trim());
                System.out.print("Gastos de envio: ");
                double envio = Double.parseDouble(scanner.nextLine().trim());
                exito = controller.registrarProductoFisico(id, nombre, precio, stock, peso, envio);
            } else if (tipo.equals("2")) {
                System.out.print("Tamaño (MB): ");
                double tam = Double.parseDouble(scanner.nextLine().trim());
                System.out.print("Licencia: ");
                String lic = scanner.nextLine().trim();
                exito = controller.registrarProductoDigital(id, nombre, precio, stock, tam, lic);
            } else {
                System.out.println("Tipo no valido.");
                return;
            }

            if (exito) {
                System.out.println("Producto dado de alta correctamente.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Error: Formato numérico incorrecto.");
        }
    }

    private static void bajaProducto() {
        System.out.print("Ingrese ID del producto a eliminar: ");
        String id = scanner.nextLine().trim();
        if (controller.eliminarProducto(id)) {
            System.out.println("Producto eliminado correctamente.");
        }
    }

    private static void listarProductos() {
        List<Product> lista = controller.obtenerTodosLosProductos();
        if (lista.isEmpty()) {
            System.out.println("No hay productos registrados.");
        } else {
            System.out.println("\n--- LISTA DE PRODUCTOS ---");
            for (Product p : lista) {
                System.out.println(p);
            }
        }
    }

    private static void buscarProductos() {
        System.out.print("Termino de busqueda: ");
        String termino = scanner.nextLine().trim();
        List<Product> resultado = controller.buscarProductosPorNombre(termino);
        if (resultado.isEmpty()) {
            System.out.println("No se encontraron coincidencias.");
        } else {
            System.out.println("\n--- RESULTADOS DE BUSQUEDA ---");
            for (Product p : resultado) {
                System.out.println(p);
            }
        }
    }

    private static void menuGestionUsuarios() {
        System.out.println("--- GESTION DE USUARIOS ---");
        System.out.println("1. Alta de usuario");
        System.out.println("2. Baja de usuario (Desactivar)");
        System.out.println("3. Listar usuarios activos");
        System.out.print("Seleccione una opcion: ");
        String op = scanner.nextLine().trim();

        switch (op) {
            case "1":
                altaUsuario();
                break;
            case "2":
                bajaUsuario();
                break;
            case "3":
                listarUsuariosActivos();
                break;
            default:
                System.out.println("Opcion invalida.");
        }
    }

    private static void altaUsuario() {
        System.out.print("ID Usuario: ");
        String id = scanner.nextLine().trim();
        System.out.print("Nombre: ");
        String nombre = scanner.nextLine().trim();
        System.out.print("Email: ");
        String email = scanner.nextLine().trim();

        if (controller.registrarUsuario(id, nombre, email)) {
            System.out.println("Usuario dado de alta correctamente.");
        }
    }

    private static void bajaUsuario() {
        System.out.print("Ingrese ID del usuario a dar de baja: ");
        String id = scanner.nextLine().trim();
        if (controller.desactivarUsuario(id)) {
            System.out.println("Usuario desactivado correctamente.");
        }
    }

    private static void listarUsuariosActivos() {
        List<User> activos = controller.obtenerUsuariosActivos();
        if (activos.isEmpty()) {
            System.out.println("No hay usuarios activos registrados.");
        } else {
            System.out.println("\n--- USUARIOS ACTIVOS ---");
            for (User u : activos) {
                System.out.println(u);
            }
        }
    }

    private static void menuGestionCarrito() {
        System.out.println("--- GESTION DEL CARRITO ---");
        System.out.println("1. Añadir producto al carrito");
        System.out.println("2. Quitar producto del carrito");
        System.out.println("3. Ver contenido y total");
        System.out.print("Seleccione una opcion: ");
        String op = scanner.nextLine().trim();

        switch (op) {
            case "1":
                agregarAlCarrito();
                break;
            case "2":
                quitarDelCarrito();
                break;
            case "3":
                verCarrito();
                break;
            default:
                System.out.println("Opcion invalida.");
        }
    }

    private static void agregarAlCarrito() {
        try {
            System.out.print("ID del producto: ");
            String id = scanner.nextLine().trim();
            System.out.print("Cantidad: ");
            int cantidad = Integer.parseInt(scanner.nextLine().trim());

            if (controller.agregarAlCarrito(id, cantidad)) {
                System.out.println("Producto añadido al carrito.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Error: La cantidad debe ser un entero válido.");
        }
    }

    private static void quitarDelCarrito() {
        System.out.print("ID del producto a quitar: ");
        String id = scanner.nextLine().trim();
        if (controller.quitarDelCarrito(id)) {
            System.out.println("Producto eliminado del carrito.");
        }
    }

    private static void verCarrito() {
        Cart carrito = controller.obtenerCarritoActual();
        if (carrito.estaVacio()) {
            System.out.println("El carrito esta vacio.");
            return;
        }

        System.out.println("\n--- CONTENIDO DEL CARRITO ---");
        for (Map.Entry<Product, Integer> entry : carrito.getItems().entrySet()) {
            Product p = entry.getKey();
            int cant = entry.getValue();
            System.out.println("- " + p.getNombre() + " (ID: " + p.getId() + ") | Cantidad: "
                    + cant + " | Subtotal: " + String.format("%.2f", p.calcularPrecioFinal() * cant) + " EUR");
        }
        System.out.printf("TOTAL ACUMULADO: %.2f EUR%n", carrito.calcularTotal());
    }

    private static void cerrarPedidoYFacturar() {
        System.out.print("Ingrese el ID del usuario que realiza la compra: ");
        String idUsuario = scanner.nextLine().trim();

        Pedido pedido = controller.procesarPedido(idUsuario);
        if (pedido != null) {
            System.out.println("\n¡Pedido procesado con éxito!");
            pedido.imprimirFactura();
        }
    }

    private static void consultarHistorialPedidos() {
        List<Pedido> historial = controller.obtenerHistorialPedidos();
        if (historial.isEmpty()) {
            System.out.println("No hay pedidos registrados en el historial.");
            return;
        }

        System.out.println("\n--- HISTORIAL DE PEDIDOS ---");
        for (Pedido p : historial) {
            p.imprimirFactura();
        }
    }

    private static void exportarHistorialCSV() {
        System.out.print("Nombre del fichero [historial_pedidos.csv]: ");
        String ruta = scanner.nextLine().trim();
        if (ruta.isEmpty()) {
            ruta = "historial_pedidos.csv";
        }
        String generado = controller.exportarHistorialCSV(ruta);
        if (generado != null) {
            System.out.println("Historial exportado correctamente en: " + generado);
        }
    }
}