package com.javacore5.feature.fsm;

import java.util.ArrayList;
import java.util.List;

public class StateMachine {
    private State state = State.idle;
    private List<StateMachineListener> listeners = new ArrayList<>();
    private String message;
    private int time;
    public void addListener(StateMachineListener listener){
        listeners.add(listener);
    }
    public void handle(String text){
        if ( text.equals("Cоздать напоминание")){
            onCreateNotificationPressed();
            return;
        }
        onTextReceived(text);

        try {
            int number = Integer.parseInt(text);
            onNumberReceived(number);
        } catch (Exception exception){
        }

    }
    public void onCreateNotificationPressed(){
        if(state == State.idle){
            switchState(State.waitForMessage);
        }
        System.out.println("onCreateNotificationPressed");

    }
    private void onTextReceived(String text){
        if (state == State.waitForMessage){
            message = text;
            switchState(State.waitForTime);
        }
        System.out.println("onTextReceived - " + text);

    }
    private void onNumberReceived(int number){
        if (state == State.waitForTime){
            this.time = number;
            switchState(State.idle);

            for (StateMachineListener listener : listeners){
                listener.onMessageAndTimeReceived(message, time);
            }

        }
        System.out.println("Time to create notification - " + number);

    }
    private void switchState(State newState){

        System.out.println("Switch state " + this.state + " => " + newState);
        this.state = newState;
    }

}
