package testHarness;

import com.google.gson.*;
import model.Level;
import model.LevelBuilder;
import model.Location;
import java.lang.reflect.Type;

/**
 * A deserializer for a Question.
 */
public class LocationQueryDeserializer implements JsonDeserializer<Question> {

  /**
   * Deserializes a question for the Snarl test harness.
   * @param jsonElement the element to deserialize
   * @param type the type to deserialize to
   * @param context the context in which to continue deserialization
   * @return the question that needs to be deserialized
   * @throws JsonParseException if the input is not formed as expected for this question
   */
  @Override
  public Question deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context)
      throws JsonParseException {
    if (!jsonElement.isJsonArray()) {
      throw new JsonParseException("Expecting a JSON array, received: " + jsonElement.toString());
    }
    JsonArray jsonArray = (JsonArray) jsonElement;

    LevelBuilder levelBuilder = context.deserialize(jsonArray.get(0), LevelBuilder.class);
    Level level = levelBuilder.build();
    Location location = context.deserialize(jsonArray.get(1), Location.class);
    Location roomOrigin = getRoomOrigin(jsonArray.get(0), context);

    return new LocationQuery(level, location, roomOrigin);
  }

  /**
   * Gets the room origin from this query.
   * @param jsonElement the room object that should have the origin
   * @param context the context in which to continue deserialization
   * @return the location of the origin
   * @throws JsonParseException when the query is not an object with the "origin" key
   */
  private static Location getRoomOrigin(JsonElement jsonElement, JsonDeserializationContext context)
      throws JsonParseException {
    if (!jsonElement.isJsonObject()) {
      throw new JsonParseException("expected this query to have a room object, received: " + jsonElement.toString());
    }
    JsonObject jsonObject = jsonElement.getAsJsonObject();
    JsonElement origin = jsonObject.get("origin");
    return context.deserialize(origin, Location.class);
  }
}
