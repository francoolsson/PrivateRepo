package ProyectoIntegradorSpring.demo.Services;

import ProyectoIntegradorSpring.demo.DTO.ArticlesDTO;

import java.util.List;
import java.util.Map;

public interface SearchEngine {

    List<ArticlesDTO> allProductsService ();
    List<ArticlesDTO> filterProductsService (Map<String,String> filters);
}
