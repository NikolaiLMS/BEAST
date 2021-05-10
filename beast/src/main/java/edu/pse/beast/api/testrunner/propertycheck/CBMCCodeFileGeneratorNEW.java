package edu.pse.beast.api.testrunner.propertycheck;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

import edu.pse.beast.api.codegen.CBMCCodeGeneratorNEW;
import edu.pse.beast.api.codegen.CBMCGeneratedCodeInfo;
import edu.pse.beast.api.codegen.CodeGenOptions;
import edu.pse.beast.api.codegen.loopbounds.LoopBoundHandler;
import edu.pse.beast.api.electiondescription.CElectionDescription;
import edu.pse.beast.datatypes.electiondescription.ElectionDescription;
import edu.pse.beast.datatypes.propertydescription.PreAndPostConditionsDescription;
import edu.pse.beast.electionsimulator.ElectionSimulationData;
import edu.pse.beast.propertychecker.CBMCCodeGenerator;
import edu.pse.beast.toolbox.ErrorLogger;
import edu.pse.beast.toolbox.FileLoader;
import edu.pse.beast.toolbox.FileSaver;
import edu.pse.beast.toolbox.SuperFolderFinder;

public class CBMCCodeFileGeneratorNEW {

	/** The Constant PATH_TO_TEMP_FOLDER. */
	private static final String PATH_TO_TEMP_FOLDER = "/core/generated_c_files/";

	private static final String CANNOT_FIND_PARENT = "Cannot find a parent to your file!";

	public static CBMCCodeFileData createCodeFileTest(
			final CElectionDescription descr,
			final PreAndPostConditionsDescription propDescr,
			CodeGenOptions options, LoopBoundHandler loopBoundHandler)
			throws IOException {

		CBMCGeneratedCodeInfo code = CBMCCodeGeneratorNEW.generateCode(descr,
				propDescr, options, loopBoundHandler);

		final String absolutePath = SuperFolderFinder.getSuperFolder()
				+ PATH_TO_TEMP_FOLDER;
		final File file = new File(new File(absolutePath),
				FileLoader.getNewUniqueName(absolutePath)
						+ FileLoader.C_FILE_ENDING);

		if (file.getParentFile() == null) {
			ErrorLogger.log(CANNOT_FIND_PARENT);
		} else if (!file.getParentFile().exists()) {
			file.getParentFile().mkdirs();
		}
		FileUtils.writeStringToFile(file, code.getCode(),
				Charset.defaultCharset());;
		CBMCCodeFileData codeFile = new CBMCCodeFileData();
		codeFile.setCodeInfo(code);
		codeFile.setFile(file);
		return codeFile;
	}
}