package jblockui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.RadioButton;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import jblockenums.EActivityType;
import jblockenums.EInputType;
import jblockmain.JBlockCreatorApp;

import java.io.File;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;

public class GettingStartedController extends BaseController
{
    @FXML
    private Label stepInputLabel;

    @FXML
    private Label inputFilePathLabel;

    @FXML
    private Label outputPathLabel;

    @FXML
    private Button nextButton;

    @FXML
    private Button inputButton;

    @FXML
    private Button outputButton;

    @FXML
    ToggleGroup inputRadioGrp;

    @FXML
    RadioButton radioInputInteractive;

    @FXML
    RadioButton radioInputFile;

    @FXML
    ToggleGroup activityRadioGrp;

    @FXML
    RadioButton radioActivityDrafting;

    @FXML
    RadioButton radioActivityAnalysis;

    // Locals
    final FileChooser fileChooser = new FileChooser();
    final DirectoryChooser directoryChooser = new DirectoryChooser();

    // Public
    public EInputType inputType = EInputType.INTERACTIVE;
    public EActivityType activityType = EActivityType.DRAFTING;

    public void initialize()
    {
        inputRadioGrp.selectedToggleProperty().addListener((changed, oldVal, newVal) ->
            {
                RadioButton rb = (RadioButton)newVal;
                inputType = rb == radioInputInteractive ? EInputType.INTERACTIVE : EInputType.FILE;
                Validate();
            }
        );

        activityRadioGrp.selectedToggleProperty().addListener((changed, oldVal, newVal) ->
            {
                RadioButton rb = (RadioButton)newVal;
                activityType = rb == radioActivityDrafting ? EActivityType.DRAFTING : EActivityType.ANALYSIS;
                Validate();
            }
        );

        inputButton.setOnAction(e ->
        {
            File file = fileChooser.showOpenDialog(null);
            if (file != null)
            {
                inputFilePathLabel.setText(file.getPath());
            }
            Validate();
        });

        outputButton.setOnAction(e ->
        {
            File file = directoryChooser.showDialog(null);
            if (file != null)
            {
                outputPathLabel.setText(file.getPath());
            }
            Validate();
        });

        nextButton.setOnAction(e ->
        {
            JBlockCreatorApp.getInstance().setContent(activityType == EActivityType.ANALYSIS ? "Analysis" : "PatternSelection");
        });

        Validate();
    }

    /**
     * Method to determine state of the next button
     */
    private void Validate()
    {
        // Set input step
        var enabledInput = inputType == EInputType.FILE;
        inputButton.setDisable(!enabledInput);
        inputFilePathLabel.setDisable(!enabledInput);
        stepInputLabel.setDisable(!enabledInput);

        // Set next button
        var valid =
            (inputType == EInputType.INTERACTIVE || (inputType == EInputType.FILE && isValidPath(inputFilePathLabel.getText()))) &&
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