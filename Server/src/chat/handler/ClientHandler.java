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
    private static final String AUTH_CMD_PREFIX = "/chatauth";
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
    private boolean isAuth = false;
    private String nick;


    public ClientHandler(MyServer myServer, Socket clientSocket) {
        this.clientSocket = clientSocket;
        this.myServer = myServer;
        clientsInChat = new ArrayList<>();
    }

    public void handle() throws IOException {
        System.out.println("Клиент подключился...");
        out =new DataOutputStream(clientSocket.getOutputStream());
        in = new DataInputStream((clientSocket.getInputStream()));
        threadInMsg();
//        threadOutMsg();
        auth = new Auth();

    }



    private void threadInMsg() {
        Thread threadIN = new Thread(() -> {
            try {
                while (true){
                    String msg = in.readUTF();
                    System.out.println("Сообщение от клиента При создании потока: " + msg);
                    String[] arrWord = parse(msg);
//                    if(!auth(arrWord)){
//                        continue;
//                    }
                    choiseToDo(arrWord);
                    if (!isAuth){
                        continue;
                    }
                    //
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        threadIN.start();
    }

    public String getNick() {
        return nick;
    }

    private void authentication(String[] message) throws IOException {
        Auth auth = myServer.getAuth();
        String nick = auth.getNick(message[1], message[2]);
        System.out.println("АВТОРИЗАЦИЯ!!!!");
        if (nick != null){
            this.nick = nick;
            clientsInChat.add(nick);
            out.writeUTF(String.format("%s %s", AUTHok_CMD_PREFIX, nick));
            System.out.println("АВТОРИЗОВАЛСЯ!!!");
            isAuth = true;
            myServer.addClientsList(this);
        }
    }

    private void choiseToDo(String[] arrWord) throws IOException {
        if(arrWord[0].startsWith(ALL_MSG_PREFIX)){
//            sendMsg(arrWord);
            myServer.broadcastMsg(arrWord, this);
            System.out.println(String.format("%s: %s", arrWord[1], arrWord[2]));
        }
        else if(arrWord[0].startsWith(AUTH_CMD_PREFIX)){
            authentication(arrWord);
        }
        else if (arrWord[0].equals(PRIVATE_MSG_PREFIX)){
//            sendMsg(arrWord);
            myServer.broadcastPrivateMsg(arrWord, this);
            System.out.println(String.format("В clientHandler - choiseToDo от %s к %s: %s", arrWord[1], arrWord[2], arrWord[3]));
        }

        // кто кому что
    }

    private String[] parse(String msg) {
        if(msg.startsWith(PRIVATE_MSG_PREFIX)){
//            privateSendMsg(msg);
            return msg.split("\\s+", 4);
        }
        return msg.split("\\s+", 3);

    }

    private void privateSendMsg(String msg) {
        String[] arrWord = msg.split("\\s+", 4);
        try {
            sendMsg(arrWord);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // кто кому что

    public void sendMsg(String[] arr) throws IOException {
        if (arr[0].equals(ALL_MSG_PREFIX)) {
            out.writeUTF(String.format("%s %s %s", ALL_MSG_PREFIX, arr[1], arr[2]));
            System.out.println(String.format("Отправил ВСЕМ сообщение от %s: %s", arr[1], arr[2]));
        }else if (arr[0].equals(PRIVATE_MSG_PREFIX)){
            out.writeUTF(String.format("%s %s %s %s", PRIVATE_MSG_PREFIX, arr[1], arr[2], arr[3]));
            System.out.println(String.format("Отправил ПРИВАТНОЕ сообщение от %s: %s", arr[1], arr[2]));
        }
    }

    //    private boolean auth(String[] arr) throws IOException {
//        String login = arr[1];
//        String password = arr[2];
//        String nick = auth.getNick(login, password);
//        if (nick != null){
//            clientsInChat.add(nick);
//            msgInfoInput(nick);
//            sendMsgAuthOk(nick);
//        }
//        return false;
//    }

    //    private void sendMsgAuthOk(String nick) throws IOException {
//        out.writeUTF(String.format("%s %s", AUTHok_CMD_PREFIX, nick, "Авторизация"));
//    }
//
//    public void msgInfoInput(String nick) throws IOException {
//        out.writeUTF(String.format(">>> %s подключился к чату", nick));
//    }

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
}
