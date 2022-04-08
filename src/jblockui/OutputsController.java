package jblockui;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import jblockenums.EActivityType;

import java.util.ArrayList;
import java.util.List;

public class OutputsController extends BaseController
{
    @FXML
    private StackPane previewView;

    @FXML
    private CheckBox checkScaleBoxAndUser;

    @FXML
    private CheckBox checkPatternOutline;

    @FXML
    private CheckBox checkKeypointsAsCircles;

    @FXML
    private CheckBox checkKeypointCoordinates;

    @FXML
    private CheckBox checkConstructionLines;

    @FXML
    private CheckBox checkTimeStamp;

    @FXML
    ProgressBar progressBar;

    @Override
    public void initialize()
    {
        backButton.setOnAction(e ->
        {
            UiModel.getInstance().setContent(
                    UiModel.getInstance().getActivity() == EActivityType.DRAFTING ?
                    "PatternSelection" :
                    "Analysis" );
        });

        nextButton.setOnAction(e ->
        {
            UiModel.getInstance().run();
        });

        var imgFiles = new ArrayList<String>()
        {{
            add("file:images/PatternSample_00000.png");
            add("file:images/PatternSample_00001.png");
            add("file:images/PatternSample_00010.png");
            add("file:images/PatternSample_00100.png");
            add("file:images/PatternSample_01000.png");
            add("file:images/PatternSample_10000.png");
        }};
        initialisePreviewLayers(imgFiles);

        checkScaleBoxAndUser.selectedProperty().addListener((obs, oldVal, newVal) ->
        {
            UiModel.getInstance().setCheck("checkScale", newVal);
            previewView.getChildren().get(5).setVisible(newVal);
        });

        checkPatternOutline.selectedProperty().addListener((obs, oldVal, newVal) ->
        {
            UiModel.getInstance().setCheck("checkOutline", newVal);
            previewView.getChildren().get(4).setVisible(newVal);
        });

        checkKeypointsAsCircles.selectedProperty().addListener((obs, oldVal, newVal) ->
        {
            UiModel.getInstance().setCheck("checkCircles", newVal);
            previewView.getChildren().get(3).setVisible(newVal);
        });

        checkKeypointCoordinates.selectedProperty().addListener((obs, oldVal, newVal) ->
        {
            UiModel.getInstance().setCheck("checkCoord", newVal);
            previewView.getChildren().get(2).setVisible(newVal);
        });

        checkConstructionLines.selectedProperty().addListener((obs, oldVal, newVal) ->
        {
            UiModel.getInstance().setCheck("checkCon", newVal);
            previewView.getChildren().get(1).setVisible(newVal);
        });

        checkTimeStamp.selectedProperty().addListener((obs, oldVal, newVal) ->
        {
            UiModel.getInstance().setCheck("checkTime", newVal);
        });

        // Set all the checks to true by default
        checkScaleBoxAndUser.setSelected(true);
        checkPatternOutline.setSelected(true);
        checkKeypointsAsCircles.setSelected(true);
        checkKeypointCoordinates.setSelected(true);
        checkConstructionLines.setSelected(true);
        checkTimeStamp.setSelected(true);

        // Hide the timestamp if analysis
        checkTimeStamp.setVisible(UiModel.getInstance().getActivity() == EActivityType.DRAFTING);


    }

    public void setProgress(double value)
    {
        if (value == 0.0) progressBar.setVisible(false);
        else
        {
            progressBar.setVisible(true);
            progressBar.setProgress(value);
        }
    }

    private void initialisePreviewLayers(List<String> files)
    {
        var layers = previewView.getChildren();
        for (var i : files)
        {
            var imgView = new ImageView(i);
            imgView.setPreserveRatio(true);
            imgView.setFitHeight(400);
            imgView.setFitWidth(400);
            layers.add(imgView);
        }
    }
}
