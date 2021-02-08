package ProyectoIntegradorSpring.demo.Model;

import ProyectoIntegradorSpring.demo.DTO.ArticlesDTO;
import ProyectoIntegradorSpring.demo.Exceptions.BadFilterException;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Predicate;

public class ArticlesFilterPredicates {

   public static boolean articleFilter(ArticlesDTO articlesDTO, Map<String,String> mapFilter){

       List<Predicate<ArticlesDTO>> predicates = new ArrayList<>();
       Predicate<ArticlesDTO> init = (ArticlesDTO p) -> true;
       predicates.add(init);
       if(mapFilter.containsKey("category")) {
           Predicate<ArticlesDTO> p1 = (ArticlesDTO p) -> p.getCategory().toLowerCase().contains( mapFilter.get( "category" ).toLowerCase( Locale.ROOT ) );
           predicates.add(p1);
       }
       if(mapFilter.containsKey("brand")){
           Predicate<ArticlesDTO> p1 = (ArticlesDTO p) -> p.getBrand().toLowerCase().contains( mapFilter.get( "brand" ).toLowerCase( Locale.ROOT ) );
           predicates.add(p1);
       }
       if(mapFilter.containsKey("name")){
           Predicate<ArticlesDTO> p1 = (ArticlesDTO p) -> p.getName().toLowerCase().contains( mapFilter.get( "name" ).toLowerCase( Locale.ROOT ) );
           predicates.add(p1);
       }
       if(mapFilter.containsKey("price")){
           if (mapFilter.get("price").matches("^\\d+$")) {
               Predicate<ArticlesDTO> p1 = (ArticlesDTO p) -> p.getPrice() < (Integer.parseInt( mapFilter.get( "price" ) ));
               predicates.add( p1 );
           }
           else throw new BadFilterException("Price is not a number");
       }
       if(mapFilter.containsKey("quantity")){
           if (mapFilter.get("quantity").matches("^\\d+$")) {
               Predicate<ArticlesDTO> p1 = (ArticlesDTO p) -> p.getQuantity() < (Integer.parseInt( mapFilter.get( "quantity" ) ));
               predicates.add( p1 );
           }
           else throw new BadFilterException("Quantity is not a number");
       }
       if(mapFilter.containsKey("freeShipping")){
           if (mapFilter.get("freeShipping").matches("^[0,1]$")) {
               Predicate<ArticlesDTO> p1 = (ArticlesDTO p) -> p.getFreeShipping() == (Integer.parseInt( mapFilter.get( "freeShipping" ) ));
               predicates.add( p1 );
           }
           else throw new BadFilterException("Bad Free Shipping request");
       }
       if(mapFilter.containsKey("id")){
           if (mapFilter.get("id").matches("^\\d+$")) {
               Predicate<ArticlesDTO> p1 = (ArticlesDTO p) -> p.getId() == (Integer.parseInt( mapFilter.get( "id" ) ));
               predicates.add( p1 );
           }
           else throw new BadFilterException("Bad ID request");
       }

       //Todos los filtros
       return predicates.stream().reduce((p1,p2)->p1.and(p2)).get().test( articlesDTO );
   }
}
