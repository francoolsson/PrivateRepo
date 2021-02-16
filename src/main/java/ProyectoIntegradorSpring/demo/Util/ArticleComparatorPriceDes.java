package ProyectoIntegradorSpring.demo.Util;

import ProyectoIntegradorSpring.demo.DTO.ArticlesDTO;

import java.util.Comparator;

public class ArticleComparatorPriceDes implements Comparator<ArticlesDTO> {

    @Override
    public int compare(ArticlesDTO o1, ArticlesDTO o2) {
        if ((o2.getPrice()-o1.getPrice()==0)) return o1.getName().compareToIgnoreCase(o2.getName());
        else return o2.getPrice()-o1.getPrice();
    }
}
