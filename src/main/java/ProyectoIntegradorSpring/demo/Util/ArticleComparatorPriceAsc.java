package ProyectoIntegradorSpring.demo.Util;

import ProyectoIntegradorSpring.demo.DTO.ArticlesDTO;

import java.util.Comparator;

public class ArticleComparatorPriceAsc implements Comparator<ArticlesDTO> {

    @Override
    public int compare(ArticlesDTO o1, ArticlesDTO o2) {
        return o1.getPrice()-o2.getPrice();
    }
}
