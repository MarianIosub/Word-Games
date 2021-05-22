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

public class TypeFast implements AbstractGame {
    private long startTime;
    private ClientThread clientThread;
    private List<Word> words = new ArrayList<>();
    private int correctWords;
    private int badWords;

    public TypeFast(ClientThread clientThread) {
        this.clientThread = clientThread;
    }

    private void Welcome() throws IOException {
        clientThread.sendMessageWithoutWaitingForResponse("Salut " + clientThread.getUser().getUsername() + " si bine ai venit la \"Fast typing words!\"!");
        clientThread.sendMessageWithoutWaitingForResponse("Scopul acestui joc este de a vedea cat de rapid poti sa scrii!");
        clientThread.sendMessageWithoutWaitingForResponse("Astfel incat incearca sa scrii cat mai multe cuvinte corecte");
        clientThread.sendMessageWithoutWaitingForResponse("                INTR-UN SINGUR MINUT                ");
    }

    private long timeElapsed() {
        long elapsedTime = System.currentTimeMillis() - startTime;
        return elapsedTime / 1000;
    }

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

    private void checkWord() throws IOException {
        clientThread.sendMessageWithoutWaitingForResponse("Cuvintele care trebuie scrise sunt: ");
        StringBuilder stringBuilder = new StringBuilder();
        for (Word word : words) {
            stringBuilder.append(word.getText()).append(", ");
        }
        clientThread.sendMessageWithoutWaitingForResponse(stringBuilder.toString());
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

    @Override
    public void startGame() throws IOException {
        typeFastGame();
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    @Override
    public void initialize(List<ClientThread> clientThreads) { }

}
