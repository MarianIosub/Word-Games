package pa.proj.word_games.games;

import pa.proj.word_games.controllers.WordController;
import pa.proj.word_games.models.TypeFastScore;
import pa.proj.word_games.models.Word;
import pa.proj.word_games.repositories.TypeFastScoreRepository;
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

    public TypeFast() { }

    private void Welcome() throws IOException {
        clientThread.sendMessageWithoutWaitingForResponse("Salut " + clientThread.getUser().getUsername() + " si bine ai venit la \"Fast typing words!\"!");
        clientThread.sendMessageWithoutWaitingForResponse("Scopul acestui joc este de a vedea cat de rapid poti sa scrii!");
        clientThread.sendMessageWithoutWaitingForResponse("Astfel incat incearca sa scrii cat mai multe cuvinte corecte");
        clientThread.sendMessageWithoutWaitingForResponse("                INTR-UN SINGUR MINUT                ");
    }

    private long timeRemaining() {
        long elapsedTime = System.currentTimeMillis() - startTime;
        long elapsedSeconds = elapsedTime / 1000;
        return elapsedSeconds;
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
            stringBuilder.append(word.getText() + ", ");
        }
        clientThread.sendMessageWithoutWaitingForResponse(stringBuilder.toString());

        Word wordRead = new Word();

        wordRead.setText(clientThread.sendMessageAndWaitForResponse("Introdu cuvantul rapid "));
        if (wordRead.getText().equals(words.get(0).getText())) {
            correctWords++;
        } else {
            badWords++;
        }
        words.remove(0);
    }

    public void end() throws IOException {
        clientThread.sendMessageWithoutWaitingForResponse("Intr-un minut ai reusit: ");
        clientThread.sendMessageWithoutWaitingForResponse("  >>total cuvinte: "+(correctWords+badWords));
        clientThread.sendMessageWithoutWaitingForResponse("  >>cuvinte corecte:  " + correctWords);
        clientThread.sendMessageWithoutWaitingForResponse("  >>cuvinte gresite:  " + badWords);
        double rating=(double)60/correctWords;
        clientThread.sendMessageWithoutWaitingForResponse("  >>cuvinte corecte per secunda:  " + rating);
        rating=(double)60/badWords;
        clientThread.sendMessageWithoutWaitingForResponse("  >>cuvinte gresite per secunda:  " + rating);
        clientThread.sendMessageWithoutWaitingForResponse("Felicitari!");

        if(correctWords > badWords) {
            // Adaug un punct scorului sau de la acest joc
            TypeFastScore typeFastScore = TypeFastScoreRepository.getInstance().findById(clientThread.getUser().getId());
            if(typeFastScore == null) {
                typeFastScore = new TypeFastScore(
                        TypeFastScoreRepository.getInstance().getNextAvailableId(), clientThread.getUser().getId(), 0
                );
                TypeFastScoreRepository.getInstance().save(typeFastScore);
            }

            typeFastScore.setScore(typeFastScore.getScore() + 1);
            TypeFastScoreRepository.getInstance().update(typeFastScore);
            clientThread.sendMessageWithoutWaitingForResponse("Ai primit un punct!");
        }
    }

    public void typeFastGame() throws IOException {
        extractWords();
        Welcome();
        setStartTime(System.currentTimeMillis());
        while (timeRemaining() <= 60 && words.size() != 0) {
            clientThread.sendMessageWithoutWaitingForResponse("S-au scurs: " + timeRemaining() + " secunde.");
            checkWord();
        }
        end();
    }

    @Override
    public void startGame() throws IOException {
        typeFastGame();
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    @Override
    public void initialize(List<ClientThread> clientThreads) { }

}
