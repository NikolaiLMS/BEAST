package edu.pse.beast.api.testrunner.propertycheck;

import java.io.File;

import edu.pse.beast.api.CBMCTestCallback;
import edu.pse.beast.api.codegen.CodeGenOptions;
import edu.pse.beast.api.codegen.loopbounds.LoopBoundHandler;
import edu.pse.beast.api.electiondescription.CElectionDescription;
import edu.pse.beast.datatypes.electiondescription.ElectionDescription;
import edu.pse.beast.datatypes.propertydescription.PreAndPostConditionsDescription;

public interface CBMCProcessStarter {
	public ProcessBuilder startCheckForParam(
			String sessionUUID,
			CElectionDescription descr, 
			PreAndPostConditionsDescription propertyDescr,
			int V, int C, int S, 
			String uuid,
			CBMCTestCallback cb,
			File cbmcFile,
			LoopBoundHandler loopBoundHandler,
			CodeGenOptions codeGenOptions);
}
