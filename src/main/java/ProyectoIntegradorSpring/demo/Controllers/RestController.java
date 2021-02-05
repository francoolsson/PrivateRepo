package ProyectoIntegradorSpring.demo.Controllers;


import ProyectoIntegradorSpring.demo.DTO.ArticlesDTO;
import ProyectoIntegradorSpring.demo.Services.SearchEngine;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
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

}