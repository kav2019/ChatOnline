package client.controllers;

import client.Client;
import client.handler.Worker;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;

public class ChatController {

    @FXML
    private ListView listClients;
    @FXML
    private Label labelNick;
    @FXML
    private TextArea chatArea;
    @FXML
    private TextField imputField;
    @FXML
    private Button buttonSend;
    private static final String ALL_MSG_PREFIX = "/all";
    private Client client;
    private Worker worker;

    public void setWorker(Worker worker) {
        this.worker = worker;
    }

    public void setClient(Client client) {
        this.client = client;
    }



    private ObservableList<String> clients = FXCollections.observableArrayList(
            "user1",
            "user2",
            "user2"
    );


    @FXML
    public void addOne(){
        chatArea.appendText("Mda");
        System.out.println("Mda");
    }


    @FXML
    public void initialize(){
        clientToList();
    }

    @FXML
    public void sendBotoon(){
        String msg = imputField.getText();
        if (msg.isBlank()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setHeaderText("Ошибка ввода");
            alert.setHeaderText("Нельзя отправить пустое сообщение");
            alert.showAndWait();
        }
        else {
            imputField.clear();
            addMsgToChat("Я" ,msg);
            try {
                sendMsg(msg);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void clientToList(){
        listClients.setItems(clients);
    }

    public void sendMsg(String msg) throws IOException {
        //        client.sendMsg((String.format("%s %s %s", ALL_MSG_PREFIX , name, msg)));
        worker.sendMsg(msg);
    }

    public void addMsgToChat(String name, String msg){
        chatArea.appendText((String.format("%s: %s", name, msg)) + "\n");


    }



}
