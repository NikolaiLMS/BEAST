package edu.pse.beast.SaverLoader;

import edu.pse.beast.celectiondescriptioneditor.ElectionTemplates.ElectionTemplateHandler;
import edu.pse.beast.datatypes.electiondescription.ElectionDescription;
import edu.pse.beast.datatypes.electiondescription.ElectionTypeContainer;
import edu.pse.beast.saverloader.ElectionDescriptionSaverLoader;
import org.junit.*;

import java.util.ArrayList;

/**
 * JUnit Testclass for saverloader.ElectionDescriptionSaverLoader.
 * @author NikolaiLMS
 */
public class ElectionDescriptionSaverLoaderTest {
    private static ElectionDescription electionDescription;
    private static ElectionDescriptionSaverLoader electionDescriptionSaverLoader;
    private static ElectionTemplateHandler electionTemplateHandler;

    @BeforeClass
    public static void setUpClass() {
        electionTemplateHandler = new ElectionTemplateHandler();
        ElectionTypeContainer inputType = electionTemplateHandler.getById("list_of_integer_vals_per_voter");
        ElectionTypeContainer outputType = electionTemplateHandler.getById("one_candidate_or_zero");
        electionDescriptionSaverLoader = new ElectionDescriptionSaverLoader();

        electionDescription = new ElectionDescription("testDescription", inputType, outputType, 2);
        ArrayList<String> code = new ArrayList<String>();
        code.add("sdfgokdffg");
        code.add("sdkofgdfg");
        electionDescription.setCode(code);
    }

    /**
     * Tests the ElectionDescriptionSaverLoader by creating a saveString from a
     * ElectionDescription object, then recreating that object from the saveString and checking its integrity.
     */
    @Test
    public void testCreateFromSaveString() throws Exception {
        String saveString = electionDescriptionSaverLoader.createSaveString(electionDescription);
        ElectionDescription recreatedElectionDescription = (ElectionDescription)
                electionDescriptionSaverLoader.createFromSaveString(saveString);

        assert (recreatedElectionDescription.getName().equals("testDescription"));
        assert (recreatedElectionDescription.getCode().get(0).equals("sdfgokdffg"));
        assert (recreatedElectionDescription.getCode().get(1).equals("sdkofgdfg"));
        assert (recreatedElectionDescription.getInputType().getId().equals("list_of_integer_vals_per_voter"));
        assert (recreatedElectionDescription.getOutputType().getId().equals("one_candidate_or_zero"));
        assert (recreatedElectionDescription.getVotingDeclLine() == 2);
    }
}
