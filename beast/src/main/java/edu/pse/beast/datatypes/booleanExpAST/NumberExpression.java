/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.pse.beast.datatypes.booleanExpAST;

import edu.pse.beast.datatypes.internal.InternalTypeContainer;
import edu.pse.beast.datatypes.internal.InternalTypeRep;
/**
 *
 * @author Holger-Desktop
 */
public class NumberExpression extends TypeExpression{
    private final String numberAsString;
    
    public NumberExpression(String numberAsString) {
        super(new InternalTypeContainer(InternalTypeRep.INTEGER));
        this.numberAsString = numberAsString;
    }

    public String getNumberAsString() {
        return numberAsString;
    }    

    @Override
    public void getVisited(BooleanExpNodeVisitor visitor) {
        visitor.visitNumberExpNode(this);
    }
}
