package chat.auth;

public interface Authification {
    void start();
    String getNick(String login, String  password);
    void close();

}
