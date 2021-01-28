import com.google.gson.*;
import javafx.util.Pair;

import java.lang.reflect.Type;

/**
 * Converts JsonElements to NumJson.
 */
class NumJsonDeserializer implements JsonDeserializer<NumJson> {

  @Override
  public NumJson deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
      throws JsonParseException {

    if (json.isJsonPrimitive()) {
      JsonPrimitive primitive = (JsonPrimitive) json;
      return this.primitiveToNumJsonPrimitive(primitive);
    }
    else if (json.isJsonArray()) {
      JsonArray array = (JsonArray) json;
      return this.arrayToNumJsonArray(array, context);
    }
    else if (json.isJsonObject()) {
      JsonObject object = (JsonObject) json;
      return this.objectToNumJsonObject(object, context);
    }
    else {
      throw new JsonParseException("NumJson expects only number, string, object, or array, received: " +
          json.toString());
    }
  }

  /**
   * Converts a json number or string to a NumJson object
   * @param primitive the JsonPrimitive to convert to NumJson
   * @return a NumJson version of the JsonPrimitive
   * @throws JsonParseException if the JsonPrimitive is not a valid NumJson (must be String or Integer)
   */
  private NumJson primitiveToNumJsonPrimitive(JsonPrimitive primitive) throws JsonParseException {
    try {
      return new NumJsonNumber(primitive.getAsInt());
    } catch (NumberFormatException notNum) {
      try {
        return new NumJsonString(primitive.getAsString());
      } catch (JsonParseException notNumAndNotString) {
        throw new JsonParseException("NumJson expects only number or string primitives, received: " +
            primitive.toString());
      }
    }
  }

  /**
   * Converts a json array to a valid NumJsonArray
   * @param array the JsonArray to convert to NumJson
   * @param context the context in which to convert the values of the json array
   * @return a NumJson version of the JsonArray
   */
  private NumJsonArray arrayToNumJsonArray(JsonArray array, JsonDeserializationContext context) {
    NumJson[] numJsons = new NumJson[array.size()];

    for (int idx = 0; idx < array.size(); idx++) {
      JsonElement element = array.get(idx);
      numJsons[idx] = context.deserialize(element, NumJson.class);
    }

    return new NumJsonArray(numJsons);
  }

  /**
   * Converts a json object to a valid NumJsonObject
   * @param object the JsonObject to convert to NumJson
   * @param context the context in which to convert the values of the json object
   * @return a NumJson version of the JsonObject
   */
  private NumJsonObject objectToNumJsonObject(JsonObject object, JsonDeserializationContext context) {
    Pair<String, NumJson>[] pairs = new Pair[object.size()];
    int idx = 0;

    for (String key : object.keySet()) {
      JsonElement element = object.get(key);
      NumJson numJson = context.deserialize(element, NumJson.class);
      pairs[idx] = new Pair(key, numJson);
      idx++;
    }

    return new NumJsonObject(pairs);
  }
}