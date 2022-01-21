package jblockmain;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import jblockui.UiModel;

import java.io.IOException;
import java.util.*;

public class JBlockCreatorApp extends Application
{

    @Override
    public void start(Stage stage) throws IOException
    {
        // Initialise the UIModel
        var root = UiModel.getInstance().Initialise();

        // Display the scene
        Scene scene = new Scene(root, 600, 400);
        var bundle = ResourceBundle.getBundle("strings");
        var title = bundle.getString("app_name") + " - v" + bundle.getString("maj_ver") + "." + bundle.getString("min_ver");
        stage.setTitle(title);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}