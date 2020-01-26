package edu.pse.beast.propertychecker;

import static edu.pse.beast.toolbox.CCodeHelper.arrAcc;
import static edu.pse.beast.toolbox.CCodeHelper.arrAccess;
import static edu.pse.beast.toolbox.CCodeHelper.conjunct;
import static edu.pse.beast.toolbox.CCodeHelper.define;
import static edu.pse.beast.toolbox.CCodeHelper.defineIfNonDef;
import static edu.pse.beast.toolbox.CCodeHelper.dotArrStructAccess;
import static edu.pse.beast.toolbox.CCodeHelper.eq;
import static edu.pse.beast.toolbox.CCodeHelper.forLoopHeaderCode;
import static edu.pse.beast.toolbox.CCodeHelper.functionCode;
import static edu.pse.beast.toolbox.CCodeHelper.include;
import static edu.pse.beast.toolbox.CCodeHelper.leq;
import static edu.pse.beast.toolbox.CCodeHelper.lineComment;
import static edu.pse.beast.toolbox.CCodeHelper.lt;
import static edu.pse.beast.toolbox.CCodeHelper.neq;
import static edu.pse.beast.toolbox.CCodeHelper.one;
import static edu.pse.beast.toolbox.CCodeHelper.parenthesize;
import static edu.pse.beast.toolbox.CCodeHelper.pointer;
import static edu.pse.beast.toolbox.CCodeHelper.two;
import static edu.pse.beast.toolbox.CCodeHelper.unsignedIntVar;
import static edu.pse.beast.toolbox.CCodeHelper.varAddCode;
import static edu.pse.beast.toolbox.CCodeHelper.varAssignCode;
import static edu.pse.beast.toolbox.CCodeHelper.varDivideCode;
import static edu.pse.beast.toolbox.CCodeHelper.varEqualsCode;
import static edu.pse.beast.toolbox.CCodeHelper.varMultiplyCode;
import static edu.pse.beast.toolbox.CCodeHelper.varSubtractCode;
import static edu.pse.beast.toolbox.CCodeHelper.zero;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.apache.commons.lang3.RandomStringUtils;

import edu.pse.beast.datatypes.booleanexpast.BooleanExpListNode;
import edu.pse.beast.datatypes.electiondescription.ElectionDescription;
import edu.pse.beast.datatypes.propertydescription.PreAndPostConditionsDescription;
import edu.pse.beast.datatypes.propertydescription.SymbolicVariable;
import edu.pse.beast.electionsimulator.ElectionSimulationData;
import edu.pse.beast.highlevel.javafx.GUIController;
import edu.pse.beast.toolbox.CCodeHelper;
import edu.pse.beast.toolbox.CodeArrayListBeautifier;
import edu.pse.beast.toolbox.ErrorLogger;
import edu.pse.beast.toolbox.UnifiedNameContainer;
import edu.pse.beast.toolbox.antlr.booleanexp.FormalPropertyDescriptionLexer;
import edu.pse.beast.toolbox.antlr.booleanexp.FormalPropertyDescriptionParser;
import edu.pse.beast.toolbox.antlr.booleanexp.generateast.BooleanExpScope;
import edu.pse.beast.toolbox.antlr.booleanexp.generateast.FormalPropertySyntaxTreeToAstTranslator;
import edu.pse.beast.toolbox.valueContainer.cbmcValueContainers.CBMCResultValueWrapper;
import edu.pse.beast.types.ComplexType;
import edu.pse.beast.types.InOutType;
import edu.pse.beast.types.InputType;
import edu.pse.beast.types.InternalTypeContainer;
import edu.pse.beast.types.OutputType;

/**
 * This class creates the .c file which will be checked with CBMC. It generates
 * a main method (including the FormalProperty), important IncludingCode and the
 * votingMethod (the ElectionDescription).
 *
 * <p>TODO Refactor this into multiple sub classes later on.
 *
 * @author Niels Hanselmann
 */
public class CBMCCodeGenerator {
    /** The Constant ARR. */
    private static final String ARR = "arr";
    /** The Constant TMP_STRUCT. */
    private static final String TMP_STRUCT = "tmp_struct";

    /** The Constant START. */
    private static final String START = "start";
    /** The Constant STOP. */
    private static final String STOP = "stop";

    /** The Constant TO_RETURN. */
    private static final String TO_RETURN = "toReturn";
    /** The Constant ALREADY_USED_ARR. */
    private static final String ALREADY_USED_ARR = "already_used_arr";

    /** The Constant NEW_INDEX. */
    private static final String NEW_INDEX = "new_index";
    /** The Constant SUB_ARR. */
    private static final String SUB_ARR = "sub_arr";
    /** The Constant LENGTH. */
    private static final String LENGTH = "length";

    /** The Constant C. */
    private static final String C = "C";
    /** The Constant S. */
    private static final String S = "S";
    /** The Constant V. */
    private static final String V = "V";

    /** The Constant CPROVER_ASSUME. */
    private static final String CPROVER_ASSUME = "__CPROVER_assume";
    /** The Constant CPROVER_ASSERT. */
    private static final String CPROVER_ASSERT = "__CPROVER_assert";

    /** The Constant ASSUME. */
    private static final String ASSUME = "assume";
    /** The Constant ASSERT. */
    private static final String ASSERT = "assert";
    /** The Constant ASSERT2. */
    private static final String ASSERT2 = "assert2";
    /** The Constant NONDET_INT. */
    private static final String NONDET_INT = "nondet_int";
    /** The Constant NONDET_UINT. */
    private static final String NONDET_UINT = "nondet_uint";

    /** The Constant MAIN. */
    private static final String MAIN = "main";
    /** The Constant STD_LIB. */
    private static final String STD_LIB = "stdlib";
    /** The Constant STD_INT. */
    private static final String STD_INT = "stdint";

    /** The Constant I. */
    private static final String I = "i";
    /** The Constant J. */
    private static final String J = "j";
    /** The Constant X. */
    private static final String X = "x";
    /** The Constant Y. */
    private static final String Y = "y";

    /** The Constant VOTES. */
    private static final String VOTES = "votes";
    /** The Constant ELECT. */
    private static final String ELECT = "elect";

    /** The Constant ORIG_VOTES_SIZE. */
    private static final String ORIG_VOTES_SIZE = "ORIG_VOTES_SIZE";
    /** The Constant POS_DIFF. */
    private static final String POS_DIFF = "pos_diff";
    /** The Constant TOTAL_DIFF. */
    private static final String TOTAL_DIFF = "total_diff";
    /** The Constant MARGIN. */
    private static final String MARGIN = "MARGIN";

    /** The Constant CANDIDATE. */
    private static final String CANDIDATE = "candidate";
    /** The Constant AMOUNT_VOTES. */
    private static final String AMOUNT_VOTES = "amountVotes";

    /** The Constant SUM. */
    private static final String SUM = "sum";
    /** The Constant MAX. */
    private static final String MAX = "max";

    /** The Constant SPLIT. */
    private static final String SPLIT = "split";
    /** The Constant SPLITS. */
    private static final String SPLITS = "splits";
    /** The Constant SPLIT_LINES. */
    private static final String SPLIT_LINES = "splitLines";
    /** The Constant SPLIT_ARR. */
    private static final String SPLIT_ARR = "split_arr";
    /** The Constant LAST_SPLIT. */
    private static final String LAST_SPLIT = "last_split";
    /** The Constant NEXT_SPLIT. */
    private static final String NEXT_SPLIT = "next_split";
    /** The Constant GET_RANDOM_SPLIT_LINES. */
    private static final String GET_RANDOM_SPLIT_LINES = "getRandomSplitLines";

    /** The Constant ONE. */
    private static final String ONE = "one";
    /** The Constant TWO. */
    private static final String TWO = "two";

    /** The Constant VOTES_ONE. */
    private static final String VOTES_ONE = "votesOne";
    /** The Constant VOTES_TWO. */
    private static final String VOTES_TWO = "votesTwo";
    /** The Constant SIZE_ONE. */
    private static final String SIZE_ONE = "sizeOne";
    /** The Constant SIZE_TWO. */
    private static final String SIZE_TWO = "sizeTwo";

    /** The Constant CONCAT_ONE. */
    private static final String CONCAT_ONE = "concatOne";
    /** The Constant PERMUTATE_ONE. */
    private static final String PERMUTATE_ONE = "permutateOne";
    /** The Constant VOTE_SUM_FOR_CANDIDATE. */
    private static final String VOTE_SUM_FOR_CANDIDATE =
            "voteSumForCandidate";
    /** The Constant UNIQUE. */
    private static final String UNIQUE = "Unique";

    /** The Constant COMMENT_EMPTY_SUB_ARRAY. */
    private static final String COMMENT_EMPTY_SUB_ARRAY =
            "The sub array should be empty.";
    /** The Constant COMMENT_START_STOP. */
    private static final String COMMENT_START_STOP =
            "Start is inclusive, stop is exclusive.";
    /** The Constant COMMENT_LIMIT_UPPER_BOUND. */
    private static final String COMMENT_LIMIT_UPPER_BOUND =
            "Limit the size to the upper bound V.";
    /** The Constant COMMENT_SET_BEGINNING. */
    private static final String COMMENT_SET_BEGINNING =
            "Set all to C in the beginning.";

    /** The code. */
    private CodeArrayListBeautifier code;

    /** The election desc. */
    private final ElectionDescription electionDesc;

    /** The pre and post cond desc. */
    private final PreAndPostConditionsDescription preAndPostCondDesc;

    /** The translator. */
    private final FormalPropertySyntaxTreeToAstTranslator translator;

    /** The visitor. */
    private final CBMCCodeGenerationVisitor visitor;

    // This number should be the number of votes we want to perform
    /** The number of times voted. */
    private int numberOfTimesVoted;

    /** The margin. */
    private int margin;

    /** The orig result. */
    private ElectionSimulationData origResult;

    /**
     * After the build the code is fully generated and can be acquired by
     * getCode().
     *
     * @param electionDescription
     *            the election decription that holds the code that describes the
     *            voting method. that code will be merged with the generated
     *            code
     * @param propCondDesc
     *            the Descriptions that will be used to generate the C-Code for
     *            CBMC
     */
    public CBMCCodeGenerator(final ElectionDescription electionDescription,
                             final PreAndPostConditionsDescription propCondDesc) {
        this.translator = new FormalPropertySyntaxTreeToAstTranslator();
        this.electionDesc = electionDescription;
        this.preAndPostCondDesc = propCondDesc;
        code = new CodeArrayListBeautifier();
        electionDescription.getContainer();
        this.visitor = new CBMCCodeGenerationVisitor(
                electionDescription.getContainer());
        // new CCodeHelper();
        generateCodeCheck();
    }

    /**
     * After the build the code is fully generated and can be acquired by
     * getCode().
     *
     * @param electionDescription
     *            the electionDecription that holds the code that describes the
     *            voting method. that code will be merged with the generated
     *            code
     * @param propertyCondDesc
     *            the descriptions that will be used to generate the C-Code for
     *            CBMC
     * @param marginVal
     *            the margin
     * @param origResultData
     *            the original election result
     * @param votingData
     *            the voting data
     */
    public CBMCCodeGenerator(final ElectionDescription electionDescription,
                             final PreAndPostConditionsDescription propertyCondDesc,
                             final int marginVal,
                             final ElectionSimulationData origResultData,
                             final ElectionSimulationData votingData) {
        this.translator = new FormalPropertySyntaxTreeToAstTranslator();
        this.electionDesc = electionDescription;
        this.preAndPostCondDesc = propertyCondDesc;
        code = new CodeArrayListBeautifier();
        electionDescription.getContainer();
        this.visitor = new CBMCCodeGenerationVisitor(
                electionDescription.getContainer());
        // new CCodeHelper();
        this.margin = marginVal;
        this.origResult = origResultData;
        generateCodeMargin(votingData);
    }

    /**
     * Constructor when it will be used to get the code for a test.
     *
     * @param electionDescription
     *            the electionDecription that holds the code that describes the
     *            voting method. that code will be merged with the generated
     *            code
     * @param propertyCondDesc
     *            the descriptions that will be used to generate the C-Code for
     *            CBMC
     * @param votingData
     *            the voting data
     */
    public CBMCCodeGenerator(final ElectionDescription electionDescription,
                             final PreAndPostConditionsDescription propertyCondDesc,
                             final ElectionSimulationData votingData) {
        this.translator = new FormalPropertySyntaxTreeToAstTranslator();
        this.electionDesc = electionDescription;
        this.preAndPostCondDesc = propertyCondDesc;
        code = new CodeArrayListBeautifier();
        electionDescription.getContainer();
        this.visitor =
                new CBMCCodeGenerationVisitor(
                        electionDescription.getContainer()
                        );
        // new CCodeHelper();
        generateCodeTest(votingData);
    }

    /**
     * Gets the code.
     *
     * @return returns the generated code
     */
    public ArrayList<String> getCode() {
        return code.getCodeArrayList();
    }

    /**
     * Generate code check.
     */
    private void generateCodeCheck() {
        code = addHeader(code);
        addVoteSumFunc(false);
        addVoteSumFunc(true);
        addOtherFunctions();
        code.add(lineComment("User code."));
        ArrayList<String> electionDescriptionCode = new ArrayList<String>();
        electionDescriptionCode.addAll(electionDesc.getComplexCode());
        code.addList(electionDescriptionCode);
        addMainMethod();
    }

    /**
     * We want to create code for a margin computation or just a test run.
     *
     * @param votingData
     *            the voting data
     */
    private void generateCodeMargin(final ElectionSimulationData votingData) {
        // Add the header and the voting data
        addMarginHeaders(votingData);
        // Add the code the user wrote (e.g the election function)
        code.addAll(GUIController.getController().getElectionDescription()
                .getComplexCode());
        // Add the code which defines the votes
        String origVotes =
                varAssignCode(electionDesc.getContainer().getInputStruct().getStructAccess()
                                + UnifiedNameContainer.getOrigVotesName(),
                              getVotingResultCode((CBMCResultValueWrapper)
                                                      votingData.getValues()))
                + CCodeHelper.SEMICOLON;
        String sizeOfVote = "" + electionDesc.getContainer().getInputType()
                .getSizesInOrder(votingData.getVoters(),
                        votingData.getCandidates(), votingData.getSeats())
                .get(0);
        String origVotesSize =
                varEqualsCode(ORIG_VOTES_SIZE) + sizeOfVote + CCodeHelper.SEMICOLON;
        code.add(origVotesSize);
        code.add(origVotes);
        addMarginMainCheck(margin, origResult);
    }

    /**
     * We want to create code for a margin computation or just a test run.
     *
     * @param votingData
     *            the voting data
     */
    private void generateCodeTest(final ElectionSimulationData votingData) {
        // add the header and the voting data
        addMarginHeaders(votingData);
        // add the code the user wrote (e.g the election function)
        code.addAll(GUIController.getController().getElectionDescription()
                .getComplexCode());
        // add the code which defines the votes
        String origVotes =
                varAssignCode(electionDesc.getContainer().getInputStruct().getStructAccess()
                                + CCodeHelper.BLANK + UnifiedNameContainer.getOrigVotesName(),
                              getVotingResultCode((CBMCResultValueWrapper)
                                                      votingData.getValues()))
                + CCodeHelper.SEMICOLON;
        code.add(origVotes);
        String sizeOfVote = "" + electionDesc.getContainer().getInputType()
                .getSizesInOrder(votingData.getVoters(),
                        votingData.getCandidates(), votingData.getSeats())
                .get(0);
        String origVotesSize =
                varEqualsCode(ORIG_VOTES_SIZE) + sizeOfVote + CCodeHelper.SEMICOLON;
        code.add(origVotesSize);
        addMarginMainTest(); // TODO Add gcc ability here
    }

    /**
     * Adds the margin main check.
     *
     * @param marginVal
     *            the margin val
     * @param origData
     *            the orig data
     */
    private void addMarginMainCheck(final int marginVal,
            final ElectionSimulationData origData) {
        // We add the margin which will get computed by the model checker.
        code.add(defineIfNonDef(MARGIN, marginVal));
        // We also add the original result, which is calculated by compiling the
        // program and running it.

        String origResultName = UnifiedNameContainer.getOrigResultName();
        electionDesc.getContainer().getOutputType()
            .addLastResultAsCode(code, origResult, origResultName);
        // Add the verify method:
        // Taken and adjusted from the paper:
        // https://formal.iti.kit.edu/~beckert/pub/evoteid2016.pdf

        // Add the main method.
        code.add(CCodeHelper.INT + CCodeHelper.BLANK
                + functionCode(MAIN) + CCodeHelper.BLANK
                + CCodeHelper.OPENING_PARENTHESES);
        addMarginCompMethod(code, electionDesc.getContainer().getInputType(),
                electionDesc.getContainer().getOutputType(), origResultName);
        code.add(CCodeHelper.CLOSING_BRACES);
    }

    /**
     * Adds the margin comp method.
     *
     * @param codeList
     *            the code list
     * @param inType
     *            the in type
     * @param outType
     *            the out type
     * @param origResultName
     *            the orig result name
     */
    private void addMarginCompMethod(final CodeArrayListBeautifier codeList,
                                     final InputType inType, final OutputType outType,
                                     final String origResultName) {
        codeList.add(lineComment("Verify for input."));
        String voteName = UnifiedNameContainer.getNewVotesName();
        addMarginCompMethodInput(codeList, inType, voteName);
        codeList.add(lineComment("Verify for output."));
        addMarginCompMethodOutput(codeList, outType, voteName, origResultName);
    }

    /**
     * Adds the margin comp method input.
     *
     * @param codeList
     *            the code list
     * @param inType
     *            the in type
     * @param voteName
     *            the vote name
     */
    private void addMarginCompMethodInput(final CodeArrayListBeautifier codeList,
                                          final InputType inType,
                                          final String voteName) {
        codeList.add(varAssignCode(CCodeHelper.INT + CCodeHelper.BLANK + TOTAL_DIFF,
                                   zero())
                + CCodeHelper.SEMICOLON);
        codeList.add(varAssignCode(CCodeHelper.INT + CCodeHelper.BLANK + POS_DIFF,
                                   zero())
                + CCodeHelper.SEMICOLON);
        String voteContainer = inType.getStruct().getStructAccess() + CCodeHelper.BLANK
                                + voteName + CCodeHelper.SEMICOLON;
        codeList.add(voteContainer);

        // addInitialisedValue(voteName, inType,
        //                     electionDesc.getContainer().getInputStruct(),
        //                     inType.getMinimalValue(),
        //                     inType.getMaximalValue());

        // addConditionalValue(voteName, inType); // The votes had to be valid
        //                                           beforehand

        // List<String> tmploopVars =
        //     addNestedForLoopTop(code, inType.getSizeOfDimensionsAsList(),
        //                         new ArrayList<String>());

        // code.add(inType.setVoteValue(voteName,
        //                              electionDesc.getContainer()
        //                                  .getNameContainer().getOrigVotesName(),
        //                              tmploopVars));
        // // Set the previous votes to the new votes.

        // addNestedForrLoopBot(code, inType.getAmountOfDimensions());
        List<String> loopVars =
                addNestedForLoopTop(codeList,
                                    inType.getSizeOfDimensionsAsList(),
                                    new ArrayList<String>());
        inType.flipVote(voteName, UnifiedNameContainer.getOrigVotesName(),
                        loopVars, codeList);

        // addConditionalValue(voteName, inType);
        // The votes have to be valid afterwards
        addNestedForrLoopBot(codeList, inType.getAmountOfDimensions());

        // No more changes than margin allows
        codeList.add(functionCode(ASSUME, leq(POS_DIFF, MARGIN))
                    + CCodeHelper.SEMICOLON);
        codeList.add(functionCode(ASSUME, eq(TOTAL_DIFF, zero()))
                    + CCodeHelper.SEMICOLON);
    }

    /**
     * Adds the conditional value.
     *
     * @param voteName
     *            the vote name
     * @param inType
     *            the in type
     */
    private void addConditionalValue(final String voteName,
                                     final InputType inType) {
        inType.restrictVotes(voteName, code);
    }

    /**
     * Adds the margin comp method output.
     *
     * @param codeList
     *            the code list
     * @param outType
     *            the out type
     * @param newVotesName
     *            the new votes name
     * @param origResultName
     *            the orig result name
     */
    private void addMarginCompMethodOutput(final CodeArrayListBeautifier codeList,
                                           final OutputType outType,
                                           final String newVotesName,
                                           final String origResultName) {
        String resultName = UnifiedNameContainer.getNewResultName();
        String resultContainer = outType.getStruct().getStructAccess() + CCodeHelper.BLANK
                + resultName;
        String resultAssignment =
                varAssignCode(resultContainer,
                              functionCode(UnifiedNameContainer.getVotingMethod(),
                                           ORIG_VOTES_SIZE, newVotesName))
                + CCodeHelper.SEMICOLON;
        codeList.add(resultAssignment);
        List<String> loopVars = addNestedForLoopTop(codeList,
                                                    outType.getSizeOfDimensionsAsList(),
                                                    new ArrayList<String>());
        String newResultAcc = outType.getFullVarAccess(resultName, loopVars);
        String origResultAcc = outType.getFullVarAccess(origResultName, loopVars);
        codeList.add(functionCode(ASSERT, eq(newResultAcc, origResultAcc))
                    + CCodeHelper.SEMICOLON);
        addNestedForrLoopBot(codeList, loopVars.size());
    }

    /**
     * TODO move to utility class.
     *
     * @param code
     *            the code beautifier it should be added to
     * @param dimensions
     *            the size of each dimension,
     * @param nameOfLoopVariables
     *            an empty, new list. Every new loop variable will be appended
     * @return the list
     */
    public static List<String> addNestedForLoopTop(final CodeArrayListBeautifier code,
                                                   final List<String> dimensions,
                                                   final List<String> nameOfLoopVariables) {
        if (dimensions.size() > 0) {
            String name = "loop_" + nameOfLoopVariables.size();
            name = code.getNotUsedVarName(name);
            nameOfLoopVariables.add(name);
            code.add(forLoopHeaderCode(name, CCodeHelper.LT_SIGN, dimensions.get(0)));
            code.addTab();
            return addNestedForLoopTop(code,
                                       dimensions.subList(1, dimensions.size()),
                                       nameOfLoopVariables);
        }
        return nameOfLoopVariables;
    }

    /**
     * TODO move to utility class.
     *
     * @param code
     *            the code
     * @param dimensions
     *            the dimensions
     */
    public static void addNestedForrLoopBot(final CodeArrayListBeautifier code,
                                            final int dimensions) {
        for (int i = 0; i < dimensions; i++) {
            code.add(CCodeHelper.CLOSING_BRACES);
        }
    }

    /**
     * Add the headers CBMC needs.
     *
     * @param votingData
     *            the voting data
     */
    private void addMarginHeaders(final ElectionSimulationData votingData) {
        code = addHeader(code);
        code.add(defineIfNonDef(UnifiedNameContainer.getVoter(),
                                votingData.getVoters()));
        code.add(defineIfNonDef(UnifiedNameContainer.getCandidate(),
                                votingData.getCandidates()));
        code.add(defineIfNonDef(UnifiedNameContainer.getSeats(),
                                votingData.getSeats()));
    }

    /**
     * Adds the margin main test.
     */
    private void addMarginMainTest() {
        // Since we only have one vote, we hard-code the
        // value "one" here
        int voteNumber = 1;
        code = electionDesc.getContainer().getOutputType()
                .addMarginMainTest(code, voteNumber);
    }

    /**
     * Adds the vote sum func.
     *
     * @param unique
     *            the unique
     */
    private void addVoteSumFunc(final boolean unique) {
        String input = electionDesc.getContainer().getInputStruct()
                .getStructAccess() + CCodeHelper.BLANK + TMP_STRUCT;
        code.add(
            unsignedIntVar(
                functionCode(VOTE_SUM_FOR_CANDIDATE + (unique ? UNIQUE : ""),
                             input,
                             unsignedIntVar(AMOUNT_VOTES),
                             unsignedIntVar(CANDIDATE))
            )   + CCodeHelper.BLANK
                + CCodeHelper.OPENING_BRACES);
        int dimensions = electionDesc.getContainer().getInputType()
                .getAmountOfDimensions();

        String[] sizes = electionDesc.getContainer().getInputType()
                .getSizeOfDimensions();
        sizes[0] = AMOUNT_VOTES;
        String forLoopStart = "";
        List<String> loopVariables = generateLoopVariables(dimensions, ARR);
        for (int i = 0; i < dimensions; i++) { // Add all needed loop headers
            forLoopStart += generateForLoopHeader(loopVariables.get(i),
                                                  sizes[i]);
        }
        String forLoopEnd = "";
        for (int i = 0; i < dimensions; i++) {
            forLoopEnd += CCodeHelper.CLOSING_BRACES; // Close the for-loops
        }
        List<String> access = new ArrayList<String>();
        for (int i = 0; i < dimensions; i++) {
            access.add(loopVariables.get(i));
        }
        String dataDef = electionDesc.getContainer().getInputType()
                .getDataTypeAndSign();
        String definition = dataDef + CCodeHelper.BLANK + ARR + electionDesc.getContainer()
                .getInputType().getDimensionDescriptor(true) + CCodeHelper.SEMICOLON;
        code.add(definition);
        String assignment =
                forLoopStart
                + varAssignCode(arrAccess(ARR, access),
                                dotArrStructAccess(TMP_STRUCT, access))
                + CCodeHelper.SEMICOLON
                + forLoopEnd + CCodeHelper.LINE_BREAK;
        code.add(assignment);
        code.add(varEqualsCode(SUM) + zero() + CCodeHelper.SEMICOLON);
        code.add(forLoopHeaderCode(I, CCodeHelper.LT_SIGN, AMOUNT_VOTES));

        // Add the specific code which differs for different input types
        electionDesc.getContainer().getInputType().addCodeForVoteSum(code,
                                                                     unique);
        code.add(CCodeHelper.CLOSING_BRACES);
        code.add(CCodeHelper.RETURN + CCodeHelper.BLANK + SUM + CCodeHelper.SEMICOLON);
        code.add(CCodeHelper.CLOSING_BRACES);
    }

    /**
     * Other functions needed for the check.
     */
    private void addOtherFunctions() {
        addSplitArrayFunctions();
        addConcatenationFunctions();
        addPermutationFunctions();
        addIntersectFunction();
    }

    /**
     * Adds the intersect function.
     */
    private void addIntersectFunction() {
        if (electionDesc.getContainer().getOutputType()
                .getAmountOfDimensions() != 1
                || !electionDesc.getContainer().getOutputType()
                        .getSizeOfDimensions()[0].equals(C)) {
            return;
        }

        code.add(electionDesc.getContainer().getOutputStruct().getStructAccess()
                + CCodeHelper.BLANK
                + functionCode("intersect",
                        electionDesc.getContainer().getOutputStruct().getStructAccess()
                            + CCodeHelper.BLANK + ONE,
                        electionDesc.getContainer().getOutputStruct().getStructAccess()
                            + CCodeHelper.BLANK + TWO)
                + CCodeHelper.BLANK + CCodeHelper.OPENING_BRACES);
        code.add(electionDesc.getContainer().getOutputStruct().getStructAccess()
                + CCodeHelper.BLANK + TO_RETURN + CCodeHelper.SEMICOLON);
        code.addSpaces(2);
        code.add("  " + forLoopHeaderCode(I, CCodeHelper.LT_SIGN, C));
        code.add("    "
                + varAssignCode(dotArrStructAccess(TO_RETURN, I), zero())
                + CCodeHelper.SEMICOLON);
        code.add("  " + CCodeHelper.CLOSING_BRACES);
        code.addSpaces(2);
        code.add("  " + forLoopHeaderCode(I, CCodeHelper.LT_SIGN, C));
        code.add("    "
                + functionCode(CCodeHelper.IF, conjunct(dotArrStructAccess(ONE, I),
                                                        dotArrStructAccess(TWO, I)))
                + CCodeHelper.BLANK + CCodeHelper.OPENING_BRACES);
        code.add("      "
                + varAssignCode(dotArrStructAccess(TO_RETURN, I), one())
                + CCodeHelper.SEMICOLON);
        code.add("    " + CCodeHelper.CLOSING_BRACES);
        code.add("  " + CCodeHelper.CLOSING_BRACES);
        code.addSpaces(2);
        code.add("  " + CCodeHelper.RETURN + CCodeHelper.BLANK
                + TO_RETURN + CCodeHelper.SEMICOLON);
        code.add(CCodeHelper.CLOSING_BRACES);
    }

    /**
     * Adds the permutation functions.
     */
    private void addPermutationFunctions() {
        addPermutateOneFunction();
        addPermutateTwoFunction();
    }

    /**
     * Adds the permutate two function.
     */
    private void addPermutateTwoFunction() {
        if (electionDesc.getContainer().getInputType()
                .getAmountOfDimensions() != 2
                || !electionDesc.getContainer().getInputType()
                        .getSizeOfDimensions()[0].equals(V)
                || !electionDesc.getContainer().getInputType()
                        .getSizeOfDimensions()[1].equals(C)) {
            return;
        }
        String voteStruct = electionDesc.getContainer().getInputStruct()
                .getStructAccess();

        code.add(voteStruct + CCodeHelper.BLANK
                + functionCode("permutateTwo", voteStruct, VOTES,
                               unsignedIntVar(LENGTH))
                + CCodeHelper.BLANK
                + CCodeHelper.OPENING_BRACES);
        code.add(voteStruct + CCodeHelper.BLANK + SUB_ARR + CCodeHelper.SEMICOLON);
        code.addSpaces(2);
        code.add(unsignedIntVar(arrAccess(ALREADY_USED_ARR, V))
                + CCodeHelper.SEMICOLON);
        code.addSpaces(4);
        code.add(forLoopHeaderCode(I, CCodeHelper.LT_SIGN, V)
                + CCodeHelper.BLANK + lineComment(COMMENT_SET_BEGINNING));
        code.add(forLoopHeaderCode(J, CCodeHelper.LT_SIGN, C));
        code.add(varAssignCode(dotArrStructAccess(SUB_ARR, I, J), C)
                + CCodeHelper.SEMICOLON);
        code.add(CCodeHelper.CLOSING_BRACES);
        code.add(CCodeHelper.CLOSING_BRACES);
        code.add();
        code.addSpaces(2);
        code.add(forLoopHeaderCode(I, CCodeHelper.LT_SIGN, LENGTH));
        code.add(varAssignCode(unsignedIntVar(NEW_INDEX),
                               functionCode(NONDET_UINT))
                + CCodeHelper.SEMICOLON);
        code.add(functionCode(ASSUME, conjunct(leq(zero(), NEW_INDEX),
                                                 lt(NEW_INDEX, LENGTH)))
                + CCodeHelper.SEMICOLON);
        code.add();
        code.add(forLoopHeaderCode(J, CCodeHelper.LT_SIGN, I));
        code.add(functionCode(ASSUME,
                              neq(NEW_INDEX, arrAccess(ALREADY_USED_ARR, J)))
                + CCodeHelper.SEMICOLON);
        code.add(CCodeHelper.CLOSING_BRACES);
        code.addSpaces(4);
        code.add("    " + varAssignCode(arrAccess(ALREADY_USED_ARR, I), NEW_INDEX)
                + CCodeHelper.SEMICOLON);
        code.addSpaces(4);
        code.add("    " + forLoopHeaderCode(J, CCodeHelper.LT_SIGN, C));
        code.add("      "
                + varAssignCode(dotArrStructAccess(SUB_ARR, NEW_INDEX, J),
                                dotArrStructAccess(VOTES, I, J))
                + CCodeHelper.SEMICOLON);
        code.add("    " + CCodeHelper.CLOSING_BRACES);
        code.add("  " + CCodeHelper.CLOSING_BRACES);
        code.addSpaces(2);
        code.add(voteStruct + CCodeHelper.BLANK + TO_RETURN + CCodeHelper.SEMICOLON);
        code.addSpaces(4);
        code.add("  "
                + forLoopHeaderCode(I, CCodeHelper.LT_SIGN, V));
        code.add("    "
                + forLoopHeaderCode(J, CCodeHelper.LT_SIGN, C));
        code.add("      "
                + varAssignCode(dotArrStructAccess(TO_RETURN, I, J),
                                dotArrStructAccess(SUB_ARR, I, J))
                + CCodeHelper.SEMICOLON);
        code.add("    " + CCodeHelper.CLOSING_BRACES);
        code.add("  " + CCodeHelper.CLOSING_BRACES);
        code.addSpaces(4);
        code.add("  " + CCodeHelper.RETURN + CCodeHelper.BLANK
                + TO_RETURN + CCodeHelper.SEMICOLON);
        code.add(CCodeHelper.CLOSING_BRACES);
    }

    /**
     * Adds the permutate one function.
     */
    private void addPermutateOneFunction() {
        if (electionDesc.getContainer().getInputType()
                .getAmountOfDimensions() != 1
                || !electionDesc.getContainer().getInputType()
                        .getSizeOfDimensions()[0].equals(V)) {
            return;
        }
        String voteStruct =
                electionDesc.getContainer().getInputStruct().getStructAccess();

        code.add(voteStruct + CCodeHelper.BLANK
                + functionCode(PERMUTATE_ONE,
                               voteStruct + CCodeHelper.BLANK + VOTES,
                               unsignedIntVar(LENGTH))
                + CCodeHelper.BLANK
                + CCodeHelper.OPENING_BRACES);
        code.add(voteStruct + CCodeHelper.BLANK + SUB_ARR + CCodeHelper.SEMICOLON);
        code.addSpaces(2);
        code.add(" " + unsignedIntVar(arrAccess(ALREADY_USED_ARR, V))
                + CCodeHelper.SEMICOLON);
        code.addSpaces(4);
        code.add("  "
                + forLoopHeaderCode(I, CCodeHelper.LT_SIGN, V)
                + CCodeHelper.BLANK + lineComment(COMMENT_SET_BEGINNING));
        code.add("    "
                + varAssignCode(dotArrStructAccess(SUB_ARR, I), C)
                + CCodeHelper.SEMICOLON);
        code.add("  " + CCodeHelper.CLOSING_BRACES);
        code.addSpaces(2);
        code.addSpaces(2);
        code.add("  " + forLoopHeaderCode(I, CCodeHelper.LT_SIGN, LENGTH));
        code.add("    "
                + varEqualsCode(NEW_INDEX)
                + functionCode(NONDET_UINT)
                + CCodeHelper.SEMICOLON);
        code.add("    "
                + functionCode(ASSUME, conjunct(parenthesize(leq(zero(), NEW_INDEX)),
                                                parenthesize(lt(NEW_INDEX, LENGTH))))
                + CCodeHelper.SEMICOLON);
        code.addSpaces(4);
        code.add("    "
                + forLoopHeaderCode(J, CCodeHelper.LT_SIGN, I));
        code.add("      "
                + functionCode(ASSUME, neq(NEW_INDEX, arrAccess(ALREADY_USED_ARR, J)))
                + CCodeHelper.SEMICOLON);
        code.add("    " + CCodeHelper.CLOSING_BRACES);
        code.addSpaces(4);
        code.add("    " + varAssignCode(arrAccess(ALREADY_USED_ARR, I), NEW_INDEX)
               + CCodeHelper.SEMICOLON);
        code.addSpaces(4);
        code.add("    " + varAssignCode(dotArrStructAccess(SUB_ARR, NEW_INDEX),
                                        dotArrStructAccess(VOTES, I))
               + CCodeHelper.SEMICOLON);
        code.add("  " + CCodeHelper.CLOSING_BRACES);
        code.addSpaces(2);
        code.add(voteStruct + CCodeHelper.BLANK + TO_RETURN + CCodeHelper.SEMICOLON);
        code.addSpaces(4);
        code.add("  "
                + forLoopHeaderCode(I, CCodeHelper.LT_SIGN, V));
        code.add("    " + varAssignCode(dotArrStructAccess(TO_RETURN, I),
                                        dotArrStructAccess(SUB_ARR, I))
                + CCodeHelper.SEMICOLON);
        code.add("  " + CCodeHelper.CLOSING_BRACES);
        code.addSpaces(4);
        code.add("  " + CCodeHelper.RETURN + CCodeHelper.BLANK
                + TO_RETURN + CCodeHelper.SEMICOLON);
        code.addSpaces(2);
        code.add(CCodeHelper.CLOSING_BRACES);
        code.add();
    }

    /**
     * Adds the concatenation functions.
     */
    private void addConcatenationFunctions() {
        addConcatOneFunction();
        addConcatTwoFunction();
    }

    /**
     * Adds the concat two function.
     */
    private void addConcatTwoFunction() {
        if (electionDesc.getContainer().getInputType()
                .getAmountOfDimensions() != 2
                || !electionDesc.getContainer().getInputType()
                        .getSizeOfDimensions()[0].equals(V)
                || !electionDesc.getContainer().getInputType()
                        .getSizeOfDimensions()[1].equals(C)) {
            return;
        }
        String voteStruct = electionDesc.getContainer().getInputStruct()
                .getStructAccess();
        code.add(voteStruct + CCodeHelper.BLANK
                + functionCode("concatTwo",
                               voteStruct + CCodeHelper.BLANK + VOTES_ONE,
                               unsignedIntVar(SIZE_ONE),
                               voteStruct + CCodeHelper.BLANK + VOTES_TWO,
                               unsignedIntVar(SIZE_TWO))
                + CCodeHelper.BLANK + CCodeHelper.OPENING_BRACES);
        code.add(voteStruct + CCodeHelper.BLANK + SUB_ARR + CCodeHelper.SEMICOLON);
        code.addSpaces(2);
        code.add("  "
                + forLoopHeaderCode(I, CCodeHelper.LT_SIGN, V)
                + CCodeHelper.BLANK + lineComment(COMMENT_SET_BEGINNING));
        code.add("    "
                + forLoopHeaderCode(J, CCodeHelper.LT_SIGN, C));
        code.add("      "
                + varAssignCode(dotArrStructAccess(SUB_ARR, I, J), C)
                + CCodeHelper.SEMICOLON);
        code.add("    " + CCodeHelper.CLOSING_BRACES);
        code.add("  " + CCodeHelper.CLOSING_BRACES);
        code.addSpaces(2);
        code.add("  "
                + forLoopHeaderCode(I, CCodeHelper.LT_SIGN, SIZE_ONE)
                + CCodeHelper.BLANK + lineComment(COMMENT_LIMIT_UPPER_BOUND));
        code.add("    "
                + forLoopHeaderCode(J, CCodeHelper.LT_SIGN, C));
        code.add("      "
                + functionCode(CCodeHelper.IF, lt(I, V))
                + CCodeHelper.BLANK + CCodeHelper.OPENING_BRACES);
        code.add("        "
                + varAssignCode(dotArrStructAccess(SUB_ARR, I, J),
                                dotArrStructAccess(VOTES_ONE, I, J))
                + CCodeHelper.SEMICOLON);
        code.add("      " + CCodeHelper.CLOSING_BRACES);
        code.add("    " + CCodeHelper.CLOSING_BRACES);
        code.add("  " + CCodeHelper.CLOSING_BRACES);
        code.addSpaces(2);
        code.add("  "
                + forLoopHeaderCode(I, CCodeHelper.LT_SIGN, SIZE_TWO)
                + CCodeHelper.BLANK + lineComment(COMMENT_LIMIT_UPPER_BOUND));
        code.add("    "
                + forLoopHeaderCode(J, CCodeHelper.LT_SIGN, C));
        code.add("      "
                + functionCode(CCodeHelper.IF,
                               lt(varAddCode(SIZE_TWO, I), V))
                + CCodeHelper.BLANK + CCodeHelper.OPENING_BRACES);
        code.add("        " + varAssignCode(dotArrStructAccess(SUB_ARR, I, J),
                                            dotArrStructAccess(VOTES_TWO, I, J))
                + CCodeHelper.SEMICOLON);
        code.add("      " + CCodeHelper.CLOSING_BRACES);
        code.add("    " + CCodeHelper.CLOSING_BRACES);
        code.add("  " + CCodeHelper.CLOSING_BRACES);
        code.addSpaces(2);
        code.add(electionDesc.getContainer().getInputStruct().getStructAccess()
                + CCodeHelper.BLANK + TO_RETURN + CCodeHelper.SEMICOLON);
        code.addSpaces(4);
        code.add("  "
                + forLoopHeaderCode(I, CCodeHelper.LT_SIGN, V));
        code.add("    "
                + forLoopHeaderCode(J, CCodeHelper.LT_SIGN, C));
        code.add("      "
                + varAssignCode(dotArrStructAccess(TO_RETURN, I, J),
                                dotArrStructAccess(SUB_ARR, I, J))
                + CCodeHelper.SEMICOLON);
        code.add("    " + CCodeHelper.CLOSING_BRACES);
        code.add("  " + CCodeHelper.CLOSING_BRACES);
        code.addSpaces(4);
        code.add("  " + CCodeHelper.RETURN + CCodeHelper.BLANK
                + TO_RETURN + CCodeHelper.SEMICOLON);
        code.add(CCodeHelper.CLOSING_BRACES);
    }

    /**
     * Adds the concat one function.
     */
    private void addConcatOneFunction() {
        if (electionDesc.getContainer().getInputType()
                .getAmountOfDimensions() != 1
                || !electionDesc.getContainer().getInputType()
                        .getSizeOfDimensions()[0].equals(V)) {
            return;
        }
        String voteStruct =
                electionDesc.getContainer().getInputStruct().getStructAccess();
        code.add(voteStruct + CCodeHelper.BLANK
                + functionCode(CONCAT_ONE,
                               voteStruct + CCodeHelper.BLANK + VOTES_ONE,
                               unsignedIntVar(SIZE_ONE),
                               voteStruct + CCodeHelper.BLANK + VOTES_TWO,
                               unsignedIntVar(SIZE_TWO)) + CCodeHelper.BLANK
                + CCodeHelper.OPENING_BRACES);
        code.add(voteStruct + CCodeHelper.BLANK + SUB_ARR + CCodeHelper.SEMICOLON);
        code.addSpaces(2);
        code.add("  "
                + forLoopHeaderCode(I, CCodeHelper.LT_SIGN, V)
                + CCodeHelper.BLANK + lineComment(COMMENT_SET_BEGINNING));
        code.add("    "
                + varAssignCode(dotArrStructAccess(SUB_ARR, I), C)
                + CCodeHelper.SEMICOLON);
        code.add("  " + CCodeHelper.CLOSING_BRACES);
        code.addSpaces(2);
        code.add("  "
                + forLoopHeaderCode(I, CCodeHelper.LT_SIGN, SIZE_ONE)
                + CCodeHelper.BLANK + lineComment(COMMENT_LIMIT_UPPER_BOUND));
        code.add("    " + functionCode(CCodeHelper.IF, lt(I, V))
                + CCodeHelper.BLANK + CCodeHelper.OPENING_BRACES);
        code.add("      " + varAssignCode(dotArrStructAccess(SUB_ARR, I),
                                          dotArrStructAccess(VOTES_ONE, I))
                + CCodeHelper.SEMICOLON);
        code.add("    " + CCodeHelper.CLOSING_BRACES);
        code.add("  " + CCodeHelper.CLOSING_BRACES);
        code.addSpaces(2);
        code.add("  "
                + forLoopHeaderCode(I, CCodeHelper.LT_SIGN, SIZE_TWO)
                + CCodeHelper.BLANK + lineComment(COMMENT_LIMIT_UPPER_BOUND));
        code.add("    " + functionCode(CCodeHelper.IF, lt("sizeOne + i", V))
                + CCodeHelper.BLANK + CCodeHelper.OPENING_BRACES);
        code.add("      "
                + varAssignCode(dotArrStructAccess(SUB_ARR, varAddCode(SIZE_ONE, I)),
                                dotArrStructAccess(VOTES_TWO, I))
                + CCodeHelper.SEMICOLON);
        code.add("    " + CCodeHelper.CLOSING_BRACES);
        code.add("  " + CCodeHelper.CLOSING_BRACES);
        code.addSpaces(2);
        code.add(voteStruct + CCodeHelper.BLANK + TO_RETURN + CCodeHelper.SEMICOLON);
        code.addSpaces(4);
        code.add("  " + forLoopHeaderCode(I, CCodeHelper.LT_SIGN, V));
        code.add("    " + eq(dotArrStructAccess(TO_RETURN, I),
                             dotArrStructAccess(SUB_ARR, I))
                + CCodeHelper.SEMICOLON);
        code.add("  " + CCodeHelper.CLOSING_BRACES);
        code.addSpaces(4);
        code.add("  " + CCodeHelper.RETURN + CCodeHelper.BLANK
                + TO_RETURN + CCodeHelper.SEMICOLON);
        code.addSpaces(2);
        code.add(CCodeHelper.CLOSING_BRACES);
        code.add();
    }

    /**
     * Adds the split array functions.
     */
    private void addSplitArrayFunctions() {
        code.add(lineComment("Split array."));
        code.add();
        code.add(lineComment("Get splits cuts through an array of size max."));
        code.add(unsignedIntVar(pointer(
                functionCode(GET_RANDOM_SPLIT_LINES,
                             unsignedIntVar(SPLITS),
                             unsignedIntVar(MAX))))
                + CCodeHelper.BLANK + CCodeHelper.OPENING_BRACES);
        code.add("  " + varEqualsCode(pointer(SPLIT_ARR))
                + functionCode("malloc", varMultiplyCode(SPLITS,
                                                         functionCode(CCodeHelper.SIZE_OF,
                                                                      pointer(SPLIT_ARR))))
                + CCodeHelper.SEMICOLON);
        code.addSpaces(2);
        code.add("  " + functionCode(CCodeHelper.IF, eq(SPLITS, one()))
                + CCodeHelper.BLANK + CCodeHelper.OPENING_BRACES);
        code.add("    "
                + varEqualsCode(NEXT_SPLIT) + functionCode(NONDET_UINT)
                + CCodeHelper.SEMICOLON);
        code.add("    " + functionCode(ASSUME, leq(zero(), NEXT_SPLIT))
                + CCodeHelper.SEMICOLON);
        code.add("    " + functionCode(ASSUME,
                                       leq(NEXT_SPLIT,
                                               parenthesize(varDivideCode(MAX, two()))))
                + CCodeHelper.SEMICOLON);
        code.addSpaces(4);
        code.add("    " + varAssignCode(arrAccess(SPLIT_ARR, zero()), NEXT_SPLIT)
                + CCodeHelper.SEMICOLON);
        code.add("  " + CCodeHelper.CLOSING_BRACES + CCodeHelper.BLANK
                + CCodeHelper.ELSE + CCodeHelper.BLANK + CCodeHelper.OPENING_BRACES);
        code.add("    " + varEqualsCode(LAST_SPLIT) + zero() + CCodeHelper.SEMICOLON);
        code.add("    " + forLoopHeaderCode(I, CCodeHelper.LT_SIGN, SPLITS));
        code.add("      ");
        code.add("      " + varEqualsCode(NEXT_SPLIT)
                + functionCode(NONDET_UINT)
                + CCodeHelper.SEMICOLON);
        code.add("      "
                + functionCode(ASSUME, leq(LAST_SPLIT, NEXT_SPLIT))
                + CCodeHelper.SEMICOLON);
        code.add("      " + functionCode(ASSUME, leq(NEXT_SPLIT, MAX))
                + CCodeHelper.SEMICOLON);
        code.add("      ");
        code.add("      ");
        code.add("      ");
        code.add("      " + varAssignCode(arrAccess(SPLIT_ARR, I), NEXT_SPLIT)
                + CCodeHelper.SEMICOLON);
        code.add("      " + varAssignCode(LAST_SPLIT, NEXT_SPLIT) + CCodeHelper.SEMICOLON);
        code.add("    " + CCodeHelper.CLOSING_BRACES);
        code.addSpaces(4);
        code.add("    " + unsignedIntVar(pointer(SPLIT_LINES)) + CCodeHelper.SEMICOLON);
        code.add("    " + varAssignCode(SPLIT_LINES, SPLIT_ARR)
                + CCodeHelper.SEMICOLON);
        code.addSpaces(4);
        code.add("    " + forLoopHeaderCode(I, CCodeHelper.LT_SIGN, SPLITS));
        code.add("      "
                + varEqualsCode("debugrandom") + arrAccess(SPLIT_LINES, I)
                + CCodeHelper.SEMICOLON);
        code.add("    " + CCodeHelper.CLOSING_BRACES);
        code.add("  " + CCodeHelper.CLOSING_BRACES);
        code.add("  " + CCodeHelper.RETURN + CCodeHelper.BLANK
                + SPLIT_ARR + CCodeHelper.SEMICOLON);
        code.add(CCodeHelper.CLOSING_BRACES);
        code.add();
        code.add(lineComment(COMMENT_START_STOP));
        code.add();
        String voteStruct =
                electionDesc.getContainer().getInputStruct().getStructAccess();

        if (electionDesc.getContainer().getInputType()
                .getAmountOfDimensions() == 1
                && electionDesc.getContainer().getInputType()
                        .getSizeOfDimensions()[0].equals(V)) {
            code.add(voteStruct + CCodeHelper.BLANK
                    + functionCode(SPLIT,
                                   voteStruct + CCodeHelper.BLANK + VOTES,
                                   unsignedIntVar(START),
                                   unsignedIntVar(STOP))
                    + CCodeHelper.BLANK + CCodeHelper.OPENING_BRACES);
            code.add(voteStruct + CCodeHelper.BLANK + SUB_ARR + CCodeHelper.SEMICOLON);
            code.addSpaces(2);
            code.add("  " + forLoopHeaderCode(I, CCodeHelper.LT_SIGN, V)
                    + CCodeHelper.BLANK + lineComment(COMMENT_SET_BEGINNING));
            code.add("    "
                    + varAssignCode(dotArrStructAccess(SUB_ARR, I), C)
                    + CCodeHelper.SEMICOLON);
            code.add("  " + CCodeHelper.CLOSING_BRACES);
            code.addSpaces(2);
            code.add("  "
                    + functionCode(CCodeHelper.IF, eq(START, STOP))
                    + CCodeHelper.BLANK + CCodeHelper.OPENING_BRACES
                    + CCodeHelper.BLANK + lineComment(COMMENT_EMPTY_SUB_ARRAY));
            code.add(electionDesc.getContainer().getInputStruct().getStructAccess()
                    + CCodeHelper.BLANK + TO_RETURN + CCodeHelper.SEMICOLON);
            code.addSpaces(4);
            code.add("    " + forLoopHeaderCode(I, CCodeHelper.LT_SIGN, V));
            code.add("      "
                    + varAssignCode(dotArrStructAccess(TO_RETURN, I),
                                    dotArrStructAccess(SUB_ARR, I))
                    + CCodeHelper.SEMICOLON);
            code.add("    " + CCodeHelper.CLOSING_BRACES);
            code.addSpaces(4);
            code.add("    "
                    + CCodeHelper.RETURN + CCodeHelper.BLANK
                    + TO_RETURN + CCodeHelper.SEMICOLON);
            code.add("  " + CCodeHelper.CLOSING_BRACES + CCodeHelper.BLANK
                    + CCodeHelper.ELSE + CCodeHelper.BLANK + CCodeHelper.OPENING_BRACES);
            code.add();
            code.add("    "
                    + forLoopHeaderCode(I, CCodeHelper.LT_SIGN, V));
            code.add("      "
                    + functionCode(CCodeHelper.IF,
                                   conjunct(parenthesize(leq(START, I)),
                                            parenthesize(lt(I, STOP))))
                    + CCodeHelper.BLANK + CCodeHelper.OPENING_BRACES);
            code.add("        "
                    + varAssignCode(dotArrStructAccess(SUB_ARR, varSubtractCode(I, START)),
                                    dotArrStructAccess(VOTES, I))
                    + CCodeHelper.SEMICOLON);
            code.add("      " + CCodeHelper.CLOSING_BRACES);
            code.add("    " + CCodeHelper.CLOSING_BRACES);
            code.addSpaces(2);
            code.addSpaces(4);
            code.add(electionDesc.getContainer().getInputStruct().getStructAccess()
                    + CCodeHelper.BLANK + TO_RETURN + CCodeHelper.SEMICOLON);
            code.addSpaces(4);
            code.add("    " + forLoopHeaderCode(I, CCodeHelper.LT_SIGN, V));
            code.add("      " + varAssignCode(dotArrStructAccess(TO_RETURN, I),
                                              dotArrStructAccess(SUB_ARR, I))
                    + CCodeHelper.SEMICOLON);
            code.add("    " + CCodeHelper.CLOSING_BRACES);
            code.addSpaces(4);
            code.add("    " + CCodeHelper.RETURN + CCodeHelper.BLANK
                    + TO_RETURN + CCodeHelper.SEMICOLON);
            code.add("  " + CCodeHelper.CLOSING_BRACES);
            code.add(CCodeHelper.CLOSING_BRACES);
            code.add();
        }

        if (electionDesc.getContainer().getInputType()
                .getAmountOfDimensions() == 2
                && electionDesc.getContainer().getInputType()
                        .getSizeOfDimensions()[0].equals(V)
                && electionDesc.getContainer().getInputType()
                        .getSizeOfDimensions()[1].equals(C)) {

            code.add(lineComment(COMMENT_START_STOP));
            code.add(lineComment("Used for 2 dim arrays."));
            code.add(voteStruct + CCodeHelper.BLANK
                    + functionCode(SPLIT, voteStruct
                                            + CCodeHelper.BLANK + VOTES,
                                   unsignedIntVar(START),
                                   unsignedIntVar(STOP))
                    + CCodeHelper.BLANK + CCodeHelper.OPENING_BRACES);
            code.add(voteStruct + CCodeHelper.BLANK + SUB_ARR + CCodeHelper.SEMICOLON);
            code.addSpaces(2);
            code.add("  " + forLoopHeaderCode(I, CCodeHelper.LT_SIGN, V)
                    + CCodeHelper.BLANK + lineComment(COMMENT_SET_BEGINNING));
            code.add("    " + forLoopHeaderCode(J, CCodeHelper.LT_SIGN, C));
            code.add("      " + varAssignCode(dotArrStructAccess(SUB_ARR, I, J), C)
                    + CCodeHelper.SEMICOLON);
            code.add("    " + CCodeHelper.CLOSING_BRACES);
            code.add("  " + CCodeHelper.CLOSING_BRACES);
            code.addSpaces(2);
            code.add("  " + functionCode(CCodeHelper.IF, eq(START, STOP))
                    + CCodeHelper.BLANK + CCodeHelper.OPENING_BRACES
                    + CCodeHelper.BLANK + lineComment(COMMENT_EMPTY_SUB_ARRAY));
            code.add(electionDesc.getContainer().getInputStruct().getStructAccess()
                    + CCodeHelper.BLANK + TO_RETURN + CCodeHelper.SEMICOLON);
            code.addSpaces(4);
            code.add("    "
                    + forLoopHeaderCode(I, CCodeHelper.LT_SIGN, V));
            code.add("      " + forLoopHeaderCode(J, CCodeHelper.LT_SIGN, V));
            code.add("        " + varAssignCode(dotArrStructAccess(TO_RETURN, I, J),
                                                dotArrStructAccess(SUB_ARR, I, J))
                    + CCodeHelper.SEMICOLON);
            code.add("      " + CCodeHelper.CLOSING_BRACES);
            code.add("    " + CCodeHelper.CLOSING_BRACES);
            code.addSpaces(4);
            code.add("    " + CCodeHelper.RETURN + CCodeHelper.BLANK
                    + TO_RETURN + CCodeHelper.SEMICOLON);
            code.add("  " + CCodeHelper.CLOSING_BRACES + CCodeHelper.BLANK
                    + CCodeHelper.ELSE + CCodeHelper.BLANK + CCodeHelper.OPENING_BRACES);
            code.add();
            code.add("    " + forLoopHeaderCode(I, CCodeHelper.LT_SIGN, V));
            code.add("      " + functionCode(CCodeHelper.IF,
                                             conjunct(leq(START, I), lt(I, STOP)))
                    + CCodeHelper.BLANK + CCodeHelper.OPENING_BRACES);
            code.add("        " + forLoopHeaderCode(J, CCodeHelper.LT_SIGN, C));
            code.add("          " + varAssignCode(dotArrStructAccess(SUB_ARR,
                                                                     varSubtractCode(I, START),
                                                                     J),
                                                  dotArrStructAccess(VOTES, I, J))
                    + CCodeHelper.SEMICOLON);
            code.add("        " + CCodeHelper.CLOSING_BRACES);
            code.add("      " + CCodeHelper.CLOSING_BRACES);
            code.add("    " + CCodeHelper.CLOSING_BRACES);
            code.addSpaces(4);
            code.add(electionDesc.getContainer().getInputStruct().getStructAccess()
                    + CCodeHelper.BLANK + TO_RETURN + CCodeHelper.SEMICOLON);
            code.addSpaces(4);
            code.add("    " + forLoopHeaderCode(I, CCodeHelper.LT_SIGN, V));
            code.add("      " + forLoopHeaderCode(J, CCodeHelper.LT_SIGN, V));
            code.add("        " + varAssignCode(dotArrStructAccess(TO_RETURN, I, J),
                                                dotArrStructAccess(SUB_ARR, I, J))
                    + CCodeHelper.SEMICOLON);
            code.add("      " + CCodeHelper.CLOSING_BRACES);
            code.add("    " + CCodeHelper.CLOSING_BRACES);
            code.addSpaces(4);
            code.add("    " + CCodeHelper.RETURN + CCodeHelper.BLANK
                    + TO_RETURN + CCodeHelper.SEMICOLON);
            code.add("  " + CCodeHelper.CLOSING_BRACES);
            code.add(CCodeHelper.CLOSING_BRACES);
            code.add();
        }
    }

    /**
     * Writes all lines of the header.
     *
     * @param codeLst
     *            the list in which the header should be written
     * @return the finished header
     */
    public CodeArrayListBeautifier addHeader(final CodeArrayListBeautifier codeLst) {
        codeLst.add(include(STD_LIB));
        codeLst.add(include(STD_INT));
        codeLst.add(include(ASSERT));
        codeLst.add("");
        codeLst.add(unsignedIntVar(functionCode(NONDET_UINT))
                + CCodeHelper.SEMICOLON);
        codeLst.add(CCodeHelper.INT + CCodeHelper.BLANK
                + functionCode(NONDET_INT) + CCodeHelper.SEMICOLON);
        codeLst.add("");
        codeLst.add(define(functionCode(ASSERT2, X, Y),
                           functionCode(CPROVER_ASSERT, X, Y)));
        codeLst.add(CCodeHelper.DEFAULT + CCodeHelper.BLANK + functionCode(ASSUME, X)
                + CCodeHelper.BLANK + functionCode(CPROVER_ASSUME, X));
        codeLst.add("");
        codeLst.addAll(Arrays.asList(electionDesc.getContainer()
                .getStructDefinitions().split("\\n")));
        return codeLst;
    }

    /**
     * Adds the main method the main method declares the boolean expression. In
     * the main method the voting method is called.
     */
    private void addMainMethod() {
        code.add(CCodeHelper.INT + CCodeHelper.BLANK
                + functionCode(MAIN,
                               CCodeHelper.INT + CCodeHelper.BLANK + "argc",
                               CCodeHelper.CHAR + CCodeHelper.BLANK + pointer("argv[]"))
                + CCodeHelper.BLANK
                + CCodeHelper.OPENING_BRACES);
        code.addTab();
        // Generating the pre and post AbstractSyntaxTrees.
        BooleanExpListNode preAST = generateAST(
                preAndPostCondDesc.getPreConditionsDescription().getCode());
        BooleanExpListNode postAST = generateAST(
                preAndPostCondDesc.getPostConditionsDescription().getCode());
        initializeNumberOfTimesVoted(preAST, postAST);
        // Initialize all voting variables for the voters.
        for (int i = 1; i <= numberOfTimesVoted; i++) {
            code.add(varEqualsCode(UnifiedNameContainer.getVoter() + i)
                    + UnifiedNameContainer.getVoter() + CCodeHelper.SEMICOLON);
        }
        // Initialize all voting variables for the candidates.
        for (int i = 1; i <= numberOfTimesVoted; i++) {
            code.add(varEqualsCode(UnifiedNameContainer.getCandidate() + i)
                    + UnifiedNameContainer.getCandidate() + CCodeHelper.SEMICOLON);
        }
        // Initialize all voting variables for the seats.
        for (int i = 1; i <= numberOfTimesVoted; i++) {
            code.add(varEqualsCode(UnifiedNameContainer.getSeats() + i)
                    + UnifiedNameContainer.getSeats() + CCodeHelper.SEMICOLON);
        }
        List<String> boundedVars = preAndPostCondDesc.getBoundedVarDescription()
                .getCodeAsList();
        code.addList(boundedVars);
        // First the variables have to be initialized.
        addSymbVarInitialisation();

        for (int voteNumber = 1; voteNumber <= numberOfTimesVoted; voteNumber++) {
            String minV = "" + electionDesc.getContainer().getInputType()
                    .getMinimalValue();
            if (electionDesc.getContainer().getInputType()
                    .hasVariableAsMinValue()) {
                minV += voteNumber;
            }
            String maxV = "" + electionDesc.getContainer().getInputType()
                    .getMaximalValue();
            if (electionDesc.getContainer().getInputType()
                    .hasVariableAsMaxValue()) {
                maxV += voteNumber;
            }
            code.add(lineComment("Init for election:") + CCodeHelper.BLANK + voteNumber);
            addInitialisedValue(
                    UnifiedNameContainer.getVotingArray() + voteNumber,
                    electionDesc.getContainer().getInputType(),
                    electionDesc.getContainer().getInputStruct(), minV, maxV);
        }

        // The preconditions must be defined
        addPreProperties(preAST);
        // Now hold all the elections
        for (int i = 1; i <= numberOfTimesVoted; i++) {
            code.add(lineComment("Election number:") + CCodeHelper.BLANK + i);
            String sizeOfVotes = UnifiedNameContainer.getVoter() + i;
            String electX =
                    varAssignCode(
                        electionDesc.getContainer().getOutputStruct().getStructAccess()
                            + CCodeHelper.BLANK + ELECT + i,
                        functionCode(UnifiedNameContainer.getVotingMethod(),
                                     sizeOfVotes, VOTES))
                    + CCodeHelper.SEMICOLON;
            code.add(electX);
        }
        // Now the postconditions can be checked
        addPostProperties(postAST);
        code.deleteTab();
        code.add(CCodeHelper.CLOSING_BRACES);
    }

    /**
     * This should be used to create the VarInitialisation within the main
     * method.
     */
    private void addSymbVarInitialisation() {
        List<SymbolicVariable> symbolicVariableList =
                preAndPostCondDesc.getSymbolicVariablesAsList();
        code.add(lineComment("Symbolic variables initialisation"));
        symbolicVariableList.forEach(symbVar -> {
            InternalTypeContainer internalType = symbVar
                    .getInternalTypeContainer();
            String id = symbVar.getId();
            if (!internalType.isList()) {
                switch (internalType.getInternalType()) {
                case VOTER:
                    code.add(varEqualsCode(id) + functionCode(NONDET_UINT)
                            + CCodeHelper.SEMICOLON);
                    // A voter is basically an unsigned integer.
                    // The number shows which vote from votesX (the array of all
                    // votes) belongs to the voter.
                    code.add(functionCode(ASSUME, conjunct(leq(zero(), id), lt(id, V)))
                            + CCodeHelper.SEMICOLON);
                    // The voter has to be in the range of possible voters. V is
                    // the total amount of Voters.
                    break;
                case CANDIDATE:
                    code.add(varEqualsCode(id) + functionCode(NONDET_UINT)
                            + CCodeHelper.SEMICOLON);
                    // A candidate is basically an unsigned int. Candidate 0 is
                    // 0 and so on.
                    code.add(functionCode(ASSUME, conjunct(leq(zero(), id), lt(id, C)))
                            + CCodeHelper.SEMICOLON);
                    // C is the total number of all candidates. 0 is a candidate. C
                    // is not a candidate.
                    break;
                case SEAT:
                    // A seat is a also an unsigned int.
                    // The return of a voting method (an array) gives the
                    // elected candidate(value) of the seat(id).
                    code.add(varEqualsCode(id) + functionCode(NONDET_UINT)
                            + CCodeHelper.SEMICOLON);
                    // There are S seats. From 0 to S-1.
                    code.add(functionCode(ASSUME, conjunct(leq(zero(), id), lt(id, S)))
                            + CCodeHelper.SEMICOLON);
                    break;
                case APPROVAL:
                    break;
                case WEIGHTED_APPROVAL:
                    break;
                case INTEGER:
                    code.add(varEqualsCode(id) + functionCode(NONDET_UINT)
                            + CCodeHelper.SEMICOLON);
                    break;
                default:
                    reportUnsupportedType(id);
                }
            } else {
                reportUnsupportedType(id);
            }
        });
        code.add();
    }

    /**
     * This adds the Code of the PreProperties. It uses a visitor which it
     * creates.
     *
     * @param preAST
     *            the pre AST
     */
    private void addPreProperties(final BooleanExpListNode preAST) {
        code.add();
        code.add(lineComment("Preconditions "));
        code.add();
        visitor.setToPreConditionMode();
        preAST.getBooleanExpressions().forEach(booleanExpressionLists -> {
            booleanExpressionLists.forEach(booleanNode -> {
                code.addList(visitor.generateCode(booleanNode));
            });
        });
    }

    /**
     * This adds the Code of the postconditions. It uses a Visitor it creates.
     *
     * @param postAST
     *            the post AST
     */
    private void addPostProperties(final BooleanExpListNode postAST) {
        code.add();
        code.add(lineComment("Postconditions "));
        code.add();
        visitor.setToPostConditionMode();
        postAST.getBooleanExpressions().forEach(booleanExpressionLists -> {
            booleanExpressionLists.forEach(booleanNode -> {
                code.addList(visitor.generateCode(booleanNode));
            });
        });
    }

    /**
     * Report unsupported type.
     *
     * @param id
     *            the id
     */
    private void reportUnsupportedType(final String id) {
        ErrorLogger.log("The type of the symbolic variable " + id
                        + " is not supported.");
    }

    /**
     * Initialize number of times voted.
     *
     * @param preAST
     *            the pre AST
     * @param postAST
     *            the post AST
     */
    private void initializeNumberOfTimesVoted(final BooleanExpListNode preAST,
                                              final BooleanExpListNode postAST) {
        numberOfTimesVoted = (preAST.getMaxVoteLevel() > postAST
                .getMaxVoteLevel()) ? preAST.getMaxVoteLevel()
                        : postAST.getMaxVoteLevel();
        numberOfTimesVoted = (preAST.getHighestElect() > numberOfTimesVoted)
                ? preAST.getHighestElect()
                : numberOfTimesVoted;
        numberOfTimesVoted = (postAST.getHighestElect() > numberOfTimesVoted)
                ? postAST.getHighestElect()
                : numberOfTimesVoted;
    }

    /**
     * Adds a value of the fitting size as described.
     *
     * @param valueName
     *            the value name
     * @param inOutType
     *            the in out type
     * @param complexType
     *            the complex type
     * @param minValue
     *            the min value
     * @param maxValue
     *            the max value
     */
    private void addInitialisedValue(final String valueName,
                                     final InOutType inOutType,
                                     final ComplexType complexType,
                                     final String minValue,
                                     final String maxValue) {
        code.add(lineComment("Init of variable" + CCodeHelper.BLANK + valueName));
        String declaration =
                complexType.getStructAccess() + CCodeHelper.BLANK
                + valueName + CCodeHelper.SEMICOLON;
        code.add(declaration);
        List<String> loopVariables =
                addNestedForLoopTop(this.code,
                                    inOutType.getSizeOfDimensionsAsList(),
                                    new ArrayList<String>());
        String assignment =
                valueName + "."
                + UnifiedNameContainer.getStructValueName();
        for (int i = 0; i < inOutType.getAmountOfDimensions(); i++) {
            assignment += arrAcc(loopVariables.get(i));
        }
        code.add(varAssignCode(assignment, functionCode(NONDET_UINT))
                + CCodeHelper.SEMICOLON);
        code.add(functionCode(ASSUME, conjunct(parenthesize(leq(minValue, assignment)),
                                               parenthesize(leq(assignment, maxValue))))
                + CCodeHelper.SEMICOLON);
        inOutType.addExtraCodeAtEndOfCodeInit(code, valueName, loopVariables);
        addNestedForrLoopBot(this.code, inOutType.getAmountOfDimensions());
    }

    /**
     * Generate AST.
     *
     * @param codeString
     *            the code string
     * @return the boolean exp list node
     */
    private BooleanExpListNode generateAST(final String codeString) {
        FormalPropertyDescriptionLexer l =
                new FormalPropertyDescriptionLexer(CharStreams.fromString(codeString));
        CommonTokenStream ts = new CommonTokenStream(l);
        FormalPropertyDescriptionParser p =
                new FormalPropertyDescriptionParser(ts);
        BooleanExpScope declaredVars = new BooleanExpScope();
        preAndPostCondDesc.getSymbolicVariablesAsList().forEach(v -> {
            declaredVars.addTypeForId(v.getId(), v.getInternalTypeContainer());
        });
        return translator.generateFromSyntaxTree(
                p.booleanExpList(),
                electionDesc.getContainer().getInputType(),
                electionDesc.getContainer().getOutputType(), declaredVars
                );
    }

    /**
     * Gets the voting result code.
     *
     * @param votingData
     *            the voting data
     * @return the voting result code
     */
    private String getVotingResultCode(final CBMCResultValueWrapper votingData) {
        return electionDesc.getContainer().getInputType()
                .getVotingResultCode(votingData);
    }

    /**
     * Generate loop variables.
     *
     * @param dimensions
     *            the dimensions
     * @param variableName
     *            the variable name
     * @return the list
     */
    private List<String> generateLoopVariables(final int dimensions,
                                               final String variableName) {
        List<String> generatedVariables = new ArrayList<String>(dimensions);
        int currentIndex = 0;
        // Use i as the default name for a loop
        String defaultName = "loop_index_";
        for (int i = 0; i < dimensions; i++) {
            String varName = defaultName + currentIndex;
            boolean duplicate = true;
            int length = 1;
            while (duplicate) {
                if (code.contains(varName) || variableName.equals(varName)) {
                    varName = generateRandomString(length) + "_" + currentIndex;
                    // Increase the length in case all words from that
                    // length are already taken.
                    length++;
                } else {
                    duplicate = false;
                }
            }
            generatedVariables.add(varName);
            currentIndex++;
        }
        return generatedVariables;
    }

    /**
     * Generate random string.
     *
     * @param length
     *            the length
     * @return the string
     */
    private String generateRandomString(final int length) {
        return RandomStringUtils.random(length, true, false);
    }

    /**
     * Generate for loop header.
     *
     * @param indexName
     *            the index name
     * @param maxSize
     *            the max size
     * @return the string
     */
    private String generateForLoopHeader(final String indexName,
                                         final String maxSize) {
        return forLoopHeaderCode(indexName, CCodeHelper.LT_SIGN, maxSize);
    }
}
