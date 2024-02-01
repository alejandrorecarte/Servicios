package Chat.controllers;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

public class mainFrame {
    private JPanel hostPanel;
    private JPanel joinPanel;
    private JLabel hostAChatServerLabel;
    private JTextField hostIpField;
    private JTextField hostPortField;
    private JLabel yourHostPortLabel;
    private JLabel hostPasswordLabel;
    private JButton createServerButton;
    private JLabel joinAServerLabel;
    private JLabel hostIPLabel;
    private JPasswordField hostPasswordField;
    private JLabel hostPortLabel;
    private JTextField joinHostPortField;
    private JTextField joinHostPort;
    private JPasswordField joinPasswordField;
    private JLabel joinUsernameLabel;
    private JTextField joinUsernameField;
    private JButton joinServerButton;
    private JLabel joinPasswordLabel;
    private JPanel mainPanel;
    private JButton savePreferencesButton;
    public static LinkedList<String> messages = new LinkedList<String>();;
    private static final Set<PrintWriter> writers = new HashSet<>();
    public static ServerSocket serverSocket;
    public static JFrame hostServerFrame;

    public mainFrame() {
        hostPanel.setBorder(new LineBorder(Color.BLACK, 1));
        joinPanel.setBorder(new LineBorder(Color.BLACK, 1));
        mainPanel.setBorder(new LineBorder(Color.BLACK, 1));

        createServerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if( !String.valueOf(hostPortField.getText()).equals("") && !String.valueOf(hostPasswordField.getText()).equals("")) {
                    JFrame frame = new JFrame("Chat Server");
                    frame.setContentPane(new hostServerFrame(Integer.parseInt(hostPortField.getText())).mainPanel);
                    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    frame.pack();
                    frame.setVisible(true);
                    frame.setBounds(0, 0, 600, 400);
                    hostServerFrame = frame;
                    startServer();
                }
            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Chat Server");
        frame.setContentPane(new mainFrame().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setBounds(0,0,600,400);
    }

    private void startServer() {
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                try {
                    serverSocket = new ServerSocket(Integer.parseInt(hostPortField.getText()));
                    messages.add("Chat Server is running...");
                    while (true) {
                        new Handler(serverSocket.accept()).start();
                    }
                } catch (SocketException e) {
                    if(e.getMessage().equals("Interrupted function call: accept failed")){
                        messages.add("Server closed");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void done() {
                // Puedes realizar acciones después de que el servidor haya terminado
                // Esto se ejecutará en el hilo de despacho de eventos de Swing
            }
        };
        worker.execute();
    }

    private class Handler extends Thread {
        private final Socket socket;
        private BufferedReader reader;
        private PrintWriter writer;

        public Handler(Socket socket) {
            this.socket = socket;
        }

        @Override

        public void run() {
            try {
                reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                writer = new PrintWriter(socket.getOutputStream(), true);
                writers.add(writer);

                while (true) {
                    String message = reader.readLine();
                    if (message == null) {
                        return;
                    }
                    System.out.println(message);
                    messages.add(message);

                    broadcast(message, writer);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (writer != null) {
                    writers.remove(writer);
                }
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void broadcast(String message, PrintWriter messageWriter) {
        for (PrintWriter writer : writers) {
            if(writer.equals(messageWriter)){
                writer.println("-- Message Sent");
            }else{
                writer.println(message);
            }
        }
    }
}

