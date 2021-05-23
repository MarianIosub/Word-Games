package pa.proj.word_games.server.components;

import pa.proj.word_games.games.AbstractGame;
import pa.proj.word_games.games.FazanGame;
import pa.proj.word_games.games.HangMan;
import pa.proj.word_games.games.TypeFast;
import pa.proj.word_games.server.threads.ClientThread;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * O camera la care se pot conecta mai multi utilizatori, pentru a juca un joc impreuna.
 */
public class GameLobby {
    /**
     * Retine toate lobby-urile create.
     */
    private static final List<GameLobby> gameLobbies = new ArrayList<>();

    /**
     * Retine numarul de jucatori conectati cand a inceput jocul.
     */
    private static int numberOfConnectedPlayers = 0;

    /**
     * Retine instanta jocului din acest fiecare lobby.
     */
    private static Map<GameLobby, AbstractGame> mapOfGames = new HashMap<>();

    /**
     * Codul pe care un utilizator il poate folosi pentru a putea intra in camera.
     */
    private String joinCode;

    /**
     * Numele jocului care va fi jucat.
     */
    private String gameName;

    /**
     * Numarul maxim de utilizatori care pot fi in lobby.
     */
    private int maxNumberOfPlayers;

    /**
     * Clientii din lobby.
     */
    private List<ClientThread> clients;

    /**
     * Client-ul care a creat lobby-ul.
     */
    private ClientThread owner;

    /**
     * Indica daca lobby-ul a fost distrus sau nu.
     */
    public boolean destroyed = false;

    /**
     * Indica daca a inceput jocul sau nu.
     */
    public boolean gameStarted = false;

    /**
     * Genereaza un string cu 6 caractere, reprezentand codul folosit pentru a putea intra in lobby.
     */
    private void generateJoinCode() {

        String characterSequence = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        joinCode = "";
        for (int i = 0; i < 6; i++)
            joinCode = joinCode + characterSequence.charAt(random.nextInt(characterSequence.length()));
    }

    /**
     * Creeaza un lobby nou caruia ii atribuie player-ul proprietar, tipul de joc care se va juca, cat si numarul de jucatori maxim pentru jocul ales
     *
     * @param owner
     * @param gameName
     * @param maxNumberOfPlayers
     */
    public GameLobby(ClientThread owner, String gameName, int maxNumberOfPlayers) {
        this.owner = owner;
        this.gameName = gameName;
        this.maxNumberOfPlayers = Math.min(maxNumberOfPlayers, 16);
        this.clients = new ArrayList<>();
        clients.add(owner);
        generateJoinCode();
        GameLobby.gameLobbies.add(this);
        switch (gameName.toLowerCase(Locale.ROOT)) {
            case "fazan": {
                GameLobby.mapOfGames.put(this, new FazanGame(this));
                break;
            }
            case "hangman": {
                GameLobby.mapOfGames.put(this, new HangMan(owner));
                break;
            }
            case "typefast": {
                GameLobby.mapOfGames.put(this, new TypeFast(owner));
                break;
            }
        }
    }

    public String getJoinCode() {
        return joinCode;
    }

    public ClientThread getOwner() {
        return owner;
    }

    /**
     * Adauga un nou client in lobby.
     *
     * @param client Thread-ul clientului.
     * @return 1, daca a fost adaugat clientul; 0, daca clientul apartine deja lobby-ului; -1, daca nu mai este loc in lobby
     */
    public int addNewClient(ClientThread client) {
        if (clients == null) {
            return -1;
        }

        if (clients.contains(client)) {
            return 0;
        }
        if (clients.size() == maxNumberOfPlayers) {
            return -1;
        }

        clients.add(client);
        return 1;
    }

    /**
     * Incepe jocul pentru acest lobby.
     */
    public synchronized void startGame(ClientThread clientThread) throws IOException, InterruptedException {
        gameStarted = true;

        if (GameLobby.mapOfGames.get(this) != null) {
            GameLobby.mapOfGames.get(this).initialize(clients);
            GameLobby.mapOfGames.get(this).startGame();

            destroyLobby();
        }
    }

    /**
     * Blocheaza procesul pana cand jocul a inceput.
     *
     * @param clientThread Thread-ul clientului care a apelat functia.
     */
    public void waitUntilGameStarted(ClientThread clientThread) throws InterruptedException, IOException {
        while (!gameStarted && !destroyed) {
            TimeUnit.SECONDS.sleep(1);
        }

        if (!destroyed) {
            clientThread.sendMessageWithoutWaitingForResponse(" ");
            clientThread.sendMessageWithoutWaitingForResponse("A inceput jocul!");
        }
        else {
            clientThread.sendMessageWithoutWaitingForResponse(" ");
            clientThread.sendMessageWithoutWaitingForResponse("Camera a fost inchisa!");
        }
    }

    /**
     * Cauta in lista de lobby-uri create dupa un anumit cod de conectare.
     *
     * @param joinCode Codul de conectare.
     * @return Lobby-ul gasit; null, daca nu a fost gasit niciunul
     */
    public static GameLobby getGameLobbyByJoinCode(String joinCode) {
        for (GameLobby gameLobby : gameLobbies) {
            if (gameLobby.getJoinCode().equals(joinCode)) {
                return gameLobby;
            }
        }

        return null;
    }

    /**
     * "Distruge" un lobby: curata memoria si il sterge din lista de lobby-uri.
     * De asemenea, "da afara" toti clientii conectati la acest lobby.
     */
    public void destroyLobby() throws IOException {
        gameLobbies.remove(this);
        mapOfGames.remove(this);

        for (ClientThread clientThread : clients) {
            if (clientThread != null) {
                clientThread.setGameLobby(null);
                clientThread.sendMessageWithoutWaitingForResponse("Ai fost dat afara din camera!");
            }
        }

        joinCode = null;
        clients = null;
        owner = null;
        gameStarted = false;
        destroyed = true;
    }

    /**
     * Sterge un anumit jucator din lista de clienti.
     * @param client Clientul care va fi sters.
     */
    public void removeClient(ClientThread client) {
        clients.remove(client);
    }
}
