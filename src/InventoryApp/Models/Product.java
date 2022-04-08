package InventoryApp.Models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Model of Product
 * @author Daniel Mudge
 */
public class Product {
    /**
     * List of Associated Parts
     * Note: A product’s associated parts can exist independent of current inventory of parts.
     */
    private ObservableList<Part> associatedParts = FXCollections.observableArrayList();

    /**
     * ID of the product
     */
    private int id;
    /**
     * Name of the product
     */
    private String name;
    /**
     * Price of the product
     */
    private double price;
    /**
     * Amount of Stock of the product
     */
    private int stock;
    /**
     * The minimum amount of stock for the product
     */
    private int min;
    /**
     * The maximum amount of stock for the product
     */
    private int max;

    /**
     * Constructor for Product
     */
    public Product(int id, String name, double price, int stock, int min, int max) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.min = min;
        this.max = max;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the price
     */
    public double getPrice() {
        return price;
    }

    /**
     * @param price the price to set
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * @return the stock
     */
    public int getStock() {
        return stock;
    }

    /**
     * @param stock the stock to set
     */
    public void setStock(int stock) {
        this.stock = stock;
    }

    /**
     * @return the min
     */
    public int getMin() {
        return min;
    }

    /**
     * @param min the min to set
     */
    public void setMin(int min) {
        this.min = min;
    }

    /**
     * @return the max
     */
    public int getMax() {
        return max;
    }

    /**
     * @param max the max to set
     */
    public void setMax(int max) {
        this.max = max;
    }

    /**
     * Add a part to the association list.
     * @param part - The part that will be added
     */
    public void addAssociatedPart(Part part) {
        associatedParts.add(part);
    }

    /**
     * Removes an associatedPart
     * @param selectedAssociatedPart - The part to remove
     * @return True if removed, false if not found
     */
    public boolean deleteAssociatedPart(Part selectedAssociatedPart) {
        boolean retVal = false;
        if(associatedParts.contains(selectedAssociatedPart)) {
            associatedParts.remove(selectedAssociatedPart);
            retVal = true;
        }
        return retVal;
    }

    /**
     * Gets the list of associated parts
     * @return the associated parts
     */
    public ObservableList<Part> getAssociatedParts() { return associatedParts;}
}
