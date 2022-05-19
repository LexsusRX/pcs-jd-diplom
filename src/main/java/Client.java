import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.Scanner;

public class Client {
    static int port = 8989;
    static String host = "localhost";

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        try {
            BufferedReader in;
            PrintWriter out;

            while (true) {
                Socket clientSocket = new Socket(host, port);
                System.out.println("Подключено " + clientSocket.getRemoteSocketAddress() + "\n");
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                out = new PrintWriter(clientSocket.getOutputStream(), true);

                System.out.println(in.readLine());
                String userData = scanner.nextLine();
                out.println(userData);
                System.out.println(in.readLine());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}