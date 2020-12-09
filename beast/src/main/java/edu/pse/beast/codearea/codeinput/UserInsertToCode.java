package edu.pse.beast.codearea.codeinput;

//import java.util.ArrayList;
//import java.util.Iterator;
//import java.util.List;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//
//import javax.swing.JTextPane;
//import javax.swing.event.CaretEvent;
//import javax.swing.event.CaretListener;
//import javax.swing.text.BadLocationException;
//import javax.swing.text.StyledDocument;
//
//import edu.pse.beast.codearea.SaveTextBeforeRemove;
//import edu.pse.beast.codearea.StoppedTypingContinuouslyListener;
//import edu.pse.beast.codearea.StoppedTypingContinuouslyMessager;
//import edu.pse.beast.codearea.codeinput.lineinserter.NewlineInserter;
//import edu.pse.beast.codearea.codeinput.lineinserter.NewlineInserterChooser;
//
///**
// * This class is responsible for translating input from the user into code
// * visible in the given JTextPane. It is used by the class CodeInputHandler.
// * One of its main functions is to make sure locked lines dont get changed.
// * It also uses several other classes to provide functionallity such as
// * closing brackets and insert newlines.
// *
// * @author Holger Klein
// */
//public class UserInsertToCode implements CaretListener, StoppedTypingContinuouslyListener {
//    private JTextPane pane;
//    private StyledDocument styledDoc;
//    private OpenCloseCharList openCloseCharList;
//    private LockedLinesHandler lockedLines;
//    private NewlineInserterChooser newlineInserterChooser;
//    private NewlineInserter currentInserter;
//    private int currentCaretPosition;
//    private TabInserter tabInserter;
//    private LineBeginningTabsHandler lineBeginningTabsHandler;
//    private SaveTextBeforeRemove saveBeforeRemove;
//    private StoppedTypingContinuouslyMessager stoppedTypingContMsger;
//    private boolean dontTypeClosingChar;
//    private LockedLinesDisplay lockedLinesDisplay;
//
//    public UserInsertToCode(JTextPane pane, OpenCloseCharList openCloseCharList,
//            SaveTextBeforeRemove saveBeforeRemove) {
//        this.pane = pane;
//        this.pane.addCaretListener(this);
//        this.styledDoc = pane.getStyledDocument();
//        this.openCloseCharList = openCloseCharList;
//        this.saveBeforeRemove = saveBeforeRemove;
//        setupObjects();
//    }
//
//    private void setupObjects() {
//        stoppedTypingContMsger = new StoppedTypingContinuouslyMessager(pane);
//        stoppedTypingContMsger.addListener(this);
//        this.tabInserter = new TabInserter(this.pane);
//        this.lockedLines = new LockedLinesHandler(this.pane, saveBeforeRemove);
//        this.lineBeginningTabsHandler = new CurlyBracesLineBeginningTabHandler(pane);
//        this.newlineInserterChooser = new NewlineInserterChooser(pane, lockedLines);
//        this.currentInserter = this.newlineInserterChooser.getNewlineInserter();
//        this.lockedLinesDisplay = new LockedLinesDisplay(pane, lockedLines);
//    }
//
//    public SaveTextBeforeRemove getSaveBeforeRemove() {
//        return saveBeforeRemove;
//    }
//
//    /**
//     * Inserts a line break at the current caret position. Depending on the
//     * position, this can have different effects. Between two curly braces,
//     * it will add two line breaks:
//     * {|} <-- before, the | represents the caret
//     * {
//     *     | <---after
//     * }
//     *
//     * @throws BadLocationException should never happen since the line break
//     *                              gets added at the caret position which
//     *                              should be valid
//     */
//    public void insertNewline() throws BadLocationException {
//        currentInserter.insertNewlineAtCurrentPosition(pane, tabInserter,
//                                                       lineBeginningTabsHandler,
//                                                       currentCaretPosition);
//        newlineInserterChooser.getNewlineInserter();
//    }
//
//    /**
//     * Inserts the given string at the current caret position unless it is in
//     * a locked line.
//     *
//     * @param string the string to be inserted
//     */
//    public void insertString(String string) {
//        if(selectionIncludesLockedLines()) return;
//        try {
//            if (isLineContainingCaretPosLocked()) {
//                return;
//            } else {
//                if (pane.getSelectedText() == null) {
//                    pane.getStyledDocument().insertString(pane.getCaretPosition(), string, null);
//                } else {
//                    int selectionStart = pane.getSelectionStart();
//                    int selectionEnd = pane.getSelectionEnd();
//                    try {
//                        pane.getStyledDocument().remove(selectionStart,
//                                                        selectionEnd
//                                                        - selectionStart);
//                        pane.getStyledDocument().insertString(selectionStart, string, null);
//                    } catch (BadLocationException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        } catch (BadLocationException e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * Removes a tab at the current caret position of the line isnt locked and
//     * there are only spaces left of the caret position in the line.
//     *
//     * @throws BadLocationException
//     */
//    public void removeTab() throws BadLocationException {
//        if(selectionIncludesLockedLines()) return;
//        if(isLineContainingCaretPosLocked()) return;
//        if(pane.getSelectedText() != null) {
//            ArrayList<Integer> selectedLines =
//                JTextPaneToolbox.getLinesBetween(pane, pane.getSelectionStart(),
//                        pane.getSelectionEnd());
//            for(int line : selectedLines) {
//                int firstCharPos = JTextPaneToolbox.getFirstCharPosInLine(pane, line);
//                saveBeforeRemove.save();
//                tabInserter.removeTabAtPos(firstCharPos);
//            }
//        } else {
//            tabInserter.removeTabAtPos(currentCaretPosition);
//        }
//    }
//
//    /**
//     * Inserts a tab at the current position of the line is not locked.
//     *
//     * @throws BadLocationException
//     */
//    public void insertTab() throws BadLocationException {
//        if(selectionIncludesLockedLines()) return;
//        if(isLineContainingCaretPosLocked()) return;
//        if(pane.getSelectedText() != null) {
//            ArrayList<Integer> selectedLines =
//                JTextPaneToolbox.getLinesBetween(pane, pane.getSelectionStart(),
//                        pane.getSelectionEnd());
//            for(int line : selectedLines) {
//                tabInserter.insertTabAtPos(JTextPaneToolbox.getLineBeginning(pane, line));
//            }
//        } else {
//            tabInserter.insertTabAtPos(currentCaretPosition);
//        }
//    }
//
//
//    private boolean isLineContainingCaretPosLocked() {
//        return lockedLines.isLineLocked(
//            JTextPaneToolbox.transformToLineNumber(pane, pane.getCaretPosition()));
//    }
//
//    /**
//     * Inserts the given char at the current caret position if the line is not locked.
//     * if the char has a corresponding closing char, such as { having }, the
//     * corresponding closing char is also inserted.
//     *
//     * @param insertChar the char to be inserted
//     * @throws BadLocationException
//     */
//    public void insertChar(char insertChar) throws BadLocationException {
//        if(selectionIncludesLockedLines()) return;
//        if(isLineContainingCaretPosLocked()) return;
//        if(pane.getSelectedText() != null) {
//            pane.getStyledDocument().remove(
//                    pane.getSelectionStart(),
//                    pane.getSelectionEnd() - pane.getSelectionStart());
//        }
//        if(openCloseCharList.isOpenChar(insertChar)) {
//            OpenCloseChar occ = openCloseCharList.getOpenCloseChar(insertChar);
//            dontTypeClosingChar = true;
//            occ.insertIntoDocument(pane, currentCaretPosition);
//            dontTypeClosingChar = true;
//        } else if(dontTypeClosingChar && openCloseCharList.isCloseChar(insertChar)) {
//            dontTypeClosingChar = false;
//            pane.setCaretPosition(currentCaretPosition + 1);
//            return;
//        } else {
//            styledDoc.insertString(currentCaretPosition, Character.toString(insertChar), null);
//        }
//    }
//
//    /**
//     * Lock the supplied line from editing.
//     *
//     * @param line the line to be locked, starting at 0
//     */
//    public void lockLine(int line) {
//        lockedLines.lockLine(line);
//        this.currentInserter = this.newlineInserterChooser.getNewlineInserter();
//    }
//
//    /**
//     * Unlocks the supplied line from editing.
//     *
//     * @param line the line to be unlocked, starting at 0
//     */
//    public void unlockLine(int line) {
//      lockedLines.unlockLine(line);
//      this.currentInserter = this.newlineInserterChooser.getNewlineInserter();
//    }
//
//    /**
//     * Returns the first locked lines number. If no line is locked, an
//     * exception is thrown.
//     *
//     * @return The first line which is locked
//     */
//    public int getFirstLockedLine() {
//        return lockedLines.getFirstLockedLine();
//    }
//
//    @Override
//    public void caretUpdate(CaretEvent ce) {
//        currentCaretPosition = ce.getDot();
//        currentInserter = newlineInserterChooser.getNewlineInserter();
//    }
//
//    /**
//     * Changes the current caret position so it is at the end of the line it
//     * is currently one.
//     */
//    public void moveToEndOfCurrentLine() {
//        int end = JTextPaneToolbox.getClosestLineBeginningAfter(pane, currentCaretPosition);
//        pane.setCaretPosition(end);
//    }
//
//     /**
//      * Changes the current caret position so it is at the beginning of the
//     *  line it is currently one.
//     */
//    public void moveToStartOfCurrentLine() {
//        int dist = JTextPaneToolbox.getDistanceToClosestLineBeginning(pane, currentCaretPosition);
//        pane.setCaretPosition(pane.getCaretPosition() - dist);
//    }
//
//    /**
//     * Removes one char to the right of the current caret position if it does not
//     * change a locked line. If more text is selected, all the selected text gets
//     * removed instead.
//     */
//    public void removeToTheRight() {
//        if (selectionIncludesLockedLines()) return;
//        if (!isCaretInEmptyLine() && isCaretAtEndOfLine()) {
//            if (isTheFollowingLineLocked()) {
//                return;
//            }
//        }
//        if (!isCaretAtEndOfLine() && isLineContainingCaretPosLocked()) {
//            return;
//        }
//
//        try {
//            if (isLineContainingCaretPosLocked() && !isFollowingLineNotEmpty()) {
//                return;
//            }
//
//            if(pane.getSelectedText() != null) {
//                pane.getStyledDocument().remove(
//                        pane.getSelectionStart(),
//                        pane.getSelectionEnd() - pane.getSelectionStart());
//                return;
//            }
//            pane.getStyledDocument().remove(currentCaretPosition, 1);
//        } catch (BadLocationException ex) {
//        }
//    }
//
//    private boolean isFollowingLineNotEmpty() throws BadLocationException {
//        String codeFollowingCaret = styledDoc.getText(pane.getCaretPosition(), 2);
//        return codeFollowingCaret.equals("\n\n");
//    }
//
//    private boolean isCaretAtEndOfLine() {
//        return JTextPaneToolbox.getCharToTheRightOfCaret(pane).equals("\n");
//    }
//
//    private boolean isCaretInEmptyLine() {
//        return isCaretAtEndOfLine()
//               && JTextPaneToolbox.getCharToTheLeftOfCaret(pane).equals("\n");
//    }
//
//    private boolean isTheFollowingLineLocked() {
//        return lockedLines.isLineLocked(
//                    JTextPaneToolbox.transformToLineNumber(pane,
//                                                           currentCaretPosition)
//                    + 1);
//    }
//
//    /**
//     * Removes one char to the left of the current caret position if it does not
//     * change a locked line. If more text is selected, all the selecte text gets
//     * removed instead.
//     */
//    public void removeToTheLeft() {
//        if(selectionIncludesLockedLines()) return;
//        try {
//            if(isLineContainingCaretPosLocked()) {
//                if(!pane.getStyledDocument().getText(currentCaretPosition - 2, 2).equals("\n\n"))
//                    return;
//            }
//            try {
//                if(caretPosIsOnLineBeginning() &&
//                        pane.getSelectedText() == null &&
//                        lineAboveIsLocked() &&
//                        !caretPosIsOnEmptyLine())
//                    return;
//            } catch(BadLocationException ex) {
//
//            }
//            if(currentCaretPosition == 0 &&
//                    pane.getSelectedText() == null) return;
//            if(pane.getSelectedText() != null) {
//                pane.getStyledDocument().remove(
//                        pane.getSelectionStart(),
//                        pane.getSelectionEnd() - pane.getSelectionStart());
//                return;
//            }
//            pane.getStyledDocument().remove(currentCaretPosition - 1, 1);
//        } catch (BadLocationException ex) {
//        }
//    }
//
//    private boolean caretPosIsOnLineBeginning() throws BadLocationException {
//        return styledDoc.getText(pane.getCaretPosition() - 1, 1).equals("\n");
//    }
//
//    private boolean caretPosIsOnEmptyLine() throws BadLocationException {
//        return styledDoc.getText(pane.getCaretPosition() - 1, 2).equals("\n\n");
//    }
//
//    private boolean lineAboveIsLocked() throws BadLocationException {
//        return pane.getStyledDocument().getText(pane.getCaretPosition() - 1, 1).equals("\n") &&
//                lockedLines.isLineLocked(
//                        JTextPaneToolbox.getLinesBetween(
//                                pane, pane.getCaretPosition(),
//                                pane.getCaretPosition()).get(0) - 1);
//    }
//
//    private boolean selectionIncludesLockedLines() {
//        if(pane.getSelectedText() == null) return false;
//        try {
//            ArrayList<Integer> lines =
//                JTextPaneToolbox.getLinesBetween(pane, pane.getSelectionStart(),
//                                                 pane.getSelectionEnd());
//            for (int i : lines) {
//                if(lockedLines.isLineLocked(i)) return true;
//            }
//            return false;
//        } catch (BadLocationException ex) {
//            Logger.getLogger(UserInsertToCode.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return false;
//    }
//
//    @Override
//    public void StoppedTypingContinuously(int newPos) {
//        dontTypeClosingChar = false;
//    }
//
//    public OpenCloseCharList getOccList() {
//        return this.openCloseCharList;
//    }
//
//    public TabInserter getTabInserter() {
//        return tabInserter;
//    }
//
//    /**
//     * Updates the whole pane to the newest description.
//     *
//     * @param code the code that will be shown in the editor
//     * @throws BadLocationException
//     */
//    public void updateVotingDeclLine(List<String> code) throws BadLocationException {
//        pane.setCaretPosition(0);
//        List<Integer> linesToRelock = lockedLines.getLockedLines();
//        lockedLines.unlockAll();
//        styledDoc.remove(0, styledDoc.getLength()); // clear the whole document
//        int offset = 0;
//
//        for (Iterator<String> iterator = code.iterator(); iterator.hasNext();) {
//            String string = (String) iterator.next();
//            styledDoc.insertString(offset, string + "\n", null);
//            offset = offset + string.length();
//        }
//        for (Iterator<Integer> iterator = linesToRelock.iterator(); iterator.hasNext();) {
//            Integer integer = (Integer) iterator.next();
//            lockedLines.lockLine(integer);
//        }
//    }
//}