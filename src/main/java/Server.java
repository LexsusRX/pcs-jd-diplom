import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.List;


public class Server {
    protected SearchEngine searchEngine;
    private static final int port = 8989;

    public Server(SearchEngine searchEngine) {
        this.searchEngine = searchEngine;
    }

    public void startServer() throws IOException {
        try (ServerSocket serverSocket = new ServerSocket(port)) {

            while (true) {
                try (Socket clientSocket = serverSocket.accept(); // ждем подключения от client
                     BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                     PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {
                    System.out.println("Установлено подключение с " + serverSocket.getLocalSocketAddress());

                    out.println("Введите слово для поиска:");
                    String clientData = in.readLine();
                    if (clientData != null) {
                        List<PageEntry> searchResult = searchEngine.search(clientData);
                        GsonBuilder builder = new GsonBuilder();
                        Gson gson = builder.create();
                        String resultList = String.valueOf(searchResult
                                .stream()
                                .map(gson::toJson)
                                .map(Object::toString).toList());
                        out.println(resultList + "\n");
                        clientSocket.close();
                    }
                }
            }
        } catch (SocketException s) {
            System.out.println(s);
        }
    }
}