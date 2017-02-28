package edu.pse.beast.toolbox;

public final class ErrorForUserDisplayer {

    private static int currentlyDisplayedErrors = 0;

    private ErrorForUserDisplayer() {

    }

    /**
     * displays the message on the users screen. if multiple messages are displayed
     * the methode will only return when all messages are read.
     * @param message the message to be displayed
     */
    public static void displayError(String message) {
        increment();

        ErrorLogger.log("HIER SPÄTER EIN POPUP AUFBAUEN!: " + message);

        decrement();

        while (true) {
            if (currentlyDisplayedErrors == 0) {
                return;
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {

            }
        }
    }

    private static synchronized void increment() {
        currentlyDisplayedErrors++;
    }

    private static synchronized void decrement() {
        currentlyDisplayedErrors--;
    }
}
