package InventoryApp.Controllers;

import InventoryApp.Main;
import InventoryApp.Models.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for the Product Form.
 * Determines if the form will add or modify a product by the static variables IsAdd and ProductToModify
 */
public class ProductController implements Initializable {

    /**
     * Determine if this the actions will result in Add or Modify
     */
    public static Boolean IsAdd = true;

    /**
     * If this is to modify, this is the product that will modified
     */
    public static Product ProductToModify = null;

    /////////////////
    /**
     * JavaFX Control - Label for the Form Header
     */
    public Label formHeaderLabel;

    /**
     * JavaFX Control - Text box for product id
     */
    public TextField productIdText;
    /**
     * JavaFX Control - Text box for product name
     */
    public TextField productNameText;
    /**
     * JavaFX Control - Text box for product stock
     */
    public TextField productInventoryText;
    /**
     * JavaFX Control - Text box for product price
     */
    public TextField productPriceText;
    /**
     * JavaFX Control - Text box for max stock
     */
    public TextField productMaxText;
    /**
     * JavaFX Control - Text box for min stock
     */
    public TextField productMinText;

    /**
     * JavaFX Control - Text box for part search
     */
    public TextField partSearchText;
    /**
     * JavaFX Control - Table of parts
     */
    public TableView partTable;
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
     * JavaFX Control - Table of associated parts
     */
    public TableView associatedPartTable;

    /**
     * JavaFX Control - Column for associated Part Id
     */
    public TableColumn columnAssociatedPartId;
    /**
     * JavaFX Control - Column for associated Part name
     */
    public TableColumn columnAssociatedPartName;
    /**
     * JavaFX Control - Column for associated Part stock
     */
    public TableColumn columnAssociatedPartStock;
    /**
     * JavaFX Control - Column for associated Part price
     */
    public TableColumn columnAssociatedPartPrice;

    /**
     * JavaFX Control - Button to add Associated Part
     */
    public Button addAssociatedPartButton;
    /**
     * JavaFX Control - Button to remove Associated Part
     */
    public Button removePartButton;
////////////////

    /**
     * Initialize the controller
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //fill parts table
        loadPartsTable();

        if (IsAdd) {
            formHeaderLabel.setText("Add Product");

            ObservableList<Part> associatedParts = FXCollections.observableArrayList();
            loadAssociatedPartsTable(associatedParts);
        } else {
            formHeaderLabel.setText("Modify Product");

            productIdText.setText(String.valueOf(ProductToModify.getId()));
            productNameText.setText(String.valueOf(ProductToModify.getName()));
            productInventoryText.setText(String.valueOf(ProductToModify.getStock()));
            productPriceText.setText(String.valueOf(ProductToModify.getPrice()));
            productMinText.setText(String.valueOf(ProductToModify.getMin()));
            productMaxText.setText(String.valueOf(ProductToModify.getMax()));

            //Fill association tables
            loadAssociatedPartsTable(ProductToModify.getAssociatedParts());
        }
    }

    /**
     * Load the part table
     * FUTURE ENHANCEMENT make generic so I can use the same code from MainController
     */
    private void loadPartsTable() {
        partTable.setItems(Inventory.getAllParts());

        columnPartId.setCellValueFactory(new PropertyValueFactory<>("id"));
        columnPartName.setCellValueFactory(new PropertyValueFactory<>("name"));
        columnPartStock.setCellValueFactory(new PropertyValueFactory<>("stock"));
        columnPartPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
    }

    /**
     * Load the part table
     */
    private void loadAssociatedPartsTable(ObservableList<Part> associatedParts) {
        associatedPartTable.setItems(associatedParts);

        columnAssociatedPartId.setCellValueFactory(new PropertyValueFactory<>("id"));
        columnAssociatedPartName.setCellValueFactory(new PropertyValueFactory<>("name"));
        columnAssociatedPartStock.setCellValueFactory(new PropertyValueFactory<>("stock"));
        columnAssociatedPartPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
    }

    /**
     * Cancel changes and return home
     */
    public void cancelButton_OnAction(ActionEvent actionEvent) throws IOException {
        //confirm
        if (Main.Confirm("Cancel changes and return to main form?")) {
            MainController.OpenMainForm(actionEvent);
        }
    }

    /**
     * Search part table
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

        if (parts.size() ==1) {
            partTable.setItems(parts);
            partTable.getSelectionModel().select(parts.get(0));
        }else {
            partTable.getSelectionModel().clearSelection();
            partTable.setItems(parts);
        }
    }

    /**
     * Add the selected associated part
     */
    public void addAssociatedPartButton_OnAction(ActionEvent actionEvent) {
        //Check if a Product is selected
        if(partTable.getSelectionModel().getSelectedItem() == null) {
            Main.ShowError("Select a part before adding it");
            return;
        }

        //get the part
        Part selectedPart = (Part)partTable.getSelectionModel().getSelectedItem();
        associatedPartTable.getItems().add(selectedPart);
        associatedPartTable.refresh();
    }

    /**
     * Remove the selected associated part
     */
    public void removePartButton_OnAction(ActionEvent actionEvent) {
        //Check if a Product is selected
        if(associatedPartTable.getSelectionModel().getSelectedItem() == null) {
            Main.ShowError("Select a part before removing it");
            return;
        }
        //get the part
        Part selectedPart = (Part)associatedPartTable.getSelectionModel().getSelectedItem();

        //confirm action
        if(Main.Confirm("Do you really want to remove Part " + selectedPart.getName() + " from this product?"))
        {
            associatedPartTable.getItems().remove(selectedPart);
        }
    }

    /**
     * Save the product
     *
     * @param actionEvent
     */
    public void saveButton_OnAction(ActionEvent actionEvent) {
        //validate form
        if (!IsValidForm()) {
            return;
        }

        try {
            String name = productNameText.getText();
            int stock = Integer.parseInt(productInventoryText.getText());
            double price = Double.parseDouble(productPriceText.getText());
            int min = Integer.parseInt(productMinText.getText());
            int max = Integer.parseInt(productMaxText.getText());

            int id = 0;
            if (IsAdd) {
                id = Inventory.getNewProductId();
            } else {
                id = ProductToModify.getId();
            }

            //add product
            Product p = new Product(id, name, price, stock, min, max);

            for(Object row : associatedPartTable.getItems()) {
                Part part = (Part) row;
                p.addAssociatedPart(part);
            }

            if (IsAdd) {
                Inventory.addProduct(p);
            }else {
                Inventory.updateProduct(Inventory.getAllProducts().indexOf(ProductToModify), p);
            }


            //show confirmation
            Main.ShowMessage(Alert.AlertType.INFORMATION, name + " was saved - routing back to the main form!");
            MainController.OpenMainForm(actionEvent);

        } catch (Exception e) {
            Main.ShowError("Something broke!");
        }
    }
    /**
     * Make sure the form is valid
     *
     * @return true if the form is valid
     * RUNTIME ERROR - If not validated properly - fixed by validating
     */
    public boolean IsValidForm() {
        boolean retVal = true;
        String errorMsg = "";

        //inv must be int
        if (!Main.IsNumeric(productInventoryText.getText())) {
            errorMsg += "Inventory must be an integer \r\n";
            retVal = false;
        }

        //min must be int
        if (!Main.IsNumeric(productMinText.getText())) {
            errorMsg += "Min must be an integer \r\n";
            retVal = false;
        }

        //max must be int
        if (!Main.IsNumeric(productMaxText.getText())) {
            errorMsg += "Max must be an integer \r\n";
            retVal = false;
        }

        //min must be less equal to max
        //max must be int
        if(Main.IsNumeric(productMinText.getText()) && Main.IsNumeric(productMaxText.getText()) && Main.IsNumeric(productInventoryText.getText())) {
            int min = Integer.parseInt(productMinText.getText());
            int max = Integer.parseInt(productMaxText.getText());
            int stock = Integer.parseInt(productInventoryText.getText());

            if(min > max || (stock < min || stock > max)) {
                errorMsg += "Min must be less than max and inventory must be between min and max. \r\n";
                retVal = false;
            }
        }

        //price must be double
        if (!Main.IsDouble(productPriceText.getText())) {
            errorMsg += "Price must be a double \r\n";
            retVal = false;
        }

        if (!retVal) {
            Main.ShowError(errorMsg);
        }

        return retVal;
    }
}
