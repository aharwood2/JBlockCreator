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

    /**
     * Do things that must be done when the view is displayed rather than created
     */
    public void onDisplayed()
    {
    };
}
