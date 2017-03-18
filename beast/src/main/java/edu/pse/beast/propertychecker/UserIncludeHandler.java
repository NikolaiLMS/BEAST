/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.pse.beast.propertychecker;

import edu.pse.beast.toolbox.ErrorForUserDisplayer;
import edu.pse.beast.toolbox.FileLoader;
import edu.pse.beast.toolbox.SuperFolderFinder;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author Niels
 */
public class UserIncludeHandler {

    private static final String[] STANDARDINCLUDES = new String[]{
        "#include <stdlib.h>",
        "#include <stdint.h>",
        "#include <assert.h>"
    };

    private ArrayList<String> code;

    public UserIncludeHandler() {
        code = new ArrayList<>();
        File file = new File(SuperFolderFinder.getSuperFolder() + "/core/includefile/userinclude.txt");

        try {
            code.addAll(FileLoader.loadFileAsString(file));
            checkIncludeCode(file);
        } catch (IOException ex) {
            String message = "User include file not found.\n"
                    + "It should be located here: " + file.getAbsolutePath() + "\n"
                    + "standard includes are loaded";
            ErrorForUserDisplayer.displayError(message);
            loadStandardIncludes();
        }
    }

    public ArrayList<String> getIncludes() {
        return code;
    }

    private void checkIncludeCode(File file) {
        boolean includeCodeCorrect = true;
        for (String includeLine : code) {
            // test for errors in every line.
            // if one line is faulty set includeCodeCorrect to false

            if (!includeCodeCorrect) {
                String message = "User include file contains errors.\n"
                        + "It is located here: " + file.getAbsolutePath() + "\n"
                        + "standard includes are loaded";
                ErrorForUserDisplayer.displayError(message);
                loadStandardIncludes();
                break;
            }
            //check right format for every line
        }
    }

    private void loadStandardIncludes() {
        code.addAll(Arrays.asList(STANDARDINCLUDES));
    }

}
