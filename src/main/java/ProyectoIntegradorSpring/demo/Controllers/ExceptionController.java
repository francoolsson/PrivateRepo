package ProyectoIntegradorSpring.demo.Controllers;


import ProyectoIntegradorSpring.demo.DTO.ErrorDTO;
import ProyectoIntegradorSpring.demo.DTO.ResponsePurchaseDTO;
import ProyectoIntegradorSpring.demo.DTO.StatusDTO;
import ProyectoIntegradorSpring.demo.Exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


//Control de excepciones. Genere diferentes exepciones para ir probando cosas. Lo ideal seria que todas las respuestas
//(menos la de un purchase) sean de formato StatusDTO.
@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler(BadURLException.class)
    public ResponseEntity<ErrorDTO> handleException(BadURLException badURLException) {
        ErrorDTO errorDTO = new ErrorDTO();
        errorDTO.setError( badURLException.getMessage() );
        return new ResponseEntity<>( errorDTO, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BadFilterException.class)
    public ResponseEntity<ErrorDTO> handleException(BadFilterException badFilterException) {
        ErrorDTO errorDTO = new ErrorDTO();
        errorDTO.setError( badFilterException.getMessage() );
        return new ResponseEntity<>( errorDTO, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BadSortPropertiesException.class)
    public ResponseEntity<ErrorDTO> handleException(BadSortPropertiesException badSortPropertiesException) {
        ErrorDTO errorDTO = new ErrorDTO();
        errorDTO.setError( badSortPropertiesException.getMessage() );
        return new ResponseEntity<>( errorDTO, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(BadPurchaseException.class)
    public ResponseEntity<ResponsePurchaseDTO> handleException(BadPurchaseException badPurchaseException) {
        ResponsePurchaseDTO responsePurchaseDTO = new ResponsePurchaseDTO();
        StatusDTO statusDTO = new StatusDTO();
        statusDTO.setCode(404);
        statusDTO.setMessage(badPurchaseException.getMessage());
        responsePurchaseDTO.setStatusCode(statusDTO);
        return new ResponseEntity<>(responsePurchaseDTO, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BadRegisterUserException.class)
    public ResponseEntity<StatusDTO> handleException(BadRegisterUserException badRegisterUserException) {
        StatusDTO statusDTO = new StatusDTO();
        statusDTO.setMessage( badRegisterUserException.getMessage() );
        statusDTO.setCode( 400 );
        return new ResponseEntity<>( statusDTO, HttpStatus.BAD_REQUEST);
    }
}


