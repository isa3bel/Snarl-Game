package testHarness;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import model.LevelBuilder;
import model.Location;
import model.RoomBuilder;

import java.util.Scanner;

public class TestHarness {


  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);
    Gson gson = new GsonBuilder()
        .registerTypeAdapter(Question.class, new QuestionDeserializer())
        .registerTypeAdapter(LevelBuilder.class, new LevelBuilderDeserializer())
        .registerTypeAdapter(RoomBuilder.class, new RoomBuilderDeserializer())
        .registerTypeAdapter(Location.class, new LocationDeserializer())
        .create();

    StringBuilder stringBuilder = new StringBuilder();
    while (scanner.hasNextLine()) {
      stringBuilder.append(scanner.nextLine());
    }
    Question nextUnit = gson.fromJson(stringBuilder.toString(), Question.class);
    System.out.print(nextUnit.getAnswer());
  }
}
