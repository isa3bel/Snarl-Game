import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class TravellerClient {

  private Socket socket;
  private BufferedReader in;
  private PrintWriter out;

  TravellerClient(String host, int port) throws IOException {
    this.socket = new Socket(host, port);

    InputStreamReader inputReader = new InputStreamReader(this.socket.getInputStream());
    this.in = new BufferedReader(inputReader);

    OutputStream output = this.socket.getOutputStream();
    this.out = new PrintWriter(output, true);
  }

  private void sendMessageToServer(String message) throws IOException {
    System.out.println("sending message: " + message);
    this.out.println(message);
  }

  private String readMessages() throws IOException {
    String messages = "";
    String current = "";

    while(current != null && !current.equals("END")) {
      messages += current;
      current = this.in.readLine();
    }

    return messages;
  }

  private void closeConnection() throws IOException {
    this.in.close();
    this.out.close();
    this.socket.close();
  }

  public static void main(String[] args) throws IOException {
    TravellerClient client = new TravellerClient("localhost", 8000);
    System.out.println("client created");

    // read from STDIN, convert JSON to strings for client
    InputReader<Command> reader = new InputReader<>(System.in, Command.class);
    ArrayList<Command> commands = reader.parseInput();

    commands.stream().forEach(command -> {
      String commandMessage = command.toString();
      try {
        client.sendMessageToServer(commandMessage);
      } catch (IOException ioException) {
        System.out.println(ioException);
      }
    });

    String received = client.readMessages();
    System.out.println("received from server: " + received);
    System.out.println("closing connection");
    client.closeConnection();
  }

}
