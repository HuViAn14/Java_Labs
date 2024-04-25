package CarsCatalog;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.Scanner;

public class CatalogFileManagment {
    private File file;

    public CatalogFileManagment() {
        file = null;
    }

    public CatalogFileManagment(String fileName) {
        file = new File("./Catalogs/" + fileName);
    }

    public void createCatalogFile(HashMap<String, ArrayList<Car>> catalog) throws IOException {
        file.createNewFile();
        FileWriter writer;
        writer = new FileWriter(file);
        Gson gson = new Gson();
        Set<String> keys = catalog.keySet();
        ArrayList<Car> cars = new ArrayList<Car>();
        for (String key : keys) {
            // writer.write(key + "\n");
            cars.addAll(catalog.get(key));
            
            // for (Car car : brand) {
            //     gson.toJson(car, writer);
            //     // writer.write("\t"
            //     //         + car.getModel() + "\n\t"
            //     //         + car.getEngineType() + "\n\t"
            //     //         + Integer.toString(car.getYear()) + "\n\t"
            //     //         + car.getPrice() + "\n\t\n");
            // }
        }
        gson.toJson(cars, writer);
        writer.close();
    }

    public CarCatalog fromFileToCatalogHash() throws IOException {
        FileReader reader = new FileReader(file);
        BufferedReader bufferedReader = new BufferedReader(reader);

        // String brand;
        // String model;
        // int year;
        // EngineType engineType;
        // double price;

        CarCatalog carCatalog = new CarCatalog();
        Gson gson = new Gson();
        // String line;
        // while (Car car = gson.fromJson(reader, Car.class);) {
            
        //     Car car = gson.fromJson(reader, Car.class);
        //     //new Car(brand, model, year, engineType, price);
        //     carCatalog.addCar(car);
        // }

        ArrayList<Car> cars = gson.fromJson(reader, new TypeToken<ArrayList<Car>>(){}.getType());
        for(Car car : cars)
            carCatalog.addCar(car);
        reader.close();
        bufferedReader.close();
        return carCatalog;
    }

    public ArrayList<Car> findCarsByModelFile(String model) throws IOException {
        CarCatalog catalog = fromFileToCatalogHash();
        return catalog.findCarsByModel(model);
    }

    public ArrayList<Car> findCarsByBrand(String brand) throws IOException {
        CarCatalog catalog = fromFileToCatalogHash();
        return catalog.findCarsByBrand(brand);
    }

    public ArrayList<Car> findCarsByModel(String model) throws IOException {
        CarCatalog catalog = fromFileToCatalogHash();
        return catalog.findCarsByModel(model);
    }

    public ArrayList<Car> findCarsByYear(int year) throws IOException {
        CarCatalog catalog = fromFileToCatalogHash();
        return catalog.findCarsByYear(year);
    }

    public ArrayList<Car> findCarsByPrice(int price) throws IOException {
        CarCatalog catalog = fromFileToCatalogHash();
        return catalog.findCarsByPrice(price);
    }

    public ArrayList<Car> findCarsByPrice(int priceMin, int priceMax) throws IOException {
        CarCatalog catalog = fromFileToCatalogHash();
        return catalog.findCarsByPrice(priceMin, priceMax);
    }

    public void addCar(Car car) throws IOException {
        CarCatalog catalog = fromFileToCatalogHash();
        catalog.addCar(car);
        createCatalogFile(catalog.getCatalogCopy());
    }

    public void addCars(ArrayList<Car> cars) throws IOException {
        CarCatalog catalog = fromFileToCatalogHash();
        for (Car car : cars) {
            catalog.addCar(car);
        }
        createCatalogFile(catalog.getCatalogCopy());
    }

    public void removeCar(Car car) throws IOException {
        CarCatalog catalog = fromFileToCatalogHash();
        catalog.removeCar(car);
        createCatalogFile(catalog.getCatalogCopy());
    }

    public void removeCars(ArrayList<Car> cars) throws IOException {
        CarCatalog catalog = fromFileToCatalogHash();
        for (Car car : cars) {
            catalog.removeCar(car);
        }
        createCatalogFile(catalog.getCatalogCopy());
    }

    public void changeCar(Car carToChange) throws IOException {
        CarCatalog catalog = fromFileToCatalogHash();
        ArrayList<Car> cars = catalog.findCarsByBrand(carToChange.getBrand());
        for (Car car : cars) {
            if (car.equals(carToChange)) {
                boolean flag = true;
                Scanner scanner = new Scanner(System.in);
                System.out.println("Car is found!");
                System.out.println("Choose what parameter you want to change:");
                System.out.println("1. Model\n2. Year\n3. Engine Type\n4. Price");
                while (flag) {
                    int option = scanner.nextInt();
                    switch (option) {
                        case 1:
                            System.out.println("Enter new Model");
                            String newModel = scanner.nextLine();
                            if(newModel == "")
                                newModel = scanner.nextLine();
                            car.setModel(newModel);
                            flag=false;
                            break;
                        case 2:
                            System.out.println("Enter new Year");
                            String newYear = scanner.nextLine();
                            if(newYear == "")
                                newYear = scanner.nextLine();
                            car.setYear(Integer.parseInt(newYear));
                            flag=false;
                            break;
                        case 3:
                            System.out.println("Enter new Engine Type");
                            String newEngine = scanner.nextLine();
                            if(newEngine == "")
                                newEngine = scanner.nextLine();
                            car.setEngineType(EngineType.valueOf(newEngine.toUpperCase()));
                            flag=false;
                            break;
                        case 4:
                            System.out.println("Enter new Price");
                            String newPrice = scanner.nextLine();
                            if(newPrice == "")
                                newPrice = scanner.nextLine();
                            car.setPrice(Integer.parseInt(newPrice));
                            flag=false;
                            break;
                        default:
                            break;
                    }
                }
            }
        }
        createCatalogFile(catalog.getCatalogCopy());
    }
}
