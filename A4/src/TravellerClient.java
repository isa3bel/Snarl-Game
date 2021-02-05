package src;

import java.io.IOException;

/**
 * Runs the traveller client by reading input from System.in and passing commands to server.
 */
public class TravellerClient {

  /**
   * Runs this program.
   * @param args args to program
   * @throws IOException is System.in is closed
   */
  public static void main(String[] args) throws IOException {
    Tcp tcpConnection = setupTcp(args);
    DoCommand function = new DoCommand(tcpConnection);

    InputReader<Command> reader = new InputReader<>(System.in, Command.class, new CommandDeserialization(), function);
    reader.parseInput();

    tcpConnection.closeConnection();
  }
  /**
   * Entry point for the client to connect with the server
   * @param args the user's commandline arguments
   * @return a ServerCommunicator
   * @throws IOException if the user specifies more than 3 params
   */
  private static Tcp setupTcp(String[] args) {
    String ipAddress = args.length < 1 ? "127.0.0.1" : args[0];
    int port = args.length < 2 ? 8000 : Integer.parseInt(args[1]);
    String name = args.length < 3 ? "Glorifrir Flintshoulder" : args[2];

    Tcp tcpConnection = null;
    try {
      // TODO: handle this
      tcpConnection = new TcpConnection(ipAddress, port);
    } catch (IOException ioException) {
      ioException.printStackTrace();
    }
    registerName(tcpConnection, name);

    return tcpConnection;
  }

  private static void registerName(Tcp tcpConnection, String name) {
    tcpConnection.sendMessage(name);
    String sessionId = "";
    try {
      // TODO: handle this
      sessionId = tcpConnection.readMessage();
    } catch (IOException ioException) {
      ioException.printStackTrace();
    }
    System.out.println("[\"the server will call me\", " + sessionId + " ]");
  }
}
