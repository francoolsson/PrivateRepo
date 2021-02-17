package ProyectoIntegradorSpring.demo.Controllers;


import ProyectoIntegradorSpring.demo.DTO.*;
import ProyectoIntegradorSpring.demo.Model.ArticleModel;
import ProyectoIntegradorSpring.demo.Services.SearchEngine;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@org.springframework.web.bind.annotation.RestController
public class RestController {


    private SearchEngine searchEngine;
    public RestController(@Autowired SearchEngine searchEngine) {this.searchEngine = searchEngine;}


    //Controlador para filtros de productos. Trabajo con un map.
    @GetMapping("/api/v1/articles")
    public List<ArticlesDTO> getProducts (@RequestParam Map<String,String> filters){

       if(filters.isEmpty()){
           return searchEngine.allProductsService();
       }
       else{
           return searchEngine.filterProductsService( filters );
       }
    }

    //Controlador para generar una compra.
    @PostMapping("/api/v1/purchase-request")
    public ResponsePurchaseDTO purchaseResponse(@RequestBody PurchaseDTO purchaseDTO) {
        return searchEngine.responsePurchase(purchaseDTO);
    }

    //Controlador para obtener el shoppingCart para un usuario en particular.
    @GetMapping("/api/v1/shopping")
    public ShoppingCartDTO getProducts (@RequestParam String user){
       return searchEngine.getShoppingCart( user );
    }

    //Controlador para obtener todos lso recibos.
    @GetMapping("/api/v1/receipts")
    public List<ReceiptDTO> getAllReceipt (){
        return searchEngine.getReceipts();
    }

    //Controlador para registrar usuario a partir de un UserDTO
    @PostMapping("/api/v1/register-user")
    public StatusDTO registerUser(@RequestBody UserDTO userDTO) {
        return searchEngine.registerUser( userDTO );
    }

    //Controlador para obtener todos los usuarios
    @GetMapping("/api/v1/all-users")
    public List<UserDTO> getAllUsers (){
        return searchEngine.allUsers();
    }

    //Controlador para filtrar usuario a partir de un userDTO obtenido de un get request.
    @GetMapping("/api/v1/users")
    public List<UserDTO> getFilterUsers (UserDTO userDTO){
        return searchEngine.filterUsers(userDTO);
    }



}
