package exceptions;

public class UserNotFoundException extends Exception{
    public UserNotFoundException(String mensaje) {
        super(mensaje);
    }
}
