package com.archosResearch.jCHEKS.gui.chat.scenario;

import mock.InvisibleViewController;
import com.archosResearch.jCHEKS.gui.chat.AppController;
import com.archosResearch.jCHEKS.gui.chat.AppControllerDefault;
import com.archosResearch.jCHEKS.gui.chat.model.Contact;
import com.archosResearch.jCHEKS.gui.chat.model.ContactCollection;
import com.archosResearch.jCHEKS.gui.chat.model.ContactCollectionDefault;
import com.archosResearch.jCHEKS.gui.chat.model.AbstractModel;
import com.archosResearch.jCHEKS.gui.chat.model.Model;
import com.archosResearch.jCHEKS.gui.chat.model.NameOfContactAlreadyExistInContactsException;
import mock.StubCommunicator;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author Michael Roussel <rousselm4@gmail.com>
 */
public class Scenario {

    @Test
    public void scenario1() throws NameOfContactAlreadyExistInContactsException {
        //Init
        ContactCollection contactCollection = new ContactCollectionDefault();
        AbstractModel model = new Model(contactCollection);
        Contact contact = new Contact("Bob", new StubCommunicator());
        model.addContact(contact);
        InvisibleViewController viewController = new InvisibleViewController();
        model.addObserver(viewController);
        AppController appController = new AppControllerDefault(model, viewController);
        viewController.setSelectedContact(contact);

        //Messages
        appController.handleIncomingMessage("Hello!", contact);
        assertEquals(viewController.lastMessageReceived.getContent(),"Hello!");
        
        viewController.forwardOutgoingMessage("Hi!");
        assertEquals(viewController.lastMessageSent.getContent(),"Hi!");
        
        appController.handleIncomingMessage("How are you?", contact);
        assertEquals(viewController.lastMessageReceived.getContent(),"How are you?");
        
        viewController.forwardOutgoingMessage("Good, and you?");
        assertEquals(viewController.lastMessageSent.getContent(),"Good, and you?");
        
        appController.handleIncomingMessage("Fine. Goodbye!", contact);
        assertEquals(viewController.lastMessageReceived.getContent(),"Fine. Goodbye!");
        
        viewController.forwardOutgoingMessage("Bye!");
        assertEquals(viewController.lastMessageSent.getContent(),"Bye!");
    }

}
