/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.pse.beast.stringresource;

import edu.pse.beast.toolbox.FileLoader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 *
 * @author Niels
 */
public abstract class StringResourceProvider {

    protected String containingFolder;
    protected String relativePath;

    public void changeLanguage(String languageId) {

    }
    
}