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
            BufferedReader in;
            PrintWriter out;

            while (true) {
                Socket clientSocket = serverSocket.accept(); // ждем подключения от client
                System.out.println("Установлено подключение с " + serverSocket.getLocalSocketAddress());
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                out = new PrintWriter(clientSocket.getOutputStream(), true);

                out.println("Введите слово для поиска в формате <<слово>> (для выхода введите *end*) : ");
                String clientData = in.readLine();
                if (clientData != null) {
                    System.out.println("Введены данные: " + clientData);
                    if ("*end*".equals(clientData)) {
                        System.out.println("Соединение закрыто");
                        out.println("Соединение закрыто");
                        out.close();
                        in.close();
                        serverSocket.close();
                        break;
                    } else if (clientData.startsWith("<<") && clientData.endsWith(">>")) {
                        String clientWord = clientData.substring(2, clientData.length() - 2);
                        System.out.println("Выполняется поиск слова: " + clientWord);
                        List<PageEntry> searchResult = searchEngine.search(clientWord);
                        GsonBuilder builder = new GsonBuilder();
                        Gson gson = builder.create();
                        String resultList = String.valueOf(searchResult
                                .stream()
                                .map(gson::toJson)
                                .map(Object::toString).toList());
                        out.println(resultList + "\n");
                        clientSocket.close();
                    }
                    continue;
                }
                continue;
            }
        } catch (SocketException s) {
            System.out.println(s);
        }
    }
}