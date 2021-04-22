package model.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import model.characters.Character;
import model.level.Location;
import model.remote.SocketReader;
import model.ruleChecker.MoveValidator;
import testHarness.deserializer.LocationDeserializer;
import view.AdversaryAIView;
import view.View;

import java.io.IOException;
import java.util.function.Function;

public class AdversaryTCPController implements Controller {

  private final SocketReader socketReader;
  private final Function<Location, MoveValidator> moveValidatorCreator;
  private final Gson gson = new GsonBuilder()
      .registerTypeAdapter(Location.class, new LocationDeserializer())
      .create();

  public AdversaryTCPController(SocketReader socketReader, Function<Location, MoveValidator> moveValidatorCreator) {
    this.socketReader = socketReader;
    this.moveValidatorCreator = moveValidatorCreator;
  }

  @Override
  public View getDefaultView(Character character) {
    return new AdversaryAIView(character.getCurrentLocation(), moveValidatorCreator);
  }

  @Override
  public Location getNextMove() {
    String response;
    this.socketReader.sendMessage("\"move\"");
    try {
      response = this.socketReader.readMessage();
    } catch (IOException ioException) {
      return null;
    }

    if (response == null) {
      // adversary is disconnected
      return null;
    }

    TypeMove typeMove = this.gson.fromJson(response, TypeMove.class);
    return typeMove.to;
  }

  @Override
  public void update(View view) {
    this.socketReader.sendMessage(view.toString());
  }

  private static class TypeMove {
    private final Location to;

    private TypeMove(Location to) {
      this.to = to;
    }
  }
}
