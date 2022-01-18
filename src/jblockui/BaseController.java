package jblockui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public abstract class BaseController
{
    @FXML
    Button nextButton;

    @FXML
    Button backButton;

    @FXML
    public abstract void initialize();
}
