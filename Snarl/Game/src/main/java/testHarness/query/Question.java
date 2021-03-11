package testHarness.query;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import model.builders.HallwayBuilder;
import model.builders.LevelBuilder;
import model.builders.RoomBuilder;
import model.characters.Adversary;
import model.level.Location;
import testHarness.answer.Answer;
import testHarness.deserializer.*;

/**
 * A question to ask about the implementation of our Snarl game.
 */
public abstract class Question {

  /**
   * Deserializes a question from the given string using the given type deserializer.
   * @param json the string to deserialize
   * @param deserializer the deserializer to use
   * @param classOfT the type to deserialize to
   * @return the Question that was deserialized
   */
  protected static <T extends Answer> T deserialize(String json, JsonDeserializer<T> deserializer, Class<T> classOfT) {
    Gson gson = new GsonBuilder()
        .registerTypeAdapter(classOfT, deserializer)
        .registerTypeAdapter(LevelBuilder.class, new LevelBuilderDeserializer())
        .registerTypeAdapter(RoomBuilder.class, new RoomBuilderDeserializer())
        .registerTypeAdapter(HallwayBuilder.class, new HallwayBuilderDeserializer())
        .registerTypeAdapter(Location.class, new LocationDeserializer())
        .registerTypeAdapter(MockPlayer.class, new MockPlayerDeserializer())
        .registerTypeAdapter(Adversary.class, new AdversaryDeserializer())
        .create();
    return gson.fromJson(json, classOfT);
  }

  /**
   * Calculates the answer to this question.
   * @return the string answer of a question
   */
  public abstract Answer getAnswer();

}
