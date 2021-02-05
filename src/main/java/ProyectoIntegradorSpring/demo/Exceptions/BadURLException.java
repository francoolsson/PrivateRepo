package ProyectoIntegradorSpring.demo.Exceptions;

public class BadURLException extends RuntimeException{

    public BadURLException() {
    }

    public BadURLException(String message) {
        super( message );
    }
}
