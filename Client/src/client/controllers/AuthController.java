package client.controllers;

import client.Client;
import client.handler.Worker;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;

public class AuthController {

    private Worker worker;
    private Client client;

    @FXML
    TextField loginField;

    @FXML
    PasswordField passwordField;
    @FXML
    Button enterBotton;

    @FXML
    public void enter(){
        String login = loginField.getText();
        String password = passwordField.getText();
        if(!login.isBlank() || !password.isBlank()){
            worker.setLogin(login);
            worker.setPassword(password);
            try {
                worker.isAuth();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public void setWorker(Worker worker) {
        this.worker = worker;
    }

    public void setClient(Client client) {
        this.client = client;
    }
}
