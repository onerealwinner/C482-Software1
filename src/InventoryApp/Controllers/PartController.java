package InventoryApp.Controllers;

import InventoryApp.Main;
import InventoryApp.Models.InHouse;
import InventoryApp.Models.Inventory;
import InventoryApp.Models.Outsourced;
import InventoryApp.Models.Part;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for the Part Form.
 * Determines if the form will add or modify a part by the static variables IsAdd and PartToModify
 */
public class PartController implements Initializable {

    /**
     * Determine if this the actions will result in Add or Modify
     */
    public static Boolean IsAdd = true;

    /**
     * If this is to modify, this is the part that will modified
     */
    public static Part PartToModify = null;

    /////////////////
    /**
     * JavaFX Control - Label for the Form Header
     */
    public Label formHeaderLabel;
    /**
     * JavaFX Control - Radio Button for Part type inhouse
     */
    public RadioButton inHouseRadioButton;
    /**
     * JavaFX Control - Control group for the part type
     */
    public ToggleGroup partToggle;
    /**
     * JavaFX Control - Radio Button for Part type outsourced
     */
    public RadioButton outsourcedRadioButton;

    /**
     * JavaFX Control - Text box for part id
     */
    public TextField partIdText;
    /**
     * JavaFX Control - Text box for part name
     */
    public TextField partNameText;
    /**
     * JavaFX Control - Text box for part stock
     */
    public TextField partInventoryText;
    /**
     * JavaFX Control - Text box for part price
     */
    public TextField partPriceText;
    /**
     * JavaFX Control - Text box for max stock
     */
    public TextField partMaxText;
    /**
     * JavaFX Control - Text box for either the part id or company name
     */
    public TextField partIdOrNameText;
    /**
     * JavaFX Control - Text box for min stock
     */
    public TextField partMinText;
    /**
     * JavaFX Control - Label for MachineId or Company Name depending on part type
     */
    public Label lblMachineOrCompany;

////////////////

    /**
     * Initialize the PartController
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (IsAdd) {
            inHouseRadioButton.setSelected(true);
            formHeaderLabel.setText("Add Part");
        }else {
            formHeaderLabel.setText("Modify Part");

            if(PartToModify instanceof InHouse) {
                inHouseRadioButton.setSelected(true);
                partIdOrNameText.setText(String.valueOf(((InHouse) PartToModify).getMachineId()));
            }else {
                outsourcedRadioButton.setSelected(true);
                partIdOrNameText.setText(String.valueOf(((Outsourced) PartToModify).getCompanyName()));
            }

            partIdText.setText(String.valueOf(PartToModify.getId()));
            partNameText.setText(String.valueOf(PartToModify.getName()));
            partInventoryText.setText(String.valueOf(PartToModify.getStock()));
            partPriceText.setText(String.valueOf(PartToModify.getPrice()));
            partMinText.setText(String.valueOf(PartToModify.getMin()));
            partMaxText.setText(String.valueOf(PartToModify.getMax()));

            partToggle_OnAction(null);
        }
    }

    /**
     * Cancel the form action and return home
     */
    public void cancelButton_OnAction(ActionEvent actionEvent) throws IOException {
        //confirm
        if(Main.Confirm("Cancel changes and return to main form?")) {
            MainController.OpenMainForm(actionEvent);
        }
    }

    /**
     * Toggle the part type
     */
    public void partToggle_OnAction(ActionEvent actionEvent) {
        if(inHouseRadioButton.isSelected()) {
            lblMachineOrCompany.setText("Machine Id");
        }else {
            lblMachineOrCompany.setText("Company Name");
        }
    }

    /**
     * Make sure the form is valid
     * @return true if the form is valid
     */
    public boolean IsValidForm() {
        boolean retVal = true;
        String errorMsg = "";

        //inv must be int
        if(!Main.IsNumeric(partInventoryText.getText())) {
            errorMsg += "Inventory must be an integer \r\n";
            retVal = false;
        }

        //min must be int
        if(!Main.IsNumeric(partMinText.getText())) {
            errorMsg += "Min must be an integer \r\n";
            retVal = false;
        }

        //max must be int
        if(!Main.IsNumeric(partMaxText.getText())) {
            errorMsg += "Max must be an integer \r\n";
            retVal = false;
        }

        //min must be less equal to max and inv must be in-between
        //max must be int
        if(Main.IsNumeric(partMinText.getText()) && Main.IsNumeric(partMaxText.getText()) && Main.IsNumeric(partInventoryText.getText())) {
            int min = Integer.parseInt(partMinText.getText());
            int max = Integer.parseInt(partMaxText.getText());
            int stock = Integer.parseInt(partInventoryText.getText());

            if(min > max || (stock < min || stock > max)) {
                errorMsg += "Min must be less than max and inventory must be between min and max. \r\n";
                retVal = false;
            }
        }

        //price must be double
        if(!Main.IsDouble(partPriceText.getText())) {
            errorMsg += "Price must be a double \r\n";
            retVal = false;
        }

        //machineId must be int
        if(inHouseRadioButton.isSelected() && !Main.IsNumeric(partIdOrNameText.getText())) {
            errorMsg += "Machine Id must be an integer \r\n";
            retVal = false;
        }

        if(!retVal ){
            Main.ShowError(errorMsg);
        }

        return retVal;
    }

    /**
     * Save the part
     * @param actionEvent
     */
    public void saveButton_OnAction(ActionEvent actionEvent) {
        //validate form
        if (!IsValidForm()) {
            return;
        }

        try {
            String name = partNameText.getText();
            int stock = Integer.parseInt(partInventoryText.getText());
            double price = Double.parseDouble(partPriceText.getText());
            int min = Integer.parseInt(partMinText.getText());
            int max = Integer.parseInt(partMaxText.getText());

            //get the ID of the part
            int id = IsAdd ? Inventory.getNewPartId() : PartToModify.getId();

            Part p = null;
            if (inHouseRadioButton.isSelected()) {
                p = new InHouse(id, name, price, stock, min, max, Integer.parseInt(partIdOrNameText.getText()));
            } else {
                p = new Outsourced(id, name, price, stock, min, max, partIdOrNameText.getText());
            }

            if (IsAdd) {
                Inventory.addPart(p);
            } else {
                Inventory.updatePart(Inventory.getAllParts().indexOf(PartToModify), p);
            }

            //show confirmation
            Main.ShowMessage(Alert.AlertType.INFORMATION, name + " was saved - routing back to the main form!");
            MainController.OpenMainForm(actionEvent);

        }catch (Exception e) {
            Main.ShowError("Something broke!");
        }
    }
}
