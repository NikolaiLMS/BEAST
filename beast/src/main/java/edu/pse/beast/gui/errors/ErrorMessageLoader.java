package edu.pse.beast.gui.errors;

import java.util.Map;

/**
 * This class can be extended to load error messages from a file
 *
 * @author Holger Klein
 *
 */
public class ErrorMessageLoader {

    public Map<BeastErrorTypes, ErrorMessage> loadMessages() {
        return Map.of(BeastErrorTypes.NOT_IMPLEMENTED_CODE_PATH,
                new ErrorMessage("Not implemented",
                        "tried to run code that hasn't been implemented yet"),
                BeastErrorTypes.CANT_REMOVE_VOTING_FUNCTION,
                new ErrorMessage("Tried removing voting function",
                        "Cant remove the voting function"));
    }
}