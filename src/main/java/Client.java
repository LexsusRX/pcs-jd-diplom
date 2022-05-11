import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    static int port = 8989;
    static String host = "localhost";

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        try {
            Socket clientSocket = new Socket(host, port);
            System.out.println("Подключено " + clientSocket.getRemoteSocketAddress() + "\n");

            BufferedReader in;
            PrintWriter out;

            while (true) {
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                out = new PrintWriter(clientSocket.getOutputStream(), true);

                System.out.println(in.readLine());
                String userData = scanner.nextLine();
                if (userData != null) {
                    if (userData.equals("*end*")) {
                        out.println("*end*");
                        System.out.print("Сеанс закончен");
                        in.close();
                        out.close();
                        break;
                    } else {
                        out.println(userData);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}