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
import java.util.ResourceBundle;

public class HomeScreenController implements Initializable {

    private Stage loginStage, registerStage;


    @FXML
    private Label welcomeHeadline;
    @FXML
    private Button registerButton;
    @FXML
    private Button loginButton;

    //Redirect to the login screen from the home screen
    public void handleLoginBtn(ActionEvent e) throws IOException {
        loginStage = (Stage)loginButton.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/LoginScreen.fxml"));
        root.getStylesheets().add("/css/stylesheet.css");
        loginStage.setTitle("Login Screen");
        loginStage.setScene(new Scene(root));
    }

    //Redirect to the register screen from the home screen
    public void handleRegisterBtn(ActionEvent e) throws IOException {
        registerStage = (Stage)registerButton.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/RegisterScreen.fxml"));
        root.getStylesheets().add("/css/stylesheet.css");
        registerStage.setTitle("Register Screen");
        registerStage.setScene(new Scene(root));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
