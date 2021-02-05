package src;

import java.io.IOException;

public interface Tcp {

  void sendMessage(String message);

  String readMessage() throws IOException;

  String readResponse() throws IOException;

  void closeConnection();

}
