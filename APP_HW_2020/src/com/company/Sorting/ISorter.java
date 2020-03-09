package com.company.Sorting;


public interface ISorter<T extends Comparable<? super T>> {
    MemoryCell<T>[] sort(MemoryCell<T>[] toSort);
    T[] sort(T[] toSort);
}
