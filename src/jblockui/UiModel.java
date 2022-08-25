package jblockui;

import analysis.RectanglePlot;
import dxfwriter.DxfFileConfiguration;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import jblockenums.EActivityType;
import jblockenums.EPattern;
import jblockenums.EPlotType;
import jblockmain.InputFileData;
import jblockmain.MeasurementSet;
import jblockmain.PatternFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class UiModel
{
    private static UiModel instance;
    private boolean isRunning;

    private UiModel()
    {
    }

    public static UiModel getInstance()
    {
        if (instance == null)
        {
            synchronized (UiModel.class)
            {
                if (instance == null)
                {
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

    // Processing elements
    private final HashMap<EPattern, MeasurementSet> measurements = new HashMap<>();
    private final List<String> outputChecks = new ArrayList<>();
    private final ArrayList<EPattern> selectedPatterns = new ArrayList<>();
    private EActivityType activityType;
    private EPlotType plotType;
    private String inputFile;
    private String outputPath;
    private String analysisIdX;
    private String analysisIdY;

    /**
     * Initialise the scene and pre-load all views
     *
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
     *
     * @param name name of subview file
     * @throws IOException if resource not found
     */
    private void loadSubView(String name) throws IOException
    {
        var loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/jblockui/" + name + ".fxml")));
        var view = (VBox) loader.load();
        subViews.put(name, view);
        controllers.put(name, loader.getController());
    }

    /**
     * Sets the content of the container
     *
     * @param key sub view key
     * @return the controller for the view
     */
    public BaseController setContent(String key)
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
        return controllers.get(key);
    }

    /**
     * Calls onDisplayed for the controller matching the key
     *
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

    /**
     * Stores the master measurement set generated on pattern enable
     *
     * @param key   the pattern
     * @param value the measurement set
     */
    public void storeMeasurementTemplate(EPattern key, MeasurementSet value)
    {
        measurements.put(key, value);
    }

    /**
     * Remove a master measurement set when pattern is disabled
     *
     * @param key the pattern
     */
    public void removeMeasurementTemplate(EPattern key)
    {
        measurements.remove(key);
    }

    /**
     * Gets the stored master measurement set generated on pattern enable
     *
     * @param key the pattern
     * @return the measurement set
     */
    public MeasurementSet getMeasurementTemplate(EPattern key)
    {
        return measurements.get(key);
    }

    /**
     * Method to run the software
     */
    public void run()
    {
        if (!isRunning)
        {
            isRunning = true;
            var ctrl = (OutputsController) controllers.get("Outputs");
            double i = 0;

            try
            {
                setContent("Running");

                String timeStamp = null;
                if (outputChecks.contains("checkTime"))
                {
                    timeStamp = new java.text.SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new java.util.Date());
                }

                // Open output file
                File outputDirectory = new File(outputPath);

                // Read in the full set of measurements
                var inputData = new InputFileData(inputFile, false);
                var names = inputData.getUserNames();

                // Create DXF config
                var config = new DxfFileConfiguration(timeStamp, outputChecks);

                // Perform drafting
                if (activityType == EActivityType.DRAFTING)
                {
                    // Loop over the patterns chosen
                    for (var p : selectedPatterns)
                    {
                        // Loop over the users in the input data
                        for (var user : names)
                        {
                            // Update progress bar
                            ctrl.setProgress(i / (selectedPatterns.size() * names.size()));

                            // Construct the pattern
                            var pattern = PatternFactory.Create(p, user, inputData, getMeasurementTemplate(p));
                            assert pattern != null;

                            // Construct the blocks
                            pattern.createBlocks();

                            // Write out the file
                            pattern.writeToDXF(outputDirectory, config);

                            // Count
                            i++;
                        }
                    }
                }

                // Perform analysis
                else
                {
                    var plot = new RectanglePlot(inputData, analysisIdX, analysisIdY, plotType);
                    for (var user : names)
                    {
                        ctrl.setProgress(i / (names.size()));
                        plot.addNewRectangle(user);
                        i++;
                    }
                    plot.writeToDXF(outputDirectory, config);
                }
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }
            finally
            {
                ctrl.setProgress(0);
                setContent("GettingStarted");
                isRunning = false;
            }
        }
    }

    /**
     * Set the state of the checkboxes
     *
     * @param key    the name of the checkbox
     * @param newVal its state
     */
    public void setCheck(String key, Boolean newVal)
    {
        if (newVal) outputChecks.add(key);
        else outputChecks.remove(key);
    }

    // Getters and Setters //

    public EActivityType getActivity()
    {
        return activityType;
    }

    public String getInputFile()
    {
        return inputFile;
    }

    public void setActivityType(EActivityType activityType)
    {
        this.activityType = activityType;
    }

    public void setInputFile(String inputFile)
    {
        this.inputFile = inputFile;
    }

    public void setOutputPath(String outputPath)
    {
        this.outputPath = outputPath;
    }

    public void setIds(String idX, String idY)
    {
        analysisIdX = idX;
        analysisIdY = idY;
    }

    public void setPlotType(EPlotType plotType)
    {
        this.plotType = plotType;
    }

    public void setPatterns(ArrayList<EPattern> patternsChecked)
    {
        selectedPatterns.clear();
        selectedPatterns.addAll(patternsChecked);
    }
}
