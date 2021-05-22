package pa.proj.word_games_client.panels;

import javax.swing.*;
import java.awt.*;

public class MenuPanel extends JPanel {
    private MainFrame mainFrame;

    private JLabel joinLobbyLabel;
    private JTextField joinLobbyTbox;
    private JButton joinLobbyButton;

    private JButton createLobbyButton;

    private JButton startGameButton;

    private JLabel statisticsLabel;
    private JLabel fazanGameStatisticsLabel;
    private JLabel hangmanGametatisticsLabel;
    private JLabel typeFastGameStatisticsLabel;

    private void init() {
        setLayout(new GridLayout(10, 10));

        // Intrara intr-o camera
        joinLobbyLabel = new JLabel("Codul de accesare al camerei: ");
        joinLobbyTbox = new JTextField();
        joinLobbyButton = new JButton("Intra intr-o camera");

        joinLobbyButton.addActionListener(actionEvent -> {
            // TODO: go to lobbypanel (request la server)
            System.out.println("x");
        });

        add(joinLobbyLabel);
        add(joinLobbyTbox);
        add(joinLobbyButton);

        add(new JSeparator());

        // Creeaza o camera
        createLobbyButton = new JButton("Creeaza o camera");

        createLobbyButton.addActionListener(actionEvent -> {
            mainFrame.showCreateLobbyPanel();
        });

        add(createLobbyButton);

        add(new JSeparator());

        // Incepe un joc
        startGameButton = new JButton("Incepe jocul");

        startGameButton.addActionListener(actionEvent -> {
            // TODO: panel joc
            System.out.println("x");
        });

        add(startGameButton);

        add(new JSeparator());

        // Statistici
        statisticsLabel = new JLabel("Statistica punctajelor tale:");
        fazanGameStatisticsLabel = new JLabel("Ai 2 puncte la jocul Fazan"); // TODO: request server
        hangmanGametatisticsLabel = new JLabel("Ai 2 puncte la jocul Spanzuratoarea"); // TODO: request server
        typeFastGameStatisticsLabel = new JLabel("Ai 2 puncte la jocul TypeFast"); // TODO: request server

        add(statisticsLabel);
        add(fazanGameStatisticsLabel);
        add(hangmanGametatisticsLabel);
        add(typeFastGameStatisticsLabel);
    }

    public MenuPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        init();
    }
}
