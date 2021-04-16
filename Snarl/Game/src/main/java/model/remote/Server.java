package model.remote;

import java.io.*;
import java.net.*;
import java.util.*;
import model.GameManager;
import model.builders.*;
import model.controller.*;
import model.level.*;
import model.observer.LocalObserver;
import model.ruleChecker.GameStatus;

public class Server {

  private final ServerSocket server;
  private final HashMap<String, SocketReader> playerSockets = new HashMap<>();

  Server(int port, int waitTime) throws IOException {
    // TODO: hostname?
    this.server = new ServerSocket(port);
    this.server.setSoTimeout(waitTime * 1000);
  }

  /**
   * Add the given player names to the game manager.
   * @param gmBuilder the game manager builder to add the players to
   * @param maxNumPlayers the maximum number of players that the game can have
   * @throws IOException if there is an IO error
   */
  private void registerPlayers(GameManagerBuilder gmBuilder, int maxNumPlayers)
      throws IOException, IllegalStateException {
    int numPlayers = 0;
    while (numPlayers < maxNumPlayers) {
      Socket playerSocket;
      try {
        playerSocket = server.accept();
      } catch (SocketTimeoutException exception) {
        break;
      }
      SocketReader socketReader = new SocketReader(playerSocket);
      Controller c = new TCPController(socketReader);

      String welcome = "{ \"type\": \"welcome\",\n\"info\": \"Edhelen\"\n}";
      socketReader.sendMessage(welcome);
      socketReader.sendMessage("\"name\"");

      String userName = socketReader.readMessage();
      this.playerSockets.put(userName, socketReader);
      System.out.println("User " + userName + " registered.");

      numPlayers++;
      gmBuilder.addPlayer(userName, c);
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
    GameManagerBuilder gmBuilder = new GameManagerBuilder(1, levels);

    Server server = new Server(arguments.port, arguments.waitTime);
    server.registerPlayers(gmBuilder, arguments.maxNumPlayers);
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
}
