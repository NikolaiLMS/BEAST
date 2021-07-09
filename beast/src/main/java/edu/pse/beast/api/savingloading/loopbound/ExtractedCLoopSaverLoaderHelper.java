package edu.pse.beast.api.savingloading.loopbound;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import edu.pse.beast.api.c_parser.CLoopParseResultType;
import edu.pse.beast.api.c_parser.CLoopTypes;
import edu.pse.beast.api.c_parser.ExtractedCLoop;
import edu.pse.beast.api.codegen.loopbounds.LoopBoundType;

public class ExtractedCLoopSaverLoaderHelper {

    private static final String UUID_KEY = "uuid";
    private static final String LOOPTYPE_KEY = "looptype";

    private static final String LINE_KEY = "line";
    private static final String POS_IN_LINE_KEY = "pos_in_line";
    private static final String NUMBER_IN_FUNCTION_KEY = "number_in_function";

    private static final String PARSE_RESULT_KEY = "parse_result";
    private static final String PARSED_BOUND_TYPE_KEY = "parsed_bound_type";

    private static final String MANUAL_BOUND_KEY = "manual_bound";

    private static final String FUNCTION_NAME_KEY = "function_name";

    private static final String PARENT_UUID_KEY = "parent_uuid";
    private static final String CHILDREN_UUIDS_KEY = "children_uuids";

    private static JSONObject fromExtractedLoop(ExtractedCLoop loop) {
        JSONObject json = new JSONObject();

        json.put(UUID_KEY, loop.getUuid());
        json.put(LOOPTYPE_KEY, loop.getLoopType().toString());

        json.put(LINE_KEY, loop.getLine());
        json.put(POS_IN_LINE_KEY, loop.getPosInLine());
        json.put(NUMBER_IN_FUNCTION_KEY, loop.getLoopNumberInFunction());

        json.put(PARSE_RESULT_KEY, loop.getLoopParseResult().toString());
        json.put(PARSED_BOUND_TYPE_KEY,
                loop.getParsedLoopBoundType().toString());

        if (loop.getParsedLoopBoundType() == LoopBoundType.MANUALLY_ENTERED_INTEGER) {
            json.put(MANUAL_BOUND_KEY, loop.getManualInteger());
        }

        json.put(FUNCTION_NAME_KEY, loop.getFunctionName());

        String parentUUID = "";
        if (loop.getParentLoop() != null) {
            json.put(PARENT_UUID_KEY, loop.getParentLoop().getUuid());
        }
        JSONArray childrenUUIDs = new JSONArray();
        for (ExtractedCLoop child : loop.getChildrenLoops()) {
            childrenUUIDs.put(child.getUuid());
        }

        json.put(CHILDREN_UUIDS_KEY, childrenUUIDs);

        return json;
    }

    private static ExtractedCLoop toExtractedLoop(JSONObject json) {
        String uuid = json.getString(UUID_KEY);
        CLoopTypes loopType = CLoopTypes.valueOf(json.getString(LOOPTYPE_KEY));

        int line = json.getInt(LINE_KEY);
        int posInLine = json.getInt(POS_IN_LINE_KEY);
        int numberInFunc = json.getInt(NUMBER_IN_FUNCTION_KEY);

        CLoopParseResultType parseResultType = CLoopParseResultType
                .valueOf(json.getString(PARSE_RESULT_KEY));
        LoopBoundType loopBoundType = LoopBoundType
                .valueOf(json.getString(PARSED_BOUND_TYPE_KEY));

        String functionName = json.getString(FUNCTION_NAME_KEY);

        ExtractedCLoop extractedCLoop = ExtractedCLoop.fromStoredValues(uuid,
                loopType, line, posInLine, numberInFunc, parseResultType,
                loopBoundType, functionName);

        if (loopBoundType == LoopBoundType.MANUALLY_ENTERED_INTEGER) {
            extractedCLoop.setManualInteger(json.getInt(MANUAL_BOUND_KEY));
        }

        return extractedCLoop;
    }

    public static JSONArray fromExtractedLoops(List<ExtractedCLoop> loops) {
        JSONArray arr = new JSONArray();
        for (ExtractedCLoop loop : loops) {
            arr.put(fromExtractedLoop(loop));
        }
        return arr;
    }

    private static void setupParentsAndChildren(ExtractedCLoop loop,
            JSONObject json, List<ExtractedCLoop> loops) {
        if (json.has(PARENT_UUID_KEY)) {
            String parentUUID = json.getString(PARENT_UUID_KEY);
            for (ExtractedCLoop possibleParent : loops) {
                if (possibleParent.getUuid().equals(parentUUID)) {
                    loop.setParentLoop(possibleParent);
                    break;
                }
            }
        }
        JSONArray childrenUUIDs = json.getJSONArray(CHILDREN_UUIDS_KEY);
        for (int i = 0; i < childrenUUIDs.length(); ++i) {
            String currentChildUUID = childrenUUIDs.getString(i);
            for (ExtractedCLoop possibleChild : loops) {
                if (possibleChild.getUuid().equals(currentChildUUID)) {
                    loop.addChild(possibleChild);
                }
            }
        }
    }

    public static List<ExtractedCLoop> toExtractedLoops(JSONArray arr) {
        List<ExtractedCLoop> loops = new ArrayList<>();
        for (int i = 0; i < arr.length(); ++i) {
            loops.add(toExtractedLoop(arr.getJSONObject(i)));
        }

        for (int i = 0; i < arr.length(); ++i) {
            setupParentsAndChildren(loops.get(i), arr.getJSONObject(i), loops);
        }

        return loops;
    }

}
