package jblockui;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import jblockmain.Measurement;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Enumeration;

public class MeasurementStackItemController extends BaseController
{
    @FXML
    Label measurementNameLabel;

    @FXML
    TextField valueEntry;

    @FXML
    Button plusButton;

    @FXML
    Button minusButton;

    private Measurement measurement;
    private boolean isValid = true;

    @Override
    public void initialize()
    {
        plusButton.setOnAction(e ->
        {
            try
            {
                setNewValue(true);
                isValid = true;
            }
            catch (NumberFormatException ex)
            {
                isValid = false;
            }
        });

        minusButton.setOnAction(e ->
        {
            try
            {
                setNewValue(false);
                isValid = true;
            }
            catch (NumberFormatException ex)
            {
                isValid = false;
            }
        });

        // Catch typed changed
        valueEntry.textProperty().addListener((observable, oldValue, newValue) ->
        {
            try
            {
                // Validate the new value
                var value = new BigDecimal(newValue);
                isValid = true;
            }
            catch (NumberFormatException ex)
            {
                isValid = false;
            }
        });
    }

    public void setNewValue(boolean isPlus)
    {
        var oldValue = new BigDecimal(valueEntry.getText());
        oldValue = oldValue.setScale(1, RoundingMode.HALF_UP);
        var newValue = isPlus ? oldValue.add(new BigDecimal("0.1")) : oldValue.subtract(new BigDecimal("0.1"));
        valueEntry.setText(String.valueOf(newValue));
    }

    public void setMeasurement(Measurement m)
    {
        measurement = m;
        measurementNameLabel.setText(m.name);
        BigDecimal value = null;
        try
        {
            value = new BigDecimal(m.getValue());
            value = value.setScale(1, RoundingMode.HALF_UP);
        }
        catch (NumberFormatException ignored)
        {
        }
        valueEntry.setText(m.getInputId() == null ? String.valueOf(value) : m.getInputId());
        valueEntry.setDisable(m.getInputId() != null);
        plusButton.setDisable(m.getInputId() != null);
        minusButton.setDisable(m.getInputId() != null);
    }

    public boolean getIsValid()
    {
        return isValid;
    }
}
