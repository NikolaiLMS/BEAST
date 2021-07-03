package edu.pse.beast.zzz.toolbox;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * The Class ErrorForUserDisplayer.
 *
 * @author Lukas Stapelbroek
 */
public final class ErrorForUserDisplayer {

    /** The Constant THOUSAND. */
    private static final int THOUSAND = 1000;

    /** The currently displayed errors. */
    private static int currentlyDisplayedErrors;

    /**
     * Instantiates a new error for user displayer.
     */
    private ErrorForUserDisplayer() { }

    /**
     * Displays the message on the users screen. if multiple messages are
     * displayed, the method will only return when all messages are read.
     *
     * @param message
     *            the message to be displayed
     */
    public static void displayError(final String message) {
        increment();
        JOptionPane.showMessageDialog(new JFrame(), message, "Error",
                                      JOptionPane.ERROR_MESSAGE);
        decrement();
        while (true) {
            if (currentlyDisplayedErrors == 0) {
                return;
            }
            try {
                Thread.sleep(THOUSAND);
            } catch (InterruptedException e) {
                // Do nothing
            }
        }
    }

    /**
     * Increment.
     */
    private static synchronized void increment() {
        currentlyDisplayedErrors++;
    }

    /**
     * Decrement.
     */
    private static synchronized void decrement() {
        currentlyDisplayedErrors--;
    }
}