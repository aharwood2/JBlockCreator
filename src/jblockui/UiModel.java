package jblockui;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import jblockenums.EActivityType;
import jblockenums.EPattern;
import jblockmain.MeasurementSet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class UiModel
{
    private static UiModel instance;

    private UiModel(){}

    public static UiModel getInstance(){
        if(instance == null){
            synchronized (UiModel.class) {
                if(instance == null){
                    instance = new UiModel();
                }
            }
        }
        return instance;
    }

    // UI Components
    private AnchorPane root;
    private final HashMap<String, VBox> subViews = new HashMap<>();
    private final HashMap<String, BaseController> controllers = new HashMap<>();

    // Properties from panes
    public ArrayList<EPattern> selectedPatterns = new ArrayList<>();
    public EActivityType activityType;
    public String inputFile;
    public String outputPath;

    /**
     * Initialise the scene and pre-load all views
     * @return Scene root
     */
    public AnchorPane Initialise()
    {
        try
        {
            // Load all the sub views
            loadSubView("GettingStarted");
            loadSubView("Analysis");
            loadSubView("PatternSelection");
            loadSubView("Measurements");
            loadSubView("Outputs");

            // Load the container
            root = (AnchorPane) FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/jblockui/UiContainer.fxml")));
            setContent("GettingStarted");
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return root;
    }

    /**
     * Loads in the sub views
     * @param name name of subview file
     * @throws IOException
     */
    private void loadSubView(String name) throws IOException
    {
        var loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/jblockui/" + name + ".fxml")));
        var view = (VBox)loader.load();
        subViews.put(name, view);
        controllers.put(name, loader.getController());
    }

    /**
     * Sets the content of the container
     * @param key sub view key
     */
    public void setContent(String key)
    {
        var view = subViews.get(key);
        if (view != null && root != null)
        {
            root.getChildren().clear();
            root.getChildren().add(view);
            AnchorPane.setTopAnchor(view, 0.0);
            AnchorPane.setBottomAnchor(view, 0.0);
            AnchorPane.setLeftAnchor(view, 0.0);
            AnchorPane.setRightAnchor(view, 0.0);
        }
    }

    /**
     * Calls onDisplayed for the controller matching the key
     * @param key sub view key
     */
    public void onDisplayed(String key)
    {
        try
        {
            controllers.get(key).onDisplayed();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void setMeasurementsInController(MeasurementSet measurementSet)
    {
        var ctrl = (MeasurementsController)controllers.get("Measurements");
        ctrl.setMeasurements(measurementSet);
    }
}
