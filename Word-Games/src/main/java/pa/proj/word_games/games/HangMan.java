package pa.proj.word_games.games;

import com.sun.security.ntlm.Client;
import pa.proj.word_games.controllers.WordController;
import pa.proj.word_games.managers.EntityFactoryManager;
import pa.proj.word_games.models.Word;
import pa.proj.word_games.server.threads.ClientThread;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class HangMan implements AbstractGame {
    private static Word wordToGuess=new Word();
    private static Word wordGuessed=new Word();
    private static Integer lifes;
    static Scanner scanner = null;
    private static Player player;
    private static ClientThread clientThread;

    public HangMan(ClientThread clientThread) {
        HangMan.clientThread = clientThread;
    }
    public void addPlayer(Player player){
        this.player=player;
    }
    public static void gameLevel() throws IOException {
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
    public static void welcome() throws IOException {
        clientThread.sendMessageWithoutWaitingForResponse("Salut " + player.getName() + " si bine ai venit la jocul Spânzurătoarea!");
        clientThread.sendMessageWithoutWaitingForResponse("Regulile sunt dupa cum urmeaza:");
        clientThread.sendMessageWithoutWaitingForResponse("\t- incearca sa ghicesti pe rand fiecare litera pana cuvantul se umple;");
        clientThread.sendMessageWithoutWaitingForResponse("\t- ai doar 7 vieti disponibile pana vei fi spanzurat;");
        clientThread.sendMessageWithoutWaitingForResponse("\t- ai grija la literele deja folosite sa nu le refolosesti inutil;");
        clientThread.sendMessageWithoutWaitingForResponse("MULT SUCCES!");
    }
    public static void initGuess() throws IOException {
        wordGuessed.setText("");
        for(int index=0;index<wordToGuess.getText().length();index++){
            wordGuessed.setText(wordGuessed.getText()+"*");

        }
        clientThread.sendMessageWithoutWaitingForResponse(wordGuessed.getText());
    }
    public static void hangImage() throws IOException {
        if (lifes == 6) {
            clientThread.sendMessageWithoutWaitingForResponse("Nu ai nimerit, reincearca!");
            clientThread.sendMessageWithoutWaitingForResponse(" ");
            clientThread.sendMessageWithoutWaitingForResponse(" ");
            clientThread.sendMessageWithoutWaitingForResponse(" ");
            clientThread.sendMessageWithoutWaitingForResponse(" ");
            clientThread.sendMessageWithoutWaitingForResponse("___|___");
            clientThread.sendMessageWithoutWaitingForResponse(" ");
        }
        if (lifes == 5) {
            clientThread.sendMessageWithoutWaitingForResponse("Nu ai nimerit, reincearca!");
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
            clientThread.sendMessageWithoutWaitingForResponse("Nu ai nimerit, reincearca!");
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
            clientThread.sendMessageWithoutWaitingForResponse("Nu ai nimerit, reincearca!");
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
            clientThread.sendMessageWithoutWaitingForResponse("Nu ai nimerit, reincearca!");
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
            clientThread.sendMessageWithoutWaitingForResponse("Nu ai nimerit, reincearca!");
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
            clientThread.sendMessageWithoutWaitingForResponse("AI PIERDUT! Cuvantul era: " + wordToGuess.getText());
        }
    }
    public static void checkLetter(String guess) throws IOException {
        String newWord="";
        for(int index=0;index<wordToGuess.getText().length();index++){
            if(wordToGuess.getText().charAt(index)==guess.charAt(0)){
                newWord+=guess.charAt(0);
            }else if(wordGuessed.getText().charAt(index)!='*'){
                newWord+=wordToGuess.getText().charAt(index);
            }else{
                newWord+="*";
            }
        }
        if(wordGuessed.getText().equals(newWord)){
            lifes--;
            hangImage();
        }else{
            wordGuessed.setText(newWord);
        }
        if(wordGuessed.getText().equals(wordToGuess.getText())){
            clientThread.sendMessageWithoutWaitingForResponse("Corect! Ai castigat! Cuvantul era: ");
            clientThread.sendMessageWithoutWaitingForResponse(">>> "+wordToGuess.getText()+" <<<" );
            lifes=0;
            return;
        }
    }
    public void startGame() throws IOException {
        welcome();
        gameLevel();
        initGuess();
        while(lifes>0){
            clientThread.sendMessageWithoutWaitingForResponse("Pana acum, ai ghicit cuvantul:");
            clientThread.sendMessageWithoutWaitingForResponse("  >> "+wordGuessed.getText()+" <<");
            String letter = clientThread.sendMessageAndWaitForResponse("Ghiceste o noua litera:");

            if(letter.length()>1){
                clientThread.sendMessageWithoutWaitingForResponse("Poti introduce doar o litera!");
                continue;
            }
            checkLetter(letter);
        }
    }

    public void initialize(List<ClientThread> clientThreads) {
        lifes = 7;
        addPlayer(new Player("PlayerName")); // TODO: de scos
    }
}
