package exceptions;

public class ExportException extends Exception {
    public ExportException(String mensaje) {
        super(mensaje);
    }

    public ExportException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
