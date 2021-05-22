package pa.proj.word_games_client.panels;

import javax.swing.*;
import java.awt.*;

public class LoginPanel extends JPanel {
    private MainFrame mainFrame;
    private JLabel username;
    private JTextField usernameBox;
    private JLabel password;
    private JTextField passwordBox;
    private JButton loginBtn;
    public LoginPanel(MainFrame mainFrame){
        this.mainFrame = mainFrame;
        init();
    }
    private void init(){
        setLayout(new GridLayout(10, 10));
        username=new JLabel("Numele de utilizator: ");
        usernameBox=new JTextField();
        password=new JLabel("Parola: ");
        passwordBox=new JTextField();
        loginBtn=new JButton("Login");

        //TODO
        loginBtn.addActionListener(actionEvent -> {
            mainFrame.showMenuPanel();
        });

        add(username);
        add(usernameBox);
        add(password);
        add(passwordBox);
        add(loginBtn);
    }
}
