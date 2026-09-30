package exceptions;

public class NotEnoughStockException extends Exception{
    public NotEnoughStockException(String mensaje) {
        super(mensaje);
    }
}
