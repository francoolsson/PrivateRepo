package ProyectoIntegradorSpring.demo.Services.Impl;

import ProyectoIntegradorSpring.demo.DAO.Repository;
import ProyectoIntegradorSpring.demo.DTO.*;
import ProyectoIntegradorSpring.demo.Exceptions.BadFilterException;
import ProyectoIntegradorSpring.demo.Exceptions.BadPurchaseException;
import ProyectoIntegradorSpring.demo.Exceptions.BadRegisterUserException;
import ProyectoIntegradorSpring.demo.Services.SearchEngine;
import ProyectoIntegradorSpring.demo.Util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class SearchEngineImpl implements SearchEngine {
    private Repository repository;

    SearchEngineImpl(@Autowired Repository repository) {this.repository = repository; }

    private Integer order;
    private List<ArticlesDTO> toOrder;
    private MiFactorySort factorySort=new MiFactorySort();


    private List<ArticlesDTO> returnArticles(Map<String, String> filters) {
        toOrder= repository.returnFilterProducts(filters);
        Sorter<ArticlesDTO> sort = factorySort.setProperties();
        if(order!=-1){
            switch (order){
                case 0: sort.sort(toOrder,new ArticleComparatorNameAsc());
                    break;
                case 1: sort.sort(toOrder,new ArticleComparatorNameDes());
                    break;
                case 2: sort.sort(toOrder,new ArticleComparatorPriceAsc());
                    break;
                case 3: sort.sort(toOrder,new ArticleComparatorPriceDes());
                    break;
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
        if(!repository.isAttribute(filters)) throw new BadFilterException("Attributes Filters Error");
        else{
            //Verifico si hay respuestas para la combinación de filtros
            if(repository.returnFilterProducts(filters).isEmpty()) throw new BadFilterException("Article Not Found");
            else {
                return returnArticles(filters);
            }
        }
    }

    @Override
    public ResponsePurchaseDTO responsePurchase(PurchaseDTO purchaseDTO) {
        if (!repository.isUser( purchaseDTO.getUserName() )) throw new BadRegisterUserException("User "+purchaseDTO.getUserName()+ " does not exist");
        List<ArticlesDTO> articlesList = new ArrayList<>();
        for (ItemsDTO items: purchaseDTO.getArticles()){
            Map<String,String> filter = new HashMap<>();
            filter.put("id",items.getProductId().toString());
            if(repository.returnFilterProducts(filter).isEmpty()) throw new BadPurchaseException("ID="+items.getProductId()+" is not found");
            if(repository.returnFilterProducts(filter).get(0).getQuantity()< items.getQuantity()) throw new BadPurchaseException(
                    "Quantity "+items.getQuantity()+" is greater than stored ("+repository.returnFilterProducts(filter).get(0).getQuantity()+") in ID="+items.getProductId());
            if(items.getDiscount()>100 || items.getDiscount()<0) throw new BadPurchaseException("Discount "+items.getDiscount()+"in item "+items.getProductId()+" is not valid");
            ArticlesDTO articlesDTO = new ArticlesDTO();
            articlesDTO.setQuantity( items.getQuantity() );
            repository.returnFilterProducts(filter).get(0).setQuantity(repository.returnFilterProducts(filter).get(0).getQuantity()- items.getQuantity());
            articlesDTO.setName( repository.returnFilterProducts(filter).get(0).getName());
            articlesDTO.setBrand( repository.returnFilterProducts(filter).get(0).getBrand());
            articlesDTO.setId( repository.returnFilterProducts(filter).get(0).getId());
            articlesDTO.setCategory( repository.returnFilterProducts(filter).get(0).getCategory());
            articlesDTO.setFreeShipping( repository.returnFilterProducts(filter).get(0).getFreeShipping());
            articlesDTO.setPrestige( repository.returnFilterProducts(filter).get(0).getPrestige());
            articlesDTO.setPrice( repository.returnFilterProducts(filter).get(0).getPrice());
            if (items.getDiscount() != null) articlesDTO.setPrice(articlesDTO.getPrice()-articlesDTO.getPrice()* items.getDiscount()/100);
            else articlesDTO.setPrice( repository.returnFilterProducts(filter).get(0).getPrice());
            articlesList.add(articlesDTO);
        }
        ReceiptDTO receiptDTO = new ReceiptDTO();
        receiptDTO.setId( repository.newReceiptID() );
        receiptDTO.setArticles(articlesList);
        receiptDTO.setPrice(articlesList.stream().reduce(0,(price,u)->price+u.getPrice()*u.getQuantity(),Integer::sum));
        receiptDTO.setStatus("Pending");
        receiptDTO.setUser(purchaseDTO.getUserName());
        repository.loadReceiptDatabase(receiptDTO);
        ResponsePurchaseDTO responsePurchaseDTO = new ResponsePurchaseDTO();
        responsePurchaseDTO.setReceipt(receiptDTO);
        StatusDTO statusDTO= new StatusDTO();
        statusDTO.setCode(200);
        statusDTO.setMessage("Generated receipt");
        responsePurchaseDTO.setStatusCode(statusDTO);
        return responsePurchaseDTO;
    }


    @Override
    public ShoppingCartDTO getShoppingCart (String user) {
        if (!repository.isUser( user )) throw new BadRegisterUserException("User "+user+ " does not exist");
        List<ReceiptDTO> receipts= repository.getReceipts(user);
        ShoppingCartDTO shoppingCart = new ShoppingCartDTO();
        if (receipts.isEmpty()) throw new BadRegisterUserException("User "+user+" has no pending products");
        else {
            shoppingCart.setName(user);
            shoppingCart.setPrice(receipts.stream().reduce( 0,(price,u)->price+u.getPrice(),Integer::sum));
            shoppingCart.setReceipts(receipts);
            receipts.stream().forEach(u-> u.setStatus("Delivered"));
        }
        return shoppingCart;
    }

    @Override
    public List<ReceiptDTO> getReceipts() {
        return repository.getAllReceipts();
    }


    @Override
    public StatusDTO registerUser(UserDTO userDTO) {
        if (userDTO.getUser()==null) throw new BadRegisterUserException("Required field User");
        if (userDTO.getName()==null) throw new BadRegisterUserException("Required field Name");
        if (repository.isUser( userDTO.getUser() )) throw new BadRegisterUserException("User "+userDTO.getUser()+ " already exists");
        if (userDTO.getState()==null) throw new BadRegisterUserException("Required field State");
        userDTO.setId( repository.newUserID() );
        repository.loadUserDatabase( userDTO );
        StatusDTO statusDTO = new StatusDTO();
        statusDTO.setCode(200);
        statusDTO.setMessage("User "+userDTO.getUser()+" (name "+userDTO.getName()+") has been loaded successfully with id "+userDTO.getId());
        return statusDTO;

    }

    @Override
    public List<UserDTO> allUsers() {
        return repository.getAllUsers();
    }


    @Override
    public List<UserDTO> filterUsers(UserDTO userDTO) {
        if (repository.filterUsers( userDTO ).isEmpty()) throw new BadFilterException("There is no user that matches the filters");
        return repository.filterUsers( userDTO );
    }
}
