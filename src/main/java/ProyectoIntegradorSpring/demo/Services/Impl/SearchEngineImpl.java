package ProyectoIntegradorSpring.demo.Services.Impl;

import ProyectoIntegradorSpring.demo.DAO.ArticleDAO;
import ProyectoIntegradorSpring.demo.DTO.*;
import ProyectoIntegradorSpring.demo.Exceptions.BadFilterException;
import ProyectoIntegradorSpring.demo.Exceptions.BadPurchaseException;
import ProyectoIntegradorSpring.demo.Services.SearchEngine;
import ProyectoIntegradorSpring.demo.Util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class SearchEngineImpl implements SearchEngine {
    private ArticleDAO articleDAO;

    SearchEngineImpl(@Autowired ArticleDAO articleDAO) {this.articleDAO = articleDAO; }

    private Integer order;
    private List<ArticlesDTO> toOrder;
    private MiFactorySort factorySort=new MiFactorySort();

    private List<ArticlesDTO> returnArticles(Map<String, String> filters) {
        toOrder= articleDAO.returnFilterProducts(filters);
        Sorter<ArticlesDTO> sort = factorySort.setProperties();
        if(order!=-1){
            switch (order){
                case 0: sort.sort(toOrder,new ArticleComparatorNameAsc());
                case 1: sort.sort(toOrder,new ArticleComparatorNameDes());
                case 2: sort.sort(toOrder,new ArticleComparatorPriceAsc());
                case 3: sort.sort(toOrder,new ArticleComparatorPriceDes());
            }
        }
        return toOrder;
    }

    @Override
    public List<ArticlesDTO> allProductsService() {
        order=-1;
        return returnArticles(new HashMap<>());
    }

    @Override
    public List<ArticlesDTO> filterProductsService(Map<String, String> filters) {
        order=-1;
        //Esta parte es para salva la petición get que puede tener como parámetro filter=freeShipping
        if (filters.containsKey("filter")){
            if (filters.get("filter").equals("freeShipping")){
                filters.remove("filter");
                filters.put("freeShipping","1");
            }
            else throw new BadFilterException("Attribute Filter Error");
        }
        //Aquí saco el atributo "Orden" y lo verifico
        if (filters.containsKey("order")){
            if (filters.get("order").matches("^[0,1,2,3]$")){
                order=Integer.parseInt(filters.get("order"));
                filters.remove("order");
            }
            else throw new BadFilterException("Order Filter Error");
        }
        //Verifico si todos los atributos para filtrar son correctos
        if(!articleDAO.isAttribute(filters)) throw new BadFilterException("Attributes Filters Error");
        else{
            //Verifico si hay respuestas para la combinación de filtros
            if(articleDAO.returnFilterProducts(filters).isEmpty()) throw new BadFilterException("Article Not Found");
            else {
                return returnArticles(filters);
            }
        }
    }

    @Override
    public ResponsePurchaseDTO responsePurchase(PurchaseDTO purchaseDTO) {
        List<ArticlesDTO> articlesList = new ArrayList<>();
        for (ItemsDTO items: purchaseDTO.getArticles()){
            Map<String,String> filter = new HashMap<>();
            filter.put("id",items.getProductId().toString());
            if(articleDAO.returnFilterProducts(filter).isEmpty()) throw new BadPurchaseException("ID="+items.getProductId()+" is not found");
            if(articleDAO.returnFilterProducts(filter).get(0).getQuantity()< items.getQuantity()) throw new BadPurchaseException(
                    "Quantity "+items.getQuantity()+" is greater than stored ("+articleDAO.returnFilterProducts(filter).get(0).getQuantity()+") in ID="+items.getProductId());
            ArticlesDTO articlesDTO = new ArticlesDTO();

            articlesDTO.setQuantity( items.getQuantity() );
            articlesDTO.setName( articleDAO.returnFilterProducts(filter).get(0).getName());
            articlesDTO.setBrand( articleDAO.returnFilterProducts(filter).get(0).getBrand());
            articlesDTO.setId( articleDAO.returnFilterProducts(filter).get(0).getId());
            articlesDTO.setCategory( articleDAO.returnFilterProducts(filter).get(0).getCategory());
            articlesDTO.setFreeShipping( articleDAO.returnFilterProducts(filter).get(0).getFreeShipping());
            articlesDTO.setPrestige( articleDAO.returnFilterProducts(filter).get(0).getPrestige());
            articlesDTO.setPrice( articleDAO.returnFilterProducts(filter).get(0).getPrice());
            if (items.getDiscount() != null) articlesDTO.setPrice(articlesDTO.getPrice()-articlesDTO.getPrice()* items.getDiscount()/100);
            else articlesDTO.setPrice( articleDAO.returnFilterProducts(filter).get(0).getPrice());
            articlesList.add(articlesDTO);
        }
        ReceiptDTO receiptDTO = new ReceiptDTO();
        receiptDTO.setId( articleDAO.newReceiptID() );
        receiptDTO.setArticles(articlesList);
        receiptDTO.setPrice(articlesList.stream().reduce(0,(price,u)->price+u.getPrice()*u.getQuantity(),Integer::sum));
        receiptDTO.setStatus("Pending?");
        ResponsePurchaseDTO responsePurchaseDTO = new ResponsePurchaseDTO();
        responsePurchaseDTO.setReceipt(receiptDTO);
        StatusDTO statusDTO= new StatusDTO();
        statusDTO.setCode(200);
        statusDTO.setMessage("Generated receipt");
        responsePurchaseDTO.setStatusCode(statusDTO);
        return responsePurchaseDTO;
    }
}
