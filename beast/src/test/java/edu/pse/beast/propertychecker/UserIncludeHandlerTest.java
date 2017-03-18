/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.pse.beast.propertychecker;

import java.util.ArrayList;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Niels
 */
public class UserIncludeHandlerTest {

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
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

}
