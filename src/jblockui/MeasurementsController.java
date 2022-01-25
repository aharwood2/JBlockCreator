package jblockui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import jblockenums.EPattern;
import jblockmain.MeasurementSet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class MeasurementsController extends BaseController
{
    private MeasurementSet measurementSet;
    private EPattern patternType;
    private final HashMap<String, Double> changedMeasurements = new HashMap<>();
    private final List<MeasurementStackItemController> controllers = new ArrayList<>();

    @FXML
    VBox measurementsStack;

    @Override
    public void initialize()
    {
        backButton.setOnAction(e ->
        {
            UiModel.getInstance().setContent("PatternSelection");
        });

        nextButton.setOnAction(e ->
        {
            // Pull values from controllers and update
            for (var c : controllers)
            {
                if (!c.valueEntry.isDisabled() && !c.getIsValid())
                {
                    var a = new Alert(Alert.AlertType.NONE, "Value specified for " + c.measurementNameLabel.getText() + " is invalid!", ButtonType.OK);
                    a.show();
                    return;
                }
            }
            for (var c : controllers)
            {
                if (!c.valueEntry.isDisabled())
                {
                    measurementSet.setValue(c.measurementNameLabel.getText(), Double.parseDouble(c.valueEntry.getText()));
                }
            }
            UiModel.getInstance().storeMeasurementTemplate(patternType, measurementSet);
            UiModel.getInstance().setContent("PatternSelection");
        });
    }

    public void initialiseMeasurementsFromTemplate(EPattern type)
    {
        this.patternType = type;
        this.measurementSet = UiModel.getInstance().getMeasurementTemplate(patternType);

        try
        {
            // Initialise the stack
            measurementsStack.getChildren().clear();
            controllers.clear();
            for (var m : measurementSet.getAllMeasurements())
            {
                var loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/jblockui/MeasurementStackItem.fxml")));
                var view = (GridPane) loader.load();
                var ctrl = (MeasurementStackItemController) loader.getController();
                ctrl.setMeasurement(m);
                controllers.add(ctrl);
                measurementsStack.getChildren().add(view);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
