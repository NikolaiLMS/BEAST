/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.pse.beast.codearea.UserActions;

import edu.pse.beast.codearea.Actionlist.Actionlist;
import edu.pse.beast.toolbox.UserAction;

/**
 * this useraction asks the given actionlist to undo the last action
 * @author Holger-Desktop
 */
public class UndoLastUserAction extends UserAction {
    private Actionlist list;
    public UndoLastUserAction(Actionlist list) {
        super("undo");
        this.list = list;
    }
    

    @Override
    public void perform() {
        list.undoLast();
    }
    
}
