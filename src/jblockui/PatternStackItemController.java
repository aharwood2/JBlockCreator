package jblockui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;

public class PatternStackItemController extends BaseController
{
    @FXML
    CheckBox checkInclude;

    @FXML
    Button measurementButton;

    @FXML
    Label statusLabel;

    private boolean checked;

    @Override
    public void initialize()
    {
        checkInclude.selectedProperty().addListener((obs, oldVal, newVal) ->
        {
            checked = newVal;
            Validate();
        });

        Validate();
    }

    private void Validate()
    {
        measurementButton.setDisable(!checked);
    }
}
