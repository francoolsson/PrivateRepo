package ProyectoIntegradorSpring.demo.DAO.Impl;


import ProyectoIntegradorSpring.demo.DAO.ArticleDAO;
import ProyectoIntegradorSpring.demo.DTO.ArticlesDTO;
import ProyectoIntegradorSpring.demo.DTO.ReceiptDTO;
import ProyectoIntegradorSpring.demo.Model.ModelPredicates;
import ProyectoIntegradorSpring.demo.Model.ArticleModel;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class ArticleDAOImpl implements ArticleDAO {

    private Map<Integer, ArticlesDTO> articlesDatabase;
    private ArrayList<String> attributes;
    private Map<Integer,ReceiptDTO> receiptDatabase;

    public ArticleDAOImpl() {
        this.articlesDatabase = loadArticleDatabase();
        this.attributes=getAttributes();
        this.receiptDatabase=loadReceiptDatabase();
    }

    private Map<Integer,ReceiptDTO> loadReceiptDatabase(){
        return new HashMap<>();
    }

    //Me permite obtener una lista con los atributos de ArticleModel
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

    private Map<Integer, ArticlesDTO> loadArticleDatabase(){
        HashMap<Integer, ArticlesDTO> database = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper();
        List<ArticleModel> productsList= new ArrayList<>();
        try {
            productsList = mapper.readValue( new File( "src/main/java/ProyectoIntegradorSpring/demo/Model/database.json" ),
                    new TypeReference<List<ArticleModel>>(){});
        } catch(Exception e){
            e.printStackTrace();
        };
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
    public List<ArticlesDTO> returnAllArticles()  {
        return articlesDatabase.values().stream().collect( Collectors.toList());
    }


    @Override
    public Boolean isAttribute(Map<String, String> filters) {
        Boolean flag=true;
        for (String str : filters.keySet()){
            if (!attributes.contains(str)) flag=false;
        }
        return flag;
    }

    @Override
    public List<ArticlesDTO> returnFilterProducts(Map<String, String> filters) {

        List<ArticlesDTO> filterProducts=articlesDatabase.values().stream().filter( u -> ModelPredicates.articleFilter(u,filters)).collect( Collectors.toList());
        return filterProducts;
    }

    @Override
    public Integer newReceiptID() {
        return receiptDatabase.size();
    }


}
