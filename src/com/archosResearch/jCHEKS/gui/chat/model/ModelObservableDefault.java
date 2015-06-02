package com.archosResearch.jCHEKS.gui.chat.model;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Michael Roussel <rousselm4@gmail.com>
 */
public abstract class ModelObservableDefault implements ModelObservable{
    private final List<ModelObserver> observers;
    
    public ModelObservableDefault(){
        this.observers  = new ArrayList();
    }
    public void addObserver(ModelObserver observer){
        this.observers.add(observer);
    }
    
    public void removeObserver(ModelObserver observer){
        this.observers.remove(observer);
    }
    
    public void broadcastMessageSent(Message message){
        for (ModelObserver observer : this.observers) {
            observer.messageSent(message);
        }
    }
    
    public void broadcastMessageReceived(Message message){
        for (ModelObserver observer : this.observers) {
            observer.messageReceived(message);
        }
    }
    
}