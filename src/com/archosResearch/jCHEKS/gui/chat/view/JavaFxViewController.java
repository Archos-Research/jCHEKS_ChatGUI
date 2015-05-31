package com.archosResearch.jCHEKS.gui.chat.view;

import com.archosResearch.jCHEKS.gui.chat.AppController;
import com.archosResearch.jCHEKS.gui.chat.model.Contact;
import com.archosResearch.jCHEKS.gui.chat.model.Message;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 *
 * @author Michael Roussel <rousselm4@gmail.com>
 */
public class JavaFxViewController extends Application implements ViewController{
    private static final CountDownLatch latch = new CountDownLatch(1);
    private static JavaFxViewController instance = null;
    private AppController appController;
    private Stage primaryStage;
    private BorderPane rootLayout;
    private ChatController chatController;
    
    public JavaFxViewController(){
        setInstance(this);
    }
    
    private static void setInstance(JavaFxViewController instance){
        JavaFxViewController.instance = instance;
        latch.countDown();
    }
    
    public static JavaFxViewController getInstance(){
        if(instance == null){
            try{
                javafx.application.Application.launch(JavaFxViewController.class);
                latch.await();
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }
        return instance;
    }
    
    @Override
    public void messageSent(Message message) {
        chatController.displayMessage(message);
        appController.handleOutgoingMessage(message);
    }

    @Override
    public void messageReceived(Message message) {
        chatController.displayMessage(message);    
    }
    
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Chat");
        initRootLayout();
    }
    
    private void initRootLayout() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(ChatController.class.getResource("chat.fxml"));
            rootLayout = (BorderPane) loader.load();
             this.chatController = (ChatController) loader.getController();
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addContact(Contact contact) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void setAppController(AppController appController) {
        this.appController = appController;
    }
}