/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.pse.beast.datatypes.propertydescription;

import edu.pse.beast.datatypes.internal.InternalTypeContainer;
import edu.pse.beast.datatypes.internal.InternalTypeRep;
import java.util.LinkedList;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Niels
 */
public class SymbolicVariableListTest {
   
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of addSymbolicVariable method, of class SymbolicVariableList.
     */
    @Test
    public void testAddSymbolicVariable() {
        System.out.println("addSymbolicVariable");
        String id = "";
        InternalTypeContainer internalTypeContainer = null;
        SymbolicVariableList instance = new SymbolicVariableList();
        instance.addSymbolicVariable(id, internalTypeContainer);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of isVarIDAllowed method, of class SymbolicVariableList.
     */
    @Test
    public void testIsVarIDAllowed() {
        System.out.println("isVarIDAllowed");
        String id = "";
        SymbolicVariableList instance = new SymbolicVariableList();
        InternalTypeContainer type;
        type = new InternalTypeContainer(InternalTypeRep.INTEGER);
        instance.addSymbolicVariable("test", type);
        boolean expResult = false;
        boolean result = instance.isVarIDAllowed(id);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setSymbolicVariableList method, of class SymbolicVariableList.
     */
    @Test
    public void testSetSymbolicVariableList() {
        System.out.println("setSymbolicVariableList");
        LinkedList<SymbolicVariable> symbolicVariableList = null;
        SymbolicVariableList instance = new SymbolicVariableList();
        instance.setSymbolicVariableList(symbolicVariableList);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getSymbolicVariables method, of class SymbolicVariableList.
     */
    @Test
    public void testGetSymbolicVariables() {
        System.out.println("getSymbolicVariables");
        SymbolicVariableList instance = new SymbolicVariableList();
        LinkedList<SymbolicVariable> expResult = null;
        LinkedList<SymbolicVariable> result = instance.getSymbolicVariables();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of removeSymbolicVariable method, of class SymbolicVariableList.
     */
    @Test
    public void testRemoveSymbolicVariable_String() {
        System.out.println("removeSymbolicVariable");
        String id = "";
        SymbolicVariableList instance = new SymbolicVariableList();
        boolean expResult = false;
        boolean result = instance.removeSymbolicVariable(id);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of removeSymbolicVariable method, of class SymbolicVariableList.
     */
    @Test
    public void testRemoveSymbolicVariable_int() {
        System.out.println("removeSymbolicVariable");
        int index = 0;
        SymbolicVariableList instance = new SymbolicVariableList();
        instance.removeSymbolicVariable(index);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of addListener method, of class SymbolicVariableList.
     */
    @Test
    public void testAddListener() {
        System.out.println("addListener");
        VariableListListener listener = null;
        SymbolicVariableList instance = new SymbolicVariableList();
        instance.addListener(listener);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of removeListener method, of class SymbolicVariableList.
     */
    @Test
    public void testRemoveListener() {
        System.out.println("removeListener");
        VariableListListener listener = null;
        SymbolicVariableList instance = new SymbolicVariableList();
        instance.removeListener(listener);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
