/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.pse.beast.datatypes;

import edu.pse.beast.datatypes.propertydescription.PostAndPrePropertiesDescription;
import java.util.List;

/**
 *
 * @author Niels
 */
public class PropertyList {
    private final List<PostAndPrePropertiesDescription> descr;
    private final List<Boolean> checked; 
    
    /**
     *
     * @param descr   
     * @param checked    
     */
    public PropertyList(List<PostAndPrePropertiesDescription> descr, List<Boolean> checked) {
        this.descr = descr;
        this.checked = checked;
    }

    /**
     *
     * @return List of PostAndPrePropertiesDescription
     */
    public List<PostAndPrePropertiesDescription> getDescr() {
        return descr;
    }
    
    /**
     *
     * @return List of booleans   
     */
    public List<Boolean> getChecked() {
        return checked;
    }
    
}