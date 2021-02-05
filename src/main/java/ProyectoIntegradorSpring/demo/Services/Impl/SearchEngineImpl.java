package ProyectoIntegradorSpring.demo.Services.Impl;

import ProyectoIntegradorSpring.demo.DAO.ArticleDAO;
import ProyectoIntegradorSpring.demo.DTO.ArticlesDTO;
import ProyectoIntegradorSpring.demo.Exceptions.BadFilterException;
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

    @Override
    public List<ArticlesDTO> allProductsService() {
        return articleDAO.returnAllArticles();
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
        }
    }
}
