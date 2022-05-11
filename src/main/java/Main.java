import java.io.*;

public class Main {

    public static void main(String[] args) throws IOException {
        String workFolder = "pdfs/";
        BooleanSearchEngine engine = new BooleanSearchEngine(new File(workFolder));
        Server server = new Server(engine);
        server.startServer();
    }
}