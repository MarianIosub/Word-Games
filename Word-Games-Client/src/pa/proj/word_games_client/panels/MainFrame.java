package pa.proj.word_games_client.panels;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private MainPanel mainPanel;
    private LoginPanel loginPanel;
    private RegisterPanel registerPanel;
    private MenuPanel menuPanel;
    private CreateLobbyPanel createLobbyPanel;

    private void init() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        loginPanel = new LoginPanel(this);
        registerPanel = new RegisterPanel(this);
        mainPanel = new MainPanel(this);
        menuPanel = new MenuPanel(this);
        createLobbyPanel = new CreateLobbyPanel(this);

        add(mainPanel, BorderLayout.CENTER);

        pack();
    }

    public MainFrame() {
        super("Word Games");
        init();
    }

    public void showRegisterPanel() {
        mainPanel.setVisible(false);
        add(registerPanel, BorderLayout.CENTER);
        registerPanel.setVisible(true);
    }

    public void showLoginPanel() {
        mainPanel.setVisible(false);
        add(loginPanel,BorderLayout.CENTER);
        loginPanel.setVisible(true);
    }

    public void showMenuPanel() {
        loginPanel.setVisible(false);
        add(menuPanel,BorderLayout.CENTER);
        menuPanel.setVisible(true);
    }

    public void showCreateLobbyPanel() {
        menuPanel.setVisible(false);
        add(createLobbyPanel,BorderLayout.CENTER);
        createLobbyPanel.setVisible(true);
    }
}
