import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class Server {

  private ServerSocket server;
  private Socket socket;
  private BufferedReader in;
  private PrintWriter out;

  private Server(int port) throws IOException {
    System.out.println("Using port" + port);
    this.server = new ServerSocket(port);
    System.out.println("Server started");
    System.out.println("Waiting for client...");

    this.socket = server.accept();
    System.out.println("Client accepted");

    this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    this.out = new PrintWriter(socket.getOutputStream(), true);
  }

  private void sendMessage(String message) throws IOException {
    System.out.println("sending message: " + message);
    this.out.println(message);
  }

  private String readMessages() throws IOException {
    String messages = "";
    String current = "";

    while(!current.equals("END")) {
      messages += current;
      current = this.in.readLine();
    }

    return messages;
  }

  private void closeConnection() throws IOException {
    this.out.close();
    this.in.close();
    this.socket.close();
    this.server.close();
  }

  private String calcResults(ArrayList<NumJson> numJsons) {
    Function<Integer> sum = new Sum();
    String resultString = "[ ";
    for (int idx = 0; idx < numJsons.size(); idx++) {
      NumJson numJson = numJsons.get(idx);
      Result result = new Result(numJson, numJson.calculateTotal(sum));
      resultString += result.toString();
      resultString += idx == numJsons.size() - 1 ? " ]" : ", ";
    }
    return resultString;
  }

  private ArrayList<NumJson> parseInput(String message) throws IOException {
    InputStream inputStream = new ByteArrayInputStream(message.getBytes());
    InputConverter inputConverter = new InputConverter(inputStream);
    return inputConverter.parseInput();
  }

  public static void main(String[] args) throws IOException {
    Server server = new Server(8000);
    System.out.println("server created");
    String messages = server.readMessages();
    System.out.println("client said: " + messages);
    ArrayList<NumJson> numJsons = server.parseInput(messages);
    String results = server.calcResults(numJsons);
    System.out.println("sending message: " + results);
    server.sendMessage(results);
    System.out.println("closing connection");
    server.closeConnection();
  }

}