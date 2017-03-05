/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.pse.beast.propertychecker;

import edu.pse.beast.datatypes.electiondescription.ElectionType;
import edu.pse.beast.highlevel.ResultInterface;
import edu.pse.beast.highlevel.ResultPresenterElement;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Niels & Lukas
 */
public abstract class Result implements ResultInterface {

    private boolean valid = false;
    private boolean finished = false;
    private List<String> result;
    private List<String> error;
    private boolean timeOut = false;
    private boolean success = false;
    private int numVoters;
    private int numSeats;
    private int numCandidates;
    private ElectionType electionType;
    private boolean forcefulleStopped;

    /**
     * Presents the result of the check. Every class that extends this class has
     * to implement it for itself.
     * 
     * @param presenter
     *            the presentable where it is supposed to be presented on
     */
    public abstract void presentTo(ResultPresenterElement presenter);

    /**
     * 
     * @return if the result is in a state that it can be presented
     */
    @Override
    public boolean readyToPresent() {
        return finished;
    }

    /**
     * 
     * @return if the result is valid (the checking finished normally and wasn't
     *         stopped by something else)
     */
    public boolean isValid() {
        return valid;
    }

    /**
     * 
     * @return if the result is ready yet
     */
    public boolean isFinished() {
        return finished;
    }

    /**
     * 
     * @return the result List
     */
    public List<String> getResult() {
        return result;
    }

    /**
     * 
     * @return the error list
     */
    public List<String> getError() {
        return error;
    }

    /**
     * 
     * @return if the result didn't finish because of a timeout
     */
    public boolean isTimedOut() {
        return timeOut;
    }

    /**
     * 
     * @return if the checking was successfull
     */
    public boolean isSuccess() {
        return success;
    }
    
    /**
     * 
     * @return true, if the process was stopped by the user or a timeout, else false
     */
    public boolean isFocefullyStopped() {
        return forcefulleStopped;
    }

    /**
     * to be set when the checking for this result is completed
     */
    public void setFinished() {
        finished = true;
    }

    /**
     * to be set when the checking was completed and there were no errors during
     * the checking
     */
    public void setValid() {
        valid = true;
    }

    /**
     * 
     */
    public void setSuccess() {
        success = true;
    }

    /**
     * sets the flag that shows that a timeout was responsible for the stopped
     * checking
     */
    public void setTimeoutFlag() {
        timeOut = true;
    }

    /**
     * sets the result of this object, so it can be displayed later.
     * 
     * @param result
     *            the result of the check that should be stored in this result
     *            object
     */
    public void setResult(List<String> result) {
        this.result = result;
    }

    /**
     * sets the error of this object, so it can be displayed later.
     * 
     * @param error
     *            the error of the check that should be stored in this result
     *            object
     */
    public void setError(List<String> error) {
        this.error = error;
    }

    /**
     * sets a single line as the error output
     * 
     * @param errorLine
     *            the error to log
     */
    public void setError(String errorLine) {
        this.error = new ArrayList<String>();
        this.error.add(errorLine);
    }

    /**
     * 
     * @return the amount of votes for which the verification failed
     */
    public int getNumVoters() {
        return numVoters;
    }

    /**
     * 
     * @return the amount of seats for which the verification failed
     */
    public int getNumSeats() {
        return numSeats;
    }

    /**
     * 
     * @return the amount of candidates for which the verification failed
     */
    public int getNumCandidates() {
        return numCandidates;
    }

    /**
     * 
     * @return the election type
     */
    public ElectionType getElectionType() {
        return electionType;
    }

    /**
     * 
     * @param numVoters
     *            the amount of voters to be set
     */
    public void setNumVoters(int numVoters) {
        this.numVoters = numVoters;
    }

    /**
     * 
     * @param numSeats
     *            the amount of seats to be set
     */
    public void setNumSeats(int numSeats) {
        this.numSeats = numSeats;
    }

    /**
     * 
     * @param numCandidates
     *            the amount of candidates to be set
     */
    public void setNumCandidates(int numCandidates) {
        this.numCandidates = numCandidates;
    }

    /**
     * 
     * @param electionType
     *            the electiontype to be set
     */
    public void setElectionType(ElectionType electionType) {
        this.electionType = electionType;
    }

    /**
     * sets the result to forcefully stop, to indicate
     * that it was stopped by the user or a timeout
     */
    public void setForcefullyStopped() {
        this.forcefulleStopped = true;
    }
}
