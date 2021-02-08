package ProyectoIntegradorSpring.demo.Controllers;


import ProyectoIntegradorSpring.demo.DTO.*;
import ProyectoIntegradorSpring.demo.Services.SearchEngine;

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

    @GetMapping("/api/v1/articles")
    public List<ArticlesDTO> getProducts (@RequestParam Map<String,String> filters){

       if(filters.isEmpty()){
           return searchEngine.allProductsService();
       }
       else{
           return searchEngine.filterProductsService( filters );
       }
    }

    @PostMapping("/api/v1/purchase-request")
    public ResponsePurchaseDTO purchaseResponse(@RequestBody PurchaseDTO purchaseDTO) {
        return searchEngine.responsePurchase(purchaseDTO);
    }

    @GetMapping("/api/v1/shopping")
    public ShoppingCartDTO getProducts (@RequestParam String user){
       return searchEngine.getShoppingCart( user );
    }

    @GetMapping("/api/v1/receipts")
    public List<ReceiptDTO> getAllReceipt (){
        return searchEngine.getReceipts();
    }

}
