package pa.proj.word_games_client.panels;
import javax.swing.*;
import java.awt.*;
public class HangmanGamePanel extends JPanel {
    private MainFrame mainFrame;
    private JLabel hangmanLevel;
    private JLabel guessedWordText;
    private JLabel guessedWord;
    private JLabel letterText;
    private JTextField letterToSend;
    private JButton send;
    public HangmanGamePanel(MainFrame mainFrame){
        this.mainFrame=mainFrame;
        init();
    }

    private void init() {
        hangmanLevel=new JLabel("Nivelul de vieti");
        guessedWordText=new JLabel("Pana acum ai ghicit cuvantul");
        guessedWord=new JLabel("<<cuvantul ghicit>>");
        letterText=new JLabel("Introdu litera pe care o doresti:");
        letterToSend=new JTextField("litera");
        send=new JButton("Trimite");
        send.addActionListener(actionEvent -> {
            System.out.println("x");
            // TODO: Trimite request la server
        });
        add(hangmanLevel);
        add(new JSeparator());
        add(guessedWordText);
        add(guessedWord);
        add(new JSeparator());
        add(letterText);
        add(letterToSend);
        add(send);


    }

}
