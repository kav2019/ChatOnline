package chat;

import java.util.List;

public class User {
    private String nickName;
    private String login;
    private String password;


    public User(String nickName, String login, String password) {
        this.nickName = nickName;
        this.login = login;
        this.password = password;
    }


    public String getNickName() {

        return nickName;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }


}
