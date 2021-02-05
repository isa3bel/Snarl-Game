package src;

import java.io.*;
import java.net.Socket;

class TcpConnection implements Tcp {

  private Socket socket;
  private BufferedReader in;
  private PrintWriter out;

  TcpConnection(String host, int port) throws IOException {
    this.socket = new Socket(host, port);
    this.socket.setSoTimeout(5000);

    InputStreamReader inputReader = new InputStreamReader(this.socket.getInputStream());
    this.in = new BufferedReader(inputReader);

    OutputStream output = this.socket.getOutputStream();
    this.out = new PrintWriter(output, true);
  }

  /**
   * Sends the given message to the server.
   * @param message the message to send
   */
  public void sendMessage(String message) {
    this.out.println(message);
  }

  public String readMessage() throws IOException {
    return this.in.readLine();
  }

  /**
   * Reads the Response object string from the server.
   * @return the response from the json server
   * @throws IOException if the socket is closed (?)
   */
  public String readResponse() throws IOException {
    String response = "";
    boolean hasInvalid = false;
    boolean invalidListClosed = false;
    boolean hasResponse = false;
    boolean objectClosed = false;

    while (!hasInvalid || !invalidListClosed || !hasResponse || !objectClosed) {
      response += this.in.readLine();
      response.trim();

      // hypothetically we don't know if "invalid" is the json object key or if it is some
      // String value in the Json somewhere BUT  we know that the only Strings in the
      // response are _inside_ the invalid key, so it works provided the server sent
      // us valid data... which we are going to assume is the case
      hasInvalid = response.contains("invalid");
      invalidListClosed = response.contains("]");
      hasResponse = response.contains("response");
      objectClosed = response.endsWith("}");
    }

    return response;
  }

  /**
   * Closes the socket connection with the server.
   * @throws IOException sometimes?
   */
  public void closeConnection() {
    try {
      this.in.close();
      this.out.close();
      this.socket.close();
    }
    catch (IOException exception) {
      System.out.println("failed to closed server connection");
    }
  }
}
