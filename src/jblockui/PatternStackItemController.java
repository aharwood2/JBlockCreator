package jblockui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import jblockenums.EPattern;
import jblockmain.InputFileData;
import jblockmain.MeasurementSet;
import jblockmain.PatternFactory;

public class PatternStackItemController extends BaseController
{
    @FXML
    CheckBox checkInclude;

    @FXML
    Button measurementButton;

    @FXML
    Label statusLabel;

    private EPattern patternType;
    private boolean checked;
    private boolean hasError;
    private PatternSelectionController parent;
    private MeasurementSet requiredMeasurements;

    @Override
    public void initialize()
    {
        checkInclude.selectedProperty().addListener((obs, oldVal, newVal) ->
        {
            checked = newVal;
            if (parent != null) parent.onCheckChanged(patternType, checked);
            Validate();
        });

        measurementButton.setOnAction(e ->
        {
            UiModel.getInstance().setContent("Measurements");
            UiModel.getInstance().setMeasurementsInController(requiredMeasurements);
        });

        Validate();
    }

    private void Validate()
    {
        // Reset flags and text
        String statusText = "";
        hasError = false;

        // Check we have the measurements we need in the input file
        if (checked)
        {
            var available = new InputFileData(UiModel.getInstance().inputFile).getInputValueIds();
            requiredMeasurements = PatternFactory.Create(patternType, null, null).getMeasurementSet();

            // Check each measurement with an ID in the required set are in the input file
            for (var id : requiredMeasurements.getIds())
            {
                if (!available.contains(id))
                {
                    statusText = id + " missing from input file!";
                    hasError = true;
                    break;
                }
            }
        }
        statusLabel.setText(statusText);
        if (parent != null) parent.onErrorChanged(patternType, hasError);

        // Set state of the measurement button
        measurementButton.setDisable(!checked);
    }

    public EPattern getPatternType()
    {
        return patternType;
    }

    public void setPatternType(EPattern patternType)
    {
        this.patternType = patternType;
    }

    public boolean isHasError()
    {
        return hasError;
    }

    public void setParent(PatternSelectionController patternSelectionController)
    {
        parent = patternSelectionController;
    }

    public MeasurementSet getRequiredMeasurements()
    {
        return requiredMeasurements;
    }
}
