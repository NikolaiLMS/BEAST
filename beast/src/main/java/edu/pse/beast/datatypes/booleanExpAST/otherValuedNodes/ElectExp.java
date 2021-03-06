package edu.pse.beast.datatypes.booleanExpAST.otherValuedNodes;

import edu.pse.beast.datatypes.booleanExpAST.BooleanExpNodeVisitor;
import edu.pse.beast.types.InOutType;
import edu.pse.beast.types.InternalTypeContainer;

/**
 * 
 * @author Holger
 *
 */
public class ElectExp extends AccessValueNode {
/**
     * @param accesVar
     * @param count the count of this vote expression
     */
    public ElectExp(InOutType type, TypeExpression[] accesVar, int count) {
        super(type, accesVar, count);
    }
    
    @Override
    public void getVisited(BooleanExpNodeVisitor visitor) {
        visitor.visitElectExp(this);
    }

    @Override
    public String getTreeString(int depth) {
        return null;
    }

}
