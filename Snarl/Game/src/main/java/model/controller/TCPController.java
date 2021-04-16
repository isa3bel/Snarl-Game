package model.controller;

import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import model.remote.SocketReader;
import model.characters.Character;
import model.level.Location;
import testHarness.deserializer.LocationDeserializer;
import view.PlayerUpdateView;
import view.View;

/**
 * Controller for an actor that connects to a client through a socket.
 */
public class TCPController implements Controller {

  private final SocketReader socketReader;
  private final Gson gson = new GsonBuilder()
      .registerTypeAdapter(Location.class, new LocationDeserializer())
      .create();

  public TCPController(SocketReader socketReader) {
    this.socketReader = socketReader;
  }

  @Override
  public View getDefaultView(Character character) {
    return new PlayerUpdateView(character, null);
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
      // player is disconnected ?
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
