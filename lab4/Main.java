import java.io.IOException;

import CarsCatalog.*;

/**
 * @author Huk Vitaliy
 * @version 0.1.0
 */

public class Main {
    
    /** 
     * @param args
     * @throws IOException 
     */
    public static void main(String[] args) throws IOException {
        // Створення декількох об'єктів класу Car
        Car car1 = new Car("Toyota", "Corolla", 2020, EngineType.PETROL, 25000.0);
        Car car2 = new Car("Tesla", "Model S", 2021, EngineType.ELECTRIC, 80000.0);
        Car car3 = new Car("BMW", "X5", 2019, EngineType.DIESEL, 60000.0);

        // Створення каталогу автомобілів та додавання до нього автомобілів
        CarCatalog catalog = new CarCatalog();
        catalog.addCar(car1);
        catalog.addCar(car2);
        catalog.addCar(car3);

        // Виведення даних каталогу
        System.out.println("All cars in the catalog:");
        for (Car car : catalog.getAllCarsSortedByPrice()) {
            car.printDetails();
        }

        // Пошук автомобілів за маркою "Toyota"
        String brandToSearch = "Toyota";
        System.out.println("\nCars with brand '" + brandToSearch + "':");
        for (Car car : catalog.findCarsByBrand(brandToSearch)) {
            car.printDetails();
        }

        // Вилучення автомобіля з каталогу та оновлення даних каталогу
        catalog.removeCar(car1);

        // Виведення оновлених даних каталогу
        System.out.println("\nAll cars in the updated catalog:");
        for (Car car : catalog.getAllCarsSortedByPrice()) {
            car.printDetails();
        }

        System.out.println("Press enter to exit the program");
        System.in.read(); // Wait for user to press Enter
    }
}

