import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    //Would have made Model, View & Controller packages, but for some reason scenebuilder can't find
    //Ids if the controller is not in the same package as the fxml file

    //Note to self
    //You could have avoided using 1 controller for 2 screens if you used your brain a little.
    //You could have accessed the content of the ArrayList inventory by accessing the products.xml
    //instead of using the same controller and accessing the ArrayList directly. Great job JO, finding
    //that out 3 days before the deadline

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/HomeScreen.fxml"));
        root.getStylesheets().add("/css/stylesheet.css");
        primaryStage.setTitle("Home Screen");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
