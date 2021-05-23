package pa.proj.word_games.server.threads;

import pa.proj.word_games.controllers.ScoreController;
import pa.proj.word_games.controllers.UserController;
import pa.proj.word_games.models.User;
import pa.proj.word_games.server.components.GameLobby;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.Map;

public class ClientThread extends Thread {
    private Socket socket = null;
    private BufferedReader in;
    private PrintWriter out;

    /**
     * Lobby-ul la care este conectat clientul.
     */
    private GameLobby gameLobby;

    /**
     * Contul in care este logat clientul
     */
    private User user;

    /**
     * Comunica cu clientul dupa ce acesta s-a conectat.
     * <p>Comunic cu un client prin mesaje de tipul message-request-response (server-ul trimite un mesaj, clientul trimite o cerere,
     * server-ul raspunde cererii).</p>
     */
    private void loggedInMenu() throws IOException, InterruptedException {
        GameLobby gameLobbyObj = null;
        String response;
        String joinCode;

        while(true) {
            sendMessageWithoutWaitingForResponse(" ");
            sendMessageWithoutWaitingForResponse("Alegeti o actiune:");
            sendMessageWithoutWaitingForResponse("\t1 - Intra intr-o camera");
            sendMessageWithoutWaitingForResponse("\t2 - Creeaza o camera");
            sendMessageWithoutWaitingForResponse("\t3 - Incepe jocul");
            sendMessageWithoutWaitingForResponse("\t4 - Inchide camera (daca esti creatorul acestuia)");
            sendMessageWithoutWaitingForResponse("\t5 - Vreau sa-mi vad punctajele");
            response = sendMessageAndWaitForResponse("\t0 - Inchide");

            if(response.equals("0")){ // Exit
                sendMessageWithoutWaitingForResponse(" ");
                sendMessageWithoutWaitingForResponse("La revedere!");
                break;
            }

            switch(response) {
                case "1" : { // Join Lobby
                    sendMessageWithoutWaitingForResponse(" ");
                    joinCode = sendMessageAndWaitForResponse("Introduceti codul de conectare:");
                    gameLobbyObj = GameLobby.getGameLobbyByJoinCode(joinCode);

                    if(gameLobbyObj == null) {
                        sendMessageWithoutWaitingForResponse(" ");
                        sendMessageWithoutWaitingForResponse("Nu exista o camera cu acest cod de conectare.");
                    }
                    else {
                        int answerCode = gameLobbyObj.addNewClient(this);
                        switch(answerCode) {
                            case -1: {
                                sendMessageWithoutWaitingForResponse(" ");
                                sendMessageWithoutWaitingForResponse("Nu mai este loc in aceasta camera.");
                                break;
                            }
                            case 1: {
                                sendMessageWithoutWaitingForResponse(" ");
                                sendMessageWithoutWaitingForResponse("Ai intrat in camera.");

                                if(this.gameLobby != null) {
                                    if (this.gameLobby.getOwner() == this) {
                                        gameLobby.destroyLobby();
                                    }
                                }
                                this.gameLobby = gameLobbyObj;

                                gameLobby.waitUntilGameStarted(this);
                                if(gameLobby != null && !gameLobby.destroyed)
                                    gameLobby.startGame(this);

                                break;
                            }
                            case 0: {
                                sendMessageWithoutWaitingForResponse(" ");
                                sendMessageWithoutWaitingForResponse("Esti deja in aceasta camera.");
                                break;
                            }
                        }

                        if(answerCode == 1)
                            break;
                    }
                    break;
                }

                case "2" : { // Create lobby
                    String gameName;
                    while(true) {
                        sendMessageWithoutWaitingForResponse(" ");
                        sendMessageWithoutWaitingForResponse("Ce se va juca in acesta camera?");
                        sendMessageWithoutWaitingForResponse("\t1 - Fazan");
                        sendMessageWithoutWaitingForResponse("\t2 - Type Fast");
                        gameName = sendMessageAndWaitForResponse("\t3 - Spanzuratoarea");

                        if(gameName.equals("1") || gameName.equals("2") || gameName.equals("3"))
                            break;

                        sendMessageWithoutWaitingForResponse(" ");
                        sendMessageWithoutWaitingForResponse("Raspuns invalid!");
                    }

                    switch (gameName) {
                        case "1":
                            String maxNumberOfPlayers;
                            while (true) {
                                sendMessageWithoutWaitingForResponse(" ");
                                maxNumberOfPlayers = sendMessageAndWaitForResponse("Cati jucatori pot fi in aceasta camera?");

                                try {
                                    int temp = Integer.parseInt(maxNumberOfPlayers);

                                    if (temp < 2) {
                                        sendMessageWithoutWaitingForResponse(" ");
                                        sendMessageWithoutWaitingForResponse("Numarul minim de jucatori este 2!");
                                    }
                                    else
                                        break;
                                } catch (Exception exception) {
                                    sendMessageWithoutWaitingForResponse(" ");
                                    sendMessageWithoutWaitingForResponse("Raspuns invalid!");
                                }
                            }

                            gameLobbyObj = new GameLobby(this, "fazan", Integer.parseInt(maxNumberOfPlayers));
                            break;
                        case "2":
                            gameLobbyObj = new GameLobby(this, "typeFast", 1);
                            break;
                        case "3":
                            gameLobbyObj = new GameLobby(this, "hangman", 1);
                            break;
                    }

                    sendMessageWithoutWaitingForResponse(" ");
                    sendMessageWithoutWaitingForResponse("Codul de conectare este: " + gameLobbyObj.getJoinCode());

                    if(this.gameLobby != null)
                        this.gameLobby.destroyLobby();
                    this.gameLobby = gameLobbyObj;
                    break;
                }

                case "3" : { // Start Game
                    if(this.gameLobby == null) {
                        sendMessageWithoutWaitingForResponse(" ");
                        sendMessageWithoutWaitingForResponse("Nu esti in nicio camera.");
                        continue;
                    }

                    this.gameLobby.startGame(this);
                    break;
                }

                case "4" : { // Inchide lobby-ul (daca esti creatorul acestuia)
                    if(this.gameLobby == null) {
                        sendMessageWithoutWaitingForResponse(" ");
                        sendMessageWithoutWaitingForResponse("Nu esti in nicio camera.");
                        continue;
                    }

                    if(this.gameLobby.getOwner() != this) {
                        sendMessageWithoutWaitingForResponse(" ");
                        sendMessageWithoutWaitingForResponse("Nu esti creatorul camerei in care esti conectat.");
                        continue;
                    }

                    gameLobby.destroyLobby();
                    break;
                }

                case "5" : { // Vreau sa-mi vad punctajele
                    Map<String, Integer> playerScores = ScoreController.getScoresByUserId(user.getId());
                    sendMessageWithoutWaitingForResponse(" ");
                    sendMessageWithoutWaitingForResponse("Punctajele tale sunt: ");
                    sendMessageWithoutWaitingForResponse("\tFazan -> " + playerScores.get("fazan"));
                    sendMessageWithoutWaitingForResponse("\tSpanzuratoarea -> " + playerScores.get("hangman"));
                    sendMessageWithoutWaitingForResponse("\tTypeFast -> " + playerScores.get("typefast"));
                    break;
                }

                default: {
                    sendMessageWithoutWaitingForResponse(" ");
                    sendMessageWithoutWaitingForResponse("Raspuns invalid!");
                    break;
                }
            }
        }
    }

    public ClientThread(Socket socket) throws IOException {
        this.socket = socket;
        user = null;
        in = new BufferedReader(
                new InputStreamReader(socket.getInputStream())
        );
        out = new PrintWriter(
                socket.getOutputStream()
        );
        gameLobby = null;
    }

    public User getUser() {
        return user;
    }

    public void setGameLobby(GameLobby gameLobby) throws IOException {
        this.gameLobby = gameLobby;

        if(gameLobby == null)
            sendMessageWithoutWaitingForResponse("Lobby-ul a fost inchis!");
    }

    /**
     * <p>Comunic cu un client prin mesaje de tipul message-request-response (server-ul trimite un mesaj, clientul trimite o cerere,
     * server-ul raspunde cererii).</p>
     */
    public void run() {
        try {
            String response;
            while(user == null) {
                sendMessageWithoutWaitingForResponse(" ");
                sendMessageWithoutWaitingForResponse("Alegeti o actiune:");
                sendMessageWithoutWaitingForResponse("\t1 - Autentificare");
                sendMessageWithoutWaitingForResponse("\t2 - Inregistrare");
                response = sendMessageAndWaitForResponse("\t0 - Inchide");

                if(response.equals("0")) { // Exit

                    sendMessageWithoutWaitingForResponse("La revedere!");
                    break;
                }

                switch(response) {
                    case "1": { // Login
                        sendMessageWithoutWaitingForResponse(" ");
                        sendMessageWithoutWaitingForResponse("Autentifica-te");

                        String username, password;
                        username = sendMessageAndWaitForResponse("Introduceti numele de utilizator");
                        password = sendMessageAndWaitForResponse("Introduceti parola");

                        user = UserController.getUserByCredentials(username, password);
                        if(user != null) {
                            sendMessageWithoutWaitingForResponse(" ");
                            sendMessageWithoutWaitingForResponse("Te-ai autentificat cu succes!");
                        }
                        else {
                            sendMessageWithoutWaitingForResponse(" ");
                            sendMessageWithoutWaitingForResponse("Numele de utilizator sau parola gresita!");
                        }
                        break;
                    }

                    case "2": { // Register
                        sendMessageWithoutWaitingForResponse(" ");
                        sendMessageWithoutWaitingForResponse("Creeaza un cont");

                        String username, password;
                        username = sendMessageAndWaitForResponse("Introduceti numele de utilizator");
                        password = sendMessageAndWaitForResponse("Introduceti parola");

                        int returnedCode = UserController.registerUser(username, password);
                        switch(returnedCode) {
                            case -1: {
                                sendMessageWithoutWaitingForResponse(" ");
                                sendMessageWithoutWaitingForResponse("Atat numele de utilizator, cat si parola trebuie sa contina cel putin 5 caractere!");
                                break;
                            }

                            case 0: {
                                sendMessageWithoutWaitingForResponse(" ");
                                sendMessageWithoutWaitingForResponse("Numele de utilizator este deja folosit!");
                                break;
                            }

                            case 1: {
                                sendMessageWithoutWaitingForResponse(" ");
                                sendMessageWithoutWaitingForResponse("Contul a fost creat cu succes!");
                                break;
                            }
                        }
                        break;
                    }

                    default: {
                        sendMessageWithoutWaitingForResponse(" ");
                        sendMessageWithoutWaitingForResponse("Raspuns invalid!");
                        break;
                    }
                }
            }

            loggedInMenu();
        }
        catch(SocketException socketException) {
            System.out.println("Client deconectat...");
            if(gameLobby != null) {
                gameLobby.removeClient(this);
            }
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
}
