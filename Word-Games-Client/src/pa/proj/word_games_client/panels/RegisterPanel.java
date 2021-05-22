package pa.proj.word_games_client.panels;

import javax.swing.*;
import java.awt.*;

public class RegisterPanel extends JPanel {
    private MainFrame mainFrame;
    private JLabel usernameLabel;
    private JTextField usernameTbox;
    private JLabel passwordLabel;
    private JTextField passwordTbox;
    private JButton registerButton;

    private void init() {
        setLayout(new GridLayout(10, 10));

        usernameLabel = new JLabel("Numele de utilizator: ");
        usernameTbox = new JTextField();
        passwordLabel = new JLabel("Parola: ");
        passwordTbox = new JTextField();
        registerButton = new JButton("Inregistrare");

        registerButton.addActionListener(actionEvent -> {
            System.out.println("x");
            // TODO: Trimite request la server
        });

        add(usernameLabel);
        add(usernameTbox);
        add(passwordLabel);
        add(passwordTbox);
        add(registerButton);
    }

    public RegisterPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        init();
    }
}
