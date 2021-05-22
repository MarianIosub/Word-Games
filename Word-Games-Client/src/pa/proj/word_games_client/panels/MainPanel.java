package pa.proj.word_games_client.panels;

import javax.swing.*;
import java.awt.*;

public class MainPanel extends JPanel {
    private MainFrame mainFrame;
    private JButton loginButton;
    private JButton registerButton;

    private void init() {
        setLayout(new GridLayout(10, 10));

        loginButton = new JButton("Autentificare");
        registerButton = new JButton("Inregistrare");

        registerButton.addActionListener(actionEvent -> {
            mainFrame.showRegisterPanel();
        });
        loginButton.addActionListener(actionEvent -> {
            mainFrame.showLoginPanel();
        });

        add(loginButton);
        add(new JSeparator());
        add(registerButton);
    }

    public MainPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        init();
    }
}
