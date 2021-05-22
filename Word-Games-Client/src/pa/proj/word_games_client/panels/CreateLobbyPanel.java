package pa.proj.word_games_client.panels;

import javax.swing.*;
import java.awt.*;

public class CreateLobbyPanel extends JPanel {
    private MainFrame mainFrame;
    private JLabel title;
    private JButton fastTypeBtn;
    private JButton hangManBtn;
    private JLabel fazanPlayers;
    private JTextField fazanNoPlayers;
    private JButton fazanBtn;

    public CreateLobbyPanel(MainFrame mainFrame) {

        this.mainFrame = mainFrame;
        init();

    }

    private void init() {
        setLayout(new GridLayout(10, 10));
        title=new JLabel("Alege jocul:");
        fastTypeBtn=new JButton("Fast type ");

        add(new JSeparator());

        hangManBtn=new JButton("Spanzuratoarea");

        add(new JSeparator());

        fazanPlayers=new JLabel("Numar jucatori: ");
        fazanBtn=new JButton("Fazan");
        fazanNoPlayers=new JTextField();
        fastTypeBtn.addActionListener(actionEvent -> {
            System.out.println("x");
            // TODO: Trimite request la server
        });
        hangManBtn.addActionListener(actionEvent -> {
            System.out.println("x");
            // TODO: Trimite request la server
        });
        fazanBtn.addActionListener(actionEvent -> {
            System.out.println("x");
            // TODO: Trimite request la server
        });
        add(title);
        add(fastTypeBtn);
        add(hangManBtn);
        add(fazanPlayers);
        add(fazanNoPlayers);
        add(fazanBtn);
    }
}
