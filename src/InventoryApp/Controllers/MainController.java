package InventoryApp.Controllers;

import InventoryApp.Main;
import InventoryApp.Models.*;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.lang.management.PlatformManagedObject;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Main form of the application
 */
public class MainController implements Initializable {

    ////////////////////////////////////////////////////////
    /**
     * JavaFX Control - TableView of parts
     */
    public TableView partTable;
    /**
     * JavaFX Control - TableView of products
     */
    public TableView productTable;

    /**
     * JavaFX Control - TextField of part search
     */
    public TextField partSearchText;
    /**
     * JavaFX Control - Column for Part Id
     */
    public TableColumn columnPartId;
    /**
     * JavaFX Control - Column for part name
     */
    public TableColumn columnPartName;
    /**
     * JavaFX Control - Column for Part stock
     */
    public TableColumn columnPartStock;
    /**
     * JavaFX Control - Column for Part price
     */
    public TableColumn columnPartPrice;

    /**
     * JavaFX Control - Text for the product search
     */
    public TextField productSearchText;
    /**
     * JavaFX Control - Column for product Id
     */
    public TableColumn columnProductId;
    /**
     * JavaFX Control - Column for product name
     */
    public TableColumn columnProductName;
    /**
     * JavaFX Control - Column for Product stock
     */
    public TableColumn columnProductStock;
    /**
     * JavaFX Control - Column for product price
     */
    public TableColumn columnProductPrice;

    ////////////////////////////////////////////////////////

    /**
     * Initialize the controller
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
//Load the tables
        loadPartsTable();
        loadProductsTable();
    }

    /**
     * Load the part table
     */
    private void loadPartsTable() {
        partTable.setItems(Inventory.getAllParts());

        columnPartId.setCellValueFactory(new PropertyValueFactory<>("id"));
        columnPartName.setCellValueFactory(new PropertyValueFactory<>("name"));
        columnPartStock.setCellValueFactory(new PropertyValueFactory<>("stock"));
        columnPartPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
    }

    /**
     * Load the products table
     */
    private void loadProductsTable() {
        productTable.setItems(Inventory.getAllProducts());

        columnProductId.setCellValueFactory(new PropertyValueFactory<>("id"));
        columnProductName.setCellValueFactory(new PropertyValueFactory<>("name"));
        columnProductStock.setCellValueFactory(new PropertyValueFactory<>("stock"));
        columnProductPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
    }

    /**
     * Exit the application
     * @param actionEvent action
     */
    public void ExitButton_OnAction(ActionEvent actionEvent) {
        if(Main.Confirm("Do you really want to leave?")) {
            Platform.exit();
            System.exit(0);
        }
    }

    /**
     * Open the form to add a part
     */
    public void partAddButton_OnAction(ActionEvent actionEvent) throws IOException {
        PartController.IsAdd = true;
        MainController.OpenNewForm("Views/PartForm.fxml", "Add Part", actionEvent);
    }

    /**
     * Open the form to modify a part
     */
    public void partModifyButton_OnAction(ActionEvent actionEvent) throws IOException {
        //Check if a part is selected
        if(partTable.getSelectionModel().getSelectedItem() == null) {
            Main.ShowError("Select a part before modifying it");
            return;
        }

        PartController.IsAdd = false;
        PartController.PartToModify = (Part)partTable.getSelectionModel().getSelectedItem();
        MainController.OpenNewForm("Views/PartForm.fxml", "Modify Part", actionEvent);
    }

    /**
     * Delete A part
     */
    public void partDeleteButton_OnAction(ActionEvent actionEvent) {
        //Check if a part is selected
        if(partTable.getSelectionModel().getSelectedItem() == null) {
            Main.ShowError("Removing the part has been cancelled");
            return;
        }

        //get the part
        Part selectedPart = (Part)partTable.getSelectionModel().getSelectedItem();

        //confirm action
        if(Main.Confirm("Do you really want to remove Part " + selectedPart.getName() + "?"))
        {
            Inventory.deletePart(selectedPart);
            Main.ShowMessage(Alert.AlertType.INFORMATION, "The Part has been removed!");
        }else {
            Main.ShowMessage(Alert.AlertType.WARNING, "Removing the part has been cancelled");
        }
    }

    /**
     * Search for a part
     */
    public void partSearchText_OnKeyReleased(KeyEvent keyEvent) {
        String searchText = partSearchText.getText();
        if (Main.IsNumeric(searchText)) {
            //try to find by id
            Part p = Inventory.lookupPart(Integer.parseInt(searchText));
            if(p != null) {
                partTable.getSelectionModel().select(p);
                partTable.refresh();
                return;
            }
        }

        // If the part or parts are found, the application highlights a single part or filters multiple parts.
        // If the part is not found, the application displays an error message in the UI or in a dialog box.
        ObservableList<Part> parts = Inventory.lookupPart(searchText);

        if (parts.size() == 1) {
            partTable.setItems(parts);
            partTable.getSelectionModel().select(parts.get(0));
        }else {
            partTable.getSelectionModel().clearSelection();
            partTable.setItems(parts);
        }
    }

    /**
     * Open the form to add a product
     */
    public void productAddButton_OnAction(ActionEvent actionEvent) throws IOException {
        ProductController.IsAdd = true;
        MainController.OpenNewForm("Views/ProductForm.fxml", "Add Product", actionEvent);
    }

    /**
     * Open the form to modify a product
     */
    public void productModifyButton_OnAction(ActionEvent actionEvent) throws IOException {
        //Check if a Product is selected
        if(productTable.getSelectionModel().getSelectedItem() == null) {
            Main.ShowError("Select a product before modifying it");
            return;
        }

        ProductController.IsAdd = false;
        ProductController.ProductToModify = (Product)productTable.getSelectionModel().getSelectedItem();
        MainController.OpenNewForm("Views/ProductForm.fxml", "Modify Product", actionEvent);
    }

    /**
     * Delete a product
     */
    public void productDeleteButton_OnAction(ActionEvent actionEvent) {
        //Check if a product is selected
        if(productTable.getSelectionModel().getSelectedItem() == null) {
            Main.ShowError("Removing the product has been cancelled");
            return;
        }

        //get the product
        Product selectedProduct = (Product)productTable.getSelectionModel().getSelectedItem();

        //check there is no associations.
        if(selectedProduct.getAssociatedParts().size() > 0) {
            Main.ShowError("To delete a product with parts associated, modify the product to remove all part associations, then delete the part");
            return;
        }

        //confirm action
        if(Main.Confirm("Do you really want to remove Product " + selectedProduct.getName() + "?"))
        {
            Inventory.deleteProduct(selectedProduct);
            Main.ShowMessage(Alert.AlertType.INFORMATION, "The Product has been removed!");
        }else {
            Main.ShowMessage(Alert.AlertType.WARNING, "Removing the product has been cancelled");
        }
    }

    /**
     * Search the product table
     */
    public void productSearchText_OnKeyReleased(KeyEvent keyEvent) {
        String searchText = productSearchText.getText();
        if (Main.IsNumeric(searchText)) {
            //try to find by id
            Product p = Inventory.lookupProduct(Integer.parseInt(searchText));
            if(p != null) {
                productTable.getSelectionModel().select(p);
                productTable.refresh();
                return;
            }
        }

        // If the product or products are found, the application highlights a single product or filters multiple products.
        // If the product is not found, the application displays an error message in the UI or in a dialog box.
        ObservableList<Product> products = Inventory.lookupProduct(searchText);

        if (products.size() ==1) {
            productTable.setItems(products);
            productTable.getSelectionModel().select(products.get(0));
        }else {
            productTable.getSelectionModel().clearSelection();
            productTable.setItems(products);
        }
    }

    /////////////////////////////////////////////////////
    /**
     * Open the main form of the application
     */
    public static void OpenMainForm(ActionEvent actionEvent) throws IOException {
        MainController.OpenNewForm("Views/MainForm.fxml",Main.MainFormTitle, actionEvent);
    }

    /**
     * RUNTIME ERROR java.lang.NullPointerException: Location is required.
     * Needed to reference the application main class replaced - FXMLLoader.load(MainController.class.getResource(formName)); with FXMLLoader.load(InventoryApp.Main.class.getResource(formName));
     * @param formName
     */
    public static void OpenNewForm(String formName, String formTitle, ActionEvent actionEvent) throws IOException {
        Parent parent = FXMLLoader.load(InventoryApp.Main.class.getResource(formName));
        Scene scene = new Scene(parent);
        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        stage.setTitle(formTitle);
        stage.setScene(scene);
        stage.show();
    }
}
