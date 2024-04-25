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
        Car car4 = new Car("Mercedes", "benz", 2019, EngineType.DIESEL, 50000.0);

        // Створення каталогу автомобілів та додавання до нього автомобілів
        CarCatalog catalog = new CarCatalog();
        catalog.addCar(car1);
        catalog.addCar(car2);
        catalog.addCar(car3);

        CatalogFileManagment cfm = new CatalogFileManagment("catalog1.json");
        cfm.createCatalogFile(catalog.getCatalogCopy());
        CarCatalog c = cfm.fromFileToCatalogHash();
        System.out.println("Catalog from file");
        c.printCatalog();
        
        cfm.addCar(car4);
        System.out.println("Insides of file after adding car");
        c = cfm.fromFileToCatalogHash();
        c.printCatalog();

        cfm.removeCar(car4);
        System.out.println("Insides of file after deleting");
        c = cfm.fromFileToCatalogHash();
        c.printCatalog();

        System.out.println("Cars in with specified price");
        cfm.findCarsByPrice(25001, 60000);

        cfm.changeCar(car2);
        c = cfm.fromFileToCatalogHash();
        c.printCatalog();
        
        System.out.println("Press enter to exit the program");
        System.in.read(); // Wait for user to press Enter

    }
}
