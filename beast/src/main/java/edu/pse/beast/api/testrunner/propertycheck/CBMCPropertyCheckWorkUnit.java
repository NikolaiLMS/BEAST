package edu.pse.beast.api.testrunner.propertycheck;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.IOUtils;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import edu.pse.beast.api.CBMCTestCallback;
import edu.pse.beast.api.codegen.CodeGenOptions;
import edu.pse.beast.api.codegen.loopbounds.LoopBoundHandler;
import edu.pse.beast.api.electiondescription.CElectionDescription;
import edu.pse.beast.api.testrunner.propertycheck.processes.process_handler.CBMCProcessHandler;
import edu.pse.beast.api.testrunner.threadpool.WorkUnit;
import edu.pse.beast.api.testrunner.threadpool.WorkUnitState;
import edu.pse.beast.datatypes.electiondescription.ElectionDescription;
import edu.pse.beast.datatypes.propertydescription.PreAndPostConditionsDescription;
import edu.pse.beast.electiontest.cbmb.CBMCCodeFileGenerator;

public class CBMCPropertyCheckWorkUnit implements WorkUnit {
	CElectionDescription descr;
	PreAndPostConditionsDescription propDescr;
	int v, c, s;
	String uuid;
	CBMCProcessHandler processStarter;
	CBMCTestCallback cb;
	private boolean finished = false;
	File cbmcCodeFile;
	private LoopBoundHandler loopBoundHandler;
	private CodeGenOptions codeGenOptions;
	private String sessionUUID;
	private Process process;
	private WorkUnitState state;

	public CBMCPropertyCheckWorkUnit(String sessionUUID,
			CElectionDescription descr,
			PreAndPostConditionsDescription propertyDescr, int v, int c, int s,
			String uuid, CBMCProcessHandler processStarter, File cbmcCodeFile,
			LoopBoundHandler loopBoundHandler, CodeGenOptions codeGenOptions) {
		this.sessionUUID = sessionUUID;
		this.descr = descr;
		this.propDescr = propertyDescr;
		this.v = v;
		this.c = c;
		this.s = s;
		this.uuid = uuid;
		this.processStarter = processStarter;
		this.cbmcCodeFile = cbmcCodeFile;
		this.loopBoundHandler = loopBoundHandler;
		this.codeGenOptions = codeGenOptions;
		this.state = WorkUnitState.CREATED;
	}

	public CBMCPropertyCheckWorkUnit(String uuid,
			CBMCProcessHandler processStarter, String sessionUUID) {
		this.uuid = uuid;
		this.processStarter = processStarter;
		this.sessionUUID = sessionUUID;
	}

	public void initialize(int v, int s, int c, CodeGenOptions codeGenOptions,
			LoopBoundHandler loopBoundHandler, File cbmcCodeFile,
			CElectionDescription descr,
			PreAndPostConditionsDescription propDescr) {
		this.descr = descr;
		this.propDescr = propDescr;
		this.v = v;
		this.c = c;
		this.s = s;
		this.cbmcCodeFile = cbmcCodeFile;
		this.loopBoundHandler = loopBoundHandler;
		this.codeGenOptions = codeGenOptions;
		this.state = WorkUnitState.CREATED;
	}

	public int getC() {
		return c;
	}

	public int getS() {
		return s;
	}

	public int getV() {
		return v;
	}

	public void setCallback(CBMCTestCallback cb) {
		this.cb = cb;
	}

	public boolean hasCallback() {
		return this.cb != null;
	}

	public void updateDataForCheck(File cbmcFile,
			LoopBoundHandler loopBoundHandler) {
		this.cbmcCodeFile = cbmcFile;
		this.loopBoundHandler = loopBoundHandler;
	}

	@Override
	public void doWork() {
		state = WorkUnitState.WORKED_ON;
		cb.onPropertyTestStart(descr, propDescr, s, c, v, uuid);
		try {
			Process p = processStarter.startCheckForParam(sessionUUID, descr,
					propDescr, v, c, s, uuid, cb, cbmcCodeFile,
					loopBoundHandler, codeGenOptions);
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(process.getInputStream()));
			String line;
			List<String> cbmcOutput = new ArrayList<>();
			try {
				while ((line = reader.readLine()) != null) {
					cb.onPropertyTestRawOutput(sessionUUID, descr, propDescr, s,
							c, v, uuid, line);
					cbmcOutput.add(line);
				}
				// TODO errorhandling
			} catch (IOException e) {
				e.printStackTrace();
			}
			cb.onPropertyTestRawOutputComplete(descr, propDescr, s, c, v, uuid,
					cbmcOutput);
			state = WorkUnitState.FINISHED;
			process.destroy();

			cb.onPropertyTestFinished(descr, propDescr, s, c, v, uuid);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public String getUUID() {
		return uuid;
	}

	@Override
	public void interrupt() {
		process.destroyForcibly();
		state = WorkUnitState.STOPPED;
		cb.onPropertyTestStopped(descr, propDescr, s, c, v, uuid);
	}

	@Override
	public void addedToQueue() {
		state = WorkUnitState.ON_QUEUE;
		cb.onPropertyTestAddedToQueue(descr, propDescr, s, c, v, uuid);
	}

	@Override
	public WorkUnitState getState() {
		return state;
	}

	public File getCbmcFile() {
		return cbmcCodeFile;
	}

	public void setCbmcFile(File cbmcFile) {
		this.cbmcCodeFile = cbmcFile;
	}
}
