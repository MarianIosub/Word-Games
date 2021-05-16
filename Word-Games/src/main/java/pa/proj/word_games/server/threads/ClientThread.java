package pa.proj.word_games.server.threads;

import pa.proj.word_games.games.FazanGame;
import pa.proj.word_games.games.Player;
import pa.proj.word_games.server.components.GameLobby;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;

public class ClientThread extends Thread {
    private Socket socket = null;
    private BufferedReader in;
    private PrintWriter out;

    /**
     * Contul in care este logat clientul
     */
    private Player user; // TODO: logare + register

    public ClientThread(Socket socket) throws IOException {
        this.socket = socket;
        user = new Player("playerName");
        in = new BufferedReader(
                new InputStreamReader(socket.getInputStream())
        );
        out = new PrintWriter(
                socket.getOutputStream()
        );
    }

    public Player getUser() {
        return user;
    }

    /**
     * <p>Comunic cu un client prin mesaje de tipul message-request-response (server-ul trimite un mesaj, clientul trimite o cerere,
     * server-ul raspunde cererii).</p>
     */
    public void run() {
        try {
            GameLobby gameLobby = null;
            String response;
            String joinCode;

            while(true) {
                sendMessageWithoutWaitingForResponse("Alegeti o actiune:");
                sendMessageWithoutWaitingForResponse("\t1 - Join Lobby");
                sendMessageWithoutWaitingForResponse("\t2 - Create Lobby");
                response = sendMessageAndWaitForResponse("\t3 - Start Game");

                if (response.equals("1")) { // Join Lobby
                    joinCode = sendMessageAndWaitForResponse("Introduceti codul lobby-ului:");
                    gameLobby = GameLobby.getGameLobbyByJoinCode(joinCode);

                    if(gameLobby == null)
                        sendMessageWithoutWaitingForResponse("Nu exista un lobby cu acest cod.");
                    else {
                        int answerCode = gameLobby.addNewClient(this);
                        switch(answerCode) {
                            case -1: {
                                sendMessageWithoutWaitingForResponse("Nu mai este loc in acest lobby.");
                                break;
                            }
                            case 1: {
                                sendMessageWithoutWaitingForResponse("Ai intrat in lobby.");
                                break;
                            }
                            case 0: {
                                sendMessageWithoutWaitingForResponse("Esti deja in acest lobby.");
                                break;
                            }
                        }

                        if(answerCode == 1)
                            break;
                    }
                }
                else if (response.equals("2")) { // Create Lobby
                    String gameName = "1";
                    while(true) {
                        sendMessageWithoutWaitingForResponse("Ce se va juca in acest lobby?");
                        sendMessageWithoutWaitingForResponse("\t1 - Fazan");
                        sendMessageWithoutWaitingForResponse("\t2 - Type Fast");
                        gameName = sendMessageAndWaitForResponse("\t3 - HangMan");

                        if(gameName.equals("1") || gameName.equals("2"))
                            break;

                        sendMessageWithoutWaitingForResponse("Raspuns invalid!");
                    }

                    // TODO: Hangman + alte jocuri
                    if(gameName.equals("1")) {
                        String maxNumberOfPlayers = "2";
                        while(true) {
                            maxNumberOfPlayers = sendMessageAndWaitForResponse("Cati jucatori pot fi in acest lobby?");

                            try {
                                int temp = Integer.parseInt(maxNumberOfPlayers);

                                if(temp < 2)
                                    sendMessageWithoutWaitingForResponse("Numarul minim de jucatori este 2!");
                                else
                                    break;
                            }
                            catch(Exception exception) {
                                sendMessageWithoutWaitingForResponse("Raspuns invalid!");
                            }
                        }

                        gameLobby = new GameLobby(this, "fazan", Integer.parseInt(maxNumberOfPlayers));
                    }
                    else if(gameName.equals("3")) {
                        gameLobby = new GameLobby(this, "hangman", 1);
                    } else if(gameName.equals("2")) {
                        gameLobby = new GameLobby(this, "typeFast", 1);
                    }

                    sendMessageWithoutWaitingForResponse("Codul de conectare este: " + gameLobby.getJoinCode());
                }
                else if (response.equals("3")) { // Start Game
                    if(gameLobby == null) {
                        sendMessageWithoutWaitingForResponse("Nu esti in niciun lobby.");
                        continue;
                    }

                    gameLobby.startGame(this);
                }
                else sendMessageWithoutWaitingForResponse("Raspuns invalid!");
            }

            gameLobby.waitUntilGameStarted(this);
            gameLobby.startGame(this);
        }
        catch(SocketException socketException) {
            System.out.println("Client deconectat...");
        }
        catch(Exception exception) {
            System.out.println("Exceptie la ClientThread:");
            exception.printStackTrace();
        }
        finally {
            try {
                System.out.println("Inchid socket-ul asociat clientului...");
                if(socket != null && !socket.isClosed())
                    socket.close();
            }
            catch(Exception ignored) { }
        }
    }

    /**
     * Trimite un mesaj catre client, fara sa mai astepte ca acesta sa trimita un raspuns.
     * @param message Mesajul care va fi trimis catre client.
     */
    public void sendMessageWithoutWaitingForResponse(String message) throws IOException {
        message = "0:::" + message;
        out.println(message);
        out.flush();

        in.readLine();
    }

    /**
     * Trimite un mesaj catre client si asteapta ca acesta sa trimita un raspuns.
     * @param message Mesajul care va fi trimis catre client.
     * @return Raspunsul primit de la client.
     */
    public String sendMessageAndWaitForResponse(String message) throws IOException {
        message = "1:::" + message;
        out.println(message);
        out.flush();

        return in.readLine();
    }

    // TODO: ca sa poata crea un lobby/sa se joace, trb sa fie logati => numele din joc va fi username-ul
    // TODO: de inlocuit Player cu noul User

    // TODO: un jucator poate fi intr-un singur lobby (nu in mai multe in acelasi timp)

    // TODO: stergerea unui lobby cand owner-ul se duce in alt lobby sau il sterge explicit
}
