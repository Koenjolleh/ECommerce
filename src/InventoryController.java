import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.omg.CosNaming.NamingContextExtPackage.StringNameHelper;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class InventoryController implements Initializable {

    private InventoryModel inventoryModel;

    private Stage homeStage;

    //AdminScreen attributes
    @FXML
    private TextField adminId;
    @FXML
    private TextField adminName;
    @FXML
    private TextField adminCost;
    @FXML
    private ChoiceBox<String> adminChoiceCategory;
    @FXML
    private TextArea adminDescription;
    @FXML
    private Label adminFeedback;
    @FXML
    private ComboBox<String> adminComboCategory;
    @FXML
    private TextArea adminDisplay;

    //CustomerScreen attributes
    @FXML
    private ComboBox<String> customerComboCategory;
    @FXML
    private Label customerFeedback;
    @FXML
    private TextArea customerDisplay;
    @FXML
    private TextArea customerCart;
    @FXML
    private TextField customerId;
    @FXML
    private TextField customerQuantity;


    //Both
    @FXML
    private Button backButton;

    /*
    AdminScreen methods
     */

    //Makes sure the category choice is not null when trying to create a new product
    //gives feedback on weather or not the product creation was successful
    public void handleCreateBtn(ActionEvent e){

        if (adminChoiceCategory.getValue() != null) {
            try {
                inventoryModel.createProduct(adminName.getText(),adminDescription.getText(),
                        Double.valueOf(adminCost.getText()),adminChoiceCategory.getValue());
                adminFeedback.setText("Product successfully created");
                //refreshes the display so that the newly created product shows on the list
                adminDisplay.setText(inventoryModel.readInventory(adminComboCategory.getValue()));
            } catch (NumberFormatException e1) {
                adminFeedback.setText("Product not created. Remember to enter everything, except id. " +
                        "The cost should be a number separated by a decimal point");
            }
        }else{
            adminFeedback.setText("Product not created. Remember to enter everything, except id.");
        }
        clearAllAdmin();
    }

    //Calls the updateProduct method and sets the feedback accordingly if all the criteria
    //for the method is okey. Then clears the input fields.
    public void handleUpdateBtn(ActionEvent e){
        if (adminChoiceCategory.getValue() != null) {
            try {
                adminFeedback.setText(inventoryModel.updateProduct(Integer.parseInt(adminId.getText()),adminName.getText(),adminDescription.getText(),
                        Double.valueOf(adminCost.getText()),adminChoiceCategory.getValue()));
                //refreshes the display so that the updated item is displyed correctly
                adminDisplay.setText(inventoryModel.readInventory(adminComboCategory.getValue()));
            } catch (NumberFormatException e1) {
                adminFeedback.setText("Product not updated. Remember to enter everything, " +
                        "The cost should be a number separated by a decimal point");
            }
        }else{
            adminFeedback.setText("Product not updated. Remember to enter everything, " +
                    "including the id of the product you want to update.");
        }
        clearAllAdmin();
    }

    //This method makes an alert box pop up asking the user weather or not he/she is sure
    //they want to delete the product with the id currently entered in the id input text field
    //if ok is pressed the deleteProduct method is called and the feedback is set accordingly
    //if the user presses cancel the feedback is set to match that and the deletion method is not called
    public void handleDeleteBtn(ActionEvent e){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("WARNING!");
        alert.setContentText("Are you sure you want to delete the product" +
                "with id# " + adminId.getText());

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            try {
                adminFeedback.setText(inventoryModel.deleteProduct(Integer.parseInt(adminId.getText())));
                //refreshes the display so that the deleted item doesn't show on the list anymore
                adminDisplay.setText(inventoryModel.readInventory(adminComboCategory.getValue()));
            } catch (NumberFormatException e1) {
                adminFeedback.setText("Please enter a valid number");
            }
        } else {
            adminFeedback.setText("You were not sure");
        }
        clearAllAdmin();
    }

    //Takes the value of the id field and puts it into the readProduct method
    //if a valid number is found. If it catches an exeption in outputs a message
    //in the feedback field. Resets all input fields each time it is called.
    @SuppressWarnings("Duplicates")
    public void handleReadBtn(ActionEvent e){
        try {
            adminDisplay.setText(inventoryModel.readProduct(Integer.parseInt(adminId.getText())));
            if (adminDisplay.getText().equals("")) {
                adminFeedback.setText("Product not found");
            } else {
                adminFeedback.setText("Product with id: " + adminId.getText() + " read");
            }
        } catch (NumberFormatException e1) {
            adminFeedback.setText("No product with id " + adminId.getText() + " found. Please type in a valid number");
        }
        clearAllAdmin();
    }

    //Sets the text of the display on the admin screen to be a list of the content of the current category
    public void changedCategoryCombo(ActionEvent e){
        adminDisplay.setText(inventoryModel.readInventory(adminComboCategory.getValue()));
        adminFeedback.setText("Category: " + adminComboCategory.getValue());
    }

    //Clears the input boxes
    public void clearAllAdmin(){
        adminId.setText("");
        adminName.setText("");
        adminCost.setText("");
        adminDescription.setText("");
        adminChoiceCategory.setValue(null);
    }

    /*
    CustomerScreen methods
     */

    //Calls the addToCart method and sets the feedback to be the return value of the method
    //if a number is not entered, the feedback is set accordingly.
    //then it updates the shopping cart and clears the input fields
    public void handleAddToCartBtn(ActionEvent e){
        try {
            customerFeedback.setText(inventoryModel.addToCart(Integer.parseInt(customerId.getText()),Integer.parseInt(customerQuantity.getText())));
        } catch (NumberFormatException e1) {
            customerFeedback.setText("Product not added to cart. Please enter a valid number");
        }
        customerCart.setText(inventoryModel.displayShoppingCart());
        clearAllCustomer();
    }

    //Calls the deleteFromCart method and sets the feedback to be the return value of the method
    //if a number is not entered, the feedback is set accordingly.
    //then it updates the shopping cart and clears the input fields
    public void handleDeleteFromCartBtn(ActionEvent e){
        try {
            customerFeedback.setText(inventoryModel.deleteFromCart(Integer.parseInt(customerId.getText()),Integer.parseInt(customerQuantity.getText())));
        } catch (NumberFormatException e1) {
            customerFeedback.setText("Product not deleted from cart. Please enter a valid number");
        }
        customerCart.setText(inventoryModel.displayShoppingCart());
        clearAllCustomer();
    }

    //See handleReadBtn description
    @SuppressWarnings("Duplicates")
    public void handleCustomerReadBtn(ActionEvent e){
        try {
            customerDisplay.setText(inventoryModel.readProduct(Integer.parseInt(customerId.getText())));
            if (customerDisplay.getText().equals("")) {
                customerFeedback.setText("Product not found");
            } else {
                customerFeedback.setText("Product with id: " + customerId.getText() + " read");
            }
        } catch (NumberFormatException e1) {
            customerFeedback.setText("No product with id " + customerId.getText() + " found. Please type in a valid number");
        }
        clearAllCustomer();
    }

    //Sets the text of the display on the admin screen to be a list of the content of the current category
    public void changedCategoryComboCustomer(ActionEvent e){
        customerDisplay.setText(inventoryModel.readInventory(customerComboCategory.getValue()));
        customerFeedback.setText("Category: " + customerComboCategory.getValue());
    }

    //Clears the input boxes
    public void clearAllCustomer(){
        customerId.setText("");
        customerQuantity.setText("");
    }

    /*
    Methods for both screens
     */

    //Makes it possible for the user to return to the home screen
    @SuppressWarnings("Duplicates")
    public void handleBackBtn(ActionEvent e){
        try {
            homeStage = (Stage)backButton.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("/HomeScreen.fxml"));
            homeStage.setTitle("Home Screen");
            homeStage.setScene(new Scene(root));
        } catch (IOException e1) {
            System.out.println(e1);
        }
    }

    ObservableList<String> categories = FXCollections.observableArrayList("All","FPS","RTS","RPG","MMORPG","BR","MOBA");
    ObservableList<String> choices = FXCollections.observableArrayList("FPS","RTS","RPG","MMORPG","BR","MOBA");

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        inventoryModel = new InventoryModel();


        //If the user logs in as a customer rather than the admin this will throw an exception
        //because these attributes does not exist on the customer screen. One of the disadvantages
        //of having 1 controller for 2 screens
        try {
            adminComboCategory.setItems(categories);
            adminChoiceCategory.setItems(choices);
            adminComboCategory.setValue("All");
            adminDisplay.setText(inventoryModel.readInventory(adminComboCategory.getValue()));
            adminFeedback.setText("Category: " + adminComboCategory.getValue());
            adminComboCategory.setValue(null);
        } catch (Exception e) {
        }

        try {
            customerComboCategory.setItems(categories);
            customerComboCategory.setValue("All");
            customerDisplay.setText(inventoryModel.readInventory(customerComboCategory.getValue()));
            customerFeedback.setText("Category: " + customerComboCategory.getValue());
            customerCart.setText(inventoryModel.displayShoppingCart());
            customerComboCategory.setValue(null);
        } catch (Exception e) {
        }
    }
}
