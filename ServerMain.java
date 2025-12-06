package online;

public class ServerMain {

    public static void main(String[] args) {
        System.out.println("=== SERVIDOR INICIADO ===");
        ServerThread server = new ServerThread();
        server.start();
    }
}

