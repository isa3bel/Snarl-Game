package testHarness.deserializer;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;
import model.level.Location;
import testHarness.query.MockPlayerController;

public class MockPlayerControllerDeserializer implements JsonDeserializer<MockPlayerController> {

  @Override
  public MockPlayerController deserialize(JsonElement jsonElement, Type type,
      JsonDeserializationContext context) throws JsonParseException {
    if (!jsonElement.isJsonArray()) {
      throw new JsonParseException("Expecting a JSON array, received: " + jsonElement.toString());
    }
    JsonArray jsonArray = jsonElement.getAsJsonArray();
    Location[] locations = new Location[jsonArray.size()];
    for (int idx = 0; idx < locations.length; idx++) {
      JsonObject object = getObject(jsonArray.get(idx));
      locations[idx] = object.get("to") != null
          ? context.deserialize(object.get("to"), Location.class)
          : null;
    }

    return new MockPlayerController(locations);
  }

  /**
   * Gets the JsonElement as an object if possible.
   * @param jsonElement the element to
   * @return the JsonObject
   * @throws JsonParseException if the given element is not a JsonObject
   */
  private static JsonObject getObject(JsonElement jsonElement) throws JsonParseException {
    if (!jsonElement.isJsonObject()) {
      throw new JsonParseException("expected an object, given: " + jsonElement.toString());
    }
    return (JsonObject) jsonElement;
  }
}
