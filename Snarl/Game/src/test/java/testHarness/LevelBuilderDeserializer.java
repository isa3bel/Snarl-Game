package testHarness;

import com.google.gson.*;
import model.LevelBuilder;
import model.RoomBuilder;

import java.lang.reflect.Type;

public class LevelBuilderDeserializer implements JsonDeserializer<LevelBuilder> {

  @Override
  public LevelBuilder deserialize(JsonElement jsonElement, Type classType, JsonDeserializationContext context)
      throws JsonParseException {
    if (!jsonElement.isJsonObject()) {
      throw new JsonParseException("room json must be an object, given: " + jsonElement.toString());
    }
    JsonObject jsonObject = (JsonObject) jsonElement;

    String type = getType(jsonObject);
    switch (type) {
      case "room":
        RoomBuilder roomBuilder = context.deserialize(jsonObject, RoomBuilder.class);
        return new LevelBuilder().addRoom(roomBuilder);
      default:
        throw new JsonParseException("expected to parse one of type [room], given: " + type);
    }
  }

  /**
   * Gets the type of this object to parse for the level.
   * @param jsonObject the room object
   * @return the type field of that object
   * @throws JsonParseException if type is not a string
   */
  private static String getType(JsonObject jsonObject) throws JsonParseException {
    String type;
    try {
      type = jsonObject.get("type").getAsString();
    }
    catch (ClassCastException exception) {
      throw new JsonParseException("expected a type to be a string, got: " + jsonObject.get("type").toString());
    }
    return type;
  }
}
