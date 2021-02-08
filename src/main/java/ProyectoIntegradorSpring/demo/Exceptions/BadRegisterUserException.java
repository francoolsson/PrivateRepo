package ProyectoIntegradorSpring.demo.Exceptions;

public class BadRegisterUserException extends RuntimeException{

    public BadRegisterUserException() {
    }

    public BadRegisterUserException(String message) {
        super( message );
    }
}
