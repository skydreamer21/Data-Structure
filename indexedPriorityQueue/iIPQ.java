package indexedHeap;

public interface iIPQ<E extends Comparable<E>> {
    void delete(E element);
    boolean contains(E element);
    E peek();
    E poll();
    void insert(E element);
    void update(E elemBefore, E elemAfter);
    boolean isEmpty();
    int size();
    

}
