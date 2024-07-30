package server;

import client.ClientGUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ServerGUI extends JFrame {
    private static final int WIDTH = 400;
    private static final int HEIGHT = 300;
    private boolean isServerWorking = false;

    JButton btnStart, btnStop;

    JTextArea chat = new JTextArea();

    List<ClientGUI> clientGUIList;

    public ServerGUI() {
        clientGUIList = new ArrayList<>();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(WIDTH, HEIGHT);
        setLocationRelativeTo(null);

        setTitle("Chat Server");
        setResizable(false);
        btnStart = new JButton("START");
        btnStop = new JButton("STOP");

        btnStop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                chat.append("Chat stopped\n");
                for (int i = 0; i < getClientGUIList().size(); i++) {
                    getClientGUIList().get(i).chat.append("Server disconnected\n");
                }
                //writeToFile();
                isServerWorking = false;
            }
        });

        btnStart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                chat.append("Chat started...\n");
                for (int i = 0; i < getClientGUIList().size(); i++) {
                    getClientGUIList().get(i).chat.append("Connection resumed\n");
                }
                //writeToFile();
                isServerWorking = true;
            }
        });

        JPanel panBottom = new JPanel(new GridLayout(1, 2));
        panBottom.add(btnStart);
        panBottom.add(btnStop);

        add(panBottom, BorderLayout.SOUTH);
        add(chat);
        setVisible(true);
    }

    public void connectUser(ClientGUI clientGUI) {
        clientGUIList.add(clientGUI);
    }

    public List<ClientGUI> getClientGUIList() {
        return clientGUIList;
    }

    public void disconnectUser(ClientGUI clientGUI){
        clientGUIList.remove(clientGUI);
    }
    public boolean isIsServerWorking() {
        return isServerWorking;
    }

    public void connectedToChat(String login) {
        chat.append(login + " joined to server\n");
        for (int i = 0; i < getClientGUIList().size(); i++) {
            this.getClientGUIList().get(i).chat.append(login + " joined to server\n");
        }
        writeToFile();
    }

    public void sendMessage(String login, String message) {
        chat.append(login + ": " + message + "\n");
        writeToFile();
    }

    public void writeToFile() {
        int length = chat.getText().split("\n").length;
        String lastStr = (chat.getText().split("\n"))[length - 1];
        FileWriter logChat;
        {
            try {
                logChat = new FileWriter("logChat.txt", true);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        try {
            logChat.write(lastStr);
            logChat.write("\n");
            logChat.close();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public List<String> downloadHistory() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("logChat.txt"));
            String line;
            List<String> history = new LinkedList<>();
            int i = 0;
            while ((line = reader.readLine()) != null) {
                history.add(i, line);
            }
            reader.close();
            return history.reversed();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
