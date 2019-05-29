package edu.pse.beast.datatypes.booleanexpast.othervaluednodes;

import edu.pse.beast.datatypes.booleanexpast.BooleanExpNodeVisitor;
import edu.pse.beast.types.InOutType;

/**
 *
 * @author Holger Klein
 *
 */
public class ElectExp extends AccessValueNode {
    /**
     * @param type     type
     * @param accessVar accessing variable
     * @param count    the count of this vote expression
     */
    public ElectExp(InOutType type, TypeExpression[] accessVar, int count) {
        super(type, accessVar, count);
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