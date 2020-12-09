package edu.pse.beast.saverloader.adapter;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import edu.pse.beast.highlevel.javafx.ChildTreeItem;
import edu.pse.beast.highlevel.javafx.CustomTreeItem;

/**
 * The Class CustomTreeItemAdapter.
 *
 * @author Lukas Stapelbroek
 */
public final class CustomTreeItemAdapter implements
        JsonSerializer<CustomTreeItem>, JsonDeserializer<CustomTreeItem> {
    /** The Constant PROPERTIES. */
    private static final String PROPERTIES = "properties";

    /** The Constant TYPE. */
    private static final String TYPE = "type";

    @Override
    public CustomTreeItem deserialize(final JsonElement json,
                                      final Type typeOfT,
                                      final JsonDeserializationContext context)
            throws JsonParseException {
        final JsonObject jsonObject = json.getAsJsonObject();
        final String type = jsonObject.get(TYPE).getAsString();
        final JsonElement element = jsonObject.get(PROPERTIES);
        if (type.equals(ChildTreeItem.class.getSimpleName())) {
            return context.deserialize(element, ChildTreeItem.class);
        } else {
            System.out.println("You should not serialize parent tree items.");
        }
        return null;
    }

    @Override
    public JsonElement serialize(final CustomTreeItem src, final Type typeOfSrc,
                                 final JsonSerializationContext context) {
        final JsonObject result = new JsonObject();
        result.add(TYPE, new JsonPrimitive(src.getClass().getSimpleName()));
        result.add(PROPERTIES, context.serialize(src, src.getClass()));
        return result;
    }
}