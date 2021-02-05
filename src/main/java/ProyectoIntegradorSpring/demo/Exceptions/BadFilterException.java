package ProyectoIntegradorSpring.demo.Exceptions;

public class BadFilterException extends RuntimeException{

    public BadFilterException() {
    }

    public BadFilterException(String message) {
        super( message );
    }
}
