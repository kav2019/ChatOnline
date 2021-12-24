package client.handler;

import client.Client;
import client.controllers.ChatController;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Worker {

    private static final String AUTH_CMD_PREFIX = "/chatauth";
    private static final String AUTHok_CMD_PREFIX = "/authok";
    private static final String AUTHerr_CMD_PREFIX = "/autherr";
    private static final String PRIVATE_MSG_PREFIX = "/private";
    private static final String SERVER_MSG_PREFIX = "/server";
    private static final String ALL_MSG_PREFIX = "/all";
    private static final String END_CMD = "/end";

    private final int SERVER_SOCKET = 8089;
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    private static String login;
    private static String password;
    private static String name;
    public ChatController controller;
    private  String nick;
    private Client client;

    public void setClient(Client client) {
        this.client = client;
    }

    public void setController(ChatController controller) {
        this.controller = controller;
    }




    public Worker(){
        goConnect();
    }

    private void goConnect() {
        try {
            socket = new Socket("localhost", SERVER_SOCKET);
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            threadWaitMsg();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void threadWaitMsg() {
        Thread thread = new Thread( () -> {
            while (true){
                try {
                    String message = in.readUTF();
                    if(message.startsWith(AUTHok_CMD_PREFIX)){
                        setNick(message);
                    }
                    parseMsg(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    private void setNick(String message) {
        String[] arrWord = message.split("\\s+", 2);
        nick = arrWord[1];
        try {
            client.openChatWindow();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public String getNick() {
        return nick;
    }

    private void parseMsg(String message) {
        String[] parts = message.split("\\s+", 3);
        if (parts[0].startsWith(AUTHok_CMD_PREFIX)){
            name = parts[1];
        }
        else if (parts[0].startsWith(ALL_MSG_PREFIX)){
            controller.addMsgToChat(parts[1], parts[2]);
        }
    }

    public Socket getSocket() {
        return socket;
    }

    public void isAuth() throws IOException {
        String msgAuth = String.format("%s %s %s",AUTH_CMD_PREFIX, login, password);
        sendMsg(msgAuth);
    }

    public void sendMsg(String massage) throws IOException {
        if (nick != null){
            out.writeUTF(String.format("%s %s %s", ALL_MSG_PREFIX, nick, massage));
            return;
        }
        System.out.println("worker письмо получил");
        out.writeUTF(massage);
        System.out.println("worker письмо отправил");

    }

    public static void setLogin(String login) {
        Worker.login = login;
    }

    public static void setPassword(String password) {
        Worker.password = password;
    }

    public void close(){
        try {
            socket.close();
        } catch (IOException e) {

        }
    }
}
