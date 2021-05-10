package pa.proj.word_games.server;

import pa.proj.word_games.server.threads.ClientThread;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
    private static Server instance = null;
    private static final int PORT = 2021;
    private ServerSocket serverSocket;

    private List<ClientThread> clientThreads;

    private Server() { }

    public static Server getInstance() {
        if (instance == null)
            instance = new Server();

        return instance;
    }

    public void initialize() throws IOException {
        serverSocket = new ServerSocket(PORT);
        clientThreads = new ArrayList<>();
    }

    /**
     * Primeste cereri de conexiuni de la potentiali clienti. Pentru fiecare client acceptat, se creeaza un thread.
     * @throws IOException
     */
    public void start() throws IOException {
        ClientThread clientThread;
        while(true) {
            System.out.println("Astept un client...");
            Socket socket = serverSocket.accept();
            System.out.println("S-a conectat un client...");

            // Creez un thread nou pentru client
            clientThread = new ClientThread(socket);
            clientThreads.add(clientThread);
            clientThread.start();
        }
    }

    /**
     * Inchide socket-ul server-ului.
     * @throws IOException
     */
    public void closeServerSocket() throws IOException {
        if(!serverSocket.isClosed())
            serverSocket.close();
    }
}
