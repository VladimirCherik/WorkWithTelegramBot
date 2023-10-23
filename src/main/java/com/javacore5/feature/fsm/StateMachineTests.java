package com.javacore5.feature.fsm;

import java.util.Scanner;

public class StateMachineTests {
    public static void main(String[] args) {
        StateMachine fsm = new StateMachine();

        fsm.addListener((message, time) -> {
            System.out.println("Listener called");
            System.out.println("Message - " + message);
            System.out.println("Text: text");

        });

        Scanner scanner = new Scanner(System.in);
        while (true){
            String text = scanner.nextLine();
            fsm.handle(text);
        }


    }


}
