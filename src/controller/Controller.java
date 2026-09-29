package controller;

import exceptions.*;
import models.*;
import service.StockService;

import java.util.List;

public class Controller {
    private final StockService servicio;

    public Controller() {
        this.servicio = new StockService();
    }

    // --- GESTIÓN DE PRODUCTOS ---

    public boolean registrarProductoFisico(String id, String nombre, double precio, int stock, double peso, double envio) {
        try {
            servicio.registrarProducto(new PhysicalProduct(id, nombre, precio, stock, peso, envio));
            return true;
        } catch (StockException e) {
            System.out.println("Error: " + e.getMessage());
            return false;
        }
    }

    public boolean registrarProductoDigital(String id, String nombre, double precio, int stock, double tamano, String licencia) {
        try {
            servicio.registrarProducto(new DigitalProduct(id, nombre, precio, stock, tamano, licencia));
            return true;
        } catch (StockException e) {
            System.out.println("Error: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminarProducto(String id) {
        try {
            servicio.darBajaProducto(id);
            return true;
        } catch (ProductNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
            return false;
        }
    }

    public List<Product> obtenerTodosLosProductos() {
        return servicio.obtenerTodosProductos();
    }

    public List<Product> buscarProductosPorNombre(String termino) {
        return servicio.buscarProductosPorNombre(termino);
    }

    // --- GESTIÓN DE USUARIOS ---

    public boolean registrarUsuario(String id, String nombre, String email) {
        try {
            servicio.registrarUsuario(new User(id, nombre, email, true));
            return true;
        } catch (StockException e) {
            System.out.println("Error: " + e.getMessage());
            return false;
        }
    }

    public boolean desactivarUsuario(String id) {
        try {
            servicio.darBajaUsuario(id);
            return true;
        } catch (UserNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
            return false;
        }
    }

    public List<User> obtenerUsuariosActivos() {
        return servicio.obtenerUsuariosActivos();
    }

    // --- GESTIÓN DEL CARRITO Y PEDIDOS ---

    public boolean agregarAlCarrito(String idProducto, int cantidad) {
        try {
            servicio.agregarAlCarrito(idProducto, cantidad);
            return true;
        } catch (StockException e) {
            System.out.println("Error: " + e.getMessage());
            return false;
        }
    }

    public boolean quitarDelCarrito(String idProducto) {
        try {
            servicio.quitarDelCarrito(idProducto);
            return true;
        } catch (ProductNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
            return false;
        }
    }

    public Cart obtenerCarritoActual() {
        return servicio.getCarritoActual();
    }

    public Pedido procesarPedido(String idUsuario) {
        try {
            return servicio.cerrarPedido(idUsuario);
        } catch (StockException e) {
            System.out.println("Error al procesar el pedido: " + e.getMessage());
            return null;
        }
    }

    public List<Pedido> obtenerHistorialPedidos() {
        return servicio.getHistorialPedidos();
    }
}