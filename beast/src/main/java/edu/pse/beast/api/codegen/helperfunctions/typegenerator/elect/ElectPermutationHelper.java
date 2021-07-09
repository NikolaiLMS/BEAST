package edu.pse.beast.api.codegen.helperfunctions.typegenerator.elect;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.NotImplementedException;

import edu.pse.beast.api.codegen.cbmc.CodeGenOptions;
import edu.pse.beast.api.codegen.cbmc.ElectionTypeCStruct;
import edu.pse.beast.api.codegen.code_template.templates.elect.CodeTemplateElectPermutation;
import edu.pse.beast.api.codegen.helperfunctions.CodeGenerationToolbox;
import edu.pse.beast.api.codegen.loopbounds.CodeGenLoopBoundHandler;
import edu.pse.beast.api.codegen.loopbounds.LoopBound;
import edu.pse.beast.api.electiondescription.VotingOutputTypes;

public class ElectPermutationHelper {
    public static String generateCode(String generatedVarName, String varName,
            ElectionTypeCStruct electStruct, VotingOutputTypes votingOutputType,
            CodeGenOptions options, CodeGenLoopBoundHandler loopBoundHandler) {

        Map<String, String> replacementMap = Map.of("ELECT_TYPE",
                electStruct.getStruct().getName(), "AMT_MEMBER",
                electStruct.getAmtName(), "LIST_MEMBER",
                electStruct.getListName(), "GENERATED_VAR_NAME",
                generatedVarName, "ASSUME", options.getCbmcAssumeName(),
                "NONDET_UINT", options.getCbmcNondetUintName(), "RHS", varName,
                "PERM", "permutationIndices", "AMT_CANDIDATES",
                options.getCbmcAmountMaxCandsVarName());

        String code = null;
        List<LoopBound> loopbounds = List.of();

        switch (votingOutputType) {
        case CANDIDATE_LIST: {
            code = CodeTemplateElectPermutation.templateCandidateList;
            loopbounds = CodeTemplateElectPermutation.loopBoundsCandidateList;
            break;
        }
        case PARLIAMENT: {
            throw new NotImplementedException();
        }
        case PARLIAMENT_STACK: {
            throw new NotImplementedException();
        }
        case SINGLE_CANDIDATE: {
            throw new NotImplementedException();
        }
        }

        loopBoundHandler.pushMainLoopBounds(loopbounds);
        code = CodeGenerationToolbox.replacePlaceholders(code, replacementMap);
        return code;
    }
}
