package model.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import model.characters.Character;
import model.level.Location;
import model.ruleChecker.MoveValidator;
import testHarness.deserializer.LocationDeserializer;
import view.AdversaryAIView;
import view.View;

import java.lang.reflect.Type;
import java.util.function.Function;

/**
 * Controller for an adversary that makes decisions using the AdversaryAI.
 */
public class AdversaryAIController implements Controller {

  private final Type aiClass;
  private Location nextMove;
  private Function<Location, MoveValidator> moveValidatorCreator;
  private final Gson gson = new GsonBuilder()
      .registerTypeAdapter(Location.class, new LocationDeserializer())
      .create();

  public AdversaryAIController(Type aiClass, Function<Location, MoveValidator> moveValidatorCreator) {
    this.aiClass = aiClass;
    this.moveValidatorCreator = moveValidatorCreator;
  }

  @Override
  public View getDefaultView(Character character) {
    return new AdversaryAIView(character.getCurrentLocation(), moveValidatorCreator);
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
