package ProyectoIntegradorSpring.demo.Services;

import ProyectoIntegradorSpring.demo.DTO.*;
import org.apache.catalina.User;

import java.util.List;
import java.util.Map;

public interface SearchEngine {

    List<ArticlesDTO> allProductsService ();
    List<ArticlesDTO> filterProductsService (Map<String,String> filters);
    ResponsePurchaseDTO responsePurchase(PurchaseDTO purchaseDTO);
    ShoppingCartDTO getShoppingCart (String user);
    List<ReceiptDTO> getReceipts();
    StatusDTO registerUser(UserDTO userDTO);
    List<UserDTO> allUsers();
    List<UserDTO> filterUsers(UserDTO userDTO);
}
