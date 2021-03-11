package testHarness.deserializer;

import com.google.gson.*;
import model.Location;
import testHarness.query.MockPlayer;

import java.lang.reflect.Type;
import java.util.HashMap;

public class MockPlayerHashMapDeserializer implements JsonDeserializer<HashMap<String, MockPlayer>> {

  @Override
  public HashMap<String, MockPlayer> deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context)
      throws JsonParseException {
    if (!jsonElement.isJsonArray()) {
      throw new JsonParseException("expected players to be a json array of type (actor-position-list)");
    }
    JsonArray playerArray = jsonElement.getAsJsonArray();
    if (playerArray.size() == 0 || playerArray.size() > 4) {
      throw new JsonParseException("expected between  1 and 4 players, given: " + playerArray.size());
    }

    HashMap<String, MockPlayer> players = new HashMap<>();
    for (int idx = 1; idx < playerArray.size(); idx++) {
      JsonObject playerJson = playerArray.get(idx).getAsJsonObject();
      String name = playerJson.get("name").getAsString();
      Location location = context.deserialize(playerJson.get("position"), Location.class);
      players.put(name, new MockPlayer(location, idx));
    }
    return players;
  }
}
