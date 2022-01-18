package jblockui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import jblockenums.EPattern;
import jblockmain.JBlockCreatorApp;

import java.io.IOException;
import java.util.Objects;

public class PatternSelectionController extends  BaseController
{
    @FXML
    private VBox patternStack;


    @Override
    public void initialize()
    {
        try
        {
            // Build views in pattern selection stack from known patterns
            EPattern[] patternTypes = EPattern.class.getEnumConstants();
            for (var p : patternTypes)
            {
                var loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/jblockui/PatternStackItem.fxml")));
                var view = (GridPane)loader.load();
                var ctrl = (PatternStackItemController)loader.getController();
                ctrl.checkInclude.setText(p.toString());
                patternStack.getChildren().add(view);
            }
        }
        catch (IOException e)
        {
        }

        nextButton.setOnAction(e ->
        {
            JBlockCreatorApp.getInstance().setContent("Outputs");
        });

        backButton.setOnAction(e ->
        {
            JBlockCreatorApp.getInstance().setContent("GettingStarted");
        });
    }
}
