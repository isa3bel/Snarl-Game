package testHarness;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import model.LevelBuilder;

import java.util.Scanner;

public class TestHarness {

  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);

    Gson gson = new GsonBuilder().registerTypeAdapter(LevelBuilder.class,
        new LevelBuilderDeserializer()).create();
  }
}
