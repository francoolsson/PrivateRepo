package ProyectoIntegradorSpring.demo.Controllers;


import ProyectoIntegradorSpring.demo.DTO.ErrorDTO;
import ProyectoIntegradorSpring.demo.DTO.ResponsePurchaseDTO;
import ProyectoIntegradorSpring.demo.DTO.StatusDTO;
import ProyectoIntegradorSpring.demo.Exceptions.BadFilterException;
import ProyectoIntegradorSpring.demo.Exceptions.BadPurchaseException;
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

    @ExceptionHandler(BadPurchaseException.class)
    public ResponseEntity<ResponsePurchaseDTO> handleException(BadPurchaseException badPurchaseException) {
        ResponsePurchaseDTO responsePurchaseDTO = new ResponsePurchaseDTO();
        StatusDTO statusDTO = new StatusDTO();
        statusDTO.setCode(404);
        statusDTO.setMessage(badPurchaseException.getMessage());
        responsePurchaseDTO.setStatusCode(statusDTO);
        return new ResponseEntity<>(responsePurchaseDTO, HttpStatus.NOT_FOUND);
    }
}


