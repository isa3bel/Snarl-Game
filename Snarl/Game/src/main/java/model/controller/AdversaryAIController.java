package model.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import model.level.Location;
import testHarness.deserializer.LocationDeserializer;
import view.View;

import java.lang.reflect.Type;

/**
 * Controller for an adversary that makes decisions using the AdversaryAI.
 */
public class AdversaryAIController implements Controller {

  private final Type aiClass;
  private Location nextMove;
  private final Gson gson = new GsonBuilder()
      .registerTypeAdapter(Location.class, new LocationDeserializer())
      .create();

  public AdversaryAIController(Type aiClass) {
    this.aiClass = aiClass;
  }

  @Override
  public Location getNextMove() {
    return nextMove;
  }

  @Override
  public void update(View view) {
    String adversaryView = view.toString();
    AdversaryAI adversaryAI = this.gson.fromJson(adversaryView, aiClass);
    this.nextMove = adversaryAI.calculateNextMove();
  }
}
