package pa.proj.word_games.games;

import pa.proj.word_games.controllers.WordController;
import pa.proj.word_games.managers.EntityFactoryManager;
import pa.proj.word_games.models.Word;

import java.io.IOException;
import java.util.Scanner;

public class HangMan {
    private static Word wordToGuess=new Word();
    private static Word wordGuessed=new Word();
    private static Integer lifes;
    static Scanner scanner = null;
    private static Player player;

    public HangMan() {
        EntityFactoryManager.getInstance();
        this.lifes = 7;
        scanner = new Scanner(System.in);
    }
    public void addPlayer(Player player){
        this.player=player;
    }
    public static void gameLevel() throws IOException {
        System.out.println("Alege nivelul de joc pe care il doresti: Usor, Mediu, Greu!");
        System.out.print("  >>");
        String level = scanner.nextLine();
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
                System.out.println("Nivelul de joc introdus nu corespunde! Reincercati:");
                gameLevel();
            }
        }
    }
    public static void welcome(){
        System.out.println("Salut "+player.getName()+" si bine ai venit la jocul Spânzurătoarea!");
        System.out.println("Regulile sunt dupa cum urmeaza:");
        System.out.println("-incearca sa ghicesti pe rand fiecare litera pana cuvantul se umple;");
        System.out.println("-ai doar 7 vieti disponibile pana vei fi spanzurat;");
        System.out.println("-ai grija la literele deja folosite sa nu le refolosesti inutil;");
        System.out.println("MULT SUCCES!");
    }
    public static void initGuess(){
        wordGuessed.setText("");
        for(int index=0;index<wordToGuess.getText().length();index++){
            wordGuessed.setText(wordGuessed.getText()+"*");

        }
        System.out.println(wordGuessed.getText());
    }
    public static void hangImage(){
        if (lifes == 6) {
            System.out.println("Nu ai nimerit, reincearca!");
            System.out.println();
            System.out.println();
            System.out.println();
            System.out.println();
            System.out.println("___|___");
            System.out.println();
        }
        if (lifes == 5) {
            System.out.println("Nu ai nimerit, reincearca!");
            System.out.println("   |");
            System.out.println("   |");
            System.out.println("   |");
            System.out.println("   |");
            System.out.println("   |");
            System.out.println("   |");
            System.out.println("   |");
            System.out.println("___|___");
        }
        if (lifes == 4) {
            System.out.println("Nu ai nimerit, reincearca!");
            System.out.println("   ____________");
            System.out.println("   |");
            System.out.println("   |");
            System.out.println("   |");
            System.out.println("   |");
            System.out.println("   |");
            System.out.println("   |");
            System.out.println("   | ");
            System.out.println("___|___");
        }
        if (lifes == 3) {
            System.out.println("Nu ai nimerit, reincearca!");
            System.out.println("   ____________");
            System.out.println("   |          _|_");
            System.out.println("   |         /   \\");
            System.out.println("   |        |     |");
            System.out.println("   |         \\_ _/");
            System.out.println("   |");
            System.out.println("   |");
            System.out.println("   |");
            System.out.println("___|___");
        }
        if (lifes == 2) {
            System.out.println("Nu ai nimerit, reincearca!");
            System.out.println("   ____________");
            System.out.println("   |          _|_");
            System.out.println("   |         /   \\");
            System.out.println("   |        |     |");
            System.out.println("   |         \\_ _/");
            System.out.println("   |           |");
            System.out.println("   |           |");
            System.out.println("   |");
            System.out.println("___|___");
        }
        if (lifes == 1) {
            System.out.println("Nu ai nimerit, reincearca!");
            System.out.println("   ____________");
            System.out.println("   |          _|_");
            System.out.println("   |         /   \\");
            System.out.println("   |        |     |");
            System.out.println("   |         \\_ _/");
            System.out.println("   |           |");
            System.out.println("   |           |");
            System.out.println("   |          / \\ ");
            System.out.println("___|___      /   \\");
        }
        if (lifes == 0) {
            System.out.println("AI PIERDUT!");
            System.out.println("   ____________");
            System.out.println("   |          _|_");
            System.out.println("   |         /   \\");
            System.out.println("   |        |     |");
            System.out.println("   |         \\_ _/");
            System.out.println("   |          _|_");
            System.out.println("   |         / | \\");
            System.out.println("   |          / \\ ");
            System.out.println("___|___      /   \\");
            System.out.println("AI PIERDUT! Cuvantul era: " + wordToGuess.getText());
        }
    }
    public static void checkLetter(String guess){
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
            System.out.println("Corect! Ai castigat! Cuvantul era: ");
            System.out.println(">>> "+wordToGuess.getText()+" <<<" );
            lifes=0;
            return;
        }
    }
    public void hangManGame() throws IOException {
        welcome();
        gameLevel();
        initGuess();
        while(lifes>0){
            System.out.println("Pana acum, ai ghicit cuvantul:");
            System.out.println("  >> "+wordGuessed.getText()+" <<");
            System.out.println("Ghiceste o noua litera:");
            System.out.print("  >> ");
            String letter= scanner.nextLine();
            if(letter.length()>1){
                System.out.println("Poti introduce doar o litera!");
                continue;
            }
            checkLetter(letter);
        }
    }
}
