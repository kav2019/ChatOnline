package chat.handler;




import chat.MyServer;
import chat.User;
import chat.auth.Auth;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ClientHandler {
    private static final String AUTH_CMD_PREFIX = "/chat/auth";
    private static final String AUTHok_CMD_PREFIX = "/authok";
    private static final String AUTHerr_CMD_PREFIX = "/autherr";
    private static final String PRIVATE_MSG_PREFIX = "/private";
    private static final String SERVER_MSG_PREFIX = "/server";
    private static final String ALL_MSG_PREFIX = "/all";
    private static final String END_CMD = "/end";



    private final MyServer myServer;
    private Socket clientSocket;
    private DataOutputStream out;
    private DataInputStream in;
    private Auth auth;
    private static List<String> clientsInChat;


    public ClientHandler(MyServer myServer, Socket clientSocket) {
        this.clientSocket = clientSocket;
        this.myServer = myServer;
        clientsInChat = new ArrayList<>();
    }

    public void handle() throws IOException {
        out =new DataOutputStream(clientSocket.getOutputStream());
        in = new DataInputStream((clientSocket.getInputStream()));
        threadInMsg();
//        threadOutMsg();
        auth = new Auth();

    }

//    private void threadOutMsg() {
//        Thread threadOut = new Thread(() -> {
//            Scanner scanner = new Scanner(System.in);
//            while (true) {
//                String msg = scanner.nextLine();
//                try {
//                    out.writeUTF(msg);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//        threadOut.start();
//    }

    private void threadInMsg() {
        Thread threadIN = new Thread(() -> {
            try {
                while (true){
                    String msg = in.readUTF();
                    String[] arrWord = parse(msg);
                    if(!auth(arrWord)){
                        continue;
                    }
                    sendMsg(arrWord);
                    //
                    System.out.println("Сообщение от клиента: " + msg);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        threadIN.start();
    }

    private String[] parse(String msg) {
        return msg.split("\\s+", 3);
    }

    private boolean auth(String[] arr) throws IOException {
        String login = arr[1];
        String password = arr[2];
        String nick = auth.getNick(login, password);
        if (nick != null){
            clientsInChat.add(nick);
            msgInfoInput(nick);
        }
        return false;
    }

    public void msgInfoInput(String nick) throws IOException {
        out.writeUTF(String.format(">>> %s подключился к чату", nick));
    }

    public void sendMsg(String[] arr) throws IOException {
        out.writeUTF(String.format("%s %s %s", ALL_MSG_PREFIX, arr[1], arr[2]));
    }
}
