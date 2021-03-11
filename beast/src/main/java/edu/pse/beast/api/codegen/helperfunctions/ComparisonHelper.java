package edu.pse.beast.api.codegen.helperfunctions;

import java.util.Map;

import edu.pse.beast.api.codegen.CodeGenOptions;
import edu.pse.beast.api.codegen.helperfunctions.templates.CodeTemplateComparison;
import edu.pse.beast.api.codegen.loopbounds.LoopBoundHandler;
import edu.pse.beast.api.electiondescription.CBMCVars;
import edu.pse.beast.api.electiondescription.CElectionVotingType;

public class ComparisonHelper {	
	
	public static String generateCode(
			String comparison, 
			String lhsVarName, 
			String rhsVarName,
			CElectionVotingType type, 
			CodeGenOptions options, 
			String assumeOrAssert,
			LoopBoundHandler loopBoundHandler) {
		
		Map<String, String> replacementMap = null;
		
		String code = null;
		if(type.getListDimensions() == 0) {
			replacementMap = Map.of(
					"ASSUME_OR_ASSERT", assumeOrAssert,
					"LHS_VAR_NAME", lhsVarName,
					"RHS_VAR_NAME", rhsVarName,
					"COMP", comparison					
					);
			code = CodeTemplateComparison.template0d;
		} else if (type.getListDimensions() == 1) {
			CBMCVars listSize = type.getListSizes().get(0);
			String amtString = null;
			switch (listSize) {
			case AMT_CANDIDATES:
				amtString = options.getCbmcAmountCandidatesVarName();
				break;
			case AMT_VOTERS:
				amtString = options.getCbmcAmountVotersVarName();
				break;
			default:
				break;
			}			
			replacementMap = Map.of(
					"AMT", amtString,
					"ASSUME_OR_ASSERT", assumeOrAssert,
					"LHS_VAR_NAME", lhsVarName,
					"RHS_VAR_NAME", rhsVarName,
					"COMP", comparison					
					);	
			if(comparison.equals("!=")) {
				code = CodeTemplateComparison.template1dUneq;
			} else {
				code = CodeTemplateComparison.template1d;
			}
		}
		
		code = CodeGenerationToolbox.replacePlaceholders(code, replacementMap);
		return code;
	}

}
