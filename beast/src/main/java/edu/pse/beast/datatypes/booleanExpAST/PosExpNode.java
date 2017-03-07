package edu.pse.beast.datatypes.booleanExpAST;

import edu.pse.beast.datatypes.internal.InternalTypeContainer;

/**
 * Created by holger on 07.03.17.
 */
public class PosExpNode extends TypeExpression {

    private int pos;

    public PosExpNode(InternalTypeContainer internalTypeContainer, int pos) {
        super(internalTypeContainer);
        this.pos = pos;
    }

    @Override
    public void getVisited(BooleanExpNodeVisitor visitor) {
        visitor.visitPosExpNode(this);
    }
}
