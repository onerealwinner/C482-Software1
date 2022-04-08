package InventoryApp;

import InventoryApp.Controllers.MainController;
import InventoryApp.Models.InHouse;
import InventoryApp.Models.Inventory;
import InventoryApp.Models.Outsourced;
import InventoryApp.Models.Product;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

/**
 * This is the starting function of the app. Its a inventory management system.
 * In the requirements it states, "You may use one FXML file for forms with an identical UI component structure" my add / modify FXML files and controllers are single files
 */
public class Main extends Application {

    /**
     * The title of the main form
     */
    public final static String MainFormTitle = "Inventory App";

    @Override
    public void start(Stage primaryStage) throws Exception{
        this.loadTestInventory();

        Parent root = FXMLLoader.load(getClass().getResource("Views/MainForm.fxml"));
        primaryStage.setTitle(MainFormTitle);
        primaryStage.setScene(new Scene(root, 1200, 500));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Generic confirmation function
     * @param message the message for the confirmation box
     * @return true if 'yes'
     */
    public static boolean Confirm(String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, message, ButtonType.YES, ButtonType.NO);
        alert.showAndWait();

        return alert.getResult() == ButtonType.YES;
    }

    /**
     * Generic show an error
     * @param message the information to show
     */
    public static void ShowError(String message) {
        Main.ShowMessage(Alert.AlertType.ERROR, message);
    }

    /**
     * Generic show Information
     * @param message the information to show
     */
    public static void ShowMessage(Alert.AlertType aType, String message) {
        Alert alert = new Alert(aType, message, ButtonType.OK);
        alert.showAndWait();
    }

    /**
     * Check if a string is a number
     * @param s the string to check
     * @return true if numeric
     */
    public static boolean IsNumeric(String s) {
        //Check if this is numeric
        try {
            int val = Integer.parseInt(s);
            return true;
        }catch (Exception e) {
            return false;
        }
    }

    /**
     * Check if a string is a double
     * @param s the string to check
     * @return true if double
     */
    public static boolean IsDouble(String s) {
        //Check if this is numeric
        try {
            double val = Double.parseDouble(s);
            return true;
        }catch (Exception e) {
            return false;
        }
    }

    /**
     * Loads some test data
     */
    public void loadTestInventory(){
        //Inhouse parts
        InHouse box = new InHouse(Inventory.getNewPartId(), "box",5.99,5,2,10,15);
        InHouse lid = new InHouse(Inventory.getNewPartId(), "lid",15.99,6,1,10,16);
        InHouse cup = new InHouse(Inventory.getNewPartId(),"cup",5.99,3,2,23,18);
        Inventory.addPart(box);
        Inventory.addPart(lid);
        Inventory.addPart(cup);

        //Outsourced parts
        Outsourced ball = new Outsourced(Inventory.getNewPartId(), "ball",54.99,8,3,12,"BallWorld");
        Outsourced string = new Outsourced(Inventory.getNewPartId(), "string",19.99,18,10,120,"StringWorld");
        Inventory.addPart(ball);
        Inventory.addPart(string);

        //Products
        Product p1 = new Product(Inventory.getNewProductId(),"Ball in a box", 99.99, 8,1,100);
        p1.addAssociatedPart(box);
        p1.addAssociatedPart(ball);
        Inventory.addProduct(p1);

        Product p2 = new Product(Inventory.getNewProductId(),"Ball in a cup", 99.99, 8,1,100);
        p2.addAssociatedPart(cup);
        p2.addAssociatedPart(ball);
        p2.addAssociatedPart(string);
        Inventory.addProduct(p2);
    }

}
