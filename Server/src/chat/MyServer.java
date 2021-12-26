package chat;

import chat.auth.Auth;
import chat.handler.ClientHandler;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MyServer {
    private static final int SERVER_SOKET = 8089;
    private ServerSocket serverSocket;
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    private ClientHandler clientHandler;
    private Auth auth;
    private List<ClientHandler> clients = new ArrayList<>();

    public MyServer() {
        initialization();


    }

    private void initialization() {
        try {
            serverSocket = new ServerSocket(SERVER_SOKET);
            System.out.println("Ожидаем клиента...");
            while (true){
                connection();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void connection() throws IOException {
        socket = serverSocket.accept();
        auth = new Auth();
        clientHandler = new ClientHandler(this, socket);
        clientHandler.handle();
    }

    public Auth getAuth() {
        return auth;
    }

    public void addClientsList(ClientHandler client){
        clients.add(client);
    }

    public void broadcastMsg(String[] msg, ClientHandler sender){
        System.out.println(Arrays.toString(msg));
        for(ClientHandler client : clients){
            if(client == sender){
                continue;
            }
            try {
                client.sendMsg(msg);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }


    // кто кому что
    public void broadcastPrivateMsg(String[] msg, ClientHandler sender){
        System.out.println("MyServer - broadcastPrivate" +Arrays.toString(msg));
        System.out.println("\n\n " + "Клиенты которые подключены: " + clients.toString() + "\n\n");
        for(ClientHandler poluchatel : clients){
            String nameGeter ="/" + poluchatel.getNick();
            System.out.println(nameGeter + " прибавление к нику - /");
            if(nameGeter.equals(msg[2])){
                try {
                    System.out.println("Нашли нужного клиента - " + nameGeter);
                    poluchatel.sendMsg(msg);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}
