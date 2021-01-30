import com.google.gson.*;

import java.lang.reflect.Type;

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
        CharacterTown place = context.deserialize(paramsElement, CharacterTown.class);
        return new PlaceCharacter(place);
      case "passage-safe?":
        CharacterTown isSafe = context.deserialize(paramsElement, CharacterTown.class);
        return new PassageSafe(isSafe);
      default:
        throw new JsonParseException("unexpacted command type, received: " + commandType);
    }
  }

  private JsonObject getObject(JsonElement jsonElement) throws JsonParseException {
    try {
      return jsonElement.getAsJsonObject();
    } catch (IllegalStateException stateException) {
      throw new JsonParseException("expected json object");
    }
  }

  private String getString(JsonElement jsonElement) throws JsonParseException {
    try {
      return jsonElement.getAsString();
    } catch (IllegalStateException stateException) {
      throw new JsonParseException("expected string for command field");
    }
  }
}
