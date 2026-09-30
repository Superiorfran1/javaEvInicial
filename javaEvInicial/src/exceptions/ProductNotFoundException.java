package exceptions;

public class ProductNotFoundException extends Exception{
    public ProductNotFoundException(String mensaje) {
        super(mensaje);
    }
}
