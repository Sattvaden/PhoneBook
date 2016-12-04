package start;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Locale;
import java.util.ResourceBundle;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("../fxml/main.fxml"));
        loader.setResources(ResourceBundle.getBundle("bundles.Locale", new Locale("ru")));
        Parent root = loader.load();
        primaryStage.setMinWidth(330);
        primaryStage.setMaxHeight(520);
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 330, 520));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
