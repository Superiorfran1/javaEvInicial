package service;

import exceptions.*;
import models.*;

import java.util.*;

public class StockService {
    private final Map<String, Product> productosMap;
    private final Map<String, User> usuariosMap;
    private final List<Pedido> historialPedidos;
    private final Cart carritoActual;

    public StockService() {
        this.productosMap = new HashMap<>();
        this.usuariosMap = new HashMap<>();
        this.historialPedidos = new ArrayList<>();
        this.carritoActual = new Cart();
        cargarDatosPrueba();
    }

    private void cargarDatosPrueba() {
        try {
            registrarProducto(new PhysicalProduct("P001", "Teclado Mecánico", 59.99, 10, 0.8, 4.50));
            registrarProducto(new PhysicalProduct("P002", "Monitor 27 Pulgadas", 189.90, 5, 4.2, 9.99));
            registrarProducto(new DigitalProduct("P003", "Antivirus Licencia 1 Año", 29.99, 50, 150.0, "STD-2026-A"));
            registrarProducto(new DigitalProduct("P004", "Curso Java Avanzado", 49.00, 100, 2500.0, "EDU-JAVA-2026"));

            registrarUsuario(new User("U001", "Carlos Gomez", "carlos@example.com", true));
            registrarUsuario(new User("U002", "Ana Martinez", "ana@example.com", true));
            registrarUsuario(new User("U003", "Luis Fernandez", "luis@example.com", false));
        } catch (StockException e) {
            System.err.println("Error al cargar datos de prueba: " + e.getMessage());
        }
    }

    public void registrarProducto(Product producto) throws StockException {
        if (productosMap.containsKey(producto.getId())) {
            throw new StockException("Ya existe un producto registrado con el ID: " + producto.getId());
        }
        productosMap.put(producto.getId(), producto);
    }

    public void darBajaProducto(String id) throws ProductNotFoundException {
        if (!productosMap.containsKey(id)) {
            throw new ProductNotFoundException("No se encontró ningún producto con el ID: " + id);
        }
        productosMap.remove(id);
    }

    // 1. Tipar la lista a List
    public List<Product> obtenerTodosProductos() {
        return new ArrayList<>(productosMap.values());
    }

    // 2. Tipar la lista a List
    public List<Product> buscarProductosPorNombre(String termino) {
        List<Product> resultado = new ArrayList<>();
        for (Product p : productosMap.values()) {
            if (p.getNombre().toLowerCase().contains(termino.toLowerCase())) {
                resultado.add(p);
            }
        }
        return resultado;
    }

    public void registrarUsuario(User usuario) throws StockException {
        if (usuariosMap.containsKey(usuario.getId())) {
            throw new StockException("Ya existe un usuario registrado con el ID: " + usuario.getId());
        }
        usuariosMap.put(usuario.getId(), usuario);
    }

    public void darBajaUsuario(String id) throws UserNotFoundException {
        User usuario = usuariosMap.get(id);
        if (usuario == null) {
            throw new UserNotFoundException("No se encontró ningún usuario con el ID: " + id);
        }
        usuario.setActivo(false);
    }

    // 3. Tipar la lista a List
    public List<User> obtenerUsuariosActivos() {
        List<User> activos = new ArrayList<>();
        for (User u : usuariosMap.values()) {
            if (u.isActivo()) {
                activos.add(u);
            }
        }
        return activos;
    }

    // 4. Se eliminan los bloques try-catch innecesarios para lanzar las excepciones directamente
    public void agregarAlCarrito(String idProducto, int cantidad) throws StockException {
        Product producto = productosMap.get(idProducto);
        if (producto == null) {
            try {
                throw new ProductNotFoundException("No existe un producto con ID: " + idProducto);
            } catch (ProductNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        if (cantidad <= 0) {
            throw new StockException("La cantidad debe ser mayor que cero.");
        }

        int cantidadEnCarrito = carritoActual.getItems().getOrDefault(producto, 0);
        if (producto.getStock() < (cantidadEnCarrito + cantidad)) {
            try {
                throw new NotEnoughStockException("Stock insuficiente para el producto '" + producto.getNombre()
                        + "'. Disponible: " + producto.getStock() + ", en carrito: " + cantidadEnCarrito);
            } catch (NotEnoughStockException e) {
                throw new RuntimeException(e);
            }
        }

        carritoActual.agregarProducto(producto, cantidad);
    }

    public void quitarDelCarrito(String idProducto) throws ProductNotFoundException {
        Product producto = productosMap.get(idProducto);
        if (producto == null || !carritoActual.eliminarProducto(producto)) {
            throw new ProductNotFoundException("El producto no esta presente en el carrito.");
        }
    }

    public Cart getCarritoActual() {
        return carritoActual;
    }

    public Pedido cerrarPedido(String idUsuario) throws StockException {
        User usuario = usuariosMap.get(idUsuario);
        if (usuario == null) {
            try {
                throw new UserNotFoundException("El usuario con ID " + idUsuario + " no existe.");
            } catch (UserNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        if (!usuario.isActivo()) {
            throw new StockException("El usuario " + usuario.getNombre() + " esta inactivo y no puede realizar compras.");
        }
        if (carritoActual.estaVacio()) {
            throw new StockException("El carrito esta vacío. Agregue productos antes de cerrar la compra.");
        }

        // 5. Especificar tipos genéricos en los entrySet
        for (Map.Entry<Product, Integer> entry : carritoActual.getItems().entrySet()) {
            Product p = entry.getKey();
            int cant = entry.getValue();
            if (p.getStock() < cant) {
                try {
                    throw new NotEnoughStockException("Stock insuficiente de " + p.getNombre() + " al procesar la orden.");
                } catch (NotEnoughStockException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        for (Map.Entry<Product, Integer> entry : carritoActual.getItems().entrySet()) {
            Product p = entry.getKey();
            p.setStock(p.getStock() - entry.getValue());
        }

        Pedido nuevoPedido = new Pedido(usuario, carritoActual.getItems());
        historialPedidos.add(nuevoPedido);
        carritoActual.vaciar();

        return nuevoPedido;
    }

    // 6. Tipar la lista a List
    public List<Pedido> getHistorialPedidos() {
        return historialPedidos;
    }
}