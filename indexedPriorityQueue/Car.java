package indexedHeap;

import java.util.Objects;

public class Car implements Comparable<Car> {
    private String name;
    private int price;
    
    public Car(String name, int price) {
        this.name = name;
        this.price = price;
    }
    
    @Override
    public int compareTo(Car o) {
        return o.price - this.price;
    }
    
    @Override
    public String toString() {
        return "Car{" +
                "name='" + name + '\'' +
                ", price=" + price +
                '}';
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Car car = (Car) o;
        return price == car.price && Objects.equals(name, car.name);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(name, price);
    }
}
