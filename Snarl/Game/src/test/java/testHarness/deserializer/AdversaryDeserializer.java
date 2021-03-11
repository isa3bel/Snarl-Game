package testHarness.deserializer;

import com.google.gson.*;
import model.Adversary;
import model.Ghost;
import model.Location;
import model.Zombie;

import java.lang.reflect.Type;

/**
 * Deserializes and adversary from the "actor-position" object in the test harness json input.
 */
public class AdversaryDeserializer implements JsonDeserializer<Adversary> {

  @Override
  public Adversary deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context)
      throws JsonParseException {
    if (!jsonElement.isJsonObject()) {
      throw new JsonParseException("expected adversary to be a json object matching (actor-position)");
    }
    JsonObject adversaryJson = jsonElement.getAsJsonObject();
    Location location = context.deserialize(adversaryJson.get("position"), Location.class);

    switch (adversaryJson.get("type").getAsString()) {
      case "zombie":
        return new Zombie(location);
      case "ghost":
        return new Ghost(location);
      default:
        throw new JsonParseException("adversary type must be one of ['zombie', 'ghost'], given:  " +
            adversaryJson.get("type").getAsString());
    }
  }
}
