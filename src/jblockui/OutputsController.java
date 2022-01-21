package jblockui;

import javafx.fxml.FXML;
import javafx.scene.layout.VBox;

public class OutputsController extends BaseController
{
    @FXML
    private VBox outputStack;

    @Override
    public void initialize()
    {
        backButton.setOnAction(e -> UiModel.getInstance().setContent("PatternSelection"));
    }
}
