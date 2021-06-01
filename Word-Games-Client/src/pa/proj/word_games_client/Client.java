package pa.proj.word_games_client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
    private static final String SERVER_ADDRESS = "127.0.0.1";
    private static final int SERVER_PORT = 2021;
    private Socket socket = null;

    public Client() throws IOException {
        System.out.println("Ma conectez la server...");
        socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
        System.out.println("M-am conectat la server...");
    }

    /**
     * Citeste un mesaj trimis de server.
     *
     * @return Mesajul trimis de server.
     */
    public String readServerMessage() throws IOException {
        // Citesc si returneaz mesajul
        BufferedReader in = new BufferedReader(
                new InputStreamReader(socket.getInputStream())
        );
        return in.readLine();
    }

    /**
     * Trimite un request catre server.
     *
     * @param request Request-ul care va fi trimis.
     */
    public void sendRequest(String request) throws IOException {
        // Trimit request-ul
        PrintWriter out = new PrintWriter(
                socket.getOutputStream(), true
        );
        out.println(request);
        out.flush();
    }

    /**
     * Inchide socket-ul asociat clientului.
     */
    public void closeSocket() throws IOException {
        if (!socket.isClosed())
            socket.close();
    }

}
