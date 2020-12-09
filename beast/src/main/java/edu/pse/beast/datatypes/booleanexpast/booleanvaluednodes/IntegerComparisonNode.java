package edu.pse.beast.datatypes.booleanexpast.booleanvaluednodes;

import edu.pse.beast.datatypes.booleanexpast.BooleanExpNodeVisitor;
import edu.pse.beast.datatypes.booleanexpast.ComparisonSymbol;
import edu.pse.beast.datatypes.booleanexpast.othervaluednodes.TypeExpression;

/**
 * The Class IntegerComparisonNode.
 *
 * @author Holger Klein
 */
public final class IntegerComparisonNode extends ComparisonNode {
    /**
     * Instantiates a new integer comparison node.
     *
     * @param lhsTypeExp
     *            the lhsExpression
     * @param rhsTypeExp
     *            the rhsExpression
     * @param comparisonSymbol
     *            the symbol that describes this comparison (for example <, >,
     *            == )
     */
    public IntegerComparisonNode(final TypeExpression lhsTypeExp,
                                 final TypeExpression rhsTypeExp,
                                 final ComparisonSymbol comparisonSymbol) {
        super(lhsTypeExp, rhsTypeExp, comparisonSymbol);
    }

    @Override
    public void getVisited(final BooleanExpNodeVisitor visitor) {
        visitor.visitIntegerComparisonNode(this);
    }
}