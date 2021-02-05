package ProyectoIntegradorSpring.demo.Util;

import java.util.Comparator;
import java.util.List;

public class QuickSortSorterImple<T> implements Sorter<T> {

    @Override
    public void sort(List<T> arr, Comparator<T> c) {
        quickSort(arr,0,(arr.size())-1,c);
    }

    private void quickSort(List<T> arr, int begin, int end,Comparator<T> c) {
        if (begin < end) {
            int partitionIndex = partition(arr, begin, end, c);

            quickSort(arr, begin, partitionIndex-1,c);
            quickSort(arr, partitionIndex+1, end,c);
        }
    }

    private int partition(List<T> arr, int begin, int end,Comparator<T> c) {
        T pivot = arr.get(end);
        int i = (begin-1);

        for (int j = begin; j < end; j++) {
            if (c.compare(arr.get(j),pivot) <= (0)) {
                i++;

                T swapTemp = arr.get(i);
                arr.set(i,arr.get(j));
                arr.set(j,swapTemp);
            }
        }

        T swapTemp = arr.get(i+1);
        arr.set(i+1,arr.get(end));
        arr.set(end,swapTemp);

        return i+1;
    }
}
