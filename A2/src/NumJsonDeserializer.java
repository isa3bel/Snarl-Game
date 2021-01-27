import com.google.gson.*;
import javafx.util.Pair;

import java.lang.reflect.Type;

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

  private NumJson primitiveToNumJsonPrimitive(JsonPrimitive primitive) {
    try {
      return new NumJsonNumber(primitive.getAsInt());
    } catch (JsonParseException notNum) {
      try {
        return new NumJsonString(primitive.getAsString());
      } catch (JsonParseException notNumAndNotString) {
        throw new JsonParseException("NumJson expects only number or string primitives, received: " +
            primitive.toString());
      }
    }
  }

  private NumJsonArray arrayToNumJsonArray(JsonArray array, JsonDeserializationContext context) {
    NumJson[] numJsons = new NumJson[array.size()];

    for (int idx = 0; idx < array.size(); idx++) {
      JsonElement element = array.get(idx);
      numJsons[idx] = context.deserialize(element, NumJson.class);
    }

    return new NumJsonArray(numJsons);
  }

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