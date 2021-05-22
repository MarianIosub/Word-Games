package pa.proj.word_games_client.panels;

import javax.swing.*;
import java.awt.*;

public class TypeFastGamePanel extends JPanel {
    private MainFrame mainFrame;

    private JLabel timerLabel;
    private JLabel wordsLabel;
    private JLabel inputLabel;
    private JTextField inputTbox;
    private JButton sendInputButton;

    private void init() {
        setLayout(new GridLayout(10, 10));

        timerLabel = new JLabel("S-au scurs 10 secunde");
        wordsLabel = new JLabel("ana, are, mere");
        inputLabel = new JLabel("Introdu primul cuvant:");
        inputTbox = new JTextField();
        sendInputButton = new JButton("Trimite");

        sendInputButton.addActionListener(actionEvent -> {
            // TODO: send word to server
            System.out.println("x");
        });

        add(timerLabel);
        add(wordsLabel);
        add(inputLabel);
        add(inputLabel);
        add(sendInputButton);
    }

    public TypeFastGamePanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        init();
    }
}
