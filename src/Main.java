import client.ClientGUI;
import server.ServerGUI;

public class Main {
    public static void main(String[] args) {

        ServerGUI serverGUI = new ServerGUI();
        new ClientGUI(serverGUI);

    }
}