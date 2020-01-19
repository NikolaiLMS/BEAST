package edu.pse.beast.highlevel.javafx.resultpresenter.resultTypes;

import java.util.Iterator;
import java.util.List;

import javafx.scene.Node;

import org.fxmisc.flowless.VirtualizedScrollPane;
import org.fxmisc.richtext.GenericStyledArea;
import org.reactfx.util.Either;

import edu.pse.beast.highlevel.javafx.AnalysisType;
import edu.pse.beast.propertychecker.Result;
import edu.pse.beast.toolbox.LinkedImage;
import edu.pse.beast.toolbox.ParStyle;
import edu.pse.beast.toolbox.TextFieldCreator;
import edu.pse.beast.toolbox.TextStyle;

/**
 * The Class CBMCError.
 *
 * @author Lukas Stapelbroek
 */
public final class CBMCError extends ResultPresentationType {
    /** The area. */
    private GenericStyledArea<ParStyle, Either<String, LinkedImage>, TextStyle> area;

    @Override
    public Node presentResult(final Result result) {
        if (area == null) {
            area = TextFieldCreator.getGenericStyledAreaInstance(
                    TextStyle.fontSize(STANDARD_SIZE), ParStyle.EMPTY);
            area.setEditable(false);
        }
        List<String> errorText = result.getErrorText();
        for (Iterator<String> iterator = errorText.iterator();
                iterator.hasNext();) {
            String text = iterator.next();
            area.appendText(text);
        }

        VirtualizedScrollPane<GenericStyledArea<ParStyle,
                                                Either<String, LinkedImage>,
                                                TextStyle>> vsPane =
            new VirtualizedScrollPane<>(area); // Wrap it in a scroll area
        return vsPane;
    }

    @Override
    public String getName() {
        return "CBMC Error";
    }

    @Override
    public String getToolTipDescription() {
        return "Shows the error output cbmc gave";
    }

    @Override
    public boolean supportsZoom() {
        return true;
    }

    @Override
    public void zoomTo(final double zoomValue) {
        if (area != null) {
            area.setStyle(0, area.getLength(),
                          TextStyle.fontSize((int) (STANDARD_SIZE + zoomValue)));
        }
    }

    @Override
    public boolean supports(final AnalysisType analysisType) {
        return true;
    }

    @Override
    public boolean isDefault() {
        return false;
    }
}
