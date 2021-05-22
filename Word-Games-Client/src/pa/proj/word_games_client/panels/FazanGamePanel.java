package pa.proj.word_games_client.panels;

import javax.swing.*;
import java.awt.*;

public class FazanGamePanel extends JPanel {
    private MainFrame mainFrame;
    private JLabel turnLabel;
    private JLabel lastWordLabel;
    private JLabel inputLabel;
    private JTextField inputTbox;
    private JButton sendInputButton;

    private void init() {
        setLayout(new GridLayout(10, 10));

        turnLabel = new JLabel("Este tura jucatorului PLAYER_NAME");
        lastWordLabel = new JLabel("Ultimul cuvant introdus este \"anarhie\"");
        inputLabel = new JLabel("Introdu un cuvant:");
        inputTbox = new JTextField();
        sendInputButton = new JButton("Trimite");

        sendInputButton.addActionListener(actionEvent -> {
            // TODO: send word to server
            System.out.println("x");
        });

        add(turnLabel);
        add(lastWordLabel);
        add(inputLabel);
        add(inputTbox);
        add(sendInputButton);
    }

    public FazanGamePanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        init();
    }
}
