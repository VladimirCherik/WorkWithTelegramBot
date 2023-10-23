package com.javacore5.feature.fsm;

public interface StateMachineListener {
    void onMessageAndTimeReceived(String message, int time);
}
