package jblockui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import jblockenums.EPattern;
import java.util.ArrayList;
import java.util.Objects;

public class PatternSelectionController extends  BaseController
{
    @FXML
    private VBox patternStack;

    private final ArrayList<EPattern> patternsWithErrors = new ArrayList<>();
    private final ArrayList<EPattern> patternsChecked = new ArrayList<>();

    @Override
    public void initialize()
    {
        try
        {
            // Build views in pattern selection stack from known patterns
            patternStack.getChildren().clear();
            EPattern[] patternTypes = EPattern.class.getEnumConstants();
            for (var p : patternTypes)
            {
                var loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/jblockui/PatternStackItem.fxml")));
                var view = (GridPane)loader.load();
                var ctrl = (PatternStackItemController)loader.getController();
                ctrl.setParent(this);
                ctrl.setPatternType(p);
                ctrl.checkInclude.setText(p.toString());
                patternStack.getChildren().add(view);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        nextButton.setOnAction(e ->
        {
            UiModel.getInstance().setPatterns(patternsChecked);
            UiModel.getInstance().setContent("Outputs");
        });

        backButton.setOnAction(e ->
        {
            UiModel.getInstance().setContent("GettingStarted");
        });

        Validate();
    }

    public void onErrorChanged(EPattern patternWithError, boolean state)
    {
        if (state && !patternsWithErrors.contains(patternWithError)) patternsWithErrors.add(patternWithError);
        else if (!state) patternsWithErrors.remove(patternWithError);
        Validate();
    }

    public void onCheckChanged(EPattern pattern, boolean state)
    {
        if (state && !patternsChecked.contains(pattern)) patternsChecked.add(pattern);
        else if (!state) patternsChecked.remove(pattern);
        Validate();
    }

    private void Validate()
    {
        nextButton.setDisable(patternsWithErrors.size() > 0 || patternsChecked.size() == 0);
    }
}
