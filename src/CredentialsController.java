import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

public class CredentialsController implements Initializable {

    private CredentialsModel credentialsModel;
    private Stage customerStage, adminStage, homeStage;

    //LoginScreen attributes
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;
    @FXML
    private Button submitButton;
    @FXML
    private Label credentialsFeedback;

    //RegisterScreen attributes
    @FXML
    private TextField registerUsername;
    @FXML
    private PasswordField registerPassword;
    @FXML
    private PasswordField registerPasswordConfirmation;
    @FXML
    private Label registerCredentialsFeedback;
    @FXML
    private Button signUpButton;

    //Both
    @FXML
    private Button backButton;

    /*
    LoginScreen methods
     */

    //Confirms login credentials and redirects to either customer screen or admin screen
    //based on the input entered by the user. Outputs a message if wrong info is submitted
    public void handleSubmitBtn(ActionEvent e) throws IOException{

        //The verifyCrendentials method returns the map entry from the map in CredentialsModel
        //if the input from the user matches the key/value pair from the entry
        Map.Entry<String, String> verification = credentialsModel.verifyCredentials(username.getText(),password.getText());

        if (verification != null && verification.getKey().trim().equals("admin") &&
                verification.getValue().trim().equals("nimda")) {
            adminStage = (Stage)submitButton.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("/AdminScreen.fxml"));
            root.getStylesheets().add("/css/stylesheet.css");
            adminStage.setTitle("Admin Screen");
            adminStage.setScene(new Scene(root));
        } else if (verification != null && verification.getKey().trim().equals(username.getText()) &&
                verification.getValue().trim().equals(password.getText())){
            customerStage = (Stage)submitButton.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("/CustomerScreen.fxml"));
            root.getStylesheets().add("/css/stylesheet.css");
            customerStage.setTitle("Customer Screen");
            customerStage.setScene(new Scene(root));
        } else{
            credentialsFeedback.setText("Username and password doesn't match. Try again.");
        }
    }

    /*
    RegisterScreen methods
     */

    //Verifies that a username and password has been entered and the two passwords match each other
    //and the verifyCredentials method in the if statement checks weather the user is already registered.
    //if all this matches the criteria it registers the user and returns to the home screen
    //else it wipes the fields and gives a feedback message.
    @SuppressWarnings("Duplicates")
    public void handleSignUpBtn(ActionEvent e) {
        if (registerUsername.getText().trim().length() > 0 && registerPassword.getText().trim().length() > 0 &&
                registerPassword.getText().trim().equals(registerPasswordConfirmation.getText().trim()) &&
                credentialsModel.verifyCredentials(registerUsername.getText().trim(), registerPassword.getText().trim()) == null) {
            credentialsModel.registerUser(registerUsername.getText(),registerPassword.getText());
            try {
                homeStage = (Stage)signUpButton.getScene().getWindow();
                Parent root = FXMLLoader.load(getClass().getResource("/HomeScreen.fxml"));
                root.getStylesheets().add("/css/stylesheet.css");
                homeStage.setTitle("Home Screen");
                homeStage.setScene(new Scene(root));
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        } else {
            registerCredentialsFeedback.setText("Remember to fill everything. Passwords didn't match or the user already exists");
            registerUsername.setText("");
            registerPassword.setText("");
            registerPasswordConfirmation.setText("");
        }
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
            root.getStylesheets().add("/css/stylesheet.css");
            homeStage.setTitle("Home Screen");
            homeStage.setScene(new Scene(root));
        } catch (IOException e1) {
            System.out.println(e1);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        credentialsModel = new CredentialsModel();
    }
}
