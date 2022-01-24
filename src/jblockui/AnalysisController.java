package jblockui;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.util.Pair;
import javafx.util.StringConverter;
import jblockenums.EActivityType;
import jblockenums.EPlotType;
import jblockmain.InputFileData;

public class AnalysisController extends BaseController
{
    @FXML
    ToggleGroup plotRadioGrp;

    @FXML
    ComboBox<Pair<String, String>> comboMeasX;

    @FXML
    ComboBox<Pair<String, String>> comboMeasY;

    @FXML
    RadioButton radioPlotRectangle;

    private String idX;
    private String idY;
    private EPlotType plotType;

    @Override
    public void initialize()
    {
        backButton.setOnAction(e -> UiModel.getInstance().setContent("GettingStarted"));

        nextButton.setOnAction(e ->
        {
            UiModel.getInstance().setIds(idX, idY);
            UiModel.getInstance().setPlotType(plotType);
            UiModel.getInstance().setContent("Outputs");
        });

        comboMeasX.valueProperty().addListener((obs, oldVal, newVal) ->
        {
            idX = newVal.getKey();
            Validate();
        });

        comboMeasY.valueProperty().addListener((obs, oldVal, newVal) ->
        {
            idY = newVal.getKey();
            Validate();
        });

        Validate();

        plotRadioGrp.selectedToggleProperty().addListener((obs, oldVal, newVal) ->
        {
            RadioButton rb = (RadioButton)newVal;
            plotType = rb == radioPlotRectangle ? EPlotType.RECTANGLE : EPlotType.LAYERED;
            Validate();
        });
    }

    private void Validate()
    {
        nextButton.setDisable(idX == null || idY == null);
    }

    @Override
    public void onDisplayed()
    {
        super.onDisplayed();

        // Populate the combo boxes
        var available = new InputFileData(UiModel.getInstance().getInputFile()).getInputValues();
        var strConverter = new StringConverter<Pair<String, String>>()
        {
            @Override
            public String toString(Pair<String, String> stringStringPair)
            {
                return "(" + stringStringPair.getKey() + ") " + stringStringPair.getValue();
            }

            @Override
            public Pair<String, String> fromString(String s)
            {
                s = s.replace("(", "");
                s = s.replace(") ", "|");
                var splits = s.split("|");
                return new Pair<>(splits[0], splits[1]);
            }
        };
        comboMeasX.setItems(FXCollections.observableList(available));
        comboMeasX.setConverter(strConverter);
        comboMeasY.setItems(FXCollections.observableList(available));
        comboMeasY.setConverter(strConverter);
    }
}
