/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.pse.beast.celectiondescriptioneditor;

import edu.pse.beast.celectiondescriptioneditor.CElectionCodeArea.CElectionCodeArea;
import edu.pse.beast.celectiondescriptioneditor.CElectionCodeArea.CElectionCodeAreaBuilder;
import edu.pse.beast.celectiondescriptioneditor.CElectionCodeArea.ErrorHandling.CErrorDisplayer;
import edu.pse.beast.celectiondescriptioneditor.View.CCodeEditorWindow;
import edu.pse.beast.celectiondescriptioneditor.View.ErrorWindow;
import edu.pse.beast.codearea.ErrorHandling.CodeError;
import edu.pse.beast.datatypes.electiondescription.ElectionDescription;
import edu.pse.beast.datatypes.electiondescription.ElectionDescriptionChangeListener;
import edu.pse.beast.highlevel.DisplaysStringsToUser;
import edu.pse.beast.highlevel.ElectionDescriptionSource;
import edu.pse.beast.saverloader.FileChooser;
import edu.pse.beast.stringresource.StringLoaderInterface;
import edu.pse.beast.toolbox.ObjectRefsForBuilder;
import edu.pse.beast.toolbox.UserAction;

import javax.swing.text.BadLocationException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This class is the interface/fassade class for the CElectionDescriptionEditor
 * interface. It provides access to the most important functionallity of this
 * package to other classes without having to know which particular classes
 * implement said functionality
 *
 * @author Holger Klein
 */
public class CElectionDescriptionEditor implements ElectionDescriptionSource, DisplaysStringsToUser {

    private CElectionCodeArea codeArea;
    private ElectionDescription currentDescription;
    private final CCodeEditorWindow window;
    private final CElectionCodeAreaBuilder builder;
    private final ErrorWindow errorWindow;
    private final CElectionDescriptionEditorChangeHandler changeHandler;
    private final ArrayList<ElectionDescriptionChangeListener> descriptionChangeListeners = new ArrayList<>();
    private CElectionEditorMenubarHandler menubarHandler;
    private CElectionEditorToolbarHandler toolbarHandler;
    private StringLoaderInterface stringLoaderInterface;
    private final FileChooser fileChooser;
    private final ArrayList<UserAction> userActions = new ArrayList<>();
    private final ArrayList<Character> userActionChars = new ArrayList<>();
    private Boolean wasVisible;
    private ObjectRefsForBuilder refs;

    public CElectionDescriptionEditor(
            CElectionCodeArea codeArea,
            CCodeEditorWindow gui,
            CElectionCodeAreaBuilder builder,
            ErrorWindow errorWindow,
            CElectionDescriptionEditorChangeHandler CElectionDescriptionEditorChangeHandler,
            StringLoaderInterface stringLoaderInterface,
            FileChooser fileChooser,
            ObjectRefsForBuilder refs) {
        this.codeArea = codeArea;
        this.window = gui;
        this.builder = builder;
        this.errorWindow = errorWindow;
        this.changeHandler = CElectionDescriptionEditorChangeHandler;
        this.stringLoaderInterface = stringLoaderInterface;
        this.fileChooser = fileChooser;
        this.refs = refs;
    }

    /**
     * returns the electiondescription in the state currently visible to the
     * user
     *
     * @return the electiondesciprion as it is currently edited by the user
     */
    @Override
    public ElectionDescription getElectionDescription() {
        updateCurrentDescription();
        return currentDescription;
    }

    public void addUserAction(char c, UserAction ac) {
        userActions.add(ac);
        userActionChars.add(c);
        codeArea.getUserInputHandler().getShortcutHandler().addAction(c, ac);
    }

    private void updateCurrentDescription() {
        List<String> code;
        currentDescription.setVotingDeclLine(codeArea.getFirstLockedLine());
        String content = codeArea.getPane().getText();
        String lines[] = content.split("\n");
        code = Arrays.asList(lines);
        currentDescription.setCode(code);
    }

    /**
     * finds all errors in the currently displayed c code and displays them
     * listed in the errorwindow
     */
    public void findErrorsAndDisplayThem() {
        ArrayList<CodeError> errors = codeArea.getErrorCtrl().getErrorFinderList().getErrors();
        errorWindow.displayErrors(errors, (CErrorDisplayer) codeArea.getErrorCtrl().getDisplayer());
    }

    public void setcElectionEditorMenubarHandler(CElectionEditorMenubarHandler cElectionEditorMenubarHandler) {
        this.menubarHandler = cElectionEditorMenubarHandler;
    }

    public void setcElectionEditorToolbarHandler(CElectionEditorToolbarHandler cElectionEditorToolbarHandler) {
        this.toolbarHandler = cElectionEditorToolbarHandler;
    }

    @Override
    public boolean isCorrect() {
        if (codeArea.getErrorCtrl().getErrorFinderList().getErrors().isEmpty()) {
            return true;
        } else {
            findErrorsAndDisplayThem();
            window.setVisible(true);
            return false;
        }
    }

    @Override
    public void stopReacting() {
        wasVisible = window.isVisible();
        window.setVisible(false);
    }

    @Override
    public void resumeReacting() {
        if (wasVisible) {
            window.setVisible(true);
        }
    }

    /**
     * Getter
     *
     * @return the CElectionCodeArea
     */
    public CElectionCodeArea getCodeArea() {
        return codeArea;
    }

    /**
     * presents the given electiondescription to the user so he can edit it,
     * prompting him to save the current description before doing so
     *
     * @param description the description to be shown to the user
     * @return true if the desciprion was updates, false otherwise
     * @throws BadLocationException
     */
    public boolean letUserEditElectionDescription(ElectionDescription description) throws BadLocationException {
        if (changeHandler.hasChanged()) {
            if (fileChooser.openSaveChangesDialog(getElectionDescription())) {
                loadElectionDescription(description);
                return true;
            } else {
                return false;
            }
        } else {
            loadElectionDescription(description);
            return true;
        }

    }

    /**
     * creates a new celectiocodearea object in which to display the given
     * electiondescription. Does not ask the user to save the current desciprion
     * before doing sos
     *
     * @param description the description to be displayed in the
     * celectiondesciprioncodearea
     * @throws BadLocationException
     */
    public void loadElectionDescription(ElectionDescription description) throws BadLocationException {
        this.currentDescription = description;
        window.setNewCodeArea();

        codeArea.getErrorCtrl().stopThread();
        codeArea.getAutoComplCtrl().stopThread();

        codeArea = builder.createCElectionCodeArea(window.getCodeArea(),
                window.getCodeAreaScrollPane(),
                new CErrorDisplayer(window.getCodeArea(), stringLoaderInterface));
        for (int i = 0; i < userActions.size(); i++) {
            UserAction get = userActions.get(i);
            char c = userActionChars.get(i);
            codeArea.linkActionToShortcut(c, get);
        }

        codeArea.letUserEditCode(description.getCode());
        codeArea.lockLine(description.getVotingDeclLine());
        codeArea.lockLine(description.getCode().size() - 1);
        changeHandler.addNewTextPane(codeArea.getPane());
        window.setWindowTitle(description.getName());
        for (ElectionDescriptionChangeListener l : descriptionChangeListeners) {
            l.inputChanged(description.getInputType());
            l.outputChanged(description.getOutputType());
        }
        findErrorsAndDisplayThem();
        refs.getOptionIF().getCElectionEditorOptions(this).reapply();
    }

    /**
     * Getter
     *
     * @return the CCodeEditorWindow object of this class
     */
    public CCodeEditorWindow getView() {
        return this.window;
    }

    /**
     * Getter
     *
     * @return the ChangeHandler object of this class
     */
    public CElectionDescriptionEditorChangeHandler getChangeHandler() {
        return this.changeHandler;
    }

    public void addListener(ElectionDescriptionChangeListener l) {
        descriptionChangeListeners.add(l);
    }

    public void removeListener(ElectionDescriptionChangeListener l) {
        descriptionChangeListeners.remove(l);
    }

    public void setVisible(boolean vis) {
        window.setVisible(vis);
    }

    public FileChooser getFileChooser() {
        return fileChooser;
    }

    public StringLoaderInterface getStringInterface() {
        return this.stringLoaderInterface;
    }

    @Override
    public void updateStringRes(StringLoaderInterface stringResIF) {
        this.stringLoaderInterface = stringResIF;
        menubarHandler.updateStringRes(stringLoaderInterface);
        toolbarHandler.updateStringRes(stringLoaderInterface);
        fileChooser.updateStringRessourceLoader(stringLoaderInterface.getCElectionEditorStringResProvider().getMenuStringRes());
        fileChooser.updateStringRessourceLoader(stringLoaderInterface.getCElectionEditorStringResProvider().getMenuStringRes());
    }
}
