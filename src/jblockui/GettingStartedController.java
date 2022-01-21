package jblockui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.RadioButton;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import jblockenums.EActivityType;

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
    public EActivityType activityType = EActivityType.DRAFTING;

    @Override
    public void initialize()
    {
        activityRadioGrp.selectedToggleProperty().addListener((changed, oldVal, newVal) ->
            {
                RadioButton rb = (RadioButton)newVal;
                activityType = rb == radioActivityDrafting ? EActivityType.DRAFTING : EActivityType.ANALYSIS;
                Validate();
            }
        );

        inputButton.setOnAction(e ->
        {
//            File file = fileChooser.showOpenDialog(null);
//            if (file != null)
//            {
//                inputFilePathLabel.setText(file.getPath());
//            }
            inputFilePathLabel.setText("C:\\Apps\\JBlockCreator\\input\\Batch5.TXT");
            Validate();
        });

        outputButton.setOnAction(e ->
        {
//            File file = directoryChooser.showDialog(null);
//            if (file != null)
//            {
//                outputPathLabel.setText(file.getPath());
//            }
            outputPathLabel.setText("C:\\");
            Validate();
        });

        nextButton.setOnAction(e ->
        {
            UiModel.getInstance().activityType = activityType;
            UiModel.getInstance().inputFile = inputFilePathLabel.getText();
            UiModel.getInstance().outputPath = outputPathLabel.getText();
            UiModel.getInstance().setContent(activityType == EActivityType.ANALYSIS ? "Analysis" : "PatternSelection");
            if (activityType == EActivityType.ANALYSIS) UiModel.getInstance().onDisplayed("Analysis");
        });

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