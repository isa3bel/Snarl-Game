package testHarness;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import model.HallwayBuilder;
import model.LevelBuilder;
import model.Location;
import model.RoomBuilder;

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
  protected static Question deserialize(String questionString, JsonDeserializer<Question> questionDeserializer) {
    Gson gson = new GsonBuilder()
        .registerTypeAdapter(Question.class, questionDeserializer)
        .registerTypeAdapter(LevelBuilder.class, new LevelBuilderDeserializer())
        .registerTypeAdapter(RoomBuilder.class, new RoomBuilderDeserializer())
        .registerTypeAdapter(HallwayBuilder.class, new HallwayBuilderDeserializer())
        .registerTypeAdapter(Location.class, new LocationDeserializer())
        .create();
    return gson.fromJson(questionString, Question.class);
  }

  /**
   * Calculates the answer to this question.
   * @return the string answer of a question
   */
  public abstract String getAnswer();

  /**
   * Given a location converts it to a string following specific format.
   * @param location the location to convert
   * @return a string representing the converted location
   */
  protected String locationToString(Location location) {
    return "[" + location.row + ", " + location.column + "]";
  }

}
