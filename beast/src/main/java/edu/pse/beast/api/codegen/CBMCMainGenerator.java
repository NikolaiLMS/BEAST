package edu.pse.beast.api.codegen;

import java.util.ArrayList;
import java.util.List;

import edu.pse.beast.api.codegen.SymbolicCBMCVar.CBMCVarType;
import edu.pse.beast.api.codegen.booleanExpAst.BooleanExpASTData;
import edu.pse.beast.api.codegen.c_code.CFunction;
import edu.pse.beast.api.codegen.helperfunctions.InitVoteHelper;
import edu.pse.beast.api.codegen.helperfunctions.PerformVoteHelper;
import edu.pse.beast.api.codegen.loopbounds.LoopBoundHandler;
import edu.pse.beast.datatypes.booleanexpast.booleanvaluednodes.BooleanExpressionNode;

public class CBMCMainGenerator {

	// for the voting arrays declare the max votes variables. They depend on
	// the properties
	// create the voting arrays, depending on how often is voted
	// fill them with nondet int
	// depending on voting type, make sure the votes make sense (are between
	// bounds)
	// other preconditions
	// call the voting func
	// post conditions
	public static CFunction main(BooleanExpASTData preAstData,
			BooleanExpASTData postAstData, List<SymbolicCBMCVar> symCbmcVars,
			ElectionTypeCStruct voteArrStruct,
			ElectionTypeCStruct voteResultStruct, CodeGenOptions options,
			LoopBoundHandler loopBoundHandler) {
		CFunction created = new CFunction("main",
				List.of("int argc", "char ** argv"), "int");
		List<String> code = new ArrayList<>();

		String votesLowerBoundVarName = "votesLowerBound";
		String votesUpperBoundVarName = "votesUpperBound";

		String lower = "unsigned int VAR = 0;".replaceAll("VAR",
				votesLowerBoundVarName);
		String upper = "unsigned int VAR = C;".replaceAll("VAR",
				votesUpperBoundVarName);

		code.add(lower);
		code.add(upper);

		options.setVotesLowerBoundVarName(votesLowerBoundVarName);
		options.setVotesUpperBoundVarName(votesUpperBoundVarName);

		// TODO cleanup
		// init global symbolic vars
		for (SymbolicCBMCVar var : symCbmcVars) {
			code.add("unsigned int " + var.getName() + " = "
					+ options.getCbmcNondetUintName() + "();\n");
			if (var.getVarType() == CBMCVarType.CANDIDATE) {
				code.add(options.getCbmcAssumeName() + "(" + var.getName()
						+ " <= C);\n");
				code.add(options.getCbmcAssumeName() + "(" + var.getName()
						+ " >= 0);\n");
			}
		}

		// init votes
		int highestVote = Math.max(preAstData.getHighestVoteOrElect(),
				postAstData.getHighestVoteOrElect());

		for (int i = 0; i < highestVote; ++i) {
			String varName = "voteNUMBER".replaceAll("NUMBER",
					String.valueOf(i + 1));
			code.add("//initializing VAR".replaceAll("VAR", varName));
			code.add(InitVoteHelper.generateCode(varName, voteArrStruct,
					options, loopBoundHandler));
		}

		loopBoundHandler.getMainLoopBounds()
				.forEach(b -> System.out.println(b.getCBMCString()));

		CodeGenASTVisitor visitor = new CodeGenASTVisitor(voteArrStruct,
				voteResultStruct, options, loopBoundHandler);

		// preconditions
		visitor.setMode(CodeGenASTVisitor.Mode.ASSUME);
		for (List<BooleanExpressionNode> nodesList : preAstData.getTopAstNode()
				.getBooleanExpressions()) {
			for (BooleanExpressionNode node : nodesList) {
				String s = node.getTreeString(0);
				node.getVisited(visitor);
				code.add(visitor.getCodeBlock().generateCode());
			}
		}

		// vote
		for (int i = 0; i < highestVote; ++i) {
			String generatedVarName = "electNUMBER".replaceAll("NUMBER",
					String.valueOf(i + 1));
			String voteVarName = "voteNUMBER".replaceAll("NUMBER",
					String.valueOf(i + 1));

			code.add("//performing vote VAR".replaceAll("VAR",
					generatedVarName));

			code.add(PerformVoteHelper.generateCode(generatedVarName,
					voteVarName, voteArrStruct, voteResultStruct, options));
		}

		// postconditions
		visitor.setMode(CodeGenASTVisitor.Mode.ASSERT);
		for (List<BooleanExpressionNode> nodesList : postAstData.getTopAstNode()
				.getBooleanExpressions()) {
			for (BooleanExpressionNode node : nodesList) {
				String s = node.getTreeString(0);
				node.getVisited(visitor);
				code.add(visitor.getCodeBlock().generateCode());
			}
		}

		code.add("return 0;");

		created.setCode(code);
		return created;
	}

}
