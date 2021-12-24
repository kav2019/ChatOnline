package chat;

import chat.auth.Auth;
import chat.handler.ClientHandler;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class MyServer {
    private static final int SERVER_SOKET = 8089;
    private ServerSocket serverSocket;
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    private ClientHandler clientHandler;
    private Auth auth;

    public MyServer() {
        initialization();


    }

    private void initialization() {
        try {
            serverSocket = new ServerSocket(SERVER_SOKET);
            System.out.println("Ожидаем клиента...");
            socket = serverSocket.accept();
            auth = new Auth();
            clientHandler = new ClientHandler(this, socket);
            clientHandler.handle();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Auth getAuth() {
        return auth;
    }


}
