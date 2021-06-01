package pa.proj.word_games.games;

import pa.proj.word_games.controllers.ScoreController;
import pa.proj.word_games.controllers.WordController;
import pa.proj.word_games.models.HangmanScore;
import pa.proj.word_games.models.Word;
import pa.proj.word_games.server.threads.ClientThread;

import java.io.IOException;
import java.util.List;

public class HangMan implements AbstractGame {
    private  Word wordToGuess = new Word();
    private  Word wordGuessed = new Word();
    private  Integer lifes;
    private static ClientThread clientThread;

    /**
     * Creeaza un joc Hang_Man caruia ii atribuie un un player
     *
     * @param clientThread - thread-ul player-ului care Joaca jocul
     */
    public HangMan(ClientThread clientThread) {
        HangMan.clientThread = clientThread;
    }

    /**
     * Pune jucatorul sa isi aleaga nivelul de joc pe care doreste sa il joace
     * la nivelul usor, lungimea cuvantului va fi pana in 6 litere, la mediu si greu lungimea acestora incrementandu-se
     *
     * @throws IOException
     */
    public  void gameLevel() throws IOException {
        clientThread.sendMessageWithoutWaitingForResponse(" ");
        String level = clientThread.sendMessageAndWaitForResponse("Alege nivelul de joc pe care il doresti: Usor, Mediu, Greu!");
        switch (level) {
            case "usor": {
                wordToGuess = WordController.extractEasyWord();
                return;
            }
            case "mediu": {
                wordToGuess = WordController.extractMediumWord();
                return;
            }
            case "greu": {
                wordToGuess = WordController.extractHardWord();
                return;
            }
            default: {
                clientThread.sendMessageWithoutWaitingForResponse("Nivelul de joc introdus nu corespunde! Reincercati:");
                gameLevel();
            }
        }
    }

    /**
     * Inceutul jocului, care prezinta jucatorului regulile jocului Spanzuratoarea
     *
     * @throws IOException
     */
    public  void welcome() throws IOException {
        clientThread.sendMessageWithoutWaitingForResponse(" ");
        clientThread.sendMessageWithoutWaitingForResponse(" ");
        clientThread.sendMessageWithoutWaitingForResponse(" ");
        clientThread.sendMessageWithoutWaitingForResponse("Salut " + clientThread.getUser().getUsername() + " si bine ai venit la jocul Spânzurătoarea!");
        clientThread.sendMessageWithoutWaitingForResponse("Regulile sunt dupa cum urmeaza:");
        clientThread.sendMessageWithoutWaitingForResponse("\t- incearca sa ghicesti pe rand fiecare litera pana cuvantul se umple;");
        clientThread.sendMessageWithoutWaitingForResponse("\t- ai doar 7 vieti disponibile pana vei fi spanzurat;");
        clientThread.sendMessageWithoutWaitingForResponse("\t- ai grija la literele deja folosite sa nu le refolosesti inutil;");
        clientThread.sendMessageWithoutWaitingForResponse("MULT SUCCES!");
    }

    /**
     * Initializeaza cuvantul ghicit pana la un moment dat cu atatea '*' cat are cuvantul pe care va trebui sa il ghiceasca
     *
     * @throws IOException
     */
    public  void initGuess() throws IOException {
        wordGuessed.setText("");
        for (int index = 0; index < wordToGuess.getText().length(); index++) {
            wordGuessed.setText(wordGuessed.getText() + "*");

        }
        clientThread.sendMessageWithoutWaitingForResponse(wordGuessed.getText());
    }

    /**
     * Prezinta jucatorului nivelul de spanzurare pana la momentul in care se afla, acesta depinzdand de un numar de vieti initializate
     *
     * @throws IOException
     */
    public  void hangImage() throws IOException {
        if (lifes == 6) {
            clientThread.sendMessageWithoutWaitingForResponse(" ");
            clientThread.sendMessageWithoutWaitingForResponse("Nu ai nimerit, mai incearca!");
            clientThread.sendMessageWithoutWaitingForResponse(" ");
            clientThread.sendMessageWithoutWaitingForResponse(" ");
            clientThread.sendMessageWithoutWaitingForResponse(" ");
            clientThread.sendMessageWithoutWaitingForResponse(" ");
            clientThread.sendMessageWithoutWaitingForResponse(" ");
            clientThread.sendMessageWithoutWaitingForResponse("___|___");
        }
        if (lifes == 5) {
            clientThread.sendMessageWithoutWaitingForResponse(" ");
            clientThread.sendMessageWithoutWaitingForResponse("Nu ai nimerit, mai incearca!");
            clientThread.sendMessageWithoutWaitingForResponse("   |");
            clientThread.sendMessageWithoutWaitingForResponse("   |");
            clientThread.sendMessageWithoutWaitingForResponse("   |");
            clientThread.sendMessageWithoutWaitingForResponse("   |");
            clientThread.sendMessageWithoutWaitingForResponse("   |");
            clientThread.sendMessageWithoutWaitingForResponse("   |");
            clientThread.sendMessageWithoutWaitingForResponse("   |");
            clientThread.sendMessageWithoutWaitingForResponse("___|___");
        }
        if (lifes == 4) {
            clientThread.sendMessageWithoutWaitingForResponse(" ");
            clientThread.sendMessageWithoutWaitingForResponse("Nu ai nimerit, mai incearca!");
            clientThread.sendMessageWithoutWaitingForResponse("   ____________");
            clientThread.sendMessageWithoutWaitingForResponse("   | ");
            clientThread.sendMessageWithoutWaitingForResponse("   | ");
            clientThread.sendMessageWithoutWaitingForResponse("   | ");
            clientThread.sendMessageWithoutWaitingForResponse("   | ");
            clientThread.sendMessageWithoutWaitingForResponse("   | ");
            clientThread.sendMessageWithoutWaitingForResponse("   | ");
            clientThread.sendMessageWithoutWaitingForResponse("   | ");
            clientThread.sendMessageWithoutWaitingForResponse("___|___");
        }
        if (lifes == 3) {
            clientThread.sendMessageWithoutWaitingForResponse(" ");
            clientThread.sendMessageWithoutWaitingForResponse("Nu ai nimerit, mai incearca!");
            clientThread.sendMessageWithoutWaitingForResponse("   ____________");
            clientThread.sendMessageWithoutWaitingForResponse("   |          _|_");
            clientThread.sendMessageWithoutWaitingForResponse("   |         /   \\");
            clientThread.sendMessageWithoutWaitingForResponse("   |        |     |");
            clientThread.sendMessageWithoutWaitingForResponse("   |         \\_ _/");
            clientThread.sendMessageWithoutWaitingForResponse("   |");
            clientThread.sendMessageWithoutWaitingForResponse("   |");
            clientThread.sendMessageWithoutWaitingForResponse("   |");
            clientThread.sendMessageWithoutWaitingForResponse("___|___");
        }
        if (lifes == 2) {
            clientThread.sendMessageWithoutWaitingForResponse(" ");
            clientThread.sendMessageWithoutWaitingForResponse("Nu ai nimerit, mai incearca!");
            clientThread.sendMessageWithoutWaitingForResponse("   ____________");
            clientThread.sendMessageWithoutWaitingForResponse("   |          _|_");
            clientThread.sendMessageWithoutWaitingForResponse("   |         /   \\");
            clientThread.sendMessageWithoutWaitingForResponse("   |        |     |");
            clientThread.sendMessageWithoutWaitingForResponse("   |         \\_ _/");
            clientThread.sendMessageWithoutWaitingForResponse("   |           |");
            clientThread.sendMessageWithoutWaitingForResponse("   |           |");
            clientThread.sendMessageWithoutWaitingForResponse("   |");
            clientThread.sendMessageWithoutWaitingForResponse("___|___");
        }
        if (lifes == 1) {
            clientThread.sendMessageWithoutWaitingForResponse(" ");
            clientThread.sendMessageWithoutWaitingForResponse("Nu ai nimerit, mai incearca!");
            clientThread.sendMessageWithoutWaitingForResponse("   ____________");
            clientThread.sendMessageWithoutWaitingForResponse("   |          _|_");
            clientThread.sendMessageWithoutWaitingForResponse("   |         /   \\");
            clientThread.sendMessageWithoutWaitingForResponse("   |        |     |");
            clientThread.sendMessageWithoutWaitingForResponse("   |         \\_ _/");
            clientThread.sendMessageWithoutWaitingForResponse("   |           |");
            clientThread.sendMessageWithoutWaitingForResponse("   |           |");
            clientThread.sendMessageWithoutWaitingForResponse("   |          / \\ ");
            clientThread.sendMessageWithoutWaitingForResponse("___|___      /   \\");
        }
        if (lifes == 0) {
            clientThread.sendMessageWithoutWaitingForResponse(" ");
            clientThread.sendMessageWithoutWaitingForResponse("AI PIERDUT!");
            clientThread.sendMessageWithoutWaitingForResponse("   ____________");
            clientThread.sendMessageWithoutWaitingForResponse("   |          _|_");
            clientThread.sendMessageWithoutWaitingForResponse("   |         /   \\");
            clientThread.sendMessageWithoutWaitingForResponse("   |        |     |");
            clientThread.sendMessageWithoutWaitingForResponse("   |         \\_ _/");
            clientThread.sendMessageWithoutWaitingForResponse("   |          _|_");
            clientThread.sendMessageWithoutWaitingForResponse("   |         / | \\");
            clientThread.sendMessageWithoutWaitingForResponse("   |          / \\ ");
            clientThread.sendMessageWithoutWaitingForResponse("___|___      /   \\");
            clientThread.sendMessageWithoutWaitingForResponse(" ");
            clientThread.sendMessageWithoutWaitingForResponse(" ");
            clientThread.sendMessageWithoutWaitingForResponse("AI PIERDUT! Cuvantul era: " + wordToGuess.getText());
        }
    }

    /**
     * Primeste o litera de la jucator
     * Aceasta litera trece prin mai multe etape de verificare, precum daca a fost deja ghicita, daca e o litera nou ghicita, sau daca nu a ghicit, decremantand dumarul de vieti
     *
     * @param guess
     * @throws IOException
     */
    public void checkLetter(String guess) throws IOException {

        String newWord = "";
        for (int index = 0; index < wordToGuess.getText().length(); index++) {
            if (wordGuessed.getText().charAt(index) == guess.charAt(0)) {
                clientThread.sendMessageWithoutWaitingForResponse(" ");
                clientThread.sendMessageWithoutWaitingForResponse("Deja ai ghicit aceasta litera! Incearca una noua!");
                return;
            }
            if (wordToGuess.getText().charAt(index) == guess.charAt(0)) {
                newWord += guess.charAt(0);
            } else if (wordGuessed.getText().charAt(index) != '*') {
                newWord += wordToGuess.getText().charAt(index);
            } else {
                newWord += "*";
            }
        }
        if (wordGuessed.getText().equals(newWord)) {
            lifes--;
            hangImage();
        } else {
            wordGuessed.setText(newWord);
        }
        if (wordGuessed.getText().equals(wordToGuess.getText())) {
            clientThread.sendMessageWithoutWaitingForResponse(" ");
            clientThread.sendMessageWithoutWaitingForResponse(" ");
            clientThread.sendMessageWithoutWaitingForResponse("Corect! Ai castigat! Cuvantul era: ");
            clientThread.sendMessageWithoutWaitingForResponse(">>> " + wordToGuess.getText() + " <<<");
            lifes = 0;

            // Adaug un punct scorului sau de la acest joc
            HangmanScore hangmanScore = ScoreController.getHangmanScoreByUserId(clientThread.getUser().getId());
            if(hangmanScore == null) {
                hangmanScore = new HangmanScore(
                        ScoreController.getNextHangmanScoreAvailableId(), clientThread.getUser().getId(), 0
                );
                ScoreController.saveHangmanScore(hangmanScore);
            }

            hangmanScore.setScore(hangmanScore.getScore() + 1);
            ScoreController.updateHangmanScore(hangmanScore);
            clientThread.sendMessageWithoutWaitingForResponse(" ");
            clientThread.sendMessageWithoutWaitingForResponse("Ai primit un punct!");

            return;
        }
    }

    /**
     * Inceputul propriu-zis a jocului, prin care acesta introduce litere pana cand a terminat de ghicit cuvantul sau nu mai are vieti disponibile
     *
     * @throws IOException
     */
    public void startGame() throws IOException {
        welcome();
        gameLevel();
        initGuess();
        while (lifes > 0) {
            clientThread.sendMessageWithoutWaitingForResponse(" ");
            clientThread.sendMessageWithoutWaitingForResponse("Pana acum, ai ghicit cuvantul:");
            clientThread.sendMessageWithoutWaitingForResponse("  >> " + wordGuessed.getText() + " <<");
            clientThread.sendMessageWithoutWaitingForResponse(" ");
            String letter = clientThread.sendMessageAndWaitForResponse("Ghiceste o noua litera:");

            if (letter.length() > 1) {
                clientThread.sendMessageWithoutWaitingForResponse("Poti introduce doar o litera!");
                continue;
            }
            checkLetter(letter);
        }
    }

    /**
     * initializeaza vietile jucatorului cu 7, specifice desenelor ce reprezinta nivelele jocului Spanzuratoarea
     *
     * @param clientThreads
     */
    public void initialize(List<ClientThread> clientThreads) {
        lifes = 7;
    }
}
