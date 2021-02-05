package ProyectoIntegradorSpring.demo.Util;

import java.util.Comparator;
import java.util.List;

public interface Sorter <T>{
    public void sort (List<T> arr, Comparator<T> c);
}
