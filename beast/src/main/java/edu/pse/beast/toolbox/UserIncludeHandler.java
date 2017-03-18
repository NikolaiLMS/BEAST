/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.pse.beast.toolbox;

import edu.pse.beast.celectiondescriptioneditor.CElectionCodeArea.ErrorHandling.DeepErrorChecker;
import edu.pse.beast.codearea.ErrorHandling.CodeError;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
    private static final String[] CLIBRARIES = new String[]{
        "<assert.h>",
        "<complex.h>",
        "<ctype.h>",
        "<errno.h>",
        "<fenv.h>",
        "<float.h>",
        "<inttypes.h>",
        "<iso646.h>",
        "<limits.h>",
        "<locale.h>",
        "<math.h>",
        "<setjmp.h>",
        "<signal.h>",
        "<stdalign.h>",
        "<stdarg.h>",
        "<stdatomic.h>",
        "<stdbool.h>",
        "<stddef.h>",
        "<stdint.h>",
        "<stdio.h>",
        "<stdlib.h>",
        "<stdnoreturn.h>",
        "<string.h>",
        "<tgmath.h>",
        "<threads.h>",
        "<time.h>",
        "<uchar.h>",
        "<wchar.h>",
        "<wctype.h>"
    };
    private ArrayList<String> code;
    private List<CodeError> errorList;
    private ArrayList<String> errorLines;
    private boolean errorsFound;

    public UserIncludeHandler() {
        errorLines = new ArrayList<>();
        errorList = new ArrayList<>();
        code = new ArrayList<>();
        errorsFound = false;
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
        findErrorLinesAndSubtractThemFromTheCode();
        if (errorsFound) {
            String message = "User include file contains errors.\n"
                    + "It is located here: " + file.getAbsolutePath() + "\n"
                    + "standard includes are loaded";
            message += errorLines.get(0);
            ErrorForUserDisplayer.displayError(message);
            loadStandardIncludes();
        }
    }

    private void loadStandardIncludes() {
        code.clear();
        code.addAll(Arrays.asList(STANDARDINCLUDES));
    }

    private void findErrorLinesAndSubtractThemFromTheCode() {
        ArrayList<String> testCode = new ArrayList<>();
        testCode.addAll(code);
        testCode.add("int main(int argc, char *argv[]) {");
        testCode.add("}");
        
        errorList = DeepErrorChecker.checkCodeForErrors(testCode);
        if (!errorList.isEmpty()) {
            System.out.println(errorList.get(0).getLine());
            errorsFound = true;
            // adds the line that caused the error to the errorLinesArray
            int errorLineNumber = errorList.get(0).getLine() - 1;
            String errorLine = testCode.get(errorLineNumber);
            code.remove(errorLineNumber);
            errorLines.add(errorLine);
            findErrorLinesAndSubtractThemFromTheCode();
        }

    }

}
