package chat.auth;

import chat.User;

import java.util.List;

public class Auth implements Authification{

    private List<User> usersList = List.of(new User("user1", "user1", "1234"),
            new User("user2", "user2", "2345"),
            new User("user3", "user3", "3456")
    );

    public Auth(){
    }

    @Override
    public void start() {
        System.out.println("запускаем процесс аутентификации");
        System.out.println("запускаем процесс аутентификации");
    }

    @Override
    public String getNick(String login, String password) {
        for (User user: usersList) {
            if(user.getLogin().equals(login) && user.getPassword().equals(password)){
                return user.getNickName();
            }
        }
        return null;
    }

    @Override
    public void close() {
        System.out.println("останавливаем процесс аутентификации");
    }
}
