package ProyectoIntegradorSpring.demo.DAO;

import ProyectoIntegradorSpring.demo.DTO.ArticlesDTO;

import java.util.List;
import java.util.Map;


public interface ArticleDAO {

    List<ArticlesDTO> returnAllArticles();
    Boolean isAttribute (Map<String,String> filters);
    List<ArticlesDTO> returnFilterProducts(Map<String,String> filters);
    Integer newReceiptID ();
}
