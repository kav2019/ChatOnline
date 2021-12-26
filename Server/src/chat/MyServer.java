package chat;

import chat.auth.Auth;
import chat.handler.ClientHandler;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
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


}
