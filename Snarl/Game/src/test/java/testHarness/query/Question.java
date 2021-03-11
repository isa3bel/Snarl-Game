package testHarness.query;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import model.*;
import testHarness.answer.Answer;
import testHarness.deserializer.*;

import java.util.HashMap;

/**
 * A question to ask about the implementation of our Snarl game.
 */
public abstract class Question {

  /**
   * Deserializes a question from the given string using the given type deserializer.
   * @param questionString the string to deserialize
   * @param questionDeserializer the deserializer to use
   * @return the Question that was deserialized
   */
  protected static <T extends Answer> T deserialize(String questionString, JsonDeserializer<T> questionDeserializer, Class<T> classOfT) {
    Gson gson = new GsonBuilder()
        .registerTypeAdapter(Answer.class, questionDeserializer)
        .registerTypeAdapter(LevelBuilder.class, new LevelBuilderDeserializer())
        .registerTypeAdapter(RoomBuilder.class, new RoomBuilderDeserializer())
        .registerTypeAdapter(HallwayBuilder.class, new HallwayBuilderDeserializer())
        .registerTypeAdapter(Location.class, new LocationDeserializer())
        .registerTypeAdapter(HashMap.class, new MockPlayerHashMapDeserializer())
        .registerTypeAdapter(Adversary.class, new AdversaryDeserializer())
        .create();
    return gson.fromJson(questionString, classOfT);
  }

  /**
   * Calculates the answer to this question.
   * @return the string answer of a question
   */
  public abstract Answer getAnswer();

}
