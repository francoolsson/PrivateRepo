package ProyectoIntegradorSpring.demo.Util;

import ProyectoIntegradorSpring.demo.DTO.ArticlesDTO;

import java.util.Comparator;

public class ArticleComparatorNameDes implements Comparator<ArticlesDTO> {

    @Override
    public int compare(ArticlesDTO o1, ArticlesDTO o2) {
        return o1.getName().compareToIgnoreCase(o2.getName());
    }
}
