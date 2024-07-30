//package client.network.socket;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import shared.request.Request;
//import shared.response.Response;
//
//import java.io.IOException;
//import java.io.PrintStream;
//import java.net.Socket;
//import java.util.Scanner;
//
//public class SocketResponseSender {
//    private final Scanner scanner;
//    private final Socket socket;
//    private final PrintStream printStream;
//    private final ObjectMapper objectMapper;
//
//    public SocketResponseSender(Socket socket, PrintStream printStream, Scanner scanner, ObjectMapper objectMapper) {
//        this.socket = socket;
//        this.printStream = printStream;
//        this.scanner = scanner;
//        this.objectMapper = objectMapper;
//    }
//
//    public Request getRequest() {
//        try {
//            if (scanner.hasNextLine()) {
//                String s = scanner.nextLine();
////                System.out.println("Read line: " + s);
//                Request request = objectMapper.readValue(s, Request.class);
////                System.out.println("Deserialized Request: " + request);
//                return request;
//            } else {
////                System.out.println("No line found");
//                return null;
//            }
//        } catch (Exception e) {
////            System.out.println("Exception occurred: " + e.getMessage());
//            e.printStackTrace(); // Print stack trace for more details
//            return null;
//        }
//    }
//
//    public void sendResponse(Response response) {
//        try {
//            printStream.println(objectMapper.writeValueAsString(response));
//        } catch (Exception e) {
//            System.out.println("asd");
//        }
//    }
//
//    public void close() {
//        try {
//            scanner.close();
//            printStream.close();
//            socket.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//}
