package ProyectoIntegradorSpring.demo.Services;

import ProyectoIntegradorSpring.demo.DTO.ArticlesDTO;
import ProyectoIntegradorSpring.demo.DTO.PurchaseDTO;
import ProyectoIntegradorSpring.demo.DTO.ResponsePurchaseDTO;
import ProyectoIntegradorSpring.demo.DTO.ShoppingCartDTO;

import java.util.List;
import java.util.Map;

public interface SearchEngine {

    List<ArticlesDTO> allProductsService ();
    List<ArticlesDTO> filterProductsService (Map<String,String> filters);
    ResponsePurchaseDTO responsePurchase(PurchaseDTO purchaseDTO);
    ShoppingCartDTO getShoppingCart (String user);
}
