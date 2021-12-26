package client;
import client.controllers.AuthController;
import client.controllers.ChatController;
import client.handler.Worker;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class Client extends Application{

    private ChatController chatController;
    private Worker worker;
    private Stage stage;
    private Stage authStage;



//    private static final String AUTH_CMD_PREFIX = "/chatauth";



    @Override
    public void start(Stage stage) throws Exception {

        this.stage = stage;
        worker = new Worker();
        worker.setClient(this);

        openAuthWindow();
        createChatWindow();
//        FXMLLoader loader = new FXMLLoader();
//        loader.setLocation(Client.class.getResource("view/chat-view.fxml"));
//        //       Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
//
//        Parent root =loader.load();
//        stage.setTitle("Massage");
//        stage.setScene(new Scene(root));
//        stage.show();
//
//
//
//        chatController = loader.getController();
//        System.out.println(chatController);
//        chatController.addOne();
//        worker.setController(chatController);
//        chatController.setWorker(worker);
//        stage.setOnCloseRequest(windowEvent ->  worker.close());



    }

    private void openAuthWindow() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Client.class.getResource("view/auth-view.fxml"));
        Parent rootAuth =loader.load();
        authStage = new Stage();
        authStage.setTitle("Авторизация");
        authStage.initModality(Modality.WINDOW_MODAL);
        authStage.initOwner(stage);
        Scene scene = new Scene(rootAuth);
        authStage.setScene(scene);
        authStage.show();

        AuthController authController = loader.getController();
        authController.setWorker(worker);
        authController.setClient(this);
        authStage.setOnCloseRequest(windowEvent -> worker.close());
    }

    public void createChatWindow() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("view/chat-view.fxml"));
//
//
        Parent root = loader.load();

//        Parent root = FXMLLoader.load(getClass().getResource("view/chat-view.fxml"));

        stage.setTitle("Messenger");
        stage.setScene(new Scene(root));

        chatController = loader.getController();
        System.out.println(chatController);
//        chatController.addOne();
        worker.setController(chatController);
        chatController.setWorker(worker);
//        chatController.setNick(worker.);

        stage.setOnCloseRequest(windowEvent -> worker.close());
    }

    public void openChatWindow(){
        authStage.close();
        stage.show();

        chatController.setNick(worker.getNick());
        stage.setAlwaysOnTop(true);
        worker.threadWaitMsg();
    }

//    public void sendMsg(String massage) throws IOException {
//        worker.sendMsg(massage);
//    }



    public static void main(String[] args) {
        launch(args);
    }
}
