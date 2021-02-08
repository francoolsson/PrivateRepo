package ProyectoIntegradorSpring.demo.Services;

import ProyectoIntegradorSpring.demo.DTO.*;

import java.util.List;
import java.util.Map;

public interface SearchEngine {

    List<ArticlesDTO> allProductsService ();
    List<ArticlesDTO> filterProductsService (Map<String,String> filters);
    ResponsePurchaseDTO responsePurchase(PurchaseDTO purchaseDTO);
    ShoppingCartDTO getShoppingCart (String user);
    List<ReceiptDTO> getReceipts();
}
