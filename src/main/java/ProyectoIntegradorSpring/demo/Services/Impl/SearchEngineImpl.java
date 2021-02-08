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

//Search engine tiene todos los servicios. No tuve tiempo de dividir en controladores para que queda mas ordenado
@Service
public class SearchEngineImpl implements SearchEngine {
    private Repository repository;

    SearchEngineImpl(@Autowired Repository repository) {this.repository = repository; }

    private Integer order;
    private List<ArticlesDTO> toOrder;
    private MiFactorySort factorySort=new MiFactorySort();

    //Filtro de productos. Trabaja con un mapa string-string. Cualquier llamada a esta función ya verifico que las keys
    //del mapa correspondan a atributos válidos.
    private List<ArticlesDTO> returnArticles(Map<String, String> filters) {
        toOrder= repository.returnFilterProducts(filters);
        //Obtengo el objeto sort de un factory. Puedo setear el mismo en Properties dentro de la carpeta util.
        Sorter<ArticlesDTO> sort = factorySort.setProperties();
        //Cualquier parte del programa que llame a esta función y no necesite ordenamiento setea order=-1;
        if(order!=-1){
            //Switcheo order para ver si me pidieron un tipo de ordenamiento. Cualquier llamada a esta función que necesite
            // un orden setea el order necesario.
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


    //Función que devuelve todos los productos.
    @Override
    public List<ArticlesDTO> allProductsService() {
        order=-1;
        return returnArticles(new HashMap<>());
    }


    //Función que devuelve los productos filtrados. Verifica que los paramétros de filtros solicitados sean correctos.
    //Además sustituye el caso filter=freeShipping por freeShipping=1, para que pueda ser correctamente interpretado por la
    //Función de filtro. Además si existe el filtro "order" lo verifica, lo remueve de la lista de filtro y setea Order para
    //Que lo interprete la función de filtro.
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

    //Servicio para comprobar una compra y generar el recibo.
    @Override
    public ResponsePurchaseDTO responsePurchase(PurchaseDTO purchaseDTO) {

        //Verifica si existe el usuario, sino lanza una excepción
        if (!repository.isUser( purchaseDTO.getUserName() )) throw new BadRegisterUserException("User "+purchaseDTO.getUserName()+ " does not exist");
        List<ArticlesDTO> articlesList = new ArrayList<>();

        //Recorro todos los items dentro de PurchaseDTO, que es una orden de compra. Verifico que existe el id del producto, que exista la cantidad solicitad y que el descuento
        //se encuentre entre 0 y 100%. Luego de recorrer de manera correcta todos los items obtengo una lista de articulos correspondientes a los items; y con el descuento aplicado al precio.
        for (ItemsDTO items: purchaseDTO.getArticles()){

            //Para buscar los productos reutilizo mi función de filtros, creando un mapa con ID=id ingresado en el item.
            Map<String,String> filter = new HashMap<>();
            filter.put("id",items.getProductId().toString());
            if(repository.returnFilterProducts(filter).isEmpty()) throw new BadPurchaseException("ID="+items.getProductId()+" is not found");
            if(repository.returnFilterProducts(filter).get(0).getQuantity()< items.getQuantity()) throw new BadPurchaseException(
                    "Quantity "+items.getQuantity()+" is greater than stored ("+repository.returnFilterProducts(filter).get(0).getQuantity()+") in ID="+items.getProductId());
            if(items.getDiscount()>100 || items.getDiscount()<0) throw new BadPurchaseException("Discount "+items.getDiscount()+"in item "+items.getProductId()+" is not valid");
            ArticlesDTO articlesDTO = new ArticlesDTO();
            articlesDTO.setQuantity( items.getQuantity() );
            //Cuando obtengo el articulo correspondiente me creo un nuevo DTO de articulo para guardarlo en el recibo. Toda esta parte debería cambiarla por un Mapper.
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
        //Genero un recibo y seteo los valores correspondientes. Si llegue a este punto la compra se puede realizar sin ningun problema.
        //Las excepciones lanzadas en el for anterior se manejan con un @ControllerAdvice.
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

    //Genero un carrito de compras con las ordenes PENDIENTES para un usuario. Cuando se genera las ordenes de compra
    //los recibos almacenados como pendientes pasan a entregados. Debería ampliar una funcionalidad para que pueda
    //ver el carrito y otra para entregar el carrito.
    @Override
    public ShoppingCartDTO getShoppingCart (String user) {
        //Verifico si existe el usuario.
        if (!repository.isUser( user )) throw new BadRegisterUserException("User "+user+ " does not exist");
        //Obtengo todos los recibos para un user que estan en estado pendiente.
        List<ReceiptDTO> receipts= repository.getReceipts(user);
        //Si la lista de recibos está vacía tiro una excepción
        if (receipts.isEmpty()) throw new BadRegisterUserException("User "+user+" has no pending products");
        //Genero el carrito y lo entrego. Aca ya modifico los recibos a Entregados. Debería generar mas funcionalidades para el carrito.
        ShoppingCartDTO shoppingCart = new ShoppingCartDTO();
        shoppingCart.setName(user);
        shoppingCart.setPrice(receipts.stream().reduce( 0,(price,u)->price+u.getPrice(),Integer::sum));
        shoppingCart.setReceipts(receipts);
        receipts.stream().forEach(u-> u.setStatus("Delivered"));
        return shoppingCart;
    }

    @Override
    //Obtengo todos los recibos
    public List<ReceiptDTO> getReceipts() {
        return repository.getAllReceipts();
    }


    @Override
    //Registro de usuarios. Ante cualquier anormalidad en la carga lanza excepción. Le faltan verificaciones.
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
    //Obtengo todos los usuarios.
    public List<UserDTO> allUsers() {
        return repository.getAllUsers();
    }


    @Override
    //Obtengo los usuarios filtrados. En este caso arme el filtro con un request GET de formato userDTO y no con un get de formato map (como en la parte de filtrado de artículos).
    public List<UserDTO> filterUsers(UserDTO userDTO) {
        if (repository.filterUsers( userDTO ).isEmpty()) throw new BadFilterException("There is no user that matches the filters");
        return repository.filterUsers( userDTO );
    }
}
