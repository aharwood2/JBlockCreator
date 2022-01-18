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
    private AnchorPane root;
    private final HashMap<String, VBox> subPanes = new HashMap<>();
    private static JBlockCreatorApp instance;

    public static JBlockCreatorApp getInstance()
    {
        return instance;
    }

    @Override
    public void start(Stage stage) throws IOException
    {
        // Load all the sub views
        subPanes.put("GettingStarted", (VBox)FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/jblockui/GettingStarted.fxml"))));
        subPanes.put("Analysis", (VBox)FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/jblockui/Analysis.fxml"))));
        subPanes.put("PatternSelection", (VBox)FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/jblockui/PatternSelection.fxml"))));
        subPanes.put("InputFileData", (VBox)FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/jblockui/Measurements.fxml"))));
        subPanes.put("Outputs", (VBox)FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/jblockui/Outputs.fxml"))));

        // Load the container
        root = (AnchorPane)FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/jblockui/UiContainer.fxml")));
        setContent("GettingStarted");

        // Build the scene
        Scene scene = new Scene(root, 600, 400);
        var bundle = ResourceBundle.getBundle("strings");
        var title = bundle.getString("app_name") + " - v" + bundle.getString("maj_ver") + "." + bundle.getString("min_ver");
        stage.setTitle(title);
        stage.setScene(scene);
        stage.show();
        instance = this;
    }

    public static void main(String[] args) {
        launch();
    }

    /**
     * Sets the content of the container
     * @param key sub pane key
     */
    public void setContent(String key)
    {
        var view = subPanes.get(key);
        if (view != null && root != null)
        {
            root.getChildren().clear();
            root.getChildren().add(view);
        }
    }
}