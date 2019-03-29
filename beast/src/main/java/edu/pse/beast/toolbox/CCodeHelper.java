package edu.pse.beast.toolbox;

import java.util.ArrayList;

import edu.pse.beast.celectiondescriptioneditor.electiontemplates.ElectionTemplateHandler;
import edu.pse.beast.datatypes.electiondescription.ElectionDescription;
import edu.pse.beast.datatypes.electiondescription.ElectionTypeContainer;
import edu.pse.beast.stringresource.StringResourceLoader;
import edu.pse.beast.types.InternalTypeContainer;
import edu.pse.beast.types.InternalTypeRep;

/**
 * This class contains functionality to generate C Code from internal data
 * structures.
 *
 * @author Holger Klein
 */
public final class CCodeHelper {
    private CCodeHelper() { }

    /**
     * returns the C constant which is the max amount of elements in a given list
     * container
     *
     * @param cont the type container representing the list
     * @return the size of the container in C Code
     */
    public static String getListSize(InternalTypeContainer cont) {
        if (cont.getAccessTypeIfList() == InternalTypeRep.CANDIDATE) {
            return UnifiedNameContainer.getCandidate();
        } else if (cont.getAccessTypeIfList() == InternalTypeRep.VOTER) {
            return UnifiedNameContainer.getVoter();
        } else if (cont.getAccessTypeIfList() == InternalTypeRep.SEAT) {
            return UnifiedNameContainer.getSeats();
        }

        ErrorLogger.log("");
        return "";
    }

    /**
     * creates the C-Type text representation of the given internal type container,
     * arrays are created as arrays: "unsigned int votes[" +
     * UnifiedNameContainer.getVoter() + "][" + UnifiedNameContainer.getCandidate()
     * + "]", for example
     *
     * @param electionContainer     the container for which the C type should be created
     * @param name                  the name of the variable
     * @return the c type
     */
    public static String getCType(ElectionTypeContainer electionContainer, String name) {
        String decl = "unsigned int " + name;
        decl = decl + electionContainer.getInputType().getInputString();
        return decl;
    }

    /**
     * creates the C-Type text representation of the given Internaltypecontainer,
     * arrays are created as pointers: "unsigned int *", for example
     *
     * @param electionContainer     the container for which the C type should be created
     * @return the c type
     */
    public static String getCTypePointer(ElectionTypeContainer electionContainer) {
        String decl = electionContainer.getOutputType().getOutputString();
        return decl;
    }

    /**
     * if the given InternaltypeContainer represents a list, it generates the String
     * representing a corresponding C-Array. Ex return: "[" +
     * UnifiedNameContainer.getCandidate() + "]"
     *
     * @param cont the container for which the C type should be created
     * @return the amount of square brackets and length constants needed
     */
    public static String getCArrayType(InternalTypeContainer cont) {
        InternalTypeContainer currentContainer = cont;
        String decl = "";
        while (currentContainer.isList()) {
            decl += "[" + getListSize(currentContainer) + "]";
            currentContainer = currentContainer.getListedType();
        }
        return decl;
    }

    /**
     * generates the declaration String for a voting function depending on its input
     * and result type.
     *
     * @param container the input format of the voting array passed to the function
     * @return the voting function declaration line
     */
    public static String generateDeclString(ElectionTypeContainer container) {
        String decl = "RESULT " + UnifiedNameContainer.getVotingMethod() + "(VOTES) {";
        decl = decl.replace("RESULT", getCTypePointer(container));
        decl = decl.replace("VOTES", getCType(container, UnifiedNameContainer.getVotingArray()));
        return decl;
    }

    /**
     * Generates the complete function block which is placed in the C editor if the
     * user creates a new election description. It adds the explanatory comments and
     * the closing curly bracket
     *
     * @param container            the input format of the voting array passed to
     *                             the function
     * @param name                 the name of the election
     * @param templateHandler      the Template handler which generated input and
     *                             result types
     * @param stringResourceLoader the string resource loader currently used
     * @return the complete voting function
     */
    public static ElectionDescription
                    generateElectionDescription(ElectionTypeContainer container,
                                                String name,
                                                ElectionTemplateHandler templateHandler,
                                                StringResourceLoader stringResourceLoader) {
        ElectionDescription description = new ElectionDescription(name, container.getInputType(),
                container.getOutputType(), 2, 0, 0, 0, true);
        ArrayList<String> code = new ArrayList<>();
        String inputIdInFile = container.getInputType().getInputIDinFile();
        String outputIdInFile = container.getOutputType().getOutputIDinFile();

        code.add("//" + stringResourceLoader.getStringFromID(inputIdInFile) + ": "
                + stringResourceLoader.getStringFromID(inputIdInFile + "_exp"));
        code.add("//" + stringResourceLoader.getStringFromID(outputIdInFile) + ": "
                + stringResourceLoader.getStringFromID(outputIdInFile + "_exp"));
        code.add(generateDeclString(container));
        code.add("} ");
        description.setCode(code);
        return description;
    }

    /**
     * returns the max value an element of the given ElectionTypeContainer can have
     *
     * @param container the list whose elements max value needs to be determined
     * @return max value an element of the given ElectionTypeContainer can have
     */
    public static String getMax(ElectionTypeContainer container) {
        return container.getInputType().getMaximalValue();
    }

    /**
     * returns the minimum value an element of the given ElectionTypeContainer can
     * have
     *
     * @param container the list whose elements min value needs to be determined
     * @return minimum value an element of the given ElectionTypeContainer can have
     */
    public static String getMin(ElectionTypeContainer container) {
        return container.getInputType().getMinimalValue();
    }
}