package client;

import server.ServerGUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

public class ClientGUI extends JFrame{
    private static final int WIDTH = 300;
    private static final int HEIGHT = 600;
    private boolean connectToServer = false;
    JButton btnSend, btnConnect;
    JTextField ipAdr, portNum, login, sendMessage;
    JPasswordField pass;
    public JTextArea chat = new JTextArea();

    public ClientGUI(ServerGUI serverGUI) {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(WIDTH, HEIGHT);
        setLocationRelativeTo(null);
        setBounds(200, 200, WIDTH, HEIGHT);
        setTitle("Chat Server");
        setResizable(false);

        ipAdr = new JTextField("192.168.1.1", 12);
        portNum = new JTextField("8080", 5);
        login = new JTextField(10);
        pass = new JPasswordField("Пароль", 10);
        pass.setEchoChar('*');
        btnConnect = new JButton("Connect");

        btnSend = new JButton("Send message");
        sendMessage = new JTextField();

        //нижняя панель для отправки сообщений
        JPanel panBottom = new JPanel(new GridLayout(1, 2));
        panBottom.add(sendMessage);
        panBottom.add(btnSend);

        //верхняя панель авторизации
        JPanel panelAuth4 = new JPanel(new GridLayout(1,1));
        panelAuth4.add(new JLabel("Сервер:                               Порт:"));
        JPanel panelAuth1 = new JPanel(new GridLayout(1, 3));
        panelAuth1.add(ipAdr);
        panelAuth1.add(portNum);
        JPanel panelAuth3 = new JPanel(new GridLayout(1,1));
        panelAuth3.add(new JLabel("Логин:                   Пароль:"));
        JPanel panelAuth2 = new JPanel(new GridLayout(1, 3));
        panelAuth2.add(login);
        panelAuth2.add(pass);
        panelAuth2.add(btnConnect);
        JPanel panelAuth = new JPanel(new GridLayout(4, 1));
        panelAuth.add(panelAuth4);
        panelAuth.add(panelAuth1);
        panelAuth.add(panelAuth3);
        panelAuth.add(panelAuth2);

        //соединение с сервером
        btnConnect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (login.getText().isEmpty()) {
                    chat.append("Введите логин!\n");
                    return;
                }

                chat.append("Connecting to server...\n");
                if (!serverGUI.isIsServerWorking()) {
                    chat.append("Server is down\n");
                    return;
                }
                //chat.setText("");
                connectToServer = true;
                connectToServer(serverGUI);
                chat.append("Connect to server successfully\n");
                for (int i = 0; i < serverGUI.downloadHistory().size(); i++) {
                    chat.append(serverGUI.downloadHistory().get(i));
                    chat.append("\n");
                    if (i==10) break;

                }
                chat.append("\n");
                chat.append("Welcome, " + login.getText() + "!\n");
                serverGUI.connectedToChat(login.getText());
                panelAuth.setVisible(false);
                panBottom.setVisible(true);
            }
        });

        //отправка сообщения по нажатию Enter
        Action action = new AbstractAction()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                sendingMessage(serverGUI, panBottom, panelAuth);
            }
        };
        sendMessage.addActionListener(action);

        //Отправка сообщения по нажатию кнопки
        btnSend.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendingMessage(serverGUI, panBottom, panelAuth);
            }
        });

        add(panelAuth, BorderLayout.NORTH);
        add(panBottom, BorderLayout.SOUTH);
        panBottom.setVisible(false);
        add(chat, BorderLayout.CENTER);
        setVisible(true);
    }

    public void connectToServer(ServerGUI serverGUI){
        serverGUI.connectUser(this);
    }
    public void sendingMessage(ServerGUI serverGUI, JPanel panBottom, JPanel panelAuth) {
        if (!serverGUI.isIsServerWorking()) {
            serverGUI.disconnectUser(this);
            chat.append("Server disconnected\n");
            panBottom.setVisible(false);
            panelAuth.setVisible(true);
            return;
        }
        if(sendMessage.getText().isEmpty()) return;
        serverGUI.sendMessage(login.getText(), sendMessage.getText());
        for (int i = 0; i < serverGUI.getClientGUIList().size(); i++) {
            serverGUI.getClientGUIList().get(i).chat.append(login.getText() + ": " + sendMessage.getText() + "\n");
        }
        //chat.append(login.getText() + ": " + sendMessage.getText() + "\n");
        sendMessage.setText("");
    }
}
