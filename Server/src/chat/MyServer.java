package chat;

import chat.handler.ClientHandler;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class MyServer {
    private static final int SERVER_SOKET = 8081;
    ServerSocket serverSocket;
    Socket socket;
    DataInputStream in;
    DataOutputStream out;
    ClientHandler clientHandler;

    public MyServer() {
        initialization();


    }

    private void initialization() {
        try {
            serverSocket = new ServerSocket(SERVER_SOKET);
            socket = serverSocket.accept();
            clientHandler = new ClientHandler(this, socket);
            clientHandler.handle();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
