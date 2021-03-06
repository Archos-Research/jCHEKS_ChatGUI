package com.archosResearch.jCHEKS.gui.chat.view;

import com.archosResearch.jCHEKS.concept.engine.message.AbstractMessage;
import javafx.application.Platform;
import javafx.collections.*;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;

/**
 *
 * @author Michael Roussel <rousselm4@gmail.com>
 */
public class ChatTab extends Tab {

    private final TextField inputField;
    private final ListView<AbstractMessage> messagesListView;
    private final Button sendButton;
    private AbstractMessage lastMessageSent;
    private final ListView<String> logListView;
    
    public ChatTab(String contactName) {
        super(contactName);
        this.messagesListView = createOutputNode();
        this.inputField = createInputNode();
        this.sendButton = createSendButton();
        this.logListView = new ListView();
        this.setContent(this.createChatContainer());
        this.setClosable(true);
    }
    public void handleMessage(AbstractMessage message) {
        Platform.runLater(() -> this.displayMessage(message));
    }

    private void displayMessage(AbstractMessage message) {
        this.messagesListView.getItems().add(message);
        this.messagesListView.scrollTo(message);
        if(message.getState() != AbstractMessage.State.FOR_ME){
            this.lastMessageSent = message;
        }
    }

    private void updateInputField() {
        lock();
        inputField.setText("");
    }

    public void refresh() {
        ObservableList<AbstractMessage> items = this.messagesListView.getItems();
        this.messagesListView.setItems(null);
        this.messagesListView.setItems(items);
        if(this.lastMessageSent.getState() == AbstractMessage.State.OK){
            unlock();
        }
        
    }
    
    void log(String logMessage){
        Platform.runLater(() -> this.logListView.getItems().add(logMessage));
    }
    
    private void lock(){
        inputField.setDisable(true);
        sendButton.setDisable(true);
    }
    
    private void unlock(){
        inputField.setDisable(false);
        sendButton.setDisable(false);
    }
    
    private ListView<AbstractMessage> createOutputNode() {
        ListView<AbstractMessage> outputNode = new ListView();
        outputNode.setItems(FXCollections.<AbstractMessage>observableArrayList());
        outputNode.setCellFactory((ListView<AbstractMessage> param) -> new MessageListCell());
        return outputNode;
    }

    private TextField createInputNode() {
        TextField inputNode = new TextField();
        inputNode.setMinWidth(500);
        inputNode.setOnKeyPressed((KeyEvent event) -> {
            if (event.getCode() == KeyCode.ENTER) {
                JavaFxViewController.getInstance().forwardOutgoingMessage(inputField.getText(), this.getText());
                updateInputField();
            }
        });
        return inputNode;
    }
    
    private Button createSendButton() {
        Button newSendButton = new Button();
        newSendButton.setText("Send");
        newSendButton.setMinWidth(100);
        newSendButton.setOnAction((ActionEvent e) -> {
            JavaFxViewController.getInstance().forwardOutgoingMessage(inputField.getText(), this.getText());
            updateInputField();
        });
        return newSendButton;
    }

    private Node createChatContainer() {
        VBox chatContainer = new VBox();
        HBox inputContainer = new HBox();
        chatContainer.getChildren().add(this.messagesListView);
        chatContainer.getChildren().add(this.logListView);
        inputContainer.getChildren().add(this.inputField);
        inputContainer.getChildren().add(new Separator());
        inputContainer.getChildren().add(this.sendButton);
        chatContainer.getChildren().add(inputContainer);
        return chatContainer;
    }
    
    private static class MessageListCell extends ListCell<AbstractMessage> {

        @Override
        public void updateItem(AbstractMessage message, boolean empty) {
            super.updateItem(message, empty);
            if (empty) {
                setGraphic(null);
                setText(null);
            } else {
                String src;
                switch (message.getState()) {
                    case FOR_ME:
                        src = "123.png";
                        break;
                    case WAITING_FOR_ACK:
                        src = "129.png";
                        break;
                    case WAITING_FOR_SECURE_ACK:
                        src = "128.png";
                        break;
                    case OK:
                        src = "121.png";
                        break;
                    case FAILED:
                        src = "127.png";
                        break;
                    default:
                        src = "127.png";
                }
                Image fxImage = new Image(JavaFxViewController.class.getResourceAsStream("res/img/" + src));
                ImageView imageView = new ImageView(fxImage);
                setGraphic(imageView);
                setText(message.getContent());
            }
        }
    }
}
