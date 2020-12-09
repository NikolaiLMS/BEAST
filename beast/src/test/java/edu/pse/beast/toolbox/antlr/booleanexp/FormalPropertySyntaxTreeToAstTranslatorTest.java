package edu.pse.beast.toolbox.antlr.booleanexp;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.pse.beast.toolbox.antlr.booleanexp.generateast.BooleanExpScope;
import edu.pse.beast.toolbox.antlr.booleanexp.generateast.FormalPropertySyntaxTreeToAstTranslator;
import edu.pse.beast.types.InternalTypeContainer;
import edu.pse.beast.types.InternalTypeRep;

/**
 * The tests for FormalPropertySyntaxTreeToAstTranslator.
 *
 * @author Holger Klein
 */
public class FormalPropertySyntaxTreeToAstTranslatorTest {
    /**
     * Instantiates a new formal property syntax tree to ast translator test.
     */
    public FormalPropertySyntaxTreeToAstTranslatorTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test create AST comparison.
     */
    @Test
    public void testCreateASTComparison() {
        new FormalPropertySyntaxTreeToAstTranslator();
        new InternalTypeContainer(
            new InternalTypeContainer(InternalTypeRep.CANDIDATE),
            InternalTypeRep.VOTER
        );
        new InternalTypeContainer(
            new InternalTypeContainer(InternalTypeRep.CANDIDATE),
            InternalTypeRep.CANDIDATE
        );

        BooleanExpScope declaredVar = new BooleanExpScope();
        declaredVar.addTypeForId("c", new InternalTypeContainer(InternalTypeRep.CANDIDATE));
        declaredVar.addTypeForId("v", new InternalTypeContainer(InternalTypeRep.VOTER));

        // String exp = "ELECT2(c) == VOTES2(v);";
        // BooleanExpListNode created =
        //     translator.generateFromSyntaxTree(createFromString(exp),
        //                                       inputType, output,
        //                                       declaredVar);
        declaredVar = new BooleanExpScope();
    }

    /**
     * Test create AST.
     */
    @Test
    public void testCreateAST() {
        final String exp =
                "FOR_ALL_VOTERS(v) : "
              + "EXISTS_ONE_CANDIDATE(c) : "
              + "VOTES1(v) == c && VOTES1(v) == c;";
        final FormalPropertyDescriptionLexer lexer =
                new FormalPropertyDescriptionLexer(CharStreams.fromString(exp));
        final CommonTokenStream tokenS = new CommonTokenStream(lexer);
        new FormalPropertyDescriptionParser(tokenS);
        new FormalPropertySyntaxTreeToAstTranslator();
    }
}