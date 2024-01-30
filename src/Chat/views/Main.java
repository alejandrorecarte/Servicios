package Chat.views;

import javax.swing.*;

public class Main {
    private JPanel mainPanel;
    private JLabel hostAChatServerLabel;
    private JButton startButton;
    private JLabel hostIPLabel;
    private JTextField hostIPField;
    private JTextField hostPortField;
    private JButton joinButton;
    private JLabel hostPortLabel;
    private JLabel joinAChatServerLabel;
    private JLabel IPToConnectLabel;
    private JTextField IPToConnectField;
    private JLabel usernameLabel;
    private JTextField usernameField;
    private JLabel portToConnectLabel;
    private JTextField textField1;
    private JPanel joinPanel;
    private JPanel hostPanel;

    public static void main(String[] args) {
        JFrame frame = new JFrame("Main");
        frame.setContentPane(new Main().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setBounds(0,0,600,300);
    }
}

