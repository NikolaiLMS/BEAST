package edu.pse.beast.types.cbmctypes.inputplugins;

import java.util.Arrays;
import java.util.List;

import edu.pse.beast.datatypes.electiondescription.ElectionTypeContainer;
import edu.pse.beast.highlevel.javafx.GUIController;
import edu.pse.beast.highlevel.javafx.NEWRowOfValues;
import edu.pse.beast.toolbox.CodeArrayListBeautifier;
import edu.pse.beast.toolbox.UnifiedNameContainer;
import edu.pse.beast.toolbox.valueContainer.ResultValueWrapper;
import edu.pse.beast.toolbox.valueContainer.cbmcValueContainers.CBMCResultValue;
import edu.pse.beast.toolbox.valueContainer.cbmcValueContainers.CBMCResultValueSingle;
import edu.pse.beast.types.InternalTypeContainer;
import edu.pse.beast.types.InternalTypeRep;
import edu.pse.beast.types.cbmctypes.CBMCInputType;

public class SingleChoice extends CBMCInputType {
    private static final int dimensions = 1;

    private final static String[] sizeOfDimensions = {
            UnifiedNameContainer.getVoter() };

    public SingleChoice() {
        super(true, DataType.INT, dimensions, sizeOfDimensions);
    }

    @Override
    public String getInputIDinFile() {
        return "SINGLE_CHOICE";
    }

    @Override
    public String getMinimalValue() {
        return "0";
    }

    @Override
    public String getMaximalValue() {
        return UnifiedNameContainer.getCandidate();
    }

    @Override
    public boolean hasVariableAsMinValue() {
        return false;
    }

    @Override
    public boolean hasVariableAsMaxValue() {
        return true;
    }

    @Override
    public boolean isVotingForOneCandidate() {
        return true;
    }

    @Override
    public String vetValue(ElectionTypeContainer container, List<NEWRowOfValues> row,
                           int rowNumber, int positionInRow, String newValue) {
        final int number;
        try {
            number = Integer.parseInt(newValue);
        } catch (NumberFormatException e) {
            return "0";
        }
        final String result;
        if (number < 0 || number > row.get(rowNumber).getAmountCandidates()) {
            result = "0";
        } else {
            result = newValue;
        }
        return result;
    }

    @Override
    public void restrictVotes(String voteName, CodeArrayListBeautifier code) {
        // dont have to restrict
    }

    @Override
    public String[] getVotePoints(String[][] votes, int amountCandidates, int amountVoters) {
        return super.wrongInputTypeArray(amountCandidates, amountVoters);
    }

    @Override
    public String[] getVotePoints(String[] votes, int amountCandidates, int amountVoters) {
        Long[] result = new Long[amountCandidates];
        Arrays.fill(result, 0L);
        for (int i = 0; i < amountVoters; i++) {
            int vote = Integer.parseInt(votes[i]);
            result[vote]++;
        }
        String[] toReturn = new String[amountCandidates];
        for (int i = 0; i < result.length; i++) {
            toReturn[i] = "" + result[i];
        }
        return toReturn;
    }

    // @Override
    // public void addMarginMainCheck(CodeArrayListBeautifier code, int margin,
    // List<String> origResult) {
    // code.add("int " + UnifiedNameContainer.getNewVotesName()
    // + "1[" + UnifiedNameContainer.getVoter() + "];");
    // code.add("for (int i = 0; i < V; i++) {"); // go over all voters
    // code.addTab();
    // code.add("int changed = nondet_int();"); // determine, if we want to
    // // changed votes for
    // // this
    // // voter
    // code.add("assume(0 <= changed);");
    // code.add("assume(changed <= 1);");
    // code.add("if(changed) {");
    // code.addTab();
    // // if we changed the vote, we keep track of it
    // code.add("total_diff++;");
    // // flip the vote (0 -> 1 | 1 -> 0)
    // code.add("" + UnifiedNameContainer.getNewVotesName()
    // + "1[i] = !ORIG_VOTES[i];");
    // code.deleteTab();
    // code.add("} else {");
    // code.addTab();
    // code.add("" + UnifiedNameContainer.getNewVotesName() + "1[i] =
    // ORIG_VOTES[i];");
    // code.deleteTab();
    // code.add("}");
    // code.deleteTab();
    // code.add("}");
    // code.add("assume(total_diff <= MARGIN);"); // no more changes than
    // // margin allows
    // }

    @Override
    public void addCodeForVoteSum(CodeArrayListBeautifier code, boolean unique) {
        code.add("if(arr[i] == candidate) sum++;");
    }

    @Override
    public InternalTypeContainer getInternalTypeContainer() {
        return new InternalTypeContainer(
                new InternalTypeContainer(InternalTypeRep.CANDIDATE),
                InternalTypeRep.VOTER);
    }

    @Override
    public int vetAmountCandidates(int amountCandidates) {
        if (amountCandidates < 1) {
            return 1;
        } else {
            return amountCandidates;
        }
    }

    @Override
    public int vetAmountVoters(int amountVoters) {
        if (amountVoters < 1) {
            return 1;
        } else {
            return amountVoters;
        }
    }

    @Override
    public int vetAmountSeats(int amountSeats) {
        if (amountSeats < 1) {
            return 1;
        } else {
            return amountSeats;
        }
    }

    @Override
    public int getNumVotingPoints(ResultValueWrapper result) {
        return GUIController.getController().getElectionSimulation().getNumVoters();
    }

    @Override
    public String otherToString() {
        return "Single choice";
    }

    @Override
    public CBMCResultValue convertRowToResultValue(NEWRowOfValues row) {
        List<String> values = row.getValues();
        String value = values.get(0);
        CBMCResultValueSingle toReturn = new CBMCResultValueSingle();
        toReturn.setValue("int", value, 32);
        return toReturn;
    }

    @Override
    public void addExtraCodeAtEndOfCodeInit(CodeArrayListBeautifier code,
                                            String valueName,
                                            List<String> loopVariables) {
    }
}
