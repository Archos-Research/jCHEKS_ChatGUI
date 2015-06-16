package com.archosResearch.jCHEKS.gui.chat.view;

import com.archosResearch.jCHEKS.concept.engine.message.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.*;
import javafx.fxml.*;
import javafx.geometry.Insets;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;

/**
 *
 * @author Michael Roussel <rousselm4@gmail.com>
 */
public class ChatViewHandler {
    
    private final JavaFxViewController mainController;
    
    HashMap<String, Tab> tabsMap = new HashMap();
    
    @FXML
    private MenuItem addContactButton;
    
    @FXML
    private TabPane mainTabPane;
      
    @FXML
    private MenuBar menuBar;
    
    @FXML
    private TextField receivingPortField;
    
    @FXML
    private Label portLabel;

    public ChatViewHandler(){
        this.mainController = JavaFxViewController.getInstance();
    }
   
    @FXML
    private void handleAboutButton() {
        try {
            Pane aboutLayout = (Pane) this.mainController.loadFxml("About.fxml");
            aboutLayout.setPadding(new Insets(15));
            this.mainController.addPopup(new Scene(aboutLayout, 300, 200), "About");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    private void handlePortConfig(KeyEvent event){
        if (event.getCode() == KeyCode.ENTER) {
            portLabel.setText("Receiving port: " + receivingPortField.getText());
            receivingPortField.setVisible(false);
            this.menuBar.setDisable(false);
            //TODO Add verification for the value if numeric
            this.mainController.setReceivingPort(Integer.parseInt(receivingPortField.getText()));
        }
    }
    
    @FXML
    private void handleNewContactButton() {
        try {
            Pane aboutLayout = (Pane) this.mainController.loadFxml("ContactForm.fxml");
            aboutLayout.setPadding(new Insets(15));
            this.mainController.addPopup(new Scene(aboutLayout, 300, 200), "Create new contact");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private ChatTab getTabByName(String contactName) throws Exception {
        //TODO Remove for() and use HashMap
        for(Tab tab : mainTabPane.getTabs()){
            if(tab.getText().equals(contactName)){
                return (ChatTab)tab;
            }
        }
        //TODO Create an exception for that.
        throw new Exception();
    }

    void displayOutgoingMessage(OutgoingMessage message) {
        try {
            getSelectedTab().displayMessage(message);
        } catch (Exception ex) {
            //TODO Change this and rethrow an exception.
            Logger.getLogger(ChatViewHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    void displayIncomingMessage(IncomingMessage message, String contactName) {
        try {
            getTabByName(contactName).displayMessage(message);
        } catch (Exception ex) {
            //TODO Change this and rethrow an exception.
            Logger.getLogger(ChatViewHandler.class.getName()).log(Level.SEVERE, null, ex);
        }    
    }

    
    void addPaneForContact(String contactName){
        this.mainTabPane.getTabs().add(new ChatTab(contactName));
    }
    
    private ChatTab getSelectedTab() throws Exception{
        //TODO use a currentTab variable.
        for(Tab tab : mainTabPane.getTabs()){
            if(tab.isSelected()){
                return (ChatTab)tab;
            }
        }
        //TODO Create an exception for that.
        throw new Exception();
    }
    
    public void refreshMessage() {
        try {
            this.getSelectedTab().refresh();
        } catch (Exception ex) {
            Logger.getLogger(ChatViewHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @FXML
    private void test_createTab() {
       addPaneForContact("Alice");
    }
    
    @FXML
    private void test_createTabGroups() {
       addPaneForContact("Alice");addPaneForContact("Bob");addPaneForContact("Tom");addPaneForContact("Pit");addPaneForContact("John");addPaneForContact("Nico");addPaneForContact("Ben");addPaneForContact("Nick");
    }
    
    @FXML
    private void test_deleteAllTab() throws Exception {
       mainTabPane.getTabs().clear();
    }
    
    @FXML
    private void test_invertSelTab(){
        try {
            Tab selectedTab = getSelectedTab();
            String txt = selectedTab.getText();
            selectedTab.setText(new StringBuilder(txt).reverse().toString());
        } catch (Exception ex) {}
    }
    
    @FXML
    private void test_write() {
        try {
            ChatTab tab = (ChatTab)getSelectedTab();
            tab.displayMessage(new IncomingMessage("This is a test message"));
            tab.displayMessage(new OutgoingMessage("This is a test message"));
        } catch (Exception ex) {}
    }
}
