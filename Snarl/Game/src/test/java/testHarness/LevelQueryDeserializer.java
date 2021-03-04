package testHarness;

import com.google.gson.*;
import model.Level;
import model.LevelBuilder;
import model.Location;

import java.lang.reflect.Type;

/**
 * A deserializer for a Question.
 */
public class LevelQueryDeserializer implements JsonDeserializer<Question> {

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

    return new LevelQuery(level, location);
  }
}
