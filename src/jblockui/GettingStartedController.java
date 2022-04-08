package jblockui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.RadioButton;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import jblockenums.EActivityType;

import java.io.File;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;
import  java.util.prefs.*;

public class GettingStartedController extends BaseController
{
    @FXML
    private Label inputFilePathLabel;

    @FXML
    private Label outputPathLabel;

    @FXML
    private Button inputButton;

    @FXML
    private Button outputButton;

    @FXML
    ToggleGroup activityRadioGrp;

    @FXML
    RadioButton radioActivityDrafting;

    @FXML
    RadioButton radioActivityAnalysis;

    // Locals
    final FileChooser fileChooser = new FileChooser();
    final DirectoryChooser directoryChooser = new DirectoryChooser();

    // Preference keys for this package
    private static final String INPUT_PATH = "in_path";
    private static final String OUTPUT_PATH = "out_path";
    private final Preferences prefs = Preferences.userNodeForPackage(GettingStartedController.class);

    // Public
    public EActivityType activityType = EActivityType.DRAFTING;

    @Override
    public void initialize()
    {
        // Initialise paths
        var path = prefs.get(INPUT_PATH, null);
        if (path != null)
        {
            inputFilePathLabel.setText(path);
            fileChooser.setInitialDirectory(new File(new File(path).getParent()));
        }
        path = prefs.get(OUTPUT_PATH, null);
        if (path != null)
        {
            outputPathLabel.setText(path);
            directoryChooser.setInitialDirectory(new File(path));
        }

        activityRadioGrp.selectedToggleProperty().addListener((changed, oldVal, newVal) ->
        {
            RadioButton rb = (RadioButton)newVal;
            activityType = rb == radioActivityDrafting ? EActivityType.DRAFTING : EActivityType.ANALYSIS;
            Validate();
        });

        inputButton.setOnAction(e ->
        {
            File file = fileChooser.showOpenDialog(null);
            if (file != null)
            {
                inputFilePathLabel.setText(file.getPath());
                prefs.put(INPUT_PATH, file.getPath());
                fileChooser.setInitialDirectory(new File(file.getParent()));
            }
            Validate();
        });

        outputButton.setOnAction(e ->
        {
            File file = directoryChooser.showDialog(null);
            if (file != null)
            {
                outputPathLabel.setText(file.getPath());
                prefs.put(OUTPUT_PATH, file.getPath());
                directoryChooser.setInitialDirectory(file);
            }
            Validate();
        });

        nextButton.setOnAction(e ->
        {
            UiModel.getInstance().setActivityType(activityType);
            UiModel.getInstance().setInputFile(inputFilePathLabel.getText());
            UiModel.getInstance().setOutputPath(outputPathLabel.getText());
            UiModel.getInstance().setContent(activityType == EActivityType.ANALYSIS ? "Analysis" : "PatternSelection");
            if (activityType == EActivityType.ANALYSIS) UiModel.getInstance().onDisplayed("Analysis");
        });

        // Set default
        activityRadioGrp.selectToggle(radioActivityDrafting);

        Validate();
    }

    /**
     * Method to determine state of the next button
     */
    private void Validate()
    {
        // Set next button
        var valid =
            (isValidPath(inputFilePathLabel.getText())) &&
            isValidPath(outputPathLabel.getText());
        nextButton.setDisable(!valid);
    }

    private boolean isValidPath(String path)
    {
        try
        {
            Paths.get(path);
        }
        catch (InvalidPathException | NullPointerException ex)
        {
            return false;
        }
        return true;
    }
}