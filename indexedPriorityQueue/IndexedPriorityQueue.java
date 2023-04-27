package indexedHeap;

import java.util.Arrays;
import java.util.HashMap;
import java.util.NoSuchElementException;

public class IndexedPriorityQueue<E extends Comparable<E>> implements iIPQ<E> {
    static final int INIT_SIZE = 100;
    static final int ROOT = 1;
    static final int EMPTY = -1;
    private Object[] queue;
    private HashMap<E, Integer> keyMap;
    private Object[] values;
    private int[] positionMap;
    private int[] inverseMap;
    private int size;
    private int keyIndex;
    
    public IndexedPriorityQueue() {
        queue = new Object[INIT_SIZE + 1];
        keyMap = new HashMap<>();
        values = new Object[INIT_SIZE + 1];
        positionMap = new int[INIT_SIZE + 1];
        Arrays.fill(positionMap, -1);
        inverseMap = new int[INIT_SIZE + 1];
        Arrays.fill(inverseMap, -1);
        size = 0;
        keyIndex = 0;
    }
    
    @Override
    public boolean isEmpty() {
        return size==0;
    }
    
    @Override
    public int size() {
        return size;
    }
    
    @Override
    public void insert(E element) {
        // 배열이 꽉 찰 경우 2배로 늘려준다.
        if (size + 1 >= queue.length) {
            resize();
        }
        
        // BiMap 관리
        keyMap.put(element, keyIndex);
        values[keyIndex] = element;
        
        positionMap[keyIndex] = size+1;
        inverseMap[size+1] = keyIndex;
        
        
        siftUp(size+1, element);
        size++;
        keyIndex++;
        
        if (keyIndex >= values.length) {
            resizeKeyIndex();
        }
    }
    
    private void resize() {
        int newSize = queue.length * 2;
        Object[] newQueue = new Object[newSize];
        System.arraycopy(queue, 1, newQueue, 1, size);
        queue = newQueue;
        
        int[] newInverseMap = new int[newSize];
        Arrays.fill(newInverseMap, EMPTY);
        System.arraycopy(inverseMap, 1, newInverseMap, 1, size);
        inverseMap = newInverseMap;
    }
    
    private void resizeKeyIndex() {
        int newSize = values.length * 2;
        Object[] newValues = new Object[newSize];
        System.arraycopy(values, 0, newValues, 1, values.length);
        values = newValues;
        
        int[] newPositionMap = new int[newSize];
        Arrays.fill(newPositionMap, EMPTY);
        System.arraycopy(positionMap, 0, newPositionMap, 0, positionMap.length);
        positionMap = newPositionMap;
    }
    
    private void siftUp(int idx, E element) {
        while (idx > ROOT) {
            int parentIdx = getParentIdx(idx);
//            System.out.printf("idx : %d, parent : %d \n", idx, parentIdx);
            Object parent = queue[parentIdx];
//            System.out.println(parent);
            
            // 새로 들어가는 요소의 우선순위가 부모보다 작다면 끝
            if (element.compareTo((E) parent) >= 0) {
                break;
            }
            
            swap(idx, parentIdx);
            queue[idx] = parent;
            idx = parentIdx;
        
        }
        
        queue[idx] = element;
    }
    
    private void swap(int i, int j) {
        positionMap[inverseMap[j]] = i;
        positionMap[inverseMap[i]] = j;
        
        int temp = inverseMap[i];
        inverseMap[i] = inverseMap[j];
        inverseMap[j] = temp;
    }
    
    @Override
    public E poll() {
        if (isEmpty()) {
            throw new NoSuchElementException("우선순위 큐가 비어있습니다.");
        }
        int polledKi = inverseMap[ROOT];
        swap(ROOT, size);
        
        E result = (E) queue[ROOT];
        E target = (E) queue[size];
//        System.out.printf("result : %s\n", result);
//        System.out.printf("target : %s\n", target);
        queue[size] = null;
        size--;
        siftDown(ROOT, target);
        
        // BiMap 관리
        values[keyMap.get(result)] = null;
        keyMap.remove(result);
        
        // map 관리
        positionMap[polledKi] = EMPTY;
        inverseMap[size+1] = EMPTY;
        
        return result;
    }
    
    private void siftDown(int idx, E target) {
        int parentIdx = idx;
        int childIdx; // 교환될 자식 인덱스
        
        while((childIdx = getLeftChildIdx(parentIdx)) <= size) {
            
            E child = (E)queue[childIdx];
            int rightChildIdx = getRightChildIdx(parentIdx);
            
            // 오른쪽이 더 높은 우선순위를 갖는다면 오른쪽 자식으로 교체
            if (rightChildIdx <= size && ((E)queue[rightChildIdx]).compareTo(child) < 0) {
                childIdx = rightChildIdx;
                child = (E) queue[rightChildIdx];
            }
            
            // 만약 부모가 더 높은 우선순위라면 종료
            if (target.compareTo(child) < 0) {
                break;
            }
            
            // 부모 인덱스를 자식으로 교체
            swap(childIdx, parentIdx);
            
            queue[parentIdx] = child;
            parentIdx = childIdx;
        }
        
        queue[parentIdx] = target;
    }
    
    private int getParentIdx(int idx) {
        return idx / 2;
    }
    
    private int getLeftChildIdx(int idx) {
        return idx * 2;
    }
    
    private int getRightChildIdx(int idx) {
        return idx*2 + 1;
    }
    
    @Override
    public void delete(E element) {
        if (!contains(element)) {
            throw new NoSuchElementException("해당 원소는 우선순위큐에 없습니다.");
        }
        
        int ki = keyMap.get(element);
        delete(ki);
        
        keyMap.remove(element);
    }
    
    private void delete(int ki) {
        int idx = positionMap[ki];
        swap(idx, size);
        
        E target = (E) queue[size];
        queue[size] = null;
        size--;
        
        siftUp(idx, target);
        if (queue[idx] == target) {
            siftDown(idx, target);
        }
        
        values[ki] = null;
        positionMap[ki] = EMPTY;
        inverseMap[size+1] = EMPTY;
    }
    
    @Override
    public boolean contains(E element) {
        return keyMap.containsKey(element);
    }
    
    @Override
    public E peek() {
        if (isEmpty()) {
            throw new NoSuchElementException("우선순위 큐가 비어있습니다.");
        }
        return (E) queue[ROOT];
    }
    
    @Override
    public void update(E elemBefore, E elemAfter) {
        if (!contains(elemBefore)) {
            throw new NoSuchElementException("해당 원소는 우선순위큐에 없습니다.");
        }
        int ki = keyMap.get(elemBefore);
        int idx = positionMap[ki];
        values[ki] = elemAfter;
        keyMap.remove(elemBefore);
        keyMap.put(elemAfter, ki);
        siftUp(idx, elemAfter);
        if (queue[idx] == elemAfter) {
            siftDown(idx, elemAfter);
        }
        
    }
    
    public void showMaps() {
        System.out.printf("pm : %s\n", Arrays.toString(Arrays.copyOf(positionMap, keyIndex)));
        System.out.printf("im : %s\n", Arrays.toString(Arrays.copyOf(inverseMap, size+1)));
    }
    
    public void showPQ() {
        System.out.println("==== pq 내부 ====");
        for (int i = 1; i <= size; i++) {
            System.out.println(queue[i]);
        }
    }
}
