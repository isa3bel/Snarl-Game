package testHarness;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;
import model.HallwayBuilder;
import model.Location;

public class HallwayBuilderDeserializer implements JsonDeserializer<HallwayBuilder> {

  @Override
  public HallwayBuilder deserialize(JsonElement jsonElement, Type type,
      JsonDeserializationContext context) throws JsonParseException {
    JsonObject jsonObject = getObject(jsonElement);
    Location from = context.deserialize(jsonObject.get("from"), Location.class);
    Location to = context.deserialize(jsonObject.get("to"), Location.class);
    Location[] waypoints = context.deserialize(jsonObject.get("waypoints"), Location[].class);
    return new HallwayBuilder(to, from, waypoints);
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
