package start;

import controllers.Controller;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import objects.Lang;
import utils.LocaleManager;

import java.io.IOException;
import java.util.Locale;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;

public class Main extends Application implements Observer{
    private static final String FXML_MAIN = "../fxml/main.fxml";
    public static final String BUNDLES_LOCALE = "bundles.Locale";

    private Controller controller;
    private FXMLLoader loader;

    private Stage primaryStage;
    private VBox currentRoot;
    @Override
    public void start(Stage primaryStage) throws Exception{
        this.primaryStage = primaryStage;
        createGUI(LocaleManager.EN_LOCALE);
//        loader = new FXMLLoader();
//        loader.setLocation(getClass().getResource("../fxml/main.fxml"));
//        loader.setResources(ResourceBundle.getBundle("bundles.Locale", new Locale("ru")));
//        Parent root = loader.load();
//        primaryStage.setMinWidth(330);
//        primaryStage.setMaxHeight(520);
//        primaryStage.setTitle(loader.getResources().getString("key.phoneBook"));
//        primaryStage.setScene(new Scene(root, 330, 520));
//        primaryStage.show();
    }
    private void createGUI(Locale locale) {
        currentRoot = loadFXML(locale);
        Scene scene = new Scene(currentRoot, 300, 275);
        primaryStage.setScene(scene);
        primaryStage.setMinHeight(600);
        primaryStage.setMinWidth(400);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void update(Observable o, Object arg) {
        Lang lang = (Lang)arg;
        VBox newNode = loadFXML(lang.getLocal());
        currentRoot.getChildren().setAll(newNode.getChildren());
    }
    private VBox loadFXML(Locale locale) {
        loader = new FXMLLoader();

        loader.setLocation(getClass().getResource(FXML_MAIN));
        loader.setResources(ResourceBundle.getBundle(BUNDLES_LOCALE, locale));
        primaryStage.setTitle(loader.getResources().getString("key.phoneBook"));
        VBox node = null;

        try {
            node = (VBox) loader.load();

            controller = loader.getController();
            controller.addObserver(this);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return node;
    }
}
