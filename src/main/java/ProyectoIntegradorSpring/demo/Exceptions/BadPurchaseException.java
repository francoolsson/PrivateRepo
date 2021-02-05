package ProyectoIntegradorSpring.demo.Exceptions;

public class BadPurchaseException extends RuntimeException{

    public BadPurchaseException() {
    }

    public BadPurchaseException(String message) {
        super( message );
    }
}
