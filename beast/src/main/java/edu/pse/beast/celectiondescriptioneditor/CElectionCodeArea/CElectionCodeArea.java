/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.pse.beast.celectiondescriptioneditor.CElectionCodeArea;

import edu.pse.beast.celectiondescriptioneditor.CElectionCodeArea.Antlr.CAntlrHandler;
import edu.pse.beast.celectiondescriptioneditor.CElectionCodeArea.ErrorHandling.CGrammarErrorFinder;
import edu.pse.beast.celectiondescriptioneditor.CElectionCodeArea.ErrorHandling.CVariableErrorFinder;
import edu.pse.beast.codearea.CodeArea;

import javax.swing.text.BadLocationException;
import java.util.List;

/**
 * This is the class which inherits from codearea to create a codearea more
 * suited towards writing c code.
 *
 * @author Holger-Desktop
 */
public class CElectionCodeArea extends CodeArea {

    private final CAntlrHandler antlrHandler;
    private final CGrammarErrorFinder grammerErrorFinder;
    private final CVariableErrorFinder varErrFinder;
    private final CSyntaxHl cSyntaxHl;

    public CElectionCodeArea(CodeArea codeArea) {
        super(codeArea);
        antlrHandler = new CAntlrHandler(pane);
        grammerErrorFinder = new CGrammarErrorFinder(antlrHandler);
        varErrFinder = new CVariableErrorFinder(pane);
        //errorCtrl.addErrorFinder(grammerErrorFinder);
        errorCtrl.addErrorFinder(varErrFinder);
        cSyntaxHl = new CSyntaxHl(antlrHandler, syntaxHL);
    }

    /**
     * displays the given code to the user in the JTextPane
     *
     * @param code the code to be displayed
     * @throws BadLocationException
     */
    public void letUserEditCode(List<String> code) throws BadLocationException {
        String s = "";
        for (int i = 0; i < code.size(); ++i) {
            s += code.get(i);
            if (i != code.size() - 1) {
                s += "\n";
            }
        }

        pane.setText(s);
        actionList.clear();
    }
}
