/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.pse.beast.celectiondescriptioneditor.UserActions;

import edu.pse.beast.celectiondescriptioneditor.View.CCodeEditorWindow;

import javax.swing.*;

/**
 * Class for checking whether the loaded CElectionDescription object has been modified since it was loaded.
 * @author NikolaiLMS
 */
public class SaveBeforeChangeHandler {
    private String preString = "";
    private JTextPane codeArea;
    private CCodeEditorWindow cCodeEditorWindow;
    private SaveElectionUserAction saveElectionUserAction;

    /**
     * Constructor
     * @param codeArea JTextPane of the CElectionEditorCodeArea
     */
    public SaveBeforeChangeHandler(JTextPane codeArea, CCodeEditorWindow cCodeEditorWindow) {
        this.codeArea = codeArea;
        this.cCodeEditorWindow = cCodeEditorWindow;
        updatePreValue();
    }

    public void addNewTextPane(JTextPane codeArea) {
        this.codeArea = codeArea;
        updatePreValue();
    }

    /**
     * Updates the "pre" variables used for comparison.
     * Called after a new PostAndPrePropertiesDescription object is loaded or saved.
     */
    void updatePreValue() {
        preString = codeArea.getText();
    }

    /**
     * @return true if the currently loaded CElectionDescription object differs from what is currently displayed
     * in CElectionEditorWindow, false otherwise
     */
    public boolean hasChanged() {
        return !(preString.equals((String) codeArea.getText()));
    }

    /**
     * Method that opens a dialog asking the user whether he wants to save his changes, before loading a new
     * PostAndPreProsObject into the editor.
     * @return false if the user pressed "Cancel" on the dialog, thus cancelling any previous load action
     *          true otherwise
     */
    public boolean ifHasChangedOpenDialog(String currentlyLoadedPropName) {
        if (hasChanged()) {
            int option = cCodeEditorWindow.showOptionPane(currentlyLoadedPropName);
            if (option == JOptionPane.YES_OPTION) {
                saveElectionUserAction.perform();
            } else if (option == JOptionPane.CANCEL_OPTION) {
                return false;
            }
        }
        return true;
    }

    /**
     * Setter
     * @param saveElectionUserAction given this class so it can call it when the user wants to save his
     *                            changes upon being shown the dialog from ifHasChangedOpenSaveDialog()
     */
    public void setSaveElectionUserAction(SaveElectionUserAction saveElectionUserAction) {
        this.saveElectionUserAction = saveElectionUserAction;
    }
}
