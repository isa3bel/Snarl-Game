package model.remote;

import java.io.*;
import java.net.*;
import java.util.*;

import com.google.gson.Gson;
import model.GameManager;
import model.builders.*;
import model.controller.*;
import model.level.*;
import model.observer.LocalObserver;
import model.ruleChecker.GameStatus;
import model.ruleChecker.GhostMoveValidator;
import model.ruleChecker.ZombieMoveValidator;

public class Server {

  private final ServerSocket server;
  private final HashMap<String, SocketReader> playerSockets = new HashMap<>();
  private final Gson gson = new Gson();

  Server(int port, int waitTime) throws IOException {
    // TODO: hostname?
    this.server = new ServerSocket(port);
    this.server.setSoTimeout(waitTime * 1000);
  }

  /**
   * Connects to a client socket.
   * @return the socket reader for the client's socket
   * @throws IOException if the server fails to accept a socket
   */
  private SocketReader connectToClient() throws IOException {
    Socket playerSocket;
    try {
      playerSocket = server.accept();
    } catch (SocketTimeoutException exception) {
      return null;
    }
    return new SocketReader(playerSocket);
  }

  /**
   * Handshake and welcome to a new actor client.
   * @param gmBuilder the builder to add the actor to
   * @param socketReader the socket reader for the client
   * @return the number of players registered in this handshake
   * @throws IOException if there is an IO error
   */
  private int handshake(GameManagerBuilder gmBuilder, SocketReader socketReader) throws IOException {
    socketReader.sendMessage("{ \"type\": \"welcome\",\n\"info\": \"Edhelen\"\n}");
    socketReader.sendMessage("\"name\"");

    IntroductionMessage introduction = gson.fromJson(socketReader.readMessage(), IntroductionMessage.class);
    switch (introduction.type) {
      case "player":
        this.playerSockets.put(introduction.name, socketReader);
        System.out.println("Player " + introduction.name + " registered.");
        Controller tcpController = new TCPController(socketReader);
        gmBuilder.addPlayer(introduction.name, tcpController);
        return 1;
      case "zombie":
        Controller zombieTcpController = new AdversaryTCPController(socketReader,
            loc -> new ZombieMoveValidator(null, loc));
        gmBuilder.addZombie(zombieTcpController);
        System.out.println("registered a zombie");
        break;
      case "ghost":
        Controller ghostTcpController = new AdversaryTCPController(socketReader,
            loc -> new GhostMoveValidator(null, loc));
        gmBuilder.addGhost(ghostTcpController);
        System.out.println("registered a ghost");
        break;
      default:
        socketReader.sendMessage("{\"error\": \"unsupported actor type\"}");
    }
    return 0;
  }

  /**
   * Add the given player names to the game manager.
   * @param gmBuilder the game manager builder to add the players to
   * @param maxNumPlayers the maximum number of players that the game can have
   * @throws IOException if there is an IO error
   */
  private void registerActors(GameManagerBuilder gmBuilder, int maxNumPlayers)
      throws IOException, IllegalStateException {
    int numPlayers = 0;
    while (numPlayers < maxNumPlayers) {
      SocketReader socketReader = this.connectToClient();
      if (socketReader == null) break;
      numPlayers += this.handshake(gmBuilder, socketReader);
    }

    if (numPlayers == 0) {
      throw new IllegalStateException("No players joined this game.");
    }
  }

  /**
   * Closes the socket connection with the server.
   */
  public void closeConnection() {
    try {
      this.server.close();
      this.playerSockets.values().forEach(SocketReader::closeConnection);
    }
    catch (IOException exception) {
      System.out.println("failed to close server connection");
    }
  }

  /**
   * Actually runs the game
   * @param args the args that are required to set up the game
   * @throws IOException if there is an IO error
   */
  public static void main(String[] args) throws IOException {
    ArgumentParser arguments = new ArgumentParser(args);

    Level[] levels = new LevelFileReader(arguments.levelFile).getLevels();
    GameManagerBuilder gmBuilder = new GameManagerBuilder(0, levels);

    Server server = new Server(arguments.port, arguments.waitTime);
    server.registerActors(gmBuilder, arguments.maxNumPlayers);
    GameManager gameManager = gmBuilder.build();

    if (arguments.showObserverView) gameManager.addObserver(new LocalObserver());
    gameManager.updatePlayers();
    playGame(gameManager);
    server.closeConnection();
  }

  /**
   * Play the given game manager across the given server.
   * @param gameManager the game to play
   */
  private static void playGame(GameManager gameManager) {
    GameStatus status;
    do {
      status = gameManager.doRound();
    } while (status.equals(GameStatus.PLAYING) || status.equals(GameStatus.ADVANCE));
  }

  private static class IntroductionMessage {

    String name;
    String type;

    private IntroductionMessage(String name, String type) {
      this.name = name;
      this.type = type;
    }
  }
}
