package com.archosResearch.jCHEKS.gui.chat.model;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Michael Roussel <rousselm4@gmail.com>
 */
public abstract class ModelObservableDefault implements ModelObservable{
    private final List<ModelObserver> observers;
    
    public ModelObservableDefault(ArrayList observers){
        this.observers  = observers;
    }
    public void addObserver(ModelObserver observer){
        this.observers.add(observer);
    }
    
    public void removeObserver(ModelObserver observer){
        this.observers.remove(observer);
    }
    
    public void notifyNewMessageSent(Contact contact, Message message){
        for (ModelObserver observer : observers) {
            observer.messageSent(message);
        }
    }
    
    public void notifyNewMessageReceived(Message message){
        for (ModelObserver observer : observers) {
            observer.messageReceived(message);
        }
    }
    
}