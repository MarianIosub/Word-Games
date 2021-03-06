package pa.proj.word_games.games;

import pa.proj.word_games.controllers.ScoreController;
import pa.proj.word_games.controllers.WordController;
import pa.proj.word_games.models.FazanScore;
import pa.proj.word_games.models.User;
import pa.proj.word_games.server.components.GameLobby;
import pa.proj.word_games.server.threads.ClientThread;

import java.io.IOException;
import java.util.*;

public class FazanGame implements AbstractGame {
    /**
     * Retine player-ul a carui tura este in acest moment.
     */
    private int playerTurnIndex;

    /**
     * Retine scorul unui jucator.
     */
    private Map<User, Integer> playerScores;

    /**
     * Thread-urile care comunica cu clientii din joc (Lista jucatorilor).
     */
    private List<ClientThread> clientThreads;

    /**
     * Retine lobby-ul in care se joaca jocul.
     */
    private GameLobby gameLobby;

    /**
     * Verifica daca un cuvant este valid (daca apare in baza de date).
     *
     * @param word Cuvantul verificat.
     * @return true, daca este valid; false, altfel
     */
    private boolean isValidWord(String word) throws IOException {
        // Verific lungimea
        if (word.length() < 3)
            return false;

        // Verific daca incepe cu doua consoane
        if (!"aeiouăîâ".contains(word.subSequence(0, 1)) && !"aeiouăîâ".contains(word.subSequence(1, 2)))
            return false;

        return WordController.verifyWordExistence(word);
    }

    /**
     * Verifica regula fazanului: noul cuvant trebuie sa inceapa cu ultimele 2 litere ale cuvantului anterior.
     *
     * @param previousWord Cuvantul anterior
     * @param actualWord   Cuvantul actual.
     * @return true, daca regula este respectata; false, altfel
     */
    private boolean verifyFazanRule(String previousWord, String actualWord) {
        if (previousWord.length() == 1)
            return previousWord.charAt(0) == actualWord.charAt(0);

        previousWord = WordController.stringWithoutDiacritics(previousWord);
        actualWord = WordController.stringWithoutDiacritics(actualWord);
        return previousWord.substring(previousWord.length() - 2).contentEquals(actualWord.subSequence(0, 2));
    }

    /**
     * Verifica daca s-a terminat jocul: nu exista niciun cuvant care sa inceapa cu ultimele doua litere ale cuvantului actual.
     *
     * @param actualWord Cuvantul actual
     * @return true, daca jocul s-a terminat; false, altfel
     */
    private boolean verifyEndOfFazanGame(String actualWord) {
        // Verific daca se termina cu doua consoane
        if (!"aeiouăîâ".contains(actualWord.subSequence(actualWord.length() - 2, actualWord.length() - 1)) &&
                !"aeiouăîâ".contains(actualWord.subSequence(actualWord.length() - 1, actualWord.length())))
            return true;

        return !WordController.existsWordsWithStartPattern(actualWord.substring(actualWord.length() - 2));
    }

    /**
     * Trec la jucatorul urmator din lista de playeri.
     */
    private void nextPlayerTurn() {
        playerTurnIndex = (playerTurnIndex + 1) % clientThreads.size();
    }

    /**
     * Alege un caracter random din alfabetul latin.
     *
     * @return Caracterul ales.
     */
    private char generateRandomAlphabetCharacter() {
        String alphabet = "abcdefghijklmnoprstuv";
        Random random = new Random();
        return alphabet.charAt(random.nextInt(alphabet.length()));
    }

    /**
     * Se joaca o runda din jocul Fazan.
     *
     * @param roundNumber Numarul rundei actuale.
     * @return Index-ul jucatorului care a pierdut.
     * @throws IOException
     */
    private synchronized int gameSession(int roundNumber, int startingPlayerIndex) throws IOException {
        // Initializarea variabilelor
        sendMessageToAllClients(" ");
        sendMessageToAllClients(" ");
        sendMessageToAllClients("Fazan Game - Runda " + roundNumber);

        playerTurnIndex = startingPlayerIndex;
        int turnNumber = 0;

        List<String> usedWords = new ArrayList<>();
        String previousWord = String.valueOf(generateRandomAlphabetCharacter());
        String actualWord;
        boolean forceEndGameSession = false; // Devine true cand un jucator introduce 5 cuvinte care nu respecta regulile/nu sunt valide/se repeta

        // Jocul
        do {
            turnNumber++;
            int numberOfWrongWords = 0;

            sendMessageToAllClients(" ");
            sendMessageToAllClients(" ");
            sendMessageToAllClients("Tura jucatorului " + clientThreads.get(playerTurnIndex).getUser().getUsername());
            if (previousWord.length() == 1)
                actualWord = sendMessageToAllClientsAndWaitResponseFromCertainClient(
                        "Caracterul cu care trebuie sa inceapa litera: " + previousWord,
                        clientThreads.get(playerTurnIndex)
                );
            else
                actualWord = sendMessageToAllClientsAndWaitResponseFromCertainClient(
                        "Caracterele cu care trebuie sa inceapa literele: " + previousWord.substring(previousWord.length() - 2),
                        clientThreads.get(playerTurnIndex)
                );

            sendMessageToAllClientsExceptCertainOne("Jucatorul " + clientThreads.get(playerTurnIndex).getUser().getUsername() +
                    "a introdus cuvantul \"" + actualWord + "\".", clientThreads.get(playerTurnIndex));

            while (true) {
                sendMessageToAllClients(" ");
                // Verific daca cuvantul este valid
                if (actualWord.length() < 3 || !isValidWord(actualWord)) {
                    actualWord = sendMessageToAllClientsAndWaitResponseFromCertainClient("Cuvantul nu este valid!", clientThreads.get(playerTurnIndex));
                }
                // Verific daca cuvantul respecta regula fazanului
                else if (!verifyFazanRule(previousWord, actualWord)) {
                    actualWord = sendMessageToAllClientsAndWaitResponseFromCertainClient("Cuvantul nu respecta regula fazanului!", clientThreads.get(playerTurnIndex));
                }
                // Verific daca a mai fost folosit cuvantul in aceasta runda
                else if (usedWords.contains(actualWord.toLowerCase(Locale.ROOT))) {
                    actualWord = sendMessageToAllClientsAndWaitResponseFromCertainClient("Cuvantul a mai fost folosit!", clientThreads.get(playerTurnIndex));
                }
                // Verific daca este cuvant care inchide si daca are voie sa inchida
                else if ((verifyEndOfFazanGame(actualWord)) && (turnNumber <= clientThreads.size())) {
                    actualWord = sendMessageToAllClientsAndWaitResponseFromCertainClient("Nu ai voie sa inchizi jocul atat de devreme!", clientThreads.get(playerTurnIndex));
                }
                // Cuvant bun
                else
                    break;

                sendMessageToAllClients(" ");
                numberOfWrongWords++;
                sendMessageToAllClientsExceptCertainOne("Jucatorul " + clientThreads.get(playerTurnIndex).getUser().getUsername() +
                        "a introdus cuvantul \"" + actualWord + "\".", clientThreads.get(playerTurnIndex));

                if (numberOfWrongWords >= 4) {
                    forceEndGameSession = true;
                    break;
                }
            }

            if (forceEndGameSession) break;

            usedWords.add(actualWord.toLowerCase(Locale.ROOT));
            previousWord = actualWord;
            nextPlayerTurn();

        } while (!verifyEndOfFazanGame(actualWord));

        // Afisarea pierzatorului si updatarea scorului acestuia
        sendMessageToAllClients(" ");
        sendMessageToAllClients(" ");
        sendMessageToAllClients("In runda " + roundNumber + " a pierdut jucatorul " + clientThreads.get(playerTurnIndex).getUser().getUsername() + ".");
        playerScores.replace(clientThreads.get(playerTurnIndex).getUser(),
                playerScores.get(clientThreads.get(playerTurnIndex).getUser()) - 1);
        return playerTurnIndex;
    }

    /**
     * Verifica daca exista un jucator care a pierdut (are scorul 0).
     *
     * @return true, daca exista; false, altfel
     */
    private boolean existsPlayerWhoLost() {
        for (ClientThread clientThread : clientThreads) {
            if (playerScores.get(clientThread.getUser()) == 0)
                return true;
        }
        return false;
    }

    /**
     * Afiseaza pe ercan scorul fiecarui jucator.
     */
    private void printPlayerScores() throws IOException {
        sendMessageToAllClients(" ");
        sendMessageToAllClients("Scorul fiecarui jucator: \n");
        for (ClientThread clientThread : clientThreads) {
            User user = clientThread.getUser();
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("\t").append(user.getUsername()).append(" - ");
            switch (playerScores.get(user)) {
                case 5: {
                    stringBuilder.append("");
                    break;
                }
                case 4: {
                    stringBuilder.append("F");
                    break;
                }
                case 3: {
                    stringBuilder.append("FA");
                    break;
                }
                case 2: {
                    stringBuilder.append("FAZ");
                    break;
                }
                case 1: {
                    stringBuilder.append("FAZA");
                    break;
                }
                case 0: {
                    stringBuilder.append("FAZAN");
                    break;
                }
            }
            sendMessageToAllClients(stringBuilder.toString());
        }
    }

    /**
     * Trimite un mesaj catre toti clientii din lista clientThreads, fara a astepta vreun raspuns de la acestia.
     *
     * @param message Mesajul care va fi trimis.
     */
    private void sendMessageToAllClients(String message) throws IOException {
        for (ClientThread clientThread : clientThreads) {
            clientThread.sendMessageWithoutWaitingForResponse(message);
        }
    }

    /**
     * Trimite un mesaj catre toti clientii din lista clientThreads, asteptand raspunsul doar de la unul dintre acestia, specificat.
     *
     * @param message Mesajul care va fi trimis.
     * @param client  Clientul de la care se va astepta raspunsul
     * @return Raspunsul dat
     */
    private String sendMessageToAllClientsAndWaitResponseFromCertainClient(String message, ClientThread client) throws IOException {
        for (ClientThread clientThread : clientThreads) {
            if (clientThread != client) {
                clientThread.sendMessageWithoutWaitingForResponse(message);
            }
        }
        return client.sendMessageAndWaitForResponse(message);
    }

    /**
     * <p>Trimite un mesaj catre toti clientii din lista clientThreads, cu exceptia unuia specific, fara a astepta
     * vreun raspuns de la acestia.</p>
     *
     * @param message        Mesajul care va fi trimis.
     * @param exceptedClient Clientul caruia nu ii va fi trimis un mesaj.
     */
    private void sendMessageToAllClientsExceptCertainOne(String message, ClientThread exceptedClient) throws IOException {
        for (ClientThread clientThread : clientThreads) {
            if (clientThread != exceptedClient) {
                clientThread.sendMessageWithoutWaitingForResponse(message);
            }
        }
    }

    public FazanGame(GameLobby gameLobby) {
        this.gameLobby = gameLobby;
    }

    /**
     * Initializeaza campurile.
     *
     * @param clientThreads Lista thread-urilor clientilor.
     */
    public synchronized void initialize(List<ClientThread> clientThreads) {
        this.clientThreads = clientThreads;
        playerScores = new HashMap<>();
    }

    /**
     * Incepe jocul.
     */
    public synchronized void startGame() throws IOException {
        if (clientThreads.size() < 2) {
            sendMessageToAllClients(" ");
            sendMessageToAllClients("Jocul de Fazan nu poate incepe deoarece nu sunt destui jucatori conectati in camera!");
            return;
        }

        // Initializez scorurile
        for (ClientThread clientThread : clientThreads) {
            playerScores.put(clientThread.getUser(), 5);
        }

        // Jucarea rundelor
        int roundNumber = 1;
        int startingPlayerIndex = 0;
        while (!existsPlayerWhoLost()) {
            startingPlayerIndex = gameSession(roundNumber, startingPlayerIndex);

            // Afisarea scorurilor fiecarui player dupa fiecare runda
            printPlayerScores();

            roundNumber++;
        }

        // Afisarea player-ului pierzator
        sendMessageToAllClients(" ");
        sendMessageToAllClients(" ");
        sendMessageToAllClients(" ");
        sendMessageToAllClients(clientThreads.get(startingPlayerIndex).getUser().getUsername() + " a pierdut jocul de Fazan!");

        // Adaug cate un punct fiecarui jucator, cu exceptia pierzatorului
        for (int index = 0; index < clientThreads.size(); index++) {
            if (index != startingPlayerIndex) {
                FazanScore fazanScore = ScoreController.getFazanScoreByUserId(clientThreads.get(index).getUser().getId());
                if (fazanScore == null) {
                    fazanScore = new FazanScore(
                            ScoreController.getNextFazanScoreAvailableId(), clientThreads.get(index).getUser().getId(), 0
                    );
                    ScoreController.saveFazanScore(fazanScore);
                }

                fazanScore.setScore(fazanScore.getScore() + 1);
                ScoreController.updateFazanScore(fazanScore);
                sendMessageToAllClients(" ");
                clientThreads.get(index).sendMessageWithoutWaitingForResponse("Ai castigat un punct!");
            }
        }
    }
}
