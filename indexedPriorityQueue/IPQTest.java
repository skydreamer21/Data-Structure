package indexedHeap;

public class IPQTest {
    public static void main(String[] args) {
        IndexedPriorityQueue<Car> pq = new IndexedPriorityQueue<>();
        Car[] cars = {
                new Car("morning", 1000),
                new Car("Lexton", 5000),
                new Car("avente", 2000),
                new Car("C class", 10000),
                new Car("Great Car", 20000),
                new Car("Apple Car", 12500)
        };
        for (Car car : cars) {
            pq.insert(car);
        }
        
        pq.showMaps();
        

        pq.update(cars[0], new Car("morning", 13000));
//        pq.showPQ();
        
        pq.showMaps();
        
        System.out.printf("peek : %s\n", pq.peek());
        
        while (!pq.isEmpty()) {
            System.out.println(pq.poll());
        }
        
    }
}
