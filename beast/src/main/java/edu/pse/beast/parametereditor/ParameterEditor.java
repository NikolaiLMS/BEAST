/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.pse.beast.parametereditor;
import edu.pse.beast.celectiondescriptioneditor.CElectionDescriptionEditor;
import edu.pse.beast.propertylist.PropertyList;
import edu.pse.beast.datatypes.ElectionCheckParameter;
import edu.pse.beast.datatypes.Project;
import edu.pse.beast.highlevel.ParameterSource;
import edu.pse.beast.highlevel.CheckListener;
import edu.pse.beast.highlevel.MainNotifier;
import edu.pse.beast.highlevel.ProjectSource;
import java.util.ArrayList;
import java.awt.event.ActionListener;

/**
 *
 * @author Jonas
 */
public class ParameterEditor implements ParameterSource, MainNotifier, ProjectSource {
    private final ParameterEditorWindowStarter windowStarter;
    private final CElectionDescriptionEditor cElectionDescriptionEditor; 
    private final PropertyList propertyList;
    private final ArrayList<CheckListener> listener = new ArrayList<>();
    private final ArrayList<ActionListener> closeListener = new ArrayList<>();
    private final ArrayList<ActionListener> saveListener = new ArrayList<>();
    //private MinMaxSpinValueHandler voterHandler;
    //private MinMaxSpinValueHandler candHandler;
    //private MinMaxSpinValueHandler seatHandler;
    //private TimeoutValueHandler timeoutHandler;
    //private SingleValueSpinnerHandler processHandler;
    //private UserArgumentHandler argumentHandler;
    //private CheckListener checkListener;
    public ParameterEditor(
            CElectionDescriptionEditor cElectionDescriptionEditor, 
            PropertyList propertyList) {
        this.cElectionDescriptionEditor = cElectionDescriptionEditor;
        this.propertyList = propertyList;
        windowStarter = new ParameterEditorWindowStarter();
        windowStarter.start();
    }
    
    /*
    * implements MainNotifier
    @Override
    public void addCheckListener(CheckListener l) {
        this.checkListener = l;
    }
    */
    @Override
    public ElectionCheckParameter getParameter() {
        //Integer[] voter = voterHandler.getValues();
        //...
        return null;
    }
    /*
    public startCheck() {
        
    }
    */
    /*
    public abortCheck() {
        
    }
    */

    @Override
    public void addCheckListener(CheckListener l) {
        listener.add(l);
    }
    @Override
    public void addCloseAllListener(ActionListener l) {
        closeListener.add(l);
    }

    @Override
    public boolean isCorrect() {
        return true;
    }

    @Override
    public void stopReacting() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void resumeReacting() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Project loadProject() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
