package ProyectoIntegradorSpring.demo.Util;

import ProyectoIntegradorSpring.demo.DTO.ArticlesDTO;

import java.util.Comparator;

public class ArticleComparatorPriceAsc implements Comparator<ArticlesDTO> {

    @Override
    public int compare(ArticlesDTO o1, ArticlesDTO o2) {
        if ((o1.getPrice()-o2.getPrice()==0)) return o1.getName().compareToIgnoreCase(o2.getName());
        else return o1.getPrice()-o2.getPrice();
    }
}
