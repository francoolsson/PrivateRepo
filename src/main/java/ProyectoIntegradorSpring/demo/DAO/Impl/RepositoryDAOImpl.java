package ProyectoIntegradorSpring.demo.DAO.Impl;


import ProyectoIntegradorSpring.demo.DAO.RepositoryDAO;
import ProyectoIntegradorSpring.demo.DTO.ArticlesDTO;
import ProyectoIntegradorSpring.demo.DTO.ReceiptDTO;
import ProyectoIntegradorSpring.demo.DTO.ShoppingCartDTO;
import ProyectoIntegradorSpring.demo.DTO.UserDTO;
import ProyectoIntegradorSpring.demo.Model.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

@org.springframework.stereotype.Repository
public class RepositoryDAOImpl implements RepositoryDAO {

    private final Map<Integer, ArticlesDTO> articlesDatabase;
    private final ArrayList<String> attributes;
    private final Map<Integer,ReceiptDTO> receiptDatabase;
    private final Map<Integer, UserDTO> usersDatabase;
    private final Map<Integer, ShoppingCartDTO> shoppingCartDatabase;
    private final UserFilterFactory userFilterFactory;


    //Inicialización de las "Bases de datos" en el constructor
    public RepositoryDAOImpl() {
        this.articlesDatabase = loadArticleDatabase();
        this.attributes=getAttributes();
        this.receiptDatabase=loadReceiptDatabase();
        this.shoppingCartDatabase=loadShoppingCartDatabase();
        this.usersDatabase=loadUsersDatabase();
        this.userFilterFactory=loadFilterFactory();
    }


    private Map<Integer,ReceiptDTO> loadReceiptDatabase(){
        return new HashMap<>();
    }
    private Map<Integer,ShoppingCartDTO> loadShoppingCartDatabase(){
        return new HashMap<>();
    }
    private Map<Integer,UserDTO> loadUsersDatabase() {return new HashMap<>();}
    private UserFilterFactory loadFilterFactory() {return new UserFilterFactoryImpl(); }

    //Me permite obtener una lista con los atributos de ArticleModel. Uso esta función para contrastar los parámetros
    //solicitados en un filtro.
    private ArrayList<String> getAttributes() {
        Field[] fields = ArticleModel.class.getDeclaredFields();
        ArrayList<String> attributes = new ArrayList<>();
        String[] help;
        for (Field field : fields) {
            help = field.toString().split( "\\." );
            attributes.add( help[(help.length) - 1] );
        }
        return attributes;
    }

    //Creo la base de datos con los valores del JSON.
    private Map<Integer, ArticlesDTO> loadArticleDatabase(){
        HashMap<Integer, ArticlesDTO> database = new HashMap<>();
        List<ArticleModel> productsList= new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            productsList = objectMapper.readValue( new File( "src/main/java/ProyectoIntegradorSpring/demo/Model/database.json" ),
                    new TypeReference<List<ArticleModel>>(){});
        } catch(Exception e){
            e.printStackTrace();
        }
        Integer i=0;
        for (ArticleModel articleModel : productsList){
            ArticlesDTO articlesDTO = new ArticlesDTO();
            articlesDTO.setId(i);
            articlesDTO.setBrand( articleModel.getBrand());
            articlesDTO.setPrice( articleModel.getPrice());
            articlesDTO.setName( articleModel.getName());
            articlesDTO.setCategory( articleModel.getCategory());
            articlesDTO.setFreeShipping( articleModel.getFreeShipping() );
            articlesDTO.setPrestige( articleModel.getPrestige() );
            articlesDTO.setQuantity( articleModel.getQuantity() );
            database.put( articlesDTO.getId(), articlesDTO );
            i++;
        }
        return database;
    }



    @Override
    //Función para verificar si algun parámetro del filtro existe como atributo de un artículo.
    public Boolean isAttribute(Map<String, String> filters) {
        for (String str : filters.keySet()){
            if (!attributes.contains(str)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public List<ArticlesDTO> returnFilterProducts(Map<String, String> filters) {
        //Filtrado de artículos. Armo un filtro de predicados enviandole el mapa de filtros que viene del servicio.
        return getArticlesDatabase().stream().filter( u -> ArticlesFilterPredicates.articleFilter(u,filters)).collect( Collectors.toList());
    }

    @Override
    //Función para obtener el ID para un recibo.
    public Integer newReceiptID() {
        return receiptDatabase.size();
    }

    @Override
    //Carga de un recibo en la base de datos
    public void loadReceiptDatabase(ReceiptDTO receiptDTO) {
        receiptDatabase.put(receiptDTO.getId(),receiptDTO);
    }


    @Override
    //Función para obtener los recibos para un usuario en particular y en estado "Pending"
    public List<ReceiptDTO> getReceipts(String user) {
        return receiptDatabase.values().stream().filter(u->u.getUser().matches(user)).filter(u->u.getStatus().matches("Pending")).collect( Collectors.toList());
    }

    @Override
    //Función para devolver todos los recibos.
    public List<ReceiptDTO> getAllReceipts() {
        return receiptDatabase.values().stream().collect( Collectors.toList());
    }

    @Override
    //Función parao obtener el ID para los usuarios.
    public Integer newUserID() {
        return usersDatabase.size();
    }

    @Override
    //Carga de usuario
    public void loadUserDatabase(UserDTO userDTO) {
        usersDatabase.put( userDTO.getId(),userDTO);
    }

    @Override
    //Obtener todos los usuarios
    public List<UserDTO> getAllUsers() {
        return usersDatabase.values().stream().collect( Collectors.toList());
    }

    @Override
    //Verificar si existe un usuario
    public Boolean isUser(String user) {
        return usersDatabase.values().stream().anyMatch( u-> u.getUser().matches( user ) );
    }

    @Override
    //Filtro para un usuario. En este caso genere una "Factoría de filtros" y no trabajo con un mapper desde el controlador,
    //sino con un userDTO.
    public List<UserDTO> filterUsers(UserDTO userDTO) {
        return usersDatabase.values().stream().filter(userFilterFactory.getUsersFilters( userDTO )).collect( Collectors.toList());
    }





    //Para testeo teòricamente
    @Override
    public void deleteArticlesDatabase() {
        articlesDatabase.clear();
    }

    @Override
    public void loadTestDatabase(String path) {
        ObjectMapper objectMapper = new ObjectMapper();
        List<ArticleModel> productsList= new ArrayList<>();
        try {
            productsList = objectMapper.readValue( new File( path ),
                    new TypeReference<List<ArticleModel>>(){});
        } catch(Exception e){
            e.printStackTrace();
        }
        Integer i=articlesDatabase.size();
        for (ArticleModel articleModel : productsList){
            ArticlesDTO articlesDTO = new ArticlesDTO();
            articlesDTO=objectMapper.convertValue( articleModel,ArticlesDTO.class );
            articlesDTO.setId(i);
            articlesDatabase.put( articlesDTO.getId(), articlesDTO );
            i++;
        }
    }

    @Override
    public List<ArticlesDTO> getArticlesDatabase() {
        return articlesDatabase.values().stream().collect( Collectors.toList());
    }
}
