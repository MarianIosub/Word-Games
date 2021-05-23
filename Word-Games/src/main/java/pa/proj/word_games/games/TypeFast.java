package pa.proj.word_games.games;

import pa.proj.word_games.controllers.ScoreController;
import pa.proj.word_games.controllers.WordController;
import pa.proj.word_games.models.TypeFastScore;
import pa.proj.word_games.models.Word;
import pa.proj.word_games.server.threads.ClientThread;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class TypeFast implements AbstractGame {
    private long startTime;
    private ClientThread clientThread;
    private List<Word> words = new ArrayList<>();
    private int correctWords;
    private int badWords;

    /**
     * Atribuie jocului Type-fast un player prin un client-thread
     *
     * @param clientThread - player-ul caruia i se atribuie jocul
     */
    public TypeFast(ClientThread clientThread) {
        this.clientThread = clientThread;
    }

    /**
     * Prezinta regulile jocului player-ului atribuit
     *
     * @throws IOException
     */
    private void Welcome() throws IOException {
        clientThread.sendMessageWithoutWaitingForResponse("Salut " + clientThread.getUser().getUsername() + " si bine ai venit la \"Fast typing words!\"!");
        clientThread.sendMessageWithoutWaitingForResponse("Scopul acestui joc este de a vedea cat de rapid poti sa scrii!");
        clientThread.sendMessageWithoutWaitingForResponse("Astfel incat incearca sa scrii cat mai multe cuvinte corecte");
        clientThread.sendMessageWithoutWaitingForResponse("                INTR-UN SINGUR MINUT                ");
    }

    /**
     * Calculeaza timpul in secunde a jocului curent
     *
     * @return cat timp a trecut de la inceputul jocului
     */
    private long timeElapsed() {
        long elapsedTime = System.currentTimeMillis() - startTime;
        return elapsedTime / 1000;
    }

    /**
     * Extrage un numar de cuvinte pe care le atribuie jocului
     *
     * @throws IOException
     */
    private void extractWords() throws IOException {

        for (int index = 0; index < 30; index++) {
            Word word = new Word();
            Random random = new Random();
            if (random.nextInt() % 2 == 0) {
                word = WordController.extractEasyWord();
            } else {
                word = WordController.extractMediumWord();
            }
            this.words.add(word);
        }
    }

    /**
     * Pentru fiecare cuvant curent, arata jucatorului cuvintele in ordinea in care trebuie sa le scrie
     * Pentru un cuvant introdus corect, incrementeaza numarul de cuvinte pana la un moment dat, iar pentru unul gresit, numarul de cuvinte gresite
     * Dupa fiecare cuvant introdus, elimina cuvantul curent din lista de cuvinte, pentru ca focus-ul playerului sa fie unul mai usor
     *
     * @throws IOException
     */
    private void checkWord() throws IOException {
        clientThread.sendMessageWithoutWaitingForResponse("Cuvintele care trebuie scrise sunt: ");
        //STREAMS
        //STREAMS
        //STREAMS
        String stringBuilder = words.stream().map(word -> word.getText() + ", ").collect(Collectors.joining());
        clientThread.sendMessageWithoutWaitingForResponse(stringBuilder);
        if(timeElapsed() >= 60) return;

        Word wordRead = new Word();

        wordRead.setText(clientThread.sendMessageAndWaitForResponse("Introdu cuvantul rapid "));
        if(timeElapsed() >= 60) return;

        if (wordRead.getText().equals(words.get(0).getText())) {
            correctWords++;
        } else {
            badWords++;
        }
        words.remove(0);
    }

    /**
     * Prezinta jucatorului statisticle finale, dupa numarul de cuvinte corecte si gresite
     * Daca acesta are in un minut mai multe corecte decat gresite, i se adauga un punct la recordul sau din abaza de date
     *
     * @throws IOException
     */
    public void end() throws IOException {
        long elapsedSeconds = timeElapsed();

        clientThread.sendMessageWithoutWaitingForResponse("In " + Long.min(elapsedSeconds, 60) + " secunde ai scris: ");
        clientThread.sendMessageWithoutWaitingForResponse("  >> " + (correctWords+badWords) + " cuvinte");
        clientThread.sendMessageWithoutWaitingForResponse("  >> " + correctWords + " cuvinte corecte");
        clientThread.sendMessageWithoutWaitingForResponse("  >> " + badWords + " cuvinte gresite");

        clientThread.sendMessageWithoutWaitingForResponse(" ");
        clientThread.sendMessageWithoutWaitingForResponse("Statistici:");

        double rating = (double)(60 * (correctWords + badWords))/elapsedSeconds;
        clientThread.sendMessageWithoutWaitingForResponse("  >> " + rating + " cuvinte per minut");

        rating = (double)(60 * correctWords)/elapsedSeconds;
        clientThread.sendMessageWithoutWaitingForResponse("  >> " + rating + " cuvinte corecte per minut");

        rating = (double)(60 * badWords)/elapsedSeconds;
        clientThread.sendMessageWithoutWaitingForResponse("  >> " + rating + " cuvinte gresite per minut");

        clientThread.sendMessageWithoutWaitingForResponse("Felicitari!");

        if(correctWords > badWords) {
            // Adaug un punct scorului sau de la acest joc
            TypeFastScore typeFastScore = ScoreController.getTypeFastScoreByUserId(clientThread.getUser().getId());
            if(typeFastScore == null) {
                typeFastScore = new TypeFastScore(
                        ScoreController.getNextTypeFastScoreAvailableId(), clientThread.getUser().getId(), 0
                );
                ScoreController.saveTypeFastScore(typeFastScore);
            }

            typeFastScore.setScore(typeFastScore.getScore() + 1);
            ScoreController.updateTypeFastScore(typeFastScore);
            clientThread.sendMessageWithoutWaitingForResponse("Ai primit un punct!");
        }
    }

    /**
     * Jocul propriu-zis, care dureaza 60 de secunde, + daca ultimul cuvant incepe sa il scrie inainte de secunda 60
     *
     * @throws IOException
     */
    public void typeFastGame() throws IOException {
        extractWords();
        Welcome();
        setStartTime(System.currentTimeMillis());
        while (timeElapsed() <= 60 && words.size() != 0) {
            clientThread.sendMessageWithoutWaitingForResponse("S-au scurs: " + timeElapsed() + " secunde.");
            checkWord();
        }
        end();
    }

    /**
     * Porneste jocul in retea pentru player-ului atribuit jocului
     *
     * @throws IOException
     */
    @Override
    public void startGame() throws IOException {
        typeFastGame();
    }

    /**
     * Seteaza timpul de start pentru a calcula dupam fiecare cuvantul introdus timpul scurs
     *
     * @param startTime - timpul cand a inceput jocului
     */
    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    @Override
    public void initialize(List<ClientThread> clientThreads) { }

}
