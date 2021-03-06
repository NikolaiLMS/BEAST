package edu.pse.beast.toolbox;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * this class is a BufferedReader that runs in a separate thread. All read lines
 * are stored in a passed list. It either stops if the stream ends, or it gets
 * stopped from the outside. When it finishes normally, it also counts down a
 * latch to notify other waiting threads.
 * 
 * @author Lukas
 *
 */
public class ThreadedBufferedReader implements Runnable {

    private final BufferedReader reader;
    private final List<String> readLines;
    private volatile boolean isInterrupted = false;
    private final CountDownLatch latch;
    
    private final boolean checkForUnwind;

    private final int checkingInterval = 5000;
    
    private final int warningInterval = 1000;
    
    private final String unwindPrefix = "Unwinding loop";
    
    /**
     * Class for reading a stream from the program
     * 
     * @param reader
     *            The reader to be read from.
     * @param readLines
     *            The list where the read lines should be added
     * @param latch
     *            the latch to synchronize on
     */
    public ThreadedBufferedReader(BufferedReader reader, List<String> readLines,
                                  CountDownLatch latch, boolean checkForUnwind) {
        this.reader = reader;
        this.readLines = readLines;
        this.latch = latch;
        this.checkForUnwind = checkForUnwind;
        
        new Thread(this, "ReaderThread").start();
    }

    /**
     * Starts the thread. The reader reads each line, adds it to the list. At
     * the end it notifies a latch, that it is finished.
     */
    @Override
    public void run() {
        boolean warningShown = false;

        int curr = 0;

        String line = null;

        try {
            line = reader.readLine();
            while (line != null && !isInterrupted) {
                readLines.add(line);
                if (checkForUnwind && (curr > checkingInterval)) {
                    if (line.startsWith(unwindPrefix)) {
                        //we are still unwinding, so we check the line now
                        //to see, how much we are unwinding
                        try {
                            int iteration = Integer.parseInt(
                                    line.split("iteration")[1].split("file")[0]
                                            .replace(" ", ""));
                            if (iteration > warningInterval) {
                                new Thread() {
                                    public void run() {
                                        ErrorForUserDisplayer.displayError(
                                            "A loop in your c program is still"
                                          + " (more than a thousand times)"
                                          + " getting unrolled. Maybe you want"
                                          + " to stop the checking manually and"
                                          + " add the \"--unwind\" option."
                                          );
                                    }
                                }.start();
                            }
                            //reset curr;
                            curr = 0;
                        } catch (Exception e) {
                            // do nothing
                        }
                    }
                }
                line = reader.readLine();
                curr++;
            }
        } catch (IOException e) {
            ErrorLogger.log("Reader was closed unexpectedly");
        }
        latch.countDown();
    }

    /**
     * interrupts the reader if it is needed
     */
    public void stopReading() {
        isInterrupted = true;
        try {
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
