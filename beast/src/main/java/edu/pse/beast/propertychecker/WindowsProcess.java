package edu.pse.beast.propertychecker;

import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.WinNT;
import edu.pse.beast.propertychecker.jna.Win32Process;
import edu.pse.beast.toolbox.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.apache.commons.lang3.StringUtils;

public class WindowsProcess extends CBMCProcess {
    private long WAITINGTIMEFORTERMINATION = 8000;

    private final String relativePathToCBMC32 = "/windows/cbmcWIN/cbmc.exe";
    private final String relativePathToCBMC64 = "/windows/cbmcWIN/cbmc64.exe";

    private final String enableUserInclude = "-I";
    private final String userIncludeFolder = "/core/user_includes/";

    // we want to compile all available c files, so the user doesn't have to
    // specify anything
    private final String cFileEnder = ".c";

    /**
     * creates a new CBMC Checker for the windows OS
     * 
     * @param voters
     *            the amount of voters
     * @param candidates
     *            the amount of candidates
     * @param seats
     *            the amount of seats
     * @param advanced
     *            the string that represents the advanced options
     * @param toCheck
     *            the file to check with cbmc
     * @param parent
     *            the parent CheckerFactory, that has to be notified about
     *            finished checking
     */
    public WindowsProcess(int voters, int candidates, int seats, String advanced, File toCheck,
            CheckerFactory parent) {
        super(voters, candidates, seats, advanced, toCheck, parent);
    }

    @Override
    protected Process createProcess(File toCheck, int voters, int candidates, int seats, String advanced) {

        String userCommands = String.join(" ", advanced.split(";"));

        // trace is mandatory under windows, or the counter example can't get
        // generated
        userCommands = userCommands + " --trace";

        // set the values for the voters, candidates and seats
        String arguments = userCommands + " -D V=" + voters + " -D C=" + candidates + " -D S=" + seats;

        // enable the usage of includes in cbmc
        String userIncludeAndPath = "\"" + enableUserInclude + SuperFolderFinder.getSuperFolder()
                + userIncludeFolder + "\"";

        //get all Files from the form "*.c" so we can include them into cbmc,
        List<String> allFiles = FileLoader.listAllFilesFromFolder("\"" + SuperFolderFinder.getSuperFolder() + userIncludeFolder +"\"", cFileEnder);
        
        //we have to give all available "*c" files to cbmc, in case the user used his own includes, so we combine them here
        String compileAllIncludesInIncludePath = StringUtils.join(allFiles, " ");
        
        String vsCmd = null;
        Process startedProcess = null;

        // try to get the vsCMD
        try {
            vsCmd = WindowsOStoolbox.getVScmdPath();
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        if (vsCmd == null) {
            ErrorForUserDisplayer.displayError(
                    "The program \"VsDevCmd.bat\" couldn't be found. It is required to run this program, so "
                            + "please supply it with it. \n"
                            + " To do so, download the Visual Studio Community Version, install it (including "
                            + "the C++ pack). \n "
                            + "Then, search for the VsDevCmd.bat in it, and copy and paste it into the foler "
                            + "/windows/ in the BEAST installation folder.");
            return null;
        } else {

            // surround the vsCMD string with quotes, in case it has spaces in
            // it
            vsCmd = "\"" + vsCmd + "\"";

            // determine the os architecture
            boolean is64bit = false;
            if (System.getProperty("os.name").contains("Windows")) {
                // only 64 bit windows contains this variable
                is64bit = (System.getenv("ProgramFiles(x86)") != null);
            } else {
                ErrorLogger.log("The Windows procedure to call cbmc was used, even though this operating "
                        + "system isn't Windows!");
            }

            String cbmcEXE = "";

            // load the system specific cbmc programs
            if (is64bit) {
                cbmcEXE = new File(SuperFolderFinder.getSuperFolder() + relativePathToCBMC64).getPath();
            } else {
                cbmcEXE = new File(SuperFolderFinder.getSuperFolder() + relativePathToCBMC32).getPath();
            }

            if (!new File(cbmcEXE).exists()) {
                ErrorForUserDisplayer.displayError(
                        "Can't find the program \"cbmc.exe\" in the subfolger \"windows/cbmcWin/\". \n "
                                + "Please download it from the cbmc website and place it there!");
            } else if (!new File(cbmcEXE).canExecute()) {
                ErrorForUserDisplayer
                        .displayError("This program doesn't have the privileges to execute this program. \n "
                                + "Please change the access rights for the program \"/windows/cbmcWin/cbmc.exe\" "
                                + "in the BEAST installation folder and try again.");
            } else {

                // surround it with quotes, in case there are spaces in the name
                cbmcEXE = "\"" + cbmcEXE + "\"";

                // because windows is weird the whole call that will get placed
                // inside
                // VScmd has to be in one giant string
                String cbmcCall =  vsCmd + " & " + cbmcEXE + " " + userIncludeAndPath + " " + "\""
                        + toCheck.getAbsolutePath() + "\"" + " " + compileAllIncludesInIncludePath + " " + arguments;

                List<String> callInList = new ArrayList<String>();
                
                callInList.add(cbmcCall);
                
                File batFile = new File(toCheck.getParent() + "\\" + toCheck.getName().replace(".c", ".bat"));
                
                FileSaver.writeStringLinesToFile(callInList, batFile);
                
                // this call starts a new VScmd instance and lets cbmc run in it
               // ProcessBuilder prossBuild = new ProcessBuilder("cmd.exe", "/c", cbmcCall);

                ProcessBuilder prossBuild = new ProcessBuilder("cmd.exe", "/c", "\"" + batFile.getAbsolutePath() + "\"");
                
                try {
                    startedProcess = prossBuild.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                
                return startedProcess;
            }
        }
        return null;
    }

    @Override
    protected void stopProcess() {

        if (!process.isAlive()) {
            ErrorLogger.log("Warning, process isn't alive anymore");
            return;
        } else {
        	kill(process.toHandle());
        }
		
		if (process.isAlive()) {
			ErrorLogger.log("Warning, the program was unable to shut down the CBMC Process \n"
					+ "Please kill it manually, especially if it starts taking up a lot of ram");
		}
	}

	/**
	 * 
	 * @return
	 * @throws IOException
	 */
	private String getVScmdPath() throws IOException {
		// TODO: this could be cached, because it takes a significant time on
		// Windows every startup
		Path x86 = new File("C:/Program Files (x86)").toPath();
		Path x64 = new File("C:/Program Files").toPath();
		String searchTerm = "Microsoft Visual Studio";
		String pathToBatch = "/Common7/Tools/VsDevCmd.bat";

		ArrayList<String> toSearch = new ArrayList<>();
		Files.list(x86).filter(Files::isReadable).filter(path -> path.toString().contains(searchTerm))
				.forEach(VSPath -> toSearch.add(VSPath.toString()));
		Files.list(x64).filter(Files::isReadable).filter(path -> path.toString().contains(searchTerm))
				.forEach(VSPath -> toSearch.add(VSPath.toString()));

		for (Iterator<String> iterator = toSearch.iterator(); iterator.hasNext();) {
			String toCheck = ((String) iterator.next()) + pathToBatch;

			if (Files.isReadable(new File(toCheck).toPath())) {
				return toCheck;
			}
		}

		String userInput = JOptionPane
				.showInputDialog("The progam was unable to find a Developer Command Prompt for Visual Studio. \n"
						+ " Please search for it on your own and paste the path to the batch-file here!");

		// important that the check against null is done first, so invalid
		// inputs are caught without causing an error
		if (userInput != null && Files.isReadable(new File(userInput).toPath()) && userInput.contains("VsDevCmd.bat")) {
			return userInput;
		} else {
			System.err.println("The provided path did not lead to the command prompt. Shutting down now.");
			return null;
		}
	}

	@Override
	protected String sanitizeArguments(String toSanitize) {
		return toSanitize;
	}
	
	public void kill(ProcessHandle handle)
	{
	    handle.descendants().forEach((child) -> kill(child));
	    handle.destroy();
	}
}
