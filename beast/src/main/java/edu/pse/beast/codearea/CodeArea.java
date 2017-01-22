/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.pse.beast.codearea;

import edu.pse.beast.codearea.Actionlist.Actionlist;
import edu.pse.beast.codearea.Autocompletion.AutocompletionController;
import edu.pse.beast.codearea.ErrorHandling.ErrorController;
import edu.pse.beast.codearea.InputToCode.UserInputHandler;
import edu.pse.beast.codearea.InputToCode.UserInsertToCode;
import edu.pse.beast.codearea.UserActions.CodeAreaUserActions;
import javax.swing.JTextPane;

/**
 *
 * @author Niels
 */
public class CodeArea {
    
    protected JTextPane pane;
    protected TextLineNumber tln;
    protected UserInputHandler userInputHandler;
    protected UserInsertToCode insertToCode;
    protected Actionlist actionList;
    protected ErrorController errorCtrl;
    protected AutocompletionController autoComplCtrl;
    protected CodeAreaUserActions userActionList;
                    
    public CodeArea(
            JTextPane pane,
            TextLineNumber tln, 
            UserInputHandler userInputHandler, 
            UserInsertToCode insertToCode,
            Actionlist actionList,
            ErrorController errorCtrl, 
            AutocompletionController autoComplCtrl) {
        this.tln = tln; 
        this.userInputHandler = userInputHandler;
        this.insertToCode = insertToCode;
        this.actionList = actionList;
        this.errorCtrl = errorCtrl;
        this.autoComplCtrl = autoComplCtrl;
    }

    public CodeArea(CodeArea codeArea) {
        this.pane = codeArea.pane;
        this.tln = codeArea.tln;
        this.userInputHandler = codeArea.userInputHandler;
        this.insertToCode = codeArea.insertToCode;
        this.actionList = codeArea.actionList;
        this.errorCtrl = codeArea.errorCtrl;
        this.autoComplCtrl = codeArea.autoComplCtrl;
        this.userActionList = codeArea.userActionList;
    }
    
    public void setUserActionList(CodeAreaUserActions userActionList) {
        this.userActionList = userActionList;
    }
    
    public CodeAreaUserActions getUserActionList() {
        return userActionList;
    }

    public JTextPane getPane() {
        return this.pane;
    }

    public Actionlist getActionlist() {
        return actionList;
    }
    
}
