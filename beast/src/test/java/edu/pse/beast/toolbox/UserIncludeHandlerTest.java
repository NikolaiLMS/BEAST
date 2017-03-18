/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.pse.beast.toolbox;

import java.util.ArrayList;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Niels
 */
public class UserIncludeHandlerTest {

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

    private UserIncludeHandler instance;

    public UserIncludeHandlerTest() {
        instance = new UserIncludeHandler();
    }

    /**
     * Test of getIncludes method, of class UserIncludeHandler.
     */
    @Test
    public void testGetIncludes() {
        System.out.println("getIncludes");
        instance = new UserIncludeHandler();
        ArrayList<String> result = instance.getIncludes();
        result.forEach((line) -> {
            System.out.println(line);
        });
        String[] expResult = new String[]{
            "#include <stdlib.h>",
            "#include <stdint.h>",
            "#include <assert.h>"
        };
        assertEquals(result.size(), expResult.length);
        for (int i = 0; i < result.size(); i++) {
            assertEquals(result.get(i), expResult[i]);

        }
        for (int i = 0; i < CLIBRARIES.length; i++) {
            System.out.println("#include " + CLIBRARIES[i]);
        }

    }

}
