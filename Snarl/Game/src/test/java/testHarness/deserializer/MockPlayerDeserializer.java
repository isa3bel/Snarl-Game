package testHarness.deserializer;

import com.google.gson.*;
import model.level.Location;
import testHarness.query.MockPlayer;

import java.lang.reflect.Type;

public class MockPlayerDeserializer implements JsonDeserializer<MockPlayer> {

  @Override
  public MockPlayer deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context)
      throws JsonParseException {
    JsonObject playerJson = jsonElement.getAsJsonObject();
    String name = playerJson.get("name").getAsString();
    Location location = context.deserialize(playerJson.get("position"), Location.class);
    return new MockPlayer(location, 1, name);
  }
}
