package edu.pse.beast.booleanexpeditor.booleanExpCodeArea;

import edu.pse.beast.booleanexpeditor.booleanExpCodeArea.autocompletion.BooleanExpAutoCompletionSrc;
import edu.pse.beast.booleanexpeditor.booleanExpCodeArea.errorFinder.BooleanExpEditorGrammarErrorFinder;
import edu.pse.beast.booleanexpeditor.booleanExpCodeArea.errorFinder.BooleanExpEditorVariableErrorFinder;
import edu.pse.beast.booleanexpeditor.booleanExpCodeArea.errorFinder.BooleanExpErrorDisplayer;
import edu.pse.beast.celectiondescriptioneditor.CElectionDescriptionEditor;
import edu.pse.beast.codearea.Autocompletion.AutocompletionOption;
import edu.pse.beast.codearea.CodeArea;
import edu.pse.beast.codearea.CodeAreaBuilder;
import edu.pse.beast.codearea.ErrorHandling.ErrorDisplayer;
import edu.pse.beast.datatypes.propertydescription.SymbolicVariableList;
import edu.pse.beast.toolbox.ObjectRefsForBuilder;
import java.util.ArrayList;

import javax.swing.*;

/**
 * Builder class to create a BooleanExpCodeArea object.
 * @author Nikolai
 */
public class BooleanExpCodeAreaBuilder extends CodeAreaBuilder{

    /**
     * Creates a BooleanExpCodeArea object with the given parameters
     * @param objectRefs the ObjectRefsForBuilder instance providing the references to needed loading interfaces
     * @param textPane the JTextPane this CodeArea controls
     * @param scrollPane the ScrollPane of said JTextPane
     * @return a BooleanExpCodeArea object
     */
    public BooleanExpCodeArea createBooleanExpCodeAreaObject(ObjectRefsForBuilder objectRefs,
                                                             JTextPane textPane, JScrollPane scrollPane, 
                                                             SymbolicVariableList symbolicVariableList,
                                                             CElectionDescriptionEditor ceditor) {
        
        BooleanExpErrorDisplayer errorDisplayer = new BooleanExpErrorDisplayer(textPane, objectRefs.getStringIF());
        CodeArea tempCodeArea = super.createCodeArea(textPane, scrollPane, objectRefs, errorDisplayer);
        BooleanExpANTLRHandler antlrHandler = new BooleanExpANTLRHandler(textPane.getStyledDocument());
        BooleanExpCodeArea created =  new BooleanExpCodeArea(tempCodeArea, antlrHandler,
                new BooleanExpEditorVariableErrorFinder(antlrHandler, symbolicVariableList, ceditor),
                new BooleanExpEditorGrammarErrorFinder(antlrHandler),
                new BooleanExpAutoCompletionSrc());        
        for(AutocompletionOption opt : createAutocompletionOptions()) {
            created.getAutoComplCtrl().add(opt);
        }
        return created;
    }
    
    private ArrayList<AutocompletionOption> createAutocompletionOptions() {
        ArrayList<AutocompletionOption> created = new ArrayList<>();
        created.add(new AutocompletionOption("FOR_ALL_VOTERS", "FOR_ALL_VOTERS() :"));
        created.add(new AutocompletionOption("FOR_ALL_CANDIDATES", "FOR_ALL_CANDIDATES() :"));
        created.add(new AutocompletionOption("FOR_ALL_SEATS", "FOR_ALL_SEATS() :"));       
        created.add(new AutocompletionOption("EXISTS_ONE_VOTER", "EXISTS_ONE_VOTER() :"));        
        created.add(new AutocompletionOption("EXISTS_ONE_CANDIDATE", "EXISTS_ONE_CANDIDATE() :"));        
        created.add(new AutocompletionOption("EXISTS_ONE_SEAT", "EXISTS_ONE_SEAT() :"));     
        created.add(new AutocompletionOption("VOTES", "VOTES"));     
        created.add(new AutocompletionOption("ELECT", "ELECT"));     
        created.add(new AutocompletionOption("==>", "==>"));     
        created.add(new AutocompletionOption("<==>", "<==>"));     
        return created;
    }
}