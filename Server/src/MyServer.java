import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class MyServer {
    private static final int SERVER_SOKET = 8081;
    ServerSocket serverSocket;
    Socket socket;
    DataInputStream in;
    DataOutputStream out;

    public MyServer() {
        initialization();

    }

    private void initialization() {
        try {
            serverSocket = new ServerSocket(SERVER_SOKET);
            socket = serverSocket.accept();
            threadInMsg();
            threadOutMsg();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void threadOutMsg() {
        Thread thread = new Thread(() -> {
            Scanner scanner = new Scanner(System.in);
            while (true) {
                String msg = scanner.nextLine();
                try {
                    out.writeUTF(msg);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    private void threadInMsg() {
        Thread thread = new Thread(() -> {
            try {
                String msg = in.readUTF();
                System.out.println("Сообщение от клиента: " + msg);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        thread.start();
    }
}
