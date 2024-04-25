package CarsCatalog;
import java.util.Objects;
/**
 * @author Huk Vitaliy
 * @version 0.1.0
 * Represents a car with various attributes such as brand, model, year, engine type, and price.
 */
public class Car {
    private String brand;
    private String model;
    private int year;
    private EngineType engineType;
    private double price;

    /**
     * Car constructor
     *@param brand The brand of the car.
     * @param model The model of the car.
     * @param year The production year of the car.
     * @param engineType The engine type of the car (e.g., PETROL, DIESEL, ELECTRIC).
     * @param price The price of the car.
     */
    public Car(String brand, String model, int year, EngineType engineType, double price) {
        this.brand = brand;
        this.model = model;
        this.year = year;
        this.engineType = engineType;
        this.price = price;
    }

    /**
     * Returns the brand of the car.
     * @return The brand of the car.
     */
    public String getBrand() {
        return brand;
    }

    /**
     * Sets the brand of the car.
     * @param brand Brand of the car
     */
    public void setBrand(String brand) {
        this.brand = brand;
    }

    /**
     * Returns the model of the car.
     * @return The model of the car.
     */
    public String getModel() {
        return model;
    }

    /**
     * Sets the model of the car.
     * @param model Model of the car
     */
    public void setModel(String model) {
        this.model = model;
    }

    /**
     * Returns the year of production of the car.
     * @return The year of production of the car.
     */
    public int getYear() {
        return year;
    }

    /**
     * Sets the production year of the car.
     * @param year Production year of the car
     */
    public void setYear(int year) {
        this.year = year;
    }

    /**
     * Returns the engine type of the car.
     * @return The engine type of the car.
     */
    public EngineType getEngineType() {
        return engineType;
    }

    /**
     * Sets the engine type of the car.
     * @param engineType Engine type of the car
     */
    public void setEngineType(EngineType engineType) {
        this.engineType = engineType;
    }

    /**
     * Returns the price of the car.
     * @return The price of the car.
     */
    public double getPrice() {
        return price;
    }

    /**
     * Sets the price of the car.
     * @param price Price of the car
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * Prints info about car
     */
    public void printDetails() {
        System.out.println("Brand: " + brand);
        System.out.println("Model: " + model);
        System.out.println("Year: " + year);
        System.out.println("Engine Type: " + engineType);
        System.out.println("Price: " + price);
    }

    @Override
    public boolean equals(Object obj) {
        // Check if the object is null or not an instance of Car
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        
        // Cast the object to Car
        Car otherCar = (Car) obj;
        
        // Check for equality of all fields
        return year == otherCar.year &&
               Double.compare(otherCar.price, price) == 0 &&
               Objects.equals(brand, otherCar.brand) &&
               Objects.equals(model, otherCar.model) &&
               engineType == otherCar.engineType;
    }
}
