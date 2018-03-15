package edu.pse.beast.highlevel.javafx;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.fxmisc.flowless.VirtualizedScrollPane;
import org.stringtemplate.v4.compiler.STParser.compoundElement_return;

import edu.pse.beast.codeareaJAVAFX.RichTextFXCodeArea;
import edu.pse.beast.codeareaJAVAFX.NewCodeArea;
import edu.pse.beast.datatypes.electioncheckparameter.ElectionCheckParameter;
import edu.pse.beast.datatypes.electioncheckparameter.TimeOut;
import edu.pse.beast.highlevel.BEASTCommunicator;
import edu.pse.beast.toolbox.SuperFolderFinder;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

public class GUIController {

	private String pathToImages = "file:///" + SuperFolderFinder.getSuperFolder() + "/core/images/";

	private boolean react = true;
	
	
	private List<TabClass> mainWindowTabs = new ArrayList<TabClass>();
	
	private List<TabClass> bottomWindowTabs = new ArrayList<TabClass>();
	
	

	@FXML // fx:id="maxVoter"
	private TextField maxVoter;

	@FXML // fx:id="minVoter"
	private TextField minVoter;

	@FXML // fx:id="maxCandidates"
	private TextField maxCandidates;

	@FXML // fx:id="minCandidates"
	private TextField minCandidates;

	@FXML // fx:id="maxSeats"
	private TextField maxSeats;

	@FXML // fx:id="minSeats"
	private TextField minSeats;

	@FXML // fx:id="timeOut"
	private TextField timeOut;

	@FXML // fx:id="TimeUnitChoice"
	private ChoiceBox<TimeUnit> TimeUnitChoice;

	@FXML // fx:id="processes"
	private TextField processes;

	@FXML // fx:id="solverChoice"
	private ChoiceBox<?> solverChoice;

	@FXML // fx:id="advancedParameters1"
	private TextField advancedParameters;

	@FXML // fx:id="maxUnrolls"
	private TextField maxUnrolls;

	@FXML // fx:id="helpButton"
	private MenuItem helpButton;

	@FXML // fx:id="startStopButton"
	private Button startStopButton;

	@FXML // fx:id="openButton"
	private Button openButton;

	@FXML // fx:id="saveButton"
	private Button saveButton;

	@FXML // fx:id="saveAsButton"
	private Button saveAsButton;

	@FXML // fx:id="undoButton"
	private Button undoButton;

	@FXML // fx:id="redoButton"
	private Button redoButton;

	@FXML // fx:id="cutButton"
	private Button cutButton;

	@FXML // fx:id="copyButton"
	private Button copyButton;

	@FXML // fx:id="pasteButton"
	private Button pasteButton;

	@FXML // fx:id="deleteButton"
	private Button deleteButton;

	@FXML // fx:id="codePane"
	private Tab codePane;

	@FXML // fx:id="propertyPane"
	private Tab propertyPane;

	@FXML // fx:id="resultPane"
	private Tab resultPane;

	@FXML // fx:id="inputPane"
	private Tab inputPane;

	@FXML // fx:id="errorPane"
	private Tab errorPane;

	@FXML // fx:id="consolePane"
	private Tab consolePane;
	
	@FXML
	private ScrollPane codeScrollPane;

	@FXML
	private ScrollPane propertyScrollPane;
	
	@FXML
	private ScrollPane resultScrollPane;
	
	@FXML
	private ScrollPane inputScrollPane;
	
	private boolean running = false;

	// initial setup
	@FXML
	public void initialize() {
		// set images for the buttons
		startStopButton.setGraphic(new ImageView(pathToImages + "toolbar/start.png"));
		openButton.setGraphic(new ImageView(pathToImages + "toolbar/load.png"));
		saveButton.setGraphic(new ImageView(pathToImages + "toolbar/save.png"));
		saveAsButton.setGraphic(new ImageView(pathToImages + "toolbar/save_as.png"));
		undoButton.setGraphic(new ImageView(pathToImages + "toolbar/undo.png"));
		redoButton.setGraphic(new ImageView(pathToImages + "toolbar/redo.png"));
		cutButton.setGraphic(new ImageView(pathToImages + "toolbar/cut.png"));
		copyButton.setGraphic(new ImageView(pathToImages + "toolbar/copy.png"));
		pasteButton.setGraphic(new ImageView(pathToImages + "toolbar/paste.png"));
		deleteButton.setGraphic(new ImageView(pathToImages + "toolbar/x-mark.png"));
		
		//populate boxes
		//add the time units you can choose
		TimeUnitChoice.getItems().add(TimeUnit.SECONDS);
		TimeUnitChoice.getItems().add(TimeUnit.MINUTES);
		TimeUnitChoice.getItems().add(TimeUnit.HOURS);
		TimeUnitChoice.setValue(TimeUnit.SECONDS);
		
		// add listener
		addNumberEnforcer(minVoter, maxVoter, 1);
		addNumberEnforcer(maxVoter, minVoter, -1);

		addNumberEnforcer(minCandidates, maxCandidates, 1);
		addNumberEnforcer(maxCandidates, minCandidates, -1);
	
		addNumberEnforcer(minSeats, maxSeats, 1);
		addNumberEnforcer(maxSeats, minSeats, -1);

		addNumberEnforcer(maxUnrolls);

		addNumberEnforcer(processes);

		addNumberEnforcer(timeOut);

		
		
		NewCodeArea test = new NewCodeArea();
		
		
		VirtualizedScrollPane<NewCodeArea> VSP = new VirtualizedScrollPane<NewCodeArea>(test);
		
		codePane.setContent(VSP);
		
		test.setStyle("-fx-font-family: consolas; -fx-font-size: 11pt;");
		
		//create the tabs which will be used
		//mainWindowTabs.add(new RichTextFXCodeArea());
		
	}

	// Top Panels
	@FXML
	void errorPaneClicked(Event event) {

	}

	@FXML
	void inputPaneClicked(Event event) {

	}

	@FXML
	void propertyPaneClicked(Event event) {

	}

	@FXML
	void resultPaneClicked(Event event) {

	}

	// ------------
	// Bottom Panels
	@FXML
	void codePaneClicked(Event event) {

	}

	@FXML
	void consolePaneClicked(Event event) {

	}

	// --------
	// Icon Bar
	@FXML
	void startStopPressed(ActionEvent event) {
		if (!running) {
			react = false; // lock the GUI
			if (BEASTCommunicator.startCheck()) { // if we start it successfull
				startStopButton.setGraphic(new ImageView(pathToImages + "toolbar/stop.png"));
			} else {
				react = true;
			}
		} else {
			if (BEASTCommunicator.stopCheck()) {
				startStopButton.setGraphic(new ImageView(pathToImages + "toolbar/start.png"));
				react = true;
			}
		}
	}

	@FXML
	void copyButton(ActionEvent event) {

	}

	@FXML
	void undoButton(ActionEvent event) {

	}

	@FXML
	void cutButton(ActionEvent event) {

	}

	@FXML
	void openButton(ActionEvent event) {

	}

	@FXML
	void pasteButton(ActionEvent event) {

	}

	@FXML
	void redoButton(ActionEvent event) {

	}

	@FXML
	void saveAsButton(ActionEvent event) {

	}

	@FXML
	void saveButton(ActionEvent event) {

	}

	@FXML
	void deleteButton(ActionEvent event) {

	}

	// text manipulation menu buttons
	@FXML
	void copy(ActionEvent event) {
		copyButton(event);
	}

	@FXML
	void delete(ActionEvent event) {
		deleteButton(event);
	}

	@FXML
	void cut(ActionEvent event) {
		cutButton(event);
	}

	@FXML
	void paste(ActionEvent event) {
		pasteButton(event);
	}

	@FXML
	void redo(ActionEvent event) {
		redoButton(event);
	}

	@FXML
	void undo(ActionEvent event) {
		undoButton(event);
	}

	// other menu buttons

	@FXML
	void advancedParameters(Event event) {

	}

	@FXML
	void helpClicked(ActionEvent event) {

	}

	@FXML
	void maxCandidates(ActionEvent event) {

	}

	@FXML
	void maxSeats(ActionEvent event) {

	}

	@FXML
	void maxUnrolls(ActionEvent event) {

	}

	@FXML
	void maxVoter(ActionEvent event) {
		System.out.println("test");
		System.out.println(maxVoter.getText());
	}

	@FXML
	void minCandidates(ActionEvent event) {

	}

	@FXML
	void minSeats(ActionEvent event) {

	}

	@FXML
	void minVoter(ActionEvent event) {

	}

	@FXML
	void newElectionDescription(ActionEvent event) {

	}

	@FXML
	void newProject(ActionEvent event) {

	}

	@FXML
	void newPropertyList(ActionEvent event) {

	}

	@FXML
	void newVotingInpup(ActionEvent event) {

	}

	@FXML
	void openElectionDescription(ActionEvent event) {

	}

	@FXML
	void openProject(ActionEvent event) {

	}

	@FXML
	void openPropertyList(ActionEvent event) {

	}

	@FXML
	void openVotingInput(ActionEvent event) {

	}

	@FXML
	void processes(ActionEvent event) {

	}

	@FXML
	void quitProgram(ActionEvent event) {

	}

	@FXML
	void saveAsElectionDescription(ActionEvent event) {

	}

	@FXML
	void saveAsProject(ActionEvent event) {

	}

	@FXML
	void saveAsPropertyList(ActionEvent event) {

	}

	@FXML
	void saveAsVotingInput(ActionEvent event) {

	}

	@FXML
	void saveElectionDescription(ActionEvent event) {

	}

	@FXML
	void saveProject(ActionEvent event) {

	}

	@FXML
	void savePropertyList(ActionEvent event) {

	}

	@FXML
	void saveVotingInput(ActionEvent event) {

	}

	@FXML
	void timeOut(ActionEvent event) {

	}

	private void addNumberEnforcer(TextField field) {
		field.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (!newValue.matches("\\d*")) {
					field.setText(newValue.replaceAll("[^\\d]", ""));
				}
			}
		});
	}
	
	
	/**
	 * 
	 * @param field the field which shall be enforced
	 * @param partnerField the partner field, which is supposed to be not bigger / smaller than the main field
	 * @param sign a sign to show if the field has to be bigger or smaller than the other one. e.g a sign of 1 means field <= partnerField, a sign of (-1) would mean field => partner field
	 */
	private void addNumberEnforcer(TextField field, TextField partnerField, int sign) {
		if (sign == 0) {
			field.setText("");
			partnerField.setText("");
		}
		
		field.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (!newValue.matches("\\d*")) {
					field.setText(newValue.replaceAll("[^\\d]", ""));
				}
				
				if (newValue.equals("")) {
					field.setText("" + 1);
				}
				
				int valueField = Integer.parseInt(field.getText());
				int valuePartner = Integer.parseInt(partnerField.getText());
				
				if (valueField * sign > valuePartner * sign) {
					partnerField.setText("" + valueField);
				}
			}
		});
		
	}

	public void setReact(boolean react) {
		this.react = react;
	}
	
	public ElectionCheckParameter getParameter() {
        List<Integer> voter = getValues(minVoter, maxVoter);
        List<Integer> cand = getValues(minCandidates, maxCandidates);
        List<Integer> seat = getValues(minSeats, maxSeats);
        
        Integer numberProcesses = Runtime.getRuntime().availableProcessors();
        
        if (processes.getText() != "") {
        	numberProcesses = Integer.parseInt(processes.getText());
        }
        
        TimeOut time = new TimeOut(TimeUnit.SECONDS, 0);

        if (timeOut.getText() != "") {
        	time = new TimeOut(TimeUnitChoice.getValue(), Integer.parseInt(timeOut.getText()));
        	numberProcesses = Integer.parseInt(processes.getText());
        }
        
        String argument = advancedParameters.getText();
        
        ElectionCheckParameter param = new ElectionCheckParameter(voter, cand, seat, time, numberProcesses, argument);
        return param;
    }
	
	private List<Integer> getValues(TextField minfield, TextField maxField) {
		List<Integer> toReturn = new ArrayList<Integer>();
		
		int valueMin = Integer.parseInt(minfield.getText());
		int valueMax = Integer.parseInt(maxField.getText());
		
		for (int i = valueMin; i <= valueMax; i++) {
			toReturn.add(i);
		}
		
		return toReturn;
	}
	
}