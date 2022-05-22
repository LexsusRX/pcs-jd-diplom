import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    static int port = 8989;
    static String host = "localhost";

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        try (Socket clientSocket = new Socket(host, port);
             BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {
            System.out.println("Подключено " + clientSocket.getRemoteSocketAddress() + "\n");

            System.out.println(in.readLine());
            String userData = scanner.nextLine();
            out.println(userData);
            System.out.println(in.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}