package testHarness.deserializer;

import com.google.gson.*;

import model.builders.HallwayBuilder;
import model.builders.LevelBuilder;
import model.level.Location;
import model.builders.RoomBuilder;

import java.lang.reflect.Type;

public class LevelBuilderDeserializer implements JsonDeserializer<LevelBuilder> {

  @Override
  public LevelBuilder deserialize(JsonElement jsonElement, Type classType, JsonDeserializationContext context)
      throws JsonParseException {
    JsonObject jsonObject = getObject(jsonElement);
    JsonArray rooms = getArray(jsonObject.get("rooms"));
    JsonArray hallways = getArray(jsonObject.get("hallways"));
    JsonArray objects = getArray(jsonObject.get("objects"));

    LevelBuilder level = new LevelBuilder();
    addRoomsToLevel(level, rooms, context);
    addHallwayToLevel(level, hallways, context);
    addObjectsToLevel(level, objects, context);

    return level;
  }

  /**
   * Adds the given level objects to the level.
   * @param level the in which to add these objects
   * @param array the array of level objects to add
   * @param context the context in which to deserialize the objects
   * @throws IllegalArgumentException if the type is not "exit" or "key"
   */
  private void addObjectsToLevel(LevelBuilder level, JsonArray array, JsonDeserializationContext context)
    throws IllegalArgumentException {
    for (JsonElement element : array) {
      JsonObject object = getObject(element);
      String type = getType(object);
      switch (type) {
        case "key":
          level.addKey(context.deserialize(object.get("position"), Location.class));
          continue;
        case "exit":
          level.addExit(context.deserialize(object.get("position"), Location.class));
          continue;
        default:
          throw new IllegalArgumentException("invalid level object - only supported types [key, exit], given: " + type);
      }
    }
  }

  /**
   * Add rooms from the given json to the level.
   * @param levelBuilder the level to add the rooms to
   * @param array the array of rooms to add
   * @param context the context in which to deserialize the rooms
   */
  private void addRoomsToLevel(LevelBuilder levelBuilder, JsonArray array, JsonDeserializationContext context) {
    for (JsonElement element : array) {
      RoomBuilder room = context.deserialize(element, RoomBuilder.class);
      levelBuilder.addRoom(room);
    }
  }

  /**
   * Add hallways from the given json to the level.
   * @param levelBuilder the level to add the hallways to
   * @param array the array of hallways to add
   * @param context the context in which to deserialize the rooms
   */
  private void addHallwayToLevel(LevelBuilder levelBuilder, JsonArray array, JsonDeserializationContext context) {
    for (JsonElement element : array) {
      HallwayBuilder hallway = context.deserialize(element, HallwayBuilder.class);
      levelBuilder.addHallway(hallway);
    }
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

  /**
   * Gets the JsonElement as an array if possible.
   * @param jsonElement the element to
   * @return the JsonArray
   * @throws JsonParseException if the given element is not a JsonArray
   */
  private static JsonArray getArray(JsonElement jsonElement) throws JsonParseException {
    if (!jsonElement.isJsonArray()) {
      throw new JsonParseException("expected an array, given: " + jsonElement.toString());
    }
    return (JsonArray) jsonElement;
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
