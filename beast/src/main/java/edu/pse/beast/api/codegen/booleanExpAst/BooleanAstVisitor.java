package edu.pse.beast.api.codegen.booleanExpAst;

import edu.pse.beast.api.codegen.booleanExpAst.nodes.booleanExp.BooleanExpIsEmptyNode;
import edu.pse.beast.api.codegen.booleanExpAst.nodes.booleanExp.BooleanExpListElementNode;
import edu.pse.beast.api.codegen.booleanExpAst.nodes.types.election.ElectIntersectionNode;
import edu.pse.beast.api.codegen.booleanExpAst.nodes.types.election.VoteIntersectionNode;
import edu.pse.beast.api.codegen.booleanExpAst.nodes.types.election.VotePermutationNode;
import edu.pse.beast.api.codegen.booleanExpAst.nodes.types.election.VoteTupleNode;
import edu.pse.beast.datatypes.booleanexpast.booleanvaluednodes.ComparisonNode;
import edu.pse.beast.datatypes.booleanexpast.booleanvaluednodes.ForAllNode;
import edu.pse.beast.datatypes.booleanexpast.booleanvaluednodes.LogicalAndNode;
import edu.pse.beast.datatypes.booleanexpast.booleanvaluednodes.NotNode;
import edu.pse.beast.datatypes.booleanexpast.booleanvaluednodes.ThereExistsNode;
import edu.pse.beast.datatypes.booleanexpast.othervaluednodes.ElectExp;
import edu.pse.beast.datatypes.booleanexpast.othervaluednodes.SymbolicVarExp;
import edu.pse.beast.datatypes.booleanexpast.othervaluednodes.VoteExp;
import edu.pse.beast.datatypes.booleanexpast.othervaluednodes.integervaluednodes.BinaryIntegerValuedNode;
import edu.pse.beast.datatypes.booleanexpast.othervaluednodes.integervaluednodes.ConstantExp;
import edu.pse.beast.datatypes.booleanexpast.othervaluednodes.integervaluednodes.IntegerNode;
import edu.pse.beast.datatypes.booleanexpast.othervaluednodes.integervaluednodes.IntegerValuedExpression;
import edu.pse.beast.datatypes.booleanexpast.othervaluednodes.integervaluednodes.VoteSumForCandExp;
import edu.pse.beast.toolbox.antlr.booleanexp.FormalPropertyDescriptionParser.BooleanExpListElementContext;

public interface BooleanAstVisitor {

	/*
	 * 
	 * Nodes returning a boolean value
	 * 
	 */

	public void visitBooleanExpListElementNode(BooleanExpListElementNode node);
	public void visitComparisonNode(ComparisonNode node);
	public void visitForAllVotersNode(ForAllNode node);
	public void visitExistsCandidateNode(ThereExistsNode node);
	public void visitNotNode(NotNode node);
	public void visitAndNode(LogicalAndNode logicalAndNode);

	/*
	 * 
	 * Nodes returning some other value to be compared into a boolean value
	 * (such as voting types or integers)
	 * 
	 */

	public void visitVoteIntersectionNode(VoteIntersectionNode node);
	public void visitVoteExpNode(VoteExp node);
	public void visitElectExpNode(ElectExp node);
	public void visitVotePermutation(VotePermutationNode node);
	public void visitVoteTuple(VoteTupleNode node);
	public void visitSymbolicVarExp(SymbolicVarExp node);
	public void visitVoteSumExp(VoteSumForCandExp node);
	public void visitIntegerExp(IntegerNode node);
	public void visitBinaryIntegerExpression(
			BinaryIntegerValuedNode binaryIntegerValuedNode);
	public void visitConstantExp(ConstantExp constantExp);
	public void visitEmptyNode(BooleanExpIsEmptyNode booleanExpIsEmptyNode);
	public void visitElectIntersectionNode(
			ElectIntersectionNode electIntersectionNode);
	
	
	
}
