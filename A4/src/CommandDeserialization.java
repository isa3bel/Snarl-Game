package src;

import com.google.gson.*;
import java.lang.reflect.Type;

/**
 * Processes and deserializes the json commands from System.in
 */
public class CommandDeserialization implements JsonDeserializer<Command> {
  @Override
  public Command deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context)
      throws JsonParseException {
    JsonObject object = this.getObject(jsonElement);
    JsonElement commandElement = object.get("command");
    String commandType = this.getString(commandElement);
    JsonElement paramsElement = object.get("params");

    switch (commandType) {
      case "roads":
        Road[] roads = context.deserialize(paramsElement, Road[].class);
        return new MakeTown(roads);
      case "place":
        return context.deserialize(paramsElement, PlaceCharacter.class);
      case "passage-safe?":
        return context.deserialize(paramsElement, PassageSafe.class);
      default:
        throw new JsonParseException("unexpected command type, received: " + commandType);
    }
  }

  /**
   * Gets the given json element as an object with an error wrapper
   * @param jsonElement
   * @return json object version of element
   * @throws JsonParseException if not an object
   */
  private JsonObject getObject(JsonElement jsonElement) throws JsonParseException {
    try {
      return jsonElement.getAsJsonObject();
    } catch (IllegalStateException stateException) {
      throw new JsonParseException("expected json object");
    }
  }

  /**
   * Gets the given json element as a string with an error wrapper
   * @param jsonElement
   * @return string version of element
   * @throws JsonParseException if not a string
   */
  private String getString(JsonElement jsonElement) throws JsonParseException {
    try {
      return jsonElement.getAsString();
    } catch (IllegalStateException stateException) {
      throw new JsonParseException("expected string for command field");
    }
  }
}
