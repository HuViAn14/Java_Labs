package CarsCatalog;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Huk Vitaliy
 * @version 0.1.0
 * Represents a catalog of cars with methods to add, remove, and search for cars.
 */
public class CarCatalog {
    /**
     * @param cars List of cars, that contains objects of class Car
     */
    private HashMap<String, ArrayList<Car>> cars;

    /**
     * Car catalog constructor
     */
    public CarCatalog() {
        this.cars = new HashMap<String, ArrayList<Car>>();
    }

    /**
     * Adds a car to the catalog.
     * @param car The car to add.
     */
    public void addCar(Car car) {
        if(!cars.containsKey(car.getBrand()))
        {
            ArrayList<Car> l = new ArrayList<>();
            l.add(car);
            cars.put(car.getBrand(), l);
        }
        else
        {
            cars.get(car.getBrand()).add(car);
        }
    }

    /**
     * Removes a car from the catalog.
     * @param car The car to remove.
     */
    public void removeCar(Car car) {
        cars.get(car.getBrand()).remove(car);
    }

    /**
     * Finds cars in the catalog by brand.
     * @param brand The brand of cars to search for.
     * @return An ArrayList of cars matching the specified brand.
     */
    public ArrayList<Car> findCarsByBrand(String brand) {
        return cars.get(brand);
    }

    /**
     * Finds cars in the catalog by model.
     * @param model The model of cars to search for.
     * @return An ArrayList of cars matching the specified model.
     */
    public ArrayList<Car> findCarsByModel(String model) {
        ArrayList<Car> result = new ArrayList<>();
        for (List<Car> carBrand : cars.values())
        {
            for (Car car : carBrand) {
                if (car.getModel().equalsIgnoreCase(model)) {
                    result.add(car);
                }
            }
        }
        return result;
    }

    /**
     * Returns a list of all cars in the catalog sorted by price.
     * @return An ArrayList of cars sorted by price.
     */
    public ArrayList<Car> getAllCarsSortedByPrice() {
        HashMap<String, ArrayList<Car>> sortedCars = new HashMap<>(cars);
        ArrayList<Car> result = new ArrayList<>();
        for (List<Car> carBrand : sortedCars.values())
        {
            Collections.sort(carBrand, Comparator.comparingDouble(Car::getPrice));
            result.addAll(carBrand);
        }
        return result;
    }
}
