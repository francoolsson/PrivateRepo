package ProyectoIntegradorSpring.demo.Controllers;


import ProyectoIntegradorSpring.demo.DTO.ErrorDTO;
import ProyectoIntegradorSpring.demo.Exceptions.BadFilterException;
import ProyectoIntegradorSpring.demo.Exceptions.BadSortPropertiesException;
import ProyectoIntegradorSpring.demo.Exceptions.BadURLException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

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

}


