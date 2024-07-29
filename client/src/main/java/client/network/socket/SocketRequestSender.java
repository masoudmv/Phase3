package client.network.socket;

import com.fasterxml.jackson.databind.ObjectMapper;
import shared.request.Request;
import shared.response.Response;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

public class SocketRequestSender {
    private final Scanner scanner;
    private final Socket socket;
    private final PrintStream printStream;
    private final ObjectMapper objectMapper;

    public SocketRequestSender() {
        try {
            this.socket = new Socket("127.0.0.1", 8080);
            printStream = new PrintStream(socket.getOutputStream());
            scanner = new Scanner(socket.getInputStream());
            objectMapper = new ObjectMapper();
        } catch (IOException e) {
            System.out.println("could not open a new socket");
            throw new RuntimeException(e);
        }
    }


    public Response sendRequest(Request request) throws IOException {
        try {
            printStream.println(objectMapper.writeValueAsString(request));
            return objectMapper.readValue(scanner.nextLine(), Response.class);
        } catch (Exception e) {
            System.out.println(e);
            close();
            throw e;
        }
    }

    public void close() {
        try {
            scanner.close();
            printStream.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
