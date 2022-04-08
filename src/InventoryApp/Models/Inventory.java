package InventoryApp.Models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Locale;

/**
 * Model of Inventory
 * @author Daniel Mudge
 */
public class Inventory {

    /**
     * Counter for partId
     * FUTURE ENHANCEMENT - save the state of this between application uses
     */
    private static int partIdCounter = 0;

    /**
     * Counter for product
     * FUTURE ENHANCEMENT - save the state of this between application uses
     */
    private static int productIdCounter = 0;

    /**
     * List of the Parts
     */
    private static ObservableList<Part> allParts = FXCollections.observableArrayList();

    /**
     * List of Products
     */
    private static ObservableList<Product> allProducts = FXCollections.observableArrayList();

    /**
     * Gets a new part Id - as long as this increments forward this will be unique - since we don't save state
     * @return new partId
     */
    public static int getNewPartId() {
        partIdCounter += 1;
        return partIdCounter;
    }

    /**
     * Gets a new product Id - as long as this increments forward this will be unique - since we don't save state
     * @return new productId
     */
    public static int getNewProductId() {
        productIdCounter += 1;
        return productIdCounter;
    }

    /**
     * Add a part to the Inventory
     * @param newPart
     */
    public static void addPart(Part newPart) {
        allParts.add(newPart);
    }

    /**
     * Add a product to the Inventory
     * @param newProduct
     */
    public static void addProduct(Product newProduct) {
        allProducts.add(newProduct);
    }

    /**
     * looks up a part
     * @param partId the id of the part to id
     * @return the first part found with this id or null if nothing found
     */
    public static Part lookupPart(int partId) {
        Part retVal = null;

        for(Part p : allParts) {
            if(p.getId() == partId) {
                retVal = p;
                break;
            }
        }
        return retVal;
    }

    /**
     * looks up a product
     * @param productId the id of the product to id
     * @return the first product found with this id or null if nothing found
     */
    public static Product lookupProduct(int productId) {
        Product retVal = null;

        for(Product p : allProducts) {
            if(p.getId() == productId) {
                retVal = p;
                break;
            }
        }
        return retVal;
    }

    /**
     * Looks up parts by name - Case-Insensitive
     * @param partName the name of the part
     * @return a list parts that contain the partName
     */
    public static ObservableList<Part> lookupPart(String partName) {
        ObservableList<Part> retVal = FXCollections.observableArrayList();

        partName = partName.toLowerCase(Locale.ROOT);
        for(Part p : allParts) {
            if(p.getName().toLowerCase(Locale.ROOT).contains(partName)) {
                retVal.add(p);
            }
        }
        return retVal;
    }

    /**
     * Looks up Products by name - Case-Insensitive
     * @param productName the name of the Product
     * @return a list Products that contain the ProductName
     */
    public static ObservableList<Product> lookupProduct(String productName) {
        ObservableList<Product> retVal = FXCollections.observableArrayList();

        productName = productName.toLowerCase(Locale.ROOT);
        for(Product p : allProducts) {
            if(p.getName().toLowerCase(Locale.ROOT).contains(productName)) {
                retVal.add(p);
            }
        }
        return retVal;
    }

    /**
     * Update a part in the inventory
     * @param index the index of the part
     * @param selectedPart the actual part
     * FUTURE ENHANCEMENT - lookup the index by the part selected
     */
    public static void updatePart(int index, Part selectedPart) {
        allParts.set(index,selectedPart);
    }

    /**
     * Update a product in the inventory
     * @param index the index of the part
     * @param selectedProduct the actual part
     * FUTURE ENHANCEMENT - lookup the index by the product selected
     */
    public static void updateProduct(int index, Product selectedProduct) {
        allProducts.set(index,selectedProduct);
    }

    /**
     * Deletes a part from the inventory
     * @param selectedPart the part to delete
     * @return true if deleted - false if not 
     */
    public static boolean deletePart(Part selectedPart) {
        boolean retVal = false;

        if (allParts.contains(selectedPart)) {
            allParts.remove(selectedPart);
            retVal = true;
        }

        return retVal;
    }

    /**
     * Deletes a Product from the inventory
     * @param selectedProduct the Product to delete
     * @return true if deleted - false if not 
     */
    public static boolean deleteProduct(Product selectedProduct) {
        boolean retVal = false;

        if (allProducts.contains(selectedProduct)) {
            allProducts.remove(selectedProduct);
            retVal = true;
        }

        return retVal;
    }

    /**
     * All the parts in inventory
     * @return All the parts in inventory
     */
    public static ObservableList<Part> getAllParts() {
        return allParts;
    }

    /**
     * All the products in inventory
     * @return All the products in inventory
     */
    public static ObservableList<Product> getAllProducts() {
        return allProducts;
    }
    
}
