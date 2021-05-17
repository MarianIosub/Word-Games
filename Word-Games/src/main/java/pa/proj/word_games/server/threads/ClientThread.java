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
            sendMessageWithoutWaitingForResponse("Alegeti o actiune:");
            sendMessageWithoutWaitingForResponse("\t1 - Join Lobby");
            sendMessageWithoutWaitingForResponse("\t2 - Create Lobby");
            sendMessageWithoutWaitingForResponse("\t3 - Start Game");
            sendMessageWithoutWaitingForResponse("\t4 - Inchide lobby-ul (daca esti creatorul acestuia)");
            sendMessageWithoutWaitingForResponse("\t5 - Vreau sa-mi vad punctajele");
            response = sendMessageAndWaitForResponse("\t0 - Exit");

            if(response.equals("0")){ // Exit
                sendMessageWithoutWaitingForResponse("La revedere!");
                break;
            }

            switch(response) {
                case "1" : { // Join Lobby
                    joinCode = sendMessageAndWaitForResponse("Introduceti codul lobby-ului:");
                    gameLobbyObj = GameLobby.getGameLobbyByJoinCode(joinCode);

                    if(gameLobbyObj == null)
                        sendMessageWithoutWaitingForResponse("Nu exista un lobby cu acest cod.");
                    else {
                        int answerCode = gameLobbyObj.addNewClient(this);
                        switch(answerCode) {
                            case -1: {
                                sendMessageWithoutWaitingForResponse("Nu mai este loc in acest lobby.");
                                break;
                            }
                            case 1: {
                                sendMessageWithoutWaitingForResponse("Ai intrat in lobby.");

                                if(this.gameLobby != null)
                                    gameLobby.destroyLobby();
                                this.gameLobby = gameLobbyObj;

                                gameLobby.waitUntilGameStarted(this);
                                if(gameLobby != null && !gameLobby.destroyed)
                                    gameLobby.startGame(this);

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
                    break;
                }

                case "2" : { // Create lobby
                    String gameName;
                    while(true) {
                        sendMessageWithoutWaitingForResponse("Ce se va juca in acest lobby?");
                        sendMessageWithoutWaitingForResponse("\t1 - Fazan");
                        sendMessageWithoutWaitingForResponse("\t2 - Type Fast");
                        gameName = sendMessageAndWaitForResponse("\t3 - HangMan");

                        if(gameName.equals("1") || gameName.equals("2") || gameName.equals("3"))
                            break;

                        sendMessageWithoutWaitingForResponse("Raspuns invalid!");
                    }

                    switch (gameName) {
                        case "1":
                            String maxNumberOfPlayers;
                            while (true) {
                                maxNumberOfPlayers = sendMessageAndWaitForResponse("Cati jucatori pot fi in acest lobby?");

                                try {
                                    int temp = Integer.parseInt(maxNumberOfPlayers);

                                    if (temp < 2)
                                        sendMessageWithoutWaitingForResponse("Numarul minim de jucatori este 2!");
                                    else
                                        break;
                                } catch (Exception exception) {
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

                    sendMessageWithoutWaitingForResponse("Codul de conectare este: " + gameLobbyObj.getJoinCode());

                    if(this.gameLobby != null)
                        this.gameLobby.destroyLobby();
                    this.gameLobby = gameLobbyObj;
                    break;
                }

                case "3" : { // Start Game
                    if(this.gameLobby == null) {
                        sendMessageWithoutWaitingForResponse("Nu esti in niciun lobby.");
                        continue;
                    }

                    this.gameLobby.startGame(this);
                    break;
                }

                case "4" : { // Inchide lobby-ul (daca esti creatorul acestuia)
                    if(this.gameLobby == null) {
                        sendMessageWithoutWaitingForResponse("Nu esti in niciun lobby.");
                        continue;
                    }

                    if(this.gameLobby.getOwner() != this) {
                        sendMessageWithoutWaitingForResponse("Nu esti creatorul lobby-ului in care esti conectat.");
                        continue;
                    }

                    gameLobby.destroyLobby();
                    break;
                }

                case "5" : { // Vreau sa-mi vad punctajele
                    Map<String, Integer> playerScores = ScoreController.getScoresByUserId(user.getId());
                    sendMessageWithoutWaitingForResponse("Punctajele tale sunt: ");
                    sendMessageWithoutWaitingForResponse("\tFazan -> " + playerScores.get("fazan"));
                    sendMessageWithoutWaitingForResponse("\tSpanzuratoarea -> " + playerScores.get("hangman"));
                    sendMessageWithoutWaitingForResponse("\tTypeFast -> " + playerScores.get("typefast"));
                    break;
                }

                default: {
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
                sendMessageWithoutWaitingForResponse("Alegeti o actiune:");
                sendMessageWithoutWaitingForResponse("\t1 - Login");
                sendMessageWithoutWaitingForResponse("\t2 - Register");
                response = sendMessageAndWaitForResponse("\t0 - Exit");

                if(response.equals("0")) { // Exit
                    sendMessageWithoutWaitingForResponse("La revedere!");
                    break;
                }

                switch(response) {
                    case "1": { // Login
                        sendMessageWithoutWaitingForResponse("Autentifica-te");

                        String username, password;
                        username = sendMessageAndWaitForResponse("Introduceti username-ul");
                        password = sendMessageAndWaitForResponse("Introduceti parola");

                        user = UserController.getUserByCredentials(username, password);
                        if(user != null) {
                            sendMessageWithoutWaitingForResponse("Te-ai autentificat cu succes!");
                        }
                        else {
                            sendMessageWithoutWaitingForResponse("Username sau parola gresita!");
                        }
                        break;
                    }

                    case "2": { // Register
                        sendMessageWithoutWaitingForResponse("Creeaza un cont");

                        String username, password;
                        username = sendMessageAndWaitForResponse("Introduceti username-ul");
                        password = sendMessageAndWaitForResponse("Introduceti parola");

                        int returnedCode = UserController.registerUser(username, password);
                        switch(returnedCode) {
                            case -1: {
                                sendMessageWithoutWaitingForResponse("Atat numele de utilizator, cat si parola trebuie sa contina cel putin 5 caractere!");
                                break;
                            }

                            case 0: {
                                sendMessageWithoutWaitingForResponse("Numele de utilizator este deja folosit!");
                                break;
                            }

                            case 1: {
                                sendMessageWithoutWaitingForResponse("Contul a fost creat cu succes!");
                                break;
                            }
                        }
                        break;
                    }

                    default: {
                        sendMessageWithoutWaitingForResponse("Raspuns invalid!");
                        break;
                    }
                }
            }

            loggedInMenu();
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
}
