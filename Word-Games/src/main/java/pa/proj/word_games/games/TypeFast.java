package pa.proj.word_games.games;

import pa.proj.word_games.controllers.WordController;
import pa.proj.word_games.models.Word;
import pa.proj.word_games.server.threads.ClientThread;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class TypeFast implements AbstractGame {
    private long startTime;
    private ClientThread clientThread;
    private List<Word> words = new ArrayList<>();
    private int correctWords;
    private int badWords;

    public TypeFast(ClientThread clientThread) {
        this.clientThread = clientThread;
    }

    public TypeFast() {
    }

    private void Welcome() {
        System.out.println("Salut si bine ai venit la \"Fast typing words!\"!");
        System.out.println("Scopul acestui joc este de a vedea cat de rapid poti sa scrii!");
        System.out.println("Astfel incat incearca sa scrii cat mai multe cuvinte corecte");
        System.out.println("                IN UN SINGUR MINUT                ");
    }

    private long timeRemaining() {
        long elapsedTime = System.currentTimeMillis() - startTime;
        long elapsedSeconds = elapsedTime / 1000;
        return elapsedSeconds;
    }

    private void extractWords() throws IOException {

        for (int index = 0; index < 20; index++) {
            System.out.println(words.size());
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
        System.out.println("Cuvintele care trebuie scrise sunt: ");
        for (Word word : words) {
            System.out.print(word.getText() + ", ");
        }
        Scanner scanner = new Scanner(System.in);
        Word wordRead = new Word();
        System.out.print("\n Introdu cuvantul rapid  >>");
        wordRead.setText(scanner.nextLine());
        if (wordRead.getText().equals(words.get(0).getText())) {
            correctWords++;
        } else {
            badWords++;
        }
        words.remove(0);
    }

    public void end() {
        System.out.println("In un minut ai reusit sa scrii: ");
        System.out.println("  >>cuvinte corecte:  " + correctWords);
        System.out.println("  >>cuvinte gresite:  " + badWords);
        System.out.println("Felicitari!");
    }

    public void typeFastGame() throws IOException {
        Welcome();
        extractWords();
        setStartTime(System.currentTimeMillis());

        while (timeRemaining() <= 60 && words.size() != 0) {
            System.out.println("\n\nS-au scurs: " + timeRemaining() + " secunde.");
            checkWord();
        }
        end();
    }

    @Override
    public void startGame() throws IOException {

    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    @Override
    public void initialize(List<ClientThread> clientThreads) {

    }

    public static void main(String[] args) throws IOException {
        TypeFast typeFast = new TypeFast();
        typeFast.typeFastGame();
    }
}
