package jblockmain;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import jblockui.UiModel;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.Style;

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
        var bundle = ResourceBundle.getBundle("settings");
        var title = bundle.getString("app_name") + " - v" + bundle.getString("maj_ver") + "." + bundle.getString("min_ver");
        Scene scene = new Scene(root,
                Double.parseDouble(bundle.getString("prefWidth")),
                Double.parseDouble(bundle.getString("prefHeight")));
        stage.setTitle(title);
        JMetro jMetro = new JMetro(Style.LIGHT);
        jMetro.setScene(scene);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}