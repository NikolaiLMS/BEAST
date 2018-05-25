/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.pse.beast.saverloader;

import java.lang.reflect.Type;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import edu.pse.beast.highlevel.javafx.ParentTreeItem;

/**
 * Implements SaverLoader methods for creating saveStrings from
 * PreAndPostConditionsDescription objects and vice versa.
 * 
 * @author lukas
 */
public class PropertyListSaverLoader implements SaverLoader<List<ParentTreeItem>> {

	private static Gson saverLoader;

	static { //here you have the chance to register typeAdapters
		GsonBuilder builder = new GsonBuilder();
		saverLoader = builder.create();
	}

	@Override
	public List<ParentTreeItem> createFromSaveString(String toLoad) throws Exception {
		return saverLoader.fromJson(toLoad, new TypeToken<List<ParentTreeItem>>(){}.getType());
	}

	@Override
	public String createSaveString(List<ParentTreeItem> toSave) {
		
		Type listType = new TypeToken<List<ParentTreeItem>>(){}.getType();
		return saverLoader.toJson(toSave, listType);
	}
}
